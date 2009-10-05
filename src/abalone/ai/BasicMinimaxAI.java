package abalone.ai;

import java.util.ArrayList;

import search.Action;
import search.tree.SearchNode;
import search.tree.SearchState;
import search.tree.games.minimax.MiniMaxNode;
import search.tree.games.minimax.MinimaxProblem;
import search.tree.games.minimax.MinimaxSearch;
import abalone.gamelogic.GameLogic;
import abalone.gamestate.GameState;
import abalone.model.Move;
import abalone.model.Player;

public class BasicMinimaxAI implements Ai
{
	private class AbaloneNode extends MiniMaxNode
	{
		public AbaloneNode(GameState s)
		{
			super(s);
		}

		@Override
		public ArrayList<SearchNode> expand()
		{
			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();
			for(Action a : getState().getPossibleActions())
			{
				GameState newState = (GameState) getState().clone();
				logic.applyMove(newState, (Move)a);
				successors.add(new AbaloneNode(newState));
			}
			
			return successors;
		}
	}
	
	private class AbaloneSearchProblem implements MinimaxProblem
	{
		/**
		 * This is just a helper to save all this ((GameState) state) typing
		 */
		private GameState gs(SearchState s)
		{
			return (GameState)s;
		}
		
		@Override
		public int getFinalStateValue(SearchState state)
		{
			if(gs(state).equals(logic.getWinner(gs(state))))
			{
				return 1;
			}
			else
			{
				return -1;
			}
		}

		@Override
		public boolean breakTest(SearchNode node)
		{
			// Cancel after a certain number of plys...
			// TODO maybe rather set some time limit here?
			return node.getPathCost() >= 6;
		}

		@Override
		public boolean goalTest(SearchState state)
		{
			return logic.getWinner(gs(state)) != null;
		}
	}
	
	private GameLogic logic;

	public Move decide(GameState state)
	{
		AbaloneSearchProblem problem = new AbaloneSearchProblem();
		AbaloneNode startNode = new AbaloneNode(state);
		
		MinimaxSearch s = new MinimaxSearch(problem);
		SearchNode n = s.search(startNode);
				
		return (Move) n.getAction();

	}
}
