package abalone.gui;

import java.util.List;

import abalone.adt.KeyValuePair;
import abalone.model.Board;
import abalone.model.Direction;
import abalone.model.Node;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QPointF;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*

public class BoardWidget extends QWidget
{
	private Board board;
	public BoardWidget(Board board)
	{
		this.board = board;
	}
	
	@Override
    protected void paintEvent(QPaintEvent event) {

        QPainter painter = new QPainter(this);
        drawBoard(painter);
    }

    private void drawBoard(QPainter painter) {

          painter.setPen(new QPen(QColor.black,1));

          QPointF center = new QPointF(this.width()/2,this.height()/2);
          double r = 15;



          List<KeyValuePair<Direction, Node>> path = board.getEquiPaths().get(0);
          for(KeyValuePair<Direction,Node> step : path)
          {
        	  if(Direction.RIGHT.equals(step.getKey())){
        		  center.setX(center.x()+2*r);
        	  }
        	  else if(Direction.LEFT.equals(step.getKey())){
        		  center.setX(center.x()-2*r);
        	  }
        	  else if(Direction.UPPER_LEFT.equals(step.getKey())){
        		  center.setY(center.y()-Math.sqrt(3)*r);
        		  center.setX(center.x()-r);
        	  }
        	  else if(Direction.UPPER_RIGHT.equals(step.getKey())){
        		  center.setY(center.y()-Math.sqrt(3)*r);
        		  center.setX(center.x()+r);
        	  }
        	  else if(Direction.DOWN_LEFT.equals(step.getKey())){
        		  center.setY(center.y()+Math.sqrt(3)*r);
        		  center.setX(center.x()-r);
        	  }
        	  else if(Direction.DOWN_RIGHT.equals(step.getKey())){
        		  center.setY(center.y()+Math.sqrt(3)*r);
        		  center.setX(center.x()+r);
        	  }

              QGradient gradient = new QRadialGradient(center,15);
              gradient.setColorAt(0.3, QColor.yellow);
              gradient.setColorAt(1.0, QColor.green);
              painter.setBrush(new QBrush(gradient));
              painter.drawEllipse(center, 15, 15);

          }
          
          painter.end();
    }
}
