package Start_Me_UP.create_DB;

import Service.FileWriter.Xml_Writer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static java.lang.System.exit;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rkppo
 */
public class create_DB 
{
    private String SID,rootpath;
    private Scanner scan;
    private Properties prop;
    public create_DB(String SID)
    {
        this.SID=SID;
        String Encoding,System_Encode;
        byte[] codechange;
        try {
            prop=new Properties();
            prop.load(new FileInputStream(new File("src/Service/db_env_conf.properties")));
            Encoding=prop.getProperty("Encoding");
            System_Encode=System.getProperty("sun.jnu.encoding");
        } catch (Exception ex) {
            System_Encode="GBK";
            Encoding="GBK";
        }
        scan=new Scanner(System.in);
        System.out.println("请输入要创建数据库文件的保存位置：");
        rootpath=scan.next();
        File rootdir=new File(rootpath);
        if(!rootdir.isDirectory())//!rootdir.exists()&&
            rootdir.mkdirs();
        //try {
        //    codechange=rootpath.getBytes(System_Encode);
        //    rootpath=new String(codechange,Encoding);
        //} catch (UnsupportedEncodingException ex) {
        //    Logger.getLogger(create_DB.class.getName()).log(Level.SEVERE, null, ex);
        //    exit(-1);
        //}
        try {
            new Xml_Writer(rootpath).create_File(rootpath,"public","tbsfile");
            new Xml_Writer(rootpath).create_File(rootpath,"log1","logfile");
            new Xml_Writer(rootpath).create_File(rootpath,SID,"ctlfile");//xml_Writer xW=
        } catch (Exception ex) {
            Logger.getLogger(create_DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public create_DB(String SID,String rootpath)
    {
        this.SID=SID;
        this.rootpath=rootpath;
        try {
            new Xml_Writer(rootpath).create_File(rootpath,SID,"ctlfile");//xml_Writer xW=
            new Xml_Writer(rootpath).create_File(rootpath,"public","tbsfile");
            new Xml_Writer(rootpath).create_File(rootpath,"log1","logfile");
        } catch (Exception ex) {
            Logger.getLogger(create_DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String return_path()
    {
        return rootpath;
    }
    
    public static void main(String[] args)
    {
        System.out.println("将会创建数据库…");
        Scanner scanf=new Scanner(System.in);
        String dbname;
        System.out.println("请输入要创建数据库的名称：");
        //dbname = scanf.next();
        dbname="zhzm";
        String rootPath;
        System.out.println("请输入要创建数据库文件的保存位置：");
        //rootPath = scanf.next();
        rootPath="E:\\db\\tksj";
        create_DB cdb=new create_DB(dbname,rootPath);
        System.out.println(cdb.return_path());
    }
}
