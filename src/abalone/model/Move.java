package abalone.model;

import java.util.List;

import search.Action;


public class Move extends Action
{
	MarbleLine line;
	Direction direction;
	
	public MarbleLine getMarbleLine()
	{
		return line;
	}
	public void setMarbleLine(MarbleLine line)
	{
		this.line = line;
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
