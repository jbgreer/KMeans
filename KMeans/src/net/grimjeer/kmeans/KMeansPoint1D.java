package net.grimjeer.kmeans;


/**
 * KMeansPoint1D - sample 1D K Means cluster analysis program
 * Creates fake data, performs analysis, shows results.
 * 
 * @author jbgreer
 *
 */
public class KMeansPoint1D {
	
	private static final boolean DEBUG = false;
	
	public KMeansPoint1D() {
		int k = 4;
		int n = 20;
		
		Point1D[] points = new Point1D[k*n];
		
		// create K sets of data with widely varying means
		for (int c = 0; c < k; c++) {
			for (int i = 0; i < n; i++) {
				double r = Math.random() * 100 + (c * 1000) ;
				points[c*n+i] = new Point1D(r);
				System.out.println(c + "," + i + ", " + points[c*n+i].getValue() );
			}
		}
		
		// analyze clusters and print results
		int[] clusters = createKMeansClusters(k, points);
		System.out.println("Clustered");
		for (int c = 0; c < k; c++) {
			for (int i = 0; i < points.length; i++) {
				if (clusters[i] == c) {
					System.out.println(c + ", " + i + ", " + points[i].getValue() );
				}
			}
		}
	}


	public int[] createKMeansClusters(final int k, final Point1D[] points) {
		int[] clusters = new int[points.length];
		Point1D[] centroids = new Point1D[k];
		boolean unstable = true;

		// find bounds of data so that initial cluster values are in range
		Point1D min = findMinimum(points);
		if (DEBUG) System.out.println("min:" + min);
		Point1D max = findMaximum(points);
		if (DEBUG) System.out.println("max:" + max);

		// create random centroid for each clusters
		for (int c = 0; c < k; c++) {
			centroids[c] = createRandom(min, max);
			if (DEBUG) System.out.println("random centroid " + c + ": " + centroids[c] );
		}

		while (unstable) {
			
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

			for (int c = 0; c < k; c++) {
				centroids[c] = findCentroid(c, clusters, points);
				if (DEBUG) System.out.println("new centroid " + c + ": " + centroids[c] );
			}
		}

		return clusters;
	}

	private Point1D createRandom(final Point1D min, final Point1D max) {
		final double d = Math.random() * (max.getValue() - min.getValue() ) + min.getValue();
		return new Point1D(d);
	}


	private Point1D findMaximum(final Point1D[] points) {
		double max = 0;
		for (int i = 0; i < points.length; i++) {
			double v = points[i].getValue();
			if (v >= max) {
				max = v;
			}
		}
		return new Point1D(max);
	}

	private Point1D findCentroid(int cluster, int[] clusters, Point1D[] points) {
		double sum = 0;
		double count = 0;
		for (int i = 0; i < clusters.length; i++) {
			if (clusters[i] == cluster) {
				sum += points[i].getValue();
				count += 1;
			}
		}
		return new Point1D(sum / count);
	}

	private Point1D findMinimum(final Point1D[] points) {
		double min = 0;
		for (int i = 0; i < points.length; i++) {
			double v = points[i].getValue();
			if (v <= min) {
				min = v;
			}
		}
		return new Point1D(min);
	}

	private int findNearestCentroid(Point1D p, Point1D[] centroids) {
		
		
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

	public static void main(String[] args) {
		new KMeansPoint1D();
	}
}
