package Service_pkg.Fileloader;

import Data.classes.Table;
import Service_pkg.Filereader.table_reader;
import Service_pkg.Handling.table_handling;
import java.io.File;
import org.dom4j.DocumentException;

/**
 *
 * @author rkppo
 * ����������������Ҫ��⣬Ҳ�޷����
 * ֻ�������ļ����м��
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
