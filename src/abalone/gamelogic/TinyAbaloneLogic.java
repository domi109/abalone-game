package abalone.gamelogic;

import java.util.List;

import abalone.adt.KeyValuePair;
import abalone.gamestate.GameState;
import abalone.model.Direction;
import abalone.model.Node;


public class TinyAbaloneLogic extends StandardAbaloneLogic
{	
	public TinyAbaloneLogic()
	{
		this.radius = 2;
		this.marblesToWin = 1;
	}
	
	@Override
	protected void initMarbles(GameState state)
	{
		List<KeyValuePair<Direction, Node>> path = state.getBoard().getEquiPaths().get(0);
		int i = 0;
		for (KeyValuePair<Direction, Node> step : path)
		{
			if (i >= 1 && i <= 2)
			{
				state.setMarble(step.getValue(), state.getPlayers().get(0));
			}
			else if (i >= 4 && i <= 5)
			{
				state.setMarble(step.getValue(), state.getPlayers().get(1));
			}
			i++;
		}
	}
	
}
