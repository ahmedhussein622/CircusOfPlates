
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class Dish extends Shape implements java.io.Serializable{
	
	public Dish() {
		//call super call constructor by default
		width = 50;
		hight = 30;
		color = Color.red;
	}
	
	@Override
	public void paintComponect(Graphics2D g) {
		g.setColor(color);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.fillRect(locationX, locationY, width, hight);
	}

	@Override
	public void setColor(Color c) {
		color = c;
	}

}

