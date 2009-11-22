package abalone.ai;

import java.util.Comparator;

import search.tree.SearchNode;
import abalone.model.Move;

class SimpleMoveComparator implements Comparator<SearchNode>
{
	@Override
	public int compare(SearchNode o1, SearchNode o2)
	{
		Move m1 = (Move) o1.getAction();
		Move m2 = (Move) o2.getAction();

		if (m1.getMarbleLine().getNodes().size() < m2.getMarbleLine().getNodes().size())
		{
			return 1;
		}
		else if (m1.getMarbleLine().getNodes().size() == m2.getMarbleLine().getNodes().size())
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
}