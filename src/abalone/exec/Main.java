package abalone.exec;

import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.gui.QApplication;

import abalone.adt.KeyValuePair;
import abalone.gamelogic.GameLogic;
import abalone.gamelogic.StandardAbaloneLogic;
import abalone.gamestate.GameState;
import abalone.gui.AbaloneFront;
import abalone.model.Board;
import abalone.model.Direction;
import abalone.model.HumanPlayer;
import abalone.model.Move;
import abalone.model.Node;
import abalone.model.Player;


public class Main {

	private GameLogic logic;
	private Board board;
	private List<Player> players;
	private GameState state;
	private AbaloneFront front;
	
	public Main(String[] args)
	{
		logic = new StandardAbaloneLogic();
		board = logic.initBoard();
		players = new ArrayList<Player>(2);
		players.add(new HumanPlayer());
		players.add(new HumanPlayer());
		state = logic.initState(board, players);
		QApplication.initialize(args);
		
		front = new AbaloneFront(state);
		front.show();
		front.getBoardWidget().move.connect(this,"moveDone(Move)");

		QApplication.exec();
		

	}
	
	private void moveDone(Move m)
	{
		if(logic.isLegal(m))
		{
			logic.applyMove(state, m);
			front.updateFront();
			System.out.println("Legal");
		}
		else
		{
			System.out.println("Not Legal");
		}
	}
	
	public static void main(String[] args)
	{
		Main main = new Main(args);
	}

}
