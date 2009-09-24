package search.tree.minimax;

import search.tree.SearchNode;
import search.tree.SearchState;

public abstract class MiniMaxNode extends SearchNode
{
	protected int value;
	
	public MiniMaxNode(SearchState s)
	{
		super(s);
	}
	
	public void setValue(int val)
	{
		this.value = val;
	}
}
