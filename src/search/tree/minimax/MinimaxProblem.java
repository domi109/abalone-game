package search.tree.minimax;

import search.tree.SearchProblem;
import search.tree.SearchState;

public interface MinimaxProblem extends SearchProblem
{
	public int getFinalStateValue(SearchState state);
}
