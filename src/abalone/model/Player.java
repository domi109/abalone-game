package abalone.model;

import java.io.Serializable;

import com.trolltech.qt.gui.QWidget;

/**
 * An interface for a Player of the game of abalone
 *
 */
public interface Player extends Serializable
{

	String getName();

}
