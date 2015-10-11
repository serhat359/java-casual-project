package casual;

import interfaces.PrintFormat;

import java.util.Iterator;

import util.Lazy;

public class Printer{

	public static <T> void print(Iterable<T> col){
		PrintFormat<T> format = new PrintFormat<T>(){
			@Override
			public String toString(T t){
				return t == null ? "null" : t.toString();
			}
		};
		
		print(col, format);
	}

	public static void print(Object o){
		if(o == null)
			System.out.print(o);
		else if(o instanceof Object[])
			print((Object[])o);
		else if(o instanceof Iterable<?>)
			print((Iterable<?>)o);
		else
			System.out.print(o);
	}

	public static <T>void print(T[] ar){
		print(Lazy.asIterable(ar));
	}

	public static <T>void print(T[] ar, PrintFormat<T> format){
		print(Lazy.asIterable(ar), format);
	}
	
	public static <T>void print(Iterable<T> col, PrintFormat<T> format){
		Iterator<T> it = col.iterator();

		print("{");
		if(it.hasNext()){
			print(" ");
			print(format.toString(it.next()));
		}
		while(it.hasNext()){
			print(", ");
			print(format.toString(it.next()));
		}
		print(" }");
	}
}
