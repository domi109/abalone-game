package abalone.gui;

import java.util.HashMap;
import java.util.List;

import abalone.adt.KeyValuePair;
import abalone.gamestate.GameState;
import abalone.model.Board;
import abalone.model.Direction;
import abalone.model.Node;
import abalone.model.Player;

import com.trolltech.qt.core.QPointF;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsView;
import com.trolltech.qt.gui.QLinearGradient;

public class BoardWidget extends QGraphicsView
{
	private GameState state;

	public BoardWidget(GameState state)
	{
		this.state = state;
		
		GameNodeEllipse.playerColors = new HashMap<Player, QColor>();
		GameNodeEllipse.playerColors.put(state.getPlayers().get(0), QColor.red);
		GameNodeEllipse.playerColors.put(state.getPlayers().get(1), QColor.blue);
		
		QGraphicsScene scene = prepareScene();
		QLinearGradient gradient = new QLinearGradient(0, 1000, 0, 0);
		QBrush brush = new QBrush(gradient);
		setBackgroundBrush(brush);

		setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
		setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
		this.setScene(scene);
		setBaseSize(sizeHint());
	}

	protected QGraphicsScene prepareScene()
	{
		QGraphicsScene scene = new QGraphicsScene();

		Board board = state.getBoard();

		QPointF center = new QPointF(this.width() / 2, this.height() / 2);
		double r = 15;
		List<KeyValuePair<Direction, Node>> path = board.getEquiPaths().get(0);
		for (KeyValuePair<Direction, Node> step : path)
		{
			if (Direction.RIGHT.equals(step.getKey()))
			{
				center.setX(center.x() + 2 * r);
			}
			else if (Direction.LEFT.equals(step.getKey()))
			{
				center.setX(center.x() - 2 * r);
			}
			else if (Direction.UPPER_LEFT.equals(step.getKey()))
			{
				center.setY(center.y() - Math.sqrt(3) * r);
				center.setX(center.x() - r);
			}
			else if (Direction.UPPER_RIGHT.equals(step.getKey()))
			{
				center.setY(center.y() - Math.sqrt(3) * r);
				center.setX(center.x() + r);
			}
			else if (Direction.DOWN_LEFT.equals(step.getKey()))
			{
				center.setY(center.y() + Math.sqrt(3) * r);
				center.setX(center.x() - r);
			}
			else if (Direction.DOWN_RIGHT.equals(step.getKey()))
			{
				center.setY(center.y() + Math.sqrt(3) * r);
				center.setX(center.x() + r);
			}

			GameNodeEllipse ellipse = new GameNodeEllipse(step.getValue(),center.x() - r, center.y() - r, 2 * r, 2 * r);
			scene.addItem(ellipse);

		}

		return scene;
	}

}
