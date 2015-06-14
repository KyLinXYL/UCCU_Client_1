/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uccu_client;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Scanner;

/**
 *
 * @author xiaoshuang
 */

class Const
{
    static String[] gateAddress;
    static int[] gatePort;
    static String gameServerAddress;
    static int gameServerPort;
    static String DBAddress = "162.105.37.13";
    static int DBPort = 8898;
    static String LoginAddress = "162.105.37.13";
    static int LoginPort = 8798;
    
    
    
    static String blank = "           ";
    static long MIN_CHAT_INTERVAL = 1000;//最小聊天间隔
    
}


class UCCUTimer
{
    private long startTime;
    private long initTime;
    public UCCUTimer()
    {
        startTime = 0;
        initTime = 0;
    }
    public void reset(long start)//单位NanoSec
    {
        startTime = start;
        initTime = System.nanoTime();
    }
    public long getMS()
    {
        return (System.nanoTime() - initTime + startTime)/1000_000L;
    }
    public long getS()
    {
        return getMS()/1000L;
    }
    public long getMin()
    {
        return getMS()/60_000L;
    }
    public long getHour()
    {
        return getMS()/3_600_000L;
    }
    public String getString()
    {
        long total = getMS();
        total /= 1000L;
        long sec = total % 60;
        long min = (total/60) % 60;
        long hour = (total/3600) % 24;
        long day = (total/86400) % 365;
        long year = (total/31_536_000L);
        
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("years, ").append(day).append("days, ");
        sb.append(hour).append("hours, ").append(min).append("minutes, ");
        sb.append(sec).append("seconds.");
        return sb.toString();
    }
    public String getTimestamp()
    {
        long total = getMS();
        total /= 1000L;
        long sec = total % 60;
        long min = (total/60) % 60;
        long hour = (total/3600) % 24;
        long day = (total/86400) % 365;
        long year = (total/31_536_000L);
        
        return String.format("[%02d:%02d:%02d:%02d:%02d]", year, day, hour, min, sec);
    }
}



class Shell
{
    Scanner scr;
    public Shell()
    {
        scr = new Scanner(System.in);
    }
    public void startShell()
    {
        //System.out.println("Start Server Shell!");
        UccuLogger.log("Shell/Start", "Shell mode is on!");
        while(scr.hasNextLine())
        {
            String str = scr.nextLine();
        }
    }
    public String getLine()
    {
        scr.hasNextLine();
        return scr.nextLine();
    }
}


public class BasicLib {
    public static String md5(String str) {
        StringBuffer sb = new StringBuffer(32);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] tmp = md.digest(str.getBytes("GBK"));
            for(int i = 0; i < tmp.length; ++i)
            {
                sb.append(Integer.toHexString((tmp[i]&0xFF)|0x100).toUpperCase().substring(1, 3));
            }
        } catch (Exception e) {
            //System.out.println("Can't get MD5.");
            UccuLogger.warn("BasicLib/MD5", "Can't calculate the MD5! "+e);
            //e.printStackTrace();
            return null;
        }
        return sb.toString();
    }
}
