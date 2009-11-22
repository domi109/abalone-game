package search.tree.games.minimax.hashing;

import java.util.Map;
import java.util.Map.Entry;

import search.hashing.SearchHashTable;
import search.tree.SearchNode;

public class MinimaxHashTable extends SearchHashTable<MinimaxHashEntry>
{
	private static final long serialVersionUID = 5363348033198176782L;

	private int maxSize;
	
	public MinimaxHashTable(int maxSize)
	{
		super();
		this.maxSize = maxSize;
	}

	public MinimaxHashTable(int maxSize, int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
		this.maxSize = maxSize;
	}

	public MinimaxHashTable(int maxSize, int initialCapacity)
	{
		super(initialCapacity);
		this.maxSize = maxSize;

	}

	public MinimaxHashTable(int maxSize, Map<? extends Long, ? extends MinimaxHashEntry> m)
	{
		super(m);
		this.maxSize = maxSize;
	}
	
	public MinimaxHashEntry put(Long hash, double value, int precision)
	{
		return super.put(hash, new MinimaxHashEntry(precision,value));
	}
	
	@Override
	public MinimaxHashEntry put(Long hash, MinimaxHashEntry value)
	{
		if(this.size()+1>maxSize)
		{
			return null;
		}
		return super.put(hash, value);
	}
	
	@Override
	public void putAll(Map<? extends Long, ? extends MinimaxHashEntry> m)
	{
		if(this.size()+m.size()>maxSize)
		{
			return;
		}
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
