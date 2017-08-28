package casual;

public class MyList<T>{

	private Object[] values;
	private int size = 0;
	
	public MyList(){
		values = new Object[16];
	}
	
	public void add(T obj){
		values[size] = obj;
		size++;
	}
	
	@SuppressWarnings("unchecked")
	public T get(int index){
		return (T)values[index];
	}
}
