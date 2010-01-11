package abalone.ai;

import java.util.Comparator;

import nl.maastrichtuniversity.dke.libreason.def.treesearch.SearchNode;
import abalone.model.Move;

class MoveComparator implements Comparator<SearchNode>
{
	
	@Override
	public int compare(SearchNode o1, SearchNode o2)
	{
		Move m1 = (Move) o1.getAction();
		Move m2 = (Move) o2.getAction();
		
		if(m1.getType().getValue() != m2.getType().getValue())
		{
			return (m1.getType().getValue() > m2.getType().getValue())?-1:1;
		}
		else
		{
			return (m1.getMarbleLine().getNodes().size() >= m2.getMarbleLine().getNodes().size())?-1:1;
		}
	}
}