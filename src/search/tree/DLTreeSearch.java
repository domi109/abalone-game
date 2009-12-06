package search.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;


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
public class DLTreeSearch implements DepthLimitedSearch
{

	private static final long serialVersionUID = 1933371370782723235L;
	protected int depthLimit;
	// A search problem
	private SearchProblem problem;

	/**
	 * Executes the search starting from a given start node
	 * 
	 * @param node
	 *            the startnode of the search
	 * @return the goal node or null if nothing was found
	 */
	public SearchNode search(SearchNode node)
	{
		Queue<SearchNode> fringe;
		fringe = node.expand();
		while (!fringe.isEmpty())
		{
			// as long as there are still nodes that can be expanded
			if (problem.goalTest(node.getState()))
			{
				// the goal test matches - a goal has been found, return it!
				return node;
			}
			if (this.breakTest(node))
			{
				// the break test matches - we should not search any further
				// here. Just remove this node from the fringe and go on!
				fringe.remove(node);
				break;
			}
			// choose a leaf node
			node = fringe.remove();

			// expand the chosen node and add the result to the fringe
			fringe.addAll(node.expand());
		}
		return null;
	}
	
	/**
	 * 
	 * TODO JavaDoc
	 * @return
	 */
	public SearchProblem getProblem()
	{
		return problem;
	}
	
	protected boolean breakTest(SearchNode node)
	{
		return node.getDepth() >= depthLimit;
	}

	public DLTreeSearch(SearchProblem t, int limit)
	{
		this.problem = t;
		this.depthLimit = limit;
	}

	@Override
	public int getDepthLimit()
	{
		return this.depthLimit;
	}

	@Override
	public void setDepthLimit(int limit)
	{
		this.depthLimit = limit;
	}
}
