/**
 * 
 */
package waste.men.datastructures.impl.hierarchical;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
    private Map<IVertex<V>, Integer> vertexLabels;
    private Integer graphLabel = null;
    private int numClasses;

	protected AdjacencyMatrixGraph(boolean isDirected, int numClasses) {
		super(isDirected);
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

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IVertex<V>> vertices() {
		// TODO Auto-generated method stub
		return (Iterator<IVertex<V>>) vertexList.iterator();
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

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IEdge<E>> edges() {
		// TODO Auto-generated method stub
		return (Iterator<IEdge<E>>)edges.iterator();
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

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IVertex<V>> neighbors(IVertex<V> vertex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateVertex(vertex);
		
		int index = vertexIndexMap.get(vertex);//retrieve the index of the vertex
		
		List<IVertex<V>> neighbors = new ArrayList<>();
		
		//Iterates through the set of edges that are associated with vertex
		for(Integer j : adjacencyMatrix.get(index).keySet()) {
			neighbors.add(vertexList.get(j));
		}
		
		return (Iterator<IVertex<V>>)neighbors.iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IEdge<E>> incidentEdges(IVertex<V> vertex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateVertex(vertex);
		
		int index = vertexIndexMap.get(vertex);//get the index for the edge connecting 
	
		return (Iterator<IEdge<E>>)adjacencyMatrix.get(index).values().iterator();
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

}
