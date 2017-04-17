package casual;

//Program to print all prime factors
import java.math.BigInteger;

public class PrimeFactor{
	
	static BigInteger big0 = toBig(0);
	static BigInteger big2 = toBig(2);
	static BigInteger big3 = toBig(3);
	
	// A function to print all prime factors
	// of a given number n
	public static void primeFactors(BigInteger n){
		
		// Print the number of 2s that divide n
		while(n.mod(big2) == big0){
			System.out.print(2 + " ");
			n = n.divide(big2);
		}

		// n must be odd at this point. So we can
		// skip one element (Note i = i +2)
		for(BigInteger i = big3; i.compareTo(bigIntSqRootFloor(n)) <= 0; i = i.add(big2)){
			// While i divides n, print i and divide n
			while(n.mod(i) == big0){
				System.out.print(i + " ");
				n = n.divide(i);
			}
		}

		// This condition is to handle the case whien
		// n is a prime number greater than 2
		if(n.compareTo(big2) > 0)
			System.out.print(n);
	}

	public static void test(){
		String x = "78153610988380942419";
		BigInteger n = new BigInteger(x);
		primeFactors(n);
	}

	private static BigInteger toBig(int x){
		return BigInteger.valueOf(x);
	}

	private static BigInteger bigIntSqRootFloor(BigInteger x) throws IllegalArgumentException{
		// square roots of 0 and 1 are trivial and
		// y == 0 will cause a divide-by-zero exception
		if(x.equals(BigInteger.ZERO) || x.equals(BigInteger.ONE)){
			return x;
		} // end if
		
		BigInteger y;
		// starting with y = x / 2 avoids magnitude issues with x squared
		for(y = x.divide(big2); y.compareTo(x.divide(y)) > 0; y = ((x.divide(y)).add(y)).divide(big2))
			;
		return y;
	} // end bigIntSqRootFloor

}