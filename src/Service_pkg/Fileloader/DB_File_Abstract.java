/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service_pkg.Fileloader;

import java.io.File;
import java.util.HashMap;

/**
 *
 * @author rkppo
 * @param <F> is father file which implement this interface
 * @param <D> is son file which implement this interface
 */
public abstract class DB_File_Abstract <F,D>
{
    protected String path,filename,fullpath;//�ļ���·��,�ļ�����������׺��,ȫ·����path��filename�ͺ�׺��
    protected F Father=null;
    protected HashMap<String,D> Son;
//һ��ʼSON�������˳�ʼ�������������п�ָ���쳣���޷�������ݿ��ʼ����������
    protected boolean detectHash;
    
    public DB_File_Abstract(F father,String path,String filename,boolean detectHash)//���Ҫ�½��ļ��Ļ��ô˹�����
    {
        Father=father;
        this.path=path;
        this.filename=filename;
        this.detectHash=detectHash;
        Son=new HashMap<>();
        //fullpathӦ�÷ŵ������̳г���������൱������ȷ��
    }
    public DB_File_Abstract(F father,File file,String filename,boolean detectHash)//��������Ѵ����ļ���ʱ���ô˹�����
    {
        Father=father;
        path=file.getParent();
        this.filename=filename;
        this.detectHash=detectHash;
        Son=new HashMap<>();
        //fullpathӦ�÷ŵ������̳г���������൱������ȷ��
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
        {//����ǳ�ʼ�������ݿ�����û�б��ʱ����keyset�������п�ָ���쳣��
            return new String[0];
        }
    }
    
    public abstract void addSon(D add) throws Exception;
    public abstract void delSon(String del) throws Exception ;
    
    public abstract void setSons(D[] down) throws Exception;
    
    
    /**@deprecated */
    public void setPath(String path)
    {//�˷���һ������²��������
        this.path=path;
    }
    /**@deprecated */
    public void setfilename(String file)
    {//�˷���һ������²��������
        filename=file;
    }
    /**@deprecated */
    public void setFather(F up)
    {//�˷���һ������²��������
        Father=up;
    }
}
