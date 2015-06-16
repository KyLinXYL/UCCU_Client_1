package uccu_panel;

import javax.swing.JInternalFrame;

public class PlayerInfoPanel extends JInternalFrame{

	public PlayerInfoPanel(String text, boolean re, boolean cl) {
		super(text, re, cl);
		this.setLocation(200, 200);
		this.setSize(200, 200);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setBorder(null);
		this.setLayout(null);
	}
	
}