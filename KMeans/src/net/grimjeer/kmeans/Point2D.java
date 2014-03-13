package net.grimjeer.kmeans;

/**
 * Point2D - simple object for 2D coordinates
 * 
 * @author jbgreer
 * 
 */
public class Point2D {

	private double x;
	private double y;

	public Point2D(final double x, final double y) { 
		setX(x);
		setY(y);
	}

	public double getX() { return x; }

	public void setX(final double x) { this.x = x; }

	public double getY() { return y; }

	public void setY(final double longitude) { this.y = longitude; }

	public double findDistance(Point2D sp) {
		return Math.sqrt( Math.pow( sp.getX() - getX(), 2)
				+ Math.pow(sp.getY() - getY(), 2) );
	}

	public String toString() { return "x:" + getX() + ", y:" + getY(); }
}
