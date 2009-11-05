/*
 *  Class file for our linear evaluation function, for determining how good a move is, without going through the whole gametree.
 */

package abalone.ai;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

        
import search.tree.heuristic.Evaluator;
import search.tree.SearchState;
import abalone.gamestate.GameState;
import abalone.model.Player;


public class LinearEvaluator implements Evaluator
{

    // variables for strategies.
    private double f1;
    private double f2;
    private double f3;
    private double f4;
    private double f5;
    private double f6;

    // variables for weights of each strategy.
    private double w1;
    private double w2;
    private double w3;
    private double w4;
    private double w5;
    private double w6;

    // variable for evaluation function.

    public LinearEvaluator(){
        w1 = 1;
        w2 = 1;
        w3 = 1;
        w4 = 1;
        w5 = 1;
        w6 = 1;
    }

    public eval(SearchState state){
        // checks to see if it is okay.
        if(state instanceof GameState){

            GameState s = (GameState) state;
            // Get player list.
            List<Player> players = s.getPlayers();
            // Get some more stuff
            


        }
        else{

        }
    }

    // Calculates the difference between sum of Manhattan distances of each player's marbles.
    private double calculateF1(List<Player>){
        for()

        return f1;
    }
    
    //
    private double calculateF2(){

        return f2;
    }

    //
    private double calculateF3(){

        return f3;
    }

    //
    private double calculateF4(){

        return f4;
    }

    //
    private double calculateF5(){

        return f5;
    }

    //
    private double calculateF6(){

        return f6;

    }

    public double getW1(){
        return w1;
    }

    public void setW1(double w1) {
        this.w1 = w1;
    }

    public double getW2() {
        return w2;
    }

    public void setW2(double w2){
        this.w2 = w2;
    }

    public double getW3(){
        return w3;
    }

    public void setW3(double w3){
        this.w3 = w3;
    }

    public double getW4(){
        return w4;
    }

    public void setW4(double w4){
        this.w4 = w4;
    }

    public double getW5(){
        return w5;
    }

    public void setW5(double w5){
        this.w5 = w5;
    }

    public double getW6(){
        return w6;
    }

    public void setW6(double w6){
        this.w6 = w6;
    }
}
