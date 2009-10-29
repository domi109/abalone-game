package abalone.model;

import java.util.List;

import search.Action;


public class Move extends Action
{
	public enum MoveType
	{
		UNKNOWN,
		SUMITO, // pushing an opponents marble, includes INLINE
		INLINE, // moving inline
		BROADSIDE, // moving broadside
		PUSHOFF; // pushing an opponents marble off the board, includes SUMITO
	}
	
	private MarbleLine line;
	private Direction direction;
	private MoveType type;
	
	public MoveType getType()
	{
		return type;
	}

	public void setType(MoveType type)
	{
		this.type = type;
	}

	public Move()
	{
		type = MoveType.UNKNOWN;
	}
	
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
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer(type+"[");
		for(Node n : line.getNodes())
		{
			sb.append(n.toString() + " ");
		}
		sb.append("]->"+direction);
		
		return sb.toString();
	}
}
