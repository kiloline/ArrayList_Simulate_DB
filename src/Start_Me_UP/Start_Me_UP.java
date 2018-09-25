package Start_Me_UP;

import Service_pkg.Service;
import Start_Me_UP.create_DB.create_DB;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rkppo
 */
public class Start_Me_UP 
{
    public static void main(String[] args)
    {
        String property_file="src/Start_Me_UP/db_SIDs.properties";
        FileInputStream FIS;
        FileOutputStream FOS;
        Scanner scan=new Scanner(System.in);
        System.out.print("请输入要打开的数据库的名称：");
        String SID=scan.next();
        Properties prop=new Properties();
        try {
            FIS=new FileInputStream(property_file);
            prop.load(FIS);
            FIS.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Start_Me_UP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Start_Me_UP.class.getName()).log(Level.SEVERE, null, ex);
        }
        String rootPath=prop.getProperty(SID);
        if(rootPath==null)
        {
            System.out.print("您要打开的库不存在，是否新建数据库？(y/n)：");
            scan=new Scanner(System.in);
            if(scan.nextLine().toLowerCase().equals("y"))
            {
                //scan.close();
                rootPath=new create_DB(SID).return_path();
            }
            else
                exit(0);
            prop.setProperty(SID, rootPath);
            try {
                FOS=new FileOutputStream(property_file);
                prop.store(FOS,"new input");
                FOS.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Start_Me_UP.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Start_Me_UP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Service service=new Service(rootPath,SID);
        service.running();
    }
}
