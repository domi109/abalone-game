package search.optimization.tree;

import java.util.ArrayList;

import search.tree.SearchNode;
import search.tree.exhaustive.DepthFirstSearch;

public class OptimizationDepthFirstSearch extends DepthFirstSearch
{
	private OptimizationProblem problem;
	
	public OptimizationDepthFirstSearch(OptimizationProblem t)
	{
		super(t);
		this.problem = t;
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
				// the goal test matches - a goal has been found, return it!
				return node;
			}
			if (problem.breakTest(node))
			{
				// the break test matches - we should not search any further
				// here. Just remove this node from the fringe and go on!
				fringe.remove(node);
				break;
			}
			problem.registerNode(node);
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
