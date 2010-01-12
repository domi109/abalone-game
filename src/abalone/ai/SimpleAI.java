package abalone.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.maastrichtuniversity.dke.libreason.def.Action;
import nl.maastrichtuniversity.dke.libreason.def.hashing.Hashable;
import nl.maastrichtuniversity.dke.libreason.def.hashing.SymmetryHashable;
import nl.maastrichtuniversity.dke.libreason.def.heuristic.Evaluator;
import nl.maastrichtuniversity.dke.libreason.def.treesearch.AbstractMinimaxSearch;
import nl.maastrichtuniversity.dke.libreason.def.treesearch.SearchNode;
import nl.maastrichtuniversity.dke.libreason.impl.treesearch.AbstractMinimaxNode;
import nl.maastrichtuniversity.dke.libreason.impl.treesearch.AlphaBetaSearch;
import nl.maastrichtuniversity.dke.libreason.impl.treesearch.DLMinimax;
import abalone.ai.evaluation.LinearEvaluator;
import abalone.gamelogic.GameLogic;
import abalone.gamestate.GameState;
import abalone.gamestate.ZobristHasher;
import abalone.model.Move;

public class SimpleAI extends Ai
{
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
			Queue<SearchNode> successors = new LinkedList<SearchNode>();
			List<Action> actions = problem.generateActions(this.getState());
			// System.out.println(actions.size());
			for (Action a : actions)
			{
				// Every possible action in this state

				// copy the current state and apply the action on the state copy
				GameState newState = (GameState) getState().clone();

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
	private LinearEvaluator evaluator;

	public SimpleAI(GameLogic logic, Evaluator<Double> evaluator)
	{
		this.logic = logic;
		this.evaluator = (LinearEvaluator) evaluator;
	}

	public SimpleAI(GameLogic logic)
	{
		this.logic = logic;
	}

	@Override
	public Move decide(GameState state) throws InterruptedException
	{
		problem = new AbaloneSearchProblem(state, logic);
		AbaloneNode startNode = new AbaloneNode(state);
		evaluator.setInitialState(state);

		int plyLevels = 2;

		AbstractMinimaxSearch<AbaloneNode> s = new AlphaBetaSearch<AbaloneNode>(new DLMinimax<AbaloneNode>(problem,evaluator,plyLevels));
		
		SearchNode n = s.search(startNode);
		
		return (Move) n.getAction();
	}

	@Override
	public String getName()
	{
		return "Simple Minimax";
	}

	public Evaluator<Double> getEvaluator()
	{
		return evaluator;
	}
}
