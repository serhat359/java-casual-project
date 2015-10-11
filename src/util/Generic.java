package util;

import java.util.Iterator;

public class Generic<T>{

	public Iterator<T> getIterator(final Object[] array){
		return new Iterator<T>(){
			int i = 0;

			@Override
			public boolean hasNext(){
				return i < array.length;
			}

			@SuppressWarnings("unchecked")
			@Override
			public T next(){
				return (T)array[i++];
			}

			@Override
			public void remove(){
				throw new UnsupportedOperationException();
			}
		};
	}
}
