package waste.men.datastructures.impl.linear;

import waste.men.datastructures.api.*;

public class DLinkedList <E> implements PositionalList<E>{
	
	/**
	 * 
	 */
	private class Node<E> implements Position<E>{
		private E element;
		private Node<E> prev;
		private Node<E> next;
		
		public Node(E element, Node<E> prev, Node<E> next) {
			setElement(element);
			setPrev(prev);
			setNext(next);
		}
		
		public void setPrev(Node<E> prev) {
			this.prev = prev;
		}
		
		public void setNext(Node<E> next) {
			this.next = next;
		}
		
		public void setElement(E element) {
			this.element = element;
		}
		
		public Node<E> getPrev() {
			return this.prev;
		}
		
		public Node<E> getNext() {
			return this.next;
		}

		@Override
		public E getElement() throws IllegalStateException {
			if(getNext() == null) throw new IllegalStateException("Position is no longer valid");
			return this.element;
		}
		
	}//end of inner Node<E> class
	
	/*
	 * 
	 */
	private Node<E> header;
	private Node<E> trailer;
	private Node<E> prev;
	private Node<E> next;
	private E element;
	private int size;
	
	/**
	 * 
	 */
	public DLinkedList() {
		header = new Node<>(null, null, null);
		trailer = new Node<>(null, header, null);
		header.setNext(trailer);
		size = 0;
	}
	
	@Override
	/**
	 * 
	 */
	public void addFirst(E element) {
		// TODO Auto-generated method stub
		addBetween(element,header,header.next);
	}

	@Override
	/**
	 * 
	 */
	public void addLast(E element) {
		// TODO Auto-generated method stub
		addBetween(element, trailer.prev, trailer);
		
	}

	@Override
	/**
	 * 
	 */
	public E first() {
		// TODO Auto-generated method stub
		return (header.next == null ? null : header.next.element);
	}

	@Override
	/**
	 * 
	 */
	public E last() {
		// TODO Auto-generated method stub
		return (trailer.prev == null ? null : trailer.prev.element);
	}
	
	/**
	 * 
	 */
	@Override
	public E removeFirst() {
		// TODO Auto-generated method stub
		return removeBetween(header.next, header, header.next.next);
	}
	
	/**
	 * 
	 * @return
	 */
	public E removeLast() {
		return removeBetween(trailer.prev, trailer, trailer.prev.next);
	}

	@Override
	/**
	 * 
	 */
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return size == 0;
	}

	@Override
	/**
	 * 
	 */
	public E getNext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * 
	 */
	public E getNode<E>(int i) {
		// TODO Auto-generated method stub
		return element;
	}

	@Override
	/**
	 * 
	 */
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	/**
	 * 
	 */
	public String toString() {
		return null;
	}
	
	/**
	 * 
	 * @param element
	 * @param prev
	 * @param next
	 */
	private void addBetween(E element, Node<E> prev, Node<E> next) {
		
		if(isEmpty()) {
			Node<E> currentNode<E> = new Node<E>(element,header,trailer);
			currentNode<E>.prev = header;
			currentNode<E>.next = trailer;
			header.next = currentNode<E>;
			trailer.prev = currentNode<E>;
			size++;
		}
		else {
			Node<E> currentNode<E> = new Node<E>(element, prev, next);
			currentNode<E>.prev = prev;
			currentNode<E>.next = next;
			prev.next = currentNode<E>;
			next.prev = currentNode<E>;
			size++;
		}
	}
	
	/**
	 * 
	 * @param current
	 * @param prev
	 * @param next
	 * @return
	 * @throws NullPointerException
	 */
	private E removeBetween(Node<E> current, Node<E> prev, Node<E> next) throws NullPointerException {
		
		if(isEmpty()) 
			return null;
		else if(current == null)
			throw new NullPointerException("Current Node<E> is null!");
		else{
			E removed = current.element;
			prev.next = current.next;
			next.prev = current.prev;
			size--;
			return removed;	
		}
	}

	/**
	 * @return the prev
	 */
	public Node<E> getPrev() {
		return prev;
	}

	/**
	 * @param prev the prev to set
	 */
	public void setPrev(Node<E> prev) {
		this.prev = prev;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(Node<E> next) {
		this.next = next;
	}

	/**
	 * @param element the element to set
	 */
	public void setElement(E element) {
		this.element = element;
	}

}
