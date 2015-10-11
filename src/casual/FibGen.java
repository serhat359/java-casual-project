package casual;
import java.util.ArrayList;

// Fibonacci number generator
public class FibGen{

	private int cap = 1; // calculated up to this number
	private ArrayList<Long> al = new ArrayList<Long>();
	private long low = 0; // last calculated low
	private long high = 1; // last calculated high

	public FibGen(){
		al.add(low);
		al.add(high);
	}

	public long getFib(int i){
		al.ensureCapacity(i + 1);
		for(; i > cap; cap++){
			long tmp = low + high;
			low = high;
			high = tmp;
			al.add(tmp);
		}
		return al.get(i);
	}
	
	public static void test(){
		FibGen fg = new FibGen();
		for(int i = 0; i < 100; i++)
			System.out.println(fg.getFib(i) + ", " + ((double)fg.getFib(i + 1) / fg.getFib(i)));
	}
}
