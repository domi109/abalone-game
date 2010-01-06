package abalone.ai.machinelearning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import search.genetics.GeneticPopulation;
import search.genetics.GeneticSearch;
import search.genetics.reproduction.CrossReproduction;
import search.genetics.reproduction.KeepBestPairwiseReproduction;
import search.genetics.reproduction.NoCrossoverReproduction;
import search.genetics.selection.ElitistSelection;
import abalone.gamelogic.StandardAbaloneLogic;

public class GeneticAlStarter
{
	public static void main(String[] args)
	{
		GeneticSearch search = new GeneticSearch(generatePop(16, 6), new ElitistSelection(), new KeepBestPairwiseReproduction(2, 2), new Tournament());
		search.setSelectionSize(8);

		int numberOfGenerations = 50;

		while(search.getGeneration() < numberOfGenerations)
		{
			search.spawnGeneration();
		}
		//printPop(search.getPopulation());
	}

	private static GeneticPopulation generatePop(int popSize, int genotypeSize)
	{
		GeneticPopulation pop = new GeneticPopulation();

		for (int i = 0; i < popSize; i++)
		{
			pop.add(new AbaloneIndividual(genotypeSize));
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