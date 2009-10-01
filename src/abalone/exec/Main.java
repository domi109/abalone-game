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
import abalone.model.Node;
import abalone.model.Player;


public class Main {

	public static void main(String[] args)
	{
		GameLogic logic = new StandardAbaloneLogic();
		Board board = logic.initBoard();
		List<Player> players = new ArrayList<Player>(2);
		players.add(new HumanPlayer());
		players.add(new HumanPlayer());
		GameState state = logic.initState(board, players);
		QApplication.initialize(args);
		
		AbaloneFront front = new AbaloneFront(state);
		front.show();
		
		QApplication.exec();
	}

}
