/*
 * Interface class for the Artificial Intelligence (AI).
 * 
 */

package abalone.ai;

import java.util.List;
// Some imports here, probably won't use most, will change later.
import abalone.gamestate.GameState;
import abalone.model.Move;
import abalone.model.Player;


public interface Ai extends Player {
    public Move decide(GameState state);
   
}