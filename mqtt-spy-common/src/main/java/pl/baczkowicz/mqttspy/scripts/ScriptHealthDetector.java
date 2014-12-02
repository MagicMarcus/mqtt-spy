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
package pl.baczkowicz.mqttspy.scripts;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.baczkowicz.mqttspy.utils.ThreadingUtils;
import pl.baczkowicz.mqttspy.utils.TimeUtils;

public class ScriptHealthDetector implements Runnable
{
	public final static int DEFAULT_THREAD_TIMEOUT = 5000;
	
	private final static Logger logger = LoggerFactory.getLogger(ScriptHealthDetector.class);
	
	private final PublicationScriptProperties script;

	private IScriptEventManager eventManager;

	private Executor executor;

	public ScriptHealthDetector(final IScriptEventManager eventManager, final PublicationScriptProperties script, final Executor executor)
	{
		this.script = script;
		this.eventManager = eventManager;
		this.executor = executor;
	}
	
	public void run()
	{
		Thread.currentThread().setName("Script Health Detector");
		ThreadingUtils.logStarting();
		
		while (script.getStatus().equals(ScriptRunningState.RUNNING))
		{
			if (script.getPublicationScriptIO().getLastTouch() + script.getScriptTimeout() < TimeUtils.getMonotonicTime())
			{
				logger.warn("Script {} detected as frozen, last touch = {}, current time = {}", script.getName(), 
						script.getPublicationScriptIO().getLastTouch(), TimeUtils.getMonotonicTime());
				ScriptRunner.changeState(eventManager, script.getName(), ScriptRunningState.FROZEN, script, executor);
			}
			
			if (ThreadingUtils.sleep(1000))			
			{
				break;
			}
		}
		
		ThreadingUtils.logEnding();
	}
}
