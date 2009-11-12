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
public class DepthLimitedTreeSearch extends TreeSearch
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1933371370782723235L;
	protected int depthLimit;
	
	@Override
	protected boolean breakTest(SearchNode node)
	{
		return node.getDepth() >= depthLimit;
	}

	public DepthLimitedTreeSearch(SearchProblem t, int limit)
	{
		super(t);
		this.depthLimit = limit;
	}
}
