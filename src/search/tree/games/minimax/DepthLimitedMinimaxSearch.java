package search.tree.games.minimax;

import search.tree.DepthLimitedSearch;
import search.tree.SearchProblem;
import search.tree.heuristic.Evaluator;

public class DepthLimitedMinimaxSearch extends MinimaxSearch implements DepthLimitedSearch{
	MinimaxProblem problem;
	
	public DepthLimitedMinimaxSearch(int depth, Evaluator<Double> evaluator, MinimaxProblem problem){
		setDepthLimit(depth);
		setEvaluator(evaluator);
		this.problem = problem;
	}
	
	@Override
	public SearchProblem getProblem() {
		return problem;
	}

}
