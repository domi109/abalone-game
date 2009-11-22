package abalone.gamestate;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import abalone.model.Board;
import abalone.model.Node;
import abalone.model.Player;

public class ZobristHasher
{
	private static HashMap<Node, Map<Player, ByteBuffer>> zobristTable;


	public static ByteBuffer get(Node node, Player player)
	{
		return zobristTable.get(node).get(player);
		
	}
	
	public static List<ByteBuffer> getSymmetries(ByteBuffer hash)
	{
		List<ByteBuffer> list = new ArrayList<ByteBuffer>();
		for(int i = 0; i<2;i++)
		{
			for(int j = 0;j<6;j++)
			{
				if((i==0)&&(j==0))
				{
					hash = alpha(hash);
					continue;
				}
				list.add(hash);
				hash = alpha(hash);
			}
			
			hash = beta(hash);
		}
		return list;
	}
	
	public static void generateZobristTable(Board board, List<Player> assignments)
	{
		Random rand = new Random();
		zobristTable = new HashMap<Node,Map<Player,ByteBuffer>>(board.getNodes().size());
		for(int i = 0; i < board.getEquiPaths().get(0).size();i++)
		{
			Node n = board.getEquiPaths().get(0).get(i).getValue();
			if(zobristTable.containsKey(n))
			{
				continue;
			}
			Map<Player,ByteBuffer> map = new HashMap<Player,ByteBuffer>(assignments.size());
			for(Player p : assignments)
			{
				ByteBuffer hash = ByteBuffer.allocate(8);
				hash.putLong(rand.nextLong());
				map.put(p, hash);
// TODO: Generate Symmetric Hashes. This doesn't work yet
//				for(int j = 0; j < 2;j++)
//				{
//					for(int k = 0; k < 6; k++)
//					{
//						if((j == 0)&&(k == 0))
//						{			
//							hash = alpha(hash);
//							continue;
//						}
//						Node sym = board.getEquiPaths().get(j*6+k).get(i).getValue();
//						Map<Player, ByteBuffer> m = null;
//						if((m=zobristTable.get(sym)) == null)
//						{
//							m = new HashMap<Player, ByteBuffer>(assignments.size());
//							zobristTable.put(sym, m);
//						}
//						m.put(p, hash);
//						hash = alpha(hash);
//						
//					}
//					hash = beta(hash);
//				}
			}
			zobristTable.put(n, map);
		}
	}
	
	private static ByteBuffer alpha(ByteBuffer hash)
	{
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.put(0,hash.get(0));
		bb.put(7,hash.get(7));
		for(int i = 0;i<6;i++)
		{
			bb.put(i+1, hash.get((i+1)%6+1));
		}
		
		return bb;
	}
	
	private static ByteBuffer beta(ByteBuffer hash)
	{
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.put(0,hash.get(0));
		bb.put(7,hash.get(7));
		for(int i = 1;i<7;i++)
		{
			bb.put(i, hash.get(7-i));
		}
		
		return bb;
	}
	
	
}
