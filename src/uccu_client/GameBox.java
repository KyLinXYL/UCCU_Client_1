/* 游戏逻辑核心 */
package uccu_client;

import java.awt.List;
import java.awt.geom.Area;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class GameBox{
	final int sleeptime = 20; 
	//实体池，应为一个HashMap，以ID为索引，实体对象为元素
	public HashMap<Integer, Airplane> playerPool;
	//炮弹池，为一个List
	public ArrayList<Warhead> warheadPool;
	//本人信息，应该比其他玩家的信息更丰富
	public Mainrole mainrole;
	//与服务器绝对时间的差
	static long globalTimeDelta;
	static boolean lastping = false;
	//更改playerPool和warheadPool的锁
	private static Object lock_plane = new Object(); // static确保只有一把锁
	private static Object lock_bullet = new Object(); // static确保只有一把锁
	//系统聊天信息
	public static enum chatStat{success,frequency,nopermission,blacklist,wrong};
	Painter painter;
	public GameBox(){
		playerPool = new HashMap<Integer, Airplane>();
		warheadPool = new ArrayList<Warhead>();
		painter= new Painter(this);
	}
	//开始游戏
	public void startGame(){
		boolean roleSending=true;
		int i=0;
		//打开加载等待窗口，后台开始等待服务器详细角色信息的发送
		painter.waitGameInit();
		while(roleSending){
			if(i>100){
				UccuLogger.kernel("ClientServer/GameBox/startGame", "接收角色详细信息(000A)超时!(未接收到主角信息)");
				ClientMain.isGameOver=true;
				return;
			}
			if(mainrole!=null)
				roleSending=false;			
			painter.setInitStage(i++/100.0);
			ClientMain.mySleep(200);
		}
		painter.setInitStage(1);
		//加载完成 等待窗口关闭	正式开始游戏
		UccuLogger.kernel("ClientServer/GameBox/startGame", "接收主角详细信息(000A):关闭加载窗口,开始游戏!");
		painter.gameStart();
		new Thread(new ActionThread_plane(this)).start();
		new Thread(new ActionThread_bullet(this)).start();
	}
	//由decoder调用,更新各个角色的目标位置
	public void updateTarget(int id, int targetX, int targetY,long globalTime) {
		synchronized (lock_plane) {
			Airplane pl = playerPool.get(id);
			/* magic dont move!!! */
			int dt=(int)(System.currentTimeMillis()-globalTimeDelta-globalTime);
			int deltaX = targetX - (int)pl.posX;
			int deltaY = targetY - (int)pl.posY;
			double dL = Math.sqrt(deltaX*deltaX+deltaY*deltaY);
			double speed = pl.speed;
			double tmpspeed = speed*sleeptime*dL/(sleeptime*dL-dt*speed);
			if(tmpspeed <= 0) tmpspeed = speed;
			pl.deltaV = tmpspeed*sleeptime/1000;
			/* dont move!! */
			pl.targetX = targetX;
			pl.targetY = targetY;
			UccuLogger.log("Client/GameBox/updateTarget",
					"receive Package 000C(角色目标)");
			UccuLogger.log("Client/GameBox/updateTarget", "targetX: " + targetX
					+ "/targetY " + targetY);
		}
	}
	public void sendTargetPos(int targetX,int targetY){
//		updateTarget(ClientMain.mainID,targetX, targetY,System.currentTimeMillis()-globalTimeDelta);//先更新目标地址，再发送数据包
		SendingModule.sendMovingTarget(mainrole.getID(), targetX,targetY);
		}
	//将角色初始化,加入角色池并放入贴图池
	public void addCharacter(int id,String name,String describe,byte level,byte gender,
			int life,int curlife,int mana, int curmana, int atk,int def,int exp,
			int movespeed,int posX,int posY,int pid){
		UccuLogger.debug("ClientServer/GameBox/addCharacter", "Receive a package 000A(游戏中所有玩家信息)");
		if(id!=ClientMain.mainID){	//非主角玩家
			Airplane tmpPlane= new Airplane(id, pid, posX, posY, level, gender, movespeed, name, describe, life, curlife, mana, curmana, atk, def, exp);
//			Airplane tmpPlane= new Airplane(id, pid, posX, posY,name,level,gender);
			playerPool.put(id,tmpPlane);
			painter.addEntity(tmpPlane);
			UccuLogger.debug("ClientServer/GameBox/addCharacter", "000A:加入一个非主角玩家:"+name);
		}
		else{	//主角玩家
			Mainrole tmpMain=new Mainrole(id, pid, posX, posY, level, gender, movespeed, name, describe, life, curlife, mana, curmana, atk, def, exp);
			playerPool.put(id,tmpMain);
			painter.addEntity(tmpMain);
			painter.setMainRole(tmpMain);
			mainrole=tmpMain;
			for(int i=0;i<3;++i)
				mainrole.add_skills(0, 5, 5, 5);
			UccuLogger.debug("ClientServer/GameBox/addCharacter", "000A:加入一个主角玩家:"+name);
		}
	}	
	//攻击函数,由当前游戏窗体攻击事件触发
	public void attack(int attack,int tar){
		synchronized (lock_bullet) {
			Airplane attacker=playerPool.get(attack);
			Airplane target=playerPool.get(tar);
			Warhead warhead=attacker.attack(target);
			warheadPool.add(warhead);
			painter.addEntity(warhead);		
		}
	}
	public void bloodUp(int tar){
		painter.addGIF(playerPool.get(tar), 10, 5);
	}
	public void renewCharacter(int id,String name,String describe,byte level,byte gender,
			int life,int curlife,int mana, int curmana, int atk,int def,int exp,
			int movespeed,int posX,int posY,int pid){
		Airplane renewair=playerPool.get(id);
		renewair.name=name;
		renewair.describe=describe;
		renewair.level=level;
		renewair.gender=gender;
		renewair.life= life;
		renewair.curlife=curlife;
		renewair.hp = (double)life/(double)curlife;
		renewair.mana=mana;
		renewair.curmana=curmana;
		renewair.atk=atk;
		renewair.def=def;
		renewair.exp=exp;
		renewair.speed=movespeed;
//		renewair.posX=posX;
//		renewair.posY=posY;
	}
	//Action_plane线程专门负责处理每20ms之后所有飞机的位置
	private class ActionThread_plane implements Runnable{
		GameBox gameBox;
		public ActionThread_plane(GameBox gb) {
			gameBox = gb;	//获取指示变量的引用
		}
		int sleeptime = 20;
		@Override
		public void run() {
			while(true){
				ClientMain.mySleep(sleeptime);
				synchronized (lock_plane) {
					Iterator<?> iter = gameBox.playerPool.keySet().iterator();
					Airplane plane;
					double deltaX, deltaY;
					while (iter.hasNext()) {
						plane = gameBox.playerPool.get(iter.next());
						deltaX = plane.targetX - plane.posX;
						deltaY = plane.targetY - plane.posY;
						int face = deltaX > 0 ? 0 : 1;
						if (deltaX != 0)
							plane.angle = Math.PI / 2 + Math.PI * face+ (Math.atan(deltaY / deltaX));
						/* 我修改了你的移动方法 不然deltaX很小或者deltaY是负数时会出错
						 * 这里采用总速度不变，横纵速度按比例变化的方法 最后如果delta比速度的步长短时，只移动delta*/
						double tmp = plane.deltaV/Math.sqrt(deltaX * deltaX + deltaY * deltaY);
						if (tmp > 1)
							tmp = 1;// 比例大于1说明步长大于delta，将比例修改为1，否则会不能停止移动，反复在原地抖动
						// speed 是其x轴速度,deltaXY只是用来算角度的
						plane.posX += deltaX * tmp;
						plane.posY += deltaY * tmp;
					}
				}
			}
		}		
	}
	private class ActionThread_bullet implements Runnable{
		GameBox gameBox;
		public ActionThread_bullet(GameBox gb) {
			gameBox = gb;	//获取指示变量的引用
		}
		@Override
		public void run() {
			while(true){
				ClientMain.mySleep(20);
				synchronized (lock_bullet) {
					Iterator<?> iter = gameBox.warheadPool.iterator();
					Warhead warhead;
					Airplane targetPlane;
					double deltaX, deltaY;
					while (iter.hasNext()) {
						warhead = (Warhead) iter.next();
						targetPlane=warhead.targetAirplane;
						deltaX = targetPlane.posX-warhead.posX;
						deltaY = targetPlane.posY-warhead.posY;
						if(deltaX==0 && deltaY==0){
							painter.deleteEntity(warhead);
							iter.remove();
							continue;
						}
						int face = deltaX > 0 ? 0 : 1;
						if (deltaX != 0)
							warhead.angle = Math.PI / 2 + Math.PI * face+ (Math.atan(deltaY / deltaX));
						/* 我修改了你的移动方法 不然deltaX很小或者deltaY是负数时会出错
						 * 这里采用总速度不变，横纵速度按比例变化的方法 最后如果delta比速度的步长短时，只移动delta*/
						double tmp = warhead.speed/ Math.sqrt(deltaX * deltaX + deltaY * deltaY);
						if (tmp > 1)
							tmp = 1;// 比例大于1说明步长大于delta，将比例修改为1，否则会不能停止移动，反复在原地抖动
							// speed 是其x轴速度,deltaXY只是用来算角度的
						warhead.posX += deltaX * tmp;
						warhead.posY += deltaY * tmp;
						
					}
				}
			}
		}
	}
}
