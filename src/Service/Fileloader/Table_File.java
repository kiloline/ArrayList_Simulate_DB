package Service.Fileloader;

import Data.classes.Table;
import Service.Filereader.table_reader;
import Service.Handling.table_handling;
import java.io.File;
import org.dom4j.DocumentException;

/**
 *
 * @author rkppo
 * 对于数据来讲不需要检测，也无法检测
 * 只能整个文件进行检测
 */
public class Table_File extends DB_File_Abstract<Tablespace_File,Object>
{
    Table table;
    public Table_File(Tablespace_File father, String filename, File file,boolean detection,table_handling th) throws DocumentException, Exception 
    {
        super(father, file,filename, detection);
        fullpath=path+"\\"+filename+".dba.xml";
        table=new table_reader(this,detection).getTable(th);
    }
    public Table_File(Tablespace_File father, String path,Table table,boolean detection) 
    {
        super(father, path, table.toString(),detection);
        fullpath=path+"\\"+filename+".dba.xml";
        this.table=table;
    }
    
    
    public Table getTable()
    {
        return this.table;
    }

    @Override
    /**@deprecated */
    public void addSon(Object add) {}

    @Override
    /**@deprecated */
    public void delSon(String del) {}
    @Override
    /**@deprecated */
    public void setSons(Object[] add) {}
}
