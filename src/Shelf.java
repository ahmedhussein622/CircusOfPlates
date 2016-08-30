import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;



public abstract class Shelf implements ShapesIterator , java.io.Serializable{
	
	// for the alignment
	public static final int LEFT = 64387690;
	public static final int RIGHT = 93807245;
	
	protected static final int  width = 10;// width of the Shelf
	
	protected Point startPoint;// this is the start point of the shelf . it should be chosen 
	// such that it will be out of the boundary of the game frame.
	protected int length;
	protected Deque<Shape> queueOfShapes;// queue of the shapes on the shelf.
	protected Color color;
	protected double speed;
	protected int distanceBetweenShapes;
	
	/**
	 * factory design pattern
	 * @param aalignment 	: right or left of the screen
	 * @return shelf 		: that has the desired alignment
	 * @return startPoint 	: start point of shelf which shall be out of the screen 
	 * @return endPoing		: end of shelf , any shape that reaches it is dropped
	 */
	public static Shelf getShelf(int alignment,int length,Point startPoing) {
		
		if(alignment != LEFT && alignment != RIGHT){// wrong alignment
			throw new IllegalArgumentException("alignment is wrong , chose Shelf.LEFT or Shelf.RIGHT");
		}
		
		Shelf result;
		if(alignment == LEFT){
			result = new ShelfLeft();
		}
		else{
			result = new ShelfRight();
		}
		
		// set dimensions
		result.length = length;
		result.startPoint = new Point(startPoing.x, startPoing.y);
		result.queueOfShapes = new LinkedList<Shape>();
		result.color = Color.black;
		result.distanceBetweenShapes = 20;
		return result;
		
		
	}
	
	
	
	
	
	/**
	 * update all the shapes in it and check if a shape is at the end point
	 * then it will set it's state to Falling and return it to be used in other operations
	 * probably in GameModel
	 * @return shape that has a state set to ConstantSpeed, null if there is no shapes to be thrown
	 */
	public abstract Shape update();
	
	
	/**
	 * set the speed of plates which in turn will control the rate by which they fall
	 * @param speed absolute speed
	 */
	public abstract void setSpeedOfPlates(double speed);
	
	
	/**
	 * paint the shelf from start point to end point
	 * @param g
	 */
	public abstract void paintComponect(Graphics2D g);

	
	
	
//////////////////////////////////////////////////////////////////////////////////////////
	// setters and getters
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public void setDistanceBetweenShapes(int distance){
		distanceBetweenShapes = distance;
	} 
	
	public int getDistanceBetweenShapes(){
		return distanceBetweenShapes;
	} 
	
	public double getSpeed(){
		return speed;
	}
	
	public Iterator<Shape> createIterator() {
		return queueOfShapes.iterator();
	}
	
	
	
}
