package uccu_panel;

import uccu_client.*;
import uccu_client.Painter;

import java.awt.Rectangle;

import javax.swing.*;

import uccu_client.*;
public class BagPanel extends JInternalFrame{
	JPanel bp;
	BackgroundPanel backg;
	final int lbW = 45,lbH = 45,gapW = 18,gapH=11;
	public BagPanel() {
		super("bag",false,true);
		this.setLocation(200, 200);
		this.setSize(301,510);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setLayout(null);
		this.setBorder(null);
		
		bp = null;
		backg = new BackgroundPanel(new Rectangle(0,0,getWidth(),getHeight()));
		backg.setSize(getSize());
		backg.img = new Picture("物品栏背景.png", 0, 0, 0).getImage();
		this.getRootPane().add(backg);
		((JPanel)this.getContentPane()).setOpaque(false);
	}
	public void reflash(){
		JPanel tmpbp = bp;
		bp = new JPanel();
		bp.setOpaque(false);
		bp.setLayout(null);
		bp.setBounds(32, 25, 4*(lbW+gapW), 8*(lbH+gapH));
		Item[] bagitem = uccu_client.Painter.gameBox.mainrole.items;
		for(int i=0;i<bagitem.length;++i){
			if(bagitem[i] == null || bagitem[i].empty || bagitem[i].num == 0) continue;
			ItemIcon item = new ItemIcon(bagitem[i]);
			item.setBounds((i%4)*(lbW+gapW),(i/4)*(lbH+gapH),lbW,lbH);
			bp.add(item);
		}
		if(tmpbp != null) this.remove(tmpbp);
		Painter.painter.unshowPopupInfo();
		this.add(bp);
	}
	public void showPanel(){
//		if(1 == 1)return;
		if(bp != null) this.remove(bp);
		bp = new JPanel();
		bp.setOpaque(false);
		bp.setLayout(null);
		bp.setBounds(32, 25, 4*(lbW+gapW), 8*(lbH+gapH));
		this.add(bp);
		Item[] bagitem = uccu_client.Painter.gameBox.mainrole.items;
		for(int i=0;i<bagitem.length;++i){
			if(bagitem[i] == null || bagitem[i].empty || bagitem[i].num == 0) continue;
			ItemIcon item = new ItemIcon(bagitem[i]);
			item.setBounds((i%4)*(lbW+gapW),(i/4)*(lbH+gapH),lbW,lbH);
			bp.add(item);
		}
		this.show();
	}
}