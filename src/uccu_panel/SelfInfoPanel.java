package uccu_panel;

import javax.swing.JInternalFrame;

public class SelfInfoPanel extends JInternalFrame{
	public SelfInfoPanel() {
		super("info",true,true);
		this.setLocation(200, 200);
		this.setSize(200, 200);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setBorder(null);
	}
}