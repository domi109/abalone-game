package abalone.exec;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

import abalone.ai.Ai;
import abalone.ai.BasicMinimaxAI;
import abalone.gamelogic.GameLogic;
import abalone.gamelogic.StandardAbaloneLogic;
import abalone.gamelogic.SmallAbaloneLogic;
import abalone.gamelogic.TinyAbaloneLogic;
import abalone.gamestate.GameState;
import abalone.gui.AbaloneFront;
import abalone.model.Board;
import abalone.model.HumanPlayer;
import abalone.model.Move;
import abalone.model.Player;

import com.trolltech.qt.QThread;
import com.trolltech.qt.core.Qt.ConnectionType;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QMessageBox;

/**
 * The main class of the abalone game. Here the overall program control is done.
 * The GUI is instanciated and executed, players and gamelogic are initialized,
 * ...
 * 
 */
public class Main
{
	private class Decider implements Runnable
    {
    	private Move decision;
    	private Ai ai;
    	
    	public Decider()
    	{
    	}
    	
    	public void setAi(Ai ai)
    	{
    		this.ai = ai;
    	}
    	
        @Override
        public void run()
        {
        	decision = ai.decide(state);
			if (!logic.isLegal(state, decision))
			{
				throw new RuntimeException("illegal move chosen by ai: " + decision.toString());
			}
        }
        
        public Move getDecision()
        {
        	return decision;
        }
    }
    
	// This is the object that represents the current GameLogic
	private GameLogic logic;
	// This is the board that is currently played on, is also
	// contained in the state
	private Board board;
	// This is the list of all players that are involved in the
	// current game; Is also contained in the state
	private List<Player> players;
	// The GameState - a central storage of the current state
	// of the game.
	private GameState state;
	// The Qt-based abalone frontend
	private AbaloneFront front;
	
	private Decider decider;

	// The GameLogic in use. This constant is more or less a placeholder:
	// In principle this can be just a config-option
	private static Class<? extends GameLogic> logicClass = SmallAbaloneLogic.class;

	/**
	 * Slot for the signal that is sent when the user confirms the notification
	 * that a player has won. This method basically initiates a reset.
	 * 
	 * @param button
	 */
	@SuppressWarnings("unused")
	private void messageBoxClicked(QAbstractButton button)
	{
		resetGame();
	}

	/**
	 * Resets the game so it can be played from the very beginning
	 */
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
		players.add(new HumanPlayer("Pong"));
		//players.add(new BasicMinimaxAI(logic));
		players.add(new BasicMinimaxAI(logic));
		//players.add(new HumanPlayer("Ping"));
		state = logic.initState(board, players);
		
		decider = new Decider();
		
		QApplication.initialize(args);

		front = new AbaloneFront(state);
		front.show();
		front.getBoardWidget().move.connect(this, "moveDone(Move)");
		front.getBoardWidget().updated.connect(this, "boardUpdated()");
		front.newGame.connect(this, "resetGame()");
		front.saveGame.connect(this, "saveGame(String)");
		
		boardUpdated();
		
		QApplication.exec();

	}

	/**
	 * Slot for board updated signal.
	 * Used to trigger an ai decission if ai is next
	 */
	private void boardUpdated()
	{
		if ((state.getCurrentPlayer() instanceof Ai) && logic.getWinner(state)==null)
		{
			decider.setAi((Ai)state.getCurrentPlayer());
	        QThread runner = new QThread(decider);
		    
	        runner.start();
	        runner.finished.connect(this, "decisionDone()",ConnectionType.QueuedConnection);
		}
	}
	
	/**
	 * Slot for the signal that is emitted when the AI has taken
	 * a decision.
	 */
	@SuppressWarnings("unused")
	private void decisionDone()
	{
		
		moveDone(decider.getDecision());
	}
	
	private void moveDone(Move m)
	{
		if (!logic.isLegal(state, m))
		{
			return;
		}
		logic.applyMove(state, m);
		front.updateFront();
		if (logic.getWinner(state) != null)
		{
			QMessageBox message = new QMessageBox();
			message.setText(logic.getWinner(state).getName()+", You've won.");
			message.setWindowTitle("Winner!");
			message.show();
			message.buttonClicked.connect(this, "messageBoxClicked(QAbstractButton)");
			return; // Game is over.
		}
	}
	
	private void saveGame(String place)
	{
		System.out.println(place);
		try
		{
			FileOutputStream stream = new FileOutputStream(place);
			ObjectOutputStream objects = new ObjectOutputStream(stream);
			objects.writeObject(state);
			objects.close();
			stream.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		Main main = new Main(args);
	}

}
