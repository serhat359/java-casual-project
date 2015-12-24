package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StreamUtil{

	public static ArrayList<Character> GetLine(final InputStream is) throws IOException{
		ArrayList<Character> ll = new ArrayList<>(256);

		boolean eof = false;

		for(int i = 0;;){
			i = is.read();

			if(i == '\n'){
				break;
			}
			else if(i == -1){
				eof = true;
				break;
			}
			else if(i == '\r'){
				// ignore
			}
			else{
				ll.add((char)i);
			}
		}

		return eof && ll.size() == 0 ? null : ll;
	}

	public static byte[] getAllBytes(InputStream stream) throws IOException{

		byte[] data = new byte[stream.available()];

		stream.read(data);

		return data;
	}

	public static byte[] getAllBytes(String fileName) throws IOException{

		FileInputStream fis = new FileInputStream(fileName);

		byte[] data = StreamUtil.getAllBytes(fis);

		fis.close();

		return data;
	}
}
