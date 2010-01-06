package abalone.ai;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import search.Action;
import search.genetics.Genotype;
import search.hashing.SymZobristHashable;
import search.hashing.ZobristHashable;
import search.tree.SearchNode;
import search.tree.SearchState;
import search.tree.games.minimax.MinimaxSearch;
import search.tree.games.minimax.hashing.HashableMiniMaxNode;
import search.tree.games.minimax.hashing.HashingMinimaxSearch;
import search.tree.heuristic.Evaluator;
import abalone.ai.evaluation.LinearEvaluator;
import abalone.ai.machinelearning.Weight;
import abalone.exec.StatisticGenerator;
import abalone.gamelogic.GameLogic;
import abalone.gamestate.GameState;
import abalone.gamestate.ZobristHasher;
import abalone.model.Move;

public class BasicMinimaxAI extends Ai implements StatisticGenerator
{
	private static final long serialVersionUID = -448667623469161736L;
	private long startTime;

	private class AbaloneNode extends HashableMiniMaxNode implements SymZobristHashable
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

		@Override
		public long[] symmetryHashes()
		{
			return ZobristHasher.getSymmetries(((ZobristHashable) getState()).zobristHash());
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
		startTime = System.currentTimeMillis();
		problem = new AbaloneSearchProblem(state, logic);
		AbaloneNode startNode = new AbaloneNode(state);
		Genotype weights = new Genotype();
		weights.add(0,new Weight(0.05));
		weights.add(1,new Weight(0.05));
		weights.add(2,new Weight(0.1));
		weights.add(3,new Weight(0.1));
		weights.add(4,new Weight(0.1));
		weights.add(5,new Weight(-0.1));
		LinearEvaluator evaluator = new LinearEvaluator(weights);
		evaluator.setInitialState(state);
		// Evaluator<Double> evaluator = new SimpleEvaluator(state);

		int PlyLevels = 3;

		MinimaxSearch s = new HashingMinimaxSearch(problem, evaluator, PlyLevels);
		
		Queue<SearchNode> q = s.getChildren(startNode);
		if (Math.random() < .9)
		{
			return (Move) q.remove().getAction();
		}
		q.remove();
		return (Move) q.remove().getAction();

	}

	@Override
	public String getName()
	{
		return "Basic Minimax";
	}

	@Override
	public double getCurrentState()
	{
		return System.currentTimeMillis() - startTime;
	}
}
