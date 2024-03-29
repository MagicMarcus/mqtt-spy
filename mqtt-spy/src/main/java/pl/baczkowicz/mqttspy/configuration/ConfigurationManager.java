/***********************************************************************************
 * 
 * Copyright (c) 2014 Kamil Baczkowicz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 
 *    Kamil Baczkowicz - initial API and implementation and/or initial documentation
 *    
 */
package pl.baczkowicz.mqttspy.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.baczkowicz.mqttspy.common.generated.BaseMqttMessage;
import pl.baczkowicz.mqttspy.configuration.generated.Connectivity;
import pl.baczkowicz.mqttspy.configuration.generated.FormatterDetails;
import pl.baczkowicz.mqttspy.configuration.generated.Formatting;
import pl.baczkowicz.mqttspy.configuration.generated.MqttSpyConfiguration;
import pl.baczkowicz.mqttspy.configuration.generated.TabbedSubscriptionDetails;
import pl.baczkowicz.mqttspy.configuration.generated.UserAuthenticationOptions;
import pl.baczkowicz.mqttspy.configuration.generated.UserInterfaceMqttConnectionDetails;
import pl.baczkowicz.mqttspy.configuration.generated.UserInterfaceMqttConnectionDetailsV010;
import pl.baczkowicz.mqttspy.connectivity.ConnectionIdGenerator;
import pl.baczkowicz.mqttspy.connectivity.MqttAsyncConnection;
import pl.baczkowicz.mqttspy.connectivity.MqttSubscription;
import pl.baczkowicz.mqttspy.events.EventManager;
import pl.baczkowicz.mqttspy.exceptions.XMLException;
import pl.baczkowicz.mqttspy.ui.utils.DialogUtils;
import pl.baczkowicz.mqttspy.ui.utils.MqttSpyPerspective;
import pl.baczkowicz.mqttspy.xml.XMLParser;

/**
 * Manages loading and saving configuration files.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ConfigurationManager
{
	final static Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);
	
	public static final String VERSION_INFO_URL = "application.update.url";
	
	public static final String PACKAGE = "pl.baczkowicz.mqttspy.configuration.generated";
	
	public static final String SCHEMA = "/mqtt-spy-configuration.xsd";
	
	public static final String COMMON_SCHEMA = "/mqtt-spy-common.xsd";

	public static final String DEFAULT_FILE_NAME = "mqtt-spy-configuration.xml";
	
	public static final String DEFAULT_PROPERTIES_FILE_NAME = "/mqtt-spy.properties";
	
	public static final String UI_PROPERTIES_FILE_NAME = "/mqtt-spy-ui.properties";
	
	public static final String DEFAULT_HOME_DIRECTORY = getDefaultHomeDirectory();
	
	private static final String DEFAULT_HOME_DIRECTORY_NAME = "mqtt-spy";
	
	private MqttSpyConfiguration configuration;
	
	private List<ConfiguredConnectionDetails> connections = new ArrayList<ConfiguredConnectionDetails>();	

	private File loadedConfigurationFile;

	private Exception lastException;

	private EventManager eventManager;
	
	private final XMLParser parser;
	
	private final PropertyFileLoader defaultPropertyFile;
	
	private final PropertyFileLoader uiPropertyFile;

	private ConnectionIdGenerator connectionIdGenerator;

	public ConfigurationManager(final EventManager eventManager, final ConnectionIdGenerator connectionIdGenerator) throws XMLException
	{
		// Load the default property file from classpath
		this.defaultPropertyFile = new PropertyFileLoader();
		this.defaultPropertyFile.readFromClassPath(DEFAULT_PROPERTIES_FILE_NAME);
		
		// Load the UI property file
		if (!getUiPropertiesFile().exists())
		{
			logger.info("Creating UI property file");
			ConfigurationUtils.createUiPropertyFileFromClassPath();
		}
		this.uiPropertyFile = new PropertyFileLoader();
		this.uiPropertyFile.readFromFileSystem(getUiPropertiesFile());
		
		this.parser = new XMLParser(PACKAGE, new String[] {COMMON_SCHEMA, SCHEMA});
					
		// Create empty configuration
		this.configuration = new MqttSpyConfiguration();
		this.configuration.setConnectivity(new Connectivity());
		this.configuration.setFormatting(new Formatting());		
		
		this.eventManager = eventManager;
		this.connectionIdGenerator = connectionIdGenerator;
	}
	
	public boolean loadConfiguration(final File file)
	{
		try
		{
			clear();
			configuration = (MqttSpyConfiguration) parser.loadFromFile(file);
			createConnections();
			createConfigurationDefaults();
			loadedConfigurationFile = file;
			return true;
		}
		catch (XMLException e)
		{
			setLastException(e);
			DialogUtils.showInvalidConfigurationFileDialog("Cannot process the given configuration file. See the log file for more details.");					
			logger.error("Cannot process the configuration file at " + file.getAbsolutePath(), e);
			eventManager.notifyConfigurationFileReadFailure();
		}
		catch (FileNotFoundException e)
		{
			setLastException(e);
			DialogUtils.showInvalidConfigurationFileDialog("Cannot read the given configuration file. See the log file for more details.");
			logger.error("Cannot read the configuration file from " + file.getAbsolutePath(), e);
			eventManager.notifyConfigurationFileReadFailure();
		}
		
		return false;
	}
	
	private void createConfigurationDefaults()
	{
		if (configuration.getFormatting() == null)
		{
			configuration.setFormatting(new Formatting());
		}
	}
	
	private void createConnections()
	{
		for (final Object connectionDetails : getConfiguration().getConnectivity().getConnectionOrConnectionV2())
		{
			if (connectionDetails instanceof UserInterfaceMqttConnectionDetailsV010)
			{
				final UserInterfaceMqttConnectionDetailsV010 connectionDetailsV010 = (UserInterfaceMqttConnectionDetailsV010) connectionDetails;
				
				final UserInterfaceMqttConnectionDetails details = new UserInterfaceMqttConnectionDetails();
				details.setName(connectionDetailsV010.getName());
				details.getServerURI().add(connectionDetailsV010.getServerURI());
				details.setClientID(connectionDetailsV010.getClientID());
				details.setUserCredentials(connectionDetailsV010.getUserAuthentication());
				if (connectionDetailsV010.getUserAuthentication() != null)
				{
					details.setUserAuthentication(new UserAuthenticationOptions(
							connectionDetailsV010.getUserAuthentication().isAskForUsername(), 
							connectionDetailsV010.getUserAuthentication().isAskForPassword()));
				}
				
				if (connectionDetailsV010.getLastWillAndTestament() != null)
				{
					details.setLastWillAndTestament(new BaseMqttMessage(
							connectionDetailsV010.getLastWillAndTestament().getPayload(), 
							connectionDetailsV010.getLastWillAndTestament().getTopic(), 
							connectionDetailsV010.getLastWillAndTestament().getQoS(), 
							connectionDetailsV010.getLastWillAndTestament().isRetained()));
				}
				details.setCleanSession(connectionDetailsV010.isCleanSession());
				details.setConnectionTimeout(connectionDetailsV010.getConnectionTimeout());
				details.setKeepAliveInterval(connectionDetailsV010.getKeepAliveInterval());
				
				details.setAutoOpen(connectionDetailsV010.isAutoOpen());
				details.setAutoConnect(connectionDetailsV010.isAutoConnect());
				details.setFormatter(connectionDetailsV010.getFormatter());
				details.setMinMessagesStoredPerTopic(connectionDetailsV010.getMinMessagesStoredPerTopic());
				details.setMaxMessagesStored(connectionDetailsV010.getMaxMessagesStored());
				details.setPublicationScripts(connectionDetailsV010.getPublicationScripts());
				details.getPublication().addAll(connectionDetailsV010.getPublication());
				details.getSubscription().addAll(connectionDetailsV010.getSubscription());
				
				// Put the defaults at the point of loading the config, so we don't need to do it again
				ConfigurationUtils.populateConnectionDefaults(details);
				connections.add(new ConfiguredConnectionDetails(connectionIdGenerator.getNextAvailableId(), false, false, false, details));
			}
			else
			{
				// Put the defaults at the point of loading the config, so we don't need to do it again
				ConfigurationUtils.populateConnectionDefaults((UserInterfaceMqttConnectionDetails) connectionDetails);
				connections.add(new ConfiguredConnectionDetails(connectionIdGenerator.getNextAvailableId(), false, false, false, 
						(UserInterfaceMqttConnectionDetails) connectionDetails));
			}
		}		
	}
	
	public static File getDefaultConfigurationFile()
	{			
		return new File(getDefaultHomeDirectory() + ConfigurationManager.DEFAULT_FILE_NAME);
	}
	
	public static File getUiPropertiesFile()
	{			
		return new File(getDefaultHomeDirectory() + ConfigurationManager.UI_PROPERTIES_FILE_NAME);
	}
	
	public static File getDefaultHomeDirectoryFile()
	{			
		return new File(getDefaultHomeDirectory());
	}
	
	public static String getDefaultHomeDirectory()
	{
		final String filePathSeparator = System.getProperty("file.separator");
		String userHomeDirectory = System.getProperty("user.home");
		
		if (!userHomeDirectory.endsWith(filePathSeparator))
		{
			userHomeDirectory = userHomeDirectory + filePathSeparator;
		}
		
		return userHomeDirectory + DEFAULT_HOME_DIRECTORY_NAME + filePathSeparator;
	}

//	public boolean createDefaultConfigurationFile()
//	{
//		final File dest = getDefaultConfigurationFile();
//		
//		try
//		{
//			final File orig = new File(ConfigurationManager.class.getResource("/" + ConfigurationManager.DEFAULT_FILE_NAME).toURI()); 			
//			Files.copy(orig.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
//			return true;
//		}
//		catch (IOException | URISyntaxException e)
//		{
//			setLastException(e);
//			logger.error("Cannot create the default configuration file at " + dest.getAbsolutePath(), e);
//			eventManager.notifyConfigurationFileCopyFailure();			
//		}		
//		
//		return false;
//	}	

	public boolean saveConfiguration()
	{
		if (isConfigurationWritable())
		{
			try
			{
				configuration.getConnectivity().getConnectionOrConnectionV2().clear();
				configuration.getConnectivity().getConnectionOrConnectionV2().addAll(connections);				
				populateMissingFormatters(configuration.getFormatting().getFormatter(), connections);
				
				parser.saveToFile(loadedConfigurationFile, 
						new JAXBElement(new QName("http://baczkowicz.pl/mqtt-spy-configuration", "MqttSpyConfiguration"), MqttSpyConfiguration.class, configuration));
				return true;
			}
			catch (XMLException e)
			{
				setLastException(e);
				logger.error("Cannot save the configuration file", e);
				eventManager.notifyConfigurationFileWriteFailure();
			}
		}
		
		return false;
	}
	
	private void populateMissingFormatters(final List<FormatterDetails> formatters, final List<ConfiguredConnectionDetails> connections)
	{
		for (final ConfiguredConnectionDetails connection : connections)
		{
			if (connection.getFormatter() == null)
			{
				continue;
			}
			
			boolean formatterFound = false;
			
			for (final FormatterDetails formatter : formatters)
			{
				if (((FormatterDetails) connection.getFormatter()).getID().equals(formatter.getID()))
				{
					formatterFound = true;
				}
			}
			
			if (!formatterFound)
			{
				formatters.add((FormatterDetails) connection.getFormatter());
			}
		}
	}
	
	public void clear()
	{
		connections.clear();
		configuration = null;
		loadedConfigurationFile = null;
		lastException =  null;
		connectionIdGenerator.resetLastUsedId();
	}
	
	public ConfiguredConnectionDetails getMatchingConnection(final int id)
	{
		for (final ConfiguredConnectionDetails details : getConnections())
		{
			if (details.getId() == id)
			{
				return details;
			}
		}
		
		return null;
	}
	
	public void updateSubscriptionConfiguration(final MqttAsyncConnection connection, final MqttSubscription subscription)	
	{
		final ConfiguredConnectionDetails details = getMatchingConnection(connection.getId());
		
		boolean matchFound = false;
		for (final TabbedSubscriptionDetails subscriptionDetails : details.getSubscription())
		{							
			if (subscriptionDetails.getTopic().equals(subscription.getTopic()))
			{
				subscriptionDetails.setQos(subscription.getQos());
				subscriptionDetails.setCreateTab(true);
				subscriptionDetails.setScriptFile(subscription.getDetails().getScriptFile());
				matchFound = true;
				break;
			}
		}
		
		// If no match found, add this subscription
		if (!matchFound)
		{
			final TabbedSubscriptionDetails subscriptionDetails = new TabbedSubscriptionDetails();
			subscriptionDetails.setTopic(subscription.getTopic());
			subscriptionDetails.setQos(subscription.getQos());
			subscriptionDetails.setCreateTab(true);
			subscriptionDetails.setScriptFile(subscription.getDetails().getScriptFile());
			details.getSubscription().add(subscriptionDetails);							
		}					
		
		saveConfiguration();
	}
	
	public void deleteSubscriptionConfiguration(final MqttAsyncConnection connection, final MqttSubscription subscription)	
	{
		final ConfiguredConnectionDetails details = getMatchingConnection(connection.getId());
		
		TabbedSubscriptionDetails itemToRemove = null;
		
		for (final TabbedSubscriptionDetails subscriptionDetails : details.getSubscription())
		{							
			if (subscriptionDetails.getTopic().equals(subscription.getTopic()))
			{
				itemToRemove = subscriptionDetails;
				break;
			}
		}
		
		if (itemToRemove != null)
		{
			details.getSubscription().remove(itemToRemove);
		}		
		
		saveConfiguration();
	}
	
	// ===============================
	// === Setters and getters =======
	// ===============================

	public Exception getLastException()
	{
		return lastException;
	}

	public void setLastException(Exception lastException)
	{
		this.lastException = lastException;
	}
	
	public File getLoadedConfigurationFile()
	{
		return loadedConfigurationFile;
	}
	
	public boolean isConfigurationWritable()
	{
		if (loadedConfigurationFile != null && loadedConfigurationFile.canWrite())
		{
			return true;
		}
		return false;
	}
	
	public boolean isConfigurationReadOnly()
	{
		if (loadedConfigurationFile != null && !loadedConfigurationFile.canWrite())
		{					
			return true;
		}
		
		return false;
	}
	
	public MqttSpyConfiguration getConfiguration()
	{
		return configuration;
	}
	
	public List<ConfiguredConnectionDetails> getConnections()
	{
		return connections;
	}
	

	/** 
	 * Gets the connection ID generator.
	 * 
	 * @return the connectionIdGenerator
	 */
	public ConnectionIdGenerator getConnectionIdGenerator()
	{
		return connectionIdGenerator;
	}

	/**
	 * Gets the default property file.
	 * 
	 * @return the defaultPropertyFile
	 */
	public PropertyFileLoader getDefaultPropertyFile()
	{
		return defaultPropertyFile;
	}

	/**
	 * Gets the UI property file.
	 * 
	 * @return the uiPropertyFile
	 */
	public PropertyFileLoader getUiPropertyFile()
	{
		return uiPropertyFile;
	}

	public void saveUiProperties(final double width, final double height, boolean maximized, final MqttSpyPerspective selectedPerspective)
	{
		uiPropertyFile.setProperty(ConfigurationUtils.WIDTH_PROPERTY, String.valueOf(width));
		uiPropertyFile.setProperty(ConfigurationUtils.HEIGHT_PROPERTY, String.valueOf(height));
		uiPropertyFile.setProperty(ConfigurationUtils.MAXIMIZED_PROPERTY, String.valueOf(maximized));
		uiPropertyFile.setProperty(ConfigurationUtils.PERSPECTIVE_PROPERTY, selectedPerspective.toString());
		
		try
		{
			uiPropertyFile.saveToFileSystem(getUiPropertiesFile());
		}
		catch (IOException e)
		{
			logger.error("Cannot save UI properties", e);
		}
	}
}
