package search.tree.games.minimax.hashing;

import search.Action;
import search.tree.ZobristHashableState;
import search.tree.SearchState;
import search.tree.games.minimax.MiniMaxNode;

public abstract class HashableMiniMaxNode extends MiniMaxNode
{
	private static final long serialVersionUID = 7497043064698901831L;

	public HashableMiniMaxNode(ZobristHashableState s, MiniMaxNode parent, Action action)
	{
		super(s, parent, action);
	}

	public HashableMiniMaxNode(SearchState s)
	{
		super(s);
	}
	
	@Override
	public int hashCode()
	{
		return (int)((ZobristHashableState)getState()).zobristHash();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof HashableMiniMaxNode))
		{
			return false;
		}
		HashableMiniMaxNode n = (HashableMiniMaxNode)obj;
		return ((ZobristHashableState)getState()).zobristHash() == ((ZobristHashableState)n.getState()).zobristHash();
	}
	
	
	
}
