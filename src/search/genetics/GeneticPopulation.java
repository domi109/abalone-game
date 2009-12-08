package search.genetics;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import search.genetics.reproduction.ReproductionMethod;

public class GeneticPopulation extends ArrayList<GeneticIndividual>
{
	NumberFormat f;

	public GeneticPopulation()
	{
		f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(2);
	}
	
	public GeneticPopulation reproduce(ReproductionMethod r)
	{
		r.setPopulation(this);
		return r.getResult();
	}
	
	public double getTotalFitness()
	{
		double totalFitness = 0;
		for(GeneticIndividual i : this)
		{
			totalFitness += i.getFitness();
		}
		return totalFitness;
	}
	
	public double getAverageFitness()
	{
		return getTotalFitness()/size();
	}
	
	public String toString()
	{

		return "avgFitness["+f.format(getAverageFitness())+"]"+super.toString();
	}
	
	public GeneticIndividual getFittest()
	{
		return Collections.max(this);
	}

	public String getResults()
	{
		String str = "";

		for (GeneticIndividual ind : this)
		{
			str += ind.toString() + "\n";
		}

		return str;
	}
}
