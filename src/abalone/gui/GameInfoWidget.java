package abalone.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout.Alignment;

import abalone.gamestate.GameState;
import abalone.model.Player;

import com.trolltech.qt.core.*;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.*;

public class GameInfoWidget extends QWidget
{
	public Signal0 revert = new Signal0();
	public List<PushedMarblesDisplayWidget> pmDisplays;
	
	public GameInfoWidget(GameState state)
	{	
		 // A revert button
		 QPushButton revert = new QPushButton("Revert");
		 revert.clicked.connect(this,"revertClicked()");
		 
		 QGridLayout layout = new QGridLayout();

		 pmDisplays = new ArrayList<PushedMarblesDisplayWidget>(2);
		 int i = 0;
		 for(Player p : state.getPlayers())
		 {
			 PushedMarblesDisplayWidget pmdw = new PushedMarblesDisplayWidget(p, state);
			 pmDisplays.add(pmdw);
			 QLabel pLabel = new QLabel(p.getName());
			 pLabel.setAlignment(AlignmentFlag.AlignCenter);
			 pLabel.setFont(new QFont("Arial",12));
			 layout.addWidget(pLabel,0,i);
			 layout.addWidget(pmdw,1,i);
			 i++;
		 }
		 
		 layout.addWidget(revert,2,0,1,-1);
		 // there is still room for more buttons if needed!
		 setLayout(layout);
		 setFixedSize(sizeHint().width(),sizeHint().height());
	}
	
	
	/**
	 * Signal that needs to be propagated to the main method
	 */
	private void revertClicked()
	{
		revert.emit();
	}


	public void updateGameInfo(GameState state)
	{

		for(PushedMarblesDisplayWidget display : pmDisplays)
		{
			display.updateDisplay(state);
		}		
	}
}
