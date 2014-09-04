package pl.baczkowicz.mqttspy.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.baczkowicz.mqttspy.configuration.ConfigurationManager;
import pl.baczkowicz.mqttspy.configuration.ConfigurationUtils;
import pl.baczkowicz.mqttspy.configuration.ConfiguredConnectionDetails;
import pl.baczkowicz.mqttspy.configuration.generated.ConnectionDetails;
import pl.baczkowicz.mqttspy.configuration.generated.ConversionMethod;
import pl.baczkowicz.mqttspy.configuration.generated.FormatterDetails;
import pl.baczkowicz.mqttspy.configuration.generated.PublicationDetails;
import pl.baczkowicz.mqttspy.configuration.generated.SubscriptionDetails;
import pl.baczkowicz.mqttspy.configuration.generated.UserAuthentication;
import pl.baczkowicz.mqttspy.connectivity.MqttConnection;
import pl.baczkowicz.mqttspy.connectivity.MqttManager;
import pl.baczkowicz.mqttspy.connectivity.MqttUtils;
import pl.baczkowicz.mqttspy.exceptions.ConfigurationException;
import pl.baczkowicz.mqttspy.ui.utils.ConnectionUtils;
import pl.baczkowicz.mqttspy.ui.utils.DialogUtils;
import pl.baczkowicz.mqttspy.ui.utils.FormattingUtils;
import pl.baczkowicz.mqttspy.ui.utils.KeyboardUtils;

@SuppressWarnings({"unchecked", "rawtypes"})
public class EditConnectionController extends AnchorPane implements Initializable
{
	final static Logger logger = LoggerFactory.getLogger(EditConnectionController.class);

	// Connectivity
	
	@FXML
	private TextField brokerAddressText;

	@FXML
	private TextField clientIdText;

	@FXML
	private TextField connectionNameText;

	@FXML
	private Button addTimestampButton;
	
	@FXML
	private Label lengthLabel;
	
	@FXML
	private TextField connectionTimeout;
	
	@FXML
	private TextField keepAlive;
	
	@FXML
	private CheckBox cleanSession;

	// UI & Formatting
	
	@FXML
	private CheckBox autoConnect;
	
	@FXML
	private CheckBox autoOpen;
	
	@FXML
	private TextField maxMessagesStored;
	
	@FXML
	private ComboBox<FormatterDetails> formatter;
	
	// Security
	
	@FXML
	private CheckBox userAuthentication;
	
	@FXML
	private TextField username;
	
	@FXML
	private RadioButton predefinedUsername;
	
	@FXML
	private RadioButton askForUsername;
	
	@FXML
	private RadioButton askForPassword;
	
	@FXML
	private RadioButton predefinedPassword;
	
	@FXML
	private PasswordField password;
	
	// Action buttons
	
	@FXML
	private Button connectButton;
	
	@FXML
	private Button cancelButton;
	
	@FXML
	private Button saveButton;
	
	@FXML
	private Button undoButton;
	
	@FXML
	private Button removePublicationButton;
	
	@FXML
	private Button removeSubscriptionButton;
	
	// Tables
	
	@FXML
	private TableView<PublicationDetails> publicationsTable;
	
	@FXML
	private TableView<SubscriptionDetails> subscriptionsTable;
	
	@FXML
	private TableColumn<PublicationDetails, String> publicationTopicColumn;
	
	@FXML
	private TableColumn<SubscriptionDetails, String> subscriptionTopicColumn;
	
	@FXML
	private TableColumn<SubscriptionDetails, Integer> qosSubscriptionColumn;
	
	@FXML
	private TableColumn<SubscriptionDetails, Boolean> createTabSubscriptionColumn;
	
	// Other fields

	private String lastGeneratedConnectionName = "";

	private MqttManager mqttManager;

	private MainController mainController;

	private ConfiguredConnectionDetails editedConnectionDetails;

	private boolean recordModifications;
    
	private ConfigurationManager configurationManager;

	private EditConnectionsController editConnectionsController;

	private final ChangeListener basicOnChangeListener = new ChangeListener()
	{
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue)
		{
			onChange();			
		}		
	};

	private boolean openNewMode;

	private MqttConnection existingConnection;
	
	// ===============================
	// === Initialisation ============
	// ===============================


	public void initialize(URL location, ResourceBundle resources)
	{
		connectionNameText.textProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue)
			{
				onChange();
			}		
		});
		
		brokerAddressText.textProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue)
			{
				updateConnectionName();
				
				onChange();
			}		
		});
		
		clientIdText.textProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue)
			{
				updateConnectionName();
				
				onChange();
			}		
		});
		
		cleanSession.selectedProperty().addListener(basicOnChangeListener);
		
		connectionTimeout.textProperty().addListener(basicOnChangeListener);
		connectionTimeout.addEventFilter(KeyEvent.KEY_TYPED, KeyboardUtils.nonNumericKeyConsumer);
		keepAlive.textProperty().addListener(basicOnChangeListener);
		keepAlive.addEventFilter(KeyEvent.KEY_TYPED, KeyboardUtils.nonNumericKeyConsumer);
		
		// Security
		userAuthentication.selectedProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue)
			{
				updateUserAuthentication();
				
				onChange();
			}		
		});
		username.textProperty().addListener(basicOnChangeListener);
		password.textProperty().addListener(basicOnChangeListener);
		askForUsername.selectedProperty().addListener(basicOnChangeListener);
		askForPassword.selectedProperty().addListener(basicOnChangeListener);
		predefinedUsername.selectedProperty().addListener(basicOnChangeListener);
		predefinedPassword.selectedProperty().addListener(basicOnChangeListener);
				
		// UI
		autoConnect.selectedProperty().addListener(basicOnChangeListener);
		autoOpen.selectedProperty().addListener(basicOnChangeListener);
		
		maxMessagesStored.textProperty().addListener(basicOnChangeListener);
		maxMessagesStored.addEventFilter(KeyEvent.KEY_TYPED, KeyboardUtils.nonNumericKeyConsumer);
		
		formatter.getSelectionModel().selectedIndexProperty().addListener(basicOnChangeListener);
		formatter.setCellFactory(new Callback<ListView<FormatterDetails>, ListCell<FormatterDetails>>()
				{
					@Override
					public ListCell<FormatterDetails> call(ListView<FormatterDetails> l)
					{
						return new ListCell<FormatterDetails>()
						{
							@Override
							protected void updateItem(FormatterDetails item, boolean empty)
							{
								super.updateItem(item, empty);
								if (item == null || empty)
								{
									setText(null);
								}
								else
								{									
									setText(item.getName());
								}
							}
						};
					}
				});
		formatter.setConverter(new StringConverter<FormatterDetails>()
		{
			@Override
			public String toString(FormatterDetails item)
			{
				if (item == null)
				{
					return null;
				}
				else
				{
					return item.getName();
				}
			}

			@Override
			public FormatterDetails fromString(String id)
			{
				return null;
			}
		});
		
		// Publications
		publicationTopicColumn.setCellValueFactory(new PropertyValueFactory<PublicationDetails, String>("topic"));
		//publicationTopicColumn.setCellFactory(TextFieldTableCell.<PublicationDetails>forTableColumn());
		
		// Subscriptions
		createTabSubscriptionColumn.setCellValueFactory(new PropertyValueFactory<SubscriptionDetails, Boolean>("createTab"));
		// createTabSubscriptionColumn.setCellFactory(CheckBoxTableCell.<SubscriptionDetails>forTableColumn(null));
		
		subscriptionTopicColumn.setCellValueFactory(new PropertyValueFactory<SubscriptionDetails, String>("topic"));
		// subscriptionTopicColumn.setCellFactory(TextFieldTableCell.<SubscriptionDetails>forTableColumn());
		
		qosSubscriptionColumn.setCellValueFactory(new PropertyValueFactory<SubscriptionDetails, Integer>("qos"));
		// qosSubscriptionColumn.setCellFactory(NumberFieldTableCell.<SubscriptionDetails>forTableColumn());
	}	
	
	public void init()
	{
		formatter.getItems().clear();		
		formatter.getItems().add(FormattingUtils.createBasicFormatter("default", 				"Plain", ConversionMethod.PLAIN));
		formatter.getItems().add(FormattingUtils.createBasicFormatter("default-hexDecoder", 	"HEX decoder", ConversionMethod.HEX_DECODE));
		formatter.getItems().add(FormattingUtils.createBasicFormatter("default-hexEncoder", 	"HEX encoder", ConversionMethod.HEX_ENCODE));
		formatter.getItems().add(FormattingUtils.createBasicFormatter("default-base64Decoder", 	"Base64 decoder", ConversionMethod.BASE_64_DECODE));
		formatter.getItems().add(FormattingUtils.createBasicFormatter("default-base64Encoder", 	"Base64 encoder", ConversionMethod.BASE_64_ENCODE));		

		// Populate those from the configuration file
		for (final FormatterDetails formatterDetails : configurationManager.getConfiguration().getFormatting().getFormatter())
		{			
			// Make sure the element we're trying to add is not on the list already
			boolean found = false;
			
			for (final FormatterDetails existingFormatterDetails : formatter.getItems())
			{
				if (existingFormatterDetails.getID().equals(formatterDetails.getID()))
				{
					found = true;
					break;
				}
			}
			
			if (!found)
			{
				formatter.getItems().add(formatterDetails);
			}
		}	
	}

	// ===============================
	// === FXML ======================
	// ===============================

	@FXML
	private void addTimestamp()
	{
		updateClientId(true);
	}
	
	@FXML
	private void undo()
	{
		editedConnectionDetails.undo();
		editConnectionsController.listConnections();
		
		editConnection(editedConnectionDetails);
		
		updateButtons();
	}
	
	@FXML
	private void save()
	{
		editedConnectionDetails.apply();
		editConnectionsController.listConnections();
		
		updateButtons();
		
		logger.debug("Saving connection " + connectionNameText.getText());
		configurationManager.saveConfiguration();
	}	
	
	private boolean updateClientId(final boolean addTimestamp)
	{
		String clientId = clientIdText.getText();
		String newClientId = clientId;
		
		if (clientId.length() > MqttUtils.MAX_CLIENT_LENGTH)
		{
			newClientId = clientId.substring(0, MqttUtils.MAX_CLIENT_LENGTH);
		}
		
		if (addTimestamp)
		{
			newClientId = MqttUtils.generateClientIdWithTimestamp(newClientId);
		}
		
		if (!clientId.equals(newClientId))
		{
			final int currentCurrentPosition = clientIdText.getCaretPosition();
			clientIdText.setText(newClientId);
			clientIdText.positionCaret(currentCurrentPosition);
			return true;
		}
		
		return false;
	}

	private void updateClientIdLength()
	{					
		lengthLabel.setText("Length = " + clientIdText.getText().length() + "/" + MqttUtils.MAX_CLIENT_LENGTH);
	}
	
	@FXML
	public void removePublication()
	{
		final PublicationDetails item = publicationsTable.getSelectionModel().getSelectedItem(); 
		if (item != null)
		{
			publicationsTable.getItems().remove(item);
			onChange();
		}
	}
	
	@FXML
	public void removeSubscription()
	{
		final SubscriptionDetails item = subscriptionsTable.getSelectionModel().getSelectedItem(); 
		if (item != null)
		{
			subscriptionsTable.getItems().remove(item);
			onChange();
		}
	}

	private void updateConnectionName()
	{
		if (connectionNameText.getText().isEmpty()
				|| lastGeneratedConnectionName.equals(connectionNameText.getText()))
		{
			final String newName = composeConnectionName(clientIdText.getText(), brokerAddressText.getText());
			connectionNameText.setText(newName);
			lastGeneratedConnectionName = newName;
		}
	}
	
	@FXML
	private void cancel()
	{
		// Get a handle to the stage
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		
		// Close the window
		stage.close();
	}
	
	@FXML
	public void createConnection() throws ConfigurationException
	{
		readAndDetectChanges();
		final String validationResult = MqttUtils.validateConnectionDetails(editedConnectionDetails, false);
		
		if (validationResult != null)
		{
			DialogUtils.showValidationWarning(validationResult);
		}
		else
		{					
			if (editedConnectionDetails.isModified())
			{	
				Action response = DialogUtils.showApplyChangesQuestion("connection " + editedConnectionDetails.getName()); 
				if (response == Dialog.Actions.YES)
				{
					save();
				}
				else if (response == Dialog.Actions.NO)
				{
					// Do nothing
				}
				else
				{
					return;
				}
			}
			
			if (!openNewMode)
			{
				ConnectionUtils.disconnectAndClose(mqttManager, existingConnection.getProperties().getId(), existingConnection.getConnectionTab());
			}
			
			logger.info("Opening connection " + connectionNameText.getText());
	
			// Get a handle to the stage
			Stage stage = (Stage) connectButton.getScene().getWindow();
	
			// Close the window
			stage.close();
	 
			// mainController.openConnection(editedConnection.isModified() ? readValues() : editedConnection, mqttManager);
			Platform.runLater(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{						
						mainController.openConnection(editedConnectionDetails, mqttManager);
					}
					catch (ConfigurationException e)
					{
						// TODO: show warning dialog for invalid
						logger.error("Cannot open conection {}", editedConnectionDetails.getName(), e);
					}					
				}				
			});
			
		}
	}

	// ===============================
	// === Logic =====================
	// ===============================

	private void onChange()
	{
		if (recordModifications)
		{					
			if (readAndDetectChanges())
			{
				updateButtons();
				updateClientId(false);
				updateClientIdLength();
				updateConnectionName();								
				updateUserAuthentication();
				editConnectionsController.listConnections();
			}
		}				
	}

	private ConnectionDetails readValues()
	{
		final ConnectionDetails connection = new ConnectionDetails();
		
		connection.setName(connectionNameText.getText());
		connection.setServerURI(brokerAddressText.getText());
		connection.setClientID(clientIdText.getText());
		
		connection.setCleanSession(cleanSession.isSelected());
		connection.setConnectionTimeout(Integer.valueOf(connectionTimeout.getText()));
		connection.setKeepAliveInterval(Integer.valueOf(keepAlive.getText()));
		
		connection.setAutoConnect(autoConnect.isSelected());
		connection.setAutoOpen(autoOpen.isSelected());
		connection.setFormatter(formatter.getSelectionModel().getSelectedItem());
		connection.setMaxMessagesStored(Integer.valueOf(maxMessagesStored.getText()));
		
		if (userAuthentication.isSelected())
		{
			final UserAuthentication userAuthentication = new UserAuthentication();
			
			userAuthentication.setUsername(username.getText());
			userAuthentication.setPassword(MqttUtils.encodePassword(password.getText()));
			
			userAuthentication.setAskForUsername(askForUsername.isSelected());
			userAuthentication.setAskForPassword(askForPassword.isSelected());
			
			connection.setUserAuthentication(userAuthentication);
		}
		
		for (final PublicationDetails publicationDetails : publicationsTable.getItems())
		{
			final PublicationDetails newPublicationDetails = new PublicationDetails();
			((PublicationDetails) publicationDetails).copyTo(newPublicationDetails);
			connection.getPublication().add(newPublicationDetails);
		}
		
		for (final SubscriptionDetails subscriptionDetails : subscriptionsTable.getItems())
		{
			final SubscriptionDetails newSubscriptionDetails = new SubscriptionDetails();
			((SubscriptionDetails) subscriptionDetails).copyTo(newSubscriptionDetails);
			connection.getSubscription().add(newSubscriptionDetails);
		}		
		
		return connection;
	}
	
	private boolean readAndDetectChanges()
	{
		final ConnectionDetails connection = readValues();
		boolean changed = !connection.equals(editedConnectionDetails.getSavedValues());
			
		logger.info("Values read. Changed = " + changed);
		editedConnectionDetails.setModified(changed);
		editedConnectionDetails.setConnectionDetails(connection);
		
		return changed;
	}

	public void editConnection(final ConfiguredConnectionDetails connectionDetails)
	{	
		synchronized (this)
		{
			this.editedConnectionDetails = connectionDetails;
			
			// Set 'open connection' button mode
			openNewMode = true;
			existingConnection = null;
			connectButton.setText("Open connection");
			
			logger.debug("Editing connection id={} name={}", editedConnectionDetails.getId(),
					editedConnectionDetails.getName());
			for (final MqttConnection mqttConnection : mqttManager.getConnections())
			{
				if (connectionDetails.getId() == mqttConnection.getProperties().getConfiguredProperties().getId() && mqttConnection.isOpened())
				{
					openNewMode = false;
					existingConnection = mqttConnection;
					connectButton.setText("Close and re-open existing connection");
					break;
				}				
			}
			
			
			if (editedConnectionDetails.getName().equals(composeConnectionName(editedConnectionDetails.getClientID(), editedConnectionDetails.getServerURI())))
			{
				lastGeneratedConnectionName = editedConnectionDetails.getName();
			}
			else
			{
				lastGeneratedConnectionName = "";
			}
			
			displayConnectionDetails(editedConnectionDetails);		
			updateClientIdLength();
			updateConnectionName();
						
			updateButtons();
		}
	}
	
	private void updateButtons()
	{
		if (editedConnectionDetails.isModified())
		{
			saveButton.setDisable(false);
			undoButton.setDisable(false);
		}
		else
		{
			saveButton.setDisable(true);
			undoButton.setDisable(true);
		}
	}

	private void updateUserAuthentication()
	{
		if (userAuthentication.isSelected())
		{
			predefinedUsername.setDisable(false);
			predefinedPassword.setDisable(false);			
			askForUsername.setDisable(false);
			askForPassword.setDisable(false);
			
			if (askForUsername.isSelected())
			{
				username.setDisable(true);
			}
			else				
			{
				username.setDisable(false);
			}
			
			if (askForPassword.isSelected())
			{
				password.setDisable(true);
			}
			else				
			{
				password.setDisable(false);
			}
		}
		else
		{
			username.setDisable(true);			
			password.setDisable(true);
			predefinedUsername.setDisable(true);
			predefinedPassword.setDisable(true);
			askForUsername.setDisable(true);
			askForPassword.setDisable(true);
		}
	}
	
	private void displayConnectionDetails(final ConfiguredConnectionDetails connection)
	{
		ConfigurationUtils.populateConnectionDefaults(connection);
		
		// Connectivity
		connectionNameText.setText(connection.getName());			
		
		brokerAddressText.setText(connection.getServerURI());
		clientIdText.setText(connection.getClientID());
				
		connectionTimeout.setText(connection.getConnectionTimeout().toString());
		keepAlive.setText(connection.getKeepAliveInterval().toString());
		cleanSession.setSelected(connection.isCleanSession());

		// Security
		userAuthentication.setSelected(connection.getUserAuthentication() != null);
		predefinedUsername.setSelected(true);
		predefinedPassword.setSelected(true);
		
		if (userAuthentication.isSelected())
		{			
			username.setText(connection.getUserAuthentication().getUsername());			
			password.setText(MqttUtils.decodePassword(connection.getUserAuthentication().getPassword()));	
			
			askForUsername.setSelected(connection.getUserAuthentication().isAskForUsername());
			askForPassword.setSelected(connection.getUserAuthentication().isAskForPassword());
		}
		else
		{
			username.setText("");
			password.setText("");
		}
		
		updateUserAuthentication();

		// UI
		autoConnect.setSelected(connection.isAutoConnect() == null ? false : connection.isAutoConnect());
		autoOpen.setSelected(connection.isAutoOpen() == null ? false : connection.isAutoOpen());
		maxMessagesStored.setText(connection.getMaxMessagesStored().toString());
				
		if (formatter.getItems().size() > 0 && connection.getFormatter() != null)
		{
			for (final FormatterDetails item : formatter.getItems())
			{
				if (item.getID().equals(((FormatterDetails) connection.getFormatter()).getID()))
				{
					formatter.getSelectionModel().select(item);
					break;
				}
			}
		}	
		else
		{
			formatter.getSelectionModel().clearSelection();
		}
		
		// Publications
		removePublicationButton.setDisable(true);
		publicationsTable.getItems().clear();
		publicationsTable.getItems().addAll(connection.getPublication());
		publicationsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue)
			{
				removePublicationButton.setDisable(false);
			}		
		});
		
		// Subscriptions
		removeSubscriptionButton.setDisable(true);
		subscriptionsTable.getItems().clear();
		subscriptionsTable.getItems().addAll(connection.getSubscription());
		subscriptionsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue)
			{
				removeSubscriptionButton.setDisable(false);
			}		
		});
		
		connection.setBeingCreated(false);
	}		
		
	private String composeConnectionName(final String cliendId, final String serverURI)
	{
		return cliendId + "@" + serverURI;
	}

	// ===============================
	// === Setters and getters =======
	// ===============================
	
	public MqttManager getManager()
	{
		return mqttManager;
	}

	public void setManager(MqttManager manager)
	{
		this.mqttManager = manager;
	}

	public void setMainController(MainController mainController)
	{
		this.mainController = mainController;
	}

	public void setConfigurationManager(final ConfigurationManager configurationManager)
	{
		this.configurationManager = configurationManager;
	}

	public void setEditConnectionsController(EditConnectionsController editConnectionsController)
	{
		this.editConnectionsController = editConnectionsController;		
	}
	
	public void setRecordModifications(boolean recordModifications)
	{
		this.recordModifications = recordModifications;
	}
		
	public TextField getConnectionName()
	{
		return connectionNameText;
	}
}
