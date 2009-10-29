package search.tree.queues;

import java.util.Comparator;
import java.util.PriorityQueue;

import search.tree.SearchNode;
import search.tree.heuristic.Evaluator;

/**
 * A priority queue that corresponds to the A*-search strategy
 * 
 * @author Daniel Mescheder
 *
 */
public class GreedyBestFirstQueue extends PriorityQueue<SearchNode>
{
	private static final long serialVersionUID = 5060840009767474183L;

	private static class AStarComparator implements Comparator<SearchNode>
	{
		private Evaluator<Double> heuristic;
		
		public AStarComparator(Evaluator<Double> heuristic)
		{
			this.heuristic = heuristic;
		}
		@Override
		public int compare(SearchNode n1, SearchNode n2)
		{
			double val1 = heuristic.eval(n1.getState());
			double val2 = heuristic.eval(n2.getState());
			if(val1<val2)
			{
				return -1;
			}
			else if(val1==val2)
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
	}
	
	public GreedyBestFirstQueue(Evaluator<Double> heuristic)
	{
		super(10,new AStarComparator(heuristic));
	}
	
}
