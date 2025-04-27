/**
 * 
 */
package waste.men.datastructures.api;

/**
 * 
 */
public interface IEdge <E> {
	
	public E getElement();
	
	/**
	 * Gets the opposite vertex that is connected to the given edge via
	 * this edge
	 * @param v
	 * @return
	 */
	public IVertex<?> getOpposite(IVertex<?> v);
}
