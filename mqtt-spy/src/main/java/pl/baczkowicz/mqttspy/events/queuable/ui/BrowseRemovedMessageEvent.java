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
package pl.baczkowicz.mqttspy.events.queuable.ui;

import pl.baczkowicz.mqttspy.connectivity.MqttContent;
import pl.baczkowicz.mqttspy.storage.MessageList;

public class BrowseRemovedMessageEvent implements MqttSpyUIEvent
{
	private final MqttContent message;
	
	private final int messageIndex;

	private final MessageList store;

	public BrowseRemovedMessageEvent(final MessageList store, final MqttContent message, final int messageIndex)
	{
		this.store = store;
		this.message = message;
		this.messageIndex = messageIndex;
	}

	public MqttContent getMessage()
	{
		return message;
	}

	public int getMessageIndex()
	{
		return messageIndex;
	}

	public MessageList getList()
	{
		return store;
	}
}
