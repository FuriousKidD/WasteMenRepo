package waste.men.datastructures.impl.linear;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import waste.men.datastructures.api.IEdge;
import waste.men.datastructures.api.IVertex;
import waste.men.datastructures.api.Iterator;
import waste.men.datastructures.impl.hierarchical.AdjacencyMatrixGraph;

class AdjMatrixGraphTest {

	private AdjacencyMatrixGraph<String, String> graph;
	
	@BeforeEach
	void setUp() throws Exception {
		graph = new AdjacencyMatrixGraph<>(false,5);
	}
	
	@Test
	public void testInsertVertex() {
		var vert = graph.insertVertex("A");
		assertEquals(1, graph.numVertices());
		assertEquals("A", vert.getElement());
		
	}
	
	@Test
	public void testInsertEdge() {
		var a = graph.insertVertex("A");
		var b = graph.insertVertex("B");
		var edge = graph.insertEdge(a, b, "AB");
		
		assertEquals(1, graph.numEdges());
		assertNotNull(edge);
		assertEquals("AB", edge.getElement());
	}
	
	@Test
	public void testRemoveVertex() {
		var a = graph.insertVertex("A");
		var b = graph.insertVertex("B");
		graph.insertEdge(a, b, "AB");
		graph.removeVertex(a);
		
		assertEquals(1, graph.numVertices());
		assertEquals(0, graph.numEdges());
	}
	
	@Test
	public void testNeighbors() {
		var a = graph.insertVertex("A");
		var b = graph.insertVertex("B");
		var c = graph.insertVertex("C");
		
		graph.insertEdge(a, b, "AB");
		graph.insertEdge(a, c, "AC");
		
		Iterator<IVertex<String>> neighbors = graph.neighbors(a);
		int count = 0;
		while(neighbors.hasNext()) {
			var n = neighbors.next();
			assertTrue(n.getElement().equals("B") || n.getElement().equals("C"));
			count++;
		}
		
		assertEquals(2, count);
		
	}
	
	@Test
	public void testFeaturesAndLabels() {
		var a = graph.insertVertex("A");
		double[] features = {1.0, 2.0};
		double[] label = {0.0, 1.0};
		graph.setVertexFeature(a, features);
		graph.setVertexLabel(a, label);
		
		assertArrayEquals(features, graph.getVertexFeature(a));
		assertArrayEquals(label, graph.getVertexLabel(a));
		
	}
	
	@Test
	public void testGraphLabel() {
		double[] label = {1.0};
		graph.setGraphLLabel(label);
		assertArrayEquals(label, graph.getGraphLabel());
	}
	
   @Test
    public void testAddSelfLoops() {
        var a = graph.insertVertex("A");
        graph.addSelfLoops("loop");
        IEdge<String> loopEdge = graph.getEdge(a, a);
        assertNotNull(loopEdge);
        assertEquals("loop", loopEdge.getElement());
    }
   
   @Test
   public void testNormalizedAdjacencyMatrix() {
	   var a = graph.insertVertex("A");
	   var b = graph.insertVertex("B");
	   var c = graph.insertVertex("C");
	   
	   graph.insertEdge(a, b, "AB");
	   graph.insertEdge(a, c, "AC");
	   graph.addSelfLoops(null);//adds a self-loop before normalization
	   
	   double[][] norm = graph.getNormalizedAdjacencyMatrix();
	   assertEquals(3, norm.length);
	   assertEquals(3,  norm[0].length);
	   
	   //checking if self-loops are added
	   for(int i = 0; i < 3; i++) {
		   assertTrue(norm[i][i] > 0);
	   }
   }
	/*
	@Test
	void test() {
		fail("Not yet implemented");
	}
	*/

}
