package abalone.ai.machinelearning.TournamentModes;

import abalone.ai.SimpleAI;
import abalone.ai.evaluation.LinearEvaluator;
import abalone.gamelogic.GameLogic;
import abalone.gamelogic.StandardAbaloneLogic;
import abalone.gamestate.GameState;
import abalone.model.Board;
import abalone.model.Move;
import abalone.model.Player;
import search.genetics.FitnessEvaluator;
import search.genetics.GeneticIndividual;
import search.genetics.GeneticPopulation;
import search.tree.heuristic.Evaluator;

import java.util.*;

public class KOTournament implements FitnessEvaluator
{
	private GameLogic logic;
	private Board board;
	private GeneticPopulation pop;
	private Random random;
	private int numberOfTournaments;
	int currentRound;

	public KOTournament(int numberOfTournaments)
	{
		this.numberOfTournaments = numberOfTournaments;
		this.logic = new StandardAbaloneLogic();
		this.board = logic.initBoard();
		random = new Random();
	}

	@Override
	public GeneticPopulation eval(GeneticPopulation pop)
	{
		// TODO: pop size must be a power of 2; check / correct that

		this.pop = pop;

		int numberOfRounds = (int) ((Math.log(pop.size()))/(Math.log(2)));

		GeneticPopulation contestants;
		GeneticPopulation winners = new GeneticPopulation();

		for (int i = 0; i < numberOfTournaments; i++)
		{
			contestants = pop;
			Collections.shuffle(contestants);

			for (currentRound = 1; currentRound <= numberOfRounds; currentRound++)
			{
				for (int j = 0; j < contestants.size(); j += 2)
				{
					GeneticIndividual gen1 = pop.get(j);

					GeneticIndividual gen2 = pop.get(j+1);

					winners.add(match(gen1, gen2));
				}

				contestants = winners;
				winners = new GeneticPopulation();
			}
		}

		return pop;
	}

	private GeneticIndividual match(GeneticIndividual ind1, GeneticIndividual ind2)
	{
		LinkedList<Player> players = new LinkedList<Player>();
		players.add(new SimpleAI(logic, (Evaluator) ind1));
		players.add(new SimpleAI(logic, (Evaluator) ind2));
		GameState state = logic.initState(board, players);

		boolean finished = false;
		int numberOfPlies = 0;
		GeneticIndividual winner = null;
		int overTimeCounter = 0;

		while(!finished)
		{
			SimpleAI current = (SimpleAI) state.getCurrentPlayer();
			SimpleAI opponent = (SimpleAI) state.getOpponentPlayer();
			Move move = current.decide(state);
			logic.applyMove(state, move);

			if (logic.getWinner(state) != null)
			{
				finished = true;

				//update statistics for winner
				winner = (LinearEvaluator)current.getEvaluator();
				double wFit = winner.getFitness() + currentRound;
				winner.setFitness(wFit);
			}
			else if (numberOfPlies == 100)
			{
				finished = true;

				Map<Player, Integer> lostMarbles = state.getMarblesRemoved();
				int pushed = lostMarbles.get(opponent);
				int lost = lostMarbles.get(current);

				//double wFit = ((AbaloneIndividual)current.getEvaluator()).getFitness() +  .1*pushed - .1*lost;
				//double lFit = ((AbaloneIndividual)opponent.getEvaluator()).getFitness() -  .1*pushed + .1*lost;

				if (pushed > lost)
				{
					// current player wins --> update stats
					winner = (LinearEvaluator)current.getEvaluator();
					double fitness = winner.getFitness() + currentRound;
					winner.setFitness(fitness);
				}
				else if (pushed < lost)
				{
					// opponent player wins --> update stats
					winner = (LinearEvaluator)opponent.getEvaluator();
					double fitness = winner.getFitness() + currentRound;
					winner.setFitness(fitness);
				}
				else
				{
					// draw --> overtime
					numberOfPlies = 80;

					if (overTimeCounter == 3)
					{
						// no end in sight --> current player is declared as winner
						winner = (LinearEvaluator)current.getEvaluator();
						double fitness = winner.getFitness() + currentRound;
						winner.setFitness(fitness);
					}

					overTimeCounter++;
				}
			}

			numberOfPlies++;
		}

		return winner;
	}
}
