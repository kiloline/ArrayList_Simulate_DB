package Service_pkg.Fileloader;

import Data.Verticaltype.Vertical_Column;
import Data.Vessel.TablespaceTableList_name;
import Data.classes.Table;
import Service_pkg.Handling.table_handling;
import java.io.File;
import java.util.HashMap;

import m_Exception.runtime.deepCloneException;
import m_Exception.xml_reader.fileReader_error;
import org.dom4j.DocumentException;

/**
 * @author rkppo
 */
public class FileSystem_link_tree {
    private static FileSystem_link_tree inner_flt;
    private boolean file_detection;
    private String rootpath;
    private Control_File ControlFile;
    private Log_File[] Redo_Logs;//这两个DB_File相当于ControlFile当中文件指针的快捷方式，但是需要时刻维护
    private Tablespace_File[] TableSpace;//调用真实的数据文件时可以直接从这里调用，避免一些不必要的寻址
    
    //需要遍历的时候使用上面的DB_File数组，需要调用的时候使用下面的哈希表。
    //根据不知名的HashMap源代码来看，HashMap.put使用的是传指针方式进行赋值，因此数组和哈希表当中的元素应当保持一致性
    private HashMap<String,Tablespace_File> tbs_name;
    //这里又是没有初始化
    public FileSystem_link_tree(String rootpath,String dbname,boolean detectHash,table_handling th) throws fileReader_error, Exception
    {
        tbs_name=new HashMap<>();
        file_detection=detectHash;
        this.rootpath=rootpath;
        
        File ctf=new File(rootpath+'\\'+dbname+".ctf.xml");
        ControlFile=new Control_File(dbname,ctf,detectHash,th);//初始化ControlFile的时候要依次将所有文件初始化
        freshlink();

        inner_flt=this;
    }
    public void freshlink()//维护文件系统的快速调用机制
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
    public void add_tablespace(String tbsname) throws Exception//添加文件必须控制在一个String内
    {
        Tablespace_File tbs=new Tablespace_File(ControlFile,rootpath,tbsname,file_detection);
        ControlFile.addSon(tbs);
        freshlink();
        //return tbs;
    }
    public void add_table(String tbsname,Table table) throws Exception
    {//这里如果不直接传入Table对象的话，那么就要连带的传递很多建表参数
        Table_File tb=new Table_File(tbs_name.get(tbsname),rootpath,table,file_detection);
        tbs_name.get(tbsname).addSon(tb);
        //freshlink();
    }
    public void del_tablespace(String tbsname) throws Exception
    {//暂时不考虑从Windows文件系统当中删除文件的问题
        String[] sons=tbs_name.get(tbsname).getSons();
        for(int loop=0;loop<sons.length;loop++)//首先递归删除表空间下所有的表
            del_table(tbsname,sons[loop]);
        ControlFile.delSon(tbsname);
        freshlink();
    }
    public void del_table(String tbsname,String tbname) throws Exception
    {//暂时不考虑从Windows文件系统当中删除文件的问题
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

    public static Vertical_Column getReadonlyList(TablespaceTableList_name ttlname) throws deepCloneException {
        synchronized (inner_flt) {
            final Vertical_Column verticalColumn = inner_flt.
                    getTable(ttlname.getSpace(), ttlname.getTable()).
                    getTableMapping().
                    getData(ttlname.getList()).deepclone();
            return verticalColumn;
        }
    }
}
