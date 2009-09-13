package abalone;

import java.util.LinkedList;
import java.util.List;

public class Board
{

	private Node centralNode;
	private List<Node> nodes;
	
	public Board(int sideLength)
	{
		centralNode = new Node();
		nodes = new LinkedList<Node>();
		nodes.add(centralNode);
		
		createBoard(sideLength);
	}

	private void createBoard(int sideLength)
	{
		createNode(centralNode, sideLength-1);

	}

	private void createNode(Node node, int level)
	{
		if (level <= 0)
		{
			return;
		}
		Direction startDirection = Direction.UPPER_LEFT;
		for (Direction d : startDirection)
		{
			Direction od = d.getOpposite();
			
			Node neighbour = node.getNeighbour(d);
			if (neighbour == null)
			{
				neighbour = node.addNeighbour(d);
				nodes.add(neighbour);
			}

			neighbour.setNeighbour(od, node);
			
			Node prev = node.getNeighbour(d.getNextCCW());
			Node next = node.getNeighbour(d.getNextCW());
			if(prev != null)
			{
				neighbour.setNeighbour(od.getNextCW(), prev);
				prev.setNeighbour(d.getNextCW(), neighbour);
			}
			if(next != null)
			{
				neighbour.setNeighbour(od.getNextCCW(), next);
				next.setNeighbour(d.getNextCCW(), neighbour);
			}

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
