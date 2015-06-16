package uccu_panel;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import uccu_client.Painter;
import uccu_client.Picture;

public class ExitPanel extends JInternalFrame{
	public ExitPanel(){
		super("exit",false,true);
		this.setLayout(null);
		this.setBorder(null);
		this.setBounds(Painter.painter.width/2-200,Painter.painter.height/2-100,400,200);
		BackgroundPanel bgp = new BackgroundPanel(new Rectangle(0,0,getWidth(),getHeight()));
		bgp.img = new Picture("exit.png",0,0,0).getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
		this.getRootPane().add(bgp);
		((JPanel)this.getContentPane()).setOpaque(false);
		JButton yes = new JButton();
		yes.setBounds(230,100,70,70);
		yes.setIcon(new ImageIcon(new Picture("退出.png",0,0,0).getImage()
				.getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_DEFAULT) ));
		yes.addActionListener(e->{
			Painter.painter.dispatchEvent(new WindowEvent(Painter.painter, WindowEvent.WINDOW_CLOSING));
		});
		yes.setOpaque(false);
		yes.setBorderPainted(false);
		yes.setContentAreaFilled(false);
		this.add(yes);
		JButton no = new JButton();
		no.setBounds(320,100,70,70);
		no.setIcon(new ImageIcon(new Picture("取消.png",0,0,0).getImage()
				.getScaledInstance(no.getWidth(), no.getHeight(), Image.SCALE_DEFAULT) ));
		no.addActionListener(e->{
			this.setVisible(false);
		});
		no.setOpaque(false);
		no.setBorderPainted(false);
		no.setContentAreaFilled(false);
		this.add(no);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
}
