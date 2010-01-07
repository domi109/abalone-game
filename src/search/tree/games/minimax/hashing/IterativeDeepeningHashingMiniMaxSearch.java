package search.tree.games.minimax.hashing;

import search.tree.IterativeDeepeningSearch;
import search.tree.SearchNode;
import search.tree.SearchProblem;
import search.tree.games.minimax.MinimaxProblem;
import search.tree.heuristic.Evaluator;

public class IterativeDeepeningHashingMiniMaxSearch extends HashingMinimaxSearch implements IterativeDeepeningSearch{

	private long timeLimit;
	private MinimaxProblem t;
	private long startingTime;
	private long timeLastIteration;
	HashingMinimaxSearch hashing;
	
	public IterativeDeepeningHashingMiniMaxSearch(MinimaxProblem t, long timeLimit)
	{
		super.setEvaluator(new DummyEvaluator());
		this.t = t;
		this.timeLimit = timeLimit;
		setTable(new MinimaxHashTable(300));
	}

	public IterativeDeepeningHashingMiniMaxSearch(MinimaxProblem t, Evaluator<Double> evaluator, long timeLimit)
	{
		super.setEvaluator(evaluator);
		this.t = t;
		this.timeLimit = timeLimit;
		setTable(new MinimaxHashTable(300));
	}
	
	public SearchNode search(SearchNode node)
	{
		startingTime = System.currentTimeMillis();
		SearchNode result = node;
		SearchNode prevResult = result; 
		int depth = 0;
		timeLastIteration = 0;
		setOutOfTimeFalse();
		
		while(!outOfTime()){
			depth++;
			prevResult = result;
			timeLastIteration = System.currentTimeMillis();
			
			System.out.println("Current Depth: " + depth);
			
			setDepthLimit(depth);
			result = super.search(node, timeLimit, startingTime);
			//result = hashing.search(node, timeLimit, startingTime);
			
			
			timeLastIteration -= System.currentTimeMillis();
		}
		if(outOfTime()) {
			System.out.println("took result of depth " + (depth-1));
			return prevResult;
		}
		else{
			System.out.println("took result of depth " + depth);
			return result;
		}
	}
	
	public boolean outOfTime() {
		return System.currentTimeMillis() - startingTime > timeLimit;
	}

	@Override
	public long getTimeLimit() {
		return timeLimit;
	}

	@Override
	public void setTimeLimit(long time) {
		timeLimit = time;
	}

	@Override
	public SearchProblem getProblem() {
		return t;
	}
}
