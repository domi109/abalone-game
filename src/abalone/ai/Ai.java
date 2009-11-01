/*
 * Interface class for the Artificial Intelligence (AI).
 * 
 */

package abalone.ai;

import abalone.gamestate.GameState;
import abalone.model.Move;
import abalone.model.Player;


public interface Ai extends Player {
    public Move decide(GameState state);
   
}
