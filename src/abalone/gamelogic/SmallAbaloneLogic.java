package abalone.gamelogic;

import java.util.List;

import abalone.adt.KeyValuePair;
import abalone.gamestate.GameState;
import abalone.model.Direction;
import abalone.model.Node;


public class SmallAbaloneLogic extends StandardAbaloneLogic
{	
	public SmallAbaloneLogic()
	{
		this.radius = 3;
		this.marblesToWin = 2;
	}
	
	
	@Override
	protected void initMarbles(GameState state)
	{
		List<KeyValuePair<Direction, Node>> path = state.getBoard().getEquiPaths().get(0);
		int i = 0;
		for (KeyValuePair<Direction, Node> step : path)
		{
			if ((i >= 8 && i <= 10) || (i >= 1 && i <= 2))
			{
				state.setMarble(step.getValue(), state.getPlayers().get(0));
			}
			else if ((i >= 14 && i <= 16) || (i >= 4 && i <= 5))
			{
				state.setMarble(step.getValue(), state.getPlayers().get(1));
			}
			i++;
		}
	}
	
}
