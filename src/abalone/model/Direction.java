package abalone.model;

import java.util.Iterator;
import java.util.NoSuchElementException;

public enum Direction implements Iterable<Direction>
{
	
	UPPER_LEFT,
	UPPER_RIGHT,
	RIGHT,
	DOWN_RIGHT,
	DOWN_LEFT, 
	LEFT;
	
	private class DirectionIterator implements Iterator<Direction>{

		private Direction current,start;
		private boolean hasNext = true;
		
		public DirectionIterator(Direction start)
		{
			this.current = start;
			this.start = start;
		}
		
		@Override
		public boolean hasNext()
		{
			return hasNext;
		}

		@Override
		public Direction next()
		{
			if(!hasNext())
			{
				throw new NoSuchElementException("No next element");
			}
			Direction temp = current;
			current = current.getNextCW();
			hasNext = current != start;
			return temp;
		}

		@Override
		public void remove()
		{
			throw new RuntimeException("Cannot remove a direction");
		}
		
	}
	
	static{
		
		// init opposites
		UPPER_LEFT.opposite = DOWN_RIGHT;
		DOWN_RIGHT.opposite = UPPER_LEFT;
		UPPER_RIGHT.opposite = DOWN_LEFT;
		DOWN_LEFT.opposite = UPPER_RIGHT;
		LEFT.opposite = RIGHT;
		RIGHT.opposite = LEFT;
		
		// init CW nexts
		
		UPPER_LEFT.nextCW = UPPER_RIGHT;
		UPPER_RIGHT.nextCW = RIGHT;
		RIGHT.nextCW = DOWN_RIGHT;
		DOWN_RIGHT.nextCW = DOWN_LEFT;
		DOWN_LEFT.nextCW = LEFT;
		LEFT.nextCW = UPPER_LEFT;
		
		// init ccw nexts
		
		for(Direction d : Direction.values())
		{
			d.nextCW.nextCCW = d;
		}
		
	}
	
	private Direction opposite, nextCW, nextCCW;
	
	Direction()
	{
	}
	
	public Direction getOpposite()
	{
		return opposite;
	}
	
	public Direction getNextCW()
	{
		return nextCW;
	}
	
	public Direction getNextCCW()
	{
		return nextCCW;
	}

	/**
	 * Iterate clockwise through all directions starting from this element
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Direction> iterator()
	{
		return new DirectionIterator(this);
	}
}
