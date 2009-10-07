package abalone.gamelogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import abalone.adt.KeyValuePair;
import abalone.gamestate.GameState;
import abalone.model.Board;
import abalone.model.Direction;
import abalone.model.MarbleLine;
import abalone.model.Move;
import abalone.model.Node;
import abalone.model.Player;

public class StandardAbaloneLogic implements GameLogic
{

	@Override
	public Board initBoard()
	{
		Board b = new Board();
		// TODO Create the graph recursively:
		createNode(b, b.getCentralNode(), 4);

		for (Direction d : Direction.UPPER_LEFT)
		{
			b.addPath(createPath(b.getCentralNode(), d));
		}
		return b;
	}

	private List<KeyValuePair<Direction, Node>> createPath(Node centralNode, Direction startDirection)
	{
		List<KeyValuePair<Direction, Node>> path = new ArrayList<KeyValuePair<Direction, Node>>();

		path.add(new KeyValuePair<Direction, Node>(null, centralNode));
		Direction currentDir = startDirection;
		Node currentNode = centralNode;
		for (int i = 1; i < 5; i++)
		{
			// TODO Iterate through all the 5 rings of the board
			currentNode = currentNode.getNeighbour(currentDir);
			path.add(new KeyValuePair<Direction, Node>(currentDir, currentNode));
			currentDir = currentDir.getNextCW();

			for (Direction d : currentDir)
			{
				if (d.equals(startDirection.getNextCW()))
				{
					for (int j = 0; j < i - 1; j++)
					{
						// System.out.println(i + " " +d);
						currentNode = currentNode.getNeighbour(d);
						path.add(new KeyValuePair<Direction, Node>(d, currentNode));
					}
				}
				else
				{
					for (int j = 0; j < i; j++)
					{
						// System.out.println(i + " " +d);
						currentNode = currentNode.getNeighbour(d);
						path.add(new KeyValuePair<Direction, Node>(d, currentNode));
					}
				}
			}

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

		List<KeyValuePair<Direction, Node>> path = board.getEquiPaths().get(0);
		int i = 0;
		for (KeyValuePair<Direction, Node> step : path)
		{
			if ((i >= 8 && i <= 10) || (i >= 21 && i <= 24) || (i >= 39 && i <= 45))
			{
				step.getValue().setMarbleOwner(players.get(0));
			}
			else if ((i >= 14 && i <= 16) || (i >= 30 && i <= 33) || (i >= 51 && i <= 57))
			{
				step.getValue().setMarbleOwner(players.get(1));
			}
			i++;
		}

		return state;
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
		int curr = state.getPlayers().indexOf(state.getCurrentPlayer());
		state.setCurrentPlayer(state.getPlayers().get((curr + 1) % 2));
	}

	private void applyBroadSideMove(GameState state, Move move)
	{
		for (Node n : move.getMarbleLine().getNodes())
		{
			n.getNeighbour(move.getDirection()).setMarbleOwner(n.getMarbleOwner());
			n.setMarbleOwner(null);
		}
	}

	private void applyInlineMove(GameState state, Move move)
	{
		int llength = move.getMarbleLine().getNodes().size();
		Node n = move.getMarbleLine().getNodes().get(0);
		Player p = n.getMarbleOwner();
		Node m = null;

		while (n != null && n.getMarbleOwner() != null)
		{
			m = n;
			n = n.getNeighbour(move.getDirection());
		}

		// now n is the first free spot, m is the last seen node

		int ownNodes = 0;
		while (ownNodes < llength)
		{
			if (p.equals(m.getMarbleOwner()))
			{
				ownNodes++;
			}
			n.setMarbleOwner(m.getMarbleOwner());
			n = m;
			m = m.getNeighbour(move.getDirection().getOpposite());
		}
		n.setMarbleOwner(null);
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
	private void createNode(Board b, Node node, int level)
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
		for (Entry<Player, Integer> e : state.getMarblesRemoved().entrySet())
		{
			if (e.getValue() >= 6)
			{
				return e.getKey();
			}
		}
		return null;
	}

	@Override
	public boolean isLegal(GameState state, Move m)
	{
		boolean legal = false;
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

	private boolean isLegalInlineMove(GameState state, Move m)
	{
		int ownMarbles = m.getMarbleLine().getNodes().size();
		int opponentMarbles =0;
		Node n = m.getMarbleLine().getNodes().get(0);
		Player p = n.getMarbleOwner();

		while (n != null && n.getMarbleOwner() != null)
		{
			if(p.equals(n.getMarbleOwner()))
			{
				if(!m.getMarbleLine().getNodes().contains(n))
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
		return ownMarbles > opponentMarbles;
	}

	private boolean isLegalBroadSideMove(GameState state, Move m)
	{
		for (Node marble : m.getMarbleLine().getNodes())
		{
			if (marble.getNeighbour(m.getDirection()) != null && marble.getNeighbour(m.getDirection()).getMarbleOwner() != null)
			{
				return false;
			}
		}
		return true;
	}

}
