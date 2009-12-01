package abalone.model;

import java.io.Serializable;
import java.util.Random;

import search.hashing.ZobristHashable;

import com.trolltech.qt.gui.QWidget;

/**
 * An interface for a Player of the game of abalone
 *
 */
public abstract class Player implements Serializable
{
	private static final long serialVersionUID = 3580967490056683736L;
	private static Random random = new Random();
	
	private byte hash;
	
	public Player()
	{
		byte[] bytes = new byte[1];
		                  
		random.nextBytes(bytes);
		hash = bytes[0];
	}
	
	public abstract String getName();
	
	public byte hash()
	{
		return hash;
	}
	

}
