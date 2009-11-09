package abalone.gamestate;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.io.Serializable;

import search.Action;
import search.tree.SearchState;

import abalone.model.Board;
import abalone.model.Node;
import abalone.model.Player;

/**
 * The class representing a state in the game of abalone.
 * 
 * @author rutger
 */
public class GameState implements SearchState, Serializable
{
	private Board board;
	private List<Player> players;
	private Map<Player, Integer> marblesRemoved;
	private Player currentPlayer;
	private int marblesToWin;
	private Map<Node, Player> marbleOwners;
	private Map<Player,Set<Node>> marblePositions;

	public GameState()
	{
		marbleOwners = new HashMap<Node, Player>();
		marblePositions = new HashMap<Player,Set<Node>>();
	}

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
		for(Player p : players)
		{
			marblePositions.put(p, new HashSet<Node>());
		}
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
		if(!(state instanceof GameState))
		{
			return false;
		}
		GameState s = (GameState) state;
		if(!players.equals(s.players))
		{
			return false;
		}
		
		if(currentPlayer != s.currentPlayer)
		{
			return false;
		}
		
		if(marblesToWin != s.marblesToWin)
		{
			return false;
		}
		
		if(!board.equals(s.board))
		{
			return false;
		}
		
		if(!marbleOwners.equals(s.marbleOwners))
		{
			return false;
		}
		
		if(!marblesRemoved.equals(s.marblesRemoved))
		{
			return false;
		}
		
		return true;
		
	}

	/**
	 * Copying the game state. The clone is done in a not quite deep but also
	 * not quite shallow way. Things that are significant for the current state
	 * within the game are copied deep:
     *  - number of removed marbles
     *  - positions
	 * of marbles on the board Things that are significant for the game, are
	 * copied shallow: 
	 *  - the board geometry 
	 *  - the players
	 *  - ...
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone()
	{
		GameState s2 = new GameState();

		s2.board = board;
		s2.currentPlayer = this.currentPlayer;
		s2.marblesRemoved = new HashMap<Player, Integer>(marblesRemoved);
		s2.setPlayers(this.players);
		s2.marbleOwners = new HashMap<Node,Player>(this.marbleOwners);
		for(Entry<Player,Set<Node>> e : marblePositions.entrySet())
		{
			Set<Node> nodeset = new HashSet<Node>(e.getValue());
			s2.marblePositions.put(e.getKey(), nodeset);
		}
		s2.marblesToWin = this.marblesToWin;

		return s2;

	}

	public void setMarblesToWin(int marblesToWin)
	{
		this.marblesToWin = marblesToWin;
	}

	public int getMarblesToWin()
	{
		return marblesToWin;
	}

	public void setMarble(Node node, Player player)
	{
		this.marbleOwners.put(node, player);
		this.marblePositions.get(player).add(node);
	}
	
	public void removeMarble(Node node)
	{
		this.marblePositions.get(marbleOwners.get(node)).remove(node);
		this.marbleOwners.remove(node);
	}

	public Player getMarbleOwner(Node node)
	{
		return marbleOwners.get(node);
	}
	
	public Set<Node> getMarbles(Player player)
	{
		return marblePositions.get(player);
	}

	@Override
	public int hashCode()
	{
		// TODO Generate hashcode including symmetries!!!
		return super.hashCode();
	}
}
