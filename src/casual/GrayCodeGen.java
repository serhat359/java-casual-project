package casual;

import java.util.Iterator;
import java.util.Scanner;

public class GrayCodeGen{

	private Stack<Integer> stack;
	private int calculated;
	private Iterator<Integer> it;

	public GrayCodeGen(){
		stack = new Stack<Integer>();
		it = stack.iterator();
		calculated = 0;
	}

	public int getNext(){
		if(it.hasNext()){
			int n = it.next();
			stack.push(n);
			return n;
		}
		else{
			stack.push(++calculated);
			it = stack.iterator();
			it.next();
			return calculated;
		}
	}

	public static Iterable<Integer> asIterable(){
		return new Iterable<Integer>(){

			@Override
			public Iterator<Integer> iterator(){
				return new Iterator<Integer>(){

					private GrayCodeGen gen = new GrayCodeGen();

					@Override
					public boolean hasNext(){
						return true;
					}

					@Override
					public Integer next(){
						return gen.getNext();
					}

					@Override
					public void remove(){

					}
				};
			}
		};

	}

	public static void test(){
		GrayCodeGen gen = new GrayCodeGen();

		Scanner s = new Scanner(System.in);
		Printer.print("Starting code generator...\n");
		int number = 0;

		String str;
		do{
			str = s.next();
			number ^= 1 << (gen.getNext() - 1);
			System.out.println(Integer.toBinaryString(number));
		}
		while(str != null);

		s.close();
	}
}
