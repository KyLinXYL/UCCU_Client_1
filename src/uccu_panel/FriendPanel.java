package uccu_panel;

import javax.swing.*;

import uccu_client.Picture;

public class FriendPanel extends JInternalFrame{
	public FriendPanel(){
		super("friend",true,true);
		this.setLayout(null);
		this.setLocation(200, 200);
		this.setSize(200, 200);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setBorder(null);
//		ItemIcon tic = new ItemIcon(new Picture("0.png", 0, 0, 0), "haah\nninin\nfdafa", 1);
//		tic.setBounds(20, 20, 50, 50);
//		this.add(tic);
	}
}