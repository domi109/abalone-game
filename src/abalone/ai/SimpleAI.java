package abalone.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import search.Action;
import search.hashing.SymZobristHashable;
import search.hashing.ZobristHashable;
import search.tree.SearchNode;
import search.tree.games.minimax.MinimaxSearch;
import search.tree.games.minimax.hashing.HashableMiniMaxNode;
import search.tree.heuristic.Evaluator;
import abalone.ai.machinelearning.AbaloneIndividual;
import abalone.gamelogic.GameLogic;
import abalone.gamestate.GameState;
import abalone.gamestate.ZobristHasher;
import abalone.model.Move;

public class SimpleAI extends Ai
{
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
			Queue<SearchNode> successors = new LinkedList<SearchNode>();
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
	private AbaloneIndividual evaluator;

	public SimpleAI(GameLogic logic, Evaluator evaluator)
	{
		this.logic = logic;
		this.evaluator = (AbaloneIndividual) evaluator;
	}

	public SimpleAI(GameLogic logic)
	{
		this.logic = logic;
	}

	@Override
	public Move decide(GameState state)
	{
		problem = new AbaloneSearchProblem(state, logic);
		AbaloneNode startNode = new AbaloneNode(state);

		evaluator.setInitialState(state);

		int PlyLevels = 1;

		MinimaxSearch s = new MinimaxSearch(problem, evaluator, PlyLevels);

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
