package abalone.exec;

import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QMessageBox;

import abalone.adt.KeyValuePair;
import abalone.gamelogic.GameLogic;
import abalone.gamelogic.SmallAbaloneLogic;
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
	private static Class<? extends GameLogic> logicClass = StandardAbaloneLogic.class;
	
	
	
	@SuppressWarnings("unused") // suppress - it is used: as an event handler
	private void messageBoxClicked(QAbstractButton button)
	{
		resetGame();
	}
	
	private void resetGame()
	{
		try
		{
			logic = logicClass.newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		board = logic.initBoard();
		state = logic.initState(board, players);
		front.updateFront(state);
	}
	
	public Main(String[] args)
	{
		try
		{
			logic = logicClass.newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
		board = logic.initBoard();
		players = new ArrayList<Player>(2);
		players.add(new HumanPlayer());
		players.add(new HumanPlayer());
		state = logic.initState(board, players);
		QApplication.initialize(args);
		
		front = new AbaloneFront(state);
		front.show();
		front.getBoardWidget().move.connect(this,"moveDone(Move)");
		front.newGame.connect(this,"resetGame()");

		QApplication.exec();
		

	}
	
	private void moveDone(Move m)
	{
		if(logic.isLegal(state,m))
		{
			logic.applyMove(state, m);
			front.updateFront();
			if(logic.getWinner(state)!=null)
			{
				QMessageBox message = new QMessageBox();
				message.setText("You've won");
				message.setWindowTitle("Winner!");
				message.show();
				//message.buttonClicked.connect(this,"messageBoxClicked(QAbstractButton)");
			}
		}
		else
		{
		}
	}
	
	public static void main(String[] args)
	{
		Main main = new Main(args);
	}

}
