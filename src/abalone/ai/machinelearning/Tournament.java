package abalone.ai.machinelearning;

import abalone.ai.SimpleAI;
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

public class Tournament implements FitnessEvaluator
{
	private GameLogic logic;
	private Tournament tournament;
	HashMap<AbaloneIndividual, ArrayList<MatchStats>> statsMap;
	ArrayList<GeneticIndividual> contestants;
	private GeneticPopulation pop;

	public Tournament(Tournament tournament, GameLogic logic)
	{
		this.tournament = tournament;
		logic = this.logic;
		contestants = new ArrayList<GeneticIndividual>();
	}

	public Tournament(GameLogic logic)
	{
		this.logic = logic;
		contestants = new ArrayList<GeneticIndividual>();
	}

	@Override
	public GeneticPopulation eval(GeneticPopulation aPop)
	{
		// The individuals will probably play multiple matches. Therefore, the gathered
		// statistics are stored in a map. After all matches have been played, the fitness
		// of each individual is calculated using these stats.
		this.pop = aPop;
		statsMap = new HashMap(pop.size());
		for (GeneticIndividual individual : pop)
		{
			statsMap.put((AbaloneIndividual) individual, new ArrayList<MatchStats>());
		}

		// 5 random matches for each Individual - just for testing purposes
		Random r = new Random();

		for (int i = 0; i < pop.size(); i++)
		{
			for (int j = 0; j < 5; j++)
			{
				contestants.add(pop.get(r.nextInt(pop.size())));
				contestants.add(pop.get(i));
				match();
			}

			contestants.clear();
		}

		assignFitness();

		return pop;
	}

	private void match()
	{
		logic = new StandardAbaloneLogic();
		Board board = logic.initBoard();
		LinkedList<Player> players = new LinkedList<Player>();
		players.add(new SimpleAI(logic, (Evaluator) contestants.get(0)));
		players.add(new SimpleAI(logic, (Evaluator) contestants.get(1)));
		GameState state = logic.initState(board, players);

		boolean finished = false;
		int numberOfPlies = 0;
		int playersTurn = 0;

		System.out.println("Match started");

		while(!finished)
		{
			SimpleAI pl = (SimpleAI) players.getFirst();
			Move move = pl.decide(state);
			logic.applyMove(state, move);

			if (logic.getWinner(state) != null)
			{
				finished = true;

				// update statistics of winner
				Map<Player, Integer> lostMarbles = state.getMarblesRemoved();
				int pushed = lostMarbles.get((SimpleAI) players.getLast());
				int lost = lostMarbles.get(pl);
				//ArrayList<MatchStats> statWinner = statsMap.get((AbaloneIndividual) pl.getEvaluator());
				//statWinner.add(new MatchStats(true, pushed, lost, numberOfPlies));
				statsMap.get((AbaloneIndividual) pl.getEvaluator()).add(new MatchStats(true, pushed, lost, numberOfPlies));

				// update statistics for loser
				SimpleAI loser = (SimpleAI) players.getLast();
				//ArrayList<MatchStats> statLoser = statsMap.get((AbaloneIndividual) loser.getEvaluator());
				//statLoser.add(new MatchStats(false, lost, pushed, numberOfPlies));
				statsMap.get((AbaloneIndividual) loser.getEvaluator()).add(new MatchStats(false, lost, pushed, numberOfPlies));

				System.out.println("Surprise, somebody WON!");
			}

			if (numberOfPlies == 80 && (logic.getWinner(state) == null))
			{
				finished = true;

				// update statistics of winner
				Map<Player, Integer> lostMarbles = state.getMarblesRemoved();
				int pushed = lostMarbles.get((SimpleAI) players.getLast());
				int lost = lostMarbles.get(pl);
				//ArrayList<MatchStats> statWinner = statsMap.get((AbaloneIndividual) pl.getEvaluator());
				//statWinner.add(new MatchStats(false, pushed, lost, numberOfPlies));
				statsMap.get((AbaloneIndividual) pl.getEvaluator()).add(new MatchStats(false, pushed, lost, numberOfPlies));



				// update statistics for loser
				SimpleAI loser = (SimpleAI) players.getLast();
				//ArrayList<MatchStats> statLoser = statsMap.get((AbaloneIndividual) loser.getEvaluator());
				//statLoser.add(new MatchStats(false, lost, pushed, numberOfPlies));
				statsMap.get((AbaloneIndividual) loser.getEvaluator()).add(new MatchStats(false, lost, pushed, numberOfPlies));

			}

			// switch active player
			players.addLast(players.removeFirst());
			numberOfPlies++;
		}

		System.out.println("Match finished");
	}

	private void assignFitness()
	{
		for (GeneticIndividual x : pop)
		{
			AbaloneIndividual ind = (AbaloneIndividual) x;

			ind.setFitness(calculateFitness(statsMap.get(x)));
		}

		for (GeneticIndividual x : pop)
		{
			AbaloneIndividual ind = (AbaloneIndividual) x;
			
			System.out.println(x.getPhenotype());
		}
	}

	private double calculateFitness(ArrayList<MatchStats> stats)
	{
		double fitness = 0;

		for (MatchStats stat : stats)
		{
			// TODO: Think of a fitness function that makes sense. This is just random
			//fitness += stat.isWinner() ? 1 : -.9;
			fitness += stat.getPushedMarbles() * 1.5;
			fitness += stat.getLostMarbles() * -0.5;
		}

		System.out.println("Fitness: " + fitness);

		return fitness;
	}
}