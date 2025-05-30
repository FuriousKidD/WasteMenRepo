package waste.men.datastructures.impl.linear;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import waste.men.datastructures.api.Queues;

class QueueTest {

	@BeforeEach
	void setUp() throws Exception {
	}
	
	Queues<Integer> que;

	@Test
	void testIsEmpty() {
		que = new LinkedListQueue<>();
		assertTrue(que.isEmpty());
		assertTrue(que.size() == 0);
	}
	
	@Test
	void testEnqueue() {
		que = new LinkedListQueue<>();
		que.enqueue(12);
		assertEquals(12, que.dequeue(), "enqueue and dequeue work well");
	}
	
	@Test
	void testFirst() {
		que = new LinkedListQueue<>();
		int first = 0;
		for(int i = 0; i < 10; i++) {
			java.util.Random rand = new Random();
			int randomInt = rand.nextInt(0, 12342);
			que.enqueue(randomInt);
			if(i == 0) {
				first = que.first();
			}
		}
		assertEquals(first, que.first());
	}
	
	/*
	@Test
	void test() {
		fail("Not yet implemented");
	}
	*/

}
