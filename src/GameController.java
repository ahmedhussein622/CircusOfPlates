import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;




public class GameController implements KeyListener ,ActionListener {
	
	private static final int waitTime = 15;
	
	private GameView view;
	private GameModel model;
	private Thread gameLoopThread;//to update game model;
	private Updater updaterRunnable;// implements runnable and update game model
	private boolean running ;
	private boolean gameFinished;//due to reaching upper bound of shapes height of run out of time
	
	public GameController(){
		
		updaterRunnable = new Updater();
		gameLoopThread = new Thread(updaterRunnable);
		running = false;
		gameFinished = false;
	}
	
	public void setGameView(GameView view){
		this.view = view;
		view.addKeyListener(this);
	}
	
	public void setGameModel(GameModel model){
		this.model = model;
	}
	
	
	public void start() {
		running = true;
		view.setRunning(true);
		gameLoopThread.start();
	}
	
	public void stop() {
		
		running = false;
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			if(running || gameFinished){
				running = false;
				view.setRunning(false);
				view.repaint();
				return;
			}
			else {
				gameLoopThread = new Thread(updaterRunnable);
				start();
				return;
			}
		}
		model.pressedKeys.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int x = model.pressedKeys.indexOf(e.getKeyCode());
		if(x != -1)
			model.pressedKeys.remove(x);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void newGame(){
		model.reset();
		running = true;
		gameFinished = false;
		gameLoopThread = new Thread(updaterRunnable);
		model.pressedKeys = new ArrayList<Integer>();
		start();
	}
	
	public void loadShape(URL url) {
		ShapePool.getInstance().load(url);
	}
	
	
	private String calculateGameResult(){
		if(model.firstPlayer.getTotalHeight() > GameModel.MAXIMUMHEIGH){
			return "second Player Wins";
		}
		
		if(model.secondtPlayer.getTotalHeight() > GameModel.MAXIMUMHEIGH){
			return "first Player Wins";
		}
		
		if(model.firstPlayer.getScore() > model.secondtPlayer.getScore()) {
			return "first Player Wins by " +( model.firstPlayer.getScore()-model.secondtPlayer.getScore())
					+"  points";
		}
		
		if(model.secondtPlayer.getScore() > model.firstPlayer.getScore()) {
			return "first Player Wins by " +( model.secondtPlayer.getScore()-model.firstPlayer.getScore())
					+"  points";
		}
		return null;
	}
	
	
	public void update(){
		gameFinished = !model.update();
		if(gameFinished){
			running = false;
			view.showDialog(calculateGameResult());
		}
		view.repaint();
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////	
//this class is just to implement Runnable and update gameModel
	private class Updater implements Runnable{
		
		
		
		@Override
		public void run() {
			while(running){
				update();
				try {
					Thread.sleep(GameController.waitTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	
	}
	


	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton a = (JButton)e.getSource();
		if(a.getText().equals("new Game")){
			newGame();
		}
		if(a.getText().equals("load Shape")){
			URL u = view.getURL();
			if(u == null)
				return ;
			loadShape(u);
		}
		if(a.getText().equals("load")){
			model = deserialize();
			view.setGameModel(model);
		}
		if(a.getText().equals("save")){
			serialize();
		}
		view.requestFocus();
	}

	
	public void serialize(){
		String URL = view.getURL_Saving() + ".ser";
		try
	      {
	         FileOutputStream fileOut =
	         new FileOutputStream(URL);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(model);
	         out.close();
	         fileOut.close();
	       
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	
	public GameModel deserialize(){
		GameModel gameModel = null;
		String URL = view.getURL_Loading();
		try
	      {
	         FileInputStream fileIn = new FileInputStream(URL);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         gameModel = (GameModel) in.readObject();
	         in.close();
	         fileIn.close();
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return null;
	      }catch(ClassNotFoundException c)
	      {
	         c.printStackTrace();
	         return null;
	      }
		return gameModel;
	}
	
	
}
