package casual;

import java.util.Iterator;

public class Queue<T> implements Iterable<T>{

	private class Node<E>{
		Node<E> behind = null;
		E o;

		Node(E o){
			this.o = o;
		}
	}

	private Node<T> first = null;
	private Node<T> last = null;
	private int size = 0;

	public void add(T item){
		Node<T> n = new Node<>(item);

		if(last == null){
			last = first = n;
		}
		else{
			last.behind = n;
			last = n;
		}

		size++;
	}

	public T remove(){
		if(first == null)
			throw new RuntimeException("Queue is empty");

		Node<T> n = first;

		first = first.behind;
		if(first == null)
			last = null;

		size--;

		return n.o;
	}

	public T element(){
		return first.o;
	}

	public int size(){
		return size;
	}

	@Override
	public Iterator<T> iterator(){
		return new Iterator<T>(){
			Node<T> n = first;

			@Override
			public boolean hasNext(){
				return n != null;
			}

			@Override
			public T next(){
				Node<T> x = n;
				n = n.behind;
				return x.o;
			}

			@Override
			public void remove(){
				throw new UnsupportedOperationException();
			}
		};
	}

	public static void test(){

		Queue<Character> texts = new Queue<Character>();

		texts.add('S');
		texts.add('e');
		texts.add('r');
		texts.add('h');
		texts.add('a');
		texts.add('t');

		Printer.print(texts);
	}
}
