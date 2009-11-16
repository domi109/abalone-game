package search.genetics;

public interface GeneticIndividual extends Comparable<GeneticIndividual>
{
	/**
	 * value between 0 and 1
	 * @return fitness
	 */
	public double getFitness();
	public Genotype getPhenotype();
	public boolean equalsGenetic(GeneticIndividual j);
	public GeneticIndividual reproduceWith(GeneticIndividual j);
    public void mutate();
	
}
