package abalone.ai.machinelearning.TournamentModes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import nl.maastrichtuniversity.dke.libreason.def.heuristic.Evaluator;
import nl.maastrichtuniversity.dke.libreason.genetics.FitnessEvaluator;
import nl.maastrichtuniversity.dke.libreason.genetics.GeneticIndividual;
import nl.maastrichtuniversity.dke.libreason.genetics.GeneticPopulation;
import abalone.ai.SimpleAI;
import abalone.ai.evaluation.LinearEvaluator;
import abalone.ai.machinelearning.MultiThreadedPlay;
import abalone.gamelogic.GameLogic;
import abalone.gamelogic.StandardAbaloneLogic;
import abalone.gamestate.GameState;
import abalone.model.Board;
import abalone.model.Move;
import abalone.model.Player;

public class CrossTournamentMultiThreaded implements FitnessEvaluator
{
	private GeneticPopulation pop;
	private MultiThreadedPlay play;

	public CrossTournamentMultiThreaded()
	{
		play = new MultiThreadedPlay(80);
	}

	@Override
	public GeneticPopulation eval(GeneticPopulation aPop)
	{
		// The individuals will probably play multiple matches. Therefore, the
		// gathered
		// statistics are stored in a map. After all matches have been played,
		// the fitness
		// of each individual is calculated using these stats.
		this.pop = aPop;

		// random matches for each Individual - just for testing purposes

		int counter = 0;
		for (int i = 0; i < pop.size(); i++)
		{
			for (int j = i + 1; j < pop.size(); j++)
			{
				play.addMatch(pop.get(i), i, pop.get(j), j);

				counter++;
			}
		}
		System.out.println("all matches added");
		play.runMatches();

		return pop;
	}
}