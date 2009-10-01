/*
 * Interface class for the Artificial Intelligence (AI).
 * 
 */

package abalone.ai;

import java.util.List;
// Some imports here, probably won't use most, will change later.
import abalone.gamestate.GameState;
import abalone.model.Player;


public interface Ai extends Player {

    // TODO Add some generic methods for using AI in general.
    // initialize somethings, run AI, etc
    // etc, etc, etc

    public void readGamestate(GameState state);
   
}
