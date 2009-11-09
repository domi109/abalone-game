package abalone.adt;

import java.io.Serializable;

public class KeyValuePair<I,E> implements Serializable
{
	private static final long serialVersionUID = -5343270055865402286L;
	private I key;
	private E value;
	
	public I getKey()
	{
		return key;
	}
	public void setKey(I key)
	{
		this.key = key;
	}
	public E getValue()
	{
		return value;
	}
	public void setValue(E value)
	{
		this.value = value;
	}
	
	public KeyValuePair()
	{
	}
	
	public KeyValuePair(I key, E value)
	{
		this.key = key;
		this.value = value;
	}
	
	
}
