package search.tree.exhaustive;

import java.util.ArrayList;

import search.tree.SearchNode;
import search.tree.SearchProblem;
import search.tree.TreeSearch;

/**
 * An exhaustive search that always expands the first node of the fringe.
 * Advantages:
 * <ul>
 * <li>There's no information about the search space needed</li>
 * <li>The memory consumption is rather low</li>
 * </ul>
 * Disadvantages:
 * <ul>
 * <li>This search is not complete in an infinite search tree</li>
 * <li>This search is not optimal</li>
 * <li>In an exponential search tree this will probably take very long</li>
 * </ul>
 * 
 * @author Daniel Mescheder
 */
public class DepthFirstSearch extends TreeSearch
{
	/**
	 * Create a new DepthFirst search for a general search problem t.
	 * 
	 * @param t
	 *            The search problem to work with. This e.g. defines the goal
	 *            test!
	 */
	public DepthFirstSearch(SearchProblem t)
	{
		super(t);
	}
	
	protected SearchNode chooseLeafNode(ArrayList<SearchNode> fringe)
	{
		return fringe.get(fringe.size()-1);
	}
}
