package search.genetics;

import search.genetics.reproduction.ReproductionMethod;
import search.genetics.selection.GeneticSelection;

public class GeneticSearch
{
	private GeneticPopulation pop;
	private int generation;
	private GeneticSelection s;
	private ReproductionMethod r;
	private FitnessEvaluator fitnessEval;

	public int getSelectionSize()
	{
		return selectionSize;
	}

	public void setSelectionSize(int selectionSize)
	{
		this.selectionSize = selectionSize;
	}

	private int selectionSize = 30;

	public GeneticSearch(GeneticPopulation initialPop, GeneticSelection select, ReproductionMethod reproduce, FitnessEvaluator fitnessEval)
	{
		this.pop = initialPop;
		//System.out.println("Initial Generation: " + pop);
		this.s = select;
		this.r = reproduce;
		this.fitnessEval = fitnessEval;
	}
	
	public void spawnGeneration()
	{
		pop = fitnessEval.eval(pop);
		//System.out.println("Spawning new generation...");
		GeneticIndividual fittest = pop.getFittest();
		System.out.println(pop.getAverageFitness() + " " + fittest.getFitness() + " "+ fittest);
		GeneticPopulation selection = s.select(pop, selectionSize);
		//System.out.println("Selection: " + selection);
		pop = selection.reproduce(r);
		//System.out.println("New Generation: " + pop);

		generation++;
	}
	
	public int getGeneration()
	{
		return generation;
	}
	
	public GeneticPopulation getPopulation()
	{
		return pop;
	}
}
