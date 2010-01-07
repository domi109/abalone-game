package search.tree.games.minimax.hashing;

import java.util.PriorityQueue;
import java.util.Queue;

import search.tree.SearchNode;
import search.tree.games.minimax.MinimaxNodeComparator;
import search.tree.games.minimax.MinimaxProblem;
import search.tree.heuristic.Evaluator;

public class IterativeDeepeningHashingMinimaxKillerSearch extends IterativeDeepeningHashingMiniMaxSearch{
	private int depth;
	private int nrNodesToInvestigate;
	private long startingTime;
	private long timeLimit;

	public IterativeDeepeningHashingMinimaxKillerSearch(MinimaxProblem t, Evaluator<Double> evaluator, long timeLimit, int lookingDepth, int nrNodesToInvestigate) {
		super(t, evaluator, timeLimit);
		this. timeLimit = timeLimit;
		depth = lookingDepth;
		this. nrNodesToInvestigate = nrNodesToInvestigate;
	}
	
	public SearchNode search(SearchNode node){
		startingTime = System.currentTimeMillis();
		Queue<SearchNode> q1 = new PriorityQueue<SearchNode>(20,new MinimaxNodeComparator());
		Queue<SearchNode> q2 = new PriorityQueue<SearchNode>(20,new MinimaxNodeComparator());
		setDepthLimit(depth);
		q1 = getChildren(node);
		boolean test = timeBreakTest();
		
		while(!timeBreakTest()){
			q2.clear();
			for (int i = 0; i < nrNodesToInvestigate; i++) {
				q2.addAll(getChildren(q1.remove()));
			}
			q1 = q2;
		}
		
		return q1.remove();
	}
	
	public boolean timeBreakTest() {
		return System.currentTimeMillis() - startingTime > timeLimit;
	}

}
