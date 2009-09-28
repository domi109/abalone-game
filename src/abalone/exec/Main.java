package abalone.exec;

import java.util.ArrayList;
import java.util.List;

import abalone.adt.KeyValuePair;
import abalone.gamelogic.GameLogic;
import abalone.gamelogic.StandardAbaloneLogic;
import abalone.gamestate.GameState;
import abalone.model.Board;
import abalone.model.Direction;
import abalone.model.HumanPlayer;
import abalone.model.Node;
import abalone.model.Player;


public class Main {

    public static void main(String[] args) {
        GameLogic logic = new StandardAbaloneLogic();
        
        Board b = logic.initBoard();
        List<Player> players = new ArrayList<Player>(2);
        players.add(new HumanPlayer());
        players.add(new HumanPlayer());
        GameState s = logic.initState(b, players);
        b.printBoard();
        

        
        for(List<KeyValuePair<Direction, Node>> path : b.getEquiPaths())
        {
            System.out.println();
            System.out.println("==========");
            System.out.println();
        	System.out.println("Path");
        	int i = 0;
        	for(KeyValuePair<Direction, Node> step : path)
        	{
        		System.out.println(step.getKey() + " " + step.getValue());
        		i++;
        	}
        }
    }

}
