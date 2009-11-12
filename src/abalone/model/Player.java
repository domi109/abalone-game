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
	
	private long hash;
	
	public Player()
	{
		hash = random.nextLong();
	}
	
	public abstract String getName();
	
	public long hash()
	{
		return hash;
	}
	

}
