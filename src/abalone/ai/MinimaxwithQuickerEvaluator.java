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
import abalone.ai.evaluation.QuickerLinearEvaluator;
import abalone.ai.machinelearning.Weight;
import abalone.gamelogic.GameLogic;
import abalone.gamestate.GameState;
import abalone.model.Move;
import abalone.statistics.StatisticGenerator;
import nl.maastrichtuniversity.dke.libreason.impl.treesearch.MinimaxRandomizer;

public class MinimaxwithQuickerEvaluator extends Ai implements StatisticGenerator
{
	private static final long serialVersionUID = -448667623469161736L;
	private long startTime;

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
			return ((SymmetryHashable) getState()).getSymmetryHashes();
		}

		@Override
		public long getHash()
		{
			return ((Hashable) getState()).getHash();
		}
	}

	private GameLogic logic;
	private AbaloneSearchProblem problem;

	public MinimaxwithQuickerEvaluator(GameLogic logic)
	{
		this.logic = logic;
	}

	@Override
	public Move decide(GameState state) throws InterruptedException
	{
		startTime = System.currentTimeMillis();
		problem = new AbaloneSearchProblem(state, logic);
		AbaloneNode startNode = new AbaloneNode(state);
		Genotype weights = new Genotype();

		weights.add(0,new Weight(0.05));
		weights.add(1,new Weight(0.2));
                weights.add(2, new Weight(-0.2));

		QuickerLinearEvaluator evaluator = new QuickerLinearEvaluator(weights);
		evaluator.setInitialState(state);

		IterativeDeepeningSearch<AbaloneNode> s =
			new IDTreeSearch<AbaloneNode>(
                            new MinimaxRandomizer<AbaloneNode>(
					new SymmetricHashingMinimaxSearch<AbaloneNode>(
						new AlphaBetaSearch<AbaloneNode>(
							new DLMinimax<AbaloneNode>(problem, evaluator, 1)))),5000);


		long time = System.currentTimeMillis();
		AbaloneNode n = s.search(startNode);
		time = System.currentTimeMillis() - time;

		System.out.println("Player: Minimax & QuickerEvaluator     evaluation: " + n.getValue() + " time: "+time+" depth: "+s.getSearchStrategy().getDepthLimit());

		return (Move) n.getAction();

	}

	@Override
	public String getName()
	{
		return "Minimax & QuickerEvaluator";
	}

	@Override
	public double getCurrentState()
	{
		return System.currentTimeMillis() - startTime;
	}
}
