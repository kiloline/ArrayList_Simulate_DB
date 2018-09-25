/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data.Verticaltype;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.sun.istack.internal.NotNull;
import m_Exception.runtime.deepCloneException;
import m_Exception.runtime.insertException;

/**
 *
 * @author rkppo
 */
public abstract class Vertical_Column<E> implements Runnable,Serializable
{
    protected static final long cloneID=369285298572941L;
    protected volatile Vertical_Node<E>[] elements;  //作为整个列统一存储的基石
    protected Exception e;
    protected String Vertical_name;  //列名
    protected String Vertical_type;  //列类型的字符表示
    protected int Size,mem; //Size是已经使用的空间，mem是分配的内存块数
    protected HashMap<E,List<Integer>> index;//反向普通索引
    protected boolean isIndex;
    public Vertical_Column(String Vertical_name,String vertical_type)
    {
        this.Vertical_name=Vertical_name;
        Vertical_type=vertical_type;
        Size=0;
        mem=1;
        isIndex=false;
        elements=new Vertical_Node[1];
    }
    public Vertical_Column(LinkedList<Vertical_Node> newelement, String Vertical_name, String vertical_type, boolean index)
    {
        this.Vertical_name=Vertical_name;
        this.Vertical_type=vertical_type;
        this.Size=0;
        //this.Size=newelement.length;
        this.mem=newelement.size()+1;
        isIndex=index;
        elements=newelement.toArray(new Vertical_Node[0]);
//                new Vertical_Node[mem];
//        for(int loop=0;loop<newelement.size();loop++)
//            elements[loop]=new Vertical_Node(newelement.get(loop),loop,null);
    }
    
    public int getSize()
    {
        return Size;
    }
    public void alterSize(int Size)
    {
        this.Size=Size;
    }
    public String getVertical_type()
    {
        return Vertical_type;
    }
    public int getMem()
    {
        return mem;
    }
    
    public abstract Vertical_Node getindex_element(int index);
    public abstract LinkedList<Vertical_Node> getindex_elements(Integer... index);
    public abstract LinkedList<Vertical_Node> getAll();
    protected abstract Vertical_Node<E> insert(E element) throws insertException;
    public abstract LinkedList<Vertical_Node<E>> insert(E... element) throws insertException;
    public abstract LinkedList<Vertical_Node<E>> delete(Integer... line);
    public abstract LinkedList<Vertical_Node> update(E element, Integer... line) throws insertException;
    
    //这里的Pick_Condition()用于根据谓词查找符合条件的行号
    public abstract List<Integer> Pick_Condition(String conditionSymbol, E conditionValue) throws Exception;

    /**
     *
     * @param p_c
     * @return 所有checkout默认都以Vertical_Array来返回，不带索引，Size=mem
     */
    public abstract Vertical_Column<E> checkout(@NotNull String newVerticalName, Integer... p_c);
    public void realloc(int newmem) throws OutOfMemoryError {
        int loopmax;
        if(this.Size>newmem)
            loopmax=newmem;
        else
            loopmax=this.Size;
        Vertical_Node[] temp=this.elements;
        this.elements=new Vertical_Node[newmem];
        System.arraycopy(temp,0,elements,0,temp.length);
        this.mem=newmem;
    }

    public abstract void initIndex();
    public abstract void dropIndex();

    public Boolean isIndex() {
        return this.isIndex;
    }

    public Vertical_Column<E> deepclone() throws deepCloneException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Vertical_Column)ois.readObject();
        } catch (IOException e1) {
            throw new deepCloneException("没有权限获取被复制的列。");
        } catch (ClassNotFoundException e1) {
            throw new deepCloneException("被复制的列不存在，请检查并发操作。");
        }
    }
}
