/**
 * 
 */
package waste.men.datastructures.impl.linear;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import waste.men.datastructures.api.IGraph;
import waste.men.datastructures.api.IVertex;
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
	void setUp() throws Exception {
		mapGraph = new AdjacencyMapGraph<>(true);
	}

	/*
	 * Testing the insertion of a vertex into a graph: insertVertex(v)
	 * Checking if the vertex was actually inserted: containsVertex(v) method
	 */
	@Test
	void insertVertexTest() {
		IVertex<String> vert1 = mapGraph.insertVertex("V1");
		assertEquals("V1", vert1, "Expected V1");//V1 is expected
		assertTrue(mapGraph.containsVertex(vert1), "Checking if the graph contains V1. Expects True");//true is expected
		assertTrue(mapGraph.vertices().hasNext(), "Checking if Iterator has atleast one object. Expects True");//checking if inserted vertex is added to list of vertices
		
	}
	/*
	@Test
	void test() {
		fail("Not yet implemented");
	}*/

}
