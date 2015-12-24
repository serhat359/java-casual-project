package util;

import casual.Stopwatch;
import interfaces.Getter;

public class Cached<T>{

	T value = null;
	Getter<T> getter;
	
	public Cached(Getter<T> getter){
		this.getter = getter;
	}
	
	public T getValue(){
		if(value == null)
			value = getter.get();
		
		if(value == null)
			throw new RuntimeException("Cached value getter returned a null value");
		
		return value;
	}
	
	public static void test(){
		Cached<Integer> pi = new Cached<>(new Getter<Integer>(){
			@Override
			public Integer get(){
				int total = 0;
				int max = 100000000;
				for(int i=0; i < max; i++)
					total += i;
				
				return total / max;
			}
		});
		
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		pi.getValue();
		stopwatch.stopReportStart();
		pi.getValue();
		stopwatch.stopReportStart();
		pi.getValue();
		stopwatch.stopReportStart();
		stopwatch.stop();
	}
}
