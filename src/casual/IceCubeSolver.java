package casual;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.CharUtil;
import util.StreamUtil;

public class IceCubeSolver{

	private final JaggedArray<Character> array;

	private final char iceMarker = 'I';
	private final char buttonMarker = 'B';

	private enum Direction{
		Up(-1, 0), Down(1, 0), Left(0, -1), Right(0, 1);

		public final int row;
		public final int column;

		private Direction(int row, int column){
			this.row = row;
			this.column = column;
		}

		private Direction oppositeDirection(){
			switch(this){
				case Up:
					return Direction.Down;
				case Down:
					return Direction.Up;
				case Left:
					return Direction.Right;
				case Right:
					return Direction.Left;
				default:
					throw new RuntimeException("Direction not found");
			}
		}

		private Direction clockwiseDirection(){
			switch(this){
				case Up:
					return Direction.Right;
				case Down:
					return Direction.Left;
				case Left:
					return Direction.Up;
				case Right:
					return Direction.Down;
				default:
					throw new RuntimeException("Direction not found");
			}
		}

		private Direction counterClockwiseDirection(){
			return this.clockwiseDirection().oppositeDirection();
		}
	}

	public IceCubeSolver(String fileName) throws IOException{
		array = getArray(fileName);

		Point buttonLocation = findButton(array.getInternaList());

		List<Pair<Point, Direction>> iceCubes = getSurroundingCubes(array.getInternaList(),
				buttonLocation);

		Point point = null;
		for(Pair<Point, Direction> pair: iceCubes){
			point = searchSolution(pair.first, pair.second.oppositeDirection());

			if(point != null)
				break;
		}

		if(point != null)
			Printer.print("Solution found: x=" + point.x + ", y=" + point.y);
		else
			Printer.print("No solution found");
	}

	private Point searchSolution(Point startingPoint, Direction searchDirection){
		Point currentPoint = startingPoint;

		try{
			while(true){
				currentPoint = getNextPoint(currentPoint, searchDirection);

				Character c = array.get(currentPoint);

				if(c != null && c == iceMarker)
					return currentPoint;
			}
		}
		catch(IndexOutOfBoundsException e){
			List<Point> leftPoints = getLeftPoints(startingPoint, searchDirection);

			List<Point> rightPoints = getRightPoints(startingPoint, searchDirection);

			for(Point point: leftPoints){
				Point solutionPoint = searchSolution(point, searchDirection.clockwiseDirection());

				if(solutionPoint != null)
					return solutionPoint;
			}

			for(Point point: rightPoints){
				Point solutionPoint = searchSolution(point,
						searchDirection.counterClockwiseDirection());

				if(solutionPoint != null)
					return solutionPoint;
			}

			return null;
		}
	}

	private List<Point> getLeftPoints(Point point, Direction direction){
		Direction leftDirection = direction.counterClockwiseDirection();

		Point leftPoint = getNextPoint(point, leftDirection);

		return getIceCubes(leftPoint, direction);
	}

	private List<Point> getRightPoints(Point point, Direction direction){
		Direction rightDirection = direction.clockwiseDirection();

		Point rightPoint = getNextPoint(point, rightDirection);

		return getIceCubes(rightPoint, direction);
	}

	private List<Point> getIceCubes(Point point, Direction direction){
		List<Point> pointList = new ArrayList<>();

		try{
			while(true){
				point = getNextPoint(point, direction);

				if(array.get(point) == iceMarker)
					pointList.add(point);
			}
		}
		catch(IndexOutOfBoundsException e){
			return pointList;
		}
	}

	private Point getNextPoint(Point startingPoint, Direction direction){
		return new Point(startingPoint.x + direction.row, startingPoint.y + direction.column);
	}

	private List<Pair<Point, Direction>> getSurroundingCubes(final List<List<Character>> array,
			Point point){
		List<Pair<Point, Direction>> list = new ArrayList<>();

		Point left = new Point(point.x, point.y - 1);
		Point right = new Point(point.x, point.y + 1);
		Point up = new Point(point.x - 1, point.y);
		Point down = new Point(point.x + 1, point.y);

		addIfIce(left, array, list, Direction.Left);
		addIfIce(right, array, list, Direction.Right);
		addIfIce(up, array, list, Direction.Up);
		addIfIce(down, array, list, Direction.Down);

		return list;
	}

	private void addIfIce(Point p, final List<List<Character>> array,
			final List<Pair<Point, Direction>> list, Direction direction){
		Character c = safeGetChar(array, p.x, p.y);

		if(CharUtil.equals(c, iceMarker))
			list.add(new Pair<Point, IceCubeSolver.Direction>(p, direction));
	}

	private Point findButton(final List<List<Character>> array){
		Point point = null;

		for(int row = 0; row < array.size(); row++){
			List<Character> list = array.get(row);

			for(int column = 0; column < list.size(); column++){
				if(list.get(column) == buttonMarker){
					if(point == null)
						point = new Point(row, column);
					else
						throw new RuntimeException("More than one button found");
				}
			}
		}

		if(point == null)
			throw new RuntimeException("Button not found");
		else
			return point;
	}

	private JaggedArray<Character> getArray(String fileName) throws IOException{
		List<List<Character>> array = new ArrayList<>();

		FileInputStream fis = new FileInputStream(fileName);

		while(true){
			List<Character> list = StreamUtil.GetLine(fis);

			if(list != null){
				array.add(list);
			}
			else{
				break;
			}
		}

		fis.close();

		return new JaggedArray<Character>(array);
	}

	private Character safeGetChar(final List<List<Character>> array, int row, int column){
		if(row >= 0 && row < array.size()){
			List<Character> list = array.get(row);

			if(column >= 0 && column < list.size())
				return list.get(column);
		}

		return null;
	}

	private class JaggedArray<T>{
		private List<List<T>> array;
		private int columnCount;

		public JaggedArray(List<List<T>> array){
			this.array = array;
			this.columnCount = this.countColumns();
		}

		public int rowCount(){
			return array.size();
		}

		public int columnCount(){
			return columnCount;
		}

		public List<List<T>> getInternaList(){
			return array;
		}

		public T get(int row, int column){
			if(row >= rowCount() || column >= columnCount() || row < 0 || column < 0)
				throw new IndexOutOfBoundsException();

			try{
				return array.get(row).get(column);
			}
			catch(IndexOutOfBoundsException ex){
				return null;
			}
		}

		public T get(Point point){
			return get(point.x, point.y);
		}

		private int countColumns(){
			int i = 0;

			for(List<T> list: array){
				i = Math.max(i, list.size());
			}

			return i;
		}
	}
}
