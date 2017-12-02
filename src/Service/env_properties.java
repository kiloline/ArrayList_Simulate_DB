/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rkppo
 * �Ժ�dbenv�ĳ�ʼ���ŵ����λ�ã��ö���ʽ�������ṩ����
 * �����������򵽴��½�properties��
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
                FIS=new FileInputStream(new File("src/Service/db_env_conf.properties"));
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
            System.out.println("���������ļ������ڣ�");
            load=true;
        } catch (IOException ex) {
            System.out.println("���������ļ��޷���ȡ��");
            load=true;
        }
        finally //ͨ��finally����Ĭ�ϲ�������ֹ�������
        {
            if(load)
            {
                env.setProperty("listlength", "15");
                env.setProperty("detectHash", "false");
                env.setProperty("HASHmethod", "MD5");
                env.setProperty("Encoding", "GBK");
                env.setProperty("LogSize", "100");
                env.setProperty("Execcache", "close");
                System.out.println("Ĭ�ϲ����Ѽ���");
            }
        }
    }
    
    public static String getEnvironment(String envstring)
    {
        return init.env.getProperty(envstring);
    }
}
