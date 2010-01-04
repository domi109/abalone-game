package abalone.ai;

import search.tree.SearchState;
import search.tree.heuristic.Evaluator;
import abalone.gamestate.GameState;

public class SimpleEvaluator implements Evaluator<Double>
{
	private GameState initialState;
	
	public SimpleEvaluator(GameState initialState)
	{
		this.initialState = initialState;
	}
	@Override
	public Double eval(SearchState state)
	{
		GameState s = (GameState)state;
		int ownLoss = s.getMarblesRemoved().get(initialState.getCurrentPlayer());
		int opponentLoss = s.getMarblesRemoved().get(initialState.getOpponentPlayer());
		return (opponentLoss-ownLoss)/(double)s.getMarblesToWin();
	}

}
