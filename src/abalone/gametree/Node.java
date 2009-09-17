/*
 * Node class
 * Nodes are positions in a game
 */

package abalone.gametree;

import java.util.ArrayList;

public class Node {

    // Need arraylist for node, because can have more childeren than two
    private ArrayList<Node> children;

    // Need some data in nodes. Currently int, will change to object (gamestate?)
    private int data;

    // Constructor for node class
    public Node() {
        children = new ArrayList<Node>();
    }

   /**
    * Method to add a child to a specific node
    * @param n node n
    */
    public void addChild(Node n) {
        children.add(n);
    }

   /**
    * Method to return one child from a specific node.
    * @return  one child from a specified node
    */
    public void getChild(){
        // remove void, add return, some code, etc.
    }

   /**
    * Method to return all children from a specific node
    * @return  all children from a specified node
    */
    public ArrayList<Node> getChildren() {
    return children;
    }

   /**
    * Method which sets data in some node
    * @param newdata is new data which needs to be put into a node
    */
    public void setData(int newdata) {
        data = newdata;
    }

   /**
    * Method to return data from a specific node
    * @return  the data in a node
    */
    public int getData() {
        return data;
    }
}
