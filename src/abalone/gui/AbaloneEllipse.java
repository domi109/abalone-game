package abalone.gui;

import java.util.Map;

import abalone.model.Player;

import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.core.Qt.MouseButtons;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGradient;
import com.trolltech.qt.gui.QGraphicsEllipseItem;
import com.trolltech.qt.gui.QGraphicsSceneHoverEvent;
import com.trolltech.qt.gui.QGraphicsSceneMouseEvent;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QRadialGradient;

public class AbaloneEllipse extends QGraphicsEllipseItem
{
	public static Map<Player,QColor> playerColors;

	// clicked event
    public Signal1<AbaloneEllipse> clicked = new Signal1<AbaloneEllipse>();
    
    private Player player;
    	
	public AbaloneEllipse(Player player,double x,double y,double w, double h)
	{
		super(x,y,w,h);

		QPen pen = new QPen(QColor.black, 1);
		this.player=player;
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
	public void mousePressEvent(QGraphicsSceneMouseEvent event)
	{
		clicked.emit(this);
	}
	
	private void fillNormal()
	{
		QGradient gradient;
		if (player == null)
		{
			gradient = new QRadialGradient(rect().x()+rect().width()/2.0 + 1, rect().y()+rect().height()/2.0 + 1,rect().width()/2.0);
			gradient.setColorAt(1.0, QColor.darkGray);
			gradient.setColorAt(0.3, QColor.black);
		}
		else
		{
			gradient = new QRadialGradient(rect().x()+rect().width()/2.0 - 1, rect().y()+rect().height()/2.0 - 1,rect().width()/2.0);
			gradient.setColorAt(1.0, playerColors.get(player));
			gradient.setColorAt(0.3, QColor.gray);
		}
		this.setBrush(new QBrush(gradient));
	}
	
	private void fillMarked()
	{
		QGradient gradient;
		if (player == null)
		{
			gradient = new QRadialGradient(rect().x()+rect().width()/2.0 + 1, rect().y()+rect().height()/2.0 + 1,rect().width()/2.0);
			gradient.setColorAt(1.0, QColor.darkGray.lighter());
			gradient.setColorAt(0.3, QColor.black.lighter());
		}
		else
		{
			gradient = new QRadialGradient(rect().x()+rect().width()/2.0 - 1, rect().y()+rect().height()/2.0 - 1,rect().width()/2.0);
			gradient.setColorAt(1.0, playerColors.get(player).lighter());
			gradient.setColorAt(0.3, QColor.gray.lighter());
		}
		this.setBrush(new QBrush(gradient));
	}
	
	public void setMarble(Player player)
	{
		this.player = player;
		fillNormal();
	}
}
