package casual;
public class CuttingRod{

	private int[] prices;

	public CuttingRod(int[] prices){
		this.prices = prices;
	}

	public int getMax(int length){
		return max(length, prices.length, 0);
	}

	private int max(int length, int available, int price){
		if(length == 0)
			return price;
		if(length < available)
			return max(length, length, price);
		if(available == 0)
			return 0;
		return Math.max(max(length - available, available, price + prices[available-1]),
				max(length, available - 1, price));
	}
	
	public static void test(){

		int[] prices = { 1, 5, 7, 11, 14, 15 };
		CuttingRod c = new CuttingRod(prices);
		System.out.print(c.getMax(15));
		
	}
}
