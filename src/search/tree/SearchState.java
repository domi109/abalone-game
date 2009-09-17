package search.tree;

import java.util.Collection;

import search.Action;

/**
 * If a class implements this interface, it can be part of a
 * {@link SearchProblem} which can - for example - be worked on by a
 * {@link TreeSearch}. A SearchState is usually referenced by a
 * {@link SearchNode}
 * 
 * Some ideas for this interface are taken from "Artificial Intelligence - A Modern Approach" (Russel, Norvig).
 * 
 * @author Daniel Mescheder
 */
public interface SearchState
{
	/**
	 * Checks whether this state equals a given second state in regard of a
	 * certain criterion
	 * 
	 * @param state
	 *            the state that shall be compared to the object
	 * @return true if the two states are equal, false if they're not
	 */
	public boolean equalState(SearchState state);

	/**
	 * Call this method to get a collection of actions possible at this state.
	 * 
	 * @return a Collection of Action objects
	 */
	public Collection<Action> getPossibleActions();

	/**
	 * Use this method to store the set of possible actions in this state.
	 * Possible actions are those actions that can possibly be executed in
	 * this state and will lead to a valid new state.
	 * 
	 * @param a Collection of Action objects
	 */
	public void setPossibleActions(Collection<Action> a);

	/**
	 * Generate a cloned version of this state. The copy must be deep enough that the newly generated SearchState is independent from the old one.
	 * That means that if an action alteres one SearchState object, no other object may be affected by this change.
	 * 
	 * @return a copied version of this SearchState
	 */
	public Object clone();
}
