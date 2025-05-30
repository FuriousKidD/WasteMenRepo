/**
 * @author TW MANHEMA
 */
package waste.men.datastructures.api;

/**
 * 
 */
public interface Position <E> {
	/**
	 * This method gets the element of the current position 
	 * @return element located at this position
	 * @throws IllegalStateException if the position no longer exists
	 */
	public E getElement() throws IllegalStateException;
};