package search.tree.games.minimax;

import search.tree.heuristic.Evaluator;

/**
 * TODO: Documentation
 * @author Daniel Mescheder
 * 
 */
public class HashingMinimaxSearch extends MinimaxSearch
{

	public HashingMinimaxSearch(MinimaxProblem t)
	{
		super(t);
	}
	public HashingMinimaxSearch(MinimaxProblem t,Evaluator<Double> evaluator)
	{
		super(t, evaluator);
	}
	
	@Override
	protected boolean testNode(MiniMaxNode node)
	{
		// TODO Do hashing here
		return super.testNode(node);
	}
}
