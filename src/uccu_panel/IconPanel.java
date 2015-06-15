package uccu_panel;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import uccu_client.BasicLib;
import uccu_client.Painter;
import uccu_client.SendingModule;
import uccu_client.Painter.FrameType;

public class IconPanel extends JPanel{
	JButton info;
	JButton bag;
	JButton skill;
	JButton friend;
	ImageIcon[] infopic;
	ImageIcon[] bagpic;
	ImageIcon[] skillpic;
	ImageIcon[] friendpic;
	final int iconW = 70,iconH = 70;
	public IconPanel(){
		this.setLayout(null);
		this.setOpaque(false);
		info = new JButton();
		bag = new JButton();
		skill = new JButton();
		friend  = new JButton();
		infopic = new ImageIcon[3];
		bagpic = new ImageIcon[3];
		skillpic = new ImageIcon[3];
		friendpic = new ImageIcon[3];
		
		info.setBounds(0*iconW, 0, iconW,iconH);
		bag.setBounds(1*iconW, 0, iconW,iconH);
		skill.setBounds(2*iconW, 0, iconW,iconH);
		friend.setBounds(3*iconW, 0, iconW,iconH);
		infopic[0] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("人物1.png"));
		infopic[0].setImage(infopic[0].getImage().getScaledInstance(
				info.getWidth(), info.getHeight(), Image.SCALE_DEFAULT));
		infopic[1] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("人物2.png"));
		infopic[1].setImage(infopic[1].getImage().getScaledInstance(
				info.getWidth(), info.getHeight(), Image.SCALE_DEFAULT));
		infopic[2] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("人物1.png"));
		infopic[2].setImage(infopic[2].getImage().getScaledInstance(
				info.getWidth(), info.getHeight(), Image.SCALE_DEFAULT));
		info.setIcon(infopic[0]);
		
		bagpic[0] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("物品1.png"));
		bagpic[0].setImage(bagpic[0].getImage().getScaledInstance(
				bag.getWidth(), bag.getHeight(), Image.SCALE_DEFAULT));
		bagpic[1] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("物品2.png"));
		bagpic[1].setImage(bagpic[1].getImage().getScaledInstance(
				bag.getWidth(), bag.getHeight(), Image.SCALE_DEFAULT));
		bagpic[2] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("物品1.png"));
		bagpic[2].setImage(bagpic[2].getImage().getScaledInstance(
				bag.getWidth(), bag.getHeight(), Image.SCALE_DEFAULT));
		bag.setIcon(bagpic[0]);
		
		skillpic[0] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("技能1.png"));
		skillpic[0].setImage(skillpic[0].getImage().getScaledInstance(
				skill.getWidth(), skill.getHeight(), Image.SCALE_DEFAULT));
		skillpic[1] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("技能2.png"));
		skillpic[1].setImage(skillpic[1].getImage().getScaledInstance(
				skill.getWidth(), skill.getHeight(), Image.SCALE_DEFAULT));
		skillpic[2] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("技能1.png"));
		skillpic[2].setImage(skillpic[2].getImage().getScaledInstance(
				skill.getWidth(), skill.getHeight(), Image.SCALE_DEFAULT));
		skill.setIcon(skillpic[0]);
		
		friendpic[0] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("好友1.png"));
		friendpic[0].setImage(friendpic[0].getImage().getScaledInstance(
				friend.getWidth(), friend.getHeight(), Image.SCALE_DEFAULT));
		friendpic[1] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("好友2.png"));
		friendpic[1].setImage(friendpic[1].getImage().getScaledInstance(
				friend.getWidth(), friend.getHeight(), Image.SCALE_DEFAULT));
		friendpic[2] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("好友1.png"));
		friendpic[2].setImage(friendpic[2].getImage().getScaledInstance(
				friend.getWidth(), friend.getHeight(), Image.SCALE_DEFAULT));
		friend.setIcon(friendpic[0]);
		
		info.addMouseListener(new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent e) {
				info.setIcon(infopic[2]);
		    }
			@Override
		    public void mouseReleased(MouseEvent e) {
				info.setIcon(infopic[1]);
				Painter.painter.showInnerFrame(FrameType.info);
		    }
			@Override
		    public void mouseEntered(MouseEvent e) {
				info.setIcon(infopic[1]);
		    }
			@Override
		    public void mouseExited(MouseEvent e) {
				info.setIcon(infopic[0]);
		    }
		});
		bag.addMouseListener(new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent e) {
				bag.setIcon(bagpic[2]);
		    }
			@Override
		    public void mouseReleased(MouseEvent e) {
				bag.setIcon(bagpic[1]);
				Painter.painter.showInnerFrame(FrameType.bag);
		    }
			@Override
		    public void mouseEntered(MouseEvent e) {
				bag.setIcon(bagpic[1]);
		    }
			@Override
		    public void mouseExited(MouseEvent e) {
				bag.setIcon(bagpic[0]);
		    }
		});
		skill.addMouseListener(new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent e) {
				skill.setIcon(skillpic[2]);
		    }
			@Override
		    public void mouseReleased(MouseEvent e) {
				skill.setIcon(skillpic[1]);
				Painter.painter.showInnerFrame(FrameType.skill);
		    }
			@Override
		    public void mouseEntered(MouseEvent e) {
				skill.setIcon(skillpic[1]);
		    }
			@Override
		    public void mouseExited(MouseEvent e) {
				skill.setIcon(skillpic[0]);
		    }
		});
		friend.addMouseListener(new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent e) {
				friend.setIcon(friendpic[2]);
		    }
			@Override
		    public void mouseReleased(MouseEvent e) {
				friend.setIcon(friendpic[1]);
				Painter.painter.showInnerFrame(FrameType.friend);
		    }
			@Override
		    public void mouseEntered(MouseEvent e) {
				friend.setIcon(friendpic[1]);
		    }
			@Override
		    public void mouseExited(MouseEvent e) {
				friend.setIcon(friendpic[0]);
		    }
		});
		this.add(info);
		this.add(bag);
		this.add(skill);
		this.add(friend);
		this.setSize(4*iconW,iconH);
	}
}