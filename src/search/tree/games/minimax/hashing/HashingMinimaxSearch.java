package search.tree.games.minimax.hashing;

import java.nio.ByteBuffer;

import search.hashing.SymZobristHashable;
import search.hashing.ZobristHashable;
import search.tree.SearchNode;
import search.tree.ZobristHashableState;
import search.tree.games.minimax.MiniMaxNode;
import search.tree.games.minimax.MinimaxProblem;
import search.tree.games.minimax.MinimaxSearch;
import search.tree.heuristic.Evaluator;

/**
 * TODO: Documentation
 * 
 * @author Daniel Mescheder
 * 
 */
public class HashingMinimaxSearch extends MinimaxSearch
{
	private static final long serialVersionUID = 5354076006129704855L;
	private int nodeCount = 0;
	private MinimaxHashTable table;

	public HashingMinimaxSearch(MinimaxProblem t, int limit)
	{
		super(t, limit);
		this.table = new MinimaxHashTable(200);
	}

	public HashingMinimaxSearch(MinimaxProblem t, Evaluator<Double> evaluator, int limit)
	{
		super(t, evaluator, limit);

		this.table = new MinimaxHashTable(300);
	}

	@Override
	public SearchNode search(SearchNode node)
	{
		long time = System.currentTimeMillis();
		SearchNode result = super.search(node);
		System.out.println(nodeCount+","+table.size()+","+result.getAction()+","+(System.currentTimeMillis() - time));
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

			if (!testNode(current))
			{
				table.put(current.zobristHash().getLong(0), MinimaxHashEntry.OPEN);
				// Get minimal child node of this successor
				MiniMaxNode min = minNode(current, alpha, beta);
				if(min == null)
				{
					current.setValue(-1); // TODO magic number
				}
				else
				{
				current.setValue(min.getValue());
				}				
				putHash(current);

			}


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

			if (!testNode(current))
			{
				table.put(current.zobristHash().getLong(0), MinimaxHashEntry.OPEN);
				// Get maximal child node of this successor
				MiniMaxNode max = maxNode(current, alpha, beta);
				if(max == null)
				{
					current.setValue(-1); // TODO magic number
				}
				else
				{
				current.setValue(max.getValue());
				}
				putHash(current);
			}


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
		nodeCount++;
		HashableMiniMaxNode n = (HashableMiniMaxNode) node;
		MinimaxHashEntry e = table.get(n.zobristHash().getLong(0));
		if (e != null)
		{
			if (e == MinimaxHashEntry.OPEN)
			{
				node.setValue(((MinimaxProblem) getProblem()).repetitionValue());
				return true;
			}
			else if (e.getPrecision() >= (this.depthLimit - node.getDepth()))
			{
				node.setValue(e.getValue());
				return true;
			}

		}

		return super.testNode(node);
	}
	
	protected void putHash(HashableMiniMaxNode n)
	{
		Long hash = n.zobristHash().getLong(0);
		table.put(hash, n.getValue(), this.depthLimit - n.getDepth());
		if(n instanceof SymZobristHashable)
		{
			SymZobristHashable s = (SymZobristHashable) n;
			for(ByteBuffer bb : s.symmetryHashes())
			{
				table.put(bb.getLong(0), n.getValue(), this.depthLimit-n.getDepth());
			}
		}
	}
}
