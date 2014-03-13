package net.grimjeer.kmeans;

/**
 * Point1D - simple 1D real-valued object
 * 
 * @author jbgreer
 * 
 */
public class Point1D {

	private double value;
	
	public Point1D(final double value) { setValue(value); }
	
	public double getValue() { return value; }
	
	public void setValue(final double value) { this.value = value; }
	
	public double findDistance(Point1D p) {
		return Math.abs(p.getValue() - getValue() );
	}
	
	public String toString() { return "value:" + value; }
}
