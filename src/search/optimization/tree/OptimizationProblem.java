package search.optimization.tree;

import search.tree.SearchNode;
import search.tree.SearchProblem;

public interface OptimizationProblem extends SearchProblem
{
	public void registerNode(SearchNode n);
	public SearchNode getBest();
}
