/*
 *  A simple evaluator, which compares moves based upon if a marble is lost or not.
 *
 */
package abalone.ai.evaluation;

import nl.maastrichtuniversity.dke.libreason.def.SearchState;
import nl.maastrichtuniversity.dke.libreason.def.heuristic.Evaluator;
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
