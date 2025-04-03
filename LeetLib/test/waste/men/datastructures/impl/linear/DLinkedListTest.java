/**
 * 
 */
package waste.men.datastructures.impl.linear;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 
 */
public class DLinkedListTest {
	
	private DLinkedList<String> list;
	
	@BeforeEach
	/**
	 * This creates a fresh list before every test
	 */
	void setUp() {
		list = new DLinkedList<String>();
	}
	
	@Test
	@DisplayName("New list should be empty")
	/**
	 * Testing the isEmpty() method
	 */
	void emptyListTest() {
		assertTrue(list.isEmpty());//should return true
		assertEquals(0, list.size());//should return true
	}
	
}
