/*
 * Node class.
 * Nodes are positions in a game.
 */

package abalone.gametree;

import java.util.ArrayList;

public class Node {

    // Need arraylist for node, because can have more childeren than two.
    private ArrayList<Node> children;

    // Need some data in nodes. Currently int, will change to object (gamestate?).
    private int data;

    // Constructor for node class.
    public Node() {
        children = new ArrayList<Node>();
    }

    // Method to add children.
    public void addChild(Node n) {
        children.add(n);
    }

    // Method to get children..
    public ArrayList<Node> getChildren() {
    return children;
    }

    // Method to set data into a node.
    public void setData(int newdata) {
        data = newdata;
    }

    // Method to get data from a node.
    public int getData() {
        return data;
    }
}
