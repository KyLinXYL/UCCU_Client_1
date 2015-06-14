package uccu_panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import uccu_client.Painter;

public class WaitingPanel extends JPanel {
	JTextArea intro;
	JLabel stage;
	public WaitingPanel(String info,Rectangle bounds,Rectangle introbounds) {
		this.setOpaque(false);
		this.setLayout(null);
		this.setBounds(bounds);
		stage = new JLabel("...0%");
		this.add(stage);
		intro = new JTextArea(info);
		intro.setBackground(new Color(0,0,0,200));
		intro.setForeground(Color.white);
		intro.setOpaque(false);
		intro.setBounds(introbounds);
		this.add(intro);
	}
	public void setStage(double s){
//		stage = s;
	}
}
