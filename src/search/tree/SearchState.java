package search.tree;

import java.io.Serializable;

/**
 * If a class implements this interface, it can be part of a
 * {@link SearchProblem} which can - for example - be worked on by a
 * {@link IDTreeSearch}. A SearchState is usually referenced by a
 * {@link SearchNode}
 * 
 * Some ideas for this interface are taken from "Artificial Intelligence - A Modern Approach" (Russel, Norvig).
 * 
 * @author Daniel Mescheder
 */
public interface SearchState extends Serializable
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
	 * Generate a cloned version of this state. The copy must be deep enough that the newly generated SearchState is independent from the old one.
	 * That means that if an action alters one SearchState object, no other object may be affected by this change.
	 * 
	 * @return a copied version of this SearchState
	 */
	public Object clone();
}
