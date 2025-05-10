/**
 * 
 */
package waste.men.datastructures.util.abstractbase;

import waste.men.datastructures.api.Iterator;

import java.io.Serializable;

import waste.men.datastructures.api.IEdge;
import waste.men.datastructures.api.IGraph;
import waste.men.datastructures.api.IVertex;

/**
 * Some of the methods called, will be realized by the various implementations of this 
 * abstract base class
 */
public abstract class AbstractBaseGraph<V, E> implements IGraph<V, E>, Serializable {
	
	/**
	 * Default Serialiable ID
	 */
	private static final long serialVersionUID = 1L;
	protected final boolean isDirected;
	private final boolean selfLoppAllowed;
	
	protected AbstractBaseGraph(boolean isDirected, boolean selfLoopAllowed) {
		this.isDirected = isDirected;
		this.selfLoppAllowed = selfLoopAllowed;
	}
	
	//Protected Utitlity methods
	
	/**
	 * Checks if the given vertex is valid
	 * @param vertex
	 * @throws IllegalArgumentException
	 */
	protected void validateVertex(IVertex<V> vertex) throws IllegalArgumentException{
		if(this.selfLoppAllowed) {
			return;
		}
		if(!containsVertex(vertex)) {
			throw new IllegalArgumentException("Vertex " + vertex + " not found!");
		}
	}
	
	/**
	 * Checks if the given edge is valid
	 * @param edge
	 * @throws IllegalArgumentException
	 */
	protected void validateEdge(IEdge<E> edge) throws IllegalArgumentException {
		/*if(!containsEdge(edge)) {
			throw new IllegalArgumentException("Edge " + edge + "not found!");
		}*/
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public boolean isDirected() {
		return isDirected;
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public boolean containsVertex(IVertex<V> vertex) {
		Iterator<IVertex<V>> itr = vertices();/**vertices is to be implemented by the user of this base class*/
		
		//Traverses the whole collection of vertices to see if the given exists or not
		while(itr.hasNext()) {
			if(itr.next().equals(vertex)) {//if the current vertex is the same as the given vertex
				return true;
			}
		}
		return false;
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public boolean containsEdge(IEdge<E> edge) {
		
		Iterator<IEdge<E>> itr = edges();
		while(itr.hasNext()) {
			if(itr.next().equals(edge)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public int degree(IVertex<V> vertex) throws IllegalArgumentException {
		validateVertex(vertex);
		if(isDirected) {
			throw new UnsupportedOperationException("For directed graph, use inDegree() + OutDegree()");
		}
		
		int count = 0;
		Iterator<IEdge<E>> itr = incidentEdges(vertex);//returns an iterator of all the edges that connect the given vertex
		while(itr.hasNext()) {//counting how many edges the given vertex has
			itr.next();
			count++;
		}
		return count;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public IEdge<E> getEdge(IVertex<V> v, IVertex<V> u) throws IllegalArgumentException {
		
		Iterator<IEdge<E>> itr = incidentEdges(v);//returns iterator of edges connecting v
		while(itr.hasNext()) {
			IEdge<E> edge = itr.next();//get current edge
			IVertex<V>[] endpoints = endVertices(edge);//returns an array of vertices connected to the given edge
			
			if(endpoints[0].equals(u) || endpoints[1].equals(u)) {//checks if the current edge connects the two given vertices
				return edge;//returns the edge
			}
		}
		return null;//returns null if there is no edge connecting the vertices
	}

	@Override
	public IVertex<?> opposite(IVertex<V> v, IEdge<E> edge){
		validateVertex(v);
		validateEdge(edge);
		return (IVertex<?>) edge.getOpposite(v);
	}
}
