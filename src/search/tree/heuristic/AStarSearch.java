package search.tree.heuristic;

import java.util.ArrayList;

import search.tree.SearchNode;
import search.tree.TreeSearch;

/**
 * A search based on a heuristic. The idea is, that a node is only expanded, if
 * the sum of the path cost (to reach this node) and the estimated distance from
 * the goal (heuristic) is minimal in the fringe. That also means that the
 * success of this search pretty much depends on the quality of the heuristic
 * defined in the {@link HeuristicSearchProblem} object used. If the heuristic
 * is admissible, this search is optimal. As long as there are no zero cost
 * branches in the search space the algorithm also is complete. It may still be,
 * given a very vague heuristic, that the search boils down to a breadth first
 * search an therefore consumes a lot of memory space.
 * 
 * Some ideas for this class are taken from "Artificial Intelligence - A Modern Approach" (Russel, Norvig) 
 * 
 * @author Daniel Mescheder
 */
public class AStarSearch extends TreeSearch
{
	private HeuristicSearchProblem problem;

	/**
	 * Create a new AStarSearch for a general heuristic search problem t.
	 * 
	 * @param t
	 *            The search problem to work with. This e.g. defines the goal
	 *            test and the heuristic.
	 */
	public AStarSearch(HeuristicSearchProblem t)
	{
		super(t);
		problem = t;
	}

	/**
	 * A method that is called to choose the next leaf node that shall be
	 * expanded. A node is only expanded, if the sum of the path cost (to reach
	 * this node) and the estimated distance from the goal (heuristic) is
	 * minimal in the fringe.
	 * 
	 * @param fringe
	 *            the ArrayList that holds all the candidates for expansion.
	 * @return the selected node.
	 */
	protected SearchNode chooseLeafNode(ArrayList<SearchNode> fringe)
	{
		SearchNode minNode = null;
		int minCost = Integer.MAX_VALUE;
		int cost;

		for (SearchNode n : fringe)
		{
			// go through all elements in the fringe

			// calculate the cost: g(n) + h(n)
			// with pathcost g and heuristic h
			cost = n.getPathCost() + problem.estimatedDistance(n.getState());
			if (cost < minCost)
			{
				// the cost is smaller of the current minimum. Thus we have a
				// new minimum!
				minCost = cost;
				minNode = n;
			}
		}
		return minNode;
	}

}
