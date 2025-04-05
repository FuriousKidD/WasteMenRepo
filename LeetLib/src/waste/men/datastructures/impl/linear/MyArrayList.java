/**
 * 
 */
package waste.men.datastructures.impl.linear;

import java.util.Iterator;

/**
 * 
 */
public class MyArrayList <E> implements Iterable<E>{
	
	private final int DEFUALT_CAPACITY = 16;
	private static final int  RESIZE = 2;
	private int capacity = DEFUALT_CAPACITY;
	private int size = 0;
	
	private E[] arrList;
	private int resizeType = 0;
	private boolean incremental = false;
	private boolean doubling = false;
	
	/**
	 * Default Constructor
	 */
	public MyArrayList() {
		this(RESIZE);
	}
	
	/**
	 * Parameterized constructor
	 * @param resizeType 1 for Incremental method and 2 for Double method
	 */
	@SuppressWarnings("unchecked")
	public MyArrayList(int resizeType) {
		this.resizeType = resizeType;
		if(resizeType > 2 || resizeType < 1)
			resizeType = 2;
		if(resizeType == 1) 
			incremental = true;
		else if(resizeType == 2)
			doubling = true;
		
		arrList = (E[]) new Object[capacity];
	}
	
	/**
	 * Dynamically adds new elements to the array.
	 * @param element
	 */
	public void add(E element) {
		expandArray(size);
		arrList[size++] = element;
	}
	
	/**
	 * checks if the array is full. If it is then it will incrent the size of the 
	 * array
	 * @param size
	 */
	@SuppressWarnings("unchecked")
	private void expandArray(int length) {
		if(length == capacity) {
			if(doubling) {
				length*=2;
				E[] arrNew = (E[]) new Object[length];
				System.arraycopy(arrList, 0, arrNew, size - 1, length);
				this.capacity = length;
			}
			else if(incremental) {

				length += 10;
				E[] arrNew = (E[]) new Object[length];
				System.arraycopy(arrList, 0, arrNew, size - 1, length);
				this.capacity = length;
			}
		}
	}
	
	private class MyArrayListIterator implements Iterator<E>{
		
		int j = 0;
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return j < size;
		}

		@Override
		public E next(){
			// TODO Auto-generated method stub
			if(j >= size)
				throw new IllegalStateException("Reached End of List!");
			
			return arrList[j++];
		}
		
	}//end of Iterator
	
	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return new MyArrayListIterator();
	}

}
