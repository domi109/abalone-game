package search.genetics.reproduction;

import java.util.Collections;
import java.util.Random;

import search.genetics.GeneticIndividual;
import search.genetics.GeneticPopulation;

public class LimitedPairwiseReproduction implements ReproductionMethod
{

	private GeneticPopulation pop;
	private int limit;

	public LimitedPairwiseReproduction(int limit)
	{
		this.limit = limit;
	}

	public GeneticPopulation getResult()
	{

		GeneticPopulation newGeneration = new GeneticPopulation();
		Random r = new Random();
		Collections.sort(pop);
		int i = 0;
		while (newGeneration.size() < limit)
		{
			GeneticIndividual newGI = pop.get(i%pop.size())
					.reproduceWith(pop.get(r.nextInt(pop.size() - 1)));
			newGI.mutate();
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
