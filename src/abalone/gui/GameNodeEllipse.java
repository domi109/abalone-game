package abalone.gui;

import abalone.gamestate.GameState;
import abalone.model.Node;

import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QPen;

public class GameNodeEllipse extends AbaloneEllipse
{
    
	private Node node;
	private boolean activated =false;	
	
	public GameNodeEllipse(GameState state,Node node,double x,double y,double w, double h)
	{
		super(state.getMarbleOwner(node),x,y,w,h);

		this.node = node;
		
//		// --- Init tooltip --- //
//		String tt = "";
//		for(Player p : state.getPlayers())
//		{
//			ByteBuffer hash = ZobristHasher.get(node, p);
//			for(int i = 0; i<8;i++)
//			{
//				tt += " |"+hash.get(i)+"|";
//			}
//			tt+="\n";
//		}
//		ByteBuffer hash = ZobristHasher.get(node, null);
//		for(int i = 0; i<8;i++)
//		{
//			tt += " |"+hash.get(i)+"|";
//		}
//		tt+="\n";
//		this.setToolTip(tt);
//		// --- //
	}

	public void activate()
	{
		activated = true;
		QColor col = QColor.yellow;
		QPen pen = new QPen(col,4);
		this.setPen(pen);
		this.setZValue(1);
	}
	
	public void deactivate()
	{
		activated = false;
		this.setPen(new QPen(QColor.black,1));
		this.setZValue(0);
	}
	
	public boolean isActivated()
	{
		return activated;
	}
	
	public Node getNode()
	{
		return node;
	}
}
