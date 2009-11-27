/*
 *  A linear, weighted evaluation function based on a paper 'Constructing an Abalone Game-Playing Agent'
 *  by N.P.P.M. Lemmens (18th June 2005).
 *
 *  However this is a very optimized version to be much quicker than LinearEvaluator class.
 *
 */
package abalone.ai;

// Imports from Java libraries.
import java.util.Map;
import java.util.Set;

// Imports from Model library.
import abalone.model.Player;
import abalone.model.Node;
import abalone.model.Direction;

// Import from Search library.
import search.tree.heuristic.Evaluator;
import search.tree.SearchState;

// Imports from Gamestate library.
import abalone.gamestate.GameState;

public class OptimizedLinearEvaluator implements Evaluator<Double>
{

	// Variable for initial game state.
	private GameState initialState;

	// Variables for the individual functions.
	private double f1;
	private double f2;
	private double f3;
	private double f4;
	private double f5;
	private double f6;

	// Variables for weights of each strategy.
	private double w1;
	private double w2;
	private double w3;
	private double w4;
	private double w5;
	private double w6;

	// Constructor for LinearEvaluator class.
	public OptimizedLinearEvaluator(GameState initialState)
	{

		// Set the initialState.
		this.initialState = initialState;

		// Ensure functions are set back to default values.
		f1 = 0;
		f2 = 0;
		f3 = 0;
		f4 = 0;
		f5 = 0;
		f6 = 0;

		// Ensure weights are set back to default values.
		w1 = .05;
		w2 = .01;
		w3 = .01;
		w4 = .01;
		w5 = 1;
		w6 = 1;
	}

	/**
	 * Method which evaluates the desirability of a move done in a search state.
	 * 
	 * @param state
	 *            a searchstate which the AI wishes to evaluate
	 * @return the desirability of a searched move
	 */
	@Override
	public Double eval(SearchState state)
	{

		double eval = 0;

		// Checks to see if the searchstate given is an instance of a gamestate.
		if (state instanceof GameState)
		{
			// Cast searchstate to gamestate.
			GameState s = (GameState) state;
			// Get the current player.
			Player currentPlayer = initialState.getCurrentPlayer();
			// Get the opponent player.
			Player opponentPlayer = initialState.getOpponentPlayer();
			// Get current player's marbles.
			Set<Node> currentPlayerMarbles = s.getMarbles(currentPlayer);
			// Get opponent player's marbles.
			Set<Node> opponentPlayerMarbles = s.getMarbles(opponentPlayer);
			// Get the lost marbles per player.
			Map<Player, Integer> lostMarbles = s.getMarblesRemoved();

			// Calculations for current player.
			int currentPlayerManhattanDistanceCount = 0;
			int currentPlayerTeammatesCount = 0;
			int currentPlayerBreakStrongGroupStrategyCount = 0;
			int currentPlayerStrengthenGroupStrategyCount = 0;
			for (Node node : currentPlayerMarbles)
			{
				currentPlayerManhattanDistanceCount += node.getManhDist();
				for (Direction d : Direction.UPPER_LEFT)
				{
					if (s.getMarbleOwner(node.getNeighbour(d)) == s.getMarbleOwner(node))
					{
						currentPlayerTeammatesCount++;
					}
					if (s.getMarbleOwner(node.getNeighbour(d)) == opponentPlayer && s.getMarbleOwner(node.getNeighbour(d.getOpposite())) == opponentPlayer)
					{
						currentPlayerBreakStrongGroupStrategyCount++;
					}
					if (s.getMarbleOwner(node.getNeighbour(d)) == opponentPlayer && s.getMarbleOwner(node.getNeighbour(d.getOpposite())) == currentPlayer)
					{
						currentPlayerStrengthenGroupStrategyCount++;
					}
				}
			}

			// Calculations for opponent player.
			int opponentPlayerManhattanDistanceCount = 0;
			int opponentPlayerTeammatesCount = 0;
			int opponentPlayerBreakStrongGroupStrategyCount = 0;
			int opponentPlayerStrengthenGroupStrategyCount = 0;
			for (Node node : opponentPlayerMarbles)
			{
				opponentPlayerManhattanDistanceCount += node.getManhDist();
				for (Direction d : Direction.UPPER_LEFT)
				{
					if (s.getMarbleOwner(node.getNeighbour(d)) == s.getMarbleOwner(node))
					{
						opponentPlayerTeammatesCount++;
					}
					if (s.getMarbleOwner(node.getNeighbour(d)) == currentPlayer && s.getMarbleOwner(node.getNeighbour(d.getOpposite())) == currentPlayer)
					{
						opponentPlayerBreakStrongGroupStrategyCount++;
					}
					if (s.getMarbleOwner(node.getNeighbour(d)) == currentPlayer && s.getMarbleOwner(node.getNeighbour(d.getOpposite())) == opponentPlayer)
					{
						opponentPlayerStrengthenGroupStrategyCount++;
					}
				}
			}

			// Calculation of individual functions.
			f1 = opponentPlayerManhattanDistanceCount - currentPlayerManhattanDistanceCount;
			f2 = currentPlayerTeammatesCount - opponentPlayerTeammatesCount;
			f3 = currentPlayerBreakStrongGroupStrategyCount - opponentPlayerBreakStrongGroupStrategyCount;
			f4 = currentPlayerStrengthenGroupStrategyCount - opponentPlayerStrengthenGroupStrategyCount;
			f5 = lostMarbles.get(opponentPlayer);
			f6 = lostMarbles.get(currentPlayer);

			// Evaluation.
			eval = (w1 * f1) + (w2 * f2) + (w3 * f3) + (w4 * f4) + (w5 * f5) - (w6 * f6);
			return eval;
		}
		else
		{
			return 0.0;
		}
	}

	/**
	 * Getter method which gives weight of function 1 (w1).
	 * 
	 * @return the weight of function 1 (w1)
	 */
	public double getW1()
	{
		return w1;
	}

	/**
	 * Setter method which changes weight of function 1 (w1).
	 * 
	 * @param w1
	 *            the new weight of function 1 (w1)
	 */
	public void setW1(double w1)
	{
		this.w1 = w1;
	}

	/**
	 * Getter method which gives weight of function 2 (w2).
	 * 
	 * @return the weight of function 2 (w2)
	 */
	public double getW2()
	{
		return w2;
	}

	/**
	 * Setter method which changes weight of function 2 (w2).
	 * 
	 * @param w2
	 *            the new weight of function 2 (w2)
	 */
	public void setW2(double w2)
	{
		this.w2 = w2;
	}

	/**
	 * Getter method which gives weight of function 3 (w3).
	 * 
	 * @return the weight of function 3 (w3)
	 */
	public double getW3()
	{
		return w3;
	}

	/**
	 * Setter method which changes weight of function 3 (w3).
	 * 
	 * @param w3
	 *            the new weight of function 3 (w3)
	 */
	public void setW3(double w3)
	{
		this.w3 = w3;
	}

	/**
	 * Getter method which gives weight of function 4 (w4).
	 * 
	 * @return the weight of function 4 (w4)
	 */
	public double getW4()
	{
		return w4;
	}

	/**
	 * Setter method which changes weight of function 4 (w4).
	 * 
	 * @param w4
	 *            the new weight of function 4 (w4)
	 */
	public void setW4(double w4)
	{
		this.w4 = w4;
	}

	/**
	 * Getter method which gives weight of function 5 (w5).
	 * 
	 * @return the weight of function 5 (w5)
	 */
	public double getW5()
	{
		return w5;
	}

	/**
	 * Setter method which changes weight of function 5 (w5).
	 * 
	 * @param w5
	 *            the new weight of function 5 (w5)
	 */
	public void setW5(double w5)
	{
		this.w5 = w5;
	}

	/**
	 * Getter method which gives weight of function 6 (w6).
	 * 
	 * @return the weight of function 6 (w6)
	 */
	public double getW6()
	{
		return w6;
	}

	/**
	 * Setter method which changes weight of function 6 (w6).
	 * 
	 * @param w6
	 *            the new weight of function 6 (w6)
	 */
	public void setW6(double w6)
	{
		this.w6 = w6;
	}

	public double getF1()
	{
		return f1;
	}

	public double getF2()
	{
		return f2;
	}

	public double getF3()
	{
		return f3;
	}

	public double getF4()
	{
		return f4;
	}

	public double getF5()
	{
		return f5;
	}

	public double getF6()
	{
		return f6;
	}

}
