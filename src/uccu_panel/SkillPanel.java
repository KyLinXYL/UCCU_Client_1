package uccu_panel;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.*;

import uccu_client.Item;
import uccu_client.Picture;
import uccu_client.Skill;

public class SkillPanel extends JInternalFrame{
	JPanel bp;
	BackgroundPanel backg;
	final int lbW = 70,lbH = 70,gapW = 18,gapH=12;
	public SkillPanel() {
		super("skill",false,true);
		this.setLocation(200, 200);
		this.setSize(250,300);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setLayout(null);
		this.setBorder(null);
		bp = null;
		backg = new BackgroundPanel(new Rectangle(0,0,getWidth(),getHeight()));
		backg.setSize(getSize());
		backg.img = new Picture("lf.jpg", 0, 0, 0).getImage()
				.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
		this.getRootPane().add(backg);
		((JPanel)this.getContentPane()).setOpaque(false);
	}
	public void showPanel(){
//		if(1 == 1)return;
		if(bp != null) this.remove(bp);
		bp = new JPanel();
		bp.setOpaque(false);
		bp.setLayout(null);
		bp.setBounds(20, 20, 3*(lbW+gapW), 3*(lbH+gapH));
		this.add(bp);
		Skill[] skillitem = uccu_client.Painter.gameBox.mainrole.skills;
		for(int i=0;i<skillitem.length;++i){
			if(skillitem[i] == null || skillitem[i].empty) continue;
			SkillIcon item = new SkillIcon(skillitem[i]);
			SubSkillPanel itempanel = new SubSkillPanel(item, new Rectangle(0,i*(lbH+gapH),getWidth(),lbH));
			bp.add(itempanel);
		}
		this.show();
	}
}