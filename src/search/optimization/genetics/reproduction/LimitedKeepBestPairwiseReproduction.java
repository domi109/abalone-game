package search.optimization.genetics.reproduction;

import java.util.Collections;
import java.util.Random;

import search.optimization.genetics.GeneticIndividual;
import search.optimization.genetics.GeneticPopulation;

public class LimitedKeepBestPairwiseReproduction implements ReproductionMethod
{

	private GeneticPopulation pop;
	private int keep;
	private int limit;

	public LimitedKeepBestPairwiseReproduction(int limit,int keepBestGroupSize)
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
		Random r = new Random();
		Collections.sort(pop);
		int i = 0;
		while (newGeneration.size() < limit)
		{
			GeneticIndividual newGI = pop.get(i%pop.size())
					.reproduceWith(pop.get(r.nextInt(pop.size() - 1)));
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

		return pop.size();
	}

	public void setPopulation(GeneticPopulation pop)
	{
		this.pop = pop;
	}

}
