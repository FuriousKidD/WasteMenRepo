package waste.men.datastructures.impl.linear;

import waste.men.datastructures.api.*;

public class DLinkedList <E> implements LinkedListInterface<E>{
	
	/**
	 * 
	 */
	class Node{
		private E element;
		private Node prev;
		private Node next;
		
		public Node(E element, Node prev, Node next) {
			setElement(element);
			setPrev(prev);
			setNext(next);
		}
		
		public void setPrev(Node prev) {
			this.prev = prev;
		}
		
		public void setNext(Node next) {
			this.next = next;
		}
		
		public void setElement(E element) {
			this.element = element;
		}
		
		public Node getPrev() {
			return this.prev;
		}
		
		public Node getNext() {
			return this.next;
		}
		
		
	}//end of inner Node class
	
	/*
	 * 
	 */
	private Node header;
	private Node trailer;
	private Node prev;
	private Node next;
	private E element;
	private int size;
	
	/**
	 * 
	 */
	public DLinkedList() {
		header = new Node(null, null, null);
		trailer = new Node(null, header, null);
		header.next = trailer;
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
	public E getNode(int i) {
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
	private void addBetween(E element, Node prev, Node next) {
		
		if(isEmpty()) {
			Node currentNode = new Node(element,header,trailer);
			currentNode.prev = header;
			currentNode.next = trailer;
			header.next = currentNode;
			trailer.prev = currentNode;
			size++;
		}
		else {
			Node currentNode = new Node(element, prev, next);
			currentNode.prev = prev;
			currentNode.next = next;
			prev.next = currentNode;
			next.prev = currentNode;
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
	private E removeBetween(Node current, Node prev, Node next) throws NullPointerException {
		
		if(isEmpty()) 
			return null;
		else if(current == null)
			throw new NullPointerException("Current Node is null!");
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
	public Node getPrev() {
		return prev;
	}

	/**
	 * @param prev the prev to set
	 */
	public void setPrev(Node prev) {
		this.prev = prev;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(Node next) {
		this.next = next;
	}

	/**
	 * @param element the element to set
	 */
	public void setElement(E element) {
		this.element = element;
	}

}
