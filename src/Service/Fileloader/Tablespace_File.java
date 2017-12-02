package Service.Fileloader;

import Service.FileWriter.Table_writer;
import Service.FileWriter.Tablespace_writer;
import Service.Filereader.tablespace_reader;
import Service.Handling.table_handling;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import m_Exception.xml_reader.fileReader_error;
import org.dom4j.DocumentException;

/**
 *
 * @author rkppo
 */
public class Tablespace_File extends DB_File_Abstract<Control_File,Table_File>
{//此类型的特定方法只在初始化的时候使用
    tablespace_reader tr;
    HashMap<String,Boolean> isSave;
    public Tablespace_File(Control_File father, String filename, File file,boolean detectHash,table_handling th) throws DocumentException, fileReader_error, Exception 
    {
        super(father, file , filename,  detectHash);
        tr=new tablespace_reader(file,detectHash,th);
        fullpath=path+"\\"+filename+".dbt.xml";
        path=path+"\\"+filename;
        isSave=new HashMap<>();
        File f=new File(path);
        if(!f.isDirectory())
            f.mkdir();
        set();
        String[] sons=getSons();
        for(int loop=0;loop<sons.length;loop++)
            isSave.put(sons[loop], Boolean.FALSE);
    }
    public Tablespace_File(Control_File father, String path, String filename,boolean detectHash) 
    {
        super(father, path, filename,detectHash);
        fullpath=path+"\\"+filename+".dbt.xml";
        this.path=path+"\\"+filename;
        File f=new File(this.path);
        if(!f.isDirectory())
            f.mkdir();
        isSave=new HashMap<>();
    }
    private void set() throws DocumentException, fileReader_error, Exception
    {
        this.setSons(tr.get_tbfiles(this));
    }
    
    @Override
    public void addSon(Table_File tbfile) throws Exception
    {
        new Table_writer(this.path).save_table(tbfile.getTable());
        Son.put(tbfile.getfilename(), tbfile);
        isSave.put(tbfile.getfilename(), Boolean.FALSE);
        new Tablespace_writer(path).save_tablespace(this);
    }
    @Override
    public void delSon(String del) throws Exception 
    {
        new Tablespace_writer(path).save_tablespace(this);
        Son.remove(del).getFile().delete();
        isSave.remove(del);
    }
    @Override
    public void setSons(Table_File[] down) throws Exception 
    {
        for(int loop=0;loop<down.length;loop++)
        {
            this.Son.put(down[loop].getfilename(),down[loop]);
        }
    }
    public void changestatus(String tbname)
    {//当对Table对象进行更改的时候要通过这个函数通知文件系统
        isSave.put(tbname, Boolean.TRUE);
    }
    public void saveTables() throws Exception
    {
        Table_writer tw=new Table_writer(super.getPath());
        String[] sons=super.getSons();
        for(int loop=0;loop<sons.length;loop++)
        {
            if(isSave.get(sons[loop]))
            {
                Table_File tf=super.getSon(sons[loop]);
                
                tw.save_table(tf.getTable());
                isSave.put(sons[loop], false);
            }
        }
    }

}
