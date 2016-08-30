

public class Falling implements ShapeState , java.io.Serializable{
	
	
	private static final double gravityAcceleration = -.1;
	
	private Shape shape;

	
	public Falling(Shape shape){
		this.shape = shape;
	}

	@Override
	public void update() {
		double t = shape.getSpeedY()*Shape.cycleTime-.5*gravityAcceleration*Math.pow(Shape.cycleTime, 2) + shape.getLocationY();
		shape.setLocationX((int)(shape.getLocationX()+shape.getSpeedX()*shape.cycleTime));
		shape.setLocationY((int)t);
		shape.setSpeedY(shape.getSpeedY()-gravityAcceleration*shape.cycleTime);
	}
	

}
