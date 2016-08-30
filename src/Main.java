import java.awt.Color;


import javax.swing.JFrame;



public class Main {
	
	public static int width = 700;
	public static int height = 700;
	
	public static void main(String[] arg){
		
		GameView panel = new GameView();
		panel.setBackground(Color.white);
		GameController con = new GameController();
		GameModel modle = new GameModel(700, 700);
		panel.setGameModel(modle);
		
		
		con.setGameModel(modle);
		con.setGameView(panel);
		panel.setGameControl(con);
		JFrame frame = new JFrame();
		frame.add(panel);
		frame.setSize(700, 700);
		frame.setLocation(100, 100);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		modle.reset();
		con.start();
		
		
	}
}
