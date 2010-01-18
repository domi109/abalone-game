package abalone.exec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import nl.maastrichtuniversity.dke.libreason.genetics.GeneticPopulation;
import nl.maastrichtuniversity.dke.libreason.genetics.GeneticSearch;
import nl.maastrichtuniversity.dke.libreason.genetics.reproduction.KeepBestPairwiseReproduction;
import nl.maastrichtuniversity.dke.libreason.genetics.selection.ElitistSelection;
import abalone.ai.evaluation.LinearEvaluator;
import abalone.ai.machinelearning.TournamentModes.CrossTournament;
import abalone.ai.machinelearning.TournamentModes.KOTournament;
import abalone.ai.machinelearning.TournamentModes.RandomMatchTournament;

public class GeneticAlStarter
{
	public static void main(String[] args)
	{
		GeneticPopulation pop = null;
		File file = new File("population.ser");
		if(file.exists())
		{
			pop = loadPop();
		}
		else
		{
			pop = generatePop(20);
		}
		GeneticSearch search = new GeneticSearch(pop, new ElitistSelection(), new KeepBestPairwiseReproduction(4, 2), new CrossTournament());
		search.setSelectionSize(5);

		int numberOfGenerations = 100;

		try
		{
		while(search.getGeneration() < numberOfGenerations)
		{
			savePop(search.getPopulation());

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

	private static void savePop(GeneticPopulation pop)
	{
		try
		{
			FileOutputStream fs = new FileOutputStream("population.ser");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			
			os.writeObject(pop);

			os.close();
			fs.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static GeneticPopulation loadPop()
	{
		try
		{
			FileInputStream fs = new FileInputStream("population.ser");
			ObjectInputStream os = new ObjectInputStream(fs);
			
			GeneticPopulation pop = (GeneticPopulation) os.readObject();

			os.close();
			fs.close();
			return pop;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
