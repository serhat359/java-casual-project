package sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import casual.Stack;

public class SudokuSolver{

	public byte[] buffer;
	private final int subSquare = 3;
	private final int dim = subSquare * subSquare;
	private final char greatest;
	private final char empty = '-';

	public SudokuSolver(String fileName) throws IOException{
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);

		buffer = new byte[dim * dim];
		this.greatest = (char)('0' + dim);

		for(int i = 0; i < dim; i++){
			fis.read(buffer, dim * i, dim);
			fis.read();
		}

		fis.read(buffer);
		fis.close();

		print();
	}

	public static void print(PrintStream stream, int dim, byte[] buffer){
		printWithNoSpace(stream, dim, buffer);
		printWithSpaces(stream, dim, buffer);
	}

	private static void printWithNoSpace(PrintStream stream, int dim, byte[] buffer){
		for(int i = 0; i < dim; i++){
			for(int y = 0; y < dim; y++)
				stream.print((char)buffer[i * dim + y]);

			stream.print('\n');
		}

		stream.print('\n');
	}

	private static void printWithSpaces(PrintStream stream, int dim, byte[] buffer){
		// printRow
		for(int i = 0, rowSpace = 0; i < dim; i++, rowSpace++){
			for(int y = 0, columnSpace = 0; y < dim; y++, columnSpace++){
				stream.print((char)buffer[i * dim + y]);
				if(columnSpace % 3 == 2)
					stream.print(' ');
			}

			if(rowSpace % 3 == 2)
				stream.print('\n');

			stream.print('\n');
		}

		stream.print('\n');
	}

	
	
	private void print(){
		print(System.out, dim, buffer);
	}

	public void solve(){
		if(!isValid(buffer)){
			throw new RuntimeException("array not valid");
		}

		Stack<Operation> stack = new Stack<>();

		int numberOfSolutions = 0;

		boolean breakAll = false;
		for(int i = getEmpty(buffer);; i = getEmpty(buffer)){
			boolean invalid;

			if(i < 0){
				numberOfSolutions++;
				print();
				invalid = true;
			}
			else{
				buffer[i] = '1';
				stack.push(new Operation(i, '1'));
				invalid = false;
			}

			while(invalid || !isValid(buffer, i)){
				invalid = false;
				Operation lastOp = stack.peek();
				i = lastOp.x;

				if(lastOp.c == greatest){
					stack.pop();
					if(stack.size() == 0){
						breakAll = true;
						break;
					}

					buffer[i] = empty;
					invalid = true;
					continue;
				}
				else{
					lastOp.c++;
					buffer[i]++;
				}
			}

			if(breakAll)
				break;
		}

		System.out.print("Number of solutions: " + numberOfSolutions + "\n");
	}

	public int getEmpty(final byte[] array){
		for(int i = 0; i < dim * dim; i++){
			if(array[i] == empty)
				return i;
		}

		return -1;
	}

	public boolean isValid(final byte[] array, int lastIndex){

		int row = lastIndex / dim;
		int column = lastIndex % dim;

		int regX = row / subSquare;
		int regY = column / subSquare;

		return isRowValid(array, row) && isColumnValid(array, column)
				&& isSquareValid(array, regX, regY);
	}

	public boolean isValid(final byte[] array){
		for(int i = 0; i < dim; i++)
			for(int j = 0; j < dim; j++){
				byte c = array[i * dim + j];
				if((c < '1' || c > '9') && c != empty)
					return false;
			}

		for(int i = 0; i < dim; i++)
			if(!isRowValid(array, i))
				return false;

		for(int i = 0; i < dim; i++)
			if(!isColumnValid(array, i))
				return false;

		for(int i = 0; i < subSquare; i++)
			for(int j = 0; j < subSquare; j++)
				if(!isSquareValid(array, i, j))
					return false;

		return true;
	}

	public boolean isRowValid(final byte[] array, int row){
		Set<Byte> set = new HashSet<>();

		for(int i = row * dim; i < row * dim + dim; i++){
			byte c = array[i];
			if(c != empty && !set.add(c))
				return false;
		}

		return true;
	}

	public boolean isColumnValid(final byte[] array, int column){
		Set<Byte> set = new HashSet<>();

		for(int i = column; i < column + dim * dim; i += dim){
			byte c = array[i];
			if(c != empty && !set.add(c))
				return false;
		}

		return true;
	}

	public boolean isSquareValid(final byte[] array, int regX, int regY){
		Set<Byte> set = new HashSet<>();

		for(int i = 0; i < subSquare; i++){
			for(int j = 0; j < subSquare; j++){
				byte c = array[i + (j * dim) + (regX * dim * subSquare) + (regY * subSquare)];
				if(c != empty && !set.add(c))
					return false;
			}
		}

		return true;
	}

	public static void test() throws IOException{
		long startTime = System.currentTimeMillis();

		SudokuSolver solver9 = new SudokuSolver("./src/sudoku/sudoku.txt");

		try{
			solver9.solve();
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.print("This sudoku is broken");
		}

		long totalMillis = System.currentTimeMillis() - startTime;

		long total = totalMillis / 1000;
		long millis = totalMillis % 1000;

		System.out.println("Calculation time: " + total / 60 + " minutes, " + total % 60
				+ " seconds");
		System.out.println("Plus " + millis + " milliseconds");
	}
}

class Operation{
	int x;
	char c;

	public Operation(int x, char c){
		this.x = x;
		this.c = c;
	}
}
