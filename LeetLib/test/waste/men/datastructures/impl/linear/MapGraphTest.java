/**
 * 
 */
package waste.men.datastructures.impl.linear;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import waste.men.datastructures.api.IEdge;
import waste.men.datastructures.api.IGraph;
import waste.men.datastructures.api.IVertex;
import waste.men.datastructures.api.Iterator;
import waste.men.datastructures.impl.hierarchical.AdjacencyMapGraph;

/**
 * 
 */
class MapGraphTest {

	private IGraph<String, Integer> mapGraph;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	@DisplayName("Instantiates a directed/undirected graph. Testing if graph creation is possible")
	void setUp() throws Exception {
		mapGraph = new AdjacencyMapGraph<>(false);
	}

	/*
	 * Testing the insertion of a vertex into a graph: insertVertex(v)
	 * Checking if the vertex was actually inserted: containsVertex(v) method
	 */
	@DisplayName("Testing the insertion of vertices into a graph.")
	@Test
	void insertVertexTest() {
		IVertex<String> vert1 = mapGraph.insertVertex("V1");
		assertEquals("V1", vert1.getElement(), "Expected V1");//V1 is expected
		assertTrue(mapGraph.containsVertex(vert1), "Checking if the graph contains V1. Expects True");//true is expected
		//checking if inserted vertex is added to list of vertices
		assertTrue(mapGraph.vertices().hasNext(), "Checking if Iterator has atleast one object. Expects True");
		
	}
	
	/*
	 * Checks if an edge is inserted into the graph: insertEdge(v1,v2)
	 * Checks if the inserted edge exists
	 * Checks if the edge inserted, contains the vertices that were used to create it
	 */
	@DisplayName("Testing the insertion of edge connectivity into a graph")
	@Test
	void insertEdgeTest() {
		IVertex<String> vert1 = mapGraph.insertVertex("V1");
		IVertex<String> vert2 = mapGraph.insertVertex("V2");
		
		IEdge<Integer> edge1 = mapGraph.insertEdge(vert1, vert2, 15);
		assertEquals(15, edge1.getElement(), "Expecting the edge element 15");
	}
	
	/*
	 * Tests the containsEdge(e) method
	 * Tests the endVertices(e) method
	 * Tests incidentEdges(v) method
	 */
	@Test
	@DisplayName("Testing other miscelleneous methods of the graph structure")
	void edgeMethodsTest() {
		IVertex<String> vert1 = mapGraph.insertVertex("V1");
		IVertex<String> vert2 = mapGraph.insertVertex("V2");
		
		IEdge<Integer> edge1 = mapGraph.insertEdge(vert1, vert2, 15);
		assertTrue(mapGraph.containsEdge(edge1));//checks if the edge inserted exists
		
		IVertex<String>[] endpoints = mapGraph.endVertices(edge1);
		String[] endpointsElement = {endpoints[0].getElement(),endpoints[1].getElement()};
		String[] endpointsExpected = {"V1","V2"};
		assertArrayEquals(endpointsExpected, endpointsElement , "Expecting an array of V1 and V2 vertices");
		
		assertTrue(mapGraph.incidentEdges(vert2).hasNext(), "Expects true");
	}
	
	@DisplayName("Testing the removal of an edge")
	@Test
	void removeEdge() {
		IVertex<String> vert1 = mapGraph.insertVertex("V1");
		IVertex<String> vert2 = mapGraph.insertVertex("V2");
		IEdge<Integer> edge1 = mapGraph.insertEdge(vert1, vert2, 15);
		mapGraph.removeEdge(edge1);
		assertEquals(0, mapGraph.numEdges(), "Expecting 1 vertex to be left");
	}
	
	/*
	 * Testing the functionality of the neighbors(v) method
	 * Testing to see if it works as intended when graph is directional and when undirectional
	 */
	@DisplayName("Testing the neighbors of a given vertex")
	@Test
	void neighborsTest() {
		IVertex<String> vert1 = mapGraph.insertVertex("V1");
		IVertex<String> vert2 = mapGraph.insertVertex("V2");
		mapGraph.insertEdge(vert1, vert2, 12);
		Iterator<IVertex<String>> neigh = mapGraph.neighbors(vert1);
		assertTrue(neigh.hasNext(),"Expecting True");
		assertEquals("V2", neigh.next().getElement(), "Expecting V2");
	}
	
	@Test
	void removeVertexTest() {
		
	}
	
	@Test
	void removeEdgeTest() {
		
	}
	/*
	@Test
	void test() {
		fail("Not yet implemented");
	}*/

}
