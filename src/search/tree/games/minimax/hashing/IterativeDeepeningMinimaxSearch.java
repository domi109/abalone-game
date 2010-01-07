package search.tree.games.minimax.hashing;


import search.tree.IterativeDeepeningSearch;
import search.tree.SearchNode;
import search.tree.SearchProblem;
import search.tree.games.minimax.MiniMaxNode;
import search.tree.games.minimax.MinimaxProblem;
import search.tree.games.minimax.MinimaxSearch;
import search.tree.heuristic.Evaluator;

public class IterativeDeepeningMinimaxSearch extends MinimaxSearch implements IterativeDeepeningSearch
{
	private static final long serialVersionUID = 5354076006129704855L;
	private long timeLimit;
	private MinimaxProblem t;
	private long startingTime;
	private long timeLastIteration;

	public IterativeDeepeningMinimaxSearch(MinimaxProblem t, long timeLimit)
	{
		super.setEvaluator(new DummyEvaluator());
		this.t = t;
		this.timeLimit = timeLimit;
	}

	public IterativeDeepeningMinimaxSearch(MinimaxProblem t, Evaluator<Double> evaluator, long timeLimit)
	{
		super.setEvaluator(evaluator);
		this.t = t;
		this.timeLimit = timeLimit;
	}

	@Override
	public SearchNode search(SearchNode node)
	{
		startingTime = System.currentTimeMillis();
		SearchNode result = node;
		SearchNode prevResult = result; 
		int depth = 0;
		timeLastIteration = 0;
		outOfTime = false;
		
		while(!outOfTime()){
			depth++;
			prevResult = result;
			timeLastIteration = System.currentTimeMillis();
			System.out.println("Current Depth: " + depth);
			setDepthLimit(depth);
			result = super.search(node, timeLimit, startingTime);
			timeLastIteration -= System.currentTimeMillis();
		}
		if(outOfTime) {
			System.out.println("took result of depth " + (depth-1));
			return prevResult;
		}
		else{
			System.out.println("took result of depth " + depth);
			return result;
		}
	}
	
/*	public boolean outOfTime() {
		return timeLastIteration > (timeLimit / 2) ;
	}*/
	
	public boolean outOfTime() {
		return System.currentTimeMillis() - startingTime > timeLimit;
	}

	@Override
	public SearchProblem getProblem() {
		return t;
	}

	@Override
	public long getTimeLimit() {
		return timeLimit;
	}

	@Override
	public void setTimeLimit(long time) {
		timeLimit = time;
	}
}
