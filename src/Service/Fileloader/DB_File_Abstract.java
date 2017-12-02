/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service.Fileloader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author rkppo
 * @param <F> is father file which implement this interface
 * @param <D> is son file which implement this interface
 */
public abstract class DB_File_Abstract <F,D>
{
    protected String path,filename,fullpath;//文件夹路径,文件名（不含后缀）,全路径（path和filename和后缀）
    protected F Father=null;
    protected HashMap<String,D> Son;
//一开始SON变量忘了初始化，导致总是有空指针异常而无法完成数据库初始化。（囧迫
    protected boolean detectHash;
    
    public DB_File_Abstract(F father,String path,String filename,boolean detectHash)//如果要新建文件的话用此构造器
    {
        Father=father;
        this.path=path;
        this.filename=filename;
        this.detectHash=detectHash;
        Son=new HashMap<>();
        //fullpath应该放到各个继承抽象类的子类当中自行确定
    }
    public DB_File_Abstract(F father,File file,String filename,boolean detectHash)//如果导入已存在文件的时候用此构造器
    {
        Father=father;
        path=file.getParent();
        this.filename=filename;
        this.detectHash=detectHash;
        Son=new HashMap<>();
        //fullpath应该放到各个继承抽象类的子类当中自行确定
    }
    
    public File getFile()
    {
        return new File(fullpath);
    }
    public String getPath()
    {
        return this.path;
    }
    public String getfilename()
    {
        return this.filename;
    }
    public D getSon(String index)
    {
        return this.Son.get(index);
    }
    public String[] getSons()
    {
        try{
            return this.Son.keySet().toArray(new String[0]);
        }
        catch(Exception e)
        {//如果是初始化的数据库里面没有表的时候用keyset方法会有空指针异常。
            return new String[0];
        }
    }
    
    public abstract void addSon(D add) throws Exception;
    public abstract void delSon(String del) throws Exception ;
    
    public abstract void setSons(D[] down) throws Exception;
    
    
    /**@deprecated */
    public void setPath(String path)
    {//此方法一般情况下不允许调用
        this.path=path;
    }
    /**@deprecated */
    public void setfilename(String file)
    {//此方法一般情况下不允许调用
        filename=file;
    }
    /**@deprecated */
    public void setFather(F up)
    {//此方法一般情况下不允许调用
        Father=up;
    }
}
