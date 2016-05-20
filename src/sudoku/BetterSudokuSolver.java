package sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BetterSudokuSolver{

	public byte[][] buffer;
	private final int dim = 9;
	private final char empty = '-';
	private final int subSquare = 3;

	public BetterSudokuSolver(String fileName) throws IOException{
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);

		buffer = new byte[dim][dim];

		for(int i = 0; i < dim; i++){
			fis.read(buffer[i], 0, dim);
			fis.read();
		}

		fis.close();

		print();
	}

	public void solve(){
		if(tryToSolveStarting(buffer, 0, 0)){
			print();
		}
		else{
			System.out.print("Array invalid");
		}
	}

	public static void test() throws IOException{
		long startTime = System.currentTimeMillis();

		BetterSudokuSolver solver9 = new BetterSudokuSolver("./src/sudoku/sudoku.txt");

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

	private void print(){
		for(int i = 0; i < dim; i++){
			for(int y = 0; y < dim; y++)
				System.out.print((char)buffer[i][y]);

			System.out.print('\n');
		}

		System.out.print('\n');
	}

	private boolean tryToSolveStarting(byte[][] array, int row, int col){
		if(array[row][col] != empty){
			if(col < dim - 1)
				return tryToSolveStarting(array, row, col + 1);
			else if(row < dim - 1)
				return tryToSolveStarting(array, row + 1, 0);
			else
				return true;
		}
		else{
			for(byte i = '1'; i < '1' + dim; i++){
				if(canIPut(array, row, col, i)){
					array[row][col] = i;

					boolean isSolved;

					if(col < dim - 1)
						isSolved = tryToSolveStarting(array, row, col + 1);
					else if(row < dim - 1)
						isSolved = tryToSolveStarting(array, row + 1, 0);
					else
						isSolved = true;

					if(isSolved)
						return true;

					array[row][col] = empty;
				}
			}
		}

		return false;
	}

	private boolean canIPut(byte[][] array, int row, int col, byte val){
		int rowStart = row / subSquare * subSquare;
		int colStart = col / subSquare * subSquare;

		for(int i = 0; i < dim; i++){
			if(array[row][i] == val)
				return false;
			if(array[i][col] == val)
				return false;
			if(array[rowStart + i / subSquare][colStart + i % subSquare] == val)
				return false;
		}

		return true;
	}
}
