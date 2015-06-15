package uccu_panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import uccu_client.Painter;
import uccu_client.Picture;

public class WaitingPanel extends JPanel {
	JTextArea intro;
	JLabel stage;
	Rectangle introbds;
	Image introbackg;
	public WaitingPanel(String info,Rectangle bounds,Rectangle introbounds) {
		this.setOpaque(false);
		this.setLayout(null);
		this.setBounds(bounds);
		stage = new JLabel("...0%");
		stage.setFont(new Font("黑体", 1, 64));
		stage.setForeground(Color.white);
		stage.setOpaque(false);
		stage.setBounds(bounds.x+bounds.width-300, bounds.y+bounds.height-150, 300,100);
		this.add(stage);
		introbds = introbounds;
		Picture pic = new Picture("聊天背景.png", 0, 0, 0);
		introbackg = pic.getImage().getScaledInstance(introbounds.width, introbounds.height, Image.SCALE_DEFAULT);
		intro = new JTextArea(info);
		intro.setEditable(false);
		intro.setFont(new Font("黑体", 1, 24));
		intro.setForeground(Color.white);
		intro.setOpaque(false);
		intro.setBounds(introbounds.x+20,introbounds.y+20,introbounds.width-40,introbounds.height-40);
		this.add(intro);
	}
	public void setStage(double s){
		if(s<=0) s=0;
		if(s>=1) s=1;
		int stg = (int)(s*100);
		stage.setText("..."+stg+"%");
		repaint();
	}
	@Override
	public void paintComponent(Graphics g){
    	super.paintComponent(g);
    	g.drawImage(introbackg, introbds.x,introbds.y, this);
	}
}
