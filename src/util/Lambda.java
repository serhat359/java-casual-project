package util;

import interfaces.*;
import casual.Stack;

public class Lambda{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void applyOperation(Iterable s, Method m, Condition c){
		for(Object object: s){
			if(c == null || c.test(object))
				m.execute(object);
		}
	}

	public static void applyOperation(Iterable<?> s, Method<?> m){
		applyOperation(s, m, null);
	}
	
	public static void test(){

		Stack<Integer> stack = new Stack<Integer>();

		stack.push(8);
		stack.push(7);
		stack.push(2);
		stack.push(23);
		stack.push(-8);
		stack.push(0);

		@SuppressWarnings("rawtypes")
		Method print = new Method(){
			@Override
			public void execute(Object o){
				System.out.print(o + ",");
			}
		};

		Condition<Integer> absLessThanFive = new Condition<Integer>(){
			@Override
			public boolean test(Integer i){
				return Math.abs(i) < 5;
			}
		};

		Lambda.applyOperation(stack, print, absLessThanFive);
		//Lambda.applyOperation(stack, print);
	}

}
