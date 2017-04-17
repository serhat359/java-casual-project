package casual;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MyLinkedList<T>{

	// First and last elements of the linked list
	private Node<T> start = null;
	private Node<T> end = null;
	private int size = 0;

	public MyLinkedList(){
	}

	public MyLinkedList(List<T> l){
		addAll(l);
	}

	public MyLinkedList(Iterable<T> items){
		for(T item: items){
			add(item);
		}
	}
	
	public MyLinkedList(T t){
		add(t);
	}

	public void add(int index, T t){
		Node<T> toBeAdded = new Node<>(t);

		if(index == 0){
			toBeAdded.next = start;
			start = toBeAdded;
		}
		else{
			Node<T> n = start;

			for(int i = index - 1; i > 0; i--)
				n = n.next;

			toBeAdded.next = n.next;
			n.next = toBeAdded;
		}
		size++;
	}

	public boolean add(T t){
		Node<T> n = new Node<>(t);

		if(start == null)
			start = end = n;
		else{
			end.next = n;
			end = n;
		}
		size++;
		return true;
	}

	public boolean addAll(Collection<? extends T> c){
		Iterator<? extends T> it = c.iterator();
		while(it.hasNext())
			add(it.next());
		return true;
	}

	public void clear(){
		start = end = null;
		size = 0;
	}

	public boolean contains(Object o){
		return indexOf(o) >= 0;
	}

	public boolean containsAll(Collection<?> c){
		Iterator<?> it = c.iterator();
		while(it.hasNext()){
			if(!contains(it.next()))
				return false;
		}
		return true;
	}

	public T get(int index){
		Node<T> n = start;
		try{
			for(int i = index; i > 0; i--)
				n = n.next;
			return n.o;
		}
		catch(NullPointerException npe){
			throw new IndexOutOfBoundsException();
		}
	}

	public int indexOf(Object o){
		try{
			int index = 0;
			Node<T> pointer = start;
			while(pointer.o != o){
				pointer = pointer.next;
				index++;
			}
			return index;
		}
		catch(NullPointerException e){
			return -1;
		}
	}

	public boolean isEmpty(){
		return size == 0;
	}

	public Iterator<T> iterator(){
		return new Iterator<T>(){
			Node<T> n = null; // The node returned with the next() operation
			Node<T> last = null; // The node which is behind n

			@Override
			public boolean hasNext(){
				return (n == null) ? (start != null) : (n.next != null);
			}

			@Override
			public T next(){
				last = n;
				n = (n == null) ? start : n.next;
				return n.o;
			}

			@Override
			public void remove(){
				if(n == last)
					throw new IllegalStateException();
				if(last == null)
					start = start.next;
				else
					last.next = n.next;
				if(n == end)
					end = last;
				size--;
				n = last;
			}
		};
	}

	public int lastIndexOf(Object o){
		boolean found = false;
		Node<T> pointer = start;
		int index = 0;
		try{
			for(int i = 0; true; pointer = pointer.next, i++){
				if(pointer.o == o){
					index = i;
					found = true;
				}
			}
		}
		catch(NullPointerException e){
			return found ? index : -1;
		}
	}

	public T remove(int index){
		try{
			T t;
			if(index == 0){
				t = start.o;
				start = start.next;
				if(start == null)
					end = null;
				return t;
			}
			else{
				Node<T> n = start;

				for(int i = index - 1; i > 0; i--)
					n = n.next;

				t = n.next.o;

				n.next = n.next.next;
				if(n.next == null)
					end = n;
			}
			size--;
			return t;
		}
		catch(NullPointerException npe){
			throw new IndexOutOfBoundsException();
		}
	}

	/*
		public boolean remove(Object o){
			return false;
		}
	*/
	
	public T set(int index, T t){
		Node<T> n = start;
		for(int i = index; i > 0; i--)
			n = n.next;
		T old = n.o;
		n.o = t;
		return old;
	}

	public int size(){
		return size;
	}

	@SuppressWarnings("unchecked")
	public T[] toArray(){
		Object[] toBeReturned = new Object[size()];
		Node<T> p = start;
		int i = 0;
		while(p != null){
			toBeReturned[i] = p.o;
			p = p.next;
			i++;
		}
		return (T[])toBeReturned;
	}

	@Override
	public String toString(){
		String s = "{";
		if(start != null){
			s += start.o;
			for(Node<T> n = start.next; n != null; n = n.next)
				s += ", " + n.o;
		}
		s += "}";
		s += " Size: " + size();
		return s;
	}

	class Node<E>{
		E o;
		Node<E> next = null;

		public Node(E o){
			this.o = o;
		}
	}
}