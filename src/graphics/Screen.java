package graphics;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

/**
 * The viewport of the ship, contains the camera and all objects to be rendered by it
 * @author Dominic
 *
 */
public class Screen extends JPanel{
	
	private double sleepTime = 1000/global.Variables.getRefreshRate(), lastRefresh = 0;
	public static Vector viewFrom;
	public static Vector viewTo;
	public static Vector lightDir;
	public static double[][] cameraSystem, worldToCamera, CM;
	public static Vector N, U, V;
	
	private double lightPosition;
	
	public static int nPoly = 0, nPoly3D = 0;
	public static ArrayList<Poly3D> poly3Ds = new ArrayList<Poly3D>();
	boolean w, a, s, d, e, q;
	
	private String nickname;
	private Integer shipIndex = null;
	private boolean pilot;
	private boolean selfDestruct = false;
	private int destructCount = 1;
	private boolean crosshair;
	private boolean debug = false;
	private int asteroidCount = 0;
	
	/**
	 * Creates a new Screen object
	 * @param nickname The nickname of the user who is the pilot of the ship
	 * @param pilot Whether this user is the pilot or the engineer
	 */
	public Screen(String nickname, boolean pilot, boolean crosshair){
		
		this.nickname = nickname;
		this.pilot = pilot;
		this.crosshair = crosshair;
		
		//Create starting vectors
		viewFrom = new Vector(0, 0, 0);
		viewTo = new Vector(0, 0, 1);
		lightDir = new Vector(1, 1, 1);
		lightDir = lightDir.normalise();
		
		//Create camera vectors
		N = viewTo.plus(viewFrom);
		N = N.normalise();
		U = new Vector(0, 1, 0);
		U = U.normalise();
		V = U.cross(N);
		V = V.normalise();
		U = N.cross(V);
		U = U.normalise();
		
		cameraSystem = new double[][] { {V.getX(), V.getY(), V.getZ(), 0},
										{U.getX(), U.getY(), U.getZ(), 0},
										{N.getX(), N.getY(), N.getZ(), 0},
										{0,        0,        0,        1}};
										
		CM = Matrix.getCM(viewFrom, V, U, N, 2);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g){
		//Draw the background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
		
		//Perform camera calculations based on current keypresses
		camera();
		setLight();
		
		g.setColor(Color.WHITE);
		
		//Draw all polygons onto the screen
		nPoly = poly3Ds.size();

		poly3Ds.parallelStream().forEach(poly3D -> poly3D.update());

		setDrawOrder();
		
		for(Poly3D p : poly3Ds){
			p.poly.drawPoly(g);
		}
		
//		System.out.println("Predicted Polygons: " + asteroidCount * 4 * 27);
//		System.out.println("Actual Number: " + poly3Ds.size());
		
		warningLight(g);
		
		
		
		//Draw debugging information
		Vector camCoords = Matrix.multiplyVector(cameraSystem, new Vector(0, 0, 0));
		g.setColor(Color.WHITE);
		
		if(crosshair){
			g.drawLine(global.Variables.getWidth()/2 - 5, global.Variables.getHeight()/2, global.Variables.getWidth()/2 + 5, global.Variables.getHeight()/2);
			g.drawLine(global.Variables.getWidth()/2, global.Variables.getHeight()/2 - 5, global.Variables.getWidth()/2, global.Variables.getHeight()/2 + 5);
		}
		if(debug){
	        g.drawString("viewFrom: "+ viewFrom, 40, 40);
	        g.drawString("camera: "+ camCoords,  40, 60);
	        g.drawString("viewTo: "+ viewTo,     40, 80);
	        g.drawString("V: "+ V,               40, 100);
	        g.drawString("U: "+ U,               40, 120);
	        g.drawString("N: "+ N,               40, 140);
		}
        
		sleepAndRefresh();
	}
	
	/**
	 * If the ship is about to be destroyed the screen will fade in and out a red light
	 * @param g Graphics object to use
	 */
	private void warningLight(Graphics g) {
		if(selfDestruct){
			if(destructCount <= 10){
				Color warning = new Color(255 * destructCount / 10, 0, 0, 100);
				g.setColor(warning);
				g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
				destructCount++;
			}
			else if(destructCount > 10 && destructCount <= 20){
				Color warning = new Color(255, 0, 0, 100);
				g.setColor(warning);
				g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
				destructCount++;
			}
			else if(destructCount > 20 && destructCount <= 30){
				Color warning = new Color(255 * (30 - destructCount) / 10, 0, 0, 100);
				g.setColor(warning);
				g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
				destructCount++;
			}
			else if(destructCount > 30 && destructCount <= 40){
				Color warning = new Color(25, 0, 0, 100);
				g.setColor(warning);
				g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
				destructCount++;
			}
			else{
				destructCount = 1;
				Color warning = new Color(25, 0, 0, 100);
				g.setColor(warning);
				g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
			}
		}
	}

	/**
	 * If it has been longer than the sleepTime since the last refresh, repaint is called
	 */
	public void sleepAndRefresh(){
		while(true){
			if(System.currentTimeMillis() - lastRefresh > sleepTime){
				lastRefresh = System.currentTimeMillis();
				repaint();
				break;
			}
			else{
				try{
					Thread.sleep((long)(sleepTime - (System.currentTimeMillis() - lastRefresh)));
				}
				catch(Exception e){
					
				}
			}

		}
	}
	
	/**
	 * Create the order that the polygons should be drawn in in order to make sure hidden sides are hidden
	 */
	private void setDrawOrder(){
		poly3Ds.sort((poly1, poly2) -> {
			if(poly1.avgDistance > poly2.avgDistance){
				return -1;
			}
			else if(poly1.avgDistance == poly2.avgDistance){
				return 0;
			}
			else{
				return 1;
			}
		});
	}
	
	/**
	 * Sets the light position
	 */
	private void setLight(){
		int length = global.Variables.getMapSize();
		Vector mapSize = new Vector(length, length, length);
		lightDir = new Vector (mapSize.getX()/2 - (mapSize.getX()/2 + Math.cos(lightPosition) * mapSize.getX() * 10), mapSize.getY()/2 - (mapSize.getY()/2 + Math.sin(lightPosition) * mapSize.getY() * 10), -200);
	}
	
	/**
	 * Updates the camera vectors
	 */
	private void camera(){
		// Generate CM matrix for transforming points from global coordinate system to camera coordinate system
		CM = Matrix.getCM(viewFrom, V, U, N, 10);
	}
	
	/**
	 * Sets the map that the screen should display to a new map
	 * @param map The map that you want to display
	 */
	public void setMap(ArrayList<Poly3D> map){
		poly3Ds = map;
	}
}
