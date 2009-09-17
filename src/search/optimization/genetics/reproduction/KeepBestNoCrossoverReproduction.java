package search.optimization.genetics.reproduction;

import search.optimization.genetics.GeneticIndividual;
import search.optimization.genetics.GeneticPopulation;

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
			newGI.getPhenotype().mutate();
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