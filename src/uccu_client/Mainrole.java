package uccu_client;

public class Mainrole extends Airplane {
	static public Item[] pre_items;
	static public Skill[] pre_skills;
	public Item[] items= new Item[32];//物品数组
	public Skill[] skills = new Skill[32];//技能数组
	public Mainrole(int id, int picid, double x, double y,byte l,byte g,int speed,
				String name, String describe,int life,int curlife,int mana,int curmana,
				int atk,int def, int exp) {
		super(id, picid, x, y, l, g, speed, name, describe, life, curlife, mana, curmana, atk, def, exp);
//		super(id, picid, x, y,name,level,gender);
		for(int i=0;i<32;++i){
			items[i]=new Item();
			skills[i]=new Skill();
		}	
	}
	public void deleteAllItem(){
		for(int i=0;i<32;++i){
			items[i]=new Item();
		}	
	}
	public void add_items (int ID,int num,int instanceID){
		int i=0;
		for(;i<items.length;++i){
			if(items[i].empty||items[i].num==0){
				break;
			}
		}
		if(i >= items.length) return;
		items[i].empty=false;
		items[i].picID=pre_items[ID].picID;
		items[i].num=num;
		items[i].name=pre_items[ID].name;
		items[i].describtion = pre_items[ID].describtion;	
		items[i].instanceID = instanceID;
	}
	public void add_skills(int picID,int instanceID,int level,int exp){
		int i=0;
		for(;i<skills.length;++i)
			if(skills[i].empty)	break;
		skills[i].empty = false;
		skills[i].picID = picID;	
		skills[i].instanceID = instanceID;
		skills[i].level = level;
		skills[i].exp = exp;
		if(picID==1000000001||picID==1){
			skills[i].name="全局聊天";
			skills[i].describtion="向宇宙空间广播\n信息，所有飞行器\n都能收到\n你的消息";		
		}
		else if(picID==1000000002||picID==2){
			skills[i].name="私聊";
			skills[i].describtion="通过严格加密的\n精确无线电波,\n向目标飞行器\n发送讯息";		
		}
		else if(picID==1000000003||picID==3){
			skills[i].name="热跟踪导弹";
			skills[i].describtion="导弹上的探测器\n捕获目标辐射的\n能量,自动制导,\n点对点轰炸";		
		}
	}
}

