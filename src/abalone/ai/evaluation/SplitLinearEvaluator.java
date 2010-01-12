/*
 *  A linear, weighted evaluation function based on a paper 'Constructing an Abalone Game-Playing Agent'
 *  by N.P.P.M. Lemmens (18th June 2005).
 *
 *  The weights are trained in this version, and can also be adjusted. Each function has been split into
 *  opponent and own player parts.
 */
package abalone.ai.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import nl.maastrichtuniversity.dke.libreason.def.SearchState;
import nl.maastrichtuniversity.dke.libreason.def.heuristic.Evaluator;
import nl.maastrichtuniversity.dke.libreason.genetics.Gene;
import nl.maastrichtuniversity.dke.libreason.genetics.GeneticIndividual;
import nl.maastrichtuniversity.dke.libreason.genetics.Genotype;
import abalone.ai.machinelearning.Weight;
import abalone.gamestate.GameState;
import abalone.model.Direction;
import abalone.model.Node;
import abalone.model.Player;

public class SplitLinearEvaluator implements GeneticIndividual, Evaluator<Double>
{
	private double fitness;
	private Genotype phenotype;
	private GameState initialState;

	/*
	 * The mutation rate is the expected percentage of genes that will be
	 * mutated. The actual percentage might vary, since it depends on a
	 * uniformly distributed random function. The mutation factor is the the
	 * maximum factor by which a gene can mutate. For example, the value of a
	 * weight with value v will be in the interval [v * (1 - mutationFactor); v
	 * * (1 + mutationFactor)]
	 */
	public final double mutationRate = 0.2;
	public final double mutationFactor = 1;

	// Variables for scaling the functions.
	private List<Integer> max = new ArrayList<Integer>(6);
	private List<Integer> functionResults;

	
	private static Genotype generatePhenotype()
	{
		Genotype phenotype = new Genotype();
		for(int i = 0; i < 10;i++)
		{
			phenotype.add(new Weight());
		}
		return phenotype;
	}
	
	public SplitLinearEvaluator(Genotype phenotype)
	{
		this.phenotype = phenotype;
		max.add(0, 46);
		max.add(1, 46);
		max.add(2, 56);
		max.add(3, 56);
		max.add(4, 11);
		max.add(5, 11);
		max.add(6, 84);
		max.add(7, 84);
		max.add(8, 6);
		max.add(9, 6);
	}


	public SplitLinearEvaluator()
	{
		this(generatePhenotype());

		scalePhenotype();
	}

	public void setInitialState(SearchState state)
	{
		this.initialState = (GameState) state;
	}

	public Double eval(SearchState state)
	{

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
			// Calculation of individual functions
			functionResults = new ArrayList<Integer>();
			functionResults.add(0,opponentPlayerManhattanDistanceCount);
			functionResults.add(1,currentPlayerManhattanDistanceCount);
			functionResults.add(2,currentPlayerTeammatesCount);
			functionResults.add(3,opponentPlayerTeammatesCount);
			functionResults.add(4,currentPlayerBreakStrongGroupStrategyCount);
			functionResults.add(5,opponentPlayerBreakStrongGroupStrategyCount);
			functionResults.add(6,currentPlayerStrengthenGroupStrategyCount);
			functionResults.add(7,opponentPlayerStrengthenGroupStrategyCount);
			functionResults.add(8,lostMarbles.get(opponentPlayer));
			functionResults.add(9,lostMarbles.get(currentPlayer));

			// Evaluation.
			double eval = 0;

			for (int i = 0; i < functionResults.size(); i++)
			{
				double weight = (Double) phenotype.get(i).getValue();				
				eval += weight * (functionResults.get(i) / ((double) max.get(i)));
			}
			return eval;
		}
		else
		{
			return 0.0;
		}
	}

	public List<Integer> getFunctionResults()
	{
		return functionResults;
	}
	
	public double getFitness()
	{
		return fitness;
	}

	@Override
	public void setFitness(double value)
	{
		fitness = value;
	}

	public Genotype getPhenotype()
	{
		return phenotype;
	}

	public boolean equalsGenetic(GeneticIndividual j)
	{
		if (phenotype.size() != j.getPhenotype().size())
		{
			return false;
		}

		for (int i = 0; i < phenotype.size(); i++)
		{
			if (!(phenotype.get(i).equals(j.getPhenotype().get(i))))
			{
				return false;
			}
		}

		return true;
	}

	public GeneticIndividual reproduceWith(GeneticIndividual j)
	{
		Random r = new Random();
		int splitPoint = r.nextInt(phenotype.size());
		Genotype newPhenotype = new Genotype();
		for (int i = 0; i < splitPoint; i++)
		{
			newPhenotype.add(this.getPhenotype().get(i).clone());
		}
		for (int i = splitPoint; i < phenotype.size(); i++)
		{
			newPhenotype.add(j.getPhenotype().get(i).clone());
		}

		return new SplitLinearEvaluator(newPhenotype);
	}

	public void mutate()
	{
		Random r = new Random();

		for (Gene<Double> w : phenotype)
		{
			if (r.nextDouble() < mutationRate)
			{
				double value = (Double) w.getValue();
				value += mutationFactor*(2*r.nextDouble() - 1);
				//value = (2*r.nextDouble()-1);
				w.setValue(value);
			}
		}

		scalePhenotype();
	}

	public int compareTo(GeneticIndividual genInd)
	{
		return Double.compare(fitness, genInd.getFitness());
	}

	@Override
	public String toString()
	{
		String str = "";

		for (Gene<Double> g : phenotype)
		{
			str += g.toString() + " ";
		}

		return str;
	}

	private void scalePhenotype()
	{
		double totalSum = 0;

		for (Gene<Double> weight : phenotype)
		{
			totalSum += Math.abs((Double) weight.getValue());
		}

		for (Gene<Double> weight : phenotype)
		{
			double scaledValue = ((Double) weight.getValue()) / totalSum;
			weight.setValue(scaledValue);
		}
	}
}
