/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package abalone.gamestate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import search.Action;
import search.tree.SearchState;

import abalone.model.Board;
import abalone.model.Player;

/**
 *
 * @author rutger
 */
public class GameState implements SearchState{
	Board board;
	List<Player> players;
	Map<Player,Integer> marblesRemoved;
	Player currentPlayer;
	
	public Board getBoard()
	{
		return board;
	}
	public void setBoard(Board board)
	{
		this.board = board;
	}
	public List<Player> getPlayers()
	{
		return players;
	}
	public void setPlayers(List<Player> players)
	{
		this.players = players;
	}
	public Map<Player, Integer> getMarblesRemoved()
	{
		return marblesRemoved;
	}
	public void setMarblesRemoved(Map<Player, Integer> marblesRemoved)
	{
		this.marblesRemoved = marblesRemoved;
	}
	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}
	public void setCurrentPlayer(Player currentPlayer)
	{
		this.currentPlayer = currentPlayer;
	}
	
	@Override
	public boolean equalState(SearchState state)
	{
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Collection<Action> getPossibleActions()
	{
		// TODO Generate all moves that can be done in this situation
		return null;
	}
	@Override
	public void setPossibleActions(Collection<Action> a)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Object clone()
	{
		return null;
		
	}
}
