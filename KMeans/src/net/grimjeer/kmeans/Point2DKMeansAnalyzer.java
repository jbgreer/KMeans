package net.grimjeer.kmeans;

import net.grimjeer.kmeans.Point2DReader;
import net.grimjeer.kmeans.Point2D;

/**
 * Point2DKMeansAnalyzer - drive class for reading and performing K Means clustering on 2D real data
 * 
 * @author jbgreer
 *
 */
public class Point2DKMeansAnalyzer {

	private static final boolean DEBUG = false;
	
	public static void main(String[] args) {
		// two command line args
		if (args.length != 2) {
			System.err.println("usage: program <k> <inputfile>");
			System.exit(1);
		}
		
		// first is K, maximum number of clusters
		final int k = Integer.parseInt(args[0] );
		
		// second is name of file with 2D points
		final Point2D[] sps = Point2DReader.readFromFile(args[1] );

		if (DEBUG) {
			for (Point2D sp : sps) {
				System.out.println(sp);
			}
		}
		
		// analyze for clusters
		int[] clusters = KMeansPoint2D.createKMeansClusters(k, sps);
		
		// print out points and their cluster assignment
		for (int i = 0; i < sps.length; i++) {
			System.out.println(sps[i].getX() + "," + sps[i].getY() + "," + clusters[i] );
		}
	}
}