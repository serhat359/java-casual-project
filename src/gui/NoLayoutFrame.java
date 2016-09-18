package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class NoLayoutFrame extends JFrame{
	private static final long serialVersionUID = -755382625218821422L;
	final private ArrayList<JComponent> components = new ArrayList<JComponent>();
	final public JPanel panel;

	public NoLayoutFrame(String title){
		super(title);
		pack();
		panel = new JPanel();
		this.addOld(panel);
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		g = panel.getGraphics();
		for(JComponent jc: components){
			jc.paint(g);
		}
	}

	public void add(JComponent jc){
		components.add(jc);
	}

	public void remove(JComponent jc){
		components.remove(jc);
	}

	private void addOld(Component comp){
		super.add(comp);
	}

	public void setContentSize(int width, int height){
		Insets insets = this.getInsets();
		this.setSize(insets.left + insets.right + width, insets.top + insets.bottom + height);
	}

	public void setBG(Color bgColor){
		panel.setBackground(bgColor);
	}
}
