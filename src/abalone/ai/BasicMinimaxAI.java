package abalone.ai;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import search.Action;
import search.tree.SearchNode;
import search.tree.games.minimax.MiniMaxNode;
import search.tree.games.minimax.MinimaxSearch;
import search.tree.games.minimax.hashing.HashableMiniMaxNode;
import search.tree.games.minimax.hashing.HashingMinimaxSearch;
import search.tree.heuristic.Evaluator;
import abalone.gamelogic.GameLogic;
import abalone.gamestate.GameState;
import abalone.model.Move;

public class BasicMinimaxAI extends Ai
{
	private static final long serialVersionUID = -448667623469161736L;


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
			// System.out.println(actions.size());
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
		problem = new AbaloneSearchProblem(state, logic);
		AbaloneNode startNode = new AbaloneNode(state);
		//Evaluator<Double> evaluator = new OptimizedLinearEvaluator(state);
		Evaluator<Double> evaluator = new SimpleEvaluator(state);

		int PlyLevels = 8;

		MinimaxSearch s = new HashingMinimaxSearch(problem, evaluator, PlyLevels);
//		System.out.println("My Options: ");
//		for (Action a : problem.generateActions(state))
//		{
//			System.out.println(a);
//		}
		long time = System.currentTimeMillis();
		SearchNode n = s.search(startNode);
		System.out.println("[" + (System.currentTimeMillis() - time) + "ms] I want to perform " + n.getAction() + " value: " + ((MiniMaxNode) n).getValue());
		return (Move) n.getAction();

	}

	@Override
	public String getName()
	{
		return "Basic Minimax";
	}
}