package uccu_client;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import uccu_client.Entity.style;
import uccu_client.GameBox.chatStat;
import uccu_panel.BagPanel;
import uccu_panel.ChatPanel;
import uccu_panel.EntityLabel;
import uccu_panel.ExitPanel;
import uccu_panel.FriendPanel;
import uccu_panel.GameWindow;
import uccu_panel.IconPanel;
import uccu_panel.LockedPlayerPanel;
import uccu_panel.PlayerInfoPanel;
import uccu_panel.PopupInfoPanel;
import uccu_panel.SelfInfoPanel;
import uccu_panel.SkillPanel;
import uccu_panel.WaitingPanel;
import uccu_panel.ChatPanel.puborpri;

public class Painter extends JFrame{
	public static GameBox gameBox;
	public static Painter painter;
//	AioSession session;
	JDesktopPane desktopPane;
	PopupInfoPanel popupInfoPanel;
	PlayerInfoPanel playerInfoPanel;
	SelfInfoPanel selfInfoPanel;
	BagPanel bagPanel;
	SkillPanel skillPanel;
	FriendPanel friendPanel;
	ExitPanel exitPanel;
	LockedPlayerPanel lockedPlayerPanel;
	ChatPanel chatPanel;
	IconPanel iconPanel;
	GameWindow gameWindow;
	WaitingPanel waitingPanel;
	public static Dimension  screensize;
	public static int width,height,windowWidth,windowHeight;
//	final AffineTransform identity = new AffineTransform();
	Image[] imagebuffer = new Image[2];
	Graphics2D[] graphbuffer = new Graphics2D[2];
	int buffercount = 0;
	
    HashMap<Integer, Picture> picMap;
    HashMap<Integer, GIFpicture> gifPicMap;
    ConcurrentHashMap<Entity,EntityLabel> entityMap;
    ConcurrentHashMap<DoublePoint, GIF> gifMap;
    Picture background;
    Entity mainRole;
    Airplane lockPlayer;
	
	public Painter(GameBox gb) {
		gameBox = gb;
		painter = this;
		
		this.setUndecorated(true);
		this.setIconImage(new Picture("UCCU.png", 0, 0, 0).getImage());
		this.setBackground(new Color(0,0,0,0));
		screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screensize);
		width = windowWidth = getWidth();
		height = windowHeight = getHeight();
		this.setLocation(((int)screensize.getWidth()-(int)getSize().getWidth())/2
				, ((int)screensize.getHeight() - (int)getSize().getHeight())/2);
		this.setResizable(false);
		this.setLayout(null);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				ClientMain.isGameOver = true;
				((JFrame)e.getSource()).dispose();
			}
		});
		
		picMap = new HashMap<Integer,Picture>();
		gifPicMap = new HashMap<Integer,GIFpicture>();
		entityMap = new ConcurrentHashMap<Entity,EntityLabel>();
		gifMap = new ConcurrentHashMap<DoublePoint,GIF>();
		background = new Picture("bg1.jpg", 0, 0,0);
		loadPic();
		
		gameWindow = new GameWindow();
		chatPanel = new ChatPanel();
		waitingPanel = new WaitingPanel(				
				"    Berus星球被Siron称霸着,他们凭借\n着自己的智慧开创了Berus星球有史以来\n最为先进的文明。"
				+"\n    Siron用自己的无线电波技术向外发\n射着友好的信号,等待着另一个友好种族\n的回应......"
				+"\n    然而就像小孩子总是以为一切都是\n善意的一样,他们完全无法体会成年人的\n恶意。"
				+"\n  毗邻Berus星系的Neuzac星球就是这样\n的存在,Neuzac在Madlos种族的漫长统治\n之下，Neuzac星上的一切几乎被消耗\n殆尽。"
				+"\n    亟需资源的Madlos捕获到了"
				+"Berus\n——这一个年轻而富有活力的星球发出\n的信号。"
				+"\n   他们欣喜若狂,规划着仅剩不多的资\n源,悄无声息地来到了Berus的一颗行星。\n   一场种族生存之战就此打响！"
				,getBounds(),new Rectangle(100,100,500,700));
		desktopPane = new JDesktopPane();
		popupInfoPanel = new PopupInfoPanel(new Picture("聊天背景.png", 0,0,0));
		iconPanel = new IconPanel();
		iconPanel.setLocation(getWidth()-iconPanel.getWidth(), getHeight()-iconPanel.getHeight()-100);
		lockedPlayerPanel = new LockedPlayerPanel();
		playerInfoPanel = new PlayerInfoPanel("haha",true,true);
		selfInfoPanel = new SelfInfoPanel();
		bagPanel = new BagPanel();
		skillPanel = new SkillPanel();
		friendPanel = new FriendPanel();
		exitPanel = new ExitPanel();
		addEventGeter();
	}
	private void loadPic(){
		for(PicProperty pp : PainterProperty.picp)
			picMap.put(pp.id, new Picture(pp));
		for(GIFPicProperty gpp : PainterProperty.gifp)
			gifPicMap.put(gpp.id, new GIFpicture(gpp));
	}
	private void addEventGeter(){
		gameWindow.addKeyListener(new KeyAdapter() {
			long lastpress = 0;
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getWhen()-lastpress < 500) return;
				lastpress = e.getWhen();
				if(lockPlayer == null) return;
//				gameBox.attack(lockPlayer.getID());
				int id = 0;
				for(Skill sk:gameBox.mainrole.skills){
					if(sk.picID == 1000000000+3){
						id = sk.instanceID;
						break;
					}
				}
				SendingModule.sendUseSkill(id, 0, lockPlayer.getID());
			}
		});
		gameWindow.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e){
				int absX = e.getX() - getWidth()/2 + (int)mainRole.posX;
				int absY = e.getY() - getHeight()/2 + (int)mainRole.posY;
				gameWindow.requestFocus();
				switch(e.getButton()){
				case MouseEvent.BUTTON1 :
					button1Clicked(absX, absY);
					break;
				case MouseEvent.BUTTON3 :
					unsetLockedPlayer();
					break;
				}
			}
		});
	}
	
	private void flashImg(){
		//根据add进来的Entity生成图像，更新gameWindow的内容
		buffercount = (buffercount+1)%2;
        Graphics2D gbuffer = graphbuffer[buffercount];
        /* 根据主角位置绘制背景 */
        int imgW = background.getWidth()-2,imgH = background.getHeight()-2;
        int zeroX = windowWidth/2 - ((int)mainRole.posX)%imgW - imgW;
        int zeroY = windowHeight/2 - ((int)mainRole.posY)%imgH - imgH;
        for(int i=0;i<3;++i)
        	for(int j=0;j<3;++j){
        		gbuffer.drawImage(background.getImage(), zeroX+i*imgW, zeroY+j*imgH,zeroX+(i+1)*imgW, zeroY+(j+1)*imgH
        				,1,1,imgW,imgH,gameWindow);
        	}
        String mainpos = "("+(int)mainRole.posX+","+(int)mainRole.posY+")";
        gbuffer.setColor(Color.white);
        gbuffer.drawChars(mainpos.toCharArray(), 0, mainpos.length(), 10, 20);
        gbuffer.drawOval(0, 20, 200, 200);
        gbuffer.setColor(new Color(43,53,60,180));
        gbuffer.fillOval(0, 20, 200, 200);
		/* 绘制每个Entity，修改他们的点击窗口位置 */
        Iterator<Entity> iter = entityMap.keySet().iterator();
		while(iter.hasNext()){
			EntityLabel el = entityMap.get(iter.next());
			if(el == null) continue;
			el.flashPos(gbuffer,mainRole.posX, mainRole.posY);//最好每次都从内存取，不要用局部变量的，否则可能抖动
		}
		/* 绘制动画效果 */
		Iterator<DoublePoint> piter = gifMap.keySet().iterator();
		while(piter.hasNext()){
			DoublePoint dp = piter.next();
			GIF gif = gifMap.get(dp);
			if(!gif.paint(gbuffer, mainRole.posX, mainRole.posY, this) ){
				deleteGIF(dp);
			}
		}
		gameWindow.img = imagebuffer[buffercount];
		gameWindow.repaint();
	}
	public int getLockedPlayerID(){
		if(lockPlayer == null) return mainRole.getID();
		else return lockPlayer.getID();
	}
	public void button1Clicked(int absX,int absY){
		gameBox.sendTargetPos(absX,absY);
		addGIF(new DoublePoint(absX,absY), 10,1);
	}
	public Picture getPicByPid(int pid){
		return picMap.get(pid);
	}
	public void localInform(String msg){
		chatPanel.insertChat("[通知]"+msg, chatPanel.labelstat);
	}
	public void allMapChat(chatStat s,String speakerID,String msg){
		//先检查chatStat如果是success的话那么 Speakerid表示说话的人的名字，msg表示信息
		//否则输出错误信息:
//			s==chatStat.frequency; 间隔太短
//			s==chatStat.nopermission; 无喇叭权限
//			s==chatStat.wrong; 默认，就是拒绝你
		switch(s){
		case success :
			chatPanel.insertChat("[大喇叭 " + speakerID + "]" + msg,puborpri.pub);break;
		case frequency :
			chatPanel.insertChat("[Warning]你说话太快了，喝口水吧",puborpri.pub);break;
		case nopermission :
			chatPanel.insertChat("[通知]你还没有喇叭，快去商城购买吧！",puborpri.pub);break;
		case wrong :
			chatPanel.insertChat("[错误]暂时不能发送",puborpri.pub);break;
		}
		}
	public void personChat(chatStat s,String sendid, String recvid, String msg){
		//同样先检查chatStat如果是success的话那么分开为 你对xxx说 和xxx 对你说
//			s==chatStat.frequency; 间隔太短
//			s==chatStat.nopermission; 你在对方黑名单中
//			s==chatStat.wrong; 默认，就是拒绝你
			switch(s){
			case success :
				chatPanel.insertChat("["+sendid+"对" + recvid + "说]" + msg,puborpri.pri);break;
			case frequency :
				chatPanel.insertChat("[Warning]你说话太快了，喝口水吧",puborpri.pri);break;
			case nopermission :
				chatPanel.insertChat("[通知]他好像不愿意和你说话",puborpri.pri);break;
			case wrong :
				chatPanel.insertChat("[错误]暂时不能发送",puborpri.pri);break;
			}
		}
	public void waitGameInit(){
//		session = gameBox.session;
		this.getLayeredPane().add(gameWindow,new Integer(Integer.MIN_VALUE));
		((JPanel)this.getContentPane()).setOpaque(false);
		background = new Picture("load2.jpg", getWidth(), getHeight(),0);
		gameWindow.img = background.getImage().getScaledInstance(getWidth(),getHeight(), Image.SCALE_DEFAULT);
		this.getContentPane().add(waitingPanel);
		setVisible(true);
	}
	public void setInitStage(double k){
		//传入一个小数，表示初始化完成了k*100%
		if(k>1) k = 1;
		else if(k<0) k = 0;
		waitingPanel.setStage(k);
		waitingPanel.repaint();
	}
	public void setMainRole(Entity mr){
		gameWindow.setMainRole(mr);
		mainRole = mr;
	}
	public void gameStart(){
		this.setVisible(false);
		waitingPanel.setVisible(false);
		background = new Picture("背景.jpg",1000,1000,0);
		this.getContentPane().add(desktopPane);
		desktopPane.setOpaque(false);
		desktopPane.setBounds(getBounds());
		desktopPane.add(popupInfoPanel,new Integer(Integer.MAX_VALUE));
		desktopPane.add(chatPanel);
		desktopPane.add(iconPanel);
		desktopPane.add(lockedPlayerPanel);
		desktopPane.add(playerInfoPanel);
		desktopPane.add(selfInfoPanel);
		desktopPane.add(bagPanel);
		desktopPane.add(skillPanel);
		desktopPane.add(friendPanel);
		desktopPane.add(exitPanel);
		for(int i=0;i<2;++i){
			imagebuffer[i] = createImage(getWidth(), getHeight());
//			ClientMain.mySleep(100);
			graphbuffer[i] = (Graphics2D)(imagebuffer[i].getGraphics());
			graphbuffer[i].setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON );
		    graphbuffer[i].setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY );
		    graphbuffer[i].setRenderingHint(RenderingHints. KEY_DITHERING,RenderingHints.VALUE_DITHER_ENABLE );
		}
		this.setVisible(true);//this.repaint();	
		new Thread(){
			public void run(){
				while(true){
					try {Thread.sleep(17);} catch (InterruptedException e) {}
					flashImg();
				}
			}
		}.start();
		new Thread(){
			public void run(){
				while(true){
					try {
						   FileInputStream bgmFile=new FileInputStream("bgm.wav" );
						   AudioStream bgm=new AudioStream(bgmFile);
						   AudioPlayer.player.start(bgm);
					}catch (Exception e) {System.out.println("音乐打开失败");}					
					try{
					    Thread.sleep(124000);//音频播放时间
					}catch(Exception e){System.out.println(e);}
				}
			}
		}.start();
	}
	public void addEntity(Entity e){
		boolean clickable = true;
		if(e.getType() != style.airplane) clickable = false;
		EntityLabel el = new EntityLabel(e, picMap.get(e.getPicID()),clickable);
		entityMap.put(e, el);
		gameWindow.add(el);
	}
	public void deleteEntity(Entity e){
		EntityLabel el = null;
		el = entityMap.remove(e);
		if(el == null) return;
		gameWindow.remove(el);
		deleteGIF(e);
	}
	public void addGIF(DoublePoint p,int id){
		GIFpicture gp = gifPicMap.get(id);
		if(gp == null) return;
		gifMap.put(p, new GIF(gp, p));
	}
	public void addGIF(DoublePoint p,int id,int turn){
		GIFpicture gp = gifPicMap.get(id);
		if(gp == null) return;
		gifMap.put(p, new GIF(gp, p,turn));
	}
	public void deleteGIF(DoublePoint p){
		gifMap.remove(p);
	}
	public void showPlayerInfo(Entity e){
		//TODO
		playerInfoPanel.show();
	}
	public enum FrameType{
		info,bag,skill,friend,exit;
	}
	public void showInnerFrame(FrameType type){
		switch(type){
		case info:
			if(selfInfoPanel.isVisible())
				selfInfoPanel.setVisible(false);
//			else selfInfoPanel.show();
			break;
		case bag:
			if(bagPanel.isVisible())
				bagPanel.setVisible(false);
			else bagPanel.showPanel();
			break;
		case skill:
			if(skillPanel.isVisible())
				skillPanel.setVisible(false);
			else skillPanel.showPanel();
			break;
		case friend:
			if(friendPanel.isVisible())
				friendPanel.setVisible(false);
//			else friendPanel.show();
			break;
		case exit:
			exitPanel.show();
			break;
		}
	}
	public void showPopupInfo(String info,int absX,int absY){
		absX -= getX();
		absY -= getY();
		popupInfoPanel.showInfo(info, absX, absY);
	}
	public void unshowPopupInfo(){
		popupInfoPanel.setVisible(false);
	}
	public void setLockedPlayer(Airplane airplane){
		lockPlayer = airplane;
		lockedPlayerPanel.setPlayer(airplane);
	}
	public void unsetLockedPlayer(){
		lockPlayer = null;
		lockedPlayerPanel.setVisible(false);
	}
	public void setPrivateChat(Entity e){
		chatPanel.setPrivateChat(e);
	}
}
