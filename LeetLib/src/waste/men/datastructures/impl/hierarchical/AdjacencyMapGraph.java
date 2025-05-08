/**
 * 
 */
package waste.men.datastructures.impl.hierarchical;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import waste.men.datastructures.api.IEdge;
import waste.men.datastructures.api.IGraph;
import waste.men.datastructures.api.IVertex;
import waste.men.datastructures.api.Position;
import waste.men.datastructures.impl.linear.DLinkedList;
import waste.men.datastructures.util.abstractbase.AbstractBaseGraph;
import waste.men.datastructures.api.Iterator;

/**
 * 
 */
public class AdjacencyMapGraph<V, E> extends AbstractBaseGraph<V, E> 
									implements IGraph<V, E>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Vertex/Node class which will contain objects of type V
	 *
	 */
	private class Vertex implements IVertex<V>{
		
		private V element;
		private Position<IVertex<V>> p;
		
		/*
		 * The outgoing map, contains all the edges that are connected to the vertex and are 
		 * leaving such vertex (->)
		 * The incoming map, contains all the edges that are going into the vertex (<-)
		 */
		private Map<IVertex<V>, IEdge<E>> outgoing, incoming;
		
		public Vertex(V element) {
			this.element = element;
			
			outgoing = new ConcurrentHashMap<IVertex<V>,IEdge<E>>();
			
			if(isDirected) {//if the graph is directed then incoming != outgoing
				incoming = new ConcurrentHashMap<IVertex<V>,IEdge<E>>();
			}
			else {
				incoming = outgoing;
			}
		}
		@Override
		public V getElement() {
			// TODO Auto-generated method stub
			return element;
		}
		
		/**
		 * Sets the position of the vertex in the graph
		 * @param p
		 */
		public void setPosition(Position<IVertex<V>> p) {
			this.p = p;
		}
		
		public Position<IVertex<V>> getPosition(){
			return p;
		}
		
		public Map<IVertex<V>, IEdge<E>> getOutgoing(){
			return outgoing;
		}
		
		public Map<IVertex<V>, IEdge<E>> getIncoming(){
			return incoming;
		}
		
	}//end of inner vertex class
	
	/**
	 * Edge class which will contain the relationship between vertices and also their weights 
	 */
	private class Edge implements IEdge<E>{
		
		private E element;
		private Position<IEdge<E>> p;
		private IVertex<V>[] endpoints;
		
		//private IVertex<V> v,u;//end points
		
		@SuppressWarnings("unchecked")
		public Edge(IVertex<V> v, IVertex<V> u, E element) {
			this.element = element;
			endpoints = new IVertex[] {v,u};//an array of length 2.
		}
		
		/**
		 * {@inheritDoc}
		 * @return
		 */
		public IVertex<V>[] getEndPoints(){
			return this.endpoints;
		}
		
		/**
		 * Sets the position of the Edge in the graph
		 * @param p
		 */
		public void setPosition(Position<IEdge<E>> p) {
			this.p = p;
		}
		
		public Position<IEdge<E>> getPosition(){
			return this.p;
		}
		
		@Override
		public E getElement() {
			// TODO Auto-generated method stub
			return this.element;
		}

		@Override
		public IVertex<?> getOpposite(IVertex<?> v) {
			// TODO Auto-generated method stub
			if(((Vertex) endpoints[0]).equals(v)) {
				return (IVertex<?>) endpoints[1];
			}
			else if(( (Vertex) endpoints[1]).equals(v)) {
				return (IVertex<?>) endpoints[0];
			}
			return null;
		}
		
	}//end of inner edge class
	
	private boolean isDirected;
	private DLinkedList<IVertex<V>> vertices = new DLinkedList<>();//contains the vertices of the graph
	private DLinkedList<IEdge<E>> edges = new DLinkedList<>();//cintains the edges of the graph
	
	public AdjacencyMapGraph(boolean isDirected) {
		super(isDirected);
		this.isDirected = isDirected;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public int numVertices() {return vertices.size();}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public int numEdges() {return edges.size();}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Iterator<IVertex<V>> vertices() {return vertices.iterator();}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public boolean containsVertex(IVertex<V> vertex) {return super.containsVertex(vertex);}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public IVertex<V> insertVertex(V element) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Vertex newVertex = new Vertex(element);
		newVertex.setPosition(vertices.addLast(newVertex));
		return vertices.last().getElement();//returns the element of the inserted vertex
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void removeVertex(IVertex<V> vertex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateVertex(vertex);
		Vertex vert = (Vertex) vertex;
		Iterator<Edge> itrIncoming = (Iterator<Edge>)vert.getIncoming().values().iterator();
		Iterator<Edge> itrOutgoing = (Iterator<Edge>)vert.getOutgoing().values().iterator();
		
		//removes all the incident edges of vertex from the graph
		while(itrIncoming.hasNext()) {
			removeEdge(itrIncoming.next());
		}
		while(itrOutgoing.hasNext()) {
			removeEdge(itrOutgoing.next());
		}
		vertices.remove(vert.getPosition());//removes the vertex in the linked list
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Iterator<IEdge<E>> edges() {
		// TODO Auto-generated method stub
		return (Iterator<IEdge<E>>) edges.iterator();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public boolean containsEdge(IEdge<E> edge) {
		// TODO Auto-generated method stub
		return super.containsEdge(edge);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public IEdge<E> getEdge(IVertex<V> v, IVertex<V> u) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return super.getEdge(v, u);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public IVertex<V>[] endVertices(IEdge<E> edge) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Edge e = (Edge) edge; 
		return (IVertex<V>[]) e.getEndPoints();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public IEdge<E> insertEdge(IVertex<V> v, IVertex<V> u, E element) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		//Check if the edge already exists, if not then throw exception
		if(getEdge(v, u) != null) {
			throw new IllegalArgumentException("Edge from u to v already exists");
		}
		
		validateVertex(v);
		validateVertex(u);
		Vertex origin = (Vertex)v;
		Vertex destination = (Vertex)u;
		Edge newEdge = new Edge( origin, destination, element);
		newEdge.setPosition(edges.addLast(newEdge));
		origin.getOutgoing().put(origin, newEdge);//adds the outgoing edge for this particular vertex to the map
		destination.getIncoming().put(destination, newEdge);//adds the incoming edge for this particular vertex to the map
		return (IEdge<E>) newEdge;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void removeEdge(IEdge<E> edge) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		if(!super.containsEdge(edge)) {
			throw new IllegalArgumentException("Cannot remove none existing edge");
		}
		
		IVertex<V>[] endPoints = endVertices((Edge)edge);
		Vertex origin = (Vertex)endPoints[0];
		Vertex dest = (Vertex)endPoints[1];
		edges.remove(((Edge)edge).getPosition());//remove the edge from the list
		
		//The following checks if the incoming and outgoing vertices still have the incident edges and cleans them up
		if(origin.getOutgoing().containsValue(edge)) {
			origin.getOutgoing().remove(origin);
		}
		if(dest.getIncoming().containsValue(edge)) {
			dest.getIncoming().remove(dest);
		}
		
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public int degree(IVertex<V> vertex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return super.degree(vertex);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Iterator<IVertex<V>> neighbors(IVertex<V> vertex) throws IllegalArgumentException {

		DLinkedList<IVertex<V>> neighborVertices = new DLinkedList<>();
		Iterator<IEdge<E>> itrInciEdges = incidentEdges(vertex);
	
		while(itrInciEdges.hasNext()){			
			Edge nxt = (Edge)itrInciEdges.next();
			Vertex source = (Vertex) nxt.getEndPoints()[0];
			Vertex destination = (Vertex) nxt.getEndPoints()[1];
			
			/* Since edges are symmetrical in graphs, only one of two
			 * vertices is needed. For the case when the graph is directed,
			 * only the outgoing edges matter
			 */
			if(source.equals(vertex)) {
				neighborVertices.addLast(destination);
			}//end if 
			else {
				neighborVertices.addLast(source);
			}//end else
			
		}//end while
		
		return neighborVertices.iterator();
		
		/*
		 * works well for only undirected graph
		while(itrInciEdges.hasNext()) {
			Edge nextEdge = (Edge)itrInciEdges.next();
			neighborVertices.addLast((IVertex<V>) nextEdge.getOpposite(vertex));//adds the vertex that is opposite the given vertex in the same edge
		}
		return neighborVertices.iterator();
		*/
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Iterator<IEdge<E>> incidentEdges(IVertex<V> vertex) throws IllegalArgumentException {
		validateVertex(vertex);
		Vertex vert = (Vertex)vertex;
		DLinkedList<IEdge<E>> inci = new DLinkedList<>();
		
		//Adds all the outgoing edges of the given vertex to the inci list
		Iterator<IEdge<E>> outItr = outGoingEdges(vert);
		while(outItr.hasNext()) {
			inci.addLast(outItr.next());
		}
		
		//in case graph is undirected, add the incoming edges in case they are already not there
		if(vert.getOutgoing() != vert.getIncoming()) {
			//Adds all the outgoing edges of the given vertex to the inci list
			for(IEdge<E> e :vert.getOutgoing().values() ) {
				if(!inci.contains(e)) {//checks if 
					inci.addLast(e);					
				}//end if
			}//end for	
		}
		return inci.iterator();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Iterator<IEdge<E>> outGoingEdges(IVertex<V> v) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		validateVertex(v);
		Vertex vert = (Vertex)v;
		DLinkedList<IEdge<E>> outs = new DLinkedList<>();
		for(IEdge<E> e : vert.getOutgoing().values()) {
			outs.addLast(e);
		}
		return outs.iterator();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Iterator<IEdge<E>> incomingEdges(IVertex<V> u) throws IllegalArgumentException {
		validateVertex(u);
		Vertex vert = (Vertex)u;
		DLinkedList<IEdge<E>> ins = new DLinkedList<>();
		for(IEdge<E> e : vert.getIncoming().values()) {
			ins.addLast(e);
		}
		return ins.iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * {@inheritDoc}
	 */
	public IVertex<V> opposite(IVertex<V> v, IEdge<E> edge) throws IllegalArgumentException {
		return (IVertex<V>) edge.getOpposite(v);//will be safe since insertEdge enforces type safety
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public int outDegree(IVertex<V> v) throws IllegalArgumentException {
		validateVertex(v);
		Vertex vert = (Vertex)v;
		return vert.getOutgoing().size();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public int inDegree(IVertex<V> u) throws IllegalArgumentException {
		validateVertex(u);
		Vertex vert = (Vertex)u;
		return vert.getIncoming().size();	
	}
}
