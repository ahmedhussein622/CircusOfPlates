import java.util.Iterator;
//we can make it Iterator only and cast it any where more flexible for any object
public interface ShapesIterator {
	public Iterator<Shape> createIterator();
}
