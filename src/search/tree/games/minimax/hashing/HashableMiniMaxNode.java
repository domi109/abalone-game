package search.tree.games.minimax.hashing;


import java.nio.ByteBuffer;

import search.Action;
import search.hashing.ZobristHashable;
import search.tree.ZobristHashableState;
import search.tree.SearchState;
import search.tree.games.minimax.MiniMaxNode;

public abstract class HashableMiniMaxNode extends MiniMaxNode implements ZobristHashable
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
	public ByteBuffer zobristHash()
	{
		return ((ZobristHashableState)getState()).zobristHash();
	}
	
	
	
}
