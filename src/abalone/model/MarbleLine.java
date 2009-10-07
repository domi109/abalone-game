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
	
	public void remove(Node node)
	{
		this.marbles.remove(node);
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
        if(nrMarbles >= 3)
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

	public boolean checkRemove(Node node)
	{
        int nrMarbles = marbles.size();
        if(nrMarbles == 0)
        {
        	return false;
        }
        
        if(nrMarbles == 1 || nrMarbles == 2)
        {
        	remove(node);
        	return true;
        }
        
        boolean legal = true;
        if(nrMarbles == 3){
        	if(
        			marbles.contains(node.getNeighbour(getOrientation()))
        		&&  marbles.contains(node.getNeighbour(getOrientation().getOpposite()))
        	  )
        	{
        		legal = false;
        	}
        }
        
        if(legal)
        {
        	remove(node);
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
