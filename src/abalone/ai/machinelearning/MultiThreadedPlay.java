package abalone.ai.machinelearning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import nl.maastrichtuniversity.dke.libreason.def.heuristic.Evaluator;
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

public class MultiThreadedPlay {
	private GameLogic logic;
	private Board board;
	private GeneticPopulation pop;
	private Random random;
	private ArrayList<MatchThread> matches = new ArrayList<MatchThread>();
	private ArrayList<ArrayList<Thread>> threads = new ArrayList<ArrayList<Thread>>();
	protected int plyLvl = 80;
	
	public MultiThreadedPlay(int plyLvl){
		this.logic = new StandardAbaloneLogic();
		this.board = logic.initBoard();
		random = new Random();
		this.plyLvl = plyLvl;
	}
	
	public void addMatch(GeneticIndividual p1, int id1, GeneticIndividual p2, int id2){
		MatchThread match = new MatchThread();
		
		match.setp1(p1);
		match.setp2(p2);
		match.setID(id1, id2);

		matches.add(match);
	}
	
	private ArrayList<ArrayList<Thread>> splitMatches()
	{
		threads.clear();
		ArrayList<Integer> currentIndividuals = new ArrayList<Integer>();
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

	public void runMatches()
	{
		splitMatches();
		boolean finished;
		int it = 0;
		int nrMatches = 0;
		for (ArrayList<Thread> matchList : threads)
		{
			System.out.println("running matchList " + it + " (size: " + matchList.size() + " )");
			nrMatches += matchList.size();
			it++;

			for (Thread matchThread : matchList)
			{
				matchThread.start();
			}

			finished = false;
			System.out.println("Starting while loop");
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
		System.out.println("Total nr threads: " + nrMatches);
		matches.clear();
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
			players.add(new SimpleAI(logic, (Evaluator<Double>) p1));
			players.add(new SimpleAI(logic, (Evaluator<Double>) p2));
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
					else if (numberOfPlies == plyLvl)
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

