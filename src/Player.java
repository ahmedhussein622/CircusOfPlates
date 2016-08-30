import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ImageIcon;



public class Player implements ShapesIterator , java.io.Serializable{
	
	
		private ImageIcon playerImageIcon;
		private String playerImageURL = "download.jpg";//configuration file \ handle if the iamge is not found
		private int score;
		public LinkedList<Shape> playerStack;
		
		private int locationX;
		private int locationY;
		private int playerDisplacement;
		//--------------------------------------------------------------
		private int playerHeight;
		private int playerWidth;
		private int totalHeight;
		private int totalWidth;
		private int catchingPercision;
		private int X_upperLimit;
		private int X_LowerLimit;
		
		public Player(int locationX , int locationY , int playerWidth , int playerHeight , int playerDisplacement , int catchingPercision){
			playerImageIcon = new ImageIcon(getClass().getResource(playerImageURL));
			score = 0;
			playerStack = new LinkedList<Shape>();
			
			this.locationX = locationX;
			this.locationY = locationY;
			this.playerWidth = playerWidth;
			this.playerHeight = playerHeight;
			this.catchingPercision = catchingPercision;
			this.playerDisplacement = playerDisplacement;
			
			totalWidth = playerWidth;
			totalHeight = playerHeight+100;
			X_LowerLimit = locationX;
			X_upperLimit = X_LowerLimit + playerWidth;
		}
		
		public int getPlayerHeight() {
			return playerHeight;
		}

		public void setPlayerHeight(int playerHeight) {
			this.playerHeight = playerHeight;
		}

		public int getPlayerWidth() {
			return playerWidth;
		}

		public void setPlayerWidth(int playerWidth) {
			this.playerWidth = playerWidth;
		}

		public int getCatchingPercision() {
			return catchingPercision;
		}

		public void setCatchingPercision(int catchingPercision) {
			this.catchingPercision = catchingPercision;
		}

		public int getPlayerDisplacement() {
			return playerDisplacement;
		}
	
		public void setPlayerDisplacement(int playerDisplacement) {
			this.playerDisplacement = playerDisplacement;
		}
	
		public Image getPlayerImage(){
			return playerImageIcon.getImage();
		}
	
		public int getTotalHeight() {
			return totalHeight;
		}
	
		public void setTotalHeight(int totalHeight) {
			this.totalHeight = totalHeight;
		}
	
		public int getTotalWidth() {
			return totalWidth;
		}
	
		public void setTotalWidth(int totalWidth) {
			this.totalWidth = totalWidth;
		}
	
		public int getScore() {
			return score;
		}
	
		public void setScore(int score) {
			this.score = score;
		}
	
		public int getLocationX() {
			return locationX;
		}
	
		public void setLocationX(int locationX) {
			this.locationX = locationX;
		}
	
		public int getLocationY() {
			return locationY;
		}
	
		public void setLocationY(int locationY) {
			this.locationY = locationY;
		}
		
		public void goLeft(){
			if(!checkOnLeftBoundries())return;
			locationX -= playerDisplacement;
			X_LowerLimit -= playerDisplacement;
			X_upperLimit -= playerDisplacement;
			Iterator<Shape> I = playerStack.iterator();
			while(I.hasNext()){
				Shape shape = I.next();
				shape.setLocationX(shape.getLocationX()-playerDisplacement);
			}
		}
		
		public void goRight(){
			if(!checkOnRigthBoundries())return;
			locationX += playerDisplacement;
			X_LowerLimit += playerDisplacement;
			X_upperLimit += playerDisplacement;
			Iterator<Shape> I = playerStack.iterator();
			while(I.hasNext()){
				Shape shape = I.next();
				shape.setLocationX(shape.getLocationX()+playerDisplacement);
			}
		}
		
		public  boolean catchShape(Shape shape){
			
			if(shape.getLocationY() + shape.getHight() >= 700 - getTotalHeight()-catchingPercision && shape.getLocationY() + shape.getHight() <= 700 - getTotalHeight() + catchingPercision){
				int L , U;
				L = shape.getLocationX();
				U = L + shape.width;
				if((L <= X_LowerLimit && U >= X_upperLimit) ||  (L >= X_LowerLimit && L <= X_upperLimit) || (U >= X_LowerLimit && U <= X_upperLimit)){
					
					return true;
				}
			}
			return false;
		}
		
		public Shape[] addShape(Shape shape){
			if(playerStack.size() >= 2 && playerStack.get(playerStack.size()-1).getColor() == (shape.getColor()) && playerStack.get(playerStack.size()-2).getColor() == (shape.getColor())){
				
		
				Shape[]a = new Shape[3];
				a[0] = shape;
				a[1] =  playerStack.get(playerStack.size()-1);
				a[2] =  playerStack.get(playerStack.size()-2);
				
				totalHeight -= playerStack.get(playerStack.size()-1).getHight();
				totalHeight -= playerStack.get(playerStack.size()-2).getHight();
				
				
				playerStack.remove(playerStack.size()-1);
				playerStack.remove(playerStack.size()-1);
				
				
				if(playerStack.size() == 0){
					X_LowerLimit = locationX;
					X_upperLimit = X_LowerLimit + playerWidth;
				}else{
					
					Shape s = playerStack.get(playerStack.size()-1);
					X_LowerLimit = s.getLocationX();
					X_upperLimit = X_LowerLimit + s.getWidth();
				}
				score++;
				return a;
			}else{
				
				totalHeight += shape.getHight();
				shape.setState(Shape.BOUND);
				shape.setLocationY(700 - getTotalHeight());
				shape.setLocationX((X_LowerLimit+X_upperLimit)/2 - shape.getWidth()/2);
				
				playerStack.add(shape);
				
				X_LowerLimit = (X_LowerLimit+X_upperLimit)/2 - shape.getWidth()/2;
				X_upperLimit = X_LowerLimit + shape.getWidth();
			}
			
			return null;
		}
		
		private boolean checkOnLeftBoundries(){
			if(locationX-playerDisplacement < 0 || X_LowerLimit-playerDisplacement < 0){
				return false;
			}
			return true;
		}
		
		private boolean checkOnRigthBoundries(){
			if(locationX+playerWidth+playerDisplacement > Main.width || X_upperLimit+playerDisplacement > Main.width){
				return false;
			}
			return true;
		}

		@Override
		public Iterator<Shape> createIterator() {
			return playerStack.iterator();
		}
}


