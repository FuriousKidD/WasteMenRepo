/**
 * 
 */
package waste.men.datastructures.impl.linear;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import waste.men.datastructures.impl.hierarchical.PriorityQueueHeap;

/**
 * 
 */
class PriorityQueueHeapTest {

	private PriorityQueueHeap<Integer,String> priorityQ;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		priorityQ = new PriorityQueueHeap<Integer, String>();
	}
	
	@DisplayName("Test min, isEmpty, insert methods")
	@Test
	/**
	 * This method tests the following functionalities:
	 * upHeap, downHeap, insert, swap, size, isEmpty, min
	 */
	void insertAndMin() {
		
		//Must return true 
		assertTrue(priorityQ.isEmpty(), "New queue should be currently empty");
		
		priorityQ.insert(5, "Hello");//insert a new entry
		assertFalse(priorityQ.isEmpty(), "Queue must no longer be empty now");
		
		//verify if the min returns the correct value. Currently only one entry is in the queue
		assertEquals(5, priorityQ.min().getKey(), "Must return the only element");//return the key
		
		//Inserting a higher priority element(element with a smaller key value)
		priorityQ.insert(2, "World!");
		assertEquals(2, priorityQ.min().getKey(), "Must return the lowest key (highest priority)");//return
		
		//Inserting a middle priority element
		priorityQ.insert(3, " ");
		assertEquals(2, priorityQ.min().getKey(), "Highest priority must be the same as the privious");
		
		
	}

}
