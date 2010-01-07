package abalone.ai.machinelearning.TournamentModes;

import search.genetics.GeneticPopulation;

public interface TournamentMode
{
	GeneticPopulation eval(GeneticPopulation pop);
}
