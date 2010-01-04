package abalone.ai.machinelearning;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import search.genetics.Gene;
import search.genetics.GeneticIndividual;
import search.genetics.Genotype;
import search.tree.SearchState;
import search.tree.heuristic.Evaluator;
import abalone.gamestate.GameState;
import abalone.model.Direction;
import abalone.model.Node;
import abalone.model.Player;

public class AbaloneIndividual implements GeneticIndividual, Evaluator<Double>
{
	private double fitness;
    private Genotype phenotype;
	private GameState initialState;

    /* The mutation rate is the expected percentage of genes that will be mutated. The actual percentage might vary,
       since it depends on a uniformly distributed random function.
       The mutation factor is the the maximum factor by which a gene can mutate. For example, the value of a weight with
       value v will be in the interval [v * (1 - mutationFactor); v * (1 + mutationFactor)]
    */
    public final double mutationRate = 0.1;
    public final double mutationFactor = 0.3;

    public AbaloneIndividual(Genotype phenotype)
    {
        this.phenotype = phenotype;
    }

	/*public AbaloneIndividual(Evaluator evaluator, int size)
	{
		this.heuristic = evaluator;

		for (int i = 0; i < size; i++)
		{
			phenotype.add(new Weight());
		}
	}*/

	public AbaloneIndividual(int size)
	{
		phenotype = new Genotype(size);
	}


	/*public Double eval(SearchState aState)
    {
        return (Double) heuristic.eval(aState);
    }*/

	public void setInitialState(SearchState state)
	{
		this.initialState = (GameState) state;
	}


	public Double eval(SearchState state) {

        // Checks to see if the searchstate given is an instance of a gamestate.
        if (state instanceof GameState) {
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
            for (Node node : currentPlayerMarbles) {
                currentPlayerManhattanDistanceCount += node.getManhDist();
                for (Direction d : Direction.UPPER_LEFT) {
                    if (s.getMarbleOwner(node.getNeighbour(d)) == s.getMarbleOwner(node)) {
                        currentPlayerTeammatesCount++;
                    }
                    if (s.getMarbleOwner(node.getNeighbour(d)) == opponentPlayer && s.getMarbleOwner(node.getNeighbour(d.getOpposite())) == opponentPlayer) {
                        currentPlayerBreakStrongGroupStrategyCount++;
                    }
                    if (s.getMarbleOwner(node.getNeighbour(d)) == opponentPlayer && s.getMarbleOwner(node.getNeighbour(d.getOpposite())) == currentPlayer) {
                        currentPlayerStrengthenGroupStrategyCount++;
                    }
                }
            }

            // Calculations for opponent player.
            int opponentPlayerManhattanDistanceCount = 0;
            int opponentPlayerTeammatesCount = 0;
            int opponentPlayerBreakStrongGroupStrategyCount = 0;
            int opponentPlayerStrengthenGroupStrategyCount = 0;
            for (Node node : opponentPlayerMarbles) {
                opponentPlayerManhattanDistanceCount += node.getManhDist();
                for (Direction d : Direction.UPPER_LEFT) {
                    if (s.getMarbleOwner(node.getNeighbour(d)) == s.getMarbleOwner(node)) {
                        opponentPlayerTeammatesCount++;
                    }
                    if (s.getMarbleOwner(node.getNeighbour(d)) == currentPlayer && s.getMarbleOwner(node.getNeighbour(d.getOpposite())) == currentPlayer) {
                        opponentPlayerBreakStrongGroupStrategyCount++;
                    }
                    if (s.getMarbleOwner(node.getNeighbour(d)) == currentPlayer && s.getMarbleOwner(node.getNeighbour(d.getOpposite())) == opponentPlayer) {
                        opponentPlayerStrengthenGroupStrategyCount++;
                    }
                }
            }

            // Calculation of individual functions
	        ArrayList<Integer> functionResults = new ArrayList<Integer>();
            functionResults.add(opponentPlayerManhattanDistanceCount - currentPlayerManhattanDistanceCount);
            functionResults.add(currentPlayerTeammatesCount - opponentPlayerTeammatesCount);
            functionResults.add(currentPlayerBreakStrongGroupStrategyCount - opponentPlayerBreakStrongGroupStrategyCount);
            functionResults.add(currentPlayerStrengthenGroupStrategyCount - opponentPlayerStrengthenGroupStrategyCount);
            functionResults.add(lostMarbles.get(opponentPlayer));
            functionResults.add(lostMarbles.get(currentPlayer));

            // Evaluation.
	        double eval = 0;

	        for (int i = 0; i < functionResults.size(); i++)
	        {
		        double weight = (Double) phenotype.get(i).getValue();
		        eval += weight * functionResults.get(i);
	        }
            return eval;
        } else {
            return 0.0;
        }
    }

    public double getFitness()
    {
        return fitness;
    }

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
        if(phenotype.size() != j.getPhenotype().size())
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
	    //TODO: replace reproduction with something that makes sense

	    Genotype newPhenotype = new Genotype(new ArrayList<Gene>());

	    Random r = new Random();

	    for (int i = 0; i < phenotype.size(); i++)
	    {
		    if (r.nextDouble() < 0.5)
		    {
			    newPhenotype.add(this.getPhenotype().get(i));
		    }
		    else
		    {
			    newPhenotype.add(j.getPhenotype().get(i));
		    }
	    }

        return new AbaloneIndividual(newPhenotype);
    }

    public void mutate()
    {
        Random r = new Random();

        for (int i = 0; i < phenotype.size(); i++)
        {
            if (r.nextDouble() < mutationRate)
            {
                double value = (Double) phenotype.get(i).getValue();
                value += ((2 * mutationFactor * r.nextDouble())-mutationFactor);
                phenotype.get(i).setValue(value);
            }
        }
    }

    public int compareTo(GeneticIndividual genInd)
    {
        return Double.compare(fitness, genInd.getFitness());
    }

	public String toString()
	{
		String str = "";

		for (Gene g : phenotype)
		{
			str += g.toString() + " ";
		}

		return str;
	}
}
