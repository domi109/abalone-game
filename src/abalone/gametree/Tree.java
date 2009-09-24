package abalone.gametree;

import java.util.ArrayList;

public class Tree {

	private Node root;
	
	public Node getRoot()
	{
		return root;
	}
	
	public void setRoot(Node root)
	{
		this.root = root;
	}
	
	public Node getParent(Node n)
	{
		return n.getParent();
	}
	
	public ArrayList<Node> getChildren(Node n)
	{
		return n.getChildren();
	}
}