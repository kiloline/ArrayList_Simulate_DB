package Service.FileWriter;

import Service.Fileloader.Tablespace_File;
import Service.count_HASH_code;
import java.io.File;

/**
 *
 * @author rkppo
 */
public class Tablespace_writer extends Xml_Writer
{
    count_HASH_code cHc;
    public Tablespace_writer(String rootPath)
    {
        super(rootPath);
    }
    
    public void save_tablespace(Tablespace_File tbsfile) throws Exception
    {
        //String filepath=rootpath+tbsfile.getfilename()+".dbt.xml";//+"/"
        File file=tbsfile.getFile();
        
        String[] tbnames=tbsfile.getSons();
        String tbsname=tbsfile.getfilename();
        StringBuffer sb=new StringBuffer();
        for(int loop=0;loop<tbnames.length;loop++)
        {
            sb.append(rootpath+'\\'+tbnames[loop]+".dba.xml");
        }
        String SHA256=cHc.getHash(sb.toString());
        //下面将字符引用替换成实体引用
        //SHA256=SHA256.replace("&#", "&amp;#");
        //SHA256=SHA256.replace("\\", "r/");
        SHA256=SHA256.replace("&", "");
        SHA256=SHA256.replace("<", "&lt;");
        SHA256=SHA256.replace(">", "&gt;");
        SHA256=SHA256.replace("\'", "&apos;");
        SHA256=SHA256.replace("\"", "&quot;");
        
        root.addAttribute("tbsname", tbsfile.getfilename()); 
        root.addAttribute("Hash", SHA256); 
        root.addAttribute("table", String.valueOf(tbsfile.getSons().length)); 
        for(int loop=0;loop<tbnames.length;loop++)
            root.addElement("file_path").addAttribute("filename", tbnames[loop]).addText(rootpath+"\\"+tbnames[loop]+".dba.xml");
        
        this.save(file);
    }
}
