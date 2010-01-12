package abalone.ai.machinelearning.TournamentModes;

import java.util.ArrayList;
import java.util.Iterator;
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

public class CrossTournamentMultiThreaded implements FitnessEvaluator
{
	private GameLogic logic;
	private Board board;
	private GeneticPopulation pop;
	private Random random;

	public CrossTournamentMultiThreaded()
	{
		this.logic = new StandardAbaloneLogic();
		this.board = logic.initBoard();
		random = new Random();
	}

	@Override
	public GeneticPopulation eval(GeneticPopulation aPop)
	{
		// The individuals will probably play multiple matches. Therefore, the
		// gathered
		// statistics are stored in a map. After all matches have been played,
		// the fitness
		// of each individual is calculated using these stats.
		this.pop = aPop;
		ArrayList<MatchThread> matches = new ArrayList<MatchThread>();

		// random matches for each Individual - just for testing purposes

		int counter = 0;
		for (int i = 0; i < pop.size(); i++)
		{
			for (int j = i + 1; j < pop.size(); j++)
			{
				MatchThread match = new MatchThread();
				match.setp1(pop.get(i));
				match.setp2(pop.get(j));
				match.setID(i, j);

				matches.add(match);
				counter++;
			}
		}
//		System.out.println("nr matches: " + counter);
/*		for (int i = 0; i < counter; i++) {
			System.out.print(".");
		}
		System.out.print("V");
		System.out.println();*/
		ArrayList<ArrayList<Thread>> test = splitMatches(matches);
		runMatches(test);

		return pop;
	}

	private ArrayList<ArrayList<Thread>> splitMatches(ArrayList<MatchThread> matches)
	{
		ArrayList<Integer> currentIndividuals = new ArrayList<Integer>();
		ArrayList<ArrayList<Thread>> threads = new ArrayList<ArrayList<Thread>>();
		int arrayNr = 0;

		while (!matches.isEmpty())
		{
			threads.add(new ArrayList<Thread>());
			int i = 0;
			for (Iterator<MatchThread> it = matches.iterator(); it.hasNext();)
			{
				MatchThread match = it.next();
				if (!currentIndividuals.contains(match.getIDp1()) && !currentIndividuals.contains(match.getIDp2()))
				{
					currentIndividuals.add(match.getIDp1());
					currentIndividuals.add(match.getIDp2());
					threads.get(arrayNr).add(new Thread(match));
					it.remove();
				}
				i++;
			}
			currentIndividuals.clear();
			arrayNr++;
		}
		return threads;
	}

	private void runMatches(ArrayList<ArrayList<Thread>> matches)
	{
		boolean finished;
		int it = 0;
		int nrMatches = 0;
		for (ArrayList<Thread> matchList : matches)
		{
			// System.out.println("running matchList " + it + " (size: " +
			// matchList.size() + " )");
			nrMatches += matchList.size();
			it++;

			for (Thread matchThread : matchList)
			{
				matchThread.start();
			}

			finished = false;
			// System.out.println("Starting while loop");
			int counter;
			int counter2 = 0;
			while (!finished)
			{
				finished = true;
				counter = 0;
				for (Thread thread : matchList)
				{
					if (thread.isAlive())
						finished = false;
					else counter++;
				}
				if(counter > counter2) {
					System.out.print(".");
					counter2 = counter;
				}
			}
		}
		System.out.println();
		// System.out.println("Total nr threads: " + nrMatches);
	}

	private class MatchThread implements Runnable
	{

		private GeneticIndividual p1;
		private GeneticIndividual p2;
		private int iDp1;
		private int iDp2;

		public void setp1(GeneticIndividual p1)
		{
			this.p1 = p1;
		}

		public void setp2(GeneticIndividual p2)
		{
			this.p2 = p2;
		}

		public void setID(int first, int second)
		{
			iDp1 = first;
			iDp2 = second;
		}

		public int getIDp1()
		{
			return iDp1;
		}

		public int getIDp2()
		{
			return iDp2;
		}

		@Override
		public void run()
		{
			LinkedList<Player> players = new LinkedList<Player>();
			players.add(new SimpleAI(logic, (Evaluator) p1));
			players.add(new SimpleAI(logic, (Evaluator) p2));
			Board boardT = logic.initBoard();
			GameState state = logic.initState(boardT, players);

			boolean finished = false;
			int numberOfPlies = 1;

			try
			{
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
					Thread.sleep(0);
				}
			}
			catch (InterruptedException e)
			{

			}
		}
	}
}