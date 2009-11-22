package abalone.gamestate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.io.Serializable;
import java.nio.ByteBuffer;

import search.Action;
import search.hashing.SymZobristHashable;
import search.tree.ZobristHashableState;
import search.tree.SearchState;

import abalone.adt.KeyValuePair;
import abalone.model.Board;
import abalone.model.Direction;
import abalone.model.Node;
import abalone.model.Player;

/**
 * The class representing a state in the game of abalone.
 * 
 * @author rutger
 */
public class GameState implements ZobristHashableState, SymZobristHashable
{

	private static final long serialVersionUID = 8517713366992214920L;
	private Board board;
	private List<Player> players;
	private Map<Player, Integer> marblesRemoved;
	private Player currentPlayer;
	private Player opponentPlayer;
	private int marblesToWin;
	private Map<Node, Player> marbleOwners;
	private Map<Player, Set<Node>> marblePositions;
	private Long hash = null;

	public GameState()
	{
		marbleOwners = new HashMap<Node, Player>();
		marblePositions = new HashMap<Player, Set<Node>>();
	}

	public Board getBoard()
	{
		return board;
	}

	public void initHash()
	{
		List<Player> states = new ArrayList<Player>(this.getPlayers());
		states.add(null);
		ZobristHasher.generateZobristTable(board, states);
		this.hash = 0l;
		for (Node n : board.getNodes())
		{
			hash ^= ZobristHasher.get(n, getMarbleOwner(n)).getLong(0);
		}
		hash ^= currentPlayer.hash();
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
		for (Player p : players)
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
		if (hash != null)
		{
			hash ^= this.currentPlayer.hash();
			hash ^= currentPlayer.hash();
		}
		this.currentPlayer = currentPlayer;
		this.opponentPlayer = players.get((players.indexOf(currentPlayer)+1)%2);

	}

	public Player getOpponentPlayer()
	{
		return opponentPlayer;
	}

	@Override
	public boolean equalState(SearchState state)
	{
		return this.equals(state);
	}

	/**
	 * Copying the game state. The clone is done in a not quite deep but also
	 * not quite shallow way. Things that are significant for the current state
	 * within the game are copied deep: - number of removed marbles - positions
	 * of marbles on the board Things that are significant for the game, are
	 * copied shallow: - the board geometry - the players - ...
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone()
	{
		GameState s2 = new GameState();

		s2.hash = hash;
		s2.board = board;
		s2.currentPlayer = this.currentPlayer;
		s2.opponentPlayer = this.opponentPlayer;
		s2.marblesRemoved = new HashMap<Player, Integer>(marblesRemoved);
		s2.setPlayers(this.players);
		s2.marbleOwners = new HashMap<Node, Player>(this.marbleOwners);
		for (Entry<Player, Set<Node>> e : marblePositions.entrySet())
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
		if (hash != null)
		{
			hash ^= ZobristHasher.get(node, player).getLong(0);
			hash ^= ZobristHasher.get(node, null).getLong(0);
		}
	}

	public void removeMarble(Node node)
	{
		Player owner = marbleOwners.get(node);
		this.marblePositions.get(owner).remove(node);
		this.marbleOwners.remove(node);

		if (hash != null)
		{
			hash ^= ZobristHasher.get(node, owner).getLong(0);
			hash ^= ZobristHasher.get(node, null).getLong(0);
		}
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
	public ByteBuffer zobristHash()
	{
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putLong(0, hash);
		return bb;
	}

	@Override
	public List<ByteBuffer> symmetryHashes()
	{
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putLong(0, hash);
		return ZobristHasher.getSymmetries(bb);
	}

}
