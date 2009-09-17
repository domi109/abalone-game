package search.optimization.genetics.reproduction;

import search.optimization.genetics.GeneticPopulation;

public interface ReproductionMethod
{
	public void setPopulation(GeneticPopulation pop);
	public int getResultSize();
	public GeneticPopulation getResult();
}
