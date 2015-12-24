import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;
import sudoku.SudokuSolver;

public class SudokuTest{
	static SudokuSolver solver;
	
	public void init() throws IOException{
		solver = new SudokuSolver("./src/sudoku/solvedSudoku.txt");
	}
	
	@Test
	public void testIsValidColumn() throws IOException{
		init();
		
		assertEquals(solver.isColumnValid(solver.buffer, 0), true);
		assertEquals(solver.isColumnValid(solver.buffer, 1), false);
		assertEquals(solver.isColumnValid(solver.buffer, 2), true);
		assertEquals(solver.isColumnValid(solver.buffer, 3), true);
		assertEquals(solver.isColumnValid(solver.buffer, 4), true);
		assertEquals(solver.isColumnValid(solver.buffer, 5), true);
		assertEquals(solver.isColumnValid(solver.buffer, 6), true);
		assertEquals(solver.isColumnValid(solver.buffer, 7), true);
		assertEquals(solver.isColumnValid(solver.buffer, 8), true);
	}
	
	@Test
	public void testIsValidRow() throws IOException{
		init();
		
		assertEquals(solver.isRowValid(solver.buffer, 0), true);
		assertEquals(solver.isRowValid(solver.buffer, 1), true);
		assertEquals(solver.isRowValid(solver.buffer, 2), true);
		assertEquals(solver.isRowValid(solver.buffer, 3), true);
		assertEquals(solver.isRowValid(solver.buffer, 4), true);
		assertEquals(solver.isRowValid(solver.buffer, 5), true);
		assertEquals(solver.isRowValid(solver.buffer, 6), false);
		assertEquals(solver.isRowValid(solver.buffer, 7), true);
		assertEquals(solver.isRowValid(solver.buffer, 8), true);
	}
	
	@Test
	public void testIsValidSquare() throws IOException{
		init();
		
		assertEquals(solver.isSquareValid(solver.buffer, 0,0), true);
		assertEquals(solver.isSquareValid(solver.buffer, 0,1), true);
		assertEquals(solver.isSquareValid(solver.buffer, 0,2), true);
		assertEquals(solver.isSquareValid(solver.buffer, 1,0), true);
		assertEquals(solver.isSquareValid(solver.buffer, 1,1), true);
		assertEquals(solver.isSquareValid(solver.buffer, 1,2), true);
		assertEquals(solver.isSquareValid(solver.buffer, 2,0), false);
		assertEquals(solver.isSquareValid(solver.buffer, 2,1), true);
		assertEquals(solver.isSquareValid(solver.buffer, 2,2), true);
	}
	
	@Test
	public void testIsValidIndex() throws IOException{
		init();
		
		assertEquals(solver.isValid(solver.buffer, 0), true);
		assertEquals(solver.isValid(solver.buffer, 54), false);
		assertEquals(solver.isValid(solver.buffer, 56), false);
		assertEquals(solver.isValid(solver.buffer, 56), false);
		assertEquals(solver.isValid(solver.buffer, 72), false);
		assertEquals(solver.isValid(solver.buffer, 75), true);
		assertEquals(solver.isValid(solver.buffer, 62), false);
	}
}
