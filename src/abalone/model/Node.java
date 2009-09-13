package abalone.model;

import java.util.HashMap;
import java.util.Map;


public class Node {

    private Map<Direction,Node> neighbourList;

    
    public Node(){
        neighbourList = new HashMap<Direction,Node>();
    }

    /**
     * @param position Direction
     * @param newNode the new node
     */
    public void setNeighbour(Direction position, Node aNode) {
        neighbourList.put(position, aNode);
    }

    /**
     * @param position Direction
     * @return Node, the newly created node
     */
    public Node addNeighbour(Direction position) {
    	Node n = new Node();
        neighbourList.put(position, n);
        return n;
    }
    
    /**
     * @param position Direction
     */
    public Node getNeighbour(Direction position){
        return neighbourList.get(position);
    }
}
