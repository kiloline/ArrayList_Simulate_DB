package Service.Fileloader;

import Data.classes.Table;
import Service.Handling.table_handling;
import java.io.File;
import java.util.HashMap;
import m_Exception.xml_reader.fileReader_error;
import org.dom4j.DocumentException;

/**
 * @author rkppo
 */
public class FileSystem_link_tree {
    private boolean file_detection;
    private String rootpath;
    private Control_File ControlFile;
    private Log_File[] Redo_Logs;//������DB_File�൱��ControlFile�����ļ�ָ��Ŀ�ݷ�ʽ��������Ҫʱ��ά��
    private Tablespace_File[] TableSpace;//������ʵ�������ļ�ʱ����ֱ�Ӵ�������ã�����һЩ����Ҫ��Ѱַ
    
    //��Ҫ������ʱ��ʹ�������DB_File���飬��Ҫ���õ�ʱ��ʹ������Ĺ�ϣ��
    //���ݲ�֪����HashMapԴ����������HashMap.putʹ�õ��Ǵ�ָ�뷽ʽ���и�ֵ���������͹�ϣ���е�Ԫ��Ӧ������һ����
    private HashMap<String,Tablespace_File> tbs_name;
    //��������û�г�ʼ��
    public FileSystem_link_tree(String rootpath,String dbname,boolean detectHash,table_handling th) throws DocumentException, fileReader_error, Exception
    {
        tbs_name=new HashMap<>();
        file_detection=detectHash;
        this.rootpath=rootpath;
        
        File ctf=new File(rootpath+'\\'+dbname+".ctf.xml");
        ControlFile=new Control_File(dbname,ctf,detectHash,th);//��ʼ��ControlFile��ʱ��Ҫ���ν������ļ���ʼ��
        freshlink();
    }
    public void freshlink()//ά���ļ�ϵͳ�Ŀ��ٵ��û���
    {
        this.Redo_Logs=this.ControlFile.getlogs();
        this.TableSpace=this.ControlFile.gettsfs();
        tbs_name.clear();
        for(int loop=0;loop<this.TableSpace.length;loop++)
        {
            tbs_name.put(TableSpace[loop].getfilename(), TableSpace[loop]);
        }
    }
    public boolean checkFile(String tbsname,String tbname)
    {
        if(tbs_name.get(tbsname)==null)
            return false;
        else if(tbname==null)
            return true;
        else if(tbs_name.get(tbsname).getSon(tbname)==null)
            return false;
        else return true;
    }
    public Table getTable(String tbsname,String tbname)
    {
        Table_File tf=(Table_File) tbs_name.get(tbsname).getSon(tbname);
        return tf.getTable();
    }
    public void changestatus(String tbsname,String tbname)
    {
        tbs_name.get(tbsname).changestatus(tbname);
    }
    public void add_tablespace(String tbsname) throws Exception//����ļ����������һ��String��
    {
        Tablespace_File tbs=new Tablespace_File(ControlFile,rootpath,tbsname,file_detection);
        ControlFile.addSon(tbs);
        freshlink();
        //return tbs;
    }
    public void add_table(String tbsname,Table table) throws Exception
    {//���������ֱ�Ӵ���Table����Ļ�����ô��Ҫ�����Ĵ��ݺܶཨ�����
        Table_File tb=new Table_File(tbs_name.get(tbsname),rootpath,table,file_detection);
        tbs_name.get(tbsname).addSon(tb);
        //freshlink();
    }
    public void del_tablespace(String tbsname) throws Exception
    {//��ʱ�����Ǵ�Windows�ļ�ϵͳ����ɾ���ļ�������
        String[] sons=tbs_name.get(tbsname).getSons();
        for(int loop=0;loop<sons.length;loop++)//���ȵݹ�ɾ����ռ������еı�
            del_table(tbsname,sons[loop]);
        ControlFile.delSon(tbsname);
        freshlink();
    }
    public void del_table(String tbsname,String tbname) throws Exception
    {//��ʱ�����Ǵ�Windows�ļ�ϵͳ����ɾ���ļ�������
        tbs_name.get(tbsname).delSon(tbname);
    }
    public void saveTable() throws Exception
    {
        for(int loop=0;loop<TableSpace.length;loop++)
        {
            TableSpace[loop].saveTables();
        }
    }
    
    public void printDBTree()
    {
        String tb="|";
        System.out.println(ControlFile.getfilename());
        for(int loop=0;loop<TableSpace.length;loop++)
        {
            Tablespace_File t=TableSpace[loop];
            System.out.println(tb+"--"+t.getfilename());
            if(loop==TableSpace.length-1)
                tb=" ";
            for(String s:t.getSons())
            {
                System.out.println(tb+"  |--"+s);
            }
        }
    }
}
