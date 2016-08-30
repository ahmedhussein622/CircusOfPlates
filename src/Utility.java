import java.awt.Color;


public class Utility {

	public static Color getRandomColor(){
		
		switch ((int)(Math.random()*6)) {
		case 0:
			return Color.black;
		case 1:
			return Color.blue;
		case 2:
			return Color.yellow;
		case 3:
			return Color.red;
		case 4:
			return Color.green;
		case 5:
			return Color.magenta;
		default:
			return Color.black;
		}
	}
}
