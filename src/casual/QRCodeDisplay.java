package casual;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class QRCodeDisplay extends JPanel{

	private static final long serialVersionUID = -9017741391692186328L;
	static int width = 450;
	static int height = 450;
	final static int pixelWidth = 16;

	Cell[][] cells;

	public QRCodeDisplay(boolean[][] matrix){
		int rows = matrix.length;
		int columns = matrix[0].length;
		this.cells = new Cell[rows][columns];

		final int cellWidth = width / columns;
		final int cellHeight = height / rows;

		for(int y = 0; y < matrix.length; y++){
			for(int x = 0; x < matrix[0].length; x++){
				cells[x][y] = new Cell(cellWidth * y, cellHeight * x, cellWidth, cellHeight,
						matrix[x][y], matrix, x, y);
			}
		}
	}

	public static void test() throws IOException{

		BufferedImage img = ImageIO.read(new File("e2E393.bmp"));

		int rows = img.getHeight() / pixelWidth;
		int columns = img.getWidth() / pixelWidth;

		boolean[][] matrix = new boolean[rows][columns];

		for(int x = 0; x < rows; x++){
			for(int y = 0; y < columns; y++){
				int color = img.getRGB(y * pixelWidth, x * pixelWidth);
				matrix[x][y] = color == 0xff000000;
			}
		}

		QRCodeDisplay panel = new QRCodeDisplay(matrix);

		JFrame frame = new JFrame("Game of Life");
		frame.setSize(width + 16, height + 38);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setVisible(true);
		frame.setLayout(null);
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D)g;

		for(Cell[] cellArray: cells){
			for(Cell cell: cellArray){
				cell.paint(g2d);
			}
		}
	}

	public enum Way{
		top(1, 0), bottom(-1, 0), right(0, -1), left(0, 1), topright(1, -1), bottomright(-1, -1), topleft(
				1, 1), bottomleft(-1, 1);

		public final int topValue;
		public final int leftValue;

		private Way(int top, int left){
			this.topValue = top;
			this.leftValue = left;
		}
	}

	class Cell extends Rectangle{
		private static final long serialVersionUID = -8764098716962032075L;
		private final boolean[][] baseGrid;
		private final boolean isFilled;
		private final int rIndex;
		private final int cIndex;
		private final Color filledColor = new Color(0, 0, 0);

		public Cell(int x, int y, int width, int height, boolean isFilled, boolean[][] baseGrid,
				int rIndex, int cIndex){
			this.width = width;
			this.height = height;
			this.isFilled = isFilled;
			this.x = x;
			this.y = y;
			this.baseGrid = baseGrid;
			this.rIndex = rIndex;
			this.cIndex = cIndex;
		}

		public void paint(Graphics2D g2d){
			Color topLeftColor;
			Color topRightColor;
			Color bottomLeftColor;
			Color bottomRightColor;

			if(isFilled){ // isBlack
				// handle top left
				{
					Color color;

					if(isFilled(Way.top) || isFilled(Way.left) || isFilled(Way.topleft)){
						color = getColor();
					}
					else{
						color = getOppositeColor();
					}

					topLeftColor = color;
				}

				// handle top right
				{
					Color color;

					if(isFilled(Way.top) || isFilled(Way.right) || isFilled(Way.topright)){
						color = getColor();
					}
					else{
						color = getOppositeColor();
					}

					topRightColor = color;
				}

				// handle bottom left
				{
					Color color;

					if(isFilled(Way.bottom) || isFilled(Way.left) || isFilled(Way.bottomleft)){
						color = getColor();
					}
					else{
						color = getOppositeColor();
					}

					bottomLeftColor = color;
				}

				// handle bottom right
				{
					Color color;

					if(isFilled(Way.bottom) || isFilled(Way.right) || isFilled(Way.bottomright)){
						color = getColor();
					}
					else{
						color = getOppositeColor();
					}

					bottomRightColor = color;
				}
			}
			else{ // isWhite

				// handle top left
				{
					Color color;

					if(isFilled(Way.top) && isFilled(Way.left)){
						color = getOppositeColor();
					}
					else{
						color = getColor();
					}

					topLeftColor = color;
				}

				// handle top right
				{
					Color color;

					if(isFilled(Way.top) && isFilled(Way.right)){
						color = getOppositeColor();
					}
					else{
						color = getColor();
					}

					topRightColor = color;
				}

				// handle bottom left
				{
					Color color;

					if(isFilled(Way.bottom) && isFilled(Way.left)){
						color = getOppositeColor();
					}
					else{
						color = getColor();
					}

					bottomLeftColor = color;
				}

				// handle bottom right
				{
					Color color;

					if(isFilled(Way.bottom) && isFilled(Way.right)){
						color = getOppositeColor();
					}
					else{
						color = getColor();
					}

					bottomRightColor = color;
				}
			}

			g2d.setColor(topLeftColor);
			g2d.fillRect(x, y, width / 2, height / 2);

			g2d.setColor(topRightColor);
			g2d.fillRect(x + width / 2, y, width / 2, height / 2);

			g2d.setColor(bottomLeftColor);
			g2d.fillRect(x, y + height / 2, width / 2, height / 2);

			g2d.setColor(bottomRightColor);
			g2d.fillRect(x + width / 2, y + height / 2, width / 2, height / 2);

			g2d.setColor(getColor());
			g2d.fillOval(this.x, this.y, this.width, this.height);
		}

		private boolean isFilled(Way way){
			int x = cIndex - way.leftValue;
			int y = rIndex - way.topValue;

			return inBound(y, baseGrid.length) && inBound(x, baseGrid[0].length) && baseGrid[y][x];
		}

		private boolean inBound(int number, int high){
			return number >= 0 && number < high;
		}

		private Color getColor(){
			return this.isFilled ? filledColor : invert(filledColor);
		}

		private Color getOppositeColor(){
			return this.isFilled ? invert(filledColor) : filledColor;
		}

		private Color invert(Color color){
			Color textColor = new Color(255 - color.getRed(), 255 - color.getGreen(),
					255 - color.getBlue());

			return textColor;
		}
	}
}
