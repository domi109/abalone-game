package search.genetics.reproduction;

import search.genetics.GeneticPopulation;

public interface ReproductionMethod
{
	public void setPopulation(GeneticPopulation pop);
	public int getResultSize();
	public GeneticPopulation getResult();
}
