package casual;

import interfaces.Condition;
import interfaces.Selector;

import java.awt.HeadlessException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.OperationNotSupportedException;

import sudoku.BetterSudokuSolver;
import sudoku.DancingLinks;
import sudoku.SudokuSolver;
import util.CharUtil;
import util.Lazy;
import util.Lambda;
import util.Stream;
import util.StreamUtil;

public class Casual{

	public static void main(String[] args) throws NumberFormatException, HeadlessException,
			IOException, NoSuchAlgorithmException{

		String koor = "-0.23, 574.34";
		
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
		}

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