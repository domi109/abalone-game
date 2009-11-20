package abalone.ai;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import search.Action;
import search.tree.SearchNode;
import search.tree.games.minimax.MiniMaxNode;
import search.tree.games.minimax.MinimaxSearch;
import search.tree.games.minimax.hashing.HashableMiniMaxNode;
import search.tree.games.minimax.hashing.HashingMinimaxSearch;
import abalone.gamelogic.GameLogic;
import abalone.gamestate.GameState;
import abalone.model.Move;

public class BasicMinimaxAI extends Ai
{
	private static final long serialVersionUID = -448667623469161736L;

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

	private class AbaloneNode extends HashableMiniMaxNode
	{
		private static final long serialVersionUID = -6277809797290009239L;

		public AbaloneNode(GameState s)
		{
			super(s);
		}

		public AbaloneNode(GameState s, AbaloneNode parent, Action a)
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


	private GameLogic logic;
	private AbaloneSearchProblem problem;

	public BasicMinimaxAI(GameLogic logic)
	{
		this.logic = logic;
	}

	@Override
	public Move decide(GameState state)
	{
		problem = new AbaloneSearchProblem(state,logic);
		AbaloneNode startNode = new AbaloneNode(state);
                LinearEvaluator evaluated = new LinearEvaluator(state);
                int PlyLevels = 5;

		MinimaxSearch s = new HashingMinimaxSearch(problem,evaluated,PlyLevels);
//		System.out.println("My Options: ");
//		for(Action a : problem.generateActions(state))
//		{
//			System.out.println(a);
//		}
		long time = System.currentTimeMillis();
		SearchNode n = s.search(startNode);
		System.out.println("["+(System.currentTimeMillis()-time)+"ms] I want to perform "+n.getAction()+" value: "+((MiniMaxNode)n).getValue());
                evaluated.eval(n.getState());
                System.out.println("F1 is "+evaluated.getF1()+" "+"F2 is "+evaluated.getF2()+" "+"F3 is "+evaluated.getF3()+" "+"F4 is "+evaluated.getF4()+" "+"F5 is "+evaluated.getF5()+" "+"F6 is "+evaluated.getF6());
		return (Move) n.getAction();

	}

	@Override
	public String getName()
	{
		return "Basic Minimax";
	}
}
