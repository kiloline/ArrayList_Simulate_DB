package Service_pkg.FileWriter;

import Service_pkg.count_HASH_code;
import Service_pkg.env_properties;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import m_Exception.Xml_Writer.fileWriter_delete_error;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 *
 * @author gosiple
 */
public class Xml_Writer 
{
    protected String rootpath; //这是要把文件保存的位置
    //protected String newTBname;
    protected Element root;
    private count_HASH_code chc;
    protected String Encoding;
    //protected File toWrite;  //这是当前进行操作的文件对象
    public Xml_Writer(String rootpath)
    {
        root=DocumentHelper.createElement("root");
        this.rootpath=rootpath;
        Encoding=env_properties.getEnvironment("Encoding");
    }
    public Element default_Write(String filetype) throws IOException
    {
        switch(filetype)
        {
            //case "tbfile":
            //{
                //root.addAttribute("tbname",this.newTBname);
            //}
            case "tbsfile":
            {
                root.addAttribute("tbsname","public");
                root.addAttribute("SHA-256","");
                root.addAttribute("table","0");
                break;
            }
            case "logfile":
            {
                break;
            }
            case "ctlfile":
            {
                Element tstf,logf;
                tstf=DocumentHelper.createElement("tstf");
                logf=DocumentHelper.createElement("logf");
                
                String hash=verifier_file(rootpath+"\\public.dbt.xml");
                tstf.addAttribute("Hash", hash);
                tstf.addAttribute("tablespace", "1");
                tstf.addElement("file_path").addAttribute("filename", "public").setText(rootpath+"\\public.dbt.xml");
                hash=verifier_file(rootpath+"\\log1.dbl.xml");
                logf.addAttribute("Hash", hash);
                logf.addAttribute("log", "1");
                logf.addElement("file_path").addAttribute("filename", "log1").setText(rootpath+"\\log1.dbl.xml");
                root.add(tstf);
                root.add(logf);
                break;
            }
        }
        return root;
    }
    
    
    public File create_File(String rootpath,String filename,String filetype) throws Exception
    {
        this.rootpath=rootpath;
        String filepath = null;
        switch(filetype)
        {
            case "tbsfile":
                new File(rootpath+"\\"+filename).mkdir();
                filepath=rootpath+"\\"+filename+".dbt.xml";
                break;
            case "logfile":
                filepath=rootpath+"\\"+filename+".dbl.xml";
                break;
            case "ctlfile":
                filepath=rootpath+"\\"+filename+".ctf.xml";
                break;
        }
        default_Write(filetype);
        File file=new File(filepath);
        save(file);
        return file;
    }
    protected void delete_File(String filename) throws fileWriter_delete_error
    {
        String filepath=rootpath+"\\"+filename;
        if(new File(filepath).delete())
            ;
        else
            throw new fileWriter_delete_error(filepath);
    }
    
    
    public void save(File file) throws Exception {
        OutputFormat format = new OutputFormat("    ", true);
        
        //file.delete();
        //创建文档
        Document document = DocumentHelper.createDocument(root.createCopy());
        //如果不用root.createCopy()的话，那么针对已经存在的文件（通过xml_reader读取的root），会报"The Node already has an existing document"的错误
        format.setEncoding(Encoding);//设置编码格式
        XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(file), format);
        xmlWriter.write(document);
        xmlWriter.close();
        //System.out.println("Save data success!");
        
        /*if (file.exists()) {
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(file), format);
            xmlWriter.write(document);
            xmlWriter.close();
            System.out.println("Save data success!");
        } else {
            
        }*/
    }
    
    protected String verifier_String(String[] Strings) throws IOException
    {
        StringBuffer sb=new StringBuffer();
        for(int loop=0;loop<Strings.length;loop++)
            sb.append(Strings[loop]);
        return chc.getHash(sb.toString());
    }
    protected String verifier_file(String... filenames) throws IOException
    {
        LinkedList<File> files=new LinkedList<>();
        
        for(int loop=0;loop<filenames.length;loop++)
            files.add(new File(filenames[loop]));
        return chc.getHash(files);
    }
}
