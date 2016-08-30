import java.awt.Color;
import java.awt.Graphics2D;


public abstract class Shape implements java.io.Serializable {
	
	
	public static final int FALLING = 111111 ;
	public static final int CONSTANTSPEED = 222222 ;
	public static final int BOUND = 3333333 ;
	
	public static final double cycleTime  = 1.2;// type between two calls to update
	
	
	
	protected ShapeState falling;
	protected ShapeState constantSpeed;
	protected ShapeState bound;
	protected ShapeState currentState;
	protected Color color;
	protected int locationX;
	protected int locationY;
	protected int width;
	protected int hight;
	protected double speedX;
	protected double speedY;
	
	public Shape(){
		falling = new Falling(this);
		constantSpeed = new ConstantSpeed(this);
		bound = new Bound(this);
		currentState = bound;
	}
	
	
	public abstract void paintComponect(Graphics2D g);
	public abstract void setColor(Color c);
	
	public void update(){
		currentState.update();
	}
	
	
	
	
///////////////////////////////////////////////////////////////////////////
// setters and getters
	
	
	public void setState(int s){
		switch (s) {
		case FALLING:
			currentState = falling;		
			break;
		case CONSTANTSPEED:
			currentState = constantSpeed;
			break;
		case BOUND:
			currentState = bound;
			break;
		default:
			throw new IllegalArgumentException("no such a state");
		}
	}
	
	
	
	public void setLocationX(int x){
		locationX = x;
	}
	public int getLocationX(){
		return locationX;
	}
	
	public void setLocationY(int y){
		locationY = y;
	}
	public int getLocationY(){
		return locationY;
	}
	
	
	public int getWidth(){
		return width;
	}
	
	public int getHight(){
		return hight;
	}
	
	public void setSpeedX(double x){
		speedX = x;
	}
	public void setSpeedY(double y){
		speedY = y;
	}
	
	public Color getColor(){
		return color;
	}
	
	public double getSpeedX(){
		return speedX;
	}
	public double getSpeedY(){
		return speedY;
	}
	
	
}
