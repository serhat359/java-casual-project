package casual;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HOMM{

	private static class Row{
		int level;
		int exp;
	}

	public static void main(String[] args) throws IOException{
		File f = new File("new.txt");

		FileInputStream fis = new FileInputStream(f);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));

		List<Row> rowList = new ArrayList<>();

		try{
			while(true){
				Row row = new Row();
				row.level = getInt(br);
				row.exp = getInt(br);

				rowList.add(row);
			}
		}
		catch(NumberFormatException ex){

		}

		int lastExp = 0;

		List<Integer> data1 = new ArrayList<>();
		List<Double> data2 = new ArrayList<>();

		for(Row row: rowList){
			int diff = row.exp - lastExp;

			double percentage = 100.0 * diff / row.exp;

			Printer.print(row.level + "\t" + row.exp + "\t\t" + percentage + "\n");

			data1.add(row.exp);
			data2.add(percentage);

			lastExp = row.exp;
		}

		Printer.print(data2);

		// closing
		br.close();
	}

	public static int getInt(BufferedReader br) throws IOException{
		String line = br.readLine();
		int number = Integer.parseInt(line);
		return number;
	}

}
