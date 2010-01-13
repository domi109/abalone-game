package abalone.gui;

import abalone.gamestate.GameState;

import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QFileInfo;
import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.gui.*;
import com.trolltech.qt.webkit.QWebView;
import com.trolltech.qt.gui.QDialog.DialogCode;
import java.lang.Integer;

public class AbaloneFront extends QMainWindow
{
	public Signal0 newGame = new Signal0();
	public Signal1<String> saveGame = new Signal1<String>();
	public Signal1<String> loadGame = new Signal1<String>();
	public Signal2<Integer, Integer> prefGame = new Signal2<Integer, Integer>();
	
	private GameState state;
	
	private QMenu game;
	private QMenu view;
	private QMenu settings;
	private QMenu help;
	
	private QAction newAct;
	private QAction loadAct;
	private QAction saveAct;
	private QAction saveAsAct;
	private QAction networkgameAct;
	private QAction undoMoveAct;
	private QAction resignAct;
	private QAction quitAct;
	
	private QAction fullscreenAct;
	private QAction showLogsAct;
	private QAction showStatisticsAct;
	private QAction preferencesAct;
	private QAction playerSettingsAct;
	
	private QAction aboutAbaloneAct;
	private QAction reportProblemAct;
	private QAction aboutAct;
	
	private BoardWidget boardWidget;
	private GameInfoWidget gameInfoWidget;
	
	private String outputFile;
	private String filter = "Save Files (*.sav)";
	
	public AbaloneFront(GameState state)
	{
		this.state=state;
		QMenuBar menuBar = new QMenuBar();
		setMenuBar(menuBar);
		setWindowIcon(new QIcon("classpath:abalone/gui/Icons/logo.png"));
		setWindowTitle("Abalone-game");
		setMinimumSize(300,300);
		
		MainWidget game = new MainWidget();
		setCentralWidget(game);
		
		try
		{
			createActions();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		createMenus();

		this.setMinimumSize(this.sizeHint());
	}
	
	public void newGame()
	{
		newGame.emit();
	}
	
	public void load()
	{
		String fileName = QFileDialog.getOpenFileName(this, tr("load game"), QDir.currentPath(), new QFileDialog.Filter(filter));
		loadGame.emit(fileName);
	}
	
	public void save()
	{
		QFileInfo file = new QFileInfo(outputFile);
		if(file.exists())
		{
			saveGame.emit(outputFile);
		}
		else
		{
			saveAs();
		}
	}
	
	public void saveAs()
	{
		String format = "sav";
        String initialPath = QDir.currentPath() + tr("/untitled.") + format;
        outputFile = QFileDialog.getSaveFileName(this, tr("Save As"), initialPath, new QFileDialog.Filter(filter));
        if(!outputFile.equals(""))
        {
			saveGame.emit(outputFile);
		}
	}
	
	public void pref()
	{
		PreferencePage pref = new PreferencePage(this);
		int outcome = pref.exec();
		if(outcome == DialogCode.Accepted.value())
		{
			prefGame.emit(new Integer(pref.player1), new Integer(pref.player2));
		}
	}
	
	public void aboutAbalone()
	{
		QWebView view = new QWebView();
		view.setBaseSize(800, 600);
		view.setWindowTitle("About Abalone");
        view.load(new QUrl("http://en.wikipedia.org/wiki/Abalone_(board_game)"));
        view.show();
	}
	
	public void reportProblem()
	{
		QWebView view = new QWebView();
		view.setBaseSize(800, 600);
		view.setWindowTitle("IssueTracker");
        view.load(new QUrl("http://code.google.com/p/abalone-game/issues/list"));
        view.show();
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
		newAct = new QAction(new QIcon("classpath:abalone/gui/Icons/new.png"),tr("&New Game"), this);
		newAct.setShortcut(new QKeySequence(tr("Ctrl+N")));
		newAct.setStatusTip(tr("Create a new Game"));
		newAct.triggered.connect(this, "newGame()");
		
		loadAct = new QAction(new QIcon("classpath:abalone/gui/Icons/open.png"),tr("&Load"), this);
		loadAct.setShortcut(tr("Ctrl+L"));
		loadAct.setStatusTip(tr("Opens a saved game"));
		loadAct.triggered.connect(this, "load()");
		
		saveAct = new QAction(new QIcon("classpath:abalone/gui/Icons/save.png"),tr("&Save"), this);
		saveAct.setShortcut(tr("Ctrl+S"));
		saveAct.setStatusTip(tr("Saves a game"));
		saveAct.triggered.connect(this, "save()");
		
		saveAsAct = new QAction(new QIcon("classpath:abalone/gui/Icons/saveAs.png"),tr("Save &As"), this);
		saveAsAct.setShortcut(tr("Ctrl+A"));
		saveAsAct.setStatusTip(tr("Saves a game under a new name"));
		saveAsAct.triggered.connect(this, "saveAs()");
		
		//networkgameAct = new QAction(new QIcon("classpath:abalone/gui/Icons/networkGame.png"),tr("Network Game"), this);
		//networkgameAct.setStatusTip(tr("Play a game over the network"));
		
		//undoMoveAct = new QAction(new QIcon("classpath:abalone/gui/Icons/undo.png"),tr("Undo move"), this);
		//undoMoveAct.setShortcut(tr("Ctrl+Z"));
		//undoMoveAct.setStatusTip(tr("1 step back"));
		
		resignAct = new QAction(new QIcon("classpath:abalone/gui/Icons/resign.png"),tr("&Resign"), this);
		resignAct.setShortcut(tr("Ctrl+R"));
		resignAct.setStatusTip(tr("Resign this game"));
		
		quitAct = new QAction(new QIcon("classpath:abalone/gui/Icons/exit.png"),tr("&Quit"), this);
		quitAct.setShortcut(tr("Ctrl+Q"));
		quitAct.setStatusTip(tr("Quits the game"));
		quitAct.triggered.connect(this, "close()");
		
		fullscreenAct = new QAction(tr("Fullscreen"), this);
		fullscreenAct.setStatusTip(tr("View the game fullscreen"));
		
		showLogsAct = new QAction(tr("Show Logs"), this);
		showLogsAct.setStatusTip(tr("Shows a log of the game"));
		
		showStatisticsAct = new QAction(tr("Show Statistics"), this);
		showStatisticsAct.setStatusTip(tr("Shows the statistics of the game"));
		
		preferencesAct = new QAction(tr("Preferences"), this);
		preferencesAct.setStatusTip(tr("Preferences menu"));
		preferencesAct.triggered.connect(this, "pref()");
		
		aboutAbaloneAct = new QAction(tr("About Abalone"), this);
		aboutAbaloneAct.setStatusTip(tr("Link to abalone wiki"));
		aboutAbaloneAct.triggered.connect(this, "aboutAbalone()");
		
		reportProblemAct = new QAction(tr("Report Problem"), this);
		reportProblemAct.setStatusTip(tr("Link to our issuetracker"));
		reportProblemAct.triggered.connect(this, "reportProblem()");
		
		aboutAct = new QAction(tr("About"), this);
		aboutAct.setStatusTip(tr("About this game"));
		aboutAct.triggered.connect(this, "about()");
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
		//game.addAction(undoMoveAct);
		//game.addAction(resignAct);
		//game.addSeparator();
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
	
	/**
	 * This is a wrapper Widget for the main parts of the window, 
	 * added to make parts of the window dockable (statistics for 
	 * instance) and some parts not (the ones in this widget)
	 */
	class MainWidget extends QWidget
	{

		public MainWidget()
		{
			QHBoxLayout leftRight = new QHBoxLayout();
			boardWidget = new BoardWidget(state);
			leftRight.addWidget(boardWidget);
			leftRight.addSpacing(20);
			gameInfoWidget = new GameInfoWidget(state);
			leftRight.addWidget(gameInfoWidget);

			setLayout(leftRight);
		}
	}
	
	/**
	 * A separate preferences widget that will only be used by the 
	 * Front
	 */
	private class PreferencePage extends QDialog
	{
		public int player1;
		public int player2;

        public PreferencePage(QWidget parent) 
        {
            super(parent);
            QGroupBox configGroup = new QGroupBox(tr("Players"));
            
            QLabel player1Label = new QLabel(tr("Player 1"));
            QComboBox player1Combo = new QComboBox();
            player1Combo.addItem(tr("Minimax AI"));
            player1Combo.addItem(tr("Human"));
            player1Combo.setCurrentIndex(player1);
            player1Combo.currentIndexChanged.connect(this, "setPlayer1(Integer)");
            
            QHBoxLayout player1Layout = new QHBoxLayout();
            player1Layout.addWidget(player1Label);
            player1Layout.addWidget(player1Combo);
            
			QLabel player2Label = new QLabel(tr("Player 2"));
            QComboBox player2Combo = new QComboBox();
            player2Combo.addItem(tr("Minimax AI"));
            player2Combo.addItem(tr("Human"));
            player2Combo.setCurrentIndex(player2);
            player2Combo.currentIndexChanged.connect(this, "setPlayer2(Integer)");
            
            QHBoxLayout player2Layout = new QHBoxLayout();
            player2Layout.addWidget(player2Label);
            player2Layout.addWidget(player2Combo);
            
            QPushButton accept = new QPushButton(tr("Accept"));
            accept.released.connect(this, "accept()");
            QPushButton reject = new QPushButton(tr("Reject"));
            reject.released.connect(this, "reject()");
            
            QHBoxLayout buttons = new QHBoxLayout();
            buttons.addWidget(accept);
            buttons.addWidget(reject);
            
            QVBoxLayout configLayout = new QVBoxLayout();
            configLayout.addLayout(player1Layout);
            configLayout.addLayout(player2Layout);
            configGroup.setLayout(configLayout);
            
            QVBoxLayout mainLayout = new QVBoxLayout();
            mainLayout.addWidget(configGroup);
            mainLayout.addStretch(1);
            mainLayout.addLayout(buttons);
            
            setLayout(mainLayout);
        }
        
        private void setPlayer1(Integer num)
        {
			int val = num.intValue();
			player1 = val;
		}
        
        private void setPlayer2(Integer num)
        {
			int val = num.intValue();
			player2 = val;
		}
    }

	public BoardWidget getBoardWidget()
	{
		return boardWidget;
	}

	/**
	 * Method for updating (redrawing) the front-end quickly
	 * with the current game(state) in mind
	 */
	public void updateFront()
	{
		updateFront(this.state);
	}
	
	/**
	 * Updates the whole front end with a new GameState
	 */
	public void updateFront(GameState state)
	{
		this.state = state;
		boardWidget.updateBoard(state);
		gameInfoWidget.updateGameInfo(state);
	}
	

}
