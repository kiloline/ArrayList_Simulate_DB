package Service.Fileloader;

import Service.FileWriter.Tablespace_writer;
import Service.FileWriter.control_writer;
import Service.Filereader.control_reader;
import Service.Handling.table_handling;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import m_Exception.xml_reader.fileReader_error;
import org.dom4j.DocumentException;

/**
 *
 * @author rkppo
 * @param <Object>
 * @param <Tablespace_File>
 */
public class Control_File extends DB_File_Abstract <Object,Tablespace_File>
{
    control_reader cr;
    Log_File[] logs;
    Tablespace_File[] tsfs;

    public Control_File(String SID, File file,boolean detectHash,table_handling th) throws DocumentException, fileReader_error, IOException, Exception 
    {//此类型的特定方法只在初始化的时候使用
        super(null,file,SID,detectHash);
        cr=new control_reader(file,detectHash,th);
        fullpath=path+"\\"+filename+".ctf.xml";
        set();
    }
    private void set() throws DocumentException, fileReader_error, Exception
    {
        logs=cr.get_files(this,"logf",detectHash,new Log_File[0]);
        tsfs=cr.get_files(this,"tstf",detectHash,new Tablespace_File[0]);
        this.setSons(tsfs);
    }
    
    public Log_File[] getlogs()
    {
        return this.logs;
    }
    public Tablespace_File[] gettsfs()
    {
        return this.Son.values().toArray(new Tablespace_File[0]);
    }
    public String[] gettbsname()
    {
        return super.getSons();
    }

    @Override
    public void addSon(Tablespace_File add) throws Exception
    {
        new Tablespace_writer(path).save_tablespace(add);
        Son.put(add.getfilename(), add);
        new control_writer(path,cr.getRoot()).addTBSfile(fullpath,add.getfilename());
    }

    @Override
    public void delSon(String del) 
    {
        try {
            new control_writer(path,cr.getRoot()).delTBSfile(fullpath,del);
        } catch (Exception ex) {
            System.out.println("error:"+del+" "+ex.getMessage());
        }
        Son.remove(del).getFile().delete();
    }

    @Override
    public void setSons(Tablespace_File[] down) throws Exception 
    {
        for(int loop=0;loop<down.length;loop++)
        {
            this.Son.put(down[loop].getfilename(),down[loop]);
        }
    }
    
    
}
