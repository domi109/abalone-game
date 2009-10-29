package search.tree.games.minimax;

import search.Action;
import search.tree.SearchNode;
import search.tree.SearchState;

public abstract class MiniMaxNode extends SearchNode
{
	protected double value;
	
	public MiniMaxNode(SearchState s)
	{
		super(s);
	}
	
	public MiniMaxNode(SearchState s, MiniMaxNode parent, Action action)
	{
		super(s,parent, action);
	}
	
	public void setValue(double val)
	{
		this.value = val;
	}
	
	public double getValue()
	{
		return value;
	}
}
