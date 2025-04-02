/**
 * 
 */
package waste.men.datastructures.impl.linear;

import waste.men.datastructures.api.*;

/**
 * 
 */
public class ArrayDeque <E> implements Deques<E>, Cloneable{
	
	private static final int CAPACITY = 1000;
	private E [] circleQue;
	private int size;
	private int front;
	private int back;
	private int cap;
	
	public ArrayDeque() {this(CAPACITY);}
	
	@SuppressWarnings("unchecked")
	public ArrayDeque(int capacity) {
		this.cap = capacity;
		circleQue = (E[]) new Object[cap];
		front = back = 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Deques<E> clone() throws CloneNotSupportedException{
		Deques<E> cloned = (ArrayDeque<E>) super.clone();
		
		if(isEmpty()) {
			return null;
		}
		Deques<E> temp = new ArrayDeque<E>();
		for(int i = 0; i < size(); i++) {
			temp.addFirst(cloned.removeFirst());
		}
		return temp;
	}
	
	@Override
	public void addFirst(E e) {
		// TODO Auto-generated method stub
		
		/*
		 * If the Deque is full, then that means the index in the array that 
		 * comes before the front pointer is occupied.
		 * If the Deque is not full, then the index before the front pointer is
		 * unoccupied and we can fill it in and assign that to be the new front
		 */
		if(size == cap) {
			throw new IllegalStateException("Deque is FULL");
		}
		E temp = first();
		
		front = ( (front - 1) + cap) % cap;//moves the front pointer back by 1 index
		circleQue[front] = e;
		size++;
	}

	@Override
	public void addLast(E e) {
		// TODO Auto-generated method stub
		if(size == cap) {
			throw new IllegalStateException("Deque is FULL");
		}
		back = front + size % cap;
		circleQue[back] = e;
		size++;
		
		
	}

	@Override
	public E removeFirst() {
		// TODO Auto-generated method stub
		if(isEmpty()) {
			throw new IllegalStateException("Cannot remove from an empty Deque");
		}
		E removed = first();
		circleQue[front] = null;
		front = (front + 1) % cap;
		size--;
		return removed;
	}

	@Override
	public E removeLast() {
		// TODO Auto-generated method stub
		if(isEmpty()) {
			throw new IllegalStateException("Cannot remove from an empty Deque");
		}
		E removed = last();
		circleQue[back - 1] = null;
		back = (back - 1) % cap;
		size--;
		return removed;
	}

	@Override
	public E first() {
		// TODO Auto-generated method stub
		if(isEmpty()) return null;
		return circleQue[front];
	}

	@Override
	public E last() {
		// TODO Auto-generated method stub
		if(isEmpty()) return null;
		return circleQue[back-1];
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return size == 0;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}

}
