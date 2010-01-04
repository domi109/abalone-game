package abalone.gamestate;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import abalone.model.Board;
import abalone.model.Direction;
import abalone.model.Node;
import abalone.model.Player;

public class ZobristHasher
{
	private static HashMap<Node, Map<Player, ByteBuffer>> zobristTable;

	public static long get(Node node, Player player)
	{
		return zobristTable.get(node).get(player).getLong(0);

	}

	public static long[] getSymmetries(long hash)
	{
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putLong(hash);
		long[] list = new long[11];
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				if ((i == 0) && (j == 0))
				{
					bb = alpha(bb);
					continue;
				}
				list[i*6+j-1] = bb.getLong(0);
				bb = alpha(bb);
			}

			bb = beta(bb);
		}
		return list;
	}

	public static void generateZobristTable(Board board, List<Player> assignments)
	{
		zobristTable = new HashMap<Node, Map<Player, ByteBuffer>>(board.getNodes().size());

		//generateUnsymmetricZobristTable(board, assignments);
		generateSymmetricZobrist(board, assignments);
	}

	private static void generateSymmetricZobrist(Board board, List<Player> assignments)
	{
		Random rand = new Random();

		// first we look at the symmetry axes:
		//     + - - - +
		//    - + - - + -
		//   - - + - + - -
		//  - - - + + - - -
		// + + + + + + + + +
		//  - - - + + - - -
		//   - - + - + - -
		//    - + - - + -
		//     + - - - +

		// Here the hash has the structure
		// x1 x2 x3 x2 x3 x2 x3 x4

		Node[] nodes = new Node[6];
		for (int i = 0; i<Direction.values().length; i++)
		{
			nodes[i] = board.getCentralNode().getNeighbour(Direction.values()[i]);
		}

		for (int r = 0; r < 4; r++)
		{
			// iterate over radius
			for (Player p : assignments)
			{
				// for each player
				
				// create new hash that has the structure
				// x1 x2 x3 x2 x3 x2 x3 x4
				ByteBuffer hash = ByteBuffer.allocate(8);
				byte[] bytes = new byte[4];
				rand.nextBytes(bytes);
				hash.put(bytes[0]);
				hash.put(bytes[1]);
				hash.put(bytes[2]);
				hash.put(bytes[1]);
				hash.put(bytes[2]);
				hash.put(bytes[1]);
				hash.put(bytes[2]);
				hash.put(bytes[3]);

				for (int i = 0; i<nodes.length;i++)
				{
					Node n = nodes[i];
					// Look at all nodes
					
					// Get the row for the node n
					Map<Player, ByteBuffer> m = null;
					if((m=zobristTable.get(n))==null)
					{
						m = new HashMap<Player,ByteBuffer>();
						zobristTable.put(n, m);
					}
					m.put(p, hash);
					
					// take next hash
					hash = alpha(hash);
				}
			}
			if (r < 3)
			{
				for (int i = 0; i < nodes.length; i++)
				{
					// proceed with node i
					nodes[i] = nodes[i].getNeighbour(Direction.values()[i]);
				}
			}
		}
		
		// now we have one more symmetry axis:
		//     - - + - -
		//    - - - - - -
		//   - - - + - - -
		//  - - - - - - - -
		// - - - - + - - - -
		//  - - - - - - - -
		//   - - - + - - -
		//    - - - - - -
		//     - - + - -
		// Here beta is the identity, thus the hash is
		// x1 x2 x3 x4 x4 x3 x2 x5

		nodes = new Node[6];
		for (int i = 0; i<Direction.values().length; i++)
		{
			nodes[i] = board.getCentralNode().getNeighbour(Direction.values()[i]).getNeighbour(Direction.values()[i].getNextCW());
		}
		
		for (int r = 0; r < 2; r++)
		{
			// iterate over radius
			for (Player p : assignments)
			{
				// for each player
				
				// create new hash that has the structure
				// x1 x2 x3 x2 x3 x2 x3 x4
				ByteBuffer hash = ByteBuffer.allocate(8);
				byte[] bytes = new byte[5];
				rand.nextBytes(bytes);
				hash.put(bytes[0]);
				hash.put(bytes[1]);
				hash.put(bytes[2]);
				hash.put(bytes[3]);
				hash.put(bytes[3]);
				hash.put(bytes[2]);
				hash.put(bytes[1]);
				hash.put(bytes[4]);

				for (int i = 0; i<nodes.length;i++)
				{
					Node n = nodes[i];
					// Look at all nodes
					
					// Get the row for the node n
					Map<Player, ByteBuffer> m = null;
					if((m=zobristTable.get(n))==null)
					{
						m = new HashMap<Player,ByteBuffer>();
						zobristTable.put(n, m);
					}
					m.put(p, hash);
					
					// take next hash
					hash = alpha(hash);
					
				}

			}
			if (r < 1)
			{
				for (int i = 0; i < nodes.length; i++)
				{
					// proceed with node i
					nodes[i] = nodes[i].getNeighbour(Direction.values()[i]).getNeighbour(Direction.values()[i].getNextCW());
				}
			}
		}
		
		// Now there still is the special case of the center
		// there it holds that the hash takes the form
		// x1 x2 x2 x2 x2 x2 x2 x3
		{
			Node central = board.getCentralNode();
			Map<Player, ByteBuffer> m = new HashMap<Player,ByteBuffer>(assignments.size());
			for(Player p : assignments)
			{
				ByteBuffer hash = ByteBuffer.allocate(8);
				byte[] bytes = new byte[3];
				rand.nextBytes(bytes);
				hash.put(bytes[0]);
				hash.put(bytes[1]);
				hash.put(bytes[1]);
				hash.put(bytes[1]);
				hash.put(bytes[1]);
				hash.put(bytes[1]);
				hash.put(bytes[1]);
				hash.put(bytes[2]);
				
				m.put(p, hash);
			}
			zobristTable.put(central, m);
		}
		
		
		// Now we've covered all symmetry axis, so we can go on
		// to the "normal" nodes
		for (int i = 19; i < board.getEquiPaths().get(0).size(); i++)
		{
			// for every step on the path
			for (Player p : assignments)
			{
				// for every assignment
				ByteBuffer hash = ByteBuffer.allocate(8);
				hash.putLong(0,rand.nextLong());
				for(int j = 0; j<2; j++)
				{
					// for every mirroring
					for(int h = 0; h<6;h++)
					{
						// for every rotation
						Node n = board.getEquiPaths().get(j*6+h).get(i).getValue();
						Map<Player, ByteBuffer> m = null;
						if((m=zobristTable.get(n))==null)
						{
							m = new HashMap<Player,ByteBuffer>();
							zobristTable.put(n, m);
						}
						if(!m.containsKey(p))
						{
							// Only if we did not already find
							// a hash
							m.put(p, hash);
						}

						hash = alpha(hash);
					}
					hash = beta(hash);
				}
			}
		}
	}
	
	private static void generateUnsymmetricZobristTable(Board board, List<Player> assignments)
	{
		Random rand = new Random();
		zobristTable = new HashMap<Node, Map<Player, ByteBuffer>>(board.getNodes().size());
		for (int i = 0; i < board.getEquiPaths().get(0).size(); i++)
		{
			Node n = board.getEquiPaths().get(0).get(i).getValue();
			if (zobristTable.containsKey(n))
			{
				continue;
			}
			Map<Player, ByteBuffer> map = new HashMap<Player, ByteBuffer>(assignments.size());
			for (Player p : assignments)
			{
				ByteBuffer hash = ByteBuffer.allocate(8);
				hash.putLong(rand.nextLong());
				map.put(p, hash);
			}
			zobristTable.put(n, map);
		}
	}

	private static ByteBuffer alpha(ByteBuffer hash)
	{
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.put(0, hash.get(0));
		bb.put(7, hash.get(7));
		for (int i = 0; i < 6; i++)
		{
			bb.put(i + 1, hash.get((i + 1) % 6 + 1));
		}

		return bb;
	}

	private static ByteBuffer beta(ByteBuffer hash)
	{
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.put(0, hash.get(0));
		bb.put(7, hash.get(7));
		for (int i = 1; i < 7; i++)
		{
			bb.put(i, hash.get(7 - i));
		}

		return bb;
	}

}
