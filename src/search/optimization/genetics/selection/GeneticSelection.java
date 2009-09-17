package search.optimization.genetics.selection;

import search.optimization.genetics.GeneticPopulation;

public interface GeneticSelection
{
	public GeneticPopulation select(GeneticPopulation p,int size);
}
