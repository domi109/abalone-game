package abalone.gui;

import java.util.ArrayList;
import java.util.List;

import abalone.model.Direction;

import com.trolltech.qt.QSignalEmitter.Signal1;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QPointF;
import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.core.Qt.MouseButtons;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGraphicsPolygonItem;
import com.trolltech.qt.gui.QGraphicsSceneHoverEvent;
import com.trolltech.qt.gui.QGraphicsSceneMouseEvent;
import com.trolltech.qt.gui.QPolygonF;

public class Arrow extends QGraphicsPolygonItem
{
    public Signal1<Direction> clicked = new Signal1<Direction>();
	private Direction direction;
	
	private static QPolygonF createPoly(double x, double y, double angle)
	{
		double a = Math.PI-angle;
		double g = Math.PI/7;
		double d = 20;
		List<QPointF> points = new ArrayList<QPointF>(3);
		QPointF p1 = new QPointF(x,y);
		QPointF p2 = new QPointF(x-Math.cos(a-g)*d,y+Math.sin(a-g)*d);
		QPointF p3 = new QPointF(x+Math.cos(angle-g)*d,y+Math.sin(angle-g)*d);
		
		points.add(p1);
		points.add(p2);
		points.add(p3);

		QPolygonF poly = new QPolygonF(points);
		
		return poly;
	}
	
	public Arrow(double x, double y, double angle, Direction direction)
	{
		super(createPoly(x,y,angle));
		this.direction = direction;
		this.setBrush(new QBrush(QColor.green));
		this.setAcceptedMouseButtons(new MouseButtons(MouseButton.LeftButton,MouseButton.RightButton));
		this.setAcceptHoverEvents(true);
	}
	
	@Override
	public void hoverEnterEvent(QGraphicsSceneHoverEvent event)
	{
		this.setBrush(new QBrush(QColor.green.lighter()));
		super.hoverEnterEvent(event);
	}
	
	@Override
	public void hoverLeaveEvent(QGraphicsSceneHoverEvent event)
	{
		this.setBrush(new QBrush(QColor.green));
		super.hoverLeaveEvent(event);
	}
	
	@Override
	public void mousePressEvent(QGraphicsSceneMouseEvent event)
	{
		clicked.emit(direction);
	}

}
