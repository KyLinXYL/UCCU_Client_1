/* 登录窗体类 */
package uccu_client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uccu_panel.BackgroundPanel;
import uccu_panel.CharacterPanel;
import uccu_panel.CreatePanel;
import uccu_panel.LoginPanel;
import uccu_panel.WaitingPanel;

public class LoginBox extends JFrame{
	/* 登录结束后，应修改ClientMain中的isLoginOver,isLoginsuccess为对应值 */
	private BackgroundPanel backPicPanel;
	private LoginPanel loginPanel;
	private WaitingPanel waitingPanel;
	private CharacterPanel characterPanel;
	private CreatePanel createPanel;
	Dimension  screensize;
	public void createBack(){
		createPanel.setVisible(false);
		characterPanel.setVisible(true);
	}
	public void beginCreate(){
		characterPanel.setVisible(false);
		createPanel.init();
		this.add(createPanel);
		createPanel.setVisible(true);
	}
	public LoginBox(){
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				ClientMain.isLoginOver = true;
				((JFrame)e.getSource()).dispose();
			}
		});
		this.setLayout(null);
		screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setUndecorated(true);
		this.setBackground(new Color(0,0,0,0));
		loginPanel = new LoginPanel(this);
		this.setSize(loginPanel.getSize());
		this.setLocation(((int)screensize.getWidth()-(int)getSize().getWidth())/2
				, ((int)screensize.getHeight() - (int)getSize().getHeight())/2);
		this.add(loginPanel);
		backPicPanel = new BackgroundPanel(new Rectangle(0,0,1366,768));
		waitingPanel = new WaitingPanel(
				"    Berus星球被Siron称霸着,他们凭借\n着自己的智慧开创了Berus星球有史以来\n最为先进的文明。"
				+"\n    Siron用自己的无线电波技术向外发\n射着友好的信号,等待着另一个友好种族\n的回应......"
				+"\n    然而就像小孩子总是以为一切都是\n善意的一样,他们完全无法体会成年人的\n恶意。"
				+"\n  毗邻Berus星系的Neuzac星球就是这样\n的存在,Neuzac在Madlos种族的漫长统治\n之下，Neuzac星上的一切几乎被消耗\n殆尽。"
				+"\n    亟需资源的Madlos捕获到了"
				+"Berus\n——这一个年轻而富有活力的星球发出\n的信号。"
				+"\n   他们欣喜若狂,规划着仅剩不多的资\n源,悄无声息地来到了Berus的一颗行星。\n   一场种族生存之战就此打响！"
				,new Rectangle(0,0,1366,768)
				,new Rectangle(50,100,400,600));
		characterPanel = new CharacterPanel(this);
		characterPanel.setBounds(0, 0, 1366, 768);
		createPanel = new CreatePanel(this);
		createPanel.setBounds(0, 0, 1366, 768);
	}
	public void init(){
//		backPicPanel.img = Toolkit.getDefaultToolkit().getImage("bg1.jpg");
		this.setVisible(true);	
	}
	public void onLoginResponse(boolean res){
		UccuLogger.log("ClientServer/LoginBox/onLoginResponse", "Receive a package 0003(登陆反馈)");
		UccuLogger.log("ClientServer/LoginBox/onLoginResponse", "package 0003: "+res);
		if(res){
			this.setVisible(false);
			this.setSize(1366, 768);
			this.setLocation(((int)screensize.getWidth()-(int)getSize().getWidth())/2
					, ((int)screensize.getHeight() - (int)getSize().getHeight())/2);
			this.getLayeredPane().add(backPicPanel,new Integer(Integer.MIN_VALUE));
			((JPanel)this.getContentPane()).setOpaque(false);
			loginPanel.setVisible(false);
			this.add(waitingPanel);
			backPicPanel.img = Toolkit.getDefaultToolkit().getImage("load1.jpg");
			try {Thread.sleep(500);} catch (InterruptedException e) {}
			this.setVisible(true);//backPicPanel.repaint();
		}
		else{
			JOptionPane.showMessageDialog(null, "login fail!");
		}
		//decoder得到一个登录反馈时调用该函数
	}
	//在Loginresponse里面确定了角色ID之后，需将ID传到gameBox中的mainID里	
	public void onRegistResponse(boolean res){
		UccuLogger.log("ClientServer/LoginBox/onRegistResponse", "Receive a package 0005(注册结果)");
		UccuLogger.log("ClientServer/LoginBox/onRegistResponse", "package 0005: "+res);
		if(res){
			JOptionPane.showMessageDialog(null, "regist success!");
		}
		else{
			JOptionPane.showMessageDialog(null, "regist fail!");
		}
		//decoder得到一个注册反馈时调用该函数
	}
	public void onCreatResponse(boolean res){
		UccuLogger.log("ClientServer/LoginBox/onCreatResponse", "Receive a package 0009(角色创建结果)");
		if(res){
			JOptionPane.showMessageDialog(null, "create success!");
		}
		else{
			JOptionPane.showMessageDialog(null, "create fail!");
		}
	}//创建角色成功否
	public void noMorePackage() {
		UccuLogger.log("ClientServer/LoginBox/noMorePackage", "Receive a package 0006(角色预加载: 这是最后一个空包");
		this.setVisible(false);
		waitingPanel.setVisible(false);
		this.add(characterPanel);
		backPicPanel.img = Toolkit.getDefaultToolkit().getImage("角色背景.png")
				.getScaledInstance(1366,768,Image.SCALE_DEFAULT);
		try {Thread.sleep(500);} catch (InterruptedException e) {}
		this.setVisible(true);//backPicPanel.repaint();
	}
	public void addCharacter(int id,String name,byte level, byte gender,int picID){
		UccuLogger.log("ClientSever/LoginBox/addCharacter", "Receive a package 0006(角色预加载)");
		characterPanel.addCharacter(id,name,level,gender,picID);
		characterPanel.repaint();
	}
	public void serverState(byte stat){
		UccuLogger.log("ClientSever/LoginBox/serverState", "Receive a package 0001(回应连接请求)");
		UccuLogger.log("ClientSever/LoginBox/serverState", "package 0001: "+stat);
		ClientMain.isGateComfirm = true;
		//0维护 1 空闲 2爆满
		//Decoder 收到0001包后调用loginBox的serverState函数，loginBox更新服务器信息并repaint出来，在此之前阻塞在服务器未知状态阶段
		switch(stat){
		case 0:
			loginPanel.serverStat.setText("维护");break;
		case 1:
			loginPanel.serverStat.setText("空闲");break;
		case 2:
			loginPanel.serverStat.setText("爆满");break;
		}
		this.repaint();
	}
}









