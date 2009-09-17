package search.optimization.genetics.selection;

import search.optimization.genetics.GeneticIndividual;
import search.optimization.genetics.GeneticPopulation;

public class KeepBestTournamentSelection implements GeneticSelection
{
	private int groupSize = 10;
	private int keep = 2;

	public KeepBestTournamentSelection(int tournamentGroupSize,
			int keepBestGroupSize)
	{
		groupSize = tournamentGroupSize;
		keep = keepBestGroupSize;
	}

	public GeneticPopulation select(GeneticPopulation pop, int size)
	{
		if (size-keep > groupSize)
		{
			throw new RuntimeException(
					"The tournamentgroup is smaller than the selection size");
		}
		
		GeneticPopulation selected = new GeneticPopulation();
		for (int i = 0; i < keep; i++)
		{
			GeneticIndividual max = pop.getFittest();
			selected.add(max);
			pop.remove(max);
		}

		for (int i = 0; i < (pop.size()-groupSize); i++)
		{
			// removes elements from the population so that only the tournament
			// group remains
			int delInd = (int) (Math.random() * pop.size());
			pop.remove(delInd);
		}

		for (int i = 0; i < (size - keep); i++)
		{
			GeneticIndividual max = pop.getFittest();
			selected.add(max);
			pop.remove(max);

		}
		return selected;
	}
}
