package interfaces;

public interface CountedIterable<T> extends Iterable<T>{
	public int count();
}
