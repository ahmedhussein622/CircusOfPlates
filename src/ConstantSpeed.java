
public class ConstantSpeed implements ShapeState, java.io.Serializable {
	
	private Shape shape;
	
	
	public ConstantSpeed(Shape shape) {
		this.shape = shape;
	}
	
	@Override
	public void update() {
		shape.setLocationX((int)(shape.getLocationX()+shape.getSpeedX()*shape.cycleTime));
		shape.setLocationY((int)(shape.getLocationY()+shape.getSpeedY()*shape.cycleTime));
	}
	

}
