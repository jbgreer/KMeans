package net.grimjeer.kmeans;

/**
 * KMeansPoint2D - perform K Means cluster analysis on 2D real-valued data
 * 
 * @author jbgreer
 *
 */
public class KMeansPoint2D {
	
	private static final boolean DEBUG = false;
	private static int MAX_ITERATIONS = 100;
	
	/**
	 * createKMeansClusters - given K and an array of Point2Ds, attempt to find  K Clusters
	 * 
	 * @param k (int)	- maximum number of clusters
	 * @param points Point2D[]	- array of points from which to identify clusters
	 * @return int[] - array of integers of same size as points, with the cluster assignment for every point.
	 */
	public static int[] createKMeansClusters(final int k, final Point2D[] points) {
		return KMeansPoint2D.createKMeansClusters(k, MAX_ITERATIONS, points);
	}
	
	/**
	 * createKMeansClusters - @see createKMeansClusters above.  Allows user to specify maximum iterations.
	 * 
	 * @param k (int)	- maximum number of clusters
	 * @param maxIterations (int)	- maximum number of iterations for cluster identification
	 * @param points Point2D[]	- array of points from which to identify clusters
	 * @return int[] - array of integers of same size as points, with the cluster assignment for every point.
	 */
	public static int[] createKMeansClusters(final int k, int maxIterations, final Point2D[] points) {
		final int[] clusters = new int[points.length];
		final Point2D[] centroids = new Point2D[k];
		boolean unstable = true;

		// find bounds of data so that initial cluster values are in range of points
		Point2D min = findMinimum(points);
		if (DEBUG) System.out.println("min:" + min);
		Point2D max = findMaximum(points);
		if (DEBUG) System.out.println("max:" + max);

		// create random centroid for each cluster
		for (int c = 0; c < k; c++) {
			centroids[c] = createRandomPoint2D(min, max);
			if (DEBUG) System.out.println("random centroid " + c + ": " + centroids[c] );
		}

		// while assignments are still taking place and we haven't reached our iteration limit
		while (unstable && maxIterations > 0) {
			
			unstable = false; 
			
			// assign points to clusters
			for (int i = 0; i < points.length; i++) {
				final int c = findNearestCentroid(points[i], centroids);
				if (DEBUG) System.out.println("point " + i + ": " + points[i] + " nearest:" + c);
				if (clusters[i] != c) {
					unstable = true;
					if (DEBUG) System.out.println("unstable");
					clusters[i] = c;
				}
			}

			// find the new cluster centroids
			for (int c = 0; c < k; c++) {
				centroids[c] = findClusterCentroid(c, clusters, points);
				if (DEBUG) System.out.println("new centroid " + c + ": " + centroids[c] );
			}
			
			// one iteration done
			maxIterations -= 1;
		}

		return clusters;
	}

	/**
	 * createRandomPoint2D - given minimum and maximum X/Ys, create a random point in the described box
	 * @param min (Point2D)	- a point with the minimum X and Y 
	 * @param max (Point2D)	- a point with the maximum X and Y
	 * @return (Point2D) - a new Point with random X and Y coords bounded within the min and max points.
	 */
	private static Point2D createRandomPoint2D(final Point2D min, final Point2D max) {
		final double lat = Math.random() * (max.getX() - min.getX() ) + min.getX();
		final double lon = Math.random() * (max.getY() - min.getY() ) + min.getY();
		return new Point2D(lat, lon);
	}

	/**
	 * findClusterCentroid	 - given a cluster, the set of all cluster assignments and the set of all points
	 * 						   calculate the centroid of the cluster
	 * @param cluster (int)	- the numeric ID of a cluster
	 * @param clusters (int[])	- an array of all cluster assignments
	 * @param points (Point2D[])	- an array of all Point2Ds
	 * @return Point2D	- contains the coords of the cluster centroid
	 */
	private static Point2D findClusterCentroid(final int cluster, final int[] clusters, final Point2D[] points) {
		double sumX = 0;
		double sumY = 0;
		double count = 0;
		for (int i = 0; i < clusters.length; i++) {
			if (clusters[i] == cluster) {
				sumX += points[i].getX();
				sumY += points[i].getY();
				count += 1;
			}
		}
		return new Point2D(sumX/count, sumY/count);
	}

	/**
	 * findMaximum - given an array of Point2Ds, find the maximum X and Y and create a new point with those coords
	 * @param points (Point2D[])	- array of all Point2Ds
	 * @return Point2D	- new Point2D with maximum X and Y as values
	 */
	private static Point2D findMaximum(final Point2D[] points) {
		double maxX = 0;
		double maxY = 0;
		for (int i = 0; i < points.length; i++) {
			final double lat = points[i].getX();
			if (lat >= maxX) {
				maxX = lat;
			}
			final double lon = points[i].getY();
			if (lon >= maxY) {
				maxY = lon;
			}
		}
		return new Point2D(maxX, maxY);
	}

	/**
	 * findMinimum - given an array of Point2Ds, find the minimum X and Y and create a new point with those coords
	 * @param points (Point2D[])	- array of all Point2Ds
	 * @return Point2D	- new Point2D with miniimum X and Y as values
	 */
	private static Point2D findMinimum(final Point2D[] points) {
		double minX = 0;
		double minY = 0;
		for (int i = 0; i < points.length; i++) {
			final double lat = points[i].getX();
			if (lat <= minX) {
				minX = lat;
			}
			final double lon = points[i].getY();
			if (lon <= minY) {
				minY = lon;
			}
		}
		return new Point2D(minX, minY);
	}

	/**
	 * findNearestCentroid - given a Spatial Point, find the nearest centroid of the current cluster centroids
	 * @param p (Point2D)	- given point
	 * @param centroids (Point2D[])	- array of cluster centroids
	 * @return (int) - number of nearest cluster
	 */
	private static int findNearestCentroid(final Point2D p, final Point2D[] centroids) {
		
		if (DEBUG) System.out.println("findNearestCentroid point:" + p);
		for (int i = 0; i < centroids.length; i++) {
			if (DEBUG) System.out.println("centroid " + i + ": " + centroids[i] );
		}
		int nearest = 0;
		double value = Double.MAX_VALUE;
		for (int i = 0; i < centroids.length; i++) {
			if (DEBUG) System.out.println(i + ": " + p.findDistance(centroids[i] ) );
			final double d = p.findDistance(centroids[i] );
			if (d <= value) {
				nearest = i;
				value = d;
			}
		}
		if (DEBUG) System.out.println("nearest: " + nearest);
		return nearest;
	}
}
