package graphics;

/**
 * Contains calculations required to calculate the position of points in camera space
 * @author Dominic
 *
 */
public class Calculations {
	private static double t = 0;
	public static Vector w1, w2, viewVec, rotateVec, dirVec, planeVec1, planeVec2;
	public static Point calcFocusPos;
	
	/**
	 * Calculates the position of a point in camera space from a given global coordinate
	 * @param viewFrom The position of the camera
	 * @param viewTo The position the camera is looking at
	 * @param p2 The position of the point to be transformed
	 * @return
	 */
	public static Point calcPos(Vector viewFrom, Vector viewTo, Vector p2){
		Vector pInCamera = Matrix.multiplyVector(Screen.CM, p2);
		return new Point((pInCamera.getX()*5000)/pInCamera.getZ(), (pInCamera.getY()*5000)/pInCamera.getZ(), pInCamera.getZ());
	}
}
