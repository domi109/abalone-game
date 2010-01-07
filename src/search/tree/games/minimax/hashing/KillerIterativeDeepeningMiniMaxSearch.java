package search.tree.games.minimax.hashing;

import java.util.PriorityQueue;
import java.util.Queue;

import search.tree.SearchNode;
import search.tree.games.minimax.MinimaxNodeComparator;
import search.tree.games.minimax.MinimaxProblem;
import search.tree.heuristic.Evaluator;

public class KillerIterativeDeepeningMiniMaxSearch extends IterativeDeepeningMinimaxSearch{
	private int depth;
	private int nrNodesToInvestigate;

	public KillerIterativeDeepeningMiniMaxSearch(MinimaxProblem t, Evaluator<Double> evaluator, long timeLimit, int lookingDepth, int nrNodesToInvestigate) {
		super(t, evaluator, timeLimit);
		depth = lookingDepth;
		this. nrNodesToInvestigate = nrNodesToInvestigate;
	}
	
	public SearchNode search(SearchNode node){
		Queue<SearchNode> q1 = new PriorityQueue<SearchNode>(20,new MinimaxNodeComparator());
		Queue<SearchNode> q2 = new PriorityQueue<SearchNode>(20,new MinimaxNodeComparator());
		setDepthLimit(depth);
		q1 = getChildren(node);
		
		while(!outOfTime()){
			q2.clear();
			for (int i = 0; i < nrNodesToInvestigate; i++) {
				q2.addAll(getChildren(q1.remove()));
			}
			q1 = q2;
		}
		
		return node;
	}

}
