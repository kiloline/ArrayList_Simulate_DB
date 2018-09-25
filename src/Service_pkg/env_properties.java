/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service_pkg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author rkppo
 * 以后将dbenv的初始化放到这个位置，用饿汉式单例来提供服务
 * 避免整个程序到处新建properties类
 */
public class env_properties {
    private static final env_properties init=new env_properties();
    private static Properties env;
    private env_properties()
    {
        String debug="debug"; //debug,building
        boolean load=false;
        env=new Properties();
        FileInputStream FIS;
        try {
            if(debug.equals("debug"))
            {
                FIS=new FileInputStream(new File("src/Service_pkg/db_env_conf.properties"));
                env.load(FIS);
            }
            else if(debug.equals("building"))
            {
                String path= System.getProperty("java.class.path");
                int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
                int lastIndex = path.lastIndexOf(File.separator) + 1;
                path=path.substring(firstIndex, lastIndex)+"db_env_conf.properties";
                FIS=new FileInputStream(path);
                env.load(FIS);
            }
            else return;
            FIS.close();
        } catch (FileNotFoundException ex) {
            System.out.println("环境配置文件不存在！");
            load=true;
        } catch (IOException ex) {
            System.out.println("环境配置文件无法读取！");
            load=true;
        }
        finally //通过finally加载默认参数，防止程序崩溃
        {
            if(load)
            {
                env.setProperty("listlength", "15");
                env.setProperty("detectHash", "false");
                env.setProperty("HASHmethod", "MD5");
                env.setProperty("Encoding", "GBK");
                env.setProperty("LogSize", "100");
                env.setProperty("Execcache", "close");
                env.setProperty("default_tbs", "public");
                System.out.println("默认参数已加载");
            }
        }
    }
    
    public static String getEnvironment(String envstring)
    {
        return init.env.getProperty(envstring);
    }
    public static void setEnvironment(String envname,String string)
    {
        init.env.setProperty(envname,string);
    }
}
