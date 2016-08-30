import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


public class GameView extends JPanel{

	
	private static final int waitTime = 15;
	
	private GameModel gameModel;
	private GameController gameControl;
	
	private boolean running;//use this to check if escape was pressed
	//in  this case display buttons
	
	private JButton saveGame;
	private JButton loadGame;
	private JButton newGame;
	private JButton loadShape;
	private JFileChooser fileChooser;// used ot select files for loading saving and loading shapes
	
	
	public GameView() {
		
		gameModel = null;
		
		setFocusable(true);
		setSize(700, 700);
		running = false;
		
		saveGame  = new JButton("save");
		loadGame  = new JButton("load");
		newGame   = new JButton("new Game");
		loadShape = new JButton("load Shape");
		this.add(saveGame);
		this.add(loadGame);
		this.add(newGame);
		this.add(loadShape);
		
		fileChooser = new JFileChooser("select file");
		
	}

	
	
	
	public void setGameModel(GameModel gameModel) {
		this.gameModel = gameModel;
	}
	
	/**
	 * observer design pattern
	 * game control observes game view for action
	 * game view informs game model when it's updated
	 * @param c
	 */
	public void setGameControl(GameController c){
		gameControl = c;
		newGame.addActionListener(gameControl);
		loadGame.addActionListener(gameControl);
		saveGame.addActionListener(gameControl);
		loadShape.addActionListener(gameControl);
	}
	
	
	@Override
	public void paintComponent(Graphics g0) {
		
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		
		if(running){
			paintGame(g);
		}
		
	}
	
	
	
	/**
	 * if game is running then paint component will paint shapes and player in the method
	 */
	private void paintGame(Graphics2D g) {	
		
		for(int i = 0 ; i < gameModel.shelfs.size();i++){
			synchronized(gameModel.shelfs.get(i)){
				gameModel.shelfs.get(i).paintComponect(g);
			}
		}
		
		
		Iterator<Shape> it = gameModel.createIterator();
		
		while(it.hasNext()){
			it.next().paintComponect(g);
		}
		
		
		for(int i = 0; i < gameModel.usedShapes.size();i++) {
			gameModel.usedShapes.get(i).paintComponect(g);
		}
		
		// paint players
		Player t = gameModel.firstPlayer;
		g.drawImage(t.getPlayerImage(), t.getLocationX(), t.getLocationY() , t.getPlayerWidth() , t.getPlayerHeight() ,  null);
		for(int i = 0; i < t.playerStack.size(); i++)
			t.playerStack.get(i).paintComponect(g);
		
		t = gameModel.secondtPlayer;
		g.drawImage(t.getPlayerImage(), t.getLocationX(), t.getLocationY() , t.getPlayerWidth() , t.getPlayerHeight() ,  null);
		for(int i = 0; i < t.playerStack.size(); i++)
			t.playerStack.get(i).paintComponect(g);
		
		g.setColor(Color.black);
		g.drawString("Second Player Score : "+gameModel.secondtPlayer.getScore(), 10, 15);
		g.drawString(gameModel.firstPlayer.getScore()+": First Player Score", getWidth()-140, 15);
		long minute,second,partsecond ;
		second = gameModel.remainingTime/1000;
		minute = second/60;
		second = second % 60;
		partsecond = gameModel.remainingTime % 1000;
		partsecond = (partsecond*100)/1000;
		g.drawString(minute+" : "+second+" : "+partsecond, getWidth()/2-35, 15);
		g.setColor(Color.yellow);
		int x = t.getLocationY()-GameModel.MAXIMUMHEIGH+t.getPlayerHeight()+100;
		g.drawLine(0,x, getWidth(), x);
		
	}
	
	
	public void setRunning(boolean b){
		running  = b;
		loadGame.setVisible(!running);
		saveGame.setVisible(!running);
		newGame.setVisible(!running);
		loadShape.setVisible(!running);
	}
	
	
	public void showDialog(String message){ 
		JOptionPane.showMessageDialog(this,message);
	}
	
	public URL getURL(){
		
		URL result;
		int x = fileChooser.showOpenDialog(this);
		if (x != JFileChooser.APPROVE_OPTION)
			return null;
		
		try {
			return fileChooser.getSelectedFile().toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getURL_Loading() {
		String URL = "*";
		JFileChooser chooser = new JFileChooser();
		int option = chooser.showOpenDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			URL = chooser.getSelectedFile().toString();
		} else {
			return "*";
		}
	
		return URL;
	}

	public String getURL_Saving() {
		String URL = "*";
		JFileChooser chooser = new JFileChooser();
		int option = chooser.showSaveDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			URL = chooser.getSelectedFile().toString();
		} else {
			return "*";
		}

		return URL;
	}
}
