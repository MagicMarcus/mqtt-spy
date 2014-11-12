package pl.baczkowicz.mqttspy.daemon.connectivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.baczkowicz.mqttspy.common.generated.SubscriptionDetails;
import pl.baczkowicz.mqttspy.connectivity.BaseMqttConnection;
import pl.baczkowicz.mqttspy.daemon.configuration.generated.DaemonMqttConnectionDetails;
import pl.baczkowicz.mqttspy.common.generated.MessageLog;
import pl.baczkowicz.mqttspy.messages.ReceivedMqttMessage;
import pl.baczkowicz.mqttspy.scripts.PublicationScriptProperties;
import pl.baczkowicz.mqttspy.scripts.ScriptManager;

/**
 * One MQTT callback handler per connection.
 * 
 * @author Kamil Baczkowicz
 *
 */
public class MqttCallbackHandler implements MqttCallback
{
	private final static Logger logger = LoggerFactory.getLogger(MqttCallbackHandler.class);
	
	/** Stores all received messages, so that we don't block the receiving thread. */
	private final Queue<ReceivedMqttMessage> messageQueue = new LinkedBlockingQueue<ReceivedMqttMessage>();
	
	private final MqttMessageHandler messageHandler;
	
	private final BaseMqttConnection connection;
	
	private final Map<String, SubscriptionDetails> subscriptions = new HashMap<String, SubscriptionDetails>();

	private final ScriptManager scriptManager;

	private final DaemonMqttConnectionDetails connectionSettings;
	
	private long currentId = 1;

	public MqttCallbackHandler(final BaseMqttConnection connection, final DaemonMqttConnectionDetails connectionSettings, final ScriptManager scriptManager)
	{
		this.connection = connection;
		this.connectionSettings = connectionSettings;
		this.scriptManager = scriptManager;
		this.messageHandler = new MqttMessageHandler(messageQueue, connectionSettings);
		
		for (final SubscriptionDetails subscriptionDetails : connectionSettings.getSubscription())
		{
			this.subscriptions.put(subscriptionDetails.getTopic(), subscriptionDetails);
		}
		
		new Thread(messageHandler).start();			
	}

	public void connectionLost(Throwable cause)
	{
		logger.error("Connection lost", cause);
	}

	public void messageArrived(final String topic, final MqttMessage message)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("[{}] Received message on topic \"{}\". Payload = \"{}\"", messageQueue.size(), topic, new String(message.getPayload()));
		}
		
		final ReceivedMqttMessage receivedMessage = new ReceivedMqttMessage(currentId, topic, message);
		
		// Add the received message to queue for logging
		if (!MessageLog.DISABLED.equals(connectionSettings.getMessageLog()))
		{
			messageQueue.add(receivedMessage);
		}
		
		// Check matching subscriptions
		final List<String> matchingSubscriptions = connection.getMatchingSubscriptions(receivedMessage);
		
		// If configured, run scripts for the matching subscriptions
		for (final String matchingSubscription : matchingSubscriptions)
		{
			final SubscriptionDetails subscriptionDetails = subscriptions.get(matchingSubscription);
			
			if (subscriptionDetails.getScriptFile() != null)
			{
				final PublicationScriptProperties script = scriptManager.getScript(new File(subscriptionDetails.getScriptFile()));
				script.getScriptEngine().put("receivedMessage", receivedMessage);
				scriptManager.evaluateScriptFile(script);
			}
		}
		
		currentId++;
	}

	public void deliveryComplete(IMqttDeliveryToken token)
	{
		if (logger.isTraceEnabled())
		{
			logger.trace("Delivery complete for " + token.getMessageId());
		}
	}
}