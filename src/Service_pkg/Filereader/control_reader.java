package Service_pkg.Filereader;

import Service_pkg.Fileloader.Control_File;
import Service_pkg.Fileloader.DB_File_Abstract;
import Service_pkg.Fileloader.Log_File;
import Service_pkg.Fileloader.Tablespace_File;
import Service_pkg.Handling.table_handling;
import m_Exception.xml_reader.fileReader_error;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author gosiple
 */
public class control_reader extends Xml_Reader
{
    table_handling th;
    public control_reader(File file,boolean check_file,table_handling th) throws fileReader_error, DocumentException, IOException
    {
        super();
        setFile(file);
        if(check_file)
        {
        if(check_files())
            ;
        else
            throw new fileReader_error("控制文件一致性检查未通过，控制文件已被非法更改");
        }
        this.th=th;
    }
    public <E>E[] get_files(Control_File dbfile,String lab,boolean detectHash,E[] inputArray) throws DocumentException, fileReader_error, Exception
    {
        LinkedList<DB_File_Abstract> files = new LinkedList<>();
        List<Element> file_list=super.getElements(lab).get(0).elements();
        switch(lab)
        {
            case "logf":
                for(int loop=0;loop<file_list.size();loop++)
                {
                    files.add(new Log_File(dbfile,new File(file_list.get(loop).getText()),file_list.get(loop).attribute("filename").getValue(),detectHash));
                }
                break;
            case "tstf":
                for(int loop=0;loop<file_list.size();loop++)
                {
                    files.add(new Tablespace_File(dbfile,file_list.get(loop).attribute("filename").getValue(),new File(file_list.get(loop).getText()),detectHash,th));
                }
                break;
        }
        
        return files.toArray(inputArray);
    }
    private boolean check_files() throws DocumentException, IOException
    {
        List<Element> file_list;
        String[] files;
        String SHA256;
        boolean log,tsb;
        //SHA256=super.getElements("logf").get(0).attribute("SHA-256").getValue();
        SHA256=getRoot().element("logf").attribute("Hash").getValue();
        file_list=getRoot().element("logf").elements();
        if(file_list.size()==0)
        {
            SHA256="";
            files=new String[0];
        }
        else
        {
            files=new String[file_list.size()];
            for(int loop=0;loop<file_list.size();loop++)
            {
                files[loop]=file_list.get(loop).getText();
            }
        }
        log=verifier_file(SHA256, files);
        SHA256=getRoot().element("tstf").attribute("Hash").getValue();
        file_list=getRoot().element("tstf").elements();
        if(file_list.size()==0)
        {
            SHA256="";
            files=new String[0];
        }
        else
        {
            files=new String[file_list.size()];
            for(int loop=0;loop<file_list.size();loop++)
            {
                files[loop]=file_list.get(loop).getText();
            }
        }
        tsb=verifier_file(SHA256, files);
        return log&tsb;
    }
}
