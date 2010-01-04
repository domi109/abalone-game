package search.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;

import search.Action;

/**
 * A class that extends this abstract class can serve as a SearchNode in a
 * {@link IDTreeSearch}. The method expand that generates the successor nodes has
 * to be implemented by the child class.
 * 
 * Some ideas for this class are taken from
 * "Artificial Intelligence - A Modern Approach" (Russel, Norvig)
 * 
 * @author Daniel Mescheder
 */
public abstract class SearchNode implements Serializable
{
	private static final long serialVersionUID = -3043848397791138974L;
	// The state that this node refers to
	private SearchState searchState;
	// The parent node which leads to this node
	private SearchNode parent;
	// The price to get to this node from the start node
	private double pathCost;
	// The action that leads to this node
	private Action action;
	private int depth;

	/**
	 * Creates a new SearchNode object which refers to a given SearchState s and
	 * has the second parameter as its parent.
	 * 
	 * @param s
	 *            The SearchState object which the new node refers to
	 * @param parent
	 *            The SearchNode object which is a parent for the new SearchNode
	 * @param a The action that leads to this state
	 */
	public SearchNode(SearchState s, SearchNode parent, Action a)
	{
		this.searchState = s;
		this.parent = parent;
		this.action = a;
		this.pathCost = parent.pathCost + a.getCost();
		this.depth = parent.depth + 1;
	}

	/**
	 * Creates a new SearchNode object which refers to a given SearchState s.
	 * 
	 * @param s
	 *            The SearchState object which the new node refers to
	 */
	public SearchNode(SearchState s)
	{
		this.searchState = s;
		this.parent = null;
		this.pathCost = 0;
		this.action = null;
		this.depth = 0;
	}

	/**
	 * Gets the search state this node refers to
	 * 
	 * @return a SearchState object
	 */
	public SearchState getState()
	{
		return searchState;
	}

	/**
	 * Expands this node. The return value is a queue of nodes that can be
	 * reached from this node by taking appropriate actions.
	 * The implementation of the queue determines the search strategy.
	 * 
	 * @return an Queue of SearchNode objects which are children of this
	 *         node.
	 */
	public abstract Queue<SearchNode> expand();

	/**
	 * Gets the price to get from the start node to this node
	 * 
	 * @return a double representing the price
	 */
	public double getPathCost()
	{
		return pathCost;
	}

	/**
	 * Generates a formatted string for this node.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String ret = "SearchNode{\nAction leading to node: ";
		ret += (action == null) ? "<?>\n" :action.toString() + "\n";
		ret += searchState.toString();
		ret += "\n}";

		return ret;

	}

	/**
	 * Generates a list of actions that lead to this node. This method can be
	 * used to trace the path to a goal node after a search was finished.
	 * 
	 * @return an ArrayList of Action objects
	 */
	public ArrayList<Action> getPath()
	{
		ArrayList<Action> a = new ArrayList<Action>();
		if (parent != null)
		{
			a.addAll(parent.getPath());
		}
		if (action != null)
		{
			a.add(action);
		}
		return a;
		
	}

	public Action getAction()
	{
		return this.action;
	}
	
	public SearchNode getParent()
	{
		return parent;
	}
	
	public int getDepth()
	{
		return depth;
	}
}
