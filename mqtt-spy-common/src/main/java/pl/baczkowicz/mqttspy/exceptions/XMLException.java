package pl.baczkowicz.mqttspy.exceptions;

/**
 * Represents an exception during configuration loading.
 * 
 * @author Kamil Baczkowicz
 */
public class XMLException extends MqttSpyException
{
	/** serialVersionUID. */
	private static final long serialVersionUID = 7600725489860423132L;

	public XMLException(String error)
	{
		super(error);
	}
	
	public XMLException(String error, Throwable e)
	{
		super(error, e);
	}
}
