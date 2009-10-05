package abalone.model;

import java.util.List;

import search.Action;


public class Move extends Action
{
	List<Node> nodes;
	Direction direction;
	
	public List<Node> getNodes()
	{
		return nodes;
	}
	public void setNodes(List<Node> nodes)
	{
		this.nodes = nodes;
	}
	public Direction getDirection()
	{
		return direction;
	}
	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}
}
