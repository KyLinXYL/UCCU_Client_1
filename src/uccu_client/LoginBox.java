/* 登录窗体类 */
package uccu_client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uccu_panel.BackgroundPanel;
import uccu_panel.CharacterPanel;
import uccu_panel.CreatePanel;
import uccu_panel.LoginPanel;
import uccu_panel.MessageFrame;
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
		this.setIconImage(new Picture("UCCU.png", 0, 0, 0).getImage());
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
				"UCCU小组出品...\n"
						+"Track (轨迹)...\n"
						+"成员:...\n"
						+"李哲涵	 徐玉麟...\n"
						+"唐  爽	 王  丰...\n"
						+"王  佩	 潘  虹...\n"
						+"	 2015年6月...\n"
						+"..."
						+"正在与服务器建立连接...\n"
										+ "连通率100%,开始测试同步...\n"
										+"...\n"
										+ "同步率200%...\n"
										+"...\n"
										+ "同步完成,启动神经元突触对接...\n"
										+"...\n"
										+ "对接成功!...\n"
										+ "进入游戏...\n"
				,new Rectangle(0,0,1366,768)
				,new Rectangle(50,100,450,550));
		characterPanel = new CharacterPanel(this);
		characterPanel.setBounds(0, 0, 1366, 768);
		createPanel = new CreatePanel(this);
		createPanel.setBounds(0, 0, 1366, 768);
		JButton exitbutton = new JButton();
		exitbutton.setBounds(1340, 10, 20, 20);
		exitbutton.setIcon(new ImageIcon(
				new Picture("关闭.png",0,0,0).getImage()
				.getScaledInstance(20,20,Image.SCALE_DEFAULT) ));
		this.add(exitbutton);
		exitbutton.addActionListener(e->{
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		});
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
			new MessageFrame("登录失败");
		}
		//decoder得到一个登录反馈时调用该函数
	}
	//在Loginresponse里面确定了角色ID之后，需将ID传到gameBox中的mainID里	
	public void onRegistResponse(boolean res){
		UccuLogger.log("ClientServer/LoginBox/onRegistResponse", "Receive a package 0005(注册结果)");
		UccuLogger.log("ClientServer/LoginBox/onRegistResponse", "package 0005: "+res);
		if(res){
			new MessageFrame("注册成功");
		}
		else{
			new MessageFrame("注册失败");
		}
		//decoder得到一个注册反馈时调用该函数
	}
	public void onCreatResponse(boolean res){
		UccuLogger.log("ClientServer/LoginBox/onCreatResponse", "Receive a package 0009(角色创建结果)");
		if(res){
			new MessageFrame("创建成功！");
		}
		else{
			new MessageFrame("创建失败");
		}
	}//创建角色成功否
	public void noMorePackage() {
		UccuLogger.log("ClientServer/LoginBox/noMorePackage", "Receive a package 0006(角色预加载: 这是最后一个空包");
		for(int i=1;i<=100;++i){
			waitingPanel.setStage(i/100.0);
			try {Thread.sleep(50);} catch (InterruptedException e) {}
		}
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









