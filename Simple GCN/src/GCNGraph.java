import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GCNGraph implements Serializable {
    private static final long serialVersionUID = 1L;

    public static class Node implements Serializable {
        private double[] features;
        private double[] label;            // one‑hot
        private List<Integer> neighbors;

        public Node(double[] features, double[] label) {
            if (label == null) 
                throw new IllegalArgumentException("Label cannot be null");
            this.features = features;
            this.label = label;
            this.neighbors = new ArrayList<>();
        }
        public double[] getFeatures() { return features; }
        public double[] getLabel()    { return label; }
        public void setLabel(double[] newLabel) { this.label = newLabel; }
        public List<Integer> getNeighbors() { return neighbors; }
        public void addNeighbor(int idx) {
            if (!neighbors.contains(idx)) neighbors.add(idx);
        }
    }

    private List<Node> nodes;
    private final int numClasses;

    public GCNGraph(int numClasses) {
        this.nodes = new ArrayList<>();
        this.numClasses = numClasses;
    }

    public void addNode(double[] features, double[] label) {
        if (label.length != numClasses) {
            throw new IllegalArgumentException(
                "Expected label length=" + numClasses + ", got " + label.length
            );
        }
        nodes.add(new Node(features, label));
    }

    public void addEdge(int i, int j) {
        if (i < 0 || j < 0 || i >= nodes.size() || j >= nodes.size()) {
            throw new IllegalArgumentException("Invalid edge indices");
        }
        nodes.get(i).addNeighbor(j);
        nodes.get(j).addNeighbor(i);
    }

    public void setNodeLabel(int nodeIndex, double[] newLabel) {
        if (nodeIndex < 0 || nodeIndex >= nodes.size()) {
            throw new IllegalArgumentException("Invalid node index");
        }
        if (newLabel.length != numClasses) {
            throw new IllegalArgumentException("Expected label length=" + numClasses);
        }
        nodes.get(nodeIndex).setLabel(newLabel);
    }

    public double[][] getGraphLevelLabel() {
        if (nodes.isEmpty()) {
            throw new IllegalStateException("Graph has no nodes");
        }
        return new double[][] { nodes.get(0).getLabel() };
    }

    public double[][] getFeatureMatrix() {
        int n = nodes.size();
        if (n == 0) return new double[0][0];
        int d = nodes.get(0).getFeatures().length;
        var F = new double[n][d];
        for (int i = 0; i < n; i++) {
            F[i] = nodes.get(i).getFeatures();
        }
        return F;
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public double[][] getLabelMatrix() {
        int n = nodes.size();
        var L = new double[n][numClasses];
        for (int i = 0; i < n; i++) {
            L[i] = nodes.get(i).getLabel();
        }
        return L;
    }

    public void addSelfLoop(int nodeIndex) {
        addEdge(nodeIndex, nodeIndex);
    }

    public double[][] getAdjacencyMatrix() {
        int n = nodes.size();
        var A = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int nb : nodes.get(i).getNeighbors()) {
                A[i][nb] = 1.0;
            }
            A[i][i] = 1.0;  // self‑loop
        }
        return GraphUtils.normalizeAdjacency(A);
    }

    public double[][] getNormalizedAdjacencyMatrix() {
        double[][] A = getAdjacencyMatrix();
        int n = A.length;
        double[] deg = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) sum += A[i][j];
            deg[i] = sum;
        }
        var M = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (A[i][j] != 0) {
                    M[i][j] = A[i][j] / Math.sqrt(deg[i]*deg[j]);
                }
            }
        }
        return M;
    }

    public void printGraph() {
        for (int i = 0; i < nodes.size(); i++) {
            var lab = nodes.get(i).getLabel();
            int cls = -1;
            for (int k = 0; k < lab.length; k++) {
                if (lab[k] == 1.0) { cls = k; break; }
            }
            System.out.println("Node " + i + " → class " + cls +
                " neighbors=" + nodes.get(i).getNeighbors());
        }
    }
}
