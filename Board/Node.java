package abalone;


public class Node {

    private Node[] neighbourList;
    private boolean dummy;

    
    public Node(boolean aDummy){
        neighbourList = new Node[6];
        dummy = aDummy;
    }

    /**
     * 0 = upperLeft<br>
     * 1 = upperRight<br>
     * 2 = right<br>
     * 3 = downRight<br>
     * 4 = downLeft<br>
     * 5 = left
     * @param position int as in explained above
     * @param newNode the new node
     */
    public void setNeighbour(int position, Node aNode) {
        neighbourList[position] = aNode;
    }

    /**
     * 0 = upperLeft<br>
     * 1 = upperRight<br>
     * 2 = right<br>
     * 3 = downRight<br>
     * 4 = downLeft<br>
     * 5 = left
     * @param position int as in explained above
     */
    public void addNeighbour(int position) {
        neighbourList[position] = new Node(false);
    }

    /**
     * 0 = upperLeft<br>
     * 1 = upperRight<br>
     * 2 = right<br>
     * 3 = downRight<br>
     * 4 = downLeft<br>
     * 5 = left
     * @param position int as in explained above
     */
    public void addDummy(int position){
        neighbourList[position] = new Node(true);
    }
    
    /**
     * 0 = upperLeft<br>
     * 1 = upperRight<br>
     * 2 = right<br>
     * 3 = downRight<br>
     * 4 = downLeft<br>
     * 5 = left
     * @param position int as in explained above
     */
    public Node getNeighbour(int position){
        return neighbourList[position];
    }

    public boolean isDummy(){
        return dummy;
    }
}
