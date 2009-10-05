package search.tree;

import java.util.ArrayList;

import abalone.model.Move;

import search.Action;

/**
 * A class that extends this abstract class can serve as a SearchNode in a
 * {@link TreeSearch}. The method expand that generates the successor nodes has
 * to be implemented by the child class.
 * 
 * Some ideas for this class are taken from
 * "Artificial Intelligence - A Modern Approach" (Russel, Norvig)
 * 
 * @author Daniel Mescheder
 */
public abstract class SearchNode
{
	// The state that this node refers to
	private SearchState searchState;
	// The parent node which leads to this node
	private SearchNode parent;
	// The price to get to this node from the parent node
	private int stepCost;
	// The price to get to this node from the start node
	private int pathCost;
	// The action that leads to this node
	private Action action;

	/**
	 * Sets the action that leads to this node
	 * 
	 * @param a
	 *            an Action object
	 */
	public void setAction(Action a)
	{
		action = a;
	}

	/**
	 * Creates a new SearchNode object which refers to a given SearchState s and
	 * has the second parameter as its parent.
	 * 
	 * @param s
	 *            The SearchState object which the new node refers to
	 * @param parent
	 *            The SearchNode object which is a parent for the new SearchNode
	 */
	public SearchNode(SearchState s, SearchNode parent)
	{
		searchState = s;
		this.parent = parent;
	}

	/**
	 * Creates a new SearchNode object which refers to a given SearchState s.
	 * 
	 * @param s
	 *            The SearchState object which the new node refers to
	 */
	public SearchNode(SearchState s)
	{
		searchState = s;
		this.parent = null;
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
	 * Expands this node. The return value is a list of nodes that can be
	 * reached from this node by taking appropriate actions.
	 * 
	 * @return an ArrayList of SearchNode objects which are children of this
	 *         node.
	 */
	public abstract ArrayList<SearchNode> expand();

	/**
	 * Gets the price to get from the parent node to this node.
	 * 
	 * @return an integer representing the price
	 */
	public int getStepCost()
	{
		return stepCost;
	}

	/**
	 * Sets the price to get from the parent node to this node
	 * 
	 * @param cost
	 *            an integer representing the price
	 */
	public void setStepCost(int cost)
	{
		this.stepCost = cost;
		this.pathCost = (parent == null) ? stepCost : stepCost
				+ parent.getPathCost();
	}

	/**
	 * Gets the price to get from the start node to this node
	 * 
	 * @return an integer representing the price
	 */
	public int getPathCost()
	{
		return pathCost;
	}

	/**
	 * Generates a formatted string for this node.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String ret = "SearchNode{\nAction leading to node: ";
		ret += (action == null) ? "<?>\n" : "<" + action.getName() + ">\n";
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
	public ArrayList<Action> getActionList()
	{
		ArrayList<Action> a = new ArrayList<Action>();
		if (parent != null)
		{
			a.addAll(parent.getActionList());
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
}
