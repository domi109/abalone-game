package search.tree.games.minimax;

import java.util.Queue;

import search.tree.SearchNode;
import search.tree.SearchState;
import search.tree.TreeSearch;

/**
 * A basic minimax search that tries to maximize the game result (Minimax-Value)
 * given a start node.
 * 
 * The minimax value of a node is the utility (for the player "max") of being in
 * the corresponding state, assuming that both players play optimally from there
 * to the end of the game.
 * 
 * This class already applies alpha-beta pruning as a simple but efficient
 * strategy to reduce running time of this algorithm.
 * 
 * Refer to: Russel, Norvig - Artificial Intelligence a Modern Approach pp. 161
 * 
 * @author Daniel Mescheder
 * 
 */
public class MinimaxSearch extends TreeSearch
{
	// a local variable that points to the search problem object
	protected MinimaxProblem problem;

	/**
	 * Construct a new minimax search given a minimax search problem. The search
	 * problem describes the settings of the search.
	 * 
	 * Run "search" to actually run the search.
	 * 
	 * @param t
	 *            the minimax search problem that contains the search settings
	 */
	public MinimaxSearch(MinimaxProblem t)
	{
		super(t);
		this.problem = t;
	}

	/**
	 * Executes the search starting from a given start node
	 * 
	 * @param node
	 *            the startnode of the search
	 * @return the node of the next level for which max should decide in order
	 *         to win the game
	 */
	@Override
	public SearchNode search(SearchNode node)
	{
		// We assume that max executes the search.
		// Therefore we're interested in finding the max node from here on.
		SearchNode n = maxNode((MiniMaxNode) node, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		return n;
	}

	/**
	 * Recursively finds the maximal child of the passed node
	 * 
	 * @param node
	 *            the node to investigate on
	 * @param alpha
	 *            the highest value we have found so far on the path leading to
	 *            this node - i.e. the best choice there is for max so far.
	 * @param beta
	 *            the lowest value we have found so far on the path leading to
	 *            this node - i.e. the best choice there is for min so far.
	 * 
	 * @return the maximal child of the node.
	 */
	private MiniMaxNode maxNode(MiniMaxNode node, double alpha, double beta)
	{
		if (problem.breakTest(node))
		{
			// The problem says that the search should be
			// aborted here (e.g. as it already took too long...)
			// so it's time to get some default value and return.
			node.setValue(breakValue(node.getState()));
			return node;
		}
		if (problem.goalTest(node.getState()))
		{
			// Voila, we really found a goal state!!
			// So here's the plan: get the value of the final
			// state (win for min/win for max/draw/...?)
			// and return the node with this value.
			node.setValue(problem.getFinalStateValue(node.getState()));
			return node;
		}

		// Ok, this seems to be just a regular node, so we will
		// expand it and find the maximal child

		// We store the maximal child in v:
		MiniMaxNode v = null;
		for (SearchNode n : node.expand())
		{
			// For every successor node

			// Get minimal child node of this successor
			MiniMaxNode min = minNode((MiniMaxNode) n, alpha, beta);

			if (v == null || v.getValue() < min.getValue())
			{
				// Either we haven't found any node at all yet, or we found
				// one who will eventually lead to a higher outcome.
				// Anyway: store it in v!
				v = (MiniMaxNode) n;
				v.setValue(min.getValue());

			}
			if (v.getValue() >= beta)
			{
				// This node we're investigating on is worth
				// at least the value of v.
				// But as beta (the lowest value on the path so far)
				// appears to be smaller than that,
				// min would never choose this node!
				// therefore it does not make any sense to further
				// look at any successors.
				return v;
			}
			// Now we know that the highest node on the path so far
			// is max(alpha,v).
			// We can take this into account for further iterations.
			alpha = Math.max(alpha, v.getValue());
		}
		return v;
	}

	/**
	 * Recursively finds the minimal child of the passed node
	 * 
	 * @param node
	 *            the node to investigate on
	 * @param alpha
	 *            the highest value we have found so far on the path leading to
	 *            this node - i.e. the best choice there is for max so far.
	 * @param beta
	 *            the lowest value we have found so far on the path leading to
	 *            this node - i.e. the best choice there is for min so far.
	 * 
	 * @return the minimal child of the node.
	 */
	private MiniMaxNode minNode(MiniMaxNode node, double alpha, double beta)
	{
		if (problem.breakTest(node))
		{
			// The problem says that the search should be
			// aborted here (e.g. as it already took too long...)
			// so it's time to get some default value and return.
			node.setValue(breakValue(node.getState()));
			return node;
		}
		if (problem.goalTest(node.getState()))
		{
			// Voila, we really found a goal state!!
			// So here's the plan: get the value of the final
			// state (win for min/win for max/draw/...?)
			// and return the node with this value.
			node.setValue(problem.getFinalStateValue(node.getState()));
			return node;
		}
		
		// Ok, this seems to be just a regular node, so we will
		// expand it and find the minimal child

		// We store the minimal child in v:
		MiniMaxNode v = null;
		for (SearchNode n : node.expand())
		{
			// For every successor node
			// Get maximal child node of this successor
			MiniMaxNode max = maxNode((MiniMaxNode) n, alpha, beta);
			if (v == null || v.getValue() > max.getValue())
			{
				// Either we haven't found any node at all yet, or we found
				// one who will eventually lead to a lower outcome.
				// Anyway: store it in v!
				v = (MiniMaxNode) n;
				v.setValue(max.getValue());
			}
			if (v.getValue() <= alpha)
			{
				// This node we're investigating on is worth
				// at most the value of v.
				// But as alpha (the highest value on the path so far)
				// appears to be greater than that,
				// max would never choose this node!
				// therefore it does not make any sense to further
				// look at any successors.
				return v;
			}
			// Now we know that the lowest node on the path so far
			// is min(beta,v).
			// We can take this into account for further iterations.
			beta = Math.min(beta, v.getValue());
		}
		return v;

	}

	protected double breakValue(SearchState state)
	{
		// In a real application we probably want to
		// return some kind of estimation here.
		return 0;
	}

}
