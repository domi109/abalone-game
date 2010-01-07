package search.tree.games.minimax.hashing;

import search.tree.DepthLimitedSearch;
import search.tree.SearchProblem;
import search.tree.games.minimax.MinimaxProblem;
import search.tree.heuristic.Evaluator;

public class DepthLimitedHashingMinimaxSearch extends HashingMinimaxSearch implements DepthLimitedSearch{
	private MinimaxProblem t;
	
	public DepthLimitedHashingMinimaxSearch(MinimaxProblem t, int limit)
	{
		this(t, new DummyEvaluator(),  limit);
		setTable(new MinimaxHashTable(200));
	}

	public DepthLimitedHashingMinimaxSearch(MinimaxProblem t, Evaluator<Double> evaluator, int depthLimit)
	{
		this.t = t;
		super.setEvaluator(evaluator);
		setDepthLimit(depthLimit);
		setTable(new MinimaxHashTable(300));
	}
	
	@Override
	public SearchProblem getProblem() {
		return t;
	}
}
