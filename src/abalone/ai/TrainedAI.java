package abalone.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import search.Action;
import search.genetics.Gene;
import search.genetics.Genotype;
import search.hashing.SymZobristHashable;
import search.hashing.ZobristHashable;
import search.tree.SearchNode;
import search.tree.games.minimax.MinimaxSearch;
import search.tree.games.minimax.hashing.HashableMiniMaxNode;
import search.tree.games.minimax.hashing.HashingMinimaxSearch;
import abalone.ai.evaluation.LinearEvaluator;
import abalone.ai.machinelearning.Weight;
import abalone.exec.StatisticGenerator;
import abalone.gamelogic.GameLogic;
import abalone.gamestate.GameState;
import abalone.gamestate.ZobristHasher;
import abalone.model.Move;

public class TrainedAI extends Ai implements StatisticGenerator
{
	private static final long serialVersionUID = -448667623469161736L;
	private long startTime;
	private LinearEvaluator evaluator;

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

	public TrainedAI(GameLogic logic)
	{
		this.logic = logic;
		Genotype weights = new Genotype();
		weights.add(0,new Weight(0.37588279040058));
		weights.add(1,new Weight(0.1019650923704891));
		weights.add(2,new Weight(0.0230749110410881));
		weights.add(3,new Weight(0.0251705697187887));
		weights.add(4,new Weight(0.37475564250835874));
		weights.add(5,new Weight(-0.09915099396069534));
		
		evaluator = new LinearEvaluator(weights);
	}

	@Override
	public Move decide(GameState state)
	{
		startTime = System.currentTimeMillis();
		problem = new AbaloneSearchProblem(state, logic);
		AbaloneNode startNode = new AbaloneNode(state);
		
		evaluator.setInitialState(state);
		
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
		return "Trained AI";
	}

	@Override
	public double getCurrentState()
	{
		return System.currentTimeMillis()-startTime;
	}
}
