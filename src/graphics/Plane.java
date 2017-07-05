package graphics;

/**
 * Creates a graphical plane, used for calculating lighting
 * @author Dominic
 *
 */
public class Plane {
	public Vector v1, v2, v3;
	public Point p;
	
	/**
	 * Creates a plane given an object to use as a basis
	 * @param p3d The Poly3D object to use as a basis for the plane
	 */
	public Plane(Poly3D p3d){
		p = new Point(p3d.x[0], p3d.y[0], p3d.z[0]);
	
		
		v1 = new Vector(p3d.x[1] - p3d.x[0], p3d.y[1] - p3d.y[0], p3d.z[1] - p3d.z[0]);
		v2 = new Vector(p3d.x[2] - p3d.x[0], p3d.y[2] - p3d.y[0], p3d.z[2] - p3d.z[0]);
		v3 = v1.cross(v2);
	}
}
