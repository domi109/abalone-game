package abalone.model;

import java.util.ArrayList;
import java.util.List;

public class MarbleLine
{
	private List<Node> marbles;
	private Direction orientation;
	
	public MarbleLine()
	{
		this.marbles = new ArrayList<Node>();
	}
	
	
	public void add(Node node)
	{
		this.marbles.add(node);
	}
	
	public Direction getOrientation()
	{
		return orientation;
	}
	
	public void setOrientation(Direction orientation)
	{
		this.orientation = orientation;
	}
	
	public List<Node> getNodes()
	{
		return marbles;
	}
	
	/**
	 * Checks whether this is a list and sets the orientation.
	 */
	public boolean checkAdd(Node node)
	{
        Direction neighbourDir;
        int nrMarbles = marbles.size();
        if(nrMarbles >= 2)
        {
        	return false;
        }
        
        boolean legal = true;
        
        if(nrMarbles == 1)
        {
            neighbourDir = findNeighbourDirection(marbles.get(0), node);
            if (neighbourDir == null) 
            {
            	legal = false;
            }
            else
            {
            	orientation=neighbourDir;
            }
        }

        if(nrMarbles == 2){
        	legal = false;
            for(Node n : marbles)
            {
            	if(n.getNeighbour(orientation).equals(node)
            			|| n.getNeighbour(orientation.getOpposite()).equals(node))
            	{
            		legal = true;
            		break;
            	}
            }
        }
        
        if(legal)
        {
        	add(node);
        }
        return legal;
	}
	
    private Direction findNeighbourDirection(Node node, Node neighbour){
    	Direction dir = null;
        for(Direction direction : Direction.values()){
            if(node.getNeighbour(direction) == neighbour){
                dir = direction;
            }
        }
        return dir;
    }
}
