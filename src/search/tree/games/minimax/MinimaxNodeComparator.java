package search.tree.games.minimax;

import java.util.Comparator;

import search.tree.SearchNode;


public class MinimaxNodeComparator implements Comparator<SearchNode>
{
	@Override
	public int compare(SearchNode m1, SearchNode m2)
	{		
		MiniMaxNode n1 = (MiniMaxNode)m1;
		MiniMaxNode n2 = (MiniMaxNode)m2;
		return (n1.getValue()<=n2.getValue()) ? 1 : -1;
	}

}
