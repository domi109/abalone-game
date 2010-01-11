package abalone.ai;

import java.util.LinkedList;
import java.util.List;

import nl.maastrichtuniversity.dke.libreason.def.Action;
import nl.maastrichtuniversity.dke.libreason.def.SearchState;
import nl.maastrichtuniversity.dke.libreason.def.treesearch.MinimaxProblem;
import abalone.gamelogic.GameLogic;
import abalone.gamestate.GameState;
import abalone.model.Direction;
import abalone.model.MarbleLine;
import abalone.model.Move;
import abalone.model.Node;
import abalone.model.Player;
import abalone.model.Move.MoveType;

class AbaloneSearchProblem implements MinimaxProblem
{

	private static final long serialVersionUID = -3644069950454603818L;
	private GameState initialState;
	private GameLogic logic;

	public AbaloneSearchProblem(GameState initial, GameLogic logic)
	{
		this.logic = logic;
		this.initialState = initial;
	}

	/**
	 * This is just a helper to save all this ((GameState) state) typing
	 */
	private GameState gs(SearchState s)
	{
		return (GameState) s;
	}

	@Override
	public int getFinalStateValue(SearchState state)
	{
		if (initialState.getCurrentPlayer() == logic.getWinner(gs(state)))
		{
			// the current player (max) won - result is one
			return 1;
		}
		else
		{
			// the other player (min) won - result is minus one
			return -1;
		}
	}

	@Override
	public boolean goalTest(SearchState state)
	{
		return logic.getWinner(gs(state)) != null;
	}

	@Override
	public List<Action> generateActions(SearchState state)
	{
		GameState s = (GameState) state;
		LinkedList<Action> actions = new LinkedList<Action>();
		LinkedList<Action> singleMarble = new LinkedList<Action>();
		LinkedList<Action> inline = new LinkedList<Action>();
		LinkedList<Action> broadside = new LinkedList<Action>();

		for (Node n : s.getMarbles(s.getCurrentPlayer()))
		{
			for (Direction d : Direction.UPPER_LEFT)
			{
				Node neighbour = n.getNeighbour(d);
				if (neighbour != null)
				{
					Player owner = s.getMarbleOwner(neighbour);
					if (owner == null)
					{
						// found a single marble move
						MarbleLine l = new MarbleLine();
						l.add(n);
						l.setOrientation(d);
						Move m = new Move();
						m.setType(MoveType.SINGLE);
						m.setMarbleLine(l);
						m.setDirection(d);
						singleMarble.add(m);
						// check for broadside moves
						generateBroadsideMoves(s, n, d, broadside);
					}
					else if (owner == s.getCurrentPlayer())
					{
						// potential inline move, start recursive function
						generateInlineMoves(s, neighbour, d, inline, 2);
					}
				}
			}
		}
		actions.addAll(singleMarble);
		actions.addAll(inline);
		actions.addAll(broadside);

		// System.out.println();
		// System.out.println("single moves: " + singleMarble.size());
		// System.out.println("inline moves: " + inline.size());
		// System.out.println("broadside moves: " + broadside.size());
		// System.out.println("" + actions.size());

		return actions;
	}

	private void generateBroadsideMoves(GameState s, Node n, Direction d, LinkedList<Action> result)
	{
		MarbleLine l = new MarbleLine();
		generateBroadsideMoves(s, n, d, d.getNextCW(), result, 0, l);
		l = new MarbleLine();
		generateBroadsideMoves(s, n, d, d.getNextCW().getNextCW(), result, 0, l);
	}

	private void generateBroadsideMoves(GameState state, Node node, Direction direction, Direction orientation, LinkedList<Action> result, int length,
			MarbleLine l)
	{
		if (length >= 3)
		{
			return;
		}

		Node oriontationNeighbour = node.getNeighbour(orientation);
		Node directionNeighbour = node.getNeighbour(direction);
		Player currentPlayer = state.getMarbleOwner(node);
		Player directionPlayer = state.getMarbleOwner(node.getNeighbour(direction));
		Player orientationPlayer = state.getMarbleOwner(node.getNeighbour(orientation));

		if ((directionPlayer == null) && (directionNeighbour != null))
		{
			l.add(node);
			if (l.getNodes().size() > 1)
			{
				l.setOrientation(orientation);
				Move m = new Move();
				m.setType(MoveType.BROADSIDE);
				m.setMarbleLine(l);
				m.setDirection(direction);
				result.add(m);
			}
			if (currentPlayer == orientationPlayer)
			{
				generateBroadsideMoves(state, oriontationNeighbour, direction, orientation, result, (length + 1), l);
			}
		}
		else
			return;
	}

	private void generateInlineMoves(GameState state, Node last, Direction direction, LinkedList<Action> result, int length)
	{
		// First of all this method is called recursively
		// Thus it has to stop here if the length of the
		// inline move is illegal.

		if (length > 3)
		{
			return;
		}

		// Ok, now we can go on
		Node neighbour = last.getNeighbour(direction);
		if (neighbour == null)
		{
			// no suicide moves!
			return;
		}
		Player owner = state.getMarbleOwner(neighbour);
		if (owner == null)
		{
			// found a regular inline move
			MarbleLine l = new MarbleLine();
			Node curr = last;
			for (int i = 0; i < length; i++)
			{
				l.add(curr);
				curr = curr.getNeighbour(direction.getOpposite());
			}
			l.setOrientation(direction);
			Move m = new Move();
			m.setType(MoveType.INLINE);
			m.setMarbleLine(l);
			m.setDirection(direction);
			result.add(m);
		}
		else if (owner == state.getCurrentPlayer())
		{
			// possibly a longer inline move
			// start recursive call!
			generateInlineMoves(state, neighbour, direction, result, length + 1);
		}
		else
		{
			// There seems to be an opponent marble...
			// this might become a sumito move!
			int oppMarbles = 0;
			Node curr = neighbour;
			while (state.getMarbleOwner(curr) == owner)
			{
				oppMarbles++;
				if (oppMarbles >= length)
				{
					return;
				}
				curr = curr.getNeighbour(direction);
			}
			if (state.getMarbleOwner(curr) == state.getMarbleOwner(last))
			{
				return;
			}

			// OK, so it indeed now has to be a sumito move

			MarbleLine l = new MarbleLine();
			for (int i = 0; i < length; i++)
			{
				l.add(last);
				last = last.getNeighbour(direction.getOpposite());
			}
			l.setOrientation(direction);
			Move m = new Move();
			if (curr == null)
			{
				m.setType(MoveType.PUSHOFF);
			}
			else
			{
				m.setType(MoveType.SUMITO);
			}
			m.setMarbleLine(l);
			m.setDirection(direction);
			result.add(m);
		}
	}

	@Override
	public double repetitionValue()
	{
		return 0;
	}
}
