package abalone.ai;

import java.util.Comparator;

import search.tree.SearchNode;
import abalone.model.Move;

class MoveComparator implements Comparator<SearchNode>
{
	@Override
	public int compare(SearchNode o1, SearchNode o2)
	{
		Move m1 = (Move) o1.getAction();
		Move m2 = (Move) o2.getAction();
		
		double score1 = 0;
		double score2 = 0;
		
		switch(m1.getType())
		{
			case PUSHOFF:
				score1 += 5;
				break;
			case SUMITO:
				score1 += 4;
				break;
			case INLINE:
				score1 += 3;
				break;
			case BROADSIDE:
				score1 += 2;
				break;
		}
		score1 += m1.getMarbleLine().getNodes().size()/3.0;

		switch(m2.getType())
		{
			case PUSHOFF:
				score2 += 5;
				break;
			case SUMITO:
				score2 += 4;
				break;
			case INLINE:
				score2 += 3;
				break;
			case BROADSIDE:
				score2 += 2;
				break;
		}
		score2 += m2.getMarbleLine().getNodes().size()/3.0;

		return (score1 < score2)?1:-1;
	}
}