package util;

public class CharUtil{

	public static int toInt(char c){
		if(isInt(c))
			return c - '0';
		else
			throw new NumberFormatException();
	}

	public static boolean isInt(char c){
		return c >= '0' && c <= '9';
	}

	public static boolean equals(Character c1, Character c2){
		if(c1 == null && c2 == null)
			return true;
		else if((c1 == null && c2 != null) || (c1 != null && c2 == null))
			return false;
		else
			return c1 == c2;
	}

	public static char hexIntToChar(int i){
		final char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
				'D', 'E', 'F' };

		if(i >= 0 && i <= 15)
			return chars[i];
		else
			throw new RuntimeException("Number must be between 0 and 15");
	}
}
