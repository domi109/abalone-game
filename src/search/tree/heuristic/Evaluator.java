package search.tree.heuristic;

import search.tree.SearchState;

public interface Evaluator<E extends Comparable<E>>
{
	public E eval(SearchState state);
}
