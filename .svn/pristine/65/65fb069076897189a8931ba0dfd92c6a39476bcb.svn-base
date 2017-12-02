package Data.Vessel;

import Data.classes.Table;
import Service.Service;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author gosiple
 * 这是用于多线程列操作的一个指令封包类，包含多种指令类型和基指令；
 * 是执行计划引擎的一个延伸，也是执行引擎的一个中间件。
 * 注意，get函数添加String[]的时候一定要按照Table当中对应入参列表最长的操作函数的入参顺序添加
 * 按理来说这个类要做成能够执行多表关联嵌套左右内外连接无所不能，但是现在暂时只能搞单表查询
 * 
 */
public class ExecutePlan_Package 
{
    private String Command;
    private String Classtype;
    private LinkedList<String> call_tablespace;
    private LinkedList<String> call_table;
    private String[] s_vertical;//insert
    private String[] s_condition;
    private String[] newnames;
    private String[] linkmark;
    private String[] f_vertical;
    private String[] f_option;
    private String[] f_condition;
    private String[] insertion_sequence;//insert
    private LinkedList<ExecutePlan_Package> childEPP;//嵌套子查询，针对where子句中的嵌套
    private LinkedList<ExecutePlan_Package> joinEPP;//关联查询,针对from子句中的嵌套
    private LinkedList<ExecutePlan_Package> unionEPP;//union
    private boolean haschild,hasjoin,hasunion;
    public ExecutePlan_Package(String command)
    {
        Command=command;
        s_vertical=null;
        s_condition=null;
        newnames=null;
        linkmark=null;
        f_vertical=null;
        f_option=null;
        f_condition=null;
        insertion_sequence=null;
        call_tablespace=new LinkedList<>();
        call_table=new LinkedList<>();
        childEPP=new LinkedList<>();
        joinEPP=new LinkedList<>();
        unionEPP=new LinkedList<>();
        haschild=false;
        hasjoin=false;
        hasunion=false;
    }
    public ExecutePlan_Package(String joinType,String newTablename)
    {
        this("select");
    }
    
    public void initchildEPP(ExecutePlan_Package pg)//为单嵌套子查询准备的结构，现在没有使用
    {
        childEPP.add(pg);
        haschild=true;
    }
    public void initunionEPP(ExecutePlan_Package pg)//为单嵌套子查询准备的结构，现在没有使用
    {
        unionEPP.add(pg);
        hasunion=true;
    }
    public void initjoinEPP(ExecutePlan_Package pg)
    {
        joinEPP.add(pg);
        hasjoin=true;
    }
    public boolean checkchild()
    {
        return haschild;
    }
    public boolean checkunion()
    {
        return hasunion;
    }
    public boolean checkjoin()
    {
        return hasjoin;
    }
    public LinkedList<ExecutePlan_Package> getchildEPP()
    {
        return childEPP;
    }
    public LinkedList<ExecutePlan_Package> getunionEPP()
    {
        return unionEPP;
    }
    public LinkedList<ExecutePlan_Package> getjoinEPP()
    {
        return joinEPP;//
    }
    
    public void setCreateType(String type)
    {
        Classtype=type;
    }
    public void setCreate(String[] s_vertical,String[] s_condition)
    {
        this.s_vertical=s_vertical;
        this.s_condition=s_condition;
    }
    public void setInsert(String[] s_vertical,String[] insertion_sequence)
    {
        this.s_vertical=s_vertical;
        this.insertion_sequence=insertion_sequence;
    }
    public void setDelete(String[] s_vertical,String[] linkmark,String[] f_vertical,String[] f_option,String[] f_condition)
    {
        this.s_vertical=s_vertical;
        this.linkmark=linkmark;
        this.f_vertical=f_vertical;
        this.f_condition=f_condition;
        this.f_option=f_option;
    }
    public void setUpdate(String[] s_vertical,String[] s_condition,String[] linkmark,String[] f_vertical,String[] f_option,String[] f_condition)
    {
        this.s_vertical=s_vertical;
        this.linkmark=linkmark;
        this.f_vertical=f_vertical;
        this.f_condition=f_condition;
        this.f_option=f_option;
        this.s_condition=s_condition;
    }
    public void setSelect(String[] s_vertical,String[] newnames,String[] linkmark,String[] f_vertical,String[] f_option,String[] f_condition)
    {
        this.s_vertical=s_vertical;
        this.linkmark=linkmark;
        this.f_vertical=f_vertical;
        this.f_condition=f_condition;
        this.f_option=f_option;
        this.newnames=newnames;
    }
    public void setAlter(String[] s_vertical,String[] insertion_sequence)
    {
        this.s_vertical=s_vertical;
        this.insertion_sequence=insertion_sequence;
    }
    public void setShow(String[] s_vertical)
    {
        this.s_vertical=s_vertical;
    }
    public void PushClasses(String Tablespace_name,String Table_name)
    {
        call_tablespace.addLast(Tablespace_name);
        call_table.addLast(Table_name);
    }
    
    public String getCommand()
    {
        return this.Command;
    }
    public String getCreateType()
            {
                return Classtype;
            }
    public LinkedList<String[]> getCreate()
    {
        LinkedList<String[]> result=new LinkedList<>();
        result.addLast(s_vertical);
        result.addLast(s_condition);
        return result;
    }
    public LinkedList<String[]> get_insert()
    {
        LinkedList<String[]> result=new LinkedList<>();
        result.addLast(s_vertical);
        result.addLast(insertion_sequence);
        return result;
    }
    public LinkedList<String[]> get_delete()
    {
        LinkedList<String[]> result=new LinkedList<>();
        result.addLast(linkmark);
        result.addLast(f_vertical);
        result.addLast(f_option);
        result.addLast(f_condition);
        return result;
    }
    public LinkedList<String[]> get_update()
    {
        LinkedList<String[]> result=new LinkedList<>();
        result.addLast(s_vertical);
        result.addLast(s_condition);
        result.addLast(linkmark);
        result.addLast(f_vertical);
        result.addLast(f_option);
        result.addLast(f_condition);
        return result;
    }
    public LinkedList<String[]> get_select()
    {
        LinkedList<String[]> result=new LinkedList<>();
        result.addLast(s_vertical);
        result.addLast(newnames);
        result.addLast(linkmark);
        result.addLast(f_vertical);
        result.addLast(f_option);
        result.addLast(f_condition);
        return result;
    }
    public LinkedList<String[]> get_Alter()
    {
        LinkedList<String[]> result=new LinkedList<>();
        result.addLast(s_vertical);
        result.addLast(insertion_sequence);
        return result;
    }
    public String[] getShow()
    {
        return this.s_vertical;
    }
    public String[] popClasses()
    {
        String[ ] m=new String[2];
        m[0]=call_tablespace.removeLast();
        m[1]=call_table.removeLast();
        return m;
    }
    
    public List<String> checkUpperSelectVertical(String[] s_vertical)
    {
        ArrayList<String> Return=new ArrayList<>();
        for(int loop=0;loop<s_vertical.length;loop++)
        {
            if(this.s_vertical.equals(s_vertical[loop]))
                Return.add(s_vertical[loop]);
        }
        return Return;
    }
}
