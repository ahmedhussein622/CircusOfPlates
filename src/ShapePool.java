
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

//variances--> one abstrat class / Used Array / Max number of objects / starting number / inital pool
public class ShapePool  implements java.io.Serializable{
	
	private static ShapePool shapePool;
	private int defaultStartingSize;
	private ArrayList<Shape> unUsedShapes;
	private ArrayList<Class> shapeClass;// different class of shapes
	
	
	// singleton synchronized to avoid being called by two threads at the beging
	// as that might case shapePool to be initialized twice
	public static synchronized ShapePool getInstance(){
		if(shapePool== null)
			 shapePool = new ShapePool();
		
		return  shapePool;
	}
	
	
	private ShapePool(){
		
		unUsedShapes = new ArrayList<Shape>();
		shapeClass = new ArrayList<Class>();
		defaultStartingSize = 50;
		// try to load some known class if they weren't removed for some reason
		try {// try loading Plate
			shapeClass.add(ClassLoader.getSystemClassLoader().loadClass("Plate"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {// try loading Dish
			shapeClass.add(ClassLoader.getSystemClassLoader().loadClass("Dish"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		///contructInitialShapesPool();
	}
	
	private void contructInitialShapesPool(){// use clone instead
		if(shapeClass.size() > 0){
			for(int i = 0 ; i < defaultStartingSize ; i++){
				try {
					unUsedShapes.add((Shape)shapeClass.get((int)(Math.random()*(shapeClass.size()))).newInstance());
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * return shape to freeShapes list to be used again
	 * it's assumed that the caller will not use the object he just freed again
	 * pleas set it to null where ever you use it
	 * @param s shape to be sent back to the pool
	 */
	
	public void releaseShape(Shape shape){
		unUsedShapes.add(shape);
		resetShape(shape);
	}
	
	private void resetShape(Shape shape){
		shape.setState(Shape.BOUND);
	}
	
	
	/**
	 * randomly chooses a shape from the available class and returns it
	 * if there is no free Shapes then a new shape will be created
	 * else one of the shapes in freeShapes will be returned
	 * @return Shape 
	 */
	public Shape getShape(){
		
		if(unUsedShapes.size() != 0){// there is a free shape then return it
			Shape s = unUsedShapes.get(0);
			unUsedShapes.remove(0);
			return s;
		}
		
		// if you are here then there is no free shapes and a new randomly chosen shape will be
		// created
		
		if(shapeClass.size() == 0)
			throw new IllegalArgumentException("no class to create instance . Load some shapes first");
		int n = 0 + (int)(Math.random()*(shapeClass.size()));
		
		try {
			return (Shape)shapeClass.get(n).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * gets a shape from class className
	 * if no shape of type className is in  freeShapes then a new instance is initialized
	 * if there is a free instance then it's returned
	 * @param name : name of the class to get shape from it's type
	 * @return shape from class className
	 */
	public Shape getShape(String className){
		
		for(int i = 0 ;i < unUsedShapes.size();i++) {
			if(unUsedShapes.get(i).getClass().getName().equals(className)){
				Shape s = unUsedShapes.get(i);
				unUsedShapes.remove(i);
				return s;
			}
		}
		
		
		
		return null;
	}
	
	
	
	/**
	 * dynamic class loading
	 * there is a problem here as this code will run only on Linux 
	 * problems occur in windows 
	 * @param url of the class to be loaded
	 */
	
	public void load(URL url){
		
		URLClassLoader loader ;
		Method addNewURLMethod = null;
		
		// modify system class loader get method (addURL) set it to visible
		// then use it to add the new url so you can directly use system class loader 
		// where ever the class to be loaded is
		
		loader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		 addNewURLMethod  = null;
		try {
			 addNewURLMethod  = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			 addNewURLMethod .setAccessible(true);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		///// dynamic class loading is in here////////////////////////////////////				
		try {
			String g = url.toString();
			String className = url.getFile();
			// this lines are written for ubuntu , problems will occur when it's in windows 
			className = className.substring(className.lastIndexOf("/")+1,className.lastIndexOf("."));
			g = g.substring(0, g.lastIndexOf("/")+1);// get the path
			url = new URL(g);// the final URL to load class from
			
			System.out.println(g);
			System.out.println(className);
			System.out.println(url);
			
			
			addNewURLMethod.invoke(loader, url);
			shapeClass.add(loader.loadClass(className));
			
		}catch(Exception e){e.printStackTrace();}
	}
}
