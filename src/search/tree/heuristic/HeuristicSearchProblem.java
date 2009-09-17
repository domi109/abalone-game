package search.tree.heuristic;

import search.tree.SearchNode;
import search.tree.SearchProblem;
import search.tree.SearchState;

/**
 * A interface for search problems that include a heuristic. A search problem as
 * represented by this interface consists of a goal test, an abortion rule and a
 * function that estimates the distance of a given node from a goal state. This
 * interface uses the {@link SearchState} class and the {@link SearchNode}
 * class. Classes implementing this interface can be used for example within an
 * {@link AStarSearch}.
 * 
 * Some ideas for this interface are taken from
 * "Artificial Intelligence - A Modern Approach" (Russel, Norvig)
 * 
 * @author Daniel Mescheder
 */
public interface HeuristicSearchProblem extends SearchProblem
{
	/**
	 * Heuristic function to estimate the distance of a state from the goal.
	 * 
	 * @param state the state for which this function generates an estimation.
	 * @return an integer value indicating the distance.
	 */
	public int estimatedDistance(SearchState state);
}
