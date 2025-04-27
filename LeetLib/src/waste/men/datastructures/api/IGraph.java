/**
 * 
 */
package waste.men.datastructures.api;

/**
 * A Graph ADT which will contain Vertices and Edges
 * Where a Vertex is can be any object and an Edge is
 * the connection/relationship between two vertices
 */
public interface IGraph<V, E> {

	/**
	 * 
	 * @return integer representing the number of vertices in the graph
	 */
	public int numVertices();
	
	/**
	 * 
	 * @return integer representing the number of edges in the graph
	 */
	public int numEdges();
	
	/**
	 * Will check if the graph to be/being used is directed or undirected
	 * @return true if directed, else false
	 */
	public boolean isDirected();
	
	/**
	 * Shows a collection of all the vertices in the graph
	 * @return an iterator of vertices in the graph
	 */
	public Iterator<IVertex<V>> vertices();
	
	/**
	 * Checks for the existence of the given vertex within the graph
	 * @param vertex
	 * @return true if vertex exists, else false
	 */
	public boolean containsVertex(IVertex<V> vertex);
	
	/**
	 * Adds an element as a new vertex into the graph
	 * @param element
	 * @return the added vertex
	 * @throws IllegalArgumentException
	 */
	public IVertex<V> insertVertex(V element) throws IllegalArgumentException;
	
	/**
	 * Removes the given vertex from the graph
	 * @param vertex
	 * @throws IllegalArgumentException
	 */
	public void removeVertex(IVertex<V> vertex)  throws IllegalArgumentException;
	
	/**
	 * Shows a collection of all the edges in the graph
	 * @return an iterator of the edges
	 */
	public Iterator<IEdge<E>> edges();
	
	/**
	 * Checks for the existence of an edge within the graph
	 * @param edge
	 * @return true if edge exists, else false
	 */
	public boolean containsEdge(IEdge<E> edge);
	
	/**
	 * Retrieves an edge of the given vertices. The edge that connects the two
	 * @param u
	 * @param v
	 * @return the edge that connects the given vertices
	 * @throws IllegalArgumentException
	 */
	public IEdge<E> getEdge(IVertex<V> v, IVertex<V> u) throws IllegalArgumentException;
	
	/**
	 * Checks and returns the vertices at the end of the given edge.
	 * The first vertex is the source and the second vertex is the destination, if the graph is directed.
	 * if the graph is undirected then the first and second vertices don't matter which is which
	 * @param edge
	 * @return an array of size 2, containing vertices
	 * @throws IllegalArgumentException
	 */
	public IVertex<V>[] endVertices(IEdge<E> edge) throws IllegalArgumentException;
	
	/**
	 * Adds a new edge which connects the two given vertices together. This edge
	 * will contain an element
	 * @param v
	 * @param u
	 * @param element
	 * @return
	 * @throws IllegalArgumentException
	 */
	public IEdge<E> insertEdge(IVertex<V> v, IVertex<V> u, E element) throws IllegalArgumentException;
	
	/**
	 * Removes the given edge from the graph
	 * @param edge
	 * @throws IllegalArgumentException
	 */
	public void removeEdge(IEdge<E> edge) throws IllegalArgumentException;
	
	/**
	 * Shows how many edges the given vertex has
	 * @param vertex
	 * @return the number of edges
	 * @throws IllegalArgumentException
	 */
	public int degree(IVertex<V> vertex) throws IllegalArgumentException;
	
	/**
	 * Shows a collection of the neighbors of the given vertex
	 * The neighbors are the vertices that are connected to the given vertex
	 * by the edges that connect it
	 * @param v
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Iterator<IVertex<V>> neighbors(IVertex<V> vertex) throws IllegalArgumentException;
	
	/**
	 * Shows the collection of the edges that connects with V
	 * @param vertex
	 * @return iterator of incidentEdges
	 * @throws IllegalArgumentException
	 */
	public Iterator<IEdge<E>> incidentEdges(IVertex<V> vertex) throws IllegalArgumentException;
	
	/**
	 * Gets a collection of all the outgoing edges of the given vertex
	 * @param v
	 * @return iterator of outgoing edges of v
	 * @throws IllegalArgumentException
	 */
	public Iterator<IEdge<E>> outGoingEdges(IVertex<V> v) throws IllegalArgumentException;
	
	/**
	 * Gets a collection of all the incoming edges of the given vertex u
	 * @param u
	 * @return iterator of incoming edges of u
	 * @throws IllegalArgumentException
	 */
	public Iterator<IEdge<E>> incomingEdges(IVertex<V> u) throws IllegalArgumentException;
	
	/**
	 * Gets the vertex that is opposite and connected (by the given edge) with the given vertex v.
	 * Type safety will be enforced in the class that will implement the graphs
	 * @param v
	 * @param edge
	 * @return connected vertex
	 * @throws IllegalArgumentException
	 */
	public IVertex<?> opposite(IVertex<V> v, IEdge<E> edge) throws IllegalArgumentException;
	
	/**
	 * Checks and returns the number of outgoing edges connected to the given vertex v.
	 * If graph is undirected then outDegree == inDegree
	 * @param v
	 * @return
	 * @throws IllegalArgumentException
	 */
	public int outDegree(IVertex<V> v) throws IllegalArgumentException;
	
	/**
	 * Checks and returns the number of incoming edges connected to the given vertex u.
	 * If graph is undirected then outDegree == inDegree
	 * @param u
	 * @return
	 * @throws IllegalArgumentException
	 */
	public int inDegree(IVertex<V> u) throws IllegalArgumentException;
	
}
