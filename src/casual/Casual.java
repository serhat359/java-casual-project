package casual;

import gui.NoLayoutFrame;
import interfaces.*;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.naming.OperationNotSupportedException;
import javax.swing.JFrame;
import javax.swing.JPanel;

import sudoku.*;
import util.CharUtil;
//import util.*;
import util.Lazy;

public class Casual{

	public static void main(String[] args)
			throws NumberFormatException, HeadlessException, IOException, NoSuchAlgorithmException{

		/*String koor = "-0.23, 574.34";
		
		String numberPattern = "(-)?\\d+(\\.\\d+)?";
		String wholePattern = numberPattern + "\\s*(,)\\s*" + numberPattern;
		
		boolean match = koor.matches(wholePattern);
		System.out.println(match);
		
		if(match){
			Pattern p = Pattern.compile(numberPattern);
			Matcher m = p.matcher(koor);
			while(m.find()){
				System.out.println(m.group() + " -> " + parseToCoordinate(m.group()));
			}
		}
		
		if(koor.matches(wholePattern)){
			Pattern p = Pattern.compile(numberPattern);
			Matcher m = p.matcher(koor);
			
			m.find();
			double lat = Double.parseDouble(m.group());
			m.find();
			double lng = Double.parseDouble(m.group());
			
			Printer.print(lat);
			Printer.print(", ");
			Printer.print(lng);
		}*/

		// CuttingRod.test();

		// InsertionSort.test();

		// FibGen.test();

		// System.out.print(calcPostFix("2352-*7/+2-822/*+"));

		// Parser.test();

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
		
		
	}

	private static void getGeometricTriangles(){
		HashMap<Integer, String> set = new HashMap<>();
		
		for(int i = 2; i < 2000 ; i++){
			int iSq = i*i;
			
			for(int j = i - 1; j > 0; j--){
				int diff = iSq - j*j;
				
				String repres = i + " - " + j;
				
				String value = set.get(diff);
				
				if(value != null){
					System.out.println(value + " and " + repres + " are matches");
				}
				
				set.put(diff, repres);
			}
		}
	}

	private static void testLambdaExp(){
		Integer[] myList = { 3, 38, 28, 10 };

		Arrays.stream(myList).filter(x -> x > 10).map(x -> Integer.toString(x)).sorted()
				.forEach(System.out::println);
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

		for(Integer i: Lazy.range(32)){
			int last = (x >> (31 - i)) & 1;
			stringBuilder.append((char)(last + '0'));
		}

		return stringBuilder.toString();
	}

	public static int calcPostFix(String postFixText){
		Stack<Integer> stack = new Stack<Integer>();

		for(char c: postFixText.toCharArray()){
			if(CharUtil.isInt(c)){
				stack.push(CharUtil.toInt(c));
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

	public static double parseToCoordinate(String s){
		try{
			return Double.parseDouble(s);
		}
		catch(NumberFormatException e){
			if(s.contains(","))
				return Double.parseDouble(s.replaceAll(",", "."));
			else if(s.contains(" ")){
				String[] sub = s.split(" ");
				double result = Double.parseDouble(sub[0]);
				result %= 360;
				// TODO
				return result;
			}
			return Double.NaN;
		}
	}
}