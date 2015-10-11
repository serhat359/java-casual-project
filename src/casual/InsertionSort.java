package casual;

import java.util.Comparator;

public class InsertionSort{

	// Private Constructor
	private InsertionSort(){
	}

	public static enum Mode{
		ASCENDING, DESCENDING;
	}

	public static <T extends Comparable<T>>void sort(T[] array){
		sort(array, Mode.ASCENDING);
	}

	public static <T>void sort(T[] array, Comparator<T> comparator){
		sort(array, comparator, Mode.ASCENDING);
	}

	public static <T extends Comparable<T>>void sort(T[] array, Mode mode){
		Comparator<T> comparator = new Comparator<T>(){
			@Override
			public int compare(T o1, T o2){
				return o1.compareTo(o2);
			}
		};

		sort(array, comparator, mode);
	}

	public static <T>void sort(T[] array, Comparator<T> comparator, Mode mode){
		boolean descending = mode == Mode.DESCENDING;

		for(int i = 1; i < array.length; i++){
			T tbs = array[i];
			int j = i;
			for(; j > 0; j--){
				if(comparator.compare(tbs, array[j - 1]) < 0 ^ descending)
					array[j] = array[j - 1];
				else
					break;
			}
			array[j] = tbs;
		}
	}
	
	public static void test(){
		Integer[] tbs = { 1, 5, 1, 4, 5, 4, 3, 7, 3, 8, 7, 8 };
		InsertionSort.sort(tbs, InsertionSort.Mode.DESCENDING);
		InsertionSort.sort(tbs);
		Printer.print(tbs);

	}

}
