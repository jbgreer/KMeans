package net.grimjeer.kmeans;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.grimjeer.kmeans.Point2D;

/**
 * Point2DReader - utility class for reading arrays of Point2D objects
 * 
 * @author jbgreer
 *
 */
public class Point2DReader {

	private static final boolean DEBUG = false;


	/**
	 * readFromFile - given a filename, open and read the contained Point2D
	 * 					assume one point per line, real valued, space or comma separated
	 * @param filename
	 * @return (Point2D[]) 	- an array of Point2Ds
	 */
	public static Point2D[] readFromFile(final String filename) {
		final List<Point2D> sps = new ArrayList<Point2D>();

		BufferedReader br = null;
		String line = null;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename) ) );

			// e.g. 01 2014-02-20 11:50:22 35.000 -89.02 10.2
			final Pattern p = Pattern.compile("^"	// ignore commented lines 
					+ "([-]?[0-9]*.[0-9]*)"		// x
					+ "[, ]"						// space or comma-separated
					+ "([-]?[0-9]*.[0-9]*)"			//  y 
					);

			Matcher m = null;

			// per line attempt to match pattern and if so insert into list
			while ( (line = br.readLine() ) != null) {
				if (DEBUG) System.out.println("input line >" + line + "<");
				m = p.matcher(line);
				if (m.find() ) {
					if (DEBUG) System.out.println("matched: " + m);

					final double x = Double.parseDouble(m.group(1) );
					if (DEBUG) System.out.println("x: " + x);
					final double y = Double.parseDouble(m.group(2) );
					if (DEBUG) System.out.println("y: " + y);

					final Point2D sp = new Point2D(x, y);
					sps.add(sp);
				} else {
					System.err.println("input not matched: " + line);	
				}
			}

		} catch (Exception e) {
			System.err.println("Exception e " + e.getMessage() );
		} finally {
			if (br != null) {
				try { 
					br.close(); 
				} catch(Throwable t) {
				} 
			}
		}
		
		// convert list to array and return
		Point2D[] spa = new Point2D[sps.size() ];
		return sps.toArray(spa);
	}
}
