package abalone.ai;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import nl.maastrichtuniversity.dke.libreason.def.Action;
import nl.maastrichtuniversity.dke.libreason.def.hashing.Hashable;
import nl.maastrichtuniversity.dke.libreason.def.hashing.SymmetryHashable;
import nl.maastrichtuniversity.dke.libreason.def.treesearch.IterativeDeepeningSearch;
import nl.maastrichtuniversity.dke.libreason.def.treesearch.SearchNode;
import nl.maastrichtuniversity.dke.libreason.genetics.Genotype;
import nl.maastrichtuniversity.dke.libreason.impl.treesearch.AbstractMinimaxNode;
import nl.maastrichtuniversity.dke.libreason.impl.treesearch.AlphaBetaSearch;
import nl.maastrichtuniversity.dke.libreason.impl.treesearch.DLMinimax;
import nl.maastrichtuniversity.dke.libreason.impl.treesearch.IDTreeSearch;
import nl.maastrichtuniversity.dke.libreason.impl.treesearch.SymmetricHashingMinimaxSearch;
import abalone.ai.evaluation.LinearEvaluator;
import abalone.ai.machinelearning.Weight;
import abalone.gamelogic.GameLogic;
import abalone.gamestate.GameState;
import abalone.gamestate.ZobristHasher;
import abalone.model.Move;
import abalone.statistics.StatisticGenerator;

public class TrainedAI extends Ai implements StatisticGenerator
{
	private static final long serialVersionUID = -448667623469161736L;
	private long startTime;
	private LinearEvaluator evaluator;

	private class AbaloneNode extends AbstractMinimaxNode implements SymmetryHashable
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
		public long[] getSymmetryHashes()
		{
			return ZobristHasher.getSymmetries(((Hashable) getState()).getHash());
		}

		@Override
		public long getHash()
		{
			return ((Hashable) getState()).getHash();
		}
	}

	private GameLogic logic;
	private AbaloneSearchProblem problem;

	public TrainedAI(GameLogic logic)
	{
		this.logic = logic;
		Genotype weights = new Genotype();
		weights.add(0, new Weight(0.2539843191779305));
		weights.add(1, new Weight(0.0742744140171492));
		weights.add(2, new Weight(0.013942534940270314));
		weights.add(3, new Weight(0.0750486033854891));
		weights.add(4, new Weight(0.1933802758945122));
		weights.add(5, new Weight(-0.3893698525846486));

		evaluator = new LinearEvaluator(weights);
	}

	@Override
	public Move decide(GameState state) throws InterruptedException
	{
		startTime = System.currentTimeMillis();
		problem = new AbaloneSearchProblem(state, logic);
		AbaloneNode startNode = new AbaloneNode(state);

		evaluator.setInitialState(state);

		IterativeDeepeningSearch<AbaloneNode> s = new IDTreeSearch<AbaloneNode>(new SymmetricHashingMinimaxSearch<AbaloneNode>(
				new AlphaBetaSearch<AbaloneNode>(new DLMinimax<AbaloneNode>(problem, evaluator, 1))), 2000);

		SearchNode n = s.search(startNode);

		return (Move) n.getAction();
	}

	@Override
	public String getName()
	{
		return "Trained AI";
	}

	@Override
	public double getCurrentState()
	{
		return System.currentTimeMillis() - startTime;
	}
}
