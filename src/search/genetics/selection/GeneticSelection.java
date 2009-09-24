package search.genetics.selection;

import search.genetics.GeneticPopulation;

public interface GeneticSelection
{
	public GeneticPopulation select(GeneticPopulation p,int size);
}
