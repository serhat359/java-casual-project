package util;

import interfaces.*;

import java.util.Iterator;

public class Stream<T> implements Iterable<T>{

	private Iterable<T> iterable;

	public Stream(Iterable<T> iterable){
		this.iterable = iterable;
	}

	@Override
	public Iterator<T> iterator(){
		return iterable.iterator();
	}

	public <E>Stream<E> select(final Selector<T, E> selector){
		return new Stream<>(new Iterable<E>(){

			@Override
			public Iterator<E> iterator(){
				return new Iterator<E>(){

					Iterator<T> iterator = iterable.iterator();

					@Override
					public boolean hasNext(){
						return iterator.hasNext();
					}

					@Override
					public E next(){
						return selector.select(iterator.next());
					}

					@Override
					public void remove(){
						throw new UnsupportedOperationException();
					}
				};
			}
		});
	}

	public Stream<T> where(final Condition<T> condition){
		return new Stream<>(new Iterable<T>(){
			@Override
			public Iterator<T> iterator(){
				return new Iterator<T>(){

					private Iterator<T> iterator = iterable.iterator();
					private Box<T> element = null;

					@Override
					public boolean hasNext(){
						initialize();

						return element != null;
					}

					@Override
					public T next(){
						initialize();

						if(element != null){
							T result = element.value;
							element = null;
							return result;
						}
						else
							throw new RuntimeException("Iterable is empty");
					}

					@Override
					public void remove(){
						throw new UnsupportedOperationException();
					}

					private void initialize(){
						if(element == null){
							while(iterator.hasNext()){
								T elem = iterator.next();

								if(condition.test(elem)){
									element = new Box<T>(elem);
									break;
								}
							}
						}
					}
				};
			}
		});
	}

	public void forEach(Action<T> action){
		for(T element: this){
			action.run(element);
		}
	}
	
	public T reduce(Flattener<T> flattener){
		if(isEmpty(iterable))
			throw new RuntimeException("Iterable contains no element");

		Iterator<T> iter = iterable.iterator();

		T element = iter.next();

		while(iter.hasNext()){
			element = flattener.flatten(element, iter.next());
		}

		return element;
	}

	public Stream<T> concat(final Iterable<T> iterable2){
		return new Stream<>(new Iterable<T>(){
			@Override
			public Iterator<T> iterator(){
				return new Iterator<T>(){

					Iterator<T> i1 = iterable.iterator();
					Iterator<T> i2 = iterable2.iterator();

					@Override
					public boolean hasNext(){
						return i1.hasNext() || i2.hasNext();
					}

					@Override
					public T next(){
						return i1.hasNext() ? i1.next() : i2.next();
					}

					@Override
					public void remove(){
						throw new UnsupportedOperationException();
					}
				};
			}
		});
	}

	public boolean isEmpty(Iterable<T> iterable){
		return !iterable.iterator().hasNext();
	}

}
