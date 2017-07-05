package graphics;

/**
 * Representation of a 3D point
 * @author Dominic
 *
 */
public class Point {
	public double x, y, z;
	public double h = 1;
	
	/**
	 * Creates a new point
	 * @param x The x-coordinate of the point
	 * @param y The y-coordinate of the point
	 * @param z The z-coordinate of the point
	 */
	public Point(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Calculates the position of a point plus a vector
	 * @param v The vector to add to the point
	 * @return The new point
	 */
	public Point pointPlusVector(Vector v){
		Point q = new Point(x + v.getX(), y + v.getY(), z + v.getZ());
		return q;
	}
	
	/**
	 * Calculates the position of a point minus a vector
	 * @param v The vector to subtract from the point
	 * @return The new point
	 */
	public Point pointMinusVector(Vector v){
		Point q = new Point(x - v.getX(), y - v.getY(), z - v.getZ());
		return q;
	}
	
	/**
	 * Calculates a vector given one point minus another point
	 * @param q The point to take away from this point
	 * @return The resulting vector
	 */
	public Vector pointMinusPoint(Point q){
		Vector v = new Vector(x - q.x, y - q.y, z - q.z);
		return v;
	}
	
	/**
	 * Sets the parameters of this point
	 * @param p The point to set this point equal to
	 */
	public void setPoint(Point p){
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}
}
