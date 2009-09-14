package abalone.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A node in the board graph
 * 
 */
public class Node
{

	// The list of neighbours of the current node,
	// ordered by direction
	private Map<Direction, Node> neighbourList;
	// If there's no marble on this spot: null
	// If there's a marble: link to the Player that
	// owns the marble.
	private Player marbleOwner;

	public Node()
	{
		neighbourList = new HashMap<Direction, Node>();
	}

	/**
	 * 
	 * Query for the owner of the marble located at this node
	 * 
	 * @return null if there is no marble, a player object otherwise
	 */
	public Player getMarbleOwner()
	{
		return marbleOwner;
	}

	/**
	 * Set the marble information for this node
	 * 
	 * @param marbleOwner
	 *            set to null to have no marble assigned to this node, set to a
	 *            player to assign a marble belonging to the given player to
	 *            this node
	 */
	public void setMarbleOwner(Player marbleOwner)
	{
		this.marbleOwner = marbleOwner;
	}

	/**
	 * @param position
	 *            Direction
	 * @param newNode
	 *            the new node
	 */
	public void setNeighbour(Direction position, Node aNode)
	{
		neighbourList.put(position, aNode);
	}

	/**
	 * @param position
	 *            Direction
	 * @return Node, the newly created node
	 */
	public Node addNeighbour(Direction position)
	{
		Node n = new Node();
		neighbourList.put(position, n);
		return n;
	}

	/**
	 * @param position
	 *            Direction
	 */
	public Node getNeighbour(Direction position)
	{
		return neighbourList.get(position);
	}
}
