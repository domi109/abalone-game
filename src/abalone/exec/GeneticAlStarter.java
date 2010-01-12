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
		long time = System.currentTimeMillis();

		GeneticSearch search = new GeneticSearch(generatePop(16), new ElitistSelection(), new KeepBestPairwiseReproduction(2, 2), new CrossTournamentMultiThreaded());
		search.setSelectionSize(8);

		int numberOfGenerations = 10;

		try
		{
			long totalRunTime = 0;
		while(search.getGeneration() < numberOfGenerations)
		{
			long timeGen = System.currentTimeMillis();
			
			search.spawnGeneration();
	
			GeneticPopulation pop = search.getPopulation();
			System.out.print(search.getGeneration() + "/" + numberOfGenerations + "   ");
			
			totalRunTime += System.currentTimeMillis() - timeGen;
			long averageTime = (totalRunTime / search.getGeneration() * (numberOfGenerations - search.getGeneration())) / 1000; // in seconds
			System.out.print("Estimated time left: " + ((int) averageTime / 3600) + "h " + ((int) (averageTime % 3600) / 60) + "m " + (averageTime % 60) + "s");
			System.out.println();
		}
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException("Unexpected Interrupt");
		}
		System.out.println("Finished!");
		System.out.println("Running time: " + (System.currentTimeMillis() - time)/1000 + " sec");
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
