package Data.classes;

import m_Exception.data.hVlength_error;
import m_Exception.data.EachListlength_error;
import m_Exception.data.NLlength_error;
import Service.Check.check_StringtoNumber;
import Data.Verticaltype.Vertical_column_old;
import Data.Vessel.TableMapping;
import Service.Handling.table_handling;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import m_Exception.*;
import m_Exception.data.SingalList_error;

/**
 * @author rkppo
 * table结构应该是递归的，尤其是在select的时候，通过抛出一个table类的方式完成嵌套查询或者子查询
 * 因此table当中必须包含能够完整打印出自身所有内容的方法：print()或者类似方法
 * 还要有能灵活设计自身的方法：setTable(),该方法同时保证loaddatafile的实时读表文件需求
 */
/*
* 如果对表的操作失败，必须进行回滚操作，那么目前最简单的办法是先对标进行深复制，然后对复制表进行操作。
* 一旦出现错误，立刻销毁复制表，退回原表。而不是细粒度的进行回滚操作。
*/
public class Table implements Serializable{
    private String tablename;
    private int Size, mem;
    //Size是已经使用的空间，mem是分配的行数
    //String[] listnames;
    private LinkedList<String> listnames;
    //各个列的列名以及伪指针
    //多余出来的列名列表用于保存原始的列名顺序
    private TableMapping<String, String, Vertical_column_old> tm;
    private check_StringtoNumber CSN;

    public Table(String tablename, String[] listname, String[] listtype) throws Exception {
        listnames = new LinkedList<>();
        tm = new TableMapping<>();
        if (listname.length != listtype.length)  //如果列名数量和列类型数量不等的话一定有问题
            throw new NLlength_error();

        this.tablename = tablename;
        String type = "String";
        for (int loop = 0; loop < listname.length; loop++) //将传递过来的建表列名序列转换成内存结构
        {
            switch (listtype[loop]) {
                case "int":
                    type = "Integer";
                    break;
                case "double":
                    type = "Double";
                    break;
                case "string":
                    type = "String";
                    break;
            }
            listnames.add(listname[loop]);
            tm.put(listname[loop], type, table_handling.getVertical_column(listtype[loop]));
            //tm.put(listname[loop], type, table_handling.getVertical_column(listtype[loop]));
            //hashtable.put(listname[loop],new Assemble(type,service.getVertical_column(listtype[loop])));
        }
        Size = 0;
        mem = 1;
    }

    private Table(String tablename, String[] ln, String[] lt, Vertical_column_old[] verticals) throws Exception//这个是Table类内部使用的隐式构造器
    {
//用于setTable()
        if (ln.length != verticals.length || ln.length != lt.length)
            throw new hVlength_error();
        for (int loop = 1; loop < verticals.length; loop++) {
            if (verticals[0].getSize() != verticals[loop].getSize())
                throw new EachListlength_error();
        }
        listnames = new LinkedList<>();
        tm = new TableMapping<>();
        for (int loop = 0; loop < verticals.length; loop++) {
            listnames.add(ln[loop]);
            tm.put(ln[loop], lt[loop], verticals[loop]);
        }
        this.tablename = tablename;
        this.Size = verticals[0].getSize();
        this.mem = verticals[0].getMem();
        if (mem == 0)
            mem++;
        //service=handling;//未赋值，所以还需要sethandling()来完成赋值。
    }

    private Table(int size, int mem, LinkedList<String> ln, TableMapping<String, String, Vertical_column_old> TM) {
        tablename = "linktable";
        Size = size;
        this.mem = mem;
        listnames = ln;
        tm = TM;
    }

    @Override
    public String toString() {
        return this.tablename;
    }

    @Override
    public boolean equals(Object o) {//该函数验证两个Table之间的列类型是否一一对应的相同
        if (o.getClass().getName().equals(this.getClass().getName())) {
            Table t = (Table) o;
            String[] ta1, ta2;
            ta1 = this.getTableMapping().getAttributes(listnames.toArray(new String[0]));
            ta2 = t.getTableMapping().getAttributes(t.getListOrder().toArray(new String[0]));
            if (ta1.length != ta2.length)
                return false;
            for (int loop = 0; loop < ta1.length; loop++)
                if (!ta1[loop].equals(ta2[loop]))
                    return false;
            return true;
        }
        return false;
    }

    @Override
    public Table clone() {//这是基于Java Stream的深复制，需要改对象的所有引用对象都实现Serializable接口
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = null;
        try {
            oo = new ObjectOutputStream(bo);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            oo.writeObject(this);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //从流里读出来
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = null;
        try {
            oi = new ObjectInputStream(bi);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            return (Table) oi.readObject();
        } catch (IOException e1) {
            return null;
        } catch (ClassNotFoundException e1) {
            return null;
        }
        //return new Table(Size,mem, (LinkedList<String>) this.listnames.clone(),tm.clone());
    }

    public int getMem() {
        return mem;
    }

    public int getSize() {
        return Size;
    }

    public static Table setTable(String tablename, String[] ln, String[] lt, Vertical_column_old[] verticals) throws Exception {
        return new Table(tablename, ln, lt, verticals);
    }

    public TableMapping<String, String, Vertical_column_old> getTableMapping() {
        return tm;
    }

    public LinkedList<String> getListOrder() {
        return listnames;
    }

    public void printTable(int listsize)//将这张表完全打印出来
    {//最顶端的select应当调用这个函数
        //listsize是每列的默认宽度
        System.out.println();
        int loop;
        Vertical_column_old[] key = new Vertical_column_old[this.tm.size()];
        StringBuilder tempBuffer = new StringBuilder();

        tempBuffer.append('|');
        for (loop = 0; loop < this.tm.size(); loop++) {//打印列名行
            String temps = this.listnames.get(loop);
            tempBuffer.append(temps);
            if (temps.length() >= listsize)
                ;
            else
                for (int loopi = 0; loopi < listsize - temps.length(); loopi++)
                    tempBuffer.append(' ');
            tempBuffer.append('|');

            key[loop] = this.tm.getData(temps);//提前将各列压入key队列中
        }
        System.out.println(tempBuffer.toString());

        for (int loopo = 0; loopo < Size; loopo++) {
            tempBuffer.setLength(0);//据测试，该方法可能是清空String连接类最快的方法
            tempBuffer.append('|');
            for (loop = 0; loop < this.tm.size(); loop++) {
                int elementlength;
                try {
                    elementlength = key[loop].getindex_element(loopo).toString().length();
                    //因为有可能遇到空指针（null）的数据值，所以要用try-catch来捕获异常
                } catch (Exception e) {
                    tempBuffer.append("null");
                    for (int loopi = 0; loopi < listsize - 4; loopi++)
                        tempBuffer.append(' ');
                    tempBuffer.append('|');
                    continue;
                }
                if (elementlength == 0)//暂时将空字符串翻译成null
                {
                    tempBuffer.append("null");
                    for (int loopi = 0; loopi < listsize - 4; loopi++)
                        tempBuffer.append(' ');
                    tempBuffer.append('|');
                    continue;
                } else if (elementlength >= listsize)
                    tempBuffer.append(new String(key[loop].getindex_element(loopo).toString().toCharArray(), 0, listsize - 3) + "...");
                else {
                    tempBuffer.append(key[loop].getindex_element(loopo));
                    for (int loopi = 0; loopi < listsize - key[loop].getindex_element(loopo).toString().length(); loopi++)
                        tempBuffer.append(' ');
                }
                tempBuffer.append('|');
            }
            System.out.println(tempBuffer.toString());
        }
        System.out.println();
    }

    public void reSize(int newSize) {
        this.Size = newSize;
    }

    public void realloc() throws OutofMemory_error//不但要增大，还要能减小
    {
        try {
            if (this.Size >= this.mem && this.mem < Integer.MAX_VALUE)
                this.mem = Double.valueOf(mem + Math.log(Math.pow(mem, 40)) + 100).intValue();//扩容公式
            else if (this.Size == this.mem)//this.mem==Integer.MAX_VALUE,java不允许长度超过2^32的数组存在
                throw new OutofMemory_error();
            else if (this.Size <= this.mem * 0.75 && this.mem != 1)//缩容公式
                this.mem = Double.valueOf(this.mem * 0.75).intValue() + 1;
            else
                return;
        } catch (Exception e) {
            throw new OutofMemory_error();
        }
        Iterator<? extends Vertical_column_old> toRealloc = this.tm.DataIterator();
        while (toRealloc.hasNext()) {
            try {
                toRealloc.next().realloc(this.mem);
            } catch (Exception ex) {
                throw new OutofMemory_error();
            }
        }
    }

    public List<String> checkselectlist(String[] s_vertical) {
        ArrayList<String> Return = new ArrayList<>();
        for (int loop = 0; loop < s_vertical.length; loop++) {
            if (this.listnames.size() < 20) {
                if (listnames.indexOf(s_vertical[loop]) != -1)
                    Return.add(s_vertical[loop]);
            } else if (this.tm.getAttribute(s_vertical[loop]) != null)
                Return.add(s_vertical[loop]);
        }
        return Return;
    }
}
