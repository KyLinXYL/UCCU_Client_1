package uccu_panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import uccu_client.Picture;

public class SubSkillPanel extends JPanel{
	SkillIcon skillIcon;
	JTextArea name;
	JTextArea level;
	Image backg;
	public SubSkillPanel(SkillIcon sk,Rectangle bounds){
		this.setOpaque(false);
		this.setLayout(null);
		this.setBounds(bounds);
		skillIcon = sk;
		name = new JTextArea(sk.info.name);
		name.setEditable(false);
		level = new JTextArea("等级："+sk.info.level);
		level.setEditable(false);
		backg = new Picture("聊天背景.png",0,0,0).getImage()
				.getScaledInstance(bounds.width, bounds.height, Image.SCALE_DEFAULT);
		skillIcon.setBounds(5, 5, bounds.height-10, bounds.height-10);//正方形
		this.add(skillIcon);
		name.setBounds(bounds.height+20,5
				,bounds.width-bounds.height,bounds.height/2);
		name.setOpaque(false);
		name.setFont(new Font("黑体",1,24));
		this.add(name);
		level.setBounds(bounds.height+20,bounds.height/2+5
				,bounds.width-bounds.height,bounds.height/2-10);
		level.setOpaque(false);
		level.setFont(new Font("黑体",1,18));
		this.add(level);
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(backg, 0, 0, this);
	}
}
