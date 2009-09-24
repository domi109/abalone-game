package search.genetics.reproduction;

import search.genetics.GeneticIndividual;
import search.genetics.GeneticPopulation;

public class NoCrossoverReproduction implements ReproductionMethod
{

	private GeneticPopulation pop;

	public GeneticPopulation getResult()
	{

		GeneticPopulation newGeneration = new GeneticPopulation();
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
