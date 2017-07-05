package graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

/**
 * Creates a polygon for a given side of an object
 * @author Dominic
 *
 */
public class PolygonObj {
	private Polygon p;
	private Color c;
	public double avgDist = 0;
	public double light = 1;
	public boolean draw = true;
	
	/**
	 * Creates a polygon from given arrays of x and y coordinates
	 * @param x An array of all the x-coordinates of the vertices of the polygon
	 * @param y An array of all the y-coordinates of the vertices of the polygon
	 * @param c The colour of the polygon
	 */
	public PolygonObj(double[] x, double[] y, Color c){
		p = new Polygon();
		for(int i = 0; i < x.length; i++){
			p.addPoint((int)x[i], (int)y[i]);
		}
		this.c = c;
	}
	
	/**
	 * Updates the position of the polygon
	 * @param x The x-coordinates of the polygon
	 * @param y The y-coordinates of the polygon
	 */
	public void update(double[] x, double[] y){
		p.reset();
		for(int i = 0; i < x.length; i++){
			p.xpoints[i] = (int)x[i];
			p.ypoints[i] = (int)y[i];
			p.npoints = x.length;
		}
	}
	
	
	/**
	 * Tests whether the polygon should be drawn on the screen
	 * @return Boolean stating whether the polygon is within the bounds of the screen
	 */
	private boolean screenTest(){
		boolean onScreen = false;
		for(int i = 0; i < p.xpoints.length; i++){
			if(p.xpoints[i] >= 0 && p.xpoints[i] < global.Variables.getWidth() && p.ypoints[i] >= 0 && p.ypoints[i] < global.Variables.getHeight()){
				onScreen = true;
			}
		}
		return onScreen;
	}
	
	/**
	 * Draws the polygon onto the Screen
	 * @param g The Graphics object
	 */
	public void drawPoly(Graphics g){
		if(draw && screenTest()){
			g.setColor(new Color((int)(c.getRed() * light), (int)(c.getGreen() * light), (int)(c.getBlue() * light)));
			g.fillPolygon(p);
			g.setColor(Color.BLACK);
			g.drawPolygon(p);
		}
	}
}
