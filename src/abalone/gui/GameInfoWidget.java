package abalone.gui;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class GameInfoWidget extends QWidget
{
	public Signal0 revert = new Signal0();
	
	private int redLost = 0;
	private int blueLost = 0;
	
	public GameInfoWidget()
	{	
		// just start out with an empty scene
		 QGraphicsScene scene = prepareScene();
		 
		 // we need a QGraphicsView for viewing the scene
		 QGraphicsView view = new QGraphicsView(scene);
		 
		// draw the same background as the board
		QLinearGradient gradient = new QLinearGradient(0, 1000, 0, 0);
		QBrush brush = new QBrush(gradient);
		view.setBackgroundBrush(brush);
		view.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
		view.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff); 
		 
		 // A revert button
		 QPushButton revert = new QPushButton("Revert");
		 revert.clicked.connect(this,"revertClicked()");
		 
		 QVBoxLayout layout = new QVBoxLayout();
		 layout.addWidget(view);
		 layout.addWidget(revert);
		 // there is still room for more buttons if needed!
		 setLayout(layout);
	}
	
	public void setMarblesLost(int red, int blue)
	{
		redLost = red;
		blueLost = blue;
	}
	
	protected QGraphicsScene prepareScene()
	{
		double r = BoardWidget.getRadius();
		int redMarbles = redLost;
		int blueMarbles = blueLost;
		
		QGraphicsScene scene = new QGraphicsScene();
		
		QGraphicsTextItem red = new QGraphicsTextItem("Red");
		red.setDefaultTextColor(QColor.red);
		red.setPos(new QPointF(0,0));
		scene.addItem(red);
		
		QGraphicsTextItem blue = new QGraphicsTextItem("Blue");
		blue.setDefaultTextColor(QColor.blue);
		blue.setPos(new QPointF((2 * r), 0));
		scene.addItem(blue);
		
		for(int i = 0; i < 6 ; i++)
		{
			GameNodeEllipse leftEllipse;
			if(redMarbles > 0)
			{
				leftEllipse = new GameNodeEllipse(QColor.red, true, 0, (2*r)+(i*2.0*r), 2 * r, 2 * r);
				redMarbles--;
			}
			else
			{
				leftEllipse = new GameNodeEllipse(QColor.red, false, 0, (2*r)+(i*2.0*r), 2 * r, 2 * r);
			}
			leftEllipse.deactivateUse();
			scene.addItem(leftEllipse);
			
			GameNodeEllipse rightEllipse;
			if(blueMarbles > 0)
			{ 
				rightEllipse = new GameNodeEllipse(QColor.blue, true, (2*r), (2*r)+(i*2.0*r), 2 * r, 2 * r);
				blueMarbles--;
			}
			else
			{
				rightEllipse = new GameNodeEllipse(QColor.blue, false, (2*r), (2*r)+(i*2.0*r), 2 * r, 2 * r);
			}
			rightEllipse.deactivateUse();
			scene.addItem(rightEllipse);
		}
		
		return scene;
	}
	
	/**
	 * Signal that needs to be propagated to the main method
	 */
	private void revertClicked()
	{
		revert.emit();
	}
}
