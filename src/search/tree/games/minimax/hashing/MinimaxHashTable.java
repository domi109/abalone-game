package search.tree.games.minimax.hashing;

import java.util.Map;
import java.util.Map.Entry;

import search.hashing.SearchHashTable;
import search.tree.SearchNode;

public class MinimaxHashTable extends SearchHashTable<MinimaxHashEntry>
{
	private static final long serialVersionUID = 5363348033198176782L;

	
	public MinimaxHashTable()
	{
		super();
	}

	public MinimaxHashTable(int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}

	public MinimaxHashTable(int initialCapacity)
	{
		super(initialCapacity);

	}

	public MinimaxHashTable(Map<? extends Long, ? extends MinimaxHashEntry> m)
	{
		super(m);
	}
	
	public MinimaxHashEntry put(Long hash, double value, int precision)
	{
		return super.put(hash, new MinimaxHashEntry(precision,value));
	}
	
	@Override
	public MinimaxHashEntry put(Long hash, MinimaxHashEntry value)
	{
		return super.put(hash, value);
	}
	
	@Override
	public void putAll(Map<? extends Long, ? extends MinimaxHashEntry> m)
	{
		super.putAll(m);
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		for(Entry<Long, MinimaxHashEntry> e : this.entrySet())
		{
			sb.append(e.getKey() + " | " + e.getValue()+"\n");
		}
		return sb.toString();
	}
}
