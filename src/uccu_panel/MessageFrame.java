package uccu_panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;




import javax.swing.JTextArea;

import uccu_client.Painter;
import uccu_client.Picture;

public class MessageFrame extends JFrame{
	public MessageFrame(String msg) {
		int width = 400,height = 200;
		this.setLayout(null);
		this.setUndecorated(true);
		this.setBounds((Painter.painter.width-width)/2,(Painter.painter.height-height)/2,width,height);
		BackgroundPanel bgp = new BackgroundPanel(new Rectangle(0,0,width,height));
		bgp.img = new Picture("lf.jpg", 0,0,0).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
		this.getLayeredPane().add(bgp,new Integer(Integer.MIN_VALUE));
		((JPanel)this.getContentPane()).setOpaque(false);
		JTextArea text = new JTextArea(msg);
		text.setForeground(Color.WHITE);
		text.setFont(new Font("隶书",1,24));
		text.setEditable(false);
		text.setBorder(null);
		text.setOpaque(false);
		text.setBounds(150,50,300,50);
		this.add(text);
		JButton yes = new JButton();
		yes.setBounds(150, 130, 100, 50);
		yes.setOpaque(false);
		yes.setBorderPainted(false);
		yes.setContentAreaFilled(false);
		yes.setIcon(new ImageIcon(new Picture("确定.png",0,0,0).getImage()
				.getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_DEFAULT) ));
		yes.addActionListener(e->{
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		});
		this.add(yes);
		this.setVisible(true);
	}
}
