package search.tree;

import java.util.List;
import java.util.Queue;
import java.util.Set;

import search.Action;

/**
 * A general interface for search problems. A search problem as represented by
 * this interface consists of a goal test, an abortion rule and a method to
 * generate possible actions in a state. This interface uses the
 * {@link SearchState} class and the {@link SearchNode} class. Classes
 * implementing this interface can be used within a {@link TreeSearch}.
 * 
 * Some ideas for this interface are taken from
 * "Artificial Intelligence - A Modern Approach" (Russel, Norvig)
 * 
 * @author Daniel Mescheder
 */
public interface SearchProblem
{
	/**
	 * Checks whether a given state is a goal state. The implementing class is
	 * free to decide whether this shall happen due to a certain criterion or
	 * whether complete equality to a certain state is required.
	 * 
	 * @param state
	 *            This state will be checked.
	 * @return true if the given state is a goal state, false if it is not.
	 */
	public boolean goalTest(SearchState state);

	/**
	 * Checks whether a given node matches the abortion criterion. This
	 * criterion can for instance be a certain depth in the search tree.
	 * 
	 * @param node
	 *            This node will be checked
	 * @return true if the given node matches the abortion criterion, false if
	 *         it does not.
	 */
	public boolean breakTest(SearchNode node);

	/**
	 * Generates a list of actions given a certain state.
	 * 
	 * @return List of actions
	 */
	public List<Action> generateActions(SearchState state);
}
