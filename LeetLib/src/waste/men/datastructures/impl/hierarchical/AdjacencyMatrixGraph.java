/**
 * 
 */
package waste.men.datastructures.impl.hierarchical;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import waste.men.datastructures.api.IEdge;
import waste.men.datastructures.api.IGraph;
import waste.men.datastructures.api.IVertex;
import waste.men.datastructures.api.Iterator;
import waste.men.datastructures.impl.linear.DLinkedList;
import waste.men.datastructures.util.abstractbase.AbstractBaseGraph;

/**
 * 
 */
public class AdjacencyMatrixGraph <V, E> extends AbstractBaseGraph<V, E> 
										implements IGraph<V, E>, Serializable {

	/**
	 * Defualt Serialiazable ID
	 */
	private static final long serialVersionUID = 1L;
	
	private class Vertex implements IVertex<V>{
		
		private V element;
		
		Vertex(V element){
			this.element = element;
		}
		
		@Override
		public V getElement() {
			// TODO Auto-generated method stub
			return this.element;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(element);
		}
		
	}
	
	private class Edge implements IEdge<E>{
		
		private E element;
		private IVertex<V> u, v;
		private IVertex<V> [] endPoints;
		
		Edge(IVertex<V> u, IVertex<V> v, E element){
			this.u = u;
			this.v = v;
			this.element = element;
		}
		
		@Override
		public E getElement() {
			// TODO Auto-generated method stub
			return this.element;
		}

		@Override
		public IVertex<?> getOpposite(IVertex<?> v) {
			// TODO Auto-generated method stub
			if(v.equals(this.v)) {
				return this.v;
			}
			if(v.equals(this.u)) {
				return this.u;
			}
			throw new IllegalArgumentException("Given vertex does not belong to this edge");
		}
		
		@SuppressWarnings("unchecked")
		public IVertex<V>[] getEndpoints(){
			return (IVertex<V>[])new IVertex[] {this.u, this.v};
		}
		@Override
		public int hashCode() {
			return Objects.hash(u,v,element);
		}
		
	}
	
	private List<IVertex<V>> vertexList;
    private Map<IVertex<V>, Integer> vertexIndexMap;
    private List<Map<Integer, IEdge<E>>> adjacencyMatrix;
    private Set<IEdge<E>> edges;
    private Map<IVertex<V>, double[]> vertexFeatures;
    private Map<IVertex<V>, double[]> vertexLabels;
    private double[] graphLabel = null;
    private int numClasses;

	public AdjacencyMatrixGraph(boolean isDirected, int numClasses) {
		super(isDirected, true);
		// TODO Auto-generated constructor stub
		this.vertexList = new ArrayList<>();
		this.vertexIndexMap = new ConcurrentHashMap<>();
		this.adjacencyMatrix = new ArrayList<>();
		this.edges = new HashSet<>();
		this.vertexFeatures = new ConcurrentHashMap<>();
		this.vertexLabels = new ConcurrentHashMap<>();
	}

	@Override
	public int numVertices() {
		// TODO Auto-generated method stub
		return this.vertexList.size();
	}

	@Override
	public int numEdges() {
		// TODO Auto-generated method stub
		return this.edges.size();
	}

	@Override
	public Iterator<IVertex<V>> vertices() {
		// TODO Auto-generated method stub
		DLinkedList<IVertex<V>> lst = new DLinkedList<>();
		for(int i = 0; i < vertexList.size(); i++) {
			lst.addLast(vertexList.get(i));
		}
		return (Iterator<IVertex<V>>) lst.iterator();
	}

	@Override
	public IVertex<V> insertVertex(V element) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Vertex vert = new Vertex(element);
		if(vertexIndexMap.containsKey(vert)) {
			throw new IllegalArgumentException("Vertex already exists");
		}
		vertexList.add(vert);
		vertexIndexMap.put(vert, vertexList.size() - 1);
		adjacencyMatrix.add(new HashMap<>());
		return (IVertex<V>) vert;
	}

	/*
	 * Rebuilds the indices of the VertexMap after an update operation
	 * was performed on the graph.
	 * Clears the Map containing the indices and repopulates
	 */
	private void rebuildIndex() {
		vertexIndexMap.clear();
		for(int i = 0; i < vertexList.size(); i++) {
			vertexIndexMap.put(vertexList.get(i), i);
		}
	}
	@Override
	public void removeVertex(IVertex<V> vertex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateVertex(vertex);
		int index = vertexIndexMap.remove(vertex);
		vertexList.remove(index);
		adjacencyMatrix.remove(index);
		
		for(Map<Integer, IEdge<E>> row : adjacencyMatrix ) {
			row.remove(index);
		}
		
		//removing the edges that contain vertex. The array is first converted to a list
		edges.removeIf(e -> Arrays.asList(endVertices(e)).contains(vertex));
		
		vertexFeatures.remove(vertex);
		vertexLabels.remove(vertex);
		rebuildIndex();//ensures that the vertices are at their correct index.
	}

	@Override
	public Iterator<IEdge<E>> edges() {
		// TODO Auto-generated method stub
		
		DLinkedList<IEdge<E>> lst = new DLinkedList<>();
		for(int i = 0; i < edges.size(); i++) {
			lst.addLast(edges.iterator().next());
		}
		return (Iterator<IEdge<E>>)lst.iterator();
	}

	@Override
	public IVertex<V>[] endVertices(IEdge<E> edge) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateEdge(edge);
		Edge e = (Edge)edge;
		return e.getEndpoints();
	}

	@Override
	public IEdge<E> insertEdge(IVertex<V> v, IVertex<V> u, E element) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateVertex(v);
		validateVertex(u);
		
		if(getEdge(v, u) != null) {
			throw new IllegalArgumentException("Edge already exists");
		}
		Edge edge = new Edge(v, u, element);
		
		//get the indices of the vertices
		int i = vertexIndexMap.get(v);
		int j = vertexIndexMap.get(u);
		
		//put the edge and u's index in the adjacency matrix
		adjacencyMatrix.get(i).put(j, edge);
		if(!isDirected){
			//put the edge and v's index in the adjacency matrix
			adjacencyMatrix.get(j).put(i, edge);
		}
		edges.add(edge);
		return edge;
	}

	@Override
	public void removeEdge(IEdge<E> edge) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateEdge(edge);
		IVertex<V>[] endpoints = endVertices(edge);
		
		//retrieve the indices of the endpoints of this edge
		int i = vertexIndexMap.get(endpoints[0]);
		int j = vertexIndexMap.get(endpoints[1]);
		
		adjacencyMatrix.get(i).remove(j);//remove the vertex u from the edge
		if(!isDirected) {
			adjacencyMatrix.get(j).remove(i);//remove the vertex v from the edge
		}
		edges.remove(edge);
	}

	@Override
	public Iterator<IVertex<V>> neighbors(IVertex<V> vertex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateVertex(vertex);
		
		int index = vertexIndexMap.get(vertex);//retrieve the index of the vertex
		
		DLinkedList<IVertex<V>> neighbors = new DLinkedList<>();
		
		//Iterates through the set of edges that are associated with vertex
		for(Integer j : adjacencyMatrix.get(index).keySet()) {
			neighbors.addLast(vertexList.get(j));
		}
		
		return (Iterator<IVertex<V>>)neighbors.iterator();
	}

	@Override
	public Iterator<IEdge<E>> incidentEdges(IVertex<V> vertex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateVertex(vertex);
		
		int index = vertexIndexMap.get(vertex);//get the index for the edge connecting 
		DLinkedList<IEdge<E>> lst = new DLinkedList<>();
		Collection<IEdge<E>> cl = adjacencyMatrix.get(index).values();
		
		for(int i = 0; i < cl.size(); i++) {
			lst.addLast(cl.iterator().next());
		}
		return (Iterator<IEdge<E>>)lst.iterator();
	}

	@Override
	public Iterator<IEdge<E>> outGoingEdges(IVertex<V> v) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return incidentEdges(v);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IEdge<E>> incomingEdges(IVertex<V> u) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateVertex(u);
		
		int j = vertexIndexMap.get(u);//get the edge connecting with vertex u
		
		List<IEdge<E>> incoming = new ArrayList<>();
		for(int i = 0; i < adjacencyMatrix.size(); i++) {
			
			//check if the vertex u is in the edge
			if(adjacencyMatrix.get(i).containsKey(j)) {
				incoming.add(adjacencyMatrix.get(i).get(j));//adds the edge that is connecting towards vertex u
			}
		}
		return (Iterator<IEdge<E>>) incoming.iterator();
	}

	@Override
	public int outDegree(IVertex<V> v) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateVertex(v);
		return adjacencyMatrix.get(vertexIndexMap.get(v)).size();//get number of 
	}

	@Override
	public int inDegree(IVertex<V> u) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateVertex(u);
		
		int count = 0;
		int j = vertexIndexMap.get(u);
		
		for(int i = 0; i < adjacencyMatrix.size(); i++) {
			if(adjacencyMatrix.get(i).containsKey(j)) {
				count++;
			}
		}
		return count;
	}
	
	public void setVertexFeature(IVertex<V> vertex, double[] features) {
		validateVertex(vertex);
		vertexFeatures.put(vertex, features);
	}
	
	public double[] getVertexFeature(IVertex<V> vertex) {
		validateVertex(vertex);
		return vertexFeatures.get(vertex);
	}
	
	public void setVertexLabel(IVertex<V> vertex, double[] label) {
		validateVertex(vertex);
		vertexLabels.put(vertex, label);
	}
	
	public double[] getVertexLabel(IVertex<V> vertex) {
		validateVertex(vertex);
		return vertexLabels.get(vertex);
	}
	
	public void setGraphLLabel(double[] label) {
		this.graphLabel = label;
	}
	
	public double[] getGraphLabel() {
		return graphLabel;
	}
	
	/*
	 * Creates a matrix of the vertex features and returns that matrix
	 */
	public double[][] getFeatureMatrix(){
		
		double[][] matrix = new double[vertexList.size()][];
		
		//Gets the features in the vertex map and if no feature found, a default row is porpulated to the matrix
		for(int i = 0; i < vertexList.size(); i++) {
			matrix[i] = vertexFeatures.getOrDefault(vertexList.get(i), new double[0]);
		}
		return matrix;
	}
	
	public double[][] getVertexLabelMatrix() {
		
		double[][] matrix = new double[vertexList.size()][];
		
		//Gets the labels in the vertex map and if no feature found, a default row is porpulated to the matrix
		for(int i = 0; i < vertexList.size(); i++) {
			matrix[i] = vertexLabels.getOrDefault(vertexList.get(i), new double[0]);
		}
		return matrix;
	}
	
	public double[][] getGraphLabelMatrix() {
		return new double[][] {getGraphLabel()};
	}
	
	/*
	 * The following lines of code are utilities for this matrix class
	 */
	
	//adding self loops to all nodes will help with normalizing the GCN
	public void addSelfLoops(E val) {
		for(IVertex<V> vert : vertexList) {
			if(getEdge(vert, vert) == null) {
				insertEdge(vert, vert, val);
				System.out.println("Adding Self loops");
			}
		}
	}
	
	/*
	 * Creates a normalized adjacency matrix and returns it
	 */
	public double[][] getNormalizedAdjacencyMatrix(){
		
		int n = vertexList.size();
		double[][] normAdj = new double[n][n];
		addSelfLoops(null);
		double[] degree = new double[n];
		
		System.out.println("Adjacency matrix with self-loops:");
		for (int i = 0; i < n; i++) {
		    System.out.println("Vertex " + i + ": " + adjacencyMatrix.get(i).keySet());
		}
		
		for(int i = 0; i < n; i++) {
			degree[i] = adjacencyMatrix.get(i).size();
		}
		
		for(int i = 0; i < n; i++) {
			for(Map.Entry<Integer, IEdge<E>> entry : adjacencyMatrix.get(i).entrySet()) {
				int j = entry.getKey();
			}
		}
		return normAdj;
	}
	
}
