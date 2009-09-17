package search.tree;

import java.util.ArrayList;


/**
 * A class to execute a basic tree search. If it gets a properly defined search
 * problem and a start node, it will start searching for a goal node. The
 * implementations of these methods are very basic. This class is rather meant
 * to be extended by more intelligent approaches.
 * 
 * Some ideas for this class are taken from "Artificial Intelligence - A Modern Approach" (Russel, Norvig).
 * 
 * @author Daniel Mescheder
 * 
 */
public class TreeSearch
{
	// A search problem
	private SearchProblem problem;

	/**
	 * A method that is called to choose the next leaf node that shall be
	 * expanded. In this implementation, just the first element of the fringe is
	 * returned.
	 * 
	 * @param fringe
	 *            the ArrayList that holds all the candidates for expansion.
	 * @return the selected node.
	 */
	protected SearchNode chooseLeafNode(ArrayList<SearchNode> fringe)
	{
		return fringe.get(0);
	}

	/**
	 * Creates a new TreeSearch object for a given search problem
	 * 
	 * @param t
	 *            the search problem
	 */
	public TreeSearch(SearchProblem t)
	{
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
			// choose a leaf node
			node = chooseLeafNode(fringe);

			// remove the chosen node from the fringe
			fringe.remove(node);
			// expand the chosen node and add the result to the fringe
			fringe.addAll(node.expand());
		}
		return null;
	}
}
