package sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FunctionSolveMethod{

	final static byte EMPTY = '-';
	final static int dim = 9;
	
	public static void main(String[] args) throws IOException{
		byte[] buffer = getSudoku();

		char[][] sudoku = new char[dim][dim];
		
		for(int i = 0; i < dim; i++){
			for(int j = 0; j < dim; j++){
				sudoku[i][j] = (char)buffer[i * dim + j];
			}
		}
		
		printSudoku(sudoku);
		
		boolean result = solveSudoku(sudoku, 0, 0, false);
		
		System.out.println("Result: "+result);
		
		printSudoku(sudoku);
	}
	
	static void printSudoku(char[][] sudoku){
		for(int i = 0; i < dim; i++){
			for(int j = 0; j < dim; j++){
				System.out.print(sudoku[i][j]);
			}
			System.out.print('\n');
		}
		
		System.out.print("\n\n\n");
	}
	
	static boolean solveSudoku(char[][] sudoku, int row, int column, boolean next){
		
		if(next){
			column++;
			
			if(column >= dim){
				column = 0;
				row++;
				
				if(row >= dim)
					return true;
			}
		}
		
		if(sudoku[row][column] != EMPTY){
			return solveSudoku(sudoku, row, column, true);
		}
		else{
			for(char i = '1'; i < '1' + dim; i++){
				if(canPut(i, sudoku, row, column)){
					sudoku[row][column] = i;
					
					if(!solveSudoku(sudoku, row, column, true)){
						sudoku[row][column] = EMPTY;
					}
					else{
						return true;
					}
				}
			}
			
			return false;
		}
	}
	
	static boolean canPut(char val, char[][] sudoku, int row, int column){
		
		for(int i=0; i < dim; i++){
			if(sudoku[row][i] == val)
				return false;
		}
		
		for(int i=0; i < dim; i++){
			if(sudoku[i][column] == val)
				return false;
		}
		
		int gRow = row / 3;
		int gCol = column / 3;
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(sudoku[gRow * 3 + i][gCol * 3 + j] == val)
					return false;
			}
		}
		
		return true;
	}

	static byte[] getSudoku() throws IOException{
		String fileName = "./src/sudoku/sudoku.txt";
		
		byte[] buffer;
		
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);

		buffer = new byte[dim * dim];

		for(int i = 0; i < dim; i++){
			fis.read(buffer, dim * i, dim);
			fis.read();
		}

		fis.read(buffer);
		fis.close();
		
		return buffer;
	}
}
