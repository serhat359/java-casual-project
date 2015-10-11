package util;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;

public class FrameUtil{
	public static void setContentSize(JFrame frame, int width, int height){
		Container c = frame.getContentPane();
		Dimension d = new Dimension(width, height);
		c.setPreferredSize(d);
		frame.pack();
	}
}