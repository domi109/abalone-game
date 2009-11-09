/*
 *  A linear, weighted evaluation function based on a paper 'Constructing an Abalone Game-Playing Agent'
 *  by N.P.P.M. Lemmens (18th June 2005).
 * 
 */

package abalone.ai;

// Imports from Java libraries.
import java.util.List;
import java.util.Map;
import java.util.Set;

// Imports from Model library.
import abalone.model.Board;
import abalone.model.Player;
import abalone.model.Node;

// Import from Search library.
import search.tree.heuristic.Evaluator;
import search.tree.SearchState;

// Imports from Gamestate library.
import abalone.gamestate.GameState;

public class LinearEvaluator implements Evaluator<Double>
{

    // Variables for strategies.
    private double f1;
    private double f2;
    private double f3;
    private double f4;
    private double f5;
    private double f6;

    // Variables for weights of each strategy.
    private double w1;
    private double w2;
    private double w3;
    private double w4;
    private double w5;
    private double w6;

    // Constructor for LinearEvaluator class.
    public LinearEvaluator(){
        // Ensure functions are set back to default values.
        f1 = 0;
        f2 = 0;
        f3 = 0;
        f4 = 0;
        f5 = 0;
        f6 = 0;
        // Ensure weights are set back to default values.
        w1 = 1;
        w2 = 1;
        w3 = 1;
        w4 = 1;
        w5 = 1;
        w6 = 1;
    }

   /**
    * Method which evaluates the desirability of a move done in a search state.
    * @param    state   a searchstate which the AI wishes to evaluate
    * @return           the desirability of a searched move
    */
    public Double eval(SearchState state){
        // Checks to see if the searchstate given is an instance of a gamestate.
        if(state instanceof GameState){
            // Cast searchstate to gamestate.
            GameState s = (GameState) state;
            // Get the board.
            Board board = s.getBoard();
            // Get player list.
            List<Player> players = s.getPlayers();
            // Get the current player.
            Player currentPlayer = s.getCurrentPlayer();
            // Get the opponent player.
            int tempCur = s.getPlayers().indexOf(s.getCurrentPlayer());
            Player opponentPlayer = s.getPlayers().get((tempCur + 1) % 2);
            // Get current player's marbles.
            Set<Node> currentPlayerMarbles = s.getMarbles(currentPlayer);
            // Get opponent player's marbles.
            Set<Node> opponentPlayerMarbles = s.getMarbles(opponentPlayer);
            // Get the lost marbles per player.
            Map<Player, Integer> lostMarbles = s.getMarblesRemoved();
         
            // Calculate the individual functions.
            calculateF1(players, board);
            calculateF2(players, board);
            calculateF3(players, board);
            calculateF4(players, board);
            calculateF5(lostMarbles, opponentPlayer);
            calculateF6(lostMarbles, currentPlayer);

            // Evaluate.
            double eval = (w1 * f1) + (w2 * f2) + (w3 * f3) + (w4 * f4) + (w5 * f5) - (w6 * f6);
            return eval;
        }
        else{

        }       
        return 0d;
    }

   /**
    * Method which calculates the difference between the sum of the Manhattan distances of marbles for each players.
    * <p>
    * This compares how close two players marbles are to the center. Reason for this is that in Abalone being at the
    * center is a good strategy, as it makes it harder for a player's marbles to be knocked off the board.
    * @param    Player  a list of players
    * @param
    * @return
    */
    private double calculateF1(List<Player> players, Board board){
        for (Player player : players) {
            //TODO: check distance of all marbles to center.
        }
        return f1;
    }
    
   /**
    * Method which calculates the difference between the sum of neighboring teammates of each marble for each player.
    * <p>
    * This is also known as the Cohesion Strategy. Reason for this is that in Abalone it is good for a player to have
    * his/her marbles close to each other, as it adds offensive and defensive capabilities.
    * @param    Player  a list of players
    * @param
    * @return      
    */
    private double calculateF2(List<Player> players, Board board){
        for (Player player : players) {
            //TODO: check number of teammates
        }
        return f2;
    }

   /**
    * Method which calculates the difference between the sum of opponent marble on adjacent side and opponent marble on
    * opposing adjacent side.
    * <p>
    * This is also known as the break-strong-group strategy. Reason is that when you break a group of the opponent, it
    * will weaken them offensively and defensively. Also often, when inbetween two opponent groups, a player's marbles
    * cannot be pushed.
    * @param    Player  a list of players
    * @param
    * @return
    */
    private double calculateF3(List<Player> players, Board board){
        for (Player player : players) {
            //TODO: check two sides for opponents.
        }
        return f3;
    }

   /**
    * Method which calculates
    * <p>
    * This is also known as the strengthen-group strategy. Reason is if you have contact position with an opponent marble,
    * it should be reinforced to ensure that it is safe from attack, but also to add offensive capabilities.
    * @param    Player a list of players
    * @param
    * @return
    */
    private double calculateF4(List<Player> players, Board board){
        for (Player player : players) {
            //TODO: check two sides for opponent and friend match.
        }
        return f4;
    }

   /**
    * Method which calculates the number of marbles the opponent has lost in this searchstate.
    * @param    Map a map containing the
    * @param    Player a opponent player
    * @return   
    */
    private double calculateF5(Map<Player, Integer> removed, Player opponentPlayer ){
        f5 = removed.get(opponentPlayer);
        return f5;
    }

   /**
    * Method which calculates the number of marbles the player has lost in this searchstate.
    * @param    Player  a list of players
    * @return
    */
    private double calculateF6(Map<Player, Integer> removed, Player currentPlayer){
        f6 = removed.get(currentPlayer);
        return f6;
    }

   /**
    * Getter method which gives weight of function 1 (w1).
    * @return           the weight of function 1 (w1)
    */
    public double getW1(){
        return w1;
    }

   /**
    * Setter method which changes weight of function 1 (w1).
    * @param    w1  the new weight of function 1 (w1)
    */
    public void setW1(double w1) {
        this.w1 = w1;
    }

   /**
    * Getter method which gives weight of function 2 (w2).
    * @return           the weight of function 2 (w2)
    */
    public double getW2() {
        return w2;
    }

   /**
    * Setter method which changes weight of function 2 (w2).
    * @param    w2  the new weight of function 2 (w2)
    */
    public void setW2(double w2){
        this.w2 = w2;
    }

   /**
    * Getter method which gives weight of function 3 (w3).
    * @return           the weight of function 3 (w3)
    */
    public double getW3(){
        return w3;
    }

   /**
    * Setter method which changes weight of function 3 (w3).
    * @param    w3  the new weight of function 3 (w3)
    */
    public void setW3(double w3){
        this.w3 = w3;
    }

   /**
    * Getter method which gives weight of function 4 (w4).
    * @return           the weight of function 4 (w4)
    */
    public double getW4(){
        return w4;
    }

   /**
    * Setter method which changes weight of function 4 (w4).
    * @param    w4  the new weight of function 4 (w4)
    */
    public void setW4(double w4){
        this.w4 = w4;
    }

   /**
    * Getter method which gives weight of function 5 (w5).
    * @return           the weight of function 5 (w5)
    */
    public double getW5(){
        return w5;
    }

   /**
    * Setter method which changes weight of function 5 (w5).
    * @param    w5  the new weight of function 5 (w5)
    */
    public void setW5(double w5){
        this.w5 = w5;
    }

   /**
    * Getter method which gives weight of function 6 (w6).
    * @return           the weight of function 6 (w6)
    */
    public double getW6(){
        return w6;
    }

   /**
    * Setter method which changes weight of function 6 (w6).
    * @param    w6  the new weight of function 6 (w6)
    */
    public void setW6(double w6){
        this.w6 = w6;
    }
}
