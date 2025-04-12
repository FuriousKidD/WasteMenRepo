/**
 * 
 */
package waste.men.datastructures.impl.hierarchical;

import java.util.Comparator;

import waste.men.datastructures.api.AdaptablePriorityQueueInterface;
import waste.men.datastructures.api.Entry;

/**
 * 
 */
public class AdaptablePriorityQueue <K, V> extends PriorityQueueHeap<K, V> 
						   implements AdaptablePriorityQueueInterface<K, V>{

	/**
	 * This is an extension of the entry class, which maintains its location
	 */
	protected static class AdaptablePQEntry<K, V> extends PriorityQueueEntry<K, V>{
		private int index;
		public AdaptablePQEntry(K key, V value, int index) {
			super(key, value);
			this.index = index;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int i) {
			index = i;
		}
		
	}//end of nested class
	
	/**
	 * Creates an empty adaptable priority queue with a default comparator 
	 * which compares keys using natural ordering
	 */
	public AdaptablePriorityQueue() {
		super();
	}
	

	/**
	 * Creates an empty adaptable priority queue with a user defined comparator 
	 * which compares keys using natural ordering
	 */
	public AdaptablePriorityQueue(Comparator<K> comp) {
		super(comp);
	}
	
	/**
	 * Checks if the entry passed is location aware and returns the location aware entry
	 * 
	 */
	protected AdaptablePQEntry<K, V> validate(PriorityQueueEntry<K, V> entry)
								throws IllegalArgumentException{
		
		if(!(entry instanceof AdaptablePQEntry)) {
			throw new IllegalArgumentException("Invalid entry");
		}
		AdaptablePQEntry<K, V> locator = (AdaptablePQEntry<K, V>) entry;//safe casting the given entry
		int i = locator.getIndex();
		//checking the index bounds and checking if the element at that index is correct
		if(i > heap.size() || heap.get(i) != locator) {
			throw new IllegalArgumentException("Invalid entry");
		}
		return locator;
	}
	
	@Override
	public Entry<K, V> remove(Entry<K, V> entry) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Entry<K, V> replaceKey(Entry<K, V> entry, K key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Entry<K, V> replaceValue(Entry<K, V> entry, V value) {
		// TODO Auto-generated method stub
		return null;
	}
}

