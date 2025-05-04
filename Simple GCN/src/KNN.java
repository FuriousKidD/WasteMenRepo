import java.util.Comparator;
import java.util.PriorityQueue;

public class KNN {

	public static void makeCombined16NNConnections(GCNGraph graph, double[][] features) {
	    int n = features.length;
	    for (int i = 0; i < n; i++) {
	        // Create an array to store (distance, index) pairs
	        PriorityQueue<int[]> nearest = new PriorityQueue<>(16, (a, b) -> Double.compare(b[0], a[0]));

	        for (int j = 0; j < n; j++) {
	            if (i == j) continue;
	            double spatial = Math.hypot(features[i][6] - features[j][6], features[i][7] - features[j][7]);
	            double colorDist = 0;
	            for (int d = 0; d < 6; d++) {
	                double diff = features[i][d] - features[j][d];
	                colorDist += diff * diff;
	            }
	            colorDist = Math.sqrt(colorDist);
	            double combined = spatial + colorDist;

	            if (nearest.size() < 16) {
	                nearest.offer(new int[]{(int)(combined * 1e6), j}); // store int to simplify PQ
	            } else if (combined < nearest.peek()[0] / 1e6) {
	                nearest.poll();
	                nearest.offer(new int[]{(int)(combined * 1e6), j});
	            }
	        }

	        // Add edges to the 16 nearest neighbors
	        for (int[] entry : nearest) {
	            graph.addEdge(i, entry[1]);
	        }
	    }
	}

	    public static class Pair<A extends Comparable<A>, B extends Comparable<B>> implements Comparable<Pair<A, B>> {
	        private A first;
	        private B second;

	        public Pair(A first, B second) {
	            this.first = first;
	            this.second = second;
	        }

	        public A getFirst() {
	            return first;
	        }

	        public B getSecond() {
	            return second;
	        }

	        @Override
	        public int compareTo(Pair<A, B> o) {
	            return this.second.compareTo(o.second);
	        }
	    }
    }

     class Pair<A extends Comparable<A>, B extends Comparable<B>> implements Comparable<Pair<A, B>> {
        private A first;
        private B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }
        public A getFirst() { return first; }
        public B getSecond() { return second; }
        @Override
        public int compareTo(Pair<A, B> o) {
            return this.second.compareTo(o.second);
        }
}


