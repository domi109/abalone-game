package abalone.exec;

import abalone.gamelogic.GameLogic;
import abalone.gamelogic.StandardAbaloneLogic;
import abalone.model.Board;


public class Main {

    public static void main(String[] args) {
        GameLogic logic = new StandardAbaloneLogic();
        
        Board b = logic.initBoard();
        
        b.printBoard();
    }

}
