package search.optimization.genetics.reproduction;

import java.util.Collections;
import java.util.Random;

import search.optimization.genetics.GeneticIndividual;
import search.optimization.genetics.GeneticPopulation;

public class LimitedKeepBestNoCrossoverReproduction implements ReproductionMethod
{
	private int keep;
	private GeneticPopulation pop;
	private int limit;

	public LimitedKeepBestNoCrossoverReproduction(int limit, int keepBestGroupSize)
	{
		keep = keepBestGroupSize;
		this.limit = limit;
	}

	public GeneticPopulation getResult()
	{

		GeneticPopulation newGeneration = new GeneticPopulation();

		for (int i = 0; i < keep; i++)
		{
			// Take the fittest into next generation without mutation
			GeneticIndividual max = pop.getFittest();
			pop.remove(max);
			newGeneration.add(max);
		}

		Collections.sort(pop);
		int i = 0;
		while (newGeneration.size() < limit)
		{
			GeneticIndividual newGI = pop.get(i%pop.size()).reproduceWith(pop.get(i%pop.size()));
			newGI.getPhenotype().mutate();
			newGeneration.add(newGI);
			i++;
		}

		return newGeneration;
	}

	public int getResultSize()
	{
		if (pop == null)
		{
			return 0;
		}

		return limit;
	}

	public void setPopulation(GeneticPopulation pop)
	{
		this.pop = pop;
	}

}
