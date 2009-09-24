package search.tree.minimax;

import java.util.ArrayList;

import search.tree.SearchNode;
import search.tree.exhaustive.DepthFirstSearch;
import search.tree.optimization.OptimizationDepthFirstSearch;
import search.tree.optimization.OptimizationProblem;

public class MinimaxSearch extends OptimizationDepthFirstSearch
{
	private OptimizationProblem problem;
	
	public MinimaxSearch(OptimizationProblem t)
	{
		super(t);
	}
	
	/**
	 * Executes the search starting from a given start node
	 * 
	 * @param node
	 *            the startnode of the search
	 * @return the goal node or null if nothing was found
	 */
	public SearchNode search(SearchNode node)
	{
		ArrayList<SearchNode> fringe;
		fringe = node.expand();
		while (!fringe.isEmpty())
		{
			// as long as there are still nodes that can be expanded
			if (problem.goalTest(node.getState()))
			{
				// the goal test matches - the game would be over here.
				// register the node.
				problem.registerNode(node);
				fringe.remove(node);
				break;
			}
			if (problem.breakTest(node))
			{
				// the break test matches - we should not search any further
				// here.
				problem.registerNode(node);
				fringe.remove(node);
				break;
			}
			// choose a leaf node
			node = chooseLeafNode(fringe);
			// remove the chosen node from the fringe
			fringe.remove(node);
			// expand the chosen node and add the result to the fringe
			fringe.addAll(node.expand());
		}
		return problem.getBest();
	}

}
