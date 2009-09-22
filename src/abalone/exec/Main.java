package abalone.exec;

import java.util.List;

import abalone.adt.KeyValuePair;
import abalone.gamelogic.GameLogic;
import abalone.gamelogic.StandardAbaloneLogic;
import abalone.model.Board;
import abalone.model.Direction;
import abalone.model.Node;


public class Main {

    public static void main(String[] args) {
        GameLogic logic = new StandardAbaloneLogic();
        
        Board b = logic.initBoard();
        
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
        		System.out.println(step.getKey() + " " + i);
        		i++;
        	}
        }
    }

}
