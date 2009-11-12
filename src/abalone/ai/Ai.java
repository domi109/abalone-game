/*
 * Interface class for the Artificial Intelligence (AI).
 * 
 */

package abalone.ai;

import abalone.gamestate.GameState;
import abalone.model.Move;
import abalone.model.Player;


public abstract class Ai extends Player {
    public abstract Move decide(GameState state);
   
}
