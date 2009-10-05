/*
 * Negamax is minimax variant for two player zero-sun game.
 * Adding alpha-beta pruning to it. Currently in pseudocode state.
 */

package abalone.ai;

import abalone.gamestate.GameState;
import abalone.gametree.Node;
import abalone.gametree.Tree;

public class Negamax  {    

    public void readGamestate(GameState state) {
      // TODO read gamestate here.
    }

//    public void doNegamax(Node n, int depth, double alpha, double beta){
//        if(n == rootOfTree || depth == 0 ){
//            return evaluationValueOfNode;
//        }
//        else
//            // For each child of node
//            for(getChildren : node){
//                alpha = max(alpha, -negamax(child, depth-1, -beta, -alpha,));
//                if (alpha >= beta){
//                    return alpha;
//                }
//            }
//            return alpha;
//    }
}
