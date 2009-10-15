package abalone.gamelogic;

import java.util.Map.Entry;

import abalone.gamestate.GameState;
import abalone.model.Board;
import abalone.model.Direction;
import abalone.model.Player;

public class SmallAbaloneLogic extends StandardAbaloneLogic
{
	public SmallAbaloneLogic()
	{
		this.radius = 3;
	}
	
	/**
	 * Return the player who has won and null if there currently is no winner at
	 * all.
	 * 
	 * @see abalone.gamelogic.GameLogic#getWinner(abalone.gamestate.GameState)
	 */
	@Override
	public Player getWinner(GameState state)
	{
		for (Entry<Player, Integer> e : state.getMarblesRemoved().entrySet())
		{
			if (e.getValue() >= 2)
			{
				return e.getKey();
			}
		}
		return null;
	}}
