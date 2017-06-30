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

	public static void test(){
		ArrayList<Integer[]> upColumn = new ArrayList<>();
		upColumn.add(arr(3, 1));
		upColumn.add(arr(2, 1, 1));
		upColumn.add(arr(3, 1, 1));
		upColumn.add(arr(8));
		upColumn.add(arr(3, 3));
		upColumn.add(arr(2, 2, 1, 1));
		upColumn.add(arr(4, 2));
		upColumn.add(arr(8, 1));
		upColumn.add(arr(1, 5));
		upColumn.add(arr(2, 1, 1, 1));

		ArrayList<Integer[]> leftColumn = new ArrayList<>();
		leftColumn.add(arr(4));
		leftColumn.add(arr(6, 1));
		leftColumn.add(arr(8));
		leftColumn.add(arr(4, 3));
		leftColumn.add(arr(1, 1, 1, 3));
		leftColumn.add(arr(1, 1, 1, 2));
		leftColumn.add(arr(1, 3, 3));
		leftColumn.add(arr(1, 5, 1));
		leftColumn.add(arr(1, 3));
		leftColumn.add(arr(1, 1, 1, 1));

		solveAndDisplay(upColumn, leftColumn);
	}

	private static void solveAndDisplay(ArrayList<Integer[]> upColumn, ArrayList<Integer[]> leftColumn){

		int colCount = upColumn.size();
		int rowCount = leftColumn.size();

		int[][] picture = new int[rowCount][colCount];

		solve(picture, upColumn, leftColumn);

		display(picture);
	}

	private static void solve(int[][] picture, ArrayList<Integer[]> upColumn, ArrayList<Integer[]> leftColumn){
		processInitial(picture, upColumn, leftColumn);
		processSingles(picture, upColumn, leftColumn);
	}

	private static void processSingles(int[][] picture, ArrayList<Integer[]> upColumn, ArrayList<Integer[]> leftColumn){
		int rowCount = leftColumn.size();
		int colCount = upColumn.size();

		// Fill in betweens for each column
		for(int col = 0; col < colCount; col++){
			Integer[] values = upColumn.get(col);
			int count = values.length;

			if(count == 1){
				int firstFilled = -1;
				int lastFilled = -1;

				int i;

				for(i = 0; i < rowCount; i++){
					int val = picture[i][col];

					if(val == FILLED){
						firstFilled = i;
						break;
					}
				}

				for(int j = rowCount - 1; j >= i; j--){
					int val = picture[i][col];

					if(val == FILLED){
						lastFilled = i;
						break;
					}
				}

				for(int k = firstFilled + 1; k < lastFilled; k++)
					picture[k][col] = FILLED;
			}
		}

		// Fill in betweens for each row
		for(int row = 0; row < rowCount; row++){
			Integer[] values = leftColumn.get(row);
			int count = values.length;

			if(count == 1){
				int firstFilled = -1;
				int lastFilled = -1;

				int i;

				for(i = 0; i < colCount; i++){
					int val = picture[row][i];

					if(val == FILLED){
						firstFilled = i;
						break;
					}
				}

				for(int j = colCount - 1; j >= i; j--){
					int val = picture[row][i];

					if(val == FILLED){
						lastFilled = i;
						break;
					}
				}

				for(int k = firstFilled + 1; k < lastFilled; k++)
					picture[row][k] = FILLED;
			}
		}
	}

	private static void processInitial(int[][] picture, ArrayList<Integer[]> upColumn, ArrayList<Integer[]> leftColumn){
		int rowCount = leftColumn.size();
		int colCount = upColumn.size();

		for(int col = 0; col < colCount; col++){
			Integer[] values = upColumn.get(col);

			int sum = values.length - 1;

			for(Integer integer: values){
				sum += integer;
			}

			int rem = rowCount - sum;

			int formerSize = 0;
			for(int i = 0; i < values.length; i++){
				Integer integer = values[i];

				int diff = integer - rem;

				if(diff > 0){
					for(int j = 0; j < diff; j++)
						picture[j + rem + formerSize][col] = FILLED;
				}
				formerSize += 1 + integer;
			}
		}

		for(int row = 0; row < rowCount; row++){
			Integer[] values = leftColumn.get(row);

			int sum = values.length - 1;

			for(Integer integer: values){
				sum += integer;
			}

			int rem = colCount - sum;

			int formerSize = 0;
			for(int i = 0; i < values.length; i++){
				Integer integer = values[i];

				int diff = integer - rem;

				if(diff > 0){
					for(int j = 0; j < diff; j++)
						picture[row][j + rem + formerSize] = FILLED;
				}
				formerSize += 1 + integer;
			}
		}
	}

	private static void display(int[][] picture){
		Display panel = new PicrossSolver().new Display(picture);

		JFrame frame = new JFrame("Game of Life");
		frame.setSize(230, 250);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setVisible(true);
		frame.setLayout(null);
	}

	private static Integer[] arr(Integer... values){
		return values;
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
