package abalone.exec;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import nl.maastrichtuniversity.dke.libreason.genetics.GeneticPopulation;
import nl.maastrichtuniversity.dke.libreason.genetics.GeneticSearch;
import nl.maastrichtuniversity.dke.libreason.genetics.reproduction.KeepBestPairwiseReproduction;
import nl.maastrichtuniversity.dke.libreason.genetics.selection.ElitistSelection;
import abalone.ai.evaluation.LinearEvaluator;
import abalone.ai.machinelearning.TournamentModes.CrossTournamentMultiThreaded;
import abalone.ai.machinelearning.TournamentModes.KOTournament;

public class GeneticAlStarter
{
	public static void main(String[] args)
	{
		GeneticSearch search = new GeneticSearch(generatePop(16), new ElitistSelection(), new KeepBestPairwiseReproduction(2, 2), new CrossTournamentMultiThreaded());
		search.setSelectionSize(8);

		int numberOfGenerations = 50;

		try
		{
		while(search.getGeneration() < numberOfGenerations)
		{
			search.spawnGeneration();
		}
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException("Unexpected Interrupt");
		}
	}

	private static GeneticPopulation generatePop(int popSize)
	{
		GeneticPopulation pop = new GeneticPopulation();

		for (int i = 0; i < popSize; i++)
		{
			pop.add(new LinearEvaluator());
		}

		return pop;
	}

	private static void printPop(GeneticPopulation pop)
	{
		try
		{
			Writer writer = new BufferedWriter(new FileWriter("output.txt"));

			writer.write(pop.getResults());

			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
