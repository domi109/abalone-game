package abalone.gamelogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import abalone.adt.KeyValuePair;
import abalone.gamestate.GameState;
import abalone.model.Board;
import abalone.model.Direction;
import abalone.model.Move;
import abalone.model.Node;
import abalone.model.Player;
import com.sun.org.apache.bcel.internal.generic.IFEQ;

public class StandardAbaloneLogic implements GameLogic
{

	@Override
	public Board initBoard()
	{
		Board b = new Board();
		// TODO Create the graph recursively:
		createNode(b, b.getCentralNode(), 4);

		for (Direction d : Direction.UPPER_LEFT)
		{
			b.addPath(createPath(b.getCentralNode(), d));
		}
		return b;
	}

	private List<KeyValuePair<Direction, Node>> createPath(Node centralNode, Direction startDirection)
	{
		List<KeyValuePair<Direction, Node>> path = new ArrayList<KeyValuePair<Direction, Node>>();

		path.add(new KeyValuePair<Direction, Node>(null, centralNode));
		Direction currentDir = startDirection;
		Node currentNode = centralNode;
		for (int i = 1; i < 5; i++)
		{
			// TODO Iterate through all the 5 rings of the board
			currentNode = currentNode.getNeighbour(currentDir);
			path.add(new KeyValuePair<Direction, Node>(currentDir, currentNode));
			currentDir = currentDir.getNextCW();

			for (Direction d : currentDir)
			{
				if (d.equals(startDirection.getNextCW()))
				{
					for (int j = 0; j < i - 1; j++)
					{
						// System.out.println(i + " " +d);
						currentNode = currentNode.getNeighbour(d);
						path.add(new KeyValuePair<Direction, Node>(d, currentNode));
					}
				}
				else
				{
					for (int j = 0; j < i; j++)
					{
						// System.out.println(i + " " +d);
						currentNode = currentNode.getNeighbour(d);
						path.add(new KeyValuePair<Direction, Node>(d, currentNode));
					}
				}
			}

			currentDir = startDirection;

		}

		return path;
	}

	@Override
	public GameState initState(Board board, List<Player> players)
	{
		GameState state = new GameState();
		state.setPlayers(players);
		state.setCurrentPlayer(players.get(0));
		state.setBoard(board);

		List<KeyValuePair<Direction, Node>> path = board.getEquiPaths().get(0);
		int i = 0;
		for (KeyValuePair<Direction, Node> step : path)
		{
			if ((i >= 8 && i <= 10) || (i >= 21 && i <= 24) || (i >= 39 && i <= 45))
			{
				step.getValue().setMarbleOwner(players.get(0));
			}
			else if ((i >= 14 && i <= 16) || (i >= 30 && i <= 33) || (i >= 51 && i <= 57))
			{
				step.getValue().setMarbleOwner(players.get(1));
			}
			i++;
		}

		return state;
	}

	@Override
	public void applyMove(GameState state, Move move)
	{
		for (Node n : move.getNodes())
		{
			// TODO check whether n is in state
			// TODO check whether move is legal

			n.getNeighbour(move.getDirection()).setMarbleOwner(n.getMarbleOwner());
			n.setMarbleOwner(null);
			// TODO this won't really work, because marbles that are moved
			// within one move can overwrite each other
		}
	}

    private boolean checkIfLegal(GameState state, Move move){
        Direction direction = move.getDirection();
        Direction neighbourDir;
        List<Node> nodes = move.getNodes();
        int nrMarbles = nodes.size();
        boolean parallel;
        boolean legal = true;
        
        if(nrMarbles == 2){
            neighbourDir = findNeighbourDirection(nodes.get(0), nodes.get(2));
            if (neighbourDir == null) legal = false;        // not neighbours
            if(neighbourDir == direction || neighbourDir == direction.getOpposite()){                  // in line move or not
                parallel = false;
            }else parallel = true;   
        }
        
        if(nrMarbles == 3){                                 //check if they are neighbours
            neighbourDir = findNeighbourDirection(nodes.get(0), nodes.get(1));
            if(neighbourDir == null){
                neighbourDir = findNeighbourDirection(nodes.get(0), nodes.get(2));
                if(neighbourDir == null){
                    neighbourDir = findNeighbourDirection(nodes.get(1), nodes.get(2));
                    if(neighbourDir == null) legal = false;
                }
            }
            
            
        }
    }

    private Direction findNeighbourDirection(Node node, Node neighbour){
        Direction dir = null;
        for(Direction direction : Direction.values()){
            if(node.getNeighbour(direction) == neighbour){
                dir = direction;
            }
        }
        return dir;
    }

	/**
	 * Recursively adds nodes to the board graph
	 * 
	 * @param node
	 *            the node that is currently handled
	 * @param level
	 *            level-counter counting backwards from the center - recursion
	 *            will terminate when level is zero.
	 */
	private void createNode(Board b, Node node, int level)
	{
		// TODO: this might need to be improved
		if (level <= 0)
		{
			// Terminate recursion
			return;
		}

		Direction startDirection = Direction.UPPER_LEFT;
		for (Direction d : startDirection)
		{
			// We're going clockwise looking into all directions.
			// The direction we're currently looking at is d
			// od is the opposite direction.
			Direction od = d.getOpposite();

			// We look at the nodes neighbour in the investigated direction
			Node neighbour = node.getNeighbour(d);
			if (neighbour == null)
			{
				// The neighbour is null, so it has not been created yet
				// thus, we now create it and add it to the nodes list.
				neighbour = node.addNeighbour(d);
				b.getNodes().add(neighbour);
			}

			// We still have to link the neighbour with the current node...
			neighbour.setNeighbour(od, node);

			// Now we want to look at the nodes next to the investigated
			// neighbour...
			Node prev = node.getNeighbour(d.getNextCCW());
			Node next = node.getNeighbour(d.getNextCW());
			if (prev != null)
			{
				// there is a node next to our neighbour node
				// in counterclockwise direction... we interconnect
				// those two.
				neighbour.setNeighbour(od.getNextCW(), prev);
				prev.setNeighbour(d.getNextCW(), neighbour);
			}
			if (next != null)
			{
				// there is a node next to our neighbour node
				// in clockwise direction... we interconnect
				// those two.
				neighbour.setNeighbour(od.getNextCCW(), next);
				next.setNeighbour(d.getNextCCW(), neighbour);
			}

			// now we go deeper into the recursion tree, further investigating
			// on
			// the neighbour node.
			createNode(b, neighbour, level - 1);
		}
	}

	/**
	 * Return the player who has won and null if there currently is no winner at all.
	 * @see abalone.gamelogic.GameLogic#getWinner(abalone.gamestate.GameState)
	 */
	@Override
	public Player getWinner(GameState state)
	{
		for(Entry<Player,Integer> e : state.getMarblesRemoved().entrySet())
		{
			if(e.getValue() >= 6)
			{
				return e.getKey();
			}
		}
		return null;
	}

}
