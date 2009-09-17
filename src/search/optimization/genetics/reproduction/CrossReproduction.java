package search.optimization.genetics.reproduction;

import search.optimization.genetics.GeneticIndividual;
import search.optimization.genetics.GeneticPopulation;

public class CrossReproduction implements ReproductionMethod
{
	private GeneticPopulation pop;
	
	public GeneticPopulation getResult()
	{
		GeneticPopulation newGeneration = new GeneticPopulation();
		for(GeneticIndividual i : pop)
		{
			for(GeneticIndividual j: pop)
			{
				GeneticIndividual newGI = i.reproduceWith(j);
				newGI.getPhenotype().mutate();
				newGeneration.add(newGI);
			}
		}
		return newGeneration;
	}

	public int getResultSize()
	{
		if(pop == null)
		{
		return 0;
		}
		return pop.size()*pop.size();
	}

	public void setPopulation(GeneticPopulation pop)
	{
		this.pop = pop;
	}

}
