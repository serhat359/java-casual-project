package casual;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import util.Lazy;

public class Stack<E> implements Iterable<E>, Cloneable{

	private class Node<T>{
		T o;
		Node<T> next;

		Node(T o){
			this.o = o;
		}
	}

	private Node<E> top = null;
	private int size = 0;

	public Stack(){
	}

	public Stack(Iterable<E> c){
		for(E item: c){
			push(item);
		}
	}

	public Stack(E[] c){
		new Stack<E>(Lazy.asIterable(c));
	}

	public void push(E item){
		Node<E> n = new Node<>(item);

		n.next = top;
		top = n;
		size++;
	}

	public E pop(){
		if(top == null)
			throw new RuntimeException("Stack is empty");

		Node<E> n = top;
		top = top.next;
		size--;

		return n.o;
	}

	public E peek(){
		if(top == null)
			throw new RuntimeException("Stack is empty");

		return top.o;
	}

	public int size(){
		return size;
	}

	@Override
	public Iterator<E> iterator(){
		return new Iterator<E>(){
			Node<E> n = top;

			@Override
			public boolean hasNext(){
				return n != null;
			}

			@Override
			public E next(){
				if(n == null)
					throw new NoSuchElementException();
				Node<E> temp = n;
				n = n.next;
				return temp.o;
			}

			@Override
			public void remove(){
				throw new UnsupportedOperationException();
			}
		};
	}

	public boolean pushAll(Iterable<E> c){
		for(E e: c){
			push(e);
		}
		return true;
	}

	public void clear(){
		top = null;
		size = 0;
	}

	public boolean contains(Object o){
		for(E item: this){
			if(item == o)
				return true;
		}
		return false;
	}

	public boolean containsAll(Collection<?> c){
		for(Object object: c){
			if(!contains(object))
				return false;
		}
		return true;
	}

	public boolean isEmpty(){
		return size == 0;
	}

	public Object[] toArray(){
		Object[] array = new Object[size()];
		int i = 0;
		for(E object: this){
			array[i] = object;
			i++;
		}
		return array;
	}

	@Override
	public Stack<E> clone(){
		return this.reversed().reversed();
	}

	public boolean equals(Stack<E> s){
		Iterator<E> it1 = this.iterator();
		Iterator<E> it2 = s.iterator();
		try{
			while(it1.hasNext() || it2.hasNext()){
				if(it1.next() != it2.next())
					return false;
			}
		}
		catch(NoSuchElementException ex){
			return false;
		}
		return true;
	}

	public Stack<E> reversed(){
		return new Stack<E>(this);
	}
	
	public static void main(){
		Stack<Integer> st = new Stack<>();

		st.push(3);
		st.push(5);

		for(Integer x: Lazy.range(1, 500000)){
			st.push(x);
		}
		
		System.out.println(st.peek());

		st.pop();

		System.out.println(st.peek());
	}
}
