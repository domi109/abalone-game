package abalone.ai;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import search.Action;
import search.tree.SearchNode;
import search.tree.SearchState;
import search.tree.games.minimax.MiniMaxNode;
import search.tree.games.minimax.MinimaxProblem;
import search.tree.games.minimax.MinimaxSearch;
import abalone.gamelogic.GameLogic;
import abalone.gamestate.GameState;
import abalone.model.Direction;
import abalone.model.MarbleLine;
import abalone.model.Move;
import abalone.model.Node;
import abalone.model.Player;
import abalone.model.Move.MoveType;

public class BasicMinimaxAI implements Ai
{
	private class MoveComparator implements Comparator<SearchNode>
	{
		@Override
		public int compare(SearchNode o1, SearchNode o2)
		{
			// TODO Insert better heuristics about move quality here!

			Move m1 = (Move) o1.getAction();
			Move m2 = (Move) o2.getAction();

			if (m1.getMarbleLine().getNodes().size() < m2.getMarbleLine().getNodes().size())
			{
				return 1;
			}
			else if (m1.getMarbleLine().getNodes().size() == m2.getMarbleLine().getNodes().size())
			{
				return 0;
			}
			else
			{
				return -1;
			}
		}
	}

	private class AbaloneNode extends MiniMaxNode
	{
		public AbaloneNode(GameState s)
		{
			super(s);
		}

		public AbaloneNode(SearchState s, AbaloneNode parent, Action a)
		{
			super(s, parent, a);
		}

		@Override
		public Queue<SearchNode> expand()
		{
			PriorityQueue<SearchNode> successors = new PriorityQueue<SearchNode>(10, new MoveComparator());
			List<Action> actions = problem.generateActions(this.getState());
			//System.out.println(actions.size());
			for (Action a : actions)
			{
				// Every possible action in this state

				// copy the current state and apply the action on the state copy
				GameState newState = (GameState) getState().clone();

				if (!logic.isLegal(newState, (Move) a))
				{
					// TODO: leave this if statement here until youre sure that
					// the AI knows what it's doin'
					throw new RuntimeException("illegal move generated: " + a.toString());
				}

				logic.applyMove(newState, (Move) a);

				// create new node and assign properties
				SearchNode newNode = new AbaloneNode(newState, this, a);

				// add new node to list
				successors.add(newNode);
			}

			return successors;
		}
	}

	private class AbaloneSearchProblem implements MinimaxProblem
	{
		private GameState initialState;
		public AbaloneSearchProblem(GameState initial)
		{
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
		public boolean breakTest(SearchNode node)
		{
			// Cancel after a certain number of plys...
			// TODO maybe rather set some time limit here?
			return node.getPathCost() >= 6;
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
	}

	private GameLogic logic;
	private AbaloneSearchProblem problem;

	public BasicMinimaxAI(GameLogic logic)
	{
		this.logic = logic;
	}

	public Move decide(GameState state)
	{
		problem = new AbaloneSearchProblem(state);
		AbaloneNode startNode = new AbaloneNode(state);

		MinimaxSearch s = new MinimaxSearch(problem);
		System.out.println("My Options: ");
		for(Action a : problem.generateActions(state))
		{
			System.out.println(a);
		}
		SearchNode n = s.search(startNode);
		System.out.println("I want to perform "+n.getAction()+" value: "+((MiniMaxNode)n).getValue());
		return (Move) n.getAction();

	}

	@Override
	public String getName()
	{
		return "Basic Minimax";
	}
}
