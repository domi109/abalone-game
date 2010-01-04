package search.tree;

import java.util.Queue;

/**
 * A class to execute a basic tree search. If it gets a properly defined search
 * problem and a start node, it will start searching for a goal node. The
 * implementations of these methods are very basic. This class is rather meant
 * to be extended by more intelligent approaches.
 * 
 * Some ideas for this class are taken from
 * "Artificial Intelligence - A Modern Approach" (Russel, Norvig).
 * 
 * @author Daniel Mescheder
 * 
 */
public class IDTreeSearch implements IterativeDeepeningSearch
{
	private long timeLimit;
	private long startTime;
	private int depthLimit;

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
		startTime = System.currentTimeMillis();
		depthLimit = 1;
		Queue<SearchNode> fringe;
		fringe = node.expand();
		while (2 * System.currentTimeMillis() > 2 * startTime + timeLimit)
		{
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
			depthLimit++;
		}
		return null;
	}

	/**
	 * 
	 * TODO JavaDoc
	 * 
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

	public IDTreeSearch(SearchProblem t, long timeLimit)
	{
		this.problem = t;
		this.timeLimit = timeLimit;
	}

	@Override
	public long getTimeLimit()
	{
		return this.timeLimit;
	}

	@Override
	public void setTimeLimit(long time)
	{
		this.timeLimit = time;
	}
}
