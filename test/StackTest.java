import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import casual.Stack;

public class StackTest{

	Stack<Integer> s = new Stack<Integer>();
	Stack<Integer> s2 = new Stack<Integer>();

	@Test
	public void testPushAndPopAndSize(){
		assertEquals(s.size(), 0);
		s.push(5);
		s.push(8);
		assertEquals(s.size(), 2);
		s.pop();
		assertEquals(s.size(), 1);
		assertEquals((int)s.pop(), 5);
	}

	@Test
	public void testIterator(){
		s.push(4);
		s.push(8);

		Iterator<Integer> it = s.iterator();
		while(it.hasNext()){
			s.push(it.next());
		}

		assertArrayEquals(new Object[] { 4, 8, 8, 4 }, s.toArray());
	}

	@Test
	public void testPushAll(){
		s.push(4);
		s.push(8);
		s.push(4);
		
		ArrayList<Integer> ar = new ArrayList<Integer>();
		for(Integer integer: s){
			ar.add(integer);
		}
		
		s2.pushAll(ar);
		
		assertEquals(s.equals(s2), true);
	}

	@Test
	public void testClear(){
		s.push(6);
		s.clear();
		assertEquals(s.size(), 0);
		s.clear();
		assertEquals(s.size(), 0);
	}

	@Test
	public void testContains(){
		assertEquals(s.contains(4), false);
		s.push(4);
		assertEquals(s.contains(4), true);
		assertEquals(s.contains(5), false);
	}

	@Test
	public void testContainsAll(){
		assertEquals(s.containsAll(new ArrayList<Integer>()), true);
	}

	@Test
	public void testIsEmpty(){
		assertEquals(s.isEmpty(), true);
		s.push(2);
		assertEquals(s.isEmpty(), false);
		s.push(4);
		assertEquals(s.isEmpty(), false);
		s.pop();
		s.pop();
		assertEquals(s.isEmpty(), true);
	}

	@Test
	public void testToArray(){
		s.push(8);
		s.push(5);

		assertArrayEquals(new Object[] { 5, 8 }, s.toArray());
	}

	@Test
	public void testClone(){
		s.push(8);
		s2 = s.clone();
		assertEquals(s.equals(s2), true);
		s2.pop();
		assertEquals(s.equals(s2), false);
		s2.push(8);
		assertEquals(s.equals(s2), true);
	}

	@Test
	public void testEqualsStackOfE(){
		assertEquals(s.equals(s2), true);
		s.push(8);
		assertEquals(s.equals(s2), false);
		s2.push(s.pop());
		assertEquals(s.equals(s2), false);
		s.push(8);
		assertEquals(s.equals(s2), true);
	}

}
