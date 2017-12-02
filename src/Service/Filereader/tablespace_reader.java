package Service.Filereader;

import Service.Fileloader.Table_File;
import Service.Fileloader.Tablespace_File;
import Service.Handling.table_handling;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import m_Exception.xml_reader.fileReader_error;
import org.dom4j.DocumentException;
import org.dom4j.Element;

/**
 *
 * @author rkppo
 */
public class tablespace_reader extends Xml_Reader 
{
    table_handling th;
    public tablespace_reader(File file,boolean check_file,table_handling th) throws DocumentException, fileReader_error, IOException
    {
        super();
        setFile(file);
        if(check_file)
        {
            if(check_files())
                ;
            else
                throw new fileReader_error("��ռ��ļ�һ���Լ��δͨ����"+getRoot().attribute("tbsname").getValue()+"�ѱ��Ƿ�����");
        }
        this.th=th;
    }
    public Table_File[] get_tbfiles(Tablespace_File dbfile) throws DocumentException, fileReader_error, Exception
    {
        LinkedList<Table_File> files = new LinkedList<>();
        List<Element> file_list=super.getElements("file_path");
        if(file_list.size()!=Integer.valueOf(root.attribute("table").getValue()))
            throw new fileReader_error("��ռ��ļ�һ���Լ��δͨ������ռ��ļ��ѱ��Ƿ�����");
        for(int loop=0;loop<file_list.size();loop++)
        {
            File f=new File(file_list.get(loop).getText());
            /*try {
                this.verifier_file(f, file_list.get(loop).attributeValue("SHA-256"));
            } catch (IOException ex) {
                throw new fileReader_error("���ݱ��ļ�һ���Լ��δͨ����"+file_list.get(loop).getText()+"�ѱ��Ƿ�����");
            }*/
            files.add(new Table_File(dbfile,file_list.get(loop).attribute("filename").getValue(),f,false,th));
        }
        return files.toArray(new Table_File[0]);
    }
    
    private boolean check_files() throws DocumentException, IOException
    {
        List<Element> file_list;
        String[] files;
        String SHA256;
        //SHA256=super.getElements("logf").get(0).attribute("SHA-256").getValue();
        SHA256=getRoot().attribute("Hash").getValue();
        file_list=getRoot().elements();
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
        return verifier_strings(files, SHA256);
    }
}
