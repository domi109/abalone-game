package abalone.gametree;

import java.util.ArrayList;
import abalone.gamestate.GameState;

public class Node {

	private Node parent;
    private ArrayList<Node> children;
    private GameState data;
    
    public Node() {
        children = new ArrayList<Node>();
    }
    public void setParent(Node parent)
    {
    	this.parent = parent;
    }
    
    public Node getParent()
    {
    	return this.parent;
    }

    public void addChild(Node n) {
        children.add(n);
    }

    public GameState getChild(int index){
    	return (GameState) children.get(index).getData();
    }

    public ArrayList<Node> getChildren() {
    return children;
    }
    
    public void removeChild(int index){
    	children.remove(index);
    }

    public void setData(GameState newdata) {
        data = newdata;
    }

    public GameState getData() {
        return data;
    }
}
