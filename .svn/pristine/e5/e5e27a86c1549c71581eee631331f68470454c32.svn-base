/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service.Filereader;
import Service.count_HASH_code;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 *
 * @author BFD-501
 * 该类使用dom4j来读取xml类型的数据文件和控制文件
 * 所有用来读取文件的组件都是基类Xml_Reader的子类
 */
public abstract class Xml_Reader 
{
    Element root;
    Document document;
    SAXReader reader ;
    LinkedList<Element> operation;
    count_HASH_code chc;
    
    public Xml_Reader() throws DocumentException
    {
        reader = new SAXReader();
        this.operation = new LinkedList();
    }
    public void setFile(File file) throws DocumentException
    {
        //FileInputStream fis=new FileInputStream(file);
        document=reader.read(file);
        root=document.getRootElement();
        document=null;
    }
    public Element getRoot()
    {
        return root;
    }
    public List<Element> getElements(String string)
    {
        Element tis=select_ELE(root,string);
        if(tis==null)
            return new LinkedList<>();
        return tis.elements(string);
    }
    protected boolean verifier_strings(String[] files,String SHA256) throws IOException
    {
        StringBuffer sb=new StringBuffer();
        for(int loop=0;loop<files.length;loop++)
        {
            sb.append(files[loop]);
        }
        if(SHA256.equals(chc.getHash(sb.toString())))
            return true;
        else
            return false;
    }
    protected boolean verifier_file(String SHA256, String... filenames) throws IOException
    {
        if(filenames.length==0)
        {
            if(SHA256.equals(""))
                return true;
            else return false;
        }
        LinkedList<File> files=new LinkedList<>();
        
        for(int loop=0;loop<filenames.length;loop++)
            files.add(new File(filenames[loop]));
        String countHash=chc.getHash(files);
        if(SHA256.equals(countHash))
            return true;
        else
            return false;
    }
    
    private Element select_ELE(Element e,String e_name)//根据e_name，找到含有e_name节点的父节点
    {
        List<Element> listElement=e.elements();//所有一级子节点的list  
        //for(Element temp:listElement)
        for(int loop=0;loop<listElement.size();loop++)
        {//遍历所有一级子节点  
            Element temp=listElement.get(loop);
            if(temp.getName().equals(e_name))
                return e;
            else
                select_ELE(temp,e_name);//递归  
        }
        return null;
    }
}
