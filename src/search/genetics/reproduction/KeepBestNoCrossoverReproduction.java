package search.genetics.reproduction;

import search.genetics.GeneticIndividual;
import search.genetics.GeneticPopulation;

public class KeepBestNoCrossoverReproduction implements ReproductionMethod
{
	private int keep;
	private GeneticPopulation pop;

	public KeepBestNoCrossoverReproduction(int keepBestGroupSize)
	{
		keep = keepBestGroupSize;
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

		for (int i = 0; i < pop.size(); i++)
		{
			GeneticIndividual newGI = pop.get(i);
			newGI.mutate();
			newGeneration.add(newGI);
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
