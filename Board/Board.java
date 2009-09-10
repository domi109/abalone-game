package abalone;

public class Board {

    Node centralNode;

    public Board(int sideLength) {
        centralNode = new Node(false);
        createBoard(sideLength);
        System.out.println("Board without dummy's created");
        addDummys();
        System.out.println("Board created");
    }

    private void createBoard(int sideLenght) {
        Node node = centralNode;
//        for (int i = 0; i < NUMBER_OF_NEIGHBOURS; i++) {
//            node.addNeighbour(i);
//            node.getNeighbour(i).setNeighbour(getOppositeNeighbour(i), node);
//            System.out.println("node added");
//        }
    }

    private int getOppositeNeighbour(int aNeighbour) {
        switch (aNeighbour) {
            case 0:
                return 3;
            case 1:
                return 4;
            case 2:
                return 5;
            case 3:
                return 0;
            case 4:
                return 1;
            case 5:
                return 2;
            default:
                System.out.println("wrong opposite neighbour input");
                return 11111;

        }
    }

    private void addDummys() {
        boolean finished = false;
        Node node = mostUpperLeft();
        int neighbourPos = 0;


        while (!finished) {
            for (int i = 0; i < NUMBER_OF_NEIGHBOURS; i++) {
                if (node.getNeighbour(i) == null) {
                    node.addDummy(i);
                    System.out.println("dummy added");
                }
            }
            if (neighbourPos == 5) {
                finished = true;
            } else if (node.getNeighbour(neighbourPos).isDummy()) {
                neighbourPos++;
            } else {
                node = node.getNeighbour(neighbourPos);
            }
        }
    }

    public void printBoard() {
        Node leftNode = mostUpperLeft().getNeighbour(0);
        Node node = leftNode;
        boolean finished = false;
        while (!finished) {
            if (node.isDummy()) {
                System.out.println("#");
            } else if (!node.isDummy()) {
                System.out.println("O");
            } else if (node == null) {
                node = leftNode.getNeighbour(5);
                if (node == null) {
                    node = leftNode.getNeighbour(4);
                }
                if (node == null) {
                    finished = true;
                }
                leftNode = node;
            } else {
                node = node.getNeighbour(3);
            }
        }
    }

    public Node mostUpperLeft() {
        boolean finished = false;
        Node node = centralNode;

        while (!finished) {

            if (node.getNeighbour(0) == null || node.getNeighbour(0).isDummy()) {
                finished = true;
            } else {
                node = node.getNeighbour(0);
            }
        }
        return node;
    }
    private static int NUMBER_OF_NEIGHBOURS = 6;
}
