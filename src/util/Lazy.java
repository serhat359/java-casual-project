package util;

import interfaces.CountedIterable;
import java.util.Iterator;
import java.util.List;

public class Lazy{

	public static <T>Iterator<T> getIterator(final T[] array){
		return new Iterator<T>(){
			int i = 0;

			@Override
			public boolean hasNext(){
				return i < array.length;
			}

			@Override
			public T next(){
				return array[i++];
			}

			@Override
			public void remove(){
				throw new UnsupportedOperationException();
			}
		};
	}

	public static <T>Iterable<T> asIterable(final T[] array){
		return new Iterable<T>(){
			@Override
			public Iterator<T> iterator(){
				return getIterator(array);
			}
		};
	}

	public static <T>Iterable<T> asIterable(final T object){
		return new Iterable<T>(){

			@Override
			public Iterator<T> iterator(){
				return new Iterator<T>(){

					boolean yielded = false;

					@Override
					public boolean hasNext(){
						return !yielded;
					}

					@Override
					public T next(){
						if(!yielded){
							yielded = true;
							return object;
						}

						throw new RuntimeException();
					}

					@Override
					public void remove(){
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	public static <T>CountedIterable<T> asIterableWithCount(final List<T> list){
		return new CountedIterable<T>(){

			@Override
			public Iterator<T> iterator(){
				return list.iterator();
			}

			@Override
			public int count(){
				return list.size();
			}

		};
	}

	public static <T>CountedIterable<T> asIterableWithCount(final Iterable<T> iterable, final int count){
		return new CountedIterable<T>(){

			@Override
			public Iterator<T> iterator(){
				return iterable.iterator();
			}

			@Override
			public int count(){
				return count;
			}

		};
	}

	public static <T>CountedIterable<T> asIterableWithCount(final T[] array){
		return new CountedIterable<T>(){

			@Override
			public Iterator<T> iterator(){
				return getIterator(array);
			}

			@Override
			public int count(){
				return array.length;
			}

		};
	}

	/**
	 * Generate a sequence of numbers from start to end (end is not included).
	 * Please note that stop must not be less than start
	 * @return The number sequence
	 */
	public static Iterable<Integer> range(final int start, final int stop, final int stepSize){
		return new Iterable<Integer>(){

			@Override
			public Iterator<Integer> iterator(){
				return new Iterator<Integer>(){

					int next = start;

					@Override
					public boolean hasNext(){
						return next < stop;
					}

					@Override
					public Integer next(){
						int result = next;
						next += stepSize;
						return result;
					}

					@Override
					public void remove(){
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	public static Iterable<Integer> range(final int stop){
		return range(0, stop);
	}

	public static Iterable<Integer> range(final int start, final int stop){
		return range(start, stop, 1);
	}
}
