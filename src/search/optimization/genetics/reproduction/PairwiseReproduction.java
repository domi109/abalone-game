package search.optimization.genetics.reproduction;

import java.util.Collections;

import search.optimization.genetics.GeneticIndividual;
import search.optimization.genetics.GeneticPopulation;

public class PairwiseReproduction implements ReproductionMethod
{

	private GeneticPopulation pop;
	private int multiplicator = 2;

	public PairwiseReproduction(int multiplicator)
	{
		this.multiplicator = multiplicator;
	}

	public GeneticPopulation getResult()
	{

		GeneticPopulation newGeneration = new GeneticPopulation();

		if (pop.size() % 2 != 0)
		{
			// if there's an odd number of elements, take the fittest into next
			// generation (but still mutate it)
			GeneticIndividual max = pop.getFittest();
			pop.remove(max);
			max.getPhenotype().mutate();
			newGeneration.add(max);
		}
		for (int t = 0; t < multiplicator; t++)
		{
			Collections.shuffle(pop);
			for (int i = 0; i < pop.size(); i += 2)
			{
				GeneticIndividual newGI = pop.get(i).reproduceWith(
						pop.get(i + 1));
				newGI.getPhenotype().mutate();
				newGeneration.add(newGI);
			}
		}

		return newGeneration;
	}

	public int getResultSize()
	{
		if (pop == null)
		{
			return 0;
		}

		return (pop.size() - (pop.size() % 2)) * multiplicator / 2;
	}

	public void setPopulation(GeneticPopulation pop)
	{
		this.pop = pop;
	}

}
