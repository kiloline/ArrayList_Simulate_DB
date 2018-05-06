package Service.Handling;

import Data.Verticaltype.*;
import Data.classes.Table;
import Service.Handling.Vertical_Factory.Vertical_Factory;
import Utils.Check.check_StringtoNumber;
import Data.Vessel.ExecutePlan_Package;
import Data.Vessel.TableMapping;
import Service.Service;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import m_Exception.data.HaveNoListName_error;
import m_Exception.NullTypePoint_error;
import m_Exception.OutofMemory_error;
import m_Exception.data.*;
import m_Exception.runtime.insertException;
import m_Exception.type.Type_not_exist;

/**
 *
 * @author rkppo
 */
public class table_handling
{
    LinkedList<TableMapping> stack_list;
    check_StringtoNumber CSN;
    TableMapping<String,String,Vertical_Column> list;
    Service backstage;
    Table thisTable;
    int listsize;
    public table_handling(Service server,check_StringtoNumber csn)
    {
        this.listsize = 15;
        list=null;
        CSN=csn;
        stack_list=new LinkedList<>();
        backstage=server;
    }
    public void setlistsize(int length)
    {
        listsize=length;
    }
    public void setData(TableMapping<String,String,Vertical_Column> map)
    {
        stack_list.addFirst(list);
        list=map;
    }
    public void cleanData()
    {//为下一次复用做好准备
        list=stack_list.getFirst();
    }
    public static Vertical_Column getVertical_column(String vertical_name, String vertical_type,String data_structure)
            throws NullTypePoint_error, Type_not_exist {   //目前只支持int，double和String三种类型
        return Vertical_Factory.getVertical_column(vertical_name,vertical_type,data_structure);
    }
    public Vertical_Column setVertical_column
            (String[] vertical_nodes,String vertical_name, String vertical_type,boolean isIndex,String data_structure)
            throws NullTypePoint_error, Type_not_exist, insertException {   //目前只支持int，double和String三种类型
        return Vertical_Factory.setVertical_column(vertical_nodes,vertical_name,vertical_type,isIndex,data_structure);
    }

    public synchronized void Implement_Plan(ExecutePlan_Package EPP) throws Exception
    {
        switch(EPP.getCommand())
        {
            case "show":
                for(String s:EPP.getShow())
                    switch(s)
                    {
                        case "dbtree":
                            backstage.call_FileSystem().printDBTree();
                            break;
                        case "memory_use":
                        case "cpu_use":
                            throw new Exception("该功能："+s+"的执行部分还没有解决好");
                    };
                break;
            case "commit":
                backstage.call_FileSystem().saveTable();
                break;
            case "create":
            {
                switch(EPP.getCreateType())//这里用switch，是因为以后可能还要新建别的东西，不只是table和tablespace
                {
                    case "table":
                    {
                        String[] pop=EPP.popClasses();
                        LinkedList<String[]> E=EPP.getCreate();
                        Table table=new Table(pop[1],E.get(0),E.get(1));
                        backstage.call_FileSystem().add_table(pop[0], table);
                        break;
                    }
                    case "tablespace":
                    {
                        String[] pop=EPP.popClasses();
                        backstage.call_FileSystem().add_tablespace(pop[0]);
                        break;
                    }
                }
                break;
            }
            case "drop":
            {
                switch(EPP.getCreateType())
                {
                    case "table":
                    {
                        String[] pop=EPP.popClasses();
                        backstage.call_FileSystem().del_table(pop[0], pop[1]);
                        break;
                    }
                    case "tablespace":
                    {
                        String[] pop=EPP.popClasses();
                        backstage.call_FileSystem().del_tablespace(pop[0]);
                        break;
                    }
                }
                break;
            }
            case "select":
            {
                Table selectTable;
                LinkedList<String[]> E=EPP.get_select();
                String[] classes=EPP.popClasses();
                thisTable=backstage.call_FileSystem().getTable(classes[0], classes[1]);
                selectTable=select(thisTable,E.get(0), E.get(1), E.get(2), E.get(3), E.get(4), E.get(5));
                if(EPP.checkunion())
                {
                    throw new Exception("该功能：union的执行部分还没有解决好");
                    /*LinkedList<String[]> UE=EPP.getunionEPP().get_select();
                    String[] Uclasses=EPP.getunionEPP().popClasses();
                    Table toUnion=backstage.call_FileSystem().getTable(Uclasses[0], Uclasses[1]);
                    Table unionTable=select(toUnion,UE.get(0), UE.get(1), UE.get(2), UE.get(3), UE.get(4), UE.get(5));*/
                }
                selectTable.printTable(listsize);
                break;
            }
            case "insert":
            {
                LinkedList<String[]> E=EPP.get_insert();
                String[] classes=EPP.popClasses();
                thisTable=backstage.call_FileSystem().getTable(classes[0], classes[1]);
                insert(E.get(0), E.get(1));
                backstage.call_FileSystem().changestatus(classes[0], classes[1]);
                break;
            }
            case "delete":
            {
                LinkedList<String[]> E=EPP.get_delete();
                String[] classes=EPP.popClasses();
                thisTable=backstage.call_FileSystem().getTable(classes[0], classes[1]);
                delete(E.get(0), E.get(1), E.get(2), E.get(3));
                backstage.call_FileSystem().changestatus(classes[0], classes[1]);
                break;
            }
            case "update":
            {
                LinkedList<String[]> E=EPP.get_update();
                String[] classes=EPP.popClasses();
                thisTable=backstage.call_FileSystem().getTable(classes[0], classes[1]);
                update(E.get(0), E.get(1), E.get(2), E.get(3), E.get(4), E.get(5));
                backstage.call_FileSystem().changestatus(classes[0], classes[1]);
                break;
            }
            case "alter":
            {
                String alterype=EPP.getCreateType();
                LinkedList<String[]> E=EPP.get_Alter();
                String[] classes=EPP.popClasses();
                thisTable=backstage.call_FileSystem().getTable(classes[0], classes[1]);
                alter(alterype,E.get(0),E.get(1));
                backstage.call_FileSystem().changestatus(classes[0], classes[1]);
                break;
            }
        }
    }

    public Integer[] Where_find(String[] linkmark,String[] c_vertical,String[] s_condition,String[] e_condition) throws HaveNoListName_error, Type_not_exist, Exception
    {//查找符合where条件的行号
        checkListname_beingness(c_vertical);

        Vertical_Column toSelect;
        List<List<Integer>> Results=new LinkedList<>();
        Integer[] samelocal = null;//最终的where条件选择出来的结果
        for(int loop=0;loop<c_vertical.length;loop++)
        {
            toSelect=list.getData(c_vertical[loop]);
            List<Integer> selectresult1;
            selectresult1=toSelect.Pick_Condition(s_condition[loop],typetransverter(e_condition[loop],list.getAttribute(c_vertical[loop])));
            Results.add(selectresult1);//根据条件查找到所有符合条件的行号，保存在Results当中
        }
        LinkedList<Integer> set=new LinkedList<>(Results.get(0));
        for(int loop=1;loop<Results.size();loop++)
        {//根据第一个返回的结果开始去重
            if(linkmark[loop-1].equals("or"))
                set.addAll(Results.get(loop));
            else if(linkmark[loop-1].equals("and"))
                set.retainAll(Results.get(loop));
        }
        samelocal=set.toArray(new Integer[0]);
        return samelocal;
    }
    public Vertical_Column[] Vertical_checkout(String[] listnames,String[] newnames,Integer[] LineNumber)
    {//抽出指定行号、指定列的数据重新组成新的Vertical对象
        //Vertical_column[] a=new Vertical_column[0];
        String listname;
        LinkedList<Vertical_Column> result=new LinkedList<>();
        for(int loop=0;loop<listnames.length;loop++)
        {
            listname=newnames[loop];
            result.add(list.getData(listnames[loop]).checkout(listname,LineNumber));
        }
        return result.toArray(new Vertical_Column[0]);
    }
    public boolean checkListname_beingness(String[] listnames) throws HaveNoListName_error
    {
        for(int loop=0;loop<listnames.length;loop++)
        {//检查待查找的行在当前表是否存在，有一个不存在则抛出异常
            String t=listnames[loop];
            t=list.getAttribute(t);
            if(t==null)
                throw new HaveNoListName_error(listnames[loop]);
        }
        return true;
    }
    public Object typetransverter(String element,String type) throws Type_not_exist
    {
        if(element==null)
            ;
        else if(type.equals("Integer")&&CSN.check_StringtoInteger(element))
            return (Object) Integer.valueOf(element);
        else if(type.equals("Double")&&CSN.check_StringtoDouble(element))
            return (Object) Double.valueOf(element);
        else if(type.equals("String"))
            return element;
        return null;
    }
    public <E>E[] typetransverter2Array(String[] element,String type) throws Type_not_exist //,E tab
    {
        Object[] o=new Object[0];
        if(type.equals("Integer"))
        {
            //if(element.length!=0)
            o=new Integer[element.length];
        }
        else if(type.equals("Double"))
        {
            //if(element.length!=0)
            o=new Double[element.length];
        }
        else if(type.equals("String"))
            o=new String[element.length];
        else
            o=new Object[element.length];

        for(int loop=0;loop<element.length;loop++)
            o[loop]=typetransverter(element[loop],type);
        return (E[])o;
    }

    public void insert(String[] s_vertical,String[] insertion_sequence) throws ILlength_error, HaveNoListName_error, OutofMemory_error, Type_not_exist //() into tablename values()
    {
//        int Size=thisTable.getSize();
//        TableMapping<String,String,Vertical_C> TableMap=thisTable.getTableMapping();
//        if(s_vertical==null)
//            s_vertical=thisTable.getListOrder().toArray(new String[0]);
//
//        if(insertion_sequence.length!=s_vertical.length)
//            throw new ILlength_error();
//        setData(TableMap);
//        checkListname_beingness(s_vertical);
//        cleanData();
//        thisTable.reSize(Size+1);
//        thisTable.realloc();
//        LinkedList<String> lnList=new LinkedList<>(TableMap.keySet());
//        //for(int loop=0;loop<s_vertical.length;loop++)
//        //如果采用上述循环方式的话，必然导致表中各个列的长度不一致（有些列进行了insert而另一些列没有进行insert），
//        //必须将表中所有的列都循环一次，但是要注意s_vertical当中的顺序和原表列顺序未必相同。
//        for(int loop=0;loop<s_vertical.length;loop++)
//        {
//            String type;
//            lnList.remove(s_vertical[loop]);
//            type=TableMap.getAttribute(s_vertical[loop]);
//            TableMap.getData(s_vertical[loop]).insert(typetransverter(insertion_sequence[loop], type));
//        }
//        for(int loop=0;loop<lnList.size();loop++)
//        {
//            String type=TableMap.getAttribute(lnList.get(loop));
//            TableMap.getData(lnList.get(loop)).insert(null);
//            //insert中未提及的列值为null
//        }
    }

    public void update(String[] s_vertical,String[] s_condition,String[] linkmark,String[] f_vertical,String[] f_option,String[] f_condition) throws HaveNoListName_error, Type_not_exist, Exception
    {//set s_vertical=s_condition
        //where c_vertical s_condition e_condition
//        int Size=thisTable.getSize();
//        TableMapping<String,String,Vertical_C> TableMap=thisTable.getTableMapping();
//        Integer[] samelocal;
//        setData(TableMap);
//        checkListname_beingness(s_vertical);
//        if(f_vertical==null)
//        {
//            samelocal=new Integer[Size];
//            for(int loop=0;loop<Size;loop++)
//                samelocal[loop]=loop;
//        }
//        else
//        {
//            samelocal = Where_find(linkmark,f_vertical,f_option,f_condition);
//        }
//        for(int loop=0;loop<s_vertical.length;loop++)
//        {
//            String type=TableMap.getAttribute(s_vertical[loop]);
//            TableMap.getData(s_vertical[loop]).update(samelocal,typetransverter(s_condition[loop], type));
//        }
//        cleanData();
    }

    public void delete(/*String[] s_vertical,*/String[] linkmark,String[] f_vertical,String[] f_option,String[] f_condition) throws OutofMemory_error, Type_not_exist, Exception
    {
//        int Size=thisTable.getSize();
//        TableMapping<String,String,Vertical_C> TableMap=thisTable.getTableMapping();
//        Integer[] samelocal;
//        setData(TableMap);
//        if(f_vertical==null)
//        {
//            samelocal=new Integer[Size];
//            for(int loop=0;loop<Size;loop++)
//                samelocal[loop]=loop;
//        }
//        else
//        {
//            samelocal = Where_find(linkmark,f_vertical,f_option,f_condition);//最终的where条件选择出来的结果
//        }
//        String[] listnames=thisTable.getListOrder().toArray(new String[0]);
//        for(int loop=0;loop<listnames.length;loop++)
//        {
//            TableMap.getData(listnames[loop]).delete(samelocal);
//        }
//        thisTable.reSize(Size-samelocal.length);
//        thisTable.realloc();
//        cleanData();
    }


    public Table select(Table table,String[] s_vertical,String[] newnames,String[] linkmark,String[] f_vertical,String[] f_option,String[] f_condition) throws Exception //设定查找行名称、查找条件
    {//例如 where c_vertical=e_condition('=' is s_condition)，然后根据表中保存的数据类型进行一次强制转换
//        if(s_vertical==null&f_vertical==null)
//            return table;//如果select *，那么就打印自身
//
//        TableMapping<String,String,Vertical_C> tm=table.getTableMapping();
//        setData(tm);
//        if(s_vertical==null)
//        {
//            s_vertical=table.getListOrder().toArray(new String[0]);
//            newnames=table.getListOrder().toArray(new String[0]);
//        }
//        else
//        {
//            checkListname_beingness(s_vertical);
//            //由于制定执行计划的时候newnames不是简单置空，所以这里也要相应的改变判断方式
//            for(int loop=0;loop<s_vertical.length;loop++)
//                if(newnames[loop]==null)
//                    newnames[loop]=s_vertical[loop];
//        }
//
//        Vertical_C[] temp;
//        Integer[] samelocal;
//        if(f_vertical==null)
//        {
//            samelocal=new Integer[table.getSize()];
//            for(int loop=0;loop<table.getSize();loop++)
//                samelocal[loop]=loop;
//            temp=Vertical_checkout(s_vertical,newnames, samelocal);
//        }
//        else
//        {
//            samelocal = Where_find(linkmark,f_vertical,f_option,f_condition);//最终的where条件选择出来的结果
//            temp=Vertical_checkout(s_vertical,newnames, samelocal);
//        }
//        cleanData();
//        return Table.setTable("tempselect",newnames,tm.getAttributes(s_vertical),temp);//重新生成Table对象并返回
        return null;
    }
    public void alter(String type,String[] s_vertical,String[] insertion_sequence) throws SingalList_error,HaveNoListName_error, NullTypePoint_error, Exception
    {
//        setData(thisTable.getTableMapping());
//        if(type.equals("del"))
//        {
//            checkListname_beingness(s_vertical);
//            for(int loop=0;loop<s_vertical.length;loop++)
//            {
//                thisTable.getListOrder().remove(s_vertical[loop]);
//                thisTable.getTableMapping().remove(s_vertical[loop]);
//            }
//        }
//        else if(type.equals("add"))
//        {
//            try{
//                checkListname_beingness(s_vertical);
//                throw new Exception("该列已经存在");
//            }
//            catch(HaveNoListName_error er){
//                for(int loop=0;loop<s_vertical.length;loop++)
//                {
//                    Vertical_C vc=getVertical_column(insertion_sequence[loop]);
//                    vc.realloc(thisTable.getMem());
//                    vc.alterSize(thisTable.getSize());
//                    thisTable.getListOrder().add(s_vertical[loop]);
//                    thisTable.getTableMapping().put(s_vertical[loop],vc.getVertical_name(),vc);
//                }
//            }
//        }
//        cleanData();
    }


    //private Table LinkTable_union(Table t1,Table t2)
    //{
    //Table result=t1.getClass().
    //}
}
