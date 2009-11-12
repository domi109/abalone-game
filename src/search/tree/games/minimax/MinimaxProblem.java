package search.tree.games.minimax;

import search.tree.SearchProblem;
import search.tree.SearchState;

public interface MinimaxProblem extends SearchProblem
{
	public int getFinalStateValue(SearchState state);

	public double repetitionValue();


}
