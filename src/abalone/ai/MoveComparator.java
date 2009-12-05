package abalone.ai;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import search.tree.SearchNode;
import abalone.model.Move;
import abalone.model.Move.MoveType;

class MoveComparator implements Comparator<SearchNode>
{
	private Map<MoveType,Integer> moveRating;
	
	public MoveComparator()
	{
		moveRating = new HashMap<MoveType, Integer>(5);
		moveRating.put(MoveType.BROADSIDE, 1);
		moveRating.put(MoveType.INLINE, 2);
		moveRating.put(MoveType.PUSHOFF, 4);
		moveRating.put(MoveType.SUMITO, 3);
		moveRating.put(MoveType.UNKNOWN, 0);

	}
	
	@Override
	public int compare(SearchNode o1, SearchNode o2)
	{
		Move m1 = (Move) o1.getAction();
		Move m2 = (Move) o2.getAction();
		
		if(m1.getType() != m2.getType())
		{
			return (moveRating.get(m1.getType()) > moveRating.get(m2.getType()))?-1:1;
		}
		else
		{
			return (m1.getMarbleLine().getNodes().size() >= m2.getMarbleLine().getNodes().size())?-1:1;
		}
	}
}