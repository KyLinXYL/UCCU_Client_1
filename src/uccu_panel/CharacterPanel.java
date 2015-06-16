package uccu_panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.nio.ByteBuffer;


import javax.swing.*;


import uccu_client.ClientMain;
import uccu_client.Datagram;
import uccu_client.LoginBox;
import uccu_client.Painter;
import uccu_client.Picture;
import uccu_client.SendingModule;

class CharacterInfo{
	int id;
	String name;
	byte level;
	byte gender;
	int picID;
	public CharacterInfo(	int id,String name,byte level,	byte gender,int picID) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.gender = gender;
		this.picID=picID;
	}
}
public class CharacterPanel extends JPanel{
	final int maxcharacter = 3;
	JButton[] characterButton;
	JTextArea nameText;
	JTextArea levelText;
	JTextArea genderText;
	BackgroundPanel bigPic;
	JButton entryButton;
	CharacterInfo[] characterInfo;
	LoginBox parent;
	int select;
	int characterCount;
	public CharacterPanel(LoginBox loginBox){
		parent = loginBox;
		characterCount = 0;
		select = -1;
		nameText = new JTextArea();
		levelText = new JTextArea();
		genderText = new JTextArea();
		entryButton = new JButton();
		entryButton.setBounds(1200,600,100,100);
		entryButton.setOpaque(false);
		entryButton.setBorderPainted(false);
		entryButton.setContentAreaFilled(false);
		entryButton.setIcon(new ImageIcon(new Picture("startgame.png",0,0,0).getImage()
				.getScaledInstance(entryButton.getWidth(), entryButton.getHeight(), Image.SCALE_DEFAULT) ));
		this.add(entryButton);
		entryButton.addActionListener(e->{
			if(select == -1) return;
			int pid = characterInfo[select].id;
			ClientMain.mainID = pid;
			SendingModule.sendCharacter(pid);
			ClientMain.isLoginsuccess = true;
			ClientMain.isLoginOver = true;
			loginBox.dispose();
		});
		characterButton = new JButton[maxcharacter];
		characterInfo = new CharacterInfo[maxcharacter];
		for(int i=0;i<maxcharacter;++i){
			characterButton[i] = new JButton();
			characterButton[i].setName(String.valueOf(i));
			characterButton[i].setSize(200, 200);
			characterButton[i].setOpaque(false);
			characterButton[i].setContentAreaFilled(false);
			characterButton[i].setBorderPainted(false);
			characterButton[i].setIcon(new ImageIcon(
					new Picture("newcharacter.png",0,0,0).getImage()
					.getScaledInstance(130,130,Image.SCALE_DEFAULT) ));
			characterButton[i].addActionListener(e->{
				int buttonNum = Integer.parseInt( ((JButton)e.getSource()).getName() );
				if(buttonNum >= characterCount){
					parent.beginCreate();
					return;
				}
				select = buttonNum;
				bigPic.img = Painter.painter.getPicByPid(characterInfo[buttonNum].picID)
						.getImage().getScaledInstance(bigPic.getWidth(), bigPic.getHeight(), Image.SCALE_DEFAULT);
				nameText.setText( characterInfo[select].name );
				levelText.setText( String.valueOf(characterInfo[select].level) );
				genderText.setText( characterInfo[select].gender == 0 ? "boy" : "girl" );
				repaint();
			});
		}
		
		this.setOpaque(false);
		this.setLayout(null);
		JPanel cbp = new JPanel();
		cbp.setOpaque(false);
		cbp.setLayout(new GridLayout(maxcharacter,1,0,0));
		for(JButton cb : characterButton)
			cbp.add(cb);
		cbp.setBounds(70, 65, 300, 530);
		this.add(cbp);
		
		JPanel lbp = new JPanel();
		int textW = 200,textH = 70;
		lbp.setOpaque(false);
		lbp.setLayout(null);
		lbp.setBounds(800, 500, 3*textW, 3*textH);
//		lbp.add(new JLabel("name"));
		nameText.setBounds(0,0*textH,textW,textH);
		nameText.setEditable(false);
		nameText.setOpaque(false);
		nameText.setForeground(Color.white);
		nameText.setFont(new Font("隶书", 1, 36));
		lbp.add(nameText);
//		lbp.add(new JLabel("level"));
		levelText.setBounds(0,1*textH,textW,textH);
		levelText.setEditable(false);
		levelText.setOpaque(false);
		levelText.setForeground(Color.white);
		levelText.setFont(new Font("隶书", 1, 36));
		lbp.add(levelText);
//		lbp.add(new JLabel("gender"));
		genderText.setBounds(0,2*textH,textW,textH);
		genderText.setEditable(false);
		genderText.setOpaque(false);
		genderText.setForeground(Color.white);
		genderText.setFont(new Font("隶书", 1, 36));
		lbp.add(genderText);
		this.add(lbp);
		
		bigPic = new BackgroundPanel(new Rectangle(500+textW,50,400,400));
		this.add(bigPic);

	}
	public void addCharacter(int id,String name,byte level, byte gender,int picID){
		if(characterCount == 8) return;
		characterInfo[characterCount] = new CharacterInfo(id,name,level, gender,picID);
		characterButton[characterCount].setIcon(new ImageIcon(
				uccu_client.Painter.painter.getPicByPid(picID).getImage()
				.getScaledInstance(130,130,Image.SCALE_DEFAULT) ));
		characterCount++;
	}
}