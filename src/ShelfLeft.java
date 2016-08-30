import java.awt.Graphics2D;
import java.util.Iterator;


public class ShelfLeft extends Shelf implements java.io.Serializable{
	
	
	@Override
	public Shape update() {
		
		// if there is no shapes then add new shape
		if(queueOfShapes.size() == 0){
			addShape();
		}
		
		// update all shapes
		Iterator<Shape> it = queueOfShapes.iterator();
		while(it.hasNext()){
			it.next().update();// updating
		}
		
		// there is a room for new shape to be inserted
		if(queueOfShapes.getLast().getLocationX()-queueOfShapes.getLast().getWidth()>startPoint.x+distanceBetweenShapes){
		
				addShape();
		}
		
		
		
		// check if the first shape is out of the shelf
		if(queueOfShapes.peek().getLocationX() > startPoint.x+(double)19*length/20) {
			return queueOfShapes.removeFirst();
		}
		return null;
	}

	
	
	@Override
	public void setSpeedOfPlates(double speed) {
		this.speed = Math.abs(speed);
		Iterator<Shape> it = queueOfShapes.iterator();
		while(it.hasNext()){
			it.next().setSpeedX(this.speed);
		}
	}

	@Override
	public void paintComponect(Graphics2D g) {
		g.setColor(color);
		g.fillPolygon(new int[]{startPoint.x,startPoint.x+length,startPoint.x+length-width
				,startPoint.x},new int[]{startPoint.y,startPoint.y,startPoint.y+width,startPoint.y+width}, 4);
		
		// paint all shapes
		Iterator<Shape> it = queueOfShapes.iterator();
		while(it.hasNext()){
			it.next().paintComponect(g);
		}
	}
	
	private void addShape(){
		Shape s = ShapePool.getInstance().getShape();
		s.setState(Shape.CONSTANTSPEED);
		s.setSpeedX(speed);
		s.setSpeedY(0);
		s.setLocationX(startPoint.x-10);
		s.setLocationY(startPoint.y-s.getHight());
		s.setColor(Utility.getRandomColor());
		queueOfShapes.add(s);
	}
}
