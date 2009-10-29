package abalone.gui;

import java.util.Map;

import abalone.gamestate.GameState;
import abalone.model.Node;
import abalone.model.Player;

import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.core.Qt.MouseButtons;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGradient;
import com.trolltech.qt.gui.QGraphicsEllipseItem;
import com.trolltech.qt.gui.QGraphicsSceneHoverEvent;
import com.trolltech.qt.gui.QGraphicsSceneMouseEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QRadialGradient;
import com.trolltech.qt.gui.QStyleOptionGraphicsItem;
import com.trolltech.qt.gui.QWidget;

public class GameNodeEllipse extends AbaloneEllipse
{
    
	private Node node;
	private boolean activated =false;	
	
	public GameNodeEllipse(GameState state,Node node,double x,double y,double w, double h)
	{
		super(state.getMarbleOwner(node),x,y,w,h);

		this.node = node;
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
