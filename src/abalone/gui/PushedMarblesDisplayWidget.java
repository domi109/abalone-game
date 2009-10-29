package abalone.gui;

import abalone.gamestate.GameState;
import abalone.model.Player;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class PushedMarblesDisplayWidget extends QGraphicsView
{

	private Player player;
	private GameState state;
	private final static double r = 15;


	public PushedMarblesDisplayWidget(Player player, GameState state)
	{
		this.player = player;
		this.state = state;
		
		this.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
		this.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
		this.setStyleSheet("background: transparent;border: none");		
		setScene(prepareScene());
		setFixedSize((int)(3*r),(int)((2*state.getMarblesToWin()+1)*r));
	}

	protected QGraphicsScene prepareScene()
	{

		QGraphicsScene scene = new QGraphicsScene();
		
		for (int i = 0; i < state.getMarblesToWin(); i++)
		{
			Player p = (i<state.getMarblesRemoved().get(player))?player:null;
			
			AbaloneEllipse ellipse = new AbaloneEllipse(p,this.x(),this.y()+i*2*r,2*r,2*r);
			scene.addItem(ellipse);
		}

		return scene;
	}

	public void updateDisplay(GameState state)
	{
		this.state = state;
		setScene(prepareScene());
	}
}
