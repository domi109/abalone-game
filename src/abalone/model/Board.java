package abalone.model;

import java.util.LinkedList;
import java.util.List;

/**
 * A class representing a board state in the game of abalone
 *
 * The board is represented as a graph with interconnected nodes.
 */
public class Board
{
	/**
	 * The central node of the board.
	 */
	private Node centralNode;
	private List<Node> nodes;
	
	public Board()
	{
		centralNode = new Node();
		nodes = new LinkedList<Node>();
		nodes.add(centralNode);
	}

	/**
	 * Call this to initialize the board-graph
	 */
	private void createBoard()
	{
		createNode(centralNode, 4);
	}

	/**
	 * Recursively adds nodes to the board graph
	 * @param node the node that is currently handled
	 * @param level level-counter counting backwards from the center - recursion will terminate when level is zero.
	 */
	private void createNode(Node node, int level)
	{
		if (level <= 0)
		{
			// Terminate recursion
			return;
		}
		
		Direction startDirection = Direction.UPPER_LEFT;
		for (Direction d : startDirection)
		{
			// We're going clockwise looking into all directions.
			// The direction we're currently looking at is d
			// od is the opposite direction.
			Direction od = d.getOpposite();
			
			// We look at the nodes neighbour in the investigated direction
			Node neighbour = node.getNeighbour(d);
			if (neighbour == null)
			{
				// The neighbour is null, so it has not been created yet
				// thus, we now create it and add it to the nodes list.
				neighbour = node.addNeighbour(d);
				nodes.add(neighbour);
			}

			// We still have to link the neighbour with the current node...
			neighbour.setNeighbour(od, node);
			
			// Now we want to look at the nodes next to the investigated neighbour...
			Node prev = node.getNeighbour(d.getNextCCW());
			Node next = node.getNeighbour(d.getNextCW());
			if(prev != null)
			{
				// there is a node next to our neighbour node
				// in counterclockwise direction... we interconnect
				// those two.
				neighbour.setNeighbour(od.getNextCW(), prev);
				prev.setNeighbour(d.getNextCW(), neighbour);
			}
			if(next != null)
			{
				// there is a node next to our neighbour node
				// in clockwise direction... we interconnect
				// those two.
				neighbour.setNeighbour(od.getNextCCW(), next);
				next.setNeighbour(d.getNextCCW(), neighbour);
			}

			// now we go deeper into the recursion tree, further investigating on
			// the neighbour node.
			createNode(neighbour, level - 1);			
		}
	}

	public void printBoard()
	{
		Node leftNode = furthestNode(Direction.UPPER_LEFT);
		while (leftNode != null)
		{
			Node node = leftNode;
			while (node != null)
			{
				System.out.print("0");
				node=node.getNeighbour(Direction.RIGHT);
			}
			leftNode = (leftNode.getNeighbour(Direction.DOWN_LEFT)==null)?leftNode.getNeighbour(Direction.DOWN_RIGHT):leftNode.getNeighbour(Direction.DOWN_LEFT);
			System.out.println();
		}
	}

	public Node furthestNode(Direction inDirection)
	{
		Node node = centralNode;
		
		while (node.getNeighbour(inDirection) != null)
		{
			node = node.getNeighbour(inDirection);
		}
		return node;
	}
}
