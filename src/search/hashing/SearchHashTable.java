package search.hashing;

import java.util.HashMap;
import java.util.Map;

public class SearchHashTable<V> extends HashMap<Long, V>
{
	private static final long serialVersionUID = 5567886885239514943L;

	public SearchHashTable()
	{
		super();
	}

	public SearchHashTable(int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}

	public SearchHashTable(int initialCapacity)
	{
		super(initialCapacity);
	}

	public SearchHashTable(Map<? extends Long, ? extends V> m)
	{
		super(m);
	}


}
