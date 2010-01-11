package abalone.ai.machinelearning.TournamentModes;

import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import nl.maastrichtuniversity.dke.libreason.def.heuristic.Evaluator;
import nl.maastrichtuniversity.dke.libreason.genetics.FitnessEvaluator;
import nl.maastrichtuniversity.dke.libreason.genetics.GeneticIndividual;
import nl.maastrichtuniversity.dke.libreason.genetics.GeneticPopulation;
import abalone.ai.SimpleAI;
import abalone.ai.evaluation.LinearEvaluator;
import abalone.gamelogic.GameLogic;
import abalone.gamelogic.StandardAbaloneLogic;
import abalone.gamestate.GameState;
import abalone.model.Board;
import abalone.model.Move;
import abalone.model.Player;

public class CrossTournament implements FitnessEvaluator
{
	private GameLogic logic;
	private Board board;
	private GeneticPopulation pop;
	private Random random;

	public CrossTournament()
	{
		this.logic = new StandardAbaloneLogic();
		this.board = logic.initBoard();
		random = new Random();
	}

	@Override
	public GeneticPopulation eval(GeneticPopulation aPop) throws InterruptedException
	{
		// The individuals will probably play multiple matches. Therefore, the
		// gathered
		// statistics are stored in a map. After all matches have been played,
		// the fitness
		// of each individual is calculated using these stats.
		this.pop = aPop;

		// random matches for each Individual - just for testing purposes

		for (int i = 0; i < pop.size(); i++)
		{
//			for (int j = 0; j < 5; j++)
//			{
//				int opponent;
//				do
//				{
//					opponent = random.nextInt(pop.size());
//				} while (opponent == i);
//				match(pop.get(i), pop.get(opponent));
//			}
			for(int j = i+1; j<pop.size();j++)
			{
				match(pop.get(i),pop.get(j));
			}
		}

		return pop;
	}

	@SuppressWarnings("unchecked")
	private void match(GeneticIndividual p1, GeneticIndividual p2) throws InterruptedException
	{
		LinkedList<Player> players = new LinkedList<Player>();
		players.add(new SimpleAI(logic, (Evaluator) p1));
		players.add(new SimpleAI(logic, (Evaluator) p2));
		GameState state = logic.initState(board, players);

		boolean finished = false;
		int numberOfPlies = 0;

		while (!finished)
		{
			SimpleAI current = (SimpleAI) state.getCurrentPlayer();
			SimpleAI opponent = (SimpleAI) state.getOpponentPlayer();
			Move move = current.decide(state);
			logic.applyMove(state, move);

			if (logic.getWinner(state) != null)
			{
				finished = true;

				// update statistics for winner
				double wFit = ((LinearEvaluator) current.getEvaluator()).getFitness() + 1;
				((LinearEvaluator) current.getEvaluator()).setFitness(wFit);

				// update statistics for loser
				double lFit = ((LinearEvaluator) opponent.getEvaluator()).getFitness() - 1;
				((LinearEvaluator) opponent.getEvaluator()).setFitness(lFit);

			}
			else if (numberOfPlies == 80)
			{
				finished = true;

				Map<Player, Integer> lostMarbles = state.getMarblesRemoved();
				int pushed = lostMarbles.get(opponent);
				int lost = lostMarbles.get(current);

				// update statistics for current player
				double wFit = ((LinearEvaluator) current.getEvaluator()).getFitness() + .1 * pushed - .1 * lost;
				((LinearEvaluator) current.getEvaluator()).setFitness(wFit);

				// update statistics for opponent
				double lFit = ((LinearEvaluator) opponent.getEvaluator()).getFitness() - .1 * pushed + .1 * lost;
				((LinearEvaluator) opponent.getEvaluator()).setFitness(lFit);

			}
			numberOfPlies++;
		}
	}
}