import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class Plate extends Shape implements java.io.Serializable{
	
	public Plate() {
		width = 60;
		hight = 15;
		color = Color.red;
	}
	
	@Override
	public void paintComponect(Graphics2D g) {
		g.setColor(color);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.fillPolygon(new int[]{locationX,width+locationX,width+locationX-10,locationX+10},
				new int[]{locationY,locationY,locationY+hight,locationY+hight}, 4);
		
	}

	@Override
	public void setColor(Color c) {
		color = c;
	}

}
