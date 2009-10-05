package abalone.gui;

import java.util.Map;

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

public class GameNodeEllipse extends QGraphicsEllipseItem
{
	public static Map<Player,QColor> playerColors;

	// clicked event
    public Signal1<GameNodeEllipse> clicked = new Signal1<GameNodeEllipse>();
    
	private Node node;
	private boolean activated =false;	
	
	public GameNodeEllipse(Node node,double x,double y,double w, double h)
	{
		super(x,y,w,h);

		this.node = node;
		QPen pen = new QPen(QColor.black, 1);
		this.setZValue(0);
		this.setPen(pen);
		this.setAcceptHoverEvents(true);
		this.setAcceptedMouseButtons(new MouseButtons(MouseButton.LeftButton,MouseButton.RightButton));
		fillNormal();
	}
	
	/**
	 * Special constructor for the non-ingame marbles
	 * @param c The Color of the player that the marble indicates
	 * @param lost Wether this marble needs to be drawn lost or not
	 */
	public GameNodeEllipse(QColor c, boolean lost, double x,double y,double w, double h)
	{
		super(x,y,w,h);
		
		QPen pen = new QPen(QColor.black, 1);
		this.setZValue(0);
		this.setPen(pen);
		fillSpecial(lost, c);
	}
	
	@Override
	public void hoverEnterEvent(QGraphicsSceneHoverEvent event)
	{
		fillMarked();
		super.hoverEnterEvent(event);
	}
	
	@Override
	public void hoverLeaveEvent(QGraphicsSceneHoverEvent event)
	{
		fillNormal();
		super.hoverLeaveEvent(event);
	}
	
	@Override
	public void mousePressEvent(QGraphicsSceneMouseEvent event)
	{
		clicked.emit(this);
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
	
	public void deactivateUse()
	{
		this.setAcceptHoverEvents(false);
		//this.setAcceptedMouseButtons(0);
		/*  ???
		 * According to the QT Jambi doc this should be 
		 * this.setAcceptedMouseButtons(0); but java won't accept integers here
		 * and null will return nullpointer exceptions
		 */
	}
	
	private void fillNormal()
	{
		QGradient gradient;
		if (node.getMarbleOwner() == null)
		{
			gradient = new QRadialGradient(rect().x()+rect().width()/2.0 + 1, rect().y()+rect().height()/2.0 + 1,rect().width()/2.0);
			gradient.setColorAt(1.0, QColor.darkGray);
			gradient.setColorAt(0.3, QColor.black);
		}
		else
		{
			gradient = new QRadialGradient(rect().x()+rect().width()/2.0 - 1, rect().y()+rect().height()/2.0 - 1,rect().width()/2.0);
			gradient.setColorAt(1.0, playerColors.get(node.getMarbleOwner()));
			gradient.setColorAt(0.3, QColor.gray);
		}
		this.setBrush(new QBrush(gradient));
	}
	
	private void fillMarked()
	{
		QGradient gradient;
		if (node.getMarbleOwner() == null)
		{
			gradient = new QRadialGradient(rect().x()+rect().width()/2.0 + 1, rect().y()+rect().height()/2.0 + 1,rect().width()/2.0);
			gradient.setColorAt(1.0, QColor.darkGray.lighter());
			gradient.setColorAt(0.3, QColor.black.lighter());
		}
		else
		{
			gradient = new QRadialGradient(rect().x()+rect().width()/2.0 - 1, rect().y()+rect().height()/2.0 - 1,rect().width()/2.0);

			gradient.setColorAt(1.0, playerColors.get(node.getMarbleOwner()).lighter());
			gradient.setColorAt(0.3, QColor.gray.lighter());
		}
		this.setBrush(new QBrush(gradient));
	}
	
	private void fillSpecial(boolean lost, QColor c)
	{
		QGradient gradient;
		if (!lost)
		{
			gradient = new QRadialGradient(rect().x()+rect().width()/2.0 + 1, rect().y()+rect().height()/2.0 + 1,rect().width()/2.0);
			gradient.setColorAt(1.0, QColor.darkGray.lighter());
			gradient.setColorAt(0.3, QColor.black.lighter());
		}
		else
		{
			gradient = new QRadialGradient(rect().x()+rect().width()/2.0 - 1, rect().y()+rect().height()/2.0 - 1,rect().width()/2.0);

			gradient.setColorAt(1.0, c);
			gradient.setColorAt(0.3, QColor.gray.lighter());
		}
		this.setBrush(new QBrush(gradient));
	}

	public Node getNode()
	{
		return node;
	}
}
