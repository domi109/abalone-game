package abalone.gui;

import com.trolltech.qt.QVariant;
import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class AbaloneFront extends QMainWindow
{
	QMenu game;
	QMenu view;
	QMenu settings;
	QMenu help;
	
	QAction newAct;
	QAction loadAct;
	QAction saveAct;
	QAction saveAsAct;
	QAction networkgameAct;
	QAction undoMoveAct;
	QAction resignAct;
	QAction quitAct;
	
	QAction fullscreenAct;
	QAction showLogsAct;
	QAction showStatisticsAct;
	QAction preferencesAct;
	QAction playerSettingsAct;
	
	QAction aboutAbaloneAct;
	QAction reportProblemAct;
	QAction aboutAct;
	//QAction aboutQtAct;
	
	public AbaloneFront()
	{
		QMenuBar menuBar = new QMenuBar();
		setMenuBar(menuBar);
		setWindowIcon(new QIcon("classpath:logo.png"));
		
		// abaloneBoard = new <boardWidgetClass>();
		// setCentralWidget(abaloneBoard); <-- Maybe need something to indicate that there
		// are 2 screens in the picture
		
		/* Now we set up the user interface */
		try
		{
			createActions();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		createMenus();
		//createStatusBar();	
	}
	
	public void about()
	{
		QMessageBox.about(this,
						 tr("About Abalone-game"),
						 tr("Game made by a group of students at DKE " +
							"@ university Maastricht"));
	}
	
	//public void aboutQt()
	//{
	//	aboutQt(this);
	//}
	
	private void createActions()
	{
		newAct = new QAction(tr("&New Game"), this);
		newAct.setShortcut(new QKeySequence(tr("Ctrl+N")));
		newAct.setStatusTip(tr("Create a new Game"));
		//newAct.triggered.connect(this, "newFile()");
		
		loadAct = new QAction(new QIcon(),tr("&Load"), this);
		loadAct.setShortcut(tr("Ctrl+L"));
		loadAct.setStatusTip(tr("Opens a saved game"));
		//openAct.triggered.connect(this, "open()");
		
		saveAct = new QAction(new QIcon(),tr("&Save"), this);
		saveAct.setShortcut(tr("Ctrl+S"));
		saveAct.setStatusTip(tr("Saves a game"));
		//saveAct.triggered.connect(this, "open()");
		
		saveAsAct = new QAction(new QIcon(),tr("Save &As"), this);
		saveAsAct.setShortcut(tr("Ctrl+A"));
		saveAsAct.setStatusTip(tr("Saves a game under a new name"));
		
		networkgameAct = new QAction(new QIcon(),tr("&Network Game"), this);
		networkgameAct.setShortcut(tr("Ctrl+N"));
		networkgameAct.setStatusTip(tr("Play a game over the network"));
		
		undoMoveAct = new QAction(new QIcon(),tr("&Undo move"), this);
		undoMoveAct.setShortcut(tr("Ctrl+Z"));
		undoMoveAct.setStatusTip(tr("1 step back"));
		
		resignAct = new QAction(new QIcon(),tr("&Resign"), this);
		resignAct.setShortcut(tr("Ctrl+R"));
		resignAct.setStatusTip(tr("Resign this game"));
		
		quitAct = new QAction(new QIcon(),tr("&Quit"), this);
		quitAct.setShortcut(tr("Ctrl+Q"));
		quitAct.setStatusTip(tr("Quits the game"));
		
		fullscreenAct = new QAction(new QIcon(),tr("Fullscreen"), this);
		fullscreenAct.setStatusTip(tr("View the game fullscreen"));
		
		showLogsAct = new QAction(new QIcon(),tr("Show Logs"), this);
		showLogsAct.setStatusTip(tr("Shows a log of the game"));
		
		showStatisticsAct = new QAction(new QIcon(),tr("Show Statistics"), this);
		showStatisticsAct.setStatusTip(tr("Shows the statistics of the game"));
		
		preferencesAct = new QAction(new QIcon(),tr("Preferences"), this);
		preferencesAct.setStatusTip(tr("Preferences menu"));
		
		aboutAbaloneAct = new QAction(new QIcon(),tr("About Abalone"), this);
		aboutAbaloneAct.setStatusTip(tr("Link to abalone wiki"));
		
		reportProblemAct = new QAction(new QIcon(),tr("Report Problem"), this);
		reportProblemAct.setStatusTip(tr("Link to our issuetracker"));
		
		aboutAct = new QAction(new QIcon(),tr("About"), this);
		aboutAct.setStatusTip(tr("About this game"));
	}
	
	private void createMenus()
	{
		game = menuBar().addMenu(tr("Game"));
		game.addAction(newAct);
		game.addAction(loadAct);
		game.addAction(saveAct);
		game.addAction(saveAsAct);
		game.addSeparator();
		game.addAction(networkgameAct);
		game.addSeparator();
		game.addAction(undoMoveAct);
		game.addAction(resignAct);
		game.addSeparator();
		game.addAction(quitAct);
		
		view = menuBar().addMenu(tr("View"));
		view.addAction(fullscreenAct);
		view.addAction(showLogsAct);
		view.addAction(showStatisticsAct);
		
		settings = menuBar().addMenu(tr("Settings")); 
		settings.addAction(preferencesAct);
		settings.addAction(playerSettingsAct);
		
		help = menuBar().addMenu(tr("Help"));
		help.addAction(aboutAbaloneAct);
		help.addAction(reportProblemAct);
		help.addAction(aboutAct);
	}
	public static void main(String[] args)
	{
		QApplication.initialize(args);
		
		AbaloneFront front = new AbaloneFront();
		front.show();
		
		QApplication.exec();
	}
}
