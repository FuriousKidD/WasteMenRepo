/**
 * 
 */
package waste.men.datastructures.api;

/**
 * This is an upgraded version of the PriorityQueueHeap. It has additional methods.
 * This data structure allows users to update the priority of elements in the queue,
 * can also remove entries at arbitray positions. 
 */
public interface AdaptablePriorityQueueInterface <K,V>{
	
	/**
	 * This removes a given entry from the list, provided that the 
	 * entry already exists in the list
	 * @param entry
	 * @return return the removed entry
	 */
	public Entry<K, V> remove(Entry<K,V> entry);
	
	/**
	 * Changes the key of an entry, thus changing the priority of the 
	 * given entry.
	 * @param entry
	 * @param key
	 * @return updated entry
	 */
	public Entry<K, V> replaceKey(Entry<K, V> entry,K key);
	
	/**
	 * Changes the value of a given entry.
	 * @param entry
	 * @param value
	 * @return updated entry
	 */
	public Entry<K, V> replaceValue(Entry<K, V> entry, V value);
}
