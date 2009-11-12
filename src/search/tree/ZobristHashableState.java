package search.tree;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import search.hashing.ZobristHashable;

/**
 * TODO doc
 * @author Daniel Mescheder
 */
public abstract class ZobristHashableState implements SearchState, ZobristHashable
{
	private static final long serialVersionUID = -6618492768578345252L;
	private static Map<Object,Map<Object,Long>> zobristTable;
	
	public static void generateZobristTable(List<Object> positions, List<Object> assignments)
	{
		Random rand = new Random();
		zobristTable = new HashMap<Object,Map<Object,Long>>(positions.size());
		for(Object e : positions)
		{
			Map<Object,Long> map = new HashMap<Object,Long>(assignments.size());
			for(Object i : assignments)
			{
				map.put(i, rand.nextLong());
			}
			zobristTable.put(e, map);
		}
	}
	
	public static double assessZobristTable(int no)
	{
		Random rand = new Random();
		int collisions = 0;
		long[] results = new long[no];
		for(int i = 0; i<no; i++)
		{
			results[i] = 0;
			for(Entry<Object,Map<Object,Long>> e : zobristTable.entrySet())
			{
				Long[] values = new Long[e.getValue().size()];
				values = e.getValue().values().toArray(values);
				results[i] ^= values[rand.nextInt(values.length)];
			}
			for(int j = 0; j < i; j++)
			{
				if(results[j] == results[i])
				{
					collisions++;
				}
			}
		}
		return (double)collisions/(double)no;
	}
	
	public static long getZobristValue(Object position, Object assignment)
	{
		return zobristTable.get(position).get(assignment);
	}
	
	public static void saveZobristTable(File file)
	{
		// TODO save table
	}
	
	public static void loadZobristTable(File file)
	{
		// TODO load table
	}
	
	@Override
	public abstract Object clone();
}
