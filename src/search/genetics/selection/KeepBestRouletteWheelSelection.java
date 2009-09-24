package search.genetics.selection;

import java.util.Collections;

import search.genetics.GeneticIndividual;
import search.genetics.GeneticPopulation;


public class KeepBestRouletteWheelSelection implements GeneticSelection
{
	private int keep;
	
	public KeepBestRouletteWheelSelection(int keepBestGroupSize)
	{
		keep = keepBestGroupSize;
	}
	
	public GeneticPopulation select(GeneticPopulation pop, int size)
	{
		GeneticPopulation selected = new GeneticPopulation();

		for(int i = 0; i< keep; i++)
		{
			GeneticIndividual gi = Collections.max(pop);
			selected.add(gi);
			pop.remove(gi);
		}
		
		for(int i = 0; i< size; i++)
		{
			double n = Math.random()*pop.getTotalFitness();
			double wheelPos=0;
			for(int j = 0; j<pop.size();j++)
			{
				GeneticIndividual gi = pop.get(j);
				wheelPos+=gi.getFitness();
				
				if(wheelPos >= n)
				{
					selected.add(gi);
					j = pop.size();
				}
			}
			
		}
		return selected;
	}
}
