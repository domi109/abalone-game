/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package abalone.gamelogic;

import java.util.List;

import abalone.gamestate.GameState;
import abalone.model.Board;
import abalone.model.Move;
import abalone.model.Player;

public interface GameLogic {

    // create board, return board. Initialize the game, set marbles to initial positions, with board as argument.
    // 
	
	public Board initBoard();
	
	public GameState initState(Board board, List<Player> players);
	
	public void applyMove(GameState state, Move move);

	public Object getWinner(GameState gs);

	public boolean isLegal(GameState state, Move m);
}
