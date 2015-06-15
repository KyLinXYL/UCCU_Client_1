package uccu_panel;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.nio.ByteBuffer;

import javax.swing.*;

import uccu_client.ClientMain;
import uccu_client.Datagram;
import uccu_client.LoginBox;
import uccu_client.Painter;
import uccu_client.Picture;
import uccu_client.SendingModule;

public class CreatePanel extends JPanel{
	JButton createButton;
	JButton backButton;
	JTextField nameField;
	JCheckBox boy;
	JCheckBox girl;
	CheckboxGroup gender;
	LoginBox parent;
	JButton[] characterButton;
	int select;
	BackgroundPanel bigPic;
	final int maxcharacter = 3;
	public void init(){
		for(int i=0;i<maxcharacter;++i)
			characterButton[i].setIcon(new ImageIcon(
					Painter.painter.getPicByPid(i+11).getImage()
					.getScaledInstance(130,130,Image.SCALE_DEFAULT) ));
	}
	public CreatePanel(LoginBox lb){
		parent = lb;
		nameField = new JTextField();
		createButton = new JButton("create");
		createButton.addActionListener(e->{
			String name = nameField.getText();
			byte gender = 1;
			if(boy.isSelected()) gender = 0;
			int picID=select;
			SendingModule.sendCreateCharacter(name,gender,picID);
		});
		backButton = new JButton("back");
		backButton.addActionListener(e->{
			parent.createBack();
		});
		boy = new JCheckBox("boy",true);
		girl = new JCheckBox("girl",false);
		boy.addActionListener(e->{
			girl.setSelected(false);
		});
		girl.addActionListener(e->{
			boy.setSelected(false);
		});
		characterButton = new JButton[maxcharacter];
		for(int i=0;i<maxcharacter;++i){
			characterButton[i] = new JButton();
			characterButton[i].setName(String.valueOf(i));
			characterButton[i].setSize(200, 200);
			characterButton[i].setOpaque(false);
			characterButton[i].setContentAreaFilled(false);
			characterButton[i].setBorderPainted(false);
			characterButton[i].addActionListener(e->{
				int buttonNum = Integer.parseInt( ((JButton)e.getSource()).getName() );
				select = buttonNum+11;
				bigPic.img = Painter.painter.getPicByPid(select)
						.getImage().getScaledInstance(bigPic.getWidth(), bigPic.getHeight(), Image.SCALE_DEFAULT);
				repaint();
			});
		}
		bigPic = new BackgroundPanel(new Rectangle(500,100,400,400));
		
		this.setLayout(null);
		this.setOpaque(false);
		this.add(bigPic);
		JPanel cbp = new JPanel();
		cbp.setOpaque(false);
		cbp.setLayout(new GridLayout(maxcharacter,1,0,0));
		for(JButton cb : characterButton)
			cbp.add(cb);
		cbp.setBounds(70, 65, 300, 530);
		this.add(cbp);
		boy.setBounds(1100, 250, 50, 50);
		boy.setOpaque(false);
		boy.setForeground(Color.white);
		girl.setBounds(1150, 250, 50, 50);
		girl.setOpaque(false);
		girl.setForeground(Color.white);
		this.add(boy);
		this.add(girl);
		nameField.setBounds(1100, 150, 100, 50);
		nameField.setBackground(new Color(110,110,110,150));
		nameField.setForeground(Color.white);
		this.add(nameField);
		createButton.setBounds(1100, 500, 100, 100);
		this.add(createButton);
		backButton.setBounds(1100, 600, 100, 50);
		this.add(backButton);
	}
}