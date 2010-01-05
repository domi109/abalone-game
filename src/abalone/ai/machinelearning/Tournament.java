package abalone.ai.machinelearning;

import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import search.genetics.FitnessEvaluator;
import search.genetics.GeneticIndividual;
import search.genetics.GeneticPopulation;
import search.tree.heuristic.Evaluator;
import abalone.ai.SimpleAI;
import abalone.gamelogic.GameLogic;
import abalone.gamelogic.StandardAbaloneLogic;
import abalone.gamestate.GameState;
import abalone.model.Board;
import abalone.model.Move;
import abalone.model.Player;

public class Tournament implements FitnessEvaluator
{
	private GameLogic logic;
	private Board board;
	private GeneticPopulation pop;
	private Random random;

	public Tournament()
	{
		this.logic = new StandardAbaloneLogic();
		this.board = logic.initBoard();
		random = new Random();
	}

	@Override
	public GeneticPopulation eval(GeneticPopulation aPop)
	{
		// The individuals will probably play multiple matches. Therefore, the gathered
		// statistics are stored in a map. After all matches have been played, the fitness
		// of each individual is calculated using these stats.
		this.pop = aPop;

		// 5 random matches for each Individual - just for testing purposes

		for (int i = 0; i < pop.size(); i++)
		{
			for (int j = 0; j < 10; j++)
			{
				match(pop.get(i),pop.get(random.nextInt(pop.size())));
			}
		}

		return pop;
	}

	@SuppressWarnings("unchecked")
	private void match(GeneticIndividual p1, GeneticIndividual p2)
	{
		LinkedList<Player> players = new LinkedList<Player>();
		players.add(new SimpleAI(logic, (Evaluator) p1));
		players.add(new SimpleAI(logic, (Evaluator) p2));
		GameState state = logic.initState(board, players);

		boolean finished = false;
		int numberOfPlies = 0;

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
				double wFit = ((AbaloneIndividual)current.getEvaluator()).getFitness() + 1;
				((AbaloneIndividual)current.getEvaluator()).setFitness(wFit);
				
				// update statistics for loser
				double lFit = ((AbaloneIndividual)opponent.getEvaluator()).getFitness() - 1;
				((AbaloneIndividual)opponent.getEvaluator()).setFitness(lFit);

			}
			else if (numberOfPlies == 80)
			{
				finished = true;

				Map<Player, Integer> lostMarbles = state.getMarblesRemoved();
				int pushed = lostMarbles.get(opponent);
				int lost = lostMarbles.get(current);
				
				// update statistics for current player
				double wFit = ((AbaloneIndividual)current.getEvaluator()).getFitness() +  .1*pushed - .1*lost;
				((AbaloneIndividual)current.getEvaluator()).setFitness(wFit);
				

				// update statistics for opponent
				double lFit = ((AbaloneIndividual)opponent.getEvaluator()).getFitness() -  .1*pushed + .1*lost;
				((AbaloneIndividual)opponent.getEvaluator()).setFitness(lFit);

			}
			numberOfPlies++;
		}
	}
}