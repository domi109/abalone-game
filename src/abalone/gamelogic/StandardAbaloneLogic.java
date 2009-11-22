package abalone.gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import abalone.adt.KeyValuePair;
import abalone.gamestate.GameState;
import abalone.model.Board;
import abalone.model.Direction;
import abalone.model.Move;
import abalone.model.Node;
import abalone.model.Player;

public class StandardAbaloneLogic implements GameLogic
{
	private static final long serialVersionUID = -1116931368388798072L;
	protected int radius, marblesToWin;

	public StandardAbaloneLogic()
	{
		this.radius = 5;
		this.marblesToWin = 6;
	}

	@Override
	public Board initBoard()
	{
		Board b = new Board();
		// TODO Create the graph recursively:
		createNode(b, b.getCentralNode(), radius - 1);

		for (Direction d : Direction.UPPER_LEFT)
		{
			b.addPath(createPathCW(b.getCentralNode(), d));
		}
		
		for (Direction d : Direction.UPPER_LEFT)
		{
			b.addPath(createPathCCW(b.getCentralNode(), d));
		}

		int i = 0;
		for (KeyValuePair<Direction, Node> p : b.getEquiPaths().get(0))
		{
			p.getValue().setName(String.valueOf(i));
			i++;
		}
		addManhDist(b.getEquiPaths().get(0));
		return b;
	}

	protected List<KeyValuePair<Direction, Node>> createPathCW(Node centralNode, Direction startDirection)
	{
		List<KeyValuePair<Direction, Node>> path = new ArrayList<KeyValuePair<Direction, Node>>();

		path.add(new KeyValuePair<Direction, Node>(null, centralNode));
		Direction currentDir = startDirection;
		Node currentNode = centralNode;
		for (int i = 1; i < radius; i++)
		{
			currentNode = currentNode.getNeighbour(currentDir);
			path.add(new KeyValuePair<Direction, Node>(currentDir, currentNode));
			currentDir = currentDir.getNextCW();

			for (Direction d : currentDir)
			{

				if (d.equals(startDirection.getNextCW()))
				{
					for (int j = 0; j < i - 1; j++)
					{
						currentNode = currentNode.getNeighbour(d);
						path.add(new KeyValuePair<Direction, Node>(d, currentNode));
					}
				}
				else
				{
					for (int j = 0; j < i; j++)
					{
						currentNode = currentNode.getNeighbour(d);
						path.add(new KeyValuePair<Direction, Node>(d, currentNode));
					}
				}
			}

			currentDir = startDirection;

		}

		return path;
	}
	
	protected List<KeyValuePair<Direction, Node>> createPathCCW(Node centralNode, Direction startDirection)
	{
		List<KeyValuePair<Direction, Node>> path = new ArrayList<KeyValuePair<Direction, Node>>();

		path.add(new KeyValuePair<Direction, Node>(null, centralNode));
		Direction currentDir = startDirection;
		Node currentNode = centralNode;
		for (int i = 1; i < radius; i++)
		{
			currentNode = currentNode.getNeighbour(currentDir);
			path.add(new KeyValuePair<Direction, Node>(currentDir, currentNode));
			currentDir = currentDir.getNextCCW();
			
			Direction d = currentDir;
			do 
			{
				if (d.equals(startDirection.getNextCCW()))
				{
					for (int j = 0; j < i - 1; j++)
					{
						currentNode = currentNode.getNeighbour(d);
						path.add(new KeyValuePair<Direction, Node>(d, currentNode));
					}
				}
				else
				{
					for (int j = 0; j < i; j++)
					{
						currentNode = currentNode.getNeighbour(d);
						path.add(new KeyValuePair<Direction, Node>(d, currentNode));
					}
				}
				d = d.getNextCCW();
			}while((!d.equals(startDirection.getNextCCW())));

			currentDir = startDirection;

		}

		return path;
	}

	@Override
	public GameState initState(Board board, List<Player> players)
	{
		GameState state = new GameState();
		state.setPlayers(players);
		state.setCurrentPlayer(players.get(0));
		state.setBoard(board);
		state.setMarblesToWin(marblesToWin);

		HashMap<Player, Integer> marblesRemoved = new HashMap<Player, Integer>(2);

		for (Player p : players)
		{
			marblesRemoved.put(p, 0);
		}
		state.setMarblesRemoved(marblesRemoved);

		initMarbles(state);

		return state;
	}

	protected void initMarbles(GameState state)
	{
		List<KeyValuePair<Direction, Node>> path = state.getBoard().getEquiPaths().get(0);
		int i = 0;
		for (KeyValuePair<Direction, Node> step : path)
		{
			if ((i >= 8 && i <= 10) || (i >= 21 && i <= 24) || (i >= 39 && i <= 45))
			{
				state.setMarble(step.getValue(), state.getPlayers().get(0));
			}
			else if ((i >= 14 && i <= 16) || (i >= 30 && i <= 33) || (i >= 51 && i <= 57))
			{
				state.setMarble(step.getValue(), state.getPlayers().get(1));
			}
			i++;
		}
	}

	@Override
	public void applyMove(GameState state, Move move)
	{
		// this if statement is correct but looks horrifying...
		// maybe there's a nicer way.
		if (move.getMarbleLine().getOrientation() != null
				&& (move.getDirection().equals(move.getMarbleLine().getOrientation()) || move.getDirection().equals(
						move.getMarbleLine().getOrientation().getOpposite())))
		{
			applyInlineMove(state, move);
		}
		else
		{
			applyBroadSideMove(state, move);
		}

		// Change player
		state.setCurrentPlayer(state.getOpponentPlayer());
	}

	protected void applyBroadSideMove(GameState state, Move move)
	{
		for (Node n : move.getMarbleLine().getNodes())
		{
			state.setMarble(n.getNeighbour(move.getDirection()), state.getMarbleOwner(n));
			state.removeMarble(n);
		}
	}

	protected void applyInlineMove(GameState state, Move move)
	{
		int llength = move.getMarbleLine().getNodes().size();
		Node n = move.getMarbleLine().getNodes().get(0);
		Player p = state.getMarbleOwner(n);
		Node m = null;

		while (state.getMarbleOwner(n) != null)
		{
			m = n;
			n = n.getNeighbour(move.getDirection());
		}

		// now n is the first free spot, m is the last seen node
		if (n == null)
		{
			// marble pushed off the board
			int removed = state.getMarblesRemoved().get(state.getMarbleOwner(m));
			removed++;
			state.getMarblesRemoved().put(state.getMarbleOwner(m), removed);
			state.removeMarble(m);
			n = m;
			m = m.getNeighbour(move.getDirection().getOpposite());
		}
		int ownNodes = 0;
		while (ownNodes < llength)
		{
			if (state.getMarbleOwner(m) == p)
			{
				ownNodes++;
			}
			state.setMarble(n, state.getMarbleOwner(m));
			state.removeMarble(m);

			n = m;
			m = m.getNeighbour(move.getDirection().getOpposite());
		}
	}

	/**
	 * Recursively adds nodes to the board graph
	 * 
	 * @param node
	 *            the node that is currently handled
	 * @param level
	 *            level-counter counting backwards from the center - recursion
	 *            will terminate when level is zero.
	 */
	protected void createNode(Board b, Node node, int level)
	{
		// TODO: this might need to be improved
		if (level <= 0)
		{
			// Terminate recursion
			return;
		}

		Direction startDirection = Direction.UPPER_LEFT;
		for (Direction d : startDirection)
		{
			// We're going clockwise looking into all directions.
			// The direction we're currently looking at is d
			// od is the opposite direction.
			Direction od = d.getOpposite();

			// We look at the nodes neighbour in the investigated direction
			Node neighbour = node.getNeighbour(d);
			if (neighbour == null)
			{
				// The neighbour is null, so it has not been created yet
				// thus, we now create it and add it to the nodes list.
				neighbour = node.addNeighbour(d);
				b.getNodes().add(neighbour);
			}

			// We still have to link the neighbour with the current node...
			neighbour.setNeighbour(od, node);

			// Now we want to look at the nodes next to the investigated
			// neighbour...
			Node prev = node.getNeighbour(d.getNextCCW());
			Node next = node.getNeighbour(d.getNextCW());
			if (prev != null)
			{
				// there is a node next to our neighbour node
				// in counterclockwise direction... we interconnect
				// those two.
				neighbour.setNeighbour(od.getNextCW(), prev);
				prev.setNeighbour(d.getNextCW(), neighbour);
			}
			if (next != null)
			{
				// there is a node next to our neighbour node
				// in clockwise direction... we interconnect
				// those two.
				neighbour.setNeighbour(od.getNextCCW(), next);
				next.setNeighbour(d.getNextCCW(), neighbour);
			}

			// now we go deeper into the recursion tree, further investigating
			// on
			// the neighbour node.
			createNode(b, neighbour, level - 1);
		}
	}

	/**
	 * Return the player who has won and null if there currently is no winner at
	 * all.
	 * 
	 * @see abalone.gamelogic.GameLogic#getWinner(abalone.gamestate.GameState)
	 */
	@Override
	public Player getWinner(GameState state)
	{
		if (state.getMarblesRemoved().get(state.getCurrentPlayer()) >= marblesToWin)
		{
			return state.getOpponentPlayer();
		}
		else if (state.getMarblesRemoved().get(state.getOpponentPlayer()) >= marblesToWin)
		{
			return state.getCurrentPlayer();
		}
		return null;
	}

	@Override
	public boolean isLegal(GameState state, Move m)
	{
		boolean legal = false;
		if (m.getMarbleLine().getNodes().size() == 0)
		{
			// We don't want empty moves
			return false;
		}
		// this if statement is correct but looks horrifying...
		// maybe there's a nicer way.
		if (m.getMarbleLine().getOrientation() != null
				&& (m.getDirection().equals(m.getMarbleLine().getOrientation()) || m.getDirection().equals(m.getMarbleLine().getOrientation().getOpposite())))
		{
			legal = isLegalInlineMove(state, m);
		}
		else
		{
			legal = isLegalBroadSideMove(state, m);
		}

		return legal;
	}

	protected boolean isLegalInlineMove(GameState state, Move m)
	{
		int ownMarbles = m.getMarbleLine().getNodes().size();
		int opponentMarbles = 0;
		Node n = m.getMarbleLine().getNodes().get(0);
		Player p = state.getMarbleOwner(n);

		while (n != null && state.getMarbleOwner(n) != null)
		{
			if (p.equals(state.getMarbleOwner(n)))
			{
				if (!m.getMarbleLine().getNodes().contains(n))
				{
					return false;
				}
			}
			else
			{
				opponentMarbles++;
			}
			n = n.getNeighbour(m.getDirection());
		}
		if (n == null && opponentMarbles == 0)
		{
			return false;
		}
		return ownMarbles > opponentMarbles;
	}

	protected boolean isLegalBroadSideMove(GameState state, Move m)
	{
		for (Node marble : m.getMarbleLine().getNodes())
		{
			if (marble.getNeighbour(m.getDirection()) == null || state.getMarbleOwner(marble.getNeighbour(m.getDirection())) != null)
			{
				return false;
			}
		}
		return true;
	}

	private void addManhDist(List<KeyValuePair<Direction, Node>> aPath)
	{
		boolean finished = false;
		int lvl = 0;
		int from = 1;

		aPath.get(0).getValue().setManhDist(lvl);

		while (!finished)
		{
			lvl++;
			int to = (lvl * 6) + from;
			for (int i = from; i < to; i++)
			{
				aPath.get(i).getValue().setManhDist(lvl);
			}
			from = to;
			if (lvl == radius - 1)
				finished = true;
		}
	}

}
