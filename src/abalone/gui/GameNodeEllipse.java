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
	private Node node;
	private boolean selected =false;
	
	public static Map<Player,QColor> playerColors;
	
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
	public void mouseReleaseEvent(QGraphicsSceneMouseEvent event)
	{

		super.mouseReleaseEvent(event);
	}
	
	@Override
	public void mousePressEvent(QGraphicsSceneMouseEvent event)
	{
		selected = !selected;
		if(selected)
		{
			//QColor col = (node.getMarbleOwner()==null)?QColor.black : playerColors.get(node.getMarbleOwner()).darker();
			QColor col = QColor.yellow;
			QPen pen = new QPen(col,4);
			this.setPen(pen);
			this.setZValue(1);
		}
		else
		{
			this.setPen(new QPen(QColor.black,1));
			this.setZValue(0);
		}		
		super.mousePressEvent(event);
	}
	
	public void fillNormal()
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
	
	public void fillMarked()
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
}
