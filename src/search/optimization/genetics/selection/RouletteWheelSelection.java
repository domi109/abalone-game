package search.optimization.genetics.selection;

import java.util.Collections;

import search.optimization.genetics.GeneticIndividual;
import search.optimization.genetics.GeneticPopulation;


public class RouletteWheelSelection implements GeneticSelection
{
	public GeneticPopulation select(GeneticPopulation pop, int size)
	{
		GeneticPopulation selected = new GeneticPopulation();
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
		//System.out.println(selected);
		return selected;
	}
}
