package Service.FileWriter;

import Service.Fileloader.Control_File;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import m_Exception.FileSystem.ClassNotFound;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 *
 * @author rkppo
 */
public class control_writer extends Xml_Writer
{
    public control_writer(String path,Element Root) 
    {
        super(path);
        root=Root;
    }

    
    public void addLogfile(String filename) throws Exception
    {
        super.create_File(rootpath, filename, "logfile");
    }
    public void addTBSfile(String ctfpath,String filename) throws Exception
    {
        //super.create_File(rootpath, filename, "tbsfile");
        Element tstf=root.element("tstf");
        String SHA256;
        List<Element> file_list=tstf.elements();
        String[] files=new String[file_list.size()+1];
        for(int loop=0;loop<file_list.size();loop++)
            files[loop]=file_list.get(loop).getText();
        files[files.length-1]=rootpath+"\\"+filename+".dbt.xml";
        
        SHA256=verifier_file(files);
        //下面将字符引用替换成实体引用
        //SHA256=SHA256.replace("&#", "&amp;#");
        SHA256=SHA256.replace("&", "&amp;");
        SHA256=SHA256.replace("<", "&lt;");
        SHA256=SHA256.replace(">", "&gt;");
        SHA256=SHA256.replace("\'", "&apos;");
        SHA256=SHA256.replace("\"", "&quot;");
        
        tstf.addElement("file_path").addAttribute("filename", filename).addText(rootpath+"\\"+filename+".dbt.xml");
        tstf.attribute("Hash").setValue(SHA256);
        tstf.attribute("tablespace").setValue(Integer.toString(files.length));
        this.save(new File(ctfpath));
        //施工中
    }
    public void delLogfile(String filename) throws Exception
    {
        super.delete_File(filename);
    }
    public void delTBSfile(String ctfpath,String filename) throws Exception
    {
        Element toDel = null;
        boolean find=false;//同时使用
        Element tstf=root.element("tstf");
        String SHA256;
        Iterator<Element> iter=tstf.elements().iterator();
        while(iter.hasNext())
        {
            toDel=iter.next();
            if(toDel.attribute("filename").getValue().equals(filename))
            {
                find=true;
                break;
            }
        }
        if(find==true)
            tstf.remove(toDel);
        else
            throw new ClassNotFound("have no element");
        
        List<Element> file_list=tstf.elements();
        String[] files=new String[file_list.size()];
        for(int loop=0;loop<file_list.size();loop++)
            files[loop]=file_list.get(loop).getText();
        
        SHA256=verifier_file(files);
        //下面将字符引用替换成实体引用
        SHA256=SHA256.replace("&#", "&amp;#");
        SHA256=SHA256.replace("&", "&amp;");
        SHA256=SHA256.replace("<", "&lt;");
        SHA256=SHA256.replace(">", "&gt;");
        SHA256=SHA256.replace("\'", "&apos;");
        SHA256=SHA256.replace("\"", "&quot;");
        
        tstf.attribute("Hash").setValue(SHA256);
        tstf.attribute("tablespace").setValue(Integer.toString(files.length));
        this.save(new File(ctfpath));
    }
}
