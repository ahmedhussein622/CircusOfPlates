import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;




public class GameModel implements java.io.Serializable , ShapesIterator  {

	public static int MAXIMUMHEIGH = 600;
	

	public Player firstPlayer;
	public Player secondtPlayer;
	
	public ArrayList<Shape> fallingShapes;// shapes that fall from shelfs
	public ArrayList<Shape> usedShapes;// shapes that a player throws when he collects three similar shapes
	public ArrayList<Integer> pressedKeys;// hold pressed keys and update players according to them
	public ArrayList<Shelf> shelfs;// the four shelfs
	
	public long remainingTime;
	
	public int width;// width of the panel
	public int height;// height of the panel
	
	// used in calculating remaining time
	private long previousTime;
	private long currentTime;
	
	

	
	
	public GameModel(int width, int height) {
		this.width = width;
		this.height = height;
		
		firstPlayer    = new Player(200 , 500 , 100 , 100 , 20 , 20);
		secondtPlayer  = new Player(600 , 500 , 100 , 100 , 20 , 20);
		
		
		fallingShapes = new ArrayList<Shape>();
		usedShapes = new ArrayList<Shape>();
		pressedKeys = new ArrayList<Integer>();
		
		shelfs = new ArrayList<Shelf>();
		shelfs.add( Shelf.getShelf(Shelf.LEFT, 800, new Point(-600,50)));
		shelfs.add( Shelf.getShelf(Shelf.LEFT, 800, new Point(-700,100)));
		shelfs.add( Shelf.getShelf(Shelf.RIGHT, 800, new Point(width+700,50)));
		shelfs.add( Shelf.getShelf(Shelf.RIGHT, 800, new Point(width+700,100)));
		
		shelfs.get(0).setSpeedOfPlates(2);
		shelfs.get(0).setDistanceBetweenShapes(300);
		shelfs.get(1).setSpeedOfPlates(2);
		shelfs.get(1).setDistanceBetweenShapes(300);
		shelfs.get(2).setSpeedOfPlates(4);
		shelfs.get(2).setDistanceBetweenShapes(300);
		shelfs.get(3).setSpeedOfPlates(5);
		shelfs.get(3).setDistanceBetweenShapes(300);
		
		
		
	}
	
	
	
	
	
	
	public boolean update() {
		
		// update time
		currentTime = System.currentTimeMillis();
		remainingTime -= currentTime-previousTime;
		previousTime = currentTime;
		
		updateShapes();
		updateShelfs();
		return (updatePlayers() && remainingTime  >= 0);
	}
	
	
	private void updateShapes() {
		
		for(int i = 0; i < fallingShapes.size();i++)// Update shapes
			fallingShapes.get(i).update();
		
		for(int i  = 0; i < fallingShapes.size();i++){// if shapes is out of panel then remove it
			if(fallingShapes.get(i).getLocationX() > height){
				ShapePool.getInstance().releaseShape(fallingShapes.get(i));
				fallingShapes.remove(i);
			}
		}
		
		
		for(int i = 0; i < usedShapes.size();i++)// Update shapes
			 usedShapes.get(i).update();
		
		for(int i  = 0; i <  usedShapes.size();i++){// if shapes is out of panel then remove it
			if( usedShapes.get(i).getLocationX() > height){
				ShapePool.getInstance().releaseShape(usedShapes.get(i));
				 usedShapes.remove(i);
			}
		}
	}
	
	
	
	
	
	
	private boolean updatePlayers(){
		
		playerMovesUpdater();
		int j = 2;
		Player p = firstPlayer;
		Shape[] f;// if player returns some shapes when three shapes have the same color
		
		
		while(j > 0) {// update two players
			
			for(int i = 0; i < fallingShapes.size();i++){
				if(p.catchShape(fallingShapes.get(i))){// player can catch shape at index i
					f = p.addShape(fallingShapes.get(i));// add shape to player
					fallingShapes.remove(i);//remove it from falling shapes
					if(f != null){
						f[0].setSpeedX(10);
						f[0].setSpeedY(-10);
						
						f[1].setSpeedX(-10);
						f[1].setSpeedY(-10);
						f[1].setState(Shape.FALLING);
						
						f[2].setSpeedX(0);
						f[2].setSpeedY(-9);
						f[2].setState(Shape.FALLING);
						
						usedShapes.add(f[0]);
						usedShapes.add(f[1]);
						usedShapes.add(f[2]);
					}
				}
				
			}// end of for
			
			j--;
			p = secondtPlayer;
		}// end of while
	
		if(firstPlayer.getTotalHeight() > MAXIMUMHEIGH || secondtPlayer.getTotalHeight() > MAXIMUMHEIGH){
			return false;
		}
		
		return true;
	}// end of updatePlayers
	
	
	
	private void updateShelfs() {
		
		Shape s;
		for(int i = 0; i < shelfs.size();i++){
			synchronized (shelfs.get(i)) {
				s = shelfs.get(i).update();
				if(s != null){// shelf throw a shape
					s.setState(Shape.FALLING);
					fallingShapes.add(s);
				shelfs.get(i).setSpeedOfPlates(1+(int)(Math.random()*6));
			}
			
			}
		}
	}
	
	
	
	public void reset() {
		remainingTime = 3*60*1000;
		previousTime = System.currentTimeMillis();
		
		Iterator<Shape> it;
		for(int i = 0; i < 4; i++) {//release shapes on shelfs
			it = shelfs.get(i).createIterator();
			while(it.hasNext()){
				ShapePool.getInstance().releaseShape(it.next());
				it.remove();
			}
		}
		
		
		// release falling shapes and used shapes
		
		for(int i = 0 ; i < fallingShapes.size();i++){
			ShapePool.getInstance().releaseShape(fallingShapes.get(i));
		}
		fallingShapes = new ArrayList<Shape>();

		for(int i = 0 ; i < usedShapes.size();i++){
			ShapePool.getInstance().releaseShape(usedShapes.get(i));
		}
		usedShapes = new ArrayList<Shape>();
		
		//release shapes held by player
		firstPlayer    = new Player(200 , 500 , 100 , 100 , 20 , 20);
		secondtPlayer  = new Player(600 , 500 , 100 , 100 , 20 , 20);
	}
	
	private void playerMovesUpdater(){
		for(int i =  0; i < pressedKeys.size();i++){
			switch (pressedKeys.get(i)) {
			
			case KeyEvent.VK_RIGHT:
				firstPlayer.goRight();
				break;
			case KeyEvent.VK_LEFT:
				firstPlayer.goLeft();
				break;
			case KeyEvent.VK_D:
				secondtPlayer.goRight();
				break;
			case KeyEvent.VK_A:
				secondtPlayer.goLeft();
				break;
			default:
				break;
			}
		}
	}


	@Override
	public Iterator<Shape> createIterator() {
		return fallingShapes.iterator();
	}

}// end of game model
