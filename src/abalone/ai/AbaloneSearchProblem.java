package abalone.ai;

import java.util.LinkedList;
import java.util.List;

import search.Action;
import search.tree.SearchState;
import search.tree.games.minimax.MinimaxProblem;
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
						m.setType(MoveType.INLINE);
						m.setMarbleLine(l);
						m.setDirection(d);
						singleMarble.add(m);
					}
					else if (owner == s.getCurrentPlayer())
					{
						// potential inline move, start recursive function
						generateInlineMoves(s, neighbour, d, inline, 2);
					}
				}
			}
		}

		// TODO: find broadside moves!

		actions.addAll(singleMarble);
		actions.addAll(inline);
		actions.addAll(broadside);

		return actions;
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
			curr = last;
			for (int i = 0; i < length; i++)
			{
				l.add(curr);
				curr = curr.getNeighbour(direction.getOpposite());
			}
			l.setOrientation(direction);
			Move m = new Move();
			m.setType(MoveType.SUMITO);
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

