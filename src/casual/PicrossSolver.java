package casual;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PicrossSolver{
	final static int UNKNOWN = 0;
	final static int FILLED = 1;
	final static int EMPTY = 2;

	final static int rowCount = 10;
	final static int colCount = 10;
	final static int lastRow = rowCount - 1;
	final static int lastCol = colCount - 1;

	private static int iteration = 0;
	private static int[][] pictureRef = null;

	public static void test(){
		int[][] upColumn = new int[colCount][];
		upColumn[0] = (arr(3, 1));
		upColumn[1] = (arr(2, 1, 1));
		upColumn[2] = (arr(3, 1, 1));
		upColumn[3] = (arr(8));
		upColumn[4] = (arr(3, 3));
		upColumn[5] = (arr(2, 2, 1, 1));
		upColumn[6] = (arr(4, 2));
		upColumn[7] = (arr(8, 1));
		upColumn[8] = (arr(1, 5));
		upColumn[9] = (arr(2, 1, 1, 1));

		int[][] leftColumn = new int[rowCount][];
		leftColumn[0] = (arr(4));
		leftColumn[1] = (arr(6, 1));
		leftColumn[2] = (arr(8));
		leftColumn[3] = (arr(4, 3));
		leftColumn[4] = (arr(1, 1, 1, 3));
		leftColumn[5] = (arr(1, 1, 1, 2));
		leftColumn[6] = (arr(1, 3, 3));
		leftColumn[7] = (arr(1, 5, 1));
		leftColumn[8] = (arr(1, 3));
		leftColumn[9] = (arr(1, 1, 1, 1));

		solveAndDisplay(upColumn, leftColumn);
	}

	private static void solveAndDisplay(int[][] upColumn, int[][] leftColumn){
		int[][] picture = new int[rowCount][colCount];

		solve(picture, upColumn, leftColumn);

		display(picture);
	}

	private static void solve(int[][] picture, int[][] upColumn, int[][] leftColumn){
		processInitial(picture, upColumn, leftColumn);

		dumpPicture(picture);

		for(iteration = 0;; iteration++){

			boolean isChangeDetected = false;

			// tek say� olanlar�n ara bo�lu�unu doldurup, ula�amayaca�� yerlere �arp� at�yor
			processSingles(picture, upColumn, leftColumn);
			isChangeDetected |= testPicture(picture);

			// seri ba��ndan ve sonundan itibaren bir taraf� kapal� say�lar�n kalan�n� ayaralay�p �arp� at�yor
			processStartsAndEnds(picture, upColumn, leftColumn);
			isChangeDetected |= testPicture(picture);

			// serilerdeki en b�y�k de�erler dolduysa ba��na ve sonuna �arp� at�yor
			processSetEmptiesByMax(picture, upColumn, leftColumn);
			isChangeDetected |= testPicture(picture);

			// serilerdeki �arp� aras� bo�luklar� bo�lukla dolduruyor
			processFillBetweenEmpties(picture, upColumn, leftColumn);
			isChangeDetected |= testPicture(picture);

			// serideki outlier olan de�ere kar��l�k gelen dolmu�lar� i�liyor
			processMaxValues(picture, upColumn, leftColumn);
			isChangeDetected |= testPicture(picture);

			// serideki �arp�larla ayr�lm�� k�s�mlar� bulup i�liyor
			// processDividedParts(picture, upColumn, leftColumn);
			isChangeDetected |= testPicture(picture);

			if(!isChangeDetected){
				break;
			}
		}

		System.out.println("There was no change after the iteration: " + iteration);
	}

	private static void processDividedParts(int[][] picture, int[][] upColumn, int[][] leftColumn){

		for(int col = 0; col < colCount; col++){

			int[] values = upColumn[col];
			// TODO Auto-generated method stub
		}

		for(int row = 0; row < rowCount; row++){

			int[] values = leftColumn[row];

			ArrayList<Range> dividedParts = new ArrayList<>();

			int nonEmpty = -1;
			for(int i = 0; i < colCount; i++){
				int cell = picture[row][i];

				if(cell != EMPTY && nonEmpty < 0){
					nonEmpty = i;
				}
				else if(cell == EMPTY && i - 1 >= 0 && picture[row][i - 1] != EMPTY && nonEmpty >= 0){
					dividedParts.add(new PicrossSolver().new Range(nonEmpty, i - 1));
					nonEmpty = -1;
				}
			}

			if(nonEmpty > 0){
				dividedParts.add(new PicrossSolver().new Range(nonEmpty, colCount - 1));
				nonEmpty = -1;
			}

			if(dividedParts.size() == values.length){
				int i = 0;
				for(Range range: dividedParts){
					int val = values[i];
					int rangeVal = range.end - range.start + 1;
					int reach = rangeVal - val;

					for(int j = range.start + reach; j < range.end - reach + 1; j++){
						if(j == 10){
							display(picture);
							debug();
						}
						picture[row][j] = FILLED;
					}

					i++;
				}
			}
		}
	}

	private static void processMaxValues(int[][] picture, int[][] upColumn, int[][] leftColumn){
		for(int col = 0; col < colCount; col++){

			int[] values = upColumn[col];

			if(values.length > 1){
				int secondMaxValue = values[0];
				int maxValue = values[0];

				for(int i = 1; i < values.length; i++){
					int val = values[i];

					if(val > maxValue){
						if(maxValue > secondMaxValue)
							secondMaxValue = maxValue;

						maxValue = val;
					}
				}

				int filledIndex = -1;
				int i = 0;

				for(i = 0; i < rowCount; i++){
					int cell = picture[i][col];

					if(cell == FILLED && filledIndex < 0){
						filledIndex = i;
					}
					else if(cell != FILLED && filledIndex >= 0){
						int filledSize = i - filledIndex;
						if(filledSize > secondMaxValue){
							int filledEndIndex = i - 1;

							int leftOfFilled = filledIndex - 1;
							int rightOfFilled = filledEndIndex + 1;
							if(leftOfFilled < 0 || picture[leftOfFilled][col] == EMPTY){
								int k;
								for(k = rightOfFilled; k < filledIndex + maxValue; k++){
									picture[k][col] = FILLED;
								}
								if(k < rowCount)
									picture[k][col] = EMPTY;

								filledIndex = -1;
							}
							else if(rightOfFilled > lastRow || picture[rightOfFilled][col] == EMPTY){
								int k;
								for(k = leftOfFilled; k > filledEndIndex - maxValue; k--){
									picture[k][col] = FILLED;
								}
								if(k > 0)
									picture[k][col] = EMPTY;

								filledIndex = -1;
							}
							else{
								// TODO other process
							}
						}
						else{
							filledIndex = -1;
						}
					}
				}
			}
		}

		for(int row = 0; row < rowCount; row++){

			int[] values = leftColumn[row];

			if(values.length > 1){
				int secondMaxValue = values[0];
				int maxValue = values[0];

				for(int i = 1; i < values.length; i++){
					int val = values[i];

					if(val > maxValue){
						if(maxValue > secondMaxValue)
							secondMaxValue = maxValue;

						maxValue = val;
					}
				}

				int filledIndex = -1;
				int i = 0;

				for(i = 0; i < colCount; i++){
					int cell = picture[row][i];

					if(cell == FILLED && filledIndex < 0){
						filledIndex = i;
					}
					else if(cell != FILLED && filledIndex >= 0){
						int filledSize = i - filledIndex;
						if(filledSize > secondMaxValue){
							int filledEndIndex = i - 1;

							int leftOfFilled = filledIndex - 1;
							int rightOfFilled = filledEndIndex + 1;
							if(leftOfFilled < 0 || picture[row][leftOfFilled] == EMPTY){
								int k;
								for(k = rightOfFilled; k < filledIndex + maxValue; k++){
									picture[row][k] = FILLED;
								}
								if(k < colCount)
									picture[row][k] = EMPTY;

								filledIndex = -1;
							}
							else if(rightOfFilled > lastCol || picture[row][rightOfFilled] == EMPTY){
								int k;
								for(k = leftOfFilled; k > filledEndIndex - maxValue; k--){
									picture[row][k] = FILLED;
								}
								if(k > 0)
									picture[row][k] = EMPTY;

								filledIndex = -1;
							}
							else{
								// TODO other process
							}
						}
						else{
							filledIndex = -1;
						}
					}
				}
			}
		}
	}

	private static boolean testPicture(int[][] picture){
		for(int i = 0; i < rowCount; i++)
			for(int j = 0; j < colCount; j++)
				if(pictureRef[i][j] != UNKNOWN && pictureRef[i][j] != picture[i][j]){
					display(pictureRef, "Old One");
					display(picture);
					throw new RuntimeException("Bi �nceki metot yanl�� �al���yor: " + iteration);
				}

		boolean isChangeDetected = false;

		for(int i = 0; i < rowCount; i++){
			for(int j = 0; j < colCount; j++){
				if(pictureRef[i][j] != picture[i][j]){
					isChangeDetected = true;
					break;
				}
			}

			if(isChangeDetected)
				break;
		}

		dumpPicture(picture);

		return isChangeDetected;
	}

	private static void dumpPicture(int[][] picture){
		pictureRef = new int[rowCount][colCount];

		for(int i = 0; i < rowCount; i++)
			for(int j = 0; j < colCount; j++)
				pictureRef[i][j] = picture[i][j];
	}

	private static void processFillBetweenEmpties(int[][] picture, int[][] upColumn, int[][] leftColumn){
		for(int col = 0; col < colCount; col++){

			int[] values = upColumn[col];

			// TODO generate table for this
			int minValue = getMinValue(values);

			int lastEmptyIndex = -1;

			for(int i = 0; i < rowCount; i++){
				int cell = picture[i][col];

				if(cell == FILLED)
					lastEmptyIndex = -1;
				if(cell == EMPTY){
					if(lastEmptyIndex >= 0 && i - lastEmptyIndex > 1 && i - lastEmptyIndex - 1 < minValue){
						for(int k = lastEmptyIndex + 1; k < i; k++)
							picture[k][col] = EMPTY;
					}

					lastEmptyIndex = i;
				}
			}
		}

		for(int row = 0; row < rowCount; row++){

			int[] values = leftColumn[row];

			// TODO generate table for this
			int minValue = getMinValue(values);

			int lastEmptyIndex = -1;

			for(int i = 0; i < colCount; i++){
				int cell = picture[row][i];

				if(cell == FILLED)
					lastEmptyIndex = -1;
				if(cell == EMPTY){
					if(lastEmptyIndex >= 0 && i - lastEmptyIndex > 1 && i - lastEmptyIndex - 1 < minValue){
						for(int k = lastEmptyIndex + 1; k < i; k++)
							picture[row][k] = EMPTY;
					}

					lastEmptyIndex = i;
				}
			}
		}
	}

	private static void processSetEmptiesByMax(int[][] picture, int[][] upColumn, int[][] leftColumn){
		for(int col = 0; col < colCount; col++){
			int[] values = upColumn[col];

			// TODO generate table for this
			int maxValue = getMaxValue(values);

			int first = -1;

			for(int i = 0; i < rowCount; i++){
				if(picture[i][col] != FILLED)
					continue;
				else{
					first = i;

					int last = i;

					for(i++; i < rowCount; i++){
						if(picture[i][col] == FILLED)
							last = i;
						else
							break;
					}

					int size = last - first + 1;
					if(size == maxValue){
						int leftEmptyRow = first - 1;
						int rightEmptyRow = last + 1;

						if(leftEmptyRow >= 0)
							picture[leftEmptyRow][col] = EMPTY;

						if(rightEmptyRow <= lastRow)
							picture[rightEmptyRow][col] = EMPTY;
					}

					i--;
				}
			}
		}

		for(int row = 0; row < rowCount; row++){
			int[] values = leftColumn[row];

			// TODO generate table for this
			int maxValue = getMaxValue(values);

			int first = -1;

			for(int i = 0; i < colCount; i++){
				if(picture[row][i] != FILLED)
					continue;
				else{
					first = i;

					int last = i;

					for(i++; i < colCount; i++){
						if(picture[row][i] == FILLED)
							last = i;
						else
							break;
					}

					int size = last - first + 1;
					if(size == maxValue){
						int leftEmptyCol = first - 1;
						int rightEmptyCol = last + 1;

						if(leftEmptyCol >= 0)
							picture[row][leftEmptyCol] = EMPTY;

						if(rightEmptyCol <= lastCol)
							picture[row][rightEmptyCol] = EMPTY;
					}

					i--;
				}
			}
		}
	}

	private static int getMinValue(int[] values){
		int minIndex = 0;
		int minValue = values[minIndex];

		for(int i = 1; i < values.length; i++){
			if(values[i] < minValue){
				minIndex = i;
				minValue = values[minIndex];
			}
		}

		return minValue;
	}

	private static int getMaxValue(int[] values){
		int maxIndex = 0;
		int maxValue = values[maxIndex];

		for(int i = 1; i < values.length; i++){
			if(values[i] > maxValue){
				maxIndex = i;
				maxValue = values[maxIndex];
			}
		}

		return maxValue;
	}

	private static void processStartsAndEnds(int[][] picture, int[][] upColumn, int[][] leftColumn){

		for(int col = 0; col < colCount; col++){

			int[] values = upColumn[col];
			int valuesIndex = 0;

			int i;
			for(i = 0; i < rowCount; i++){
				int cell = picture[i][col];

				if(cell == UNKNOWN){
					break;
				}
				else if(cell == FILLED){

					int val = values[valuesIndex++];
					int max = i + val;

					for(; i < max; i++){
						picture[i][col] = FILLED;
					}

					if(i < rowCount)
						picture[i][col] = EMPTY;

				}
				else if(cell == EMPTY){

				}
			}

			// Below is for checking remaining unknown cells
			boolean isAllUnknown = true;
			for(int j = i; j < rowCount; j++)
				if(picture[j][col] != UNKNOWN){
					isAllUnknown = false;
					break;
				}

			// isAllUnknown below
			if(isAllUnknown){
				int unknownSize = rowCount - i;
				int valuesNewIndex = valuesIndex;

				int sum = values.length - 1 - valuesIndex;
				for(int j = valuesIndex; j < values.length; j++)
					sum += values[j];

				// TODO this needs range process optimization
				if(unknownSize > 0 && sum == unknownSize){
					for(int rowIndex = i, j = valuesNewIndex; j < values.length; j++, rowIndex++){
						int val = values[j];

						if(rowIndex - 1 >= 0)
							picture[rowIndex - 1][col] = EMPTY;

						for(int k = 0; k < val; k++)
							picture[rowIndex++][col] = FILLED;
					}
				}
				else if(unknownSize > 0 && valuesNewIndex == -1){
					for(int k = 0; k <= i; k++)
						picture[k][col] = EMPTY;
				}
			}

			// Above is regular iteration
			// Below is for reverse iteration

			valuesIndex = values.length - 1;

			for(i = rowCount - 1; i >= 0; i--){
				int cell = picture[i][col];

				if(cell == UNKNOWN){
					break;
				}
				else if(cell == FILLED){

					int val = values[valuesIndex--];
					int max = i - val;

					for(; i > max; i--){
						picture[i][col] = FILLED;
					}

					if(i >= 0)
						picture[i][col] = EMPTY;

				}
				else if(cell == EMPTY){

				}
			}

			// Below is for checking remaining unknown cells
			isAllUnknown = true;
			for(int j = 0; j <= i; j++)
				if(picture[j][col] != UNKNOWN){
					isAllUnknown = false;
					break;
				}

			// isAllUnknown below
			if(isAllUnknown){
				int unknownSize = i + 1;
				int valuesNewIndex = valuesIndex;

				int sum = valuesIndex;
				for(int j = 0; j <= valuesNewIndex; j++)
					sum += values[j];

				// TODO this needs range process optimization
				if(unknownSize > 0 && sum == unknownSize){
					for(int rowIndex = 0, j = 0; j <= valuesNewIndex; j++, rowIndex++){
						int val = values[j];

						if(rowIndex - 1 >= 0)
							picture[rowIndex - 1][col] = EMPTY;

						for(int k = 0; k < val; k++)
							picture[rowIndex++][col] = FILLED;
					}
				}
				else if(unknownSize > 0 && valuesNewIndex == -1){
					for(int k = 0; k <= i; k++)
						picture[k][col] = EMPTY;
				}
			}
		}

		for(int row = 0; row < rowCount; row++){

			int[] values = leftColumn[row];
			int valuesIndex = 0;

			int i;
			for(i = 0; i < colCount; i++){
				int cell = picture[row][i];

				if(cell == UNKNOWN){
					break;
				}
				else if(cell == FILLED){

					int val = values[valuesIndex++];
					int max = i + val;

					for(; i < max; i++){
						picture[row][i] = FILLED;
					}

					if(i < rowCount)
						picture[row][i] = EMPTY;

				}
				else if(cell == EMPTY){

				}
			}

			// Below is for checking remaining unknown cells
			boolean isAllUnknown = true;
			for(int j = i; j < colCount; j++)
				if(picture[row][j] != UNKNOWN){
					isAllUnknown = false;
					break;
				}

			// isAllUnknown below
			if(isAllUnknown){
				int unknownSize = colCount - i;
				int valuesNewIndex = valuesIndex;

				int sum = values.length - 1 - valuesIndex; // CHANGE! sum = length - 1
				for(int j = valuesIndex; j < values.length; j++)
					sum += values[j];

				// TODO this needs range process optimization
				if(unknownSize > 0 && sum == unknownSize){
					for(int colIndex = i, j = valuesNewIndex; j < values.length; j++, colIndex++){ // CHANGE colIndex++
						int val = values[j];

						if(colIndex - 1 >= 0)
							picture[row][colIndex - 1] = EMPTY;

						for(int k = 0; k < val; k++)
							picture[row][colIndex++] = FILLED;
					}
				}
				else if(unknownSize > 0 && valuesNewIndex == -1){
					for(int k = 0; k <= i; k++)
						picture[row][k] = EMPTY;
				}
			}

			// Above is regular iteration
			// Below is for reverse iteration

			valuesIndex = values.length - 1;

			for(i = colCount - 1; i >= 0; i--){
				int cell = picture[row][i];

				if(cell == UNKNOWN){
					break;
				}
				else if(cell == FILLED){

					int val = values[valuesIndex--];
					int max = i - val;

					for(; i > max; i--){
						picture[row][i] = FILLED;
					}

					if(i >= 0)
						picture[row][i] = EMPTY;

				}
				else if(cell == EMPTY){

				}
			}

			// Below is for checking remaining unknown cells
			isAllUnknown = true;
			for(int j = 0; j <= i; j++)
				if(picture[row][j] != UNKNOWN){
					isAllUnknown = false;
					break;
				}

			// isAllUnknown below
			if(isAllUnknown){
				int unknownSize = i + 1;
				int valuesNewIndex = valuesIndex;

				int sum = valuesIndex; // CHANGE! remove -1
				for(int j = 0; j <= valuesNewIndex; j++) // CHANGE! put <=
					sum += values[j];

				// TODO this needs range process optimization
				if(unknownSize > 0 && sum == unknownSize){
					for(int colIndex = 0, j = 0; j <= valuesNewIndex; j++, colIndex++){ // CHANGE colIndex++ added
						int val = values[j];

						if(colIndex - 1 >= 0)
							picture[row][colIndex - 1] = EMPTY;

						for(int k = 0; k < val; k++)
							picture[row][colIndex++] = FILLED;
					}
				}
				else if(unknownSize > 0 && valuesNewIndex == -1){
					for(int k = 0; k <= i; k++)
						picture[row][k] = EMPTY;
				}
			}
		}
	}

	private static void processSingles(int[][] picture, int[][] upColumn, int[][] leftColumn){
		// Fill in betweens for each column
		for(int col = 0; col < colCount; col++){
			int[] values = upColumn[col];
			int count = values.length;

			if(count == 1){
				int firstFilled = -1;
				int lastFilled = -1;

				int i;

				for(i = 0; i < rowCount; i++){
					int cell = picture[i][col];

					if(cell == FILLED){
						firstFilled = i;
						break;
					}
				}

				for(int j = lastRow; j >= i; j--){
					int cell = picture[i][col];

					if(cell == FILLED){
						lastFilled = i;
						break;
					}
				}

				for(int k = firstFilled + 1; k < lastFilled; k++)
					picture[k][col] = FILLED;

				// Above is for filling in betweens
				// Below is for finding reaching

				if(firstFilled >= 0){
					int filledSize = lastFilled - firstFilled + 1;
					int reach = values[0] - filledSize;

					int marginStart = reach - firstFilled;
					if(marginStart > 0){
						for(int k = 0; k < marginStart; k++){
							picture[firstFilled + 1 + k][col] = FILLED;
						}
					}
					else
						marginStart = 0;

					int marginEnd = lastFilled + reach - lastRow;
					if(marginEnd > 0){
						for(int k = 0; k < marginEnd; k++){
							picture[lastFilled - 1 - k][col] = FILLED;
						}
					}
					else
						marginEnd = 0;

					// Above is for filling reaching
					// Below is for setting empties

					// Update variables
					firstFilled -= marginEnd;
					lastFilled += marginStart;
					filledSize += marginEnd + marginStart;
					reach = values[0] - filledSize;

					for(int k = 0; k < firstFilled - reach; k++)
						picture[k][col] = EMPTY;

					for(int k = lastFilled + reach + 1; k < rowCount; k++)
						picture[k][col] = EMPTY;
				}
			}
		}

		// Fill in betweens for each row
		for(int row = 0; row < rowCount; row++){
			int[] values = leftColumn[row];
			int count = values.length;

			if(count == 1){
				int firstFilled = -1;
				int lastFilled = -1;

				int i;

				for(i = 0; i < colCount; i++){
					int cell = picture[row][i];

					if(cell == FILLED){
						firstFilled = i;
						break;
					}
				}

				for(int j = lastCol; j >= i; j--){
					int cell = picture[row][i];

					if(cell == FILLED){
						lastFilled = i;
						break;
					}
				}

				for(int k = firstFilled + 1; k < lastFilled; k++)
					picture[row][k] = FILLED;

				// Above is for filling in betweens
				// Below is for finding reaching

				if(firstFilled >= 0){
					int filledSize = lastFilled - firstFilled + 1;
					int reach = values[0] - filledSize;

					int marginStart = reach - firstFilled;
					if(marginStart > 0){
						for(int k = 0; k < marginStart; k++){
							picture[row][firstFilled + 1 + k] = FILLED;
						}
					}
					else
						marginStart = 0;

					int marginEnd = lastFilled + reach - lastCol;
					if(marginEnd > 0){
						for(int k = 0; k < marginEnd; k++){
							picture[row][lastFilled - 1 - k] = FILLED;
						}
					}
					else
						marginEnd = 0;

					// Above is for filling reaching
					// Below is for setting empties

					// Update variables
					firstFilled -= marginEnd;
					lastFilled += marginStart;
					filledSize += marginEnd + marginStart;
					reach = values[0] - filledSize;

					for(int k = 0; k < firstFilled - reach; k++)
						picture[row][k] = EMPTY;

					for(int k = lastFilled + reach + 1; k < colCount; k++)
						picture[row][k] = EMPTY;
				}

			}
		}
	}

	private static void processInitial(int[][] picture, int[][] upColumn, int[][] leftColumn){
		for(int col = 0; col < colCount; col++){
			int[] values = upColumn[col];

			int sum = values.length - 1;

			for(int elem: values){
				sum += elem;
			}

			int rem = rowCount - sum;

			int formerSize = 0;
			for(int i = 0; i < values.length; i++){
				int elem = values[i];

				int diff = elem - rem;

				if(diff > 0){
					for(int j = 0; j < diff; j++)
						picture[j + rem + formerSize][col] = FILLED;
				}
				formerSize += 1 + elem;
			}
		}

		for(int row = 0; row < rowCount; row++){
			int[] values = leftColumn[row];

			int sum = values.length - 1;

			for(int elem: values){
				sum += elem;
			}

			int rem = colCount - sum;

			int formerSize = 0;
			for(int i = 0; i < values.length; i++){
				int elem = values[i];

				int diff = elem - rem;

				if(diff > 0){
					for(int j = 0; j < diff; j++)
						picture[row][j + rem + formerSize] = FILLED;
				}
				formerSize += 1 + elem;
			}
		}
	}

	private static void display(int[][] picture){
		display(picture, "Latest");
	}

	private static void display(int[][] picture, String title){
		Display panel = new PicrossSolver().new Display(picture);

		JFrame frame = new JFrame(title);
		frame.setSize(230, 250);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setVisible(true);
		frame.setLayout(null);
	}

	private static int[] arr(int... values){
		return values;
	}

	// TODO remove this function when it's over
	private static void debug(){
		System.currentTimeMillis();
	}

	class Range{
		public int start;
		public int end;

		public Range(int start, int end){
			super();
			this.start = start;
			this.end = end;
		}

		public String toString(){
			return "{start: " + start + ", end: " + end + "}";
		}
	}

	class Display extends JPanel{
		private static final long serialVersionUID = 4182312706498893369L;
		private int[][] picture;

		public Display(int[][] picture){
			this.picture = picture;
		}

		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D)g;

			int size = 20;
			int quarter = size / 4;
			int threeQuarter = quarter + 2 * quarter;

			for(int row = 0; row < picture.length; row++){

				for(int col = 0; col < picture[0].length; col++){

					int value = picture[row][col];

					if(value == UNKNOWN){
						Color color = Color.WHITE;
						g2d.setColor(color);
						g2d.fillRect(col * size, row * size, size, size);
					}
					else if(value == FILLED){
						Color color = Color.BLACK;
						g2d.setColor(color);
						g2d.fillRect(col * size, row * size, size, size);
					}
					else if(value == EMPTY){
						g2d.setColor(Color.DARK_GRAY);
						int xOffset = col * size;
						int yOffset = row * size;
						g2d.drawLine(xOffset + quarter, yOffset + quarter, xOffset + threeQuarter,
								yOffset + threeQuarter);
						g2d.drawLine(xOffset + quarter, yOffset + threeQuarter, xOffset + threeQuarter,
								yOffset + quarter);
					}
					else
						throw new RuntimeException("Bakana!");
				}
			}
		}
	}
}
