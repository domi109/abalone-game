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
			
			if(m1.getMarbleLine().getNodes().size() < m2.getMarbleLine().getNodes().size())
			{
				return -1;
			}
			else if(m1.getMarbleLine().getNodes().size() == m2.getMarbleLine().getNodes().size())
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
	}
	
	private class AbaloneNode extends MiniMaxNode
	{
		public AbaloneNode(GameState s)
		{
			super(s);
		}
		
		public AbaloneNode(SearchState s, AbaloneNode parent,Action a) {
			super(s, parent,a);
		}

		@Override
		public Queue<SearchNode> expand()
		{
			PriorityQueue<SearchNode> successors = new PriorityQueue<SearchNode>(10,new MoveComparator());
			for(Action a : problem.generateActions(this.getState()))
			{
				// Every possible action in this state
				
				//copy the current state and apply the action on the state copy
				GameState newState = (GameState) getState().clone();
				logic.applyMove(newState, (Move)a);
				
				// create new node and assign properties
				SearchNode newNode = new AbaloneNode(newState,this,getAction());
				
				// add new node to list
				successors.add(newNode);
			}
			
			return successors;
		}
	}
	
	private class AbaloneSearchProblem implements MinimaxProblem
	{
		/**
		 * This is just a helper to save all this ((GameState) state) typing
		 */
		private GameState gs(SearchState s)
		{
			return (GameState)s;
		}
		
		@Override
		public int getFinalStateValue(SearchState state)
		{
			if(gs(state).equals(logic.getWinner(gs(state))))
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
			
			for(Node n : s.getMarbles(s.getCurrentPlayer()))
			{
				for(Direction d : Direction.UPPER_LEFT)
				{
					if(s.getMarbleOwner(n.getNeighbour(d))==null)
					{
						// found a single marble move
						MarbleLine l = new MarbleLine();
						l.add(n);
						l.setOrientation(d);
						Move m = new Move();
						m.setMarbleLine(l);
						m.setDirection(d);
						singleMarble.add(m);
					}
				}
			}
			
			actions.addAll(singleMarble);
			actions.addAll(inline);
			actions.addAll(broadside);

			return actions;
		}
	}
	
	private GameLogic logic;
	private AbaloneSearchProblem problem;
	
	public Move decide(GameState state)
	{
		AbaloneSearchProblem problem = new AbaloneSearchProblem();
		AbaloneNode startNode = new AbaloneNode(state);
		
		MinimaxSearch s = new MinimaxSearch(problem);
		SearchNode n = s.search(startNode);
				
		return (Move) n.getAction();

	}

	@Override
	public String getName()
	{
		return "Basic Minimax";
	}
}
