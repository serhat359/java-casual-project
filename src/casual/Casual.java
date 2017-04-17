package casual;

import java.awt.HeadlessException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class Casual{
	public static void main(String[] args) throws NumberFormatException, HeadlessException{

		// CuttingRod.test();

		// InsertionSort.test();

		// FibGen.test();

		// System.out.print(calcPostFix("2352-*7/+2-822/*+"));

		// ExpressionEvaluator.test();

		// Queue.test();

		// Lambda.test();

		// GrayCodeGen.test();

		// SudokuSolver.test();

		// DancingLinks.test();

		// GenericArray<Integer> array = new GenericArray<>(new Integer[] { 2, 3 });

		// Printer.print(array);

		// Printer.print(Casual.calcPostFix("23+"));

		// BetterSudokuSolver.test();

		// new IceCubeSolver("zeldaIceCube.txt");

		// TreeNode.test();

		// Cached.test();

		// NoLayoutFrame frame = new NoLayoutFrame(null);
		// frame.setContentSize(150,150);
		// frame.setVisible(true);

		// QRCodeDisplay.test();

		// testLambdaExp();

		// getGeometricTriangles();

		// PrimeFactor.test();

		int sumOfAll = sum(6,8,3);
		
		System.out.println(sumOfAll);

		System.out.println("hello");
	}

	public static int sum(int... values){
		int sum = 0;
		for(int i: values){
			sum += i;
		}
		return sum;
	}

	public static void report(Runnable func){
		long currentNano = System.currentTimeMillis();

		func.run();

		System.out.printf("The operation took %d miliseconds", (System.currentTimeMillis() - currentNano));
	}

	public static String bigIntMul(String a, String b){
		int[] aints = stringToInts(a);
		int[] bints = stringToInts(b);
		int carry = 0;

		char[] resultChars = new char[aints.length + bints.length];

		for(int i = 0; i < resultChars.length - 1; i++){

			int result = carry;
			for(int j = 0; j <= i; j++){
				int aindex = j;
				int bindex = i - j;

				if(aindex < aints.length && bindex < bints.length)
					result += aints[aints.length - 1 - aindex] * bints[bints.length - 1 - bindex];
			}

			int div = result / 10;
			int rem = result % 10;

			carry = div;
			resultChars[resultChars.length - 1 - i] = (char)(rem + '0');
		}

		resultChars[0] = (char)(carry + '0');

		return new String(resultChars);
	}

	private static int[] stringToInts(String x){
		int[] ints = new int[x.length()];
		for(int i = 0; i < x.length(); i++)
			ints[i] = x.charAt(i) - '0';
		return ints;
	}

	public static <T> T[] appendArray(T[] arr, T object){
		T[] newarr = Arrays.copyOf(arr, arr.length + 1);

		newarr[arr.length] = object;

		return newarr;
	}

	private static void getGeometricTriangles(){
		HashMap<Integer, String> table = new HashMap<>();

		for(int i = 2; i < 2000; i++){
			int iSq = i * i;

			for(int j = i - 1; j > 0; j--){
				int diff = iSq - j * j;

				String repres = i + " - " + j;

				String value = table.get(diff);

				if(value != null){}

				table.put(diff, repres);
			}
		}
	}

	private static void testLambdaExp(){
		Integer[] myList = { 3, 38, 28, 10 };

		Arrays.stream(myList).filter(x -> x > 10).map(x -> Integer.toString(x)).sorted().forEach(System.out::println);
	}

	public static Iterable<Character> asCharIterable(final String s){
		return new Iterable<Character>(){

			int currentIndex = 0;
			final int length = s.length();

			@Override
			public Iterator<Character> iterator(){
				return new Iterator<Character>(){

					@Override
					public boolean hasNext(){
						return currentIndex < length;
					}

					@Override
					public Character next(){
						return s.charAt(currentIndex++);
					}

					@Override
					public void remove(){
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	public static String toBitString(int x){
		StringBuilder stringBuilder = new StringBuilder();

		for(Integer i: util.Lazy.range(32)){
			int last = (x >> (31 - i)) & 1;
			stringBuilder.append((char)(last + '0'));
		}

		return stringBuilder.toString();
	}

	public static int calcPostFix(String postFixText){
		Stack<Integer> stack = new Stack<Integer>();

		for(char c: postFixText.toCharArray()){
			if(util.CharUtil.isInt(c)){
				stack.push(util.CharUtil.toInt(c));
			}
			else{
				int var2 = stack.pop();
				int var1 = stack.pop();

				switch(c){
					case '+':
						stack.push(var1 + var2);
						break;
					case '-':
						stack.push(var1 - var2);
						break;
					case '*':
						stack.push(var1 * var2);
						break;
					case '/':
						stack.push(var1 / var2);
						break;
					default:
						break;
				}
			}
		}

		if(stack.size() == 1)
			return stack.pop();
		else
			throw new java.util.EmptyStackException();
	}

}