package search.tree.games.minimax.hashing;

import search.tree.SearchNode;
import search.tree.ZobristHashableState;
import search.tree.games.minimax.MiniMaxNode;
import search.tree.games.minimax.MinimaxProblem;
import search.tree.games.minimax.MinimaxSearch;
import search.tree.heuristic.Evaluator;

/**
 * TODO: Documentation
 * @author Daniel Mescheder
 * 
 */
public class HashingMinimaxSearch extends MinimaxSearch
{
	private static final long serialVersionUID = 5354076006129704855L;

	private MinimaxHashTable table;
	
	public HashingMinimaxSearch(MinimaxProblem t, int limit)
	{
		super(t, limit);
		this.table = new MinimaxHashTable(100);
	}
	public HashingMinimaxSearch(MinimaxProblem t,Evaluator<Double> evaluator, int limit)
	{
		super(t, evaluator, limit);
		
		this.table = new MinimaxHashTable(100);
	}
	
	@Override
	public SearchNode search(SearchNode node)
	{
		SearchNode result = super.search(node);
		return result;
	}
	
	/**
	 * Recursively finds the maximal child of the passed node
	 * 
	 * @param node
	 *            the node to investigate on
	 * @param alpha
	 *            the highest value we have found so far on the path leading to
	 *            this node - i.e. the best choice there is for max so far.
	 * @param beta
	 *            the lowest value we have found so far on the path leading to
	 *            this node - i.e. the best choice there is for min so far.
	 * 
	 * @return the maximal child of the node.
	 */
	@Override
	protected MiniMaxNode maxNode(MiniMaxNode node, double alpha, double beta)
	{
		// We store the maximal child in v:
		MiniMaxNode v = null;
		for (SearchNode n : node.expand())
		{
			// For every successor node

			HashableMiniMaxNode current = (HashableMiniMaxNode) n;

			if(!testNode(current))
			{
				// Get minimal child node of this successor
				MiniMaxNode min = minNode(current, alpha, beta);
				current.setValue(min.getValue());
			}
			
			

			// close this node in the hash table by storing the value
			table.put(current, current.getValue(),this.depthLimit-current.getDepth());

			if (v == null || v.getValue() < current.getValue())
			{
				// Either we haven't found any node at all yet, or we found
				// one who will eventually lead to a higher outcome.
				// Anyway: store it in v!
				v = current;

			}
			if (v.getValue() >= beta)
			{
				// This node we're investigating on is worth
				// at least the value of v.
				// But as beta (the lowest value on the path so far)
				// appears to be smaller than that,
				// min would never choose this node!
				// therefore it does not make any sense to further
				// look at any successors.
				return v;
			}
			// Now we know that the highest node on the path so far
			// is max(alpha,v).
			// We can take this into account for further iterations.
			alpha = Math.max(alpha, v.getValue());
		}
		return v;
	}

	/**
	 * Recursively finds the minimal child of the passed node
	 * 
	 * @param node
	 *            the node to investigate on
	 * @param alpha
	 *            the highest value we have found so far on the path leading to
	 *            this node - i.e. the best choice there is for max so far.
	 * @param beta
	 *            the lowest value we have found so far on the path leading to
	 *            this node - i.e. the best choice there is for min so far.
	 * 
	 * @return the minimal child of the node.
	 */
	@Override
	protected MiniMaxNode minNode(MiniMaxNode node, double alpha, double beta)
	{

		// We store the minimal child in v:
		MiniMaxNode v = null;
		for (SearchNode n : node.expand())
		{
			HashableMiniMaxNode current = (HashableMiniMaxNode) n;
			// For every successor node

			if(!testNode(current))
			{
				// Get maximal child node of this successor
				MiniMaxNode max = maxNode(current, alpha, beta);
				current.setValue(max.getValue());
			}


			
			// put this node in the hash table by storing the value
			table.put(current, current.getValue(),this.depthLimit-current.getDepth());
			
			if (v == null || v.getValue() > current.getValue())
			{
				// Either we haven't found any node at all yet, or we found
				// one who will eventually lead to a lower outcome.
				// Anyway: store it in v!
				v = current;
			}
			if (v.getValue() <= alpha)
			{
				// This node we're investigating on is worth
				// at most the value of v.
				// But as alpha (the highest value on the path so far)
				// appears to be greater than that,
				// max would never choose this node!
				// therefore it does not make any sense to further
				// look at any successors.
				return v;
			}
			// Now we know that the lowest node on the path so far
			// is min(beta,v).
			// We can take this into account for further iterations.
			beta = Math.min(beta, v.getValue());
		}
		return v;

	}
	
	@Override
	protected boolean testNode(MiniMaxNode node)
	{
		MinimaxHashEntry e=table.get(node);
		if(e!=null && e.getPrecision() <= (this.depthLimit - node.getDepth()))
		{
			node.setValue(e.getValue());
			return true;
		}
		return super.testNode(node);
	}
}
