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
package pl.baczkowicz.mqttspy.storage.summary;

import java.util.Collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import pl.baczkowicz.mqttspy.configuration.generated.FormatterDetails;
import pl.baczkowicz.mqttspy.connectivity.MqttContent;
import pl.baczkowicz.mqttspy.ui.properties.SubscriptionTopicSummaryProperties;

/**
 * This class provides a direct feed to the topic summary table, by exposing the observable list.
 */
public class ObservableTopicSummary extends TopicSummary
{
	private final ObservableList<SubscriptionTopicSummaryProperties> observableTopicSummaryList = FXCollections.observableArrayList();

	public ObservableTopicSummary(final String name)
	{
		super(name);
	}
	
	public void clear()
	{
		synchronized (topicToSummaryMapping)
		{
			super.clear();
			observableTopicSummaryList.clear();
		}
	}
	
	public SubscriptionTopicSummaryProperties addMessage(final MqttContent message)
	{
		synchronized (topicToSummaryMapping)
		{
			SubscriptionTopicSummaryProperties newAdded = super.addMessage(message);
			
			if (newAdded != null)
			{				
				observableTopicSummaryList.add(newAdded);
			}
			
			return newAdded;
		}				
	}

	public void toggleShowValues(final Collection<String> topics)
	{
		synchronized (topicToSummaryMapping)
		{
			for (final SubscriptionTopicSummaryProperties item : observableTopicSummaryList)
			{
				if (topics.contains(item.topicProperty().getValue()))
				{
					item.showProperty().set(!item.showProperty().get());
				}
			}
		}
	}
	
	public void setShowValue(final String topic, final boolean value)
	{
		synchronized (topicToSummaryMapping)
		{
			for (final SubscriptionTopicSummaryProperties item : observableTopicSummaryList)
			{
				if (item.topicProperty().getValue().equals(topic))
				{
					item.showProperty().set(value);
					break;
				}
			}
		}
	}
	
	public void setShowValues(final Collection<String> topics, final boolean value)
	{
		synchronized (topicToSummaryMapping)
		{
			for (final SubscriptionTopicSummaryProperties item : observableTopicSummaryList)
			{
				if (topics.contains(item.topicProperty().getValue()))
				{
					item.showProperty().set(value);
				}
			}
		}
	}
	
	public void setAllShowValues(final boolean value)
	{
		synchronized (topicToSummaryMapping)
		{
			for (final SubscriptionTopicSummaryProperties item : observableTopicSummaryList)
			{
				item.showProperty().set(value);
			}
		}
	}

	public ObservableList<SubscriptionTopicSummaryProperties> getObservableMessagesPerTopic()
	{
		return observableTopicSummaryList;
	}
	
	public void setFormatter(final FormatterDetails messageFormat)
	{
		super.setFormatter(messageFormat);
		
		for (final SubscriptionTopicSummaryProperties item : observableTopicSummaryList)
		{
			item.changeFormat(messageFormat);
		}
	}
}
