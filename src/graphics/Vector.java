package graphics;

public class Vector {
	
	private double x, y, z;
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	public double length() {
		return Math.sqrt((x*x) + (y*y) + (z*z));
	}

	public Vector normalise() {
		double length = length();
		x /= length;
		y /= length;
		z /= length;
		
		return this;
	}

	public Vector cross(Vector v) {
		double a = v.getX();
		double b = v.getY();
		double c = v.getZ();
		
		return new Vector((y*c - z*b), (z*a - x*c), (x*b - y*a));
	}

	public Vector minus(Vector v) {
		double a = v.getX();
		double b = v.getY();
		double c = v.getZ();
		
		return new Vector((x - a), (y - b), (z - c));
	}

	public double dot(Vector v) {
		double a = v.getX();
		double b = v.getY();
		double c = v.getZ();
		
		return (x*a + y*b + z*c);
	}

	public Vector plus(Vector v) {
		double a = v.getX();
		double b = v.getY();
		double c = v.getZ();
		
		return new Vector((x+a), (y+b), (z+c));
	}

}
