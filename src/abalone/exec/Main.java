package abalone.exec;

import java.util.ArrayList;
import java.util.List;

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
	// This is the object that represents the current GameLogic
	private GameLogic logic;
	// This is the board that is currently played on, is also
	// contained in the state
	private Board board;
	// This is the list of all players that are involved in the
	// curren game; Is also contained in the state
	private List<Player> players;
	// The GameState - a central storage of the current state
	// of the game.
	private GameState state;
	// The Qt-based abalone frontend
	private AbaloneFront front;

	// The GameLogic in use. This constant is more or less a placeholder:
	// In principle this can be just a config-option
	private static Class<? extends GameLogic> logicClass = TinyAbaloneLogic.class;

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
		players.add(new BasicMinimaxAI(logic));
		//players.add(new HumanPlayer("Ping"));
		state = logic.initState(board, players);
		QApplication.initialize(args);

		front = new AbaloneFront(state);
		front.show();
		front.getBoardWidget().move.connect(this, "moveDone(Move)");
		front.newGame.connect(this, "resetGame()");

		QApplication.exec();

	}

	@SuppressWarnings("unused")
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
			message.setText("You've won");
			message.setWindowTitle("Winner!");
			message.show();
			message.buttonClicked.connect(this, "messageBoxClicked(QAbstractButton)");
			return; // Game is over.
		}

		// What if the next player is ai?
		if (state.getCurrentPlayer() instanceof Ai)
		{
			Ai ai = (Ai) state.getCurrentPlayer();
			Move decision = ai.decide(state);
			if (!logic.isLegal(state, decision))
			{
				// TODO: leave this if statement here until youre sure that
				// the AI knows what it's doin'
				throw new RuntimeException("illegal move chosen by ai: " + decision.toString());
			}
			moveDone(decision);
		}
	}

	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		Main main = new Main(args);
	}

}
