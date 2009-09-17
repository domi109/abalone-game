package search.optimization.genetics.selection;

import java.util.Collections;

import search.optimization.genetics.GeneticIndividual;
import search.optimization.genetics.GeneticPopulation;

public class ElitistSelection implements GeneticSelection
{
	public GeneticPopulation select(GeneticPopulation pop, int size)
	{
		GeneticPopulation selected = new GeneticPopulation();
		for(int i = 0; i< size; i++)
		{
			GeneticIndividual max = pop.getFittest();
			selected.add(max);
			pop.remove(max);
			
		}
		return selected;
	}
}
