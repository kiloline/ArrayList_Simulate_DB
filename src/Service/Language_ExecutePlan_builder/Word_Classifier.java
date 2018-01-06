package Service.Language_ExecutePlan_builder;

import Data.Vessel.ExecutePlan_Package;
import Data.Vessel.Word;

import java.util.*;

import Data.classes.Table;
import m_Exception.FileSystem.ClassNotFound;
import m_Exception.Language_error;
import org.apache.commons.collections4.keyvalue.DefaultMapEntry;

/** 
 *
 * @author gosiple
 * 单词分类器，用于第一步粗筛，将整句中的单词按照一定得语法格子分开（也就是俗称的子句），传递到下一层
 * 处理子查询的时候尤其要小心，最好设置两个方式来传递
 * 目前自从对分析复杂select的计划提上日程之后，分类器就负担了绝大部分语义分析的工作
 * 被迫写成了复杂的递归，我真的不想这么写的，但是光是写成这样计算量已经有点大了，递推执行……还完全没有方案。
 */
public class Word_Classifier 
{
    private ExecutePlan_builder backstage;
    private String start;
    private LinkedList<String> s_vertical=null;
    private LinkedList<String> s_condition=null;
    private LinkedList<String> newnames=null;
    private LinkedList<String> linkmark=null;
    private LinkedList<String> f_vertical=null;
    private LinkedList<String> f_option=null;
    private LinkedList<String> f_condition=null;
    private LinkedList<String> insertion_sequence=null;
    private LinkedList<Word> toClassify;
    private ExecutePlan_Package EPP;
    private ExecutePlan_Package childEPP;
    private ExecutePlan_Package unionEPP;
    private LinkedList<ExecutePlan_Package> joinEPP;
    private LinkedList<String> call_tablespace;
    private LinkedList<String> call_table;
    private LinkedList<Integer> Division_line_is_read;
    public Word_Classifier(ExecutePlan_builder backstage,LinkedList<Word> words)
    {
        this.backstage=backstage;
        toClassify=words;
        start=words.get(0).getName();
        call_tablespace=new LinkedList<>();
        call_table=new LinkedList<>();
        EPP=new ExecutePlan_Package(start);
        Division_line_is_read=new LinkedList<>();
        joinEPP=new LinkedList<>();
    }
    public ExecutePlan_Package Classify() throws ClassNotFound, Language_error
    {
        LinkedHashSet<Word> tableSet;
        switch(start)
        {
            case "create":
            {
                create();
                break;
            }
            case "drop":
            {
                drop();
                break;
            }
            case "insert":
            {
                insert();
                break;
            }
            case "delete":
            {
                delete();
                break;
            }
            case "update":
            {
                update();
                break;
            }
            case "select":
            {
                LinkedList<LinkedList<Word>> Division=backstage.Pretreatment_Division_clause(this.toClassify);
                HashMap<String,LinkedList<Word>> select_Map=new HashMap<>();//将select的子句和起始单词分别对应
                ArrayList<String> Cstop=backstage.getCstop();
                for(String s:Cstop)
                {
                    select_Map.put(s,new LinkedList<Word>());
                }

                String s_Cnode=null;
                for(LinkedList<Word> clause:Division)
                {
                    if(select_Map.get(clause.get(0).getName())!=null)
                    {
                        s_Cnode=clause.removeFirst().getName();
                        select_Map.get(clause.get(0).getName()).addAll(clause);
                    }
                    else if(s_Cnode!=null)
                        select_Map.get(s_Cnode).addAll(clause);
                    else
                        throw new Language_error(clause.get(0),"子句起始单词有误");
                }
                from(select_Map.get("from"));


                break;
            }
            case "show":
            {
                show();
                break;
            }
            case "alter":
            {
                alter();
                break;
            }
            case "commit":break;
            default:
        }
        return EPP;
    }

    private void show()
    {
        s_vertical=new LinkedList<>();
        for(int loop=1;loop<toClassify.size();loop++)
            s_vertical.addLast(toClassify.get(loop).getName());
        EPP.setShow(s_vertical.toArray(new String[0]));
    }
    private void create() throws Language_error
    {
        for(int loop=1;loop<toClassify.size();loop++)
                {
                    switch(toClassify.get(loop).getName())
                    {
                        case "T_name": 
                            call_table.add(toClassify.get(loop).getSubstance()); break;
                        case "TS_name": 
                            call_tablespace.add(toClassify.get(loop).getSubstance()); break;
                        case "L_name":
                            s_vertical.addLast(toClassify.get(loop).getSubstance());break;
                        case "string":case "int":case "double":
                            s_condition.addLast(toClassify.get(loop).getName());break;
                        case "table":
                            s_vertical=new LinkedList<>();
                            s_condition=new LinkedList<>();
                        case "tablespace":
                            EPP.setCreateType(toClassify.get(loop).getName());
                            break;
                    }
                }
                if(EPP.getCreateType().equals("table")&&s_vertical.isEmpty())
                    throw new Language_error("建表语句不能没有实际列项");
                if(s_vertical!=null)
                    EPP.setCreate(s_vertical.toArray(new String[0]), s_condition.toArray(new String[0]));
    }
    private void drop()
    {
        for(int loop=1;loop<toClassify.size();loop++)
                {
                    switch(toClassify.get(loop).getName())
                    {
                        case "T_name": 
                            call_table.add(toClassify.get(loop).getSubstance()); break;
                        case "TS_name": 
                            call_tablespace.add(toClassify.get(loop).getSubstance()); break;
                        case "table":case "tablespace":
                            EPP.setCreateType(toClassify.get(loop).getName());
                            break;
                    }
                }
    }
    private void insert()
    {
        boolean value=true;
                s_vertical=null;
                insertion_sequence=new LinkedList<>();
                for(int loop=1;loop<toClassify.size();loop++)
                {
                    switch(toClassify.get(loop).getName())
                    {
                        case "T_name": 
                            call_table.add(toClassify.get(loop).getSubstance()); break;
                        case "TS_name": 
                            call_tablespace.add(toClassify.get(loop).getSubstance()); break;
                        case "Integer":case "Double":case "String":
                           insertion_sequence.addLast(toClassify.get(loop).getSubstance());break;
                        case "L_name":
                            s_vertical.addLast(toClassify.get(loop).getSubstance());
                            break;
                        case "(":
                            if(toClassify.get(loop-1).getName().equals("T_name"))
                            {
                                s_vertical=new LinkedList<>();
                                value=false;
                            }
                            break;
                    }
                }
                if(value)
                    EPP.setInsert(null, insertion_sequence.toArray(new String[0]));
                else
                    EPP.setInsert(s_vertical.toArray(new String[0]), insertion_sequence.toArray(new String[0]));
    }
    private void delete()
    {
        s_vertical=new LinkedList<>();
                boolean where=true;
                for(int loop=1;loop<toClassify.size();loop++)
                {
                    switch(toClassify.get(loop).getName())
                    {
                        case "where":
                            f_vertical=new LinkedList<>();
                            f_option=new LinkedList<>();
                            f_condition=new LinkedList<>();
                            linkmark=new LinkedList<>();
                            where=false;
                            break;
                        case "Integer":case "Double":case "String":case "null":
                            f_condition.addLast(toClassify.get(loop).getSubstance());break;
                        case "L_name":
                            f_vertical.addLast(toClassify.get(loop).getSubstance());break;
                        case "is":case "isnot": case "=":case "!=":case ">":case "<":case ">=":case "<=":
                            f_option.addLast(toClassify.get(loop).getName());break;
                        case "and":case "or":
                            linkmark.addLast(toClassify.get(loop).getName());break;
                        case "T_name":
                            call_table.add(toClassify.get(loop).getSubstance()); break;
                        case "TS_name": 
                            call_tablespace.add(toClassify.get(loop).getSubstance()); break;
                    }
                }
                if(where)
                    EPP.setDelete(s_vertical.toArray(new String[0]), null, null, null, null);
                else
                    EPP.setDelete(s_vertical.toArray(new String[0]), linkmark.toArray(new String[0]), f_vertical.toArray(new String[0]), f_option.toArray(new String[0]), f_condition.toArray(new String[0]));
    }
    private void update()
    {
        boolean where=true;
                s_vertical=new LinkedList<>();
                s_condition=new LinkedList<>();
                for(int loop=1;loop<toClassify.size();loop++)
                {
                    if(where)
                        switch(toClassify.get(loop).getName())
                        {
                            case "T_name":
                                call_table.add(toClassify.get(loop).getSubstance()); break;
                            case "TS_name":
                                call_tablespace.add(toClassify.get(loop).getSubstance()); break;
                            case "L_name":
                                s_vertical.addLast(toClassify.get(loop).getSubstance());break;
                            case "Integer":case "Double":case "String":case "null":
                                s_condition.addLast(toClassify.get(loop).getSubstance());break;
                            case "where":
                                where=false;
                                linkmark=new LinkedList<>();
                                f_vertical=new LinkedList<>();
                                f_option=new LinkedList<>();
                                f_condition=new LinkedList<>();
                                break;
                        }
                    else
                        switch(toClassify.get(loop).getName())
                        {
                            case "L_name":
                                f_vertical.addLast(toClassify.get(loop).getSubstance());break;
                            case "Integer":case "Double":case "String":case "null":
                                f_condition.addLast(toClassify.get(loop).getSubstance());break;
                            case "is":case "isnot": case "=":case "!=":case ">":case "<":case ">=":case "<=":
                                f_option.addLast(toClassify.get(loop).getName());break;
                            case "and":case "or":
                                linkmark.addLast(toClassify.get(loop).getName());break;
                        }
                }
                if(where)
                    EPP.setUpdate(s_vertical.toArray(new String[0]), s_condition.toArray(new String[0]), null, null, null, null);
                else
                    EPP.setUpdate(s_vertical.toArray(new String[0]), s_condition.toArray(new String[0]), linkmark.toArray(new String[0]), f_vertical.toArray(new String[0]), f_option.toArray(new String[0]), f_condition.toArray(new String[0]));
                
    }
    private void alter()
    {
        s_vertical=new LinkedList<>();
                insertion_sequence=new LinkedList<>();
                for(int loop=1;loop<toClassify.size();loop++)
                {
                    switch(toClassify.get(loop).getName())
                    {
                        case "T_name": 
                            call_table.add(toClassify.get(loop).getSubstance()); break;
                        case "TS_name": 
                            call_tablespace.add(toClassify.get(loop).getSubstance()); break;
                        case "int":case "double":case "string":
                           insertion_sequence.addLast(toClassify.get(loop).getName());
                           break;
                        case "L_name":
                            s_vertical.addLast(toClassify.get(loop).getSubstance());
                            break;
                        case "add":case "del":
                            this.EPP.setCreateType(toClassify.get(loop).getName());
                            break;
                    }
                }
                EPP.setAlter(s_vertical.toArray(new String[0]), insertion_sequence.toArray(new String[0]));
    }
    private Map<String,Table> from(LinkedList<Word> clause)
    {
        Map<String,Table> Return=new HashMap<>();
        String TS_name=null,T_name=null,newT_name=null;
        for(Word w:clause)
        {
            switch(w.getType())
            {
                case "TS_name":
                    call_tablespace.add(w.getSubstance());
                case "T_name":
                    if(call_table.size()==call_tablespace.size())
                        call_tablespace.add(null);
                    call_table.add(w.getSubstance());
                case "newT_name":
                {
                    for(;!call_table.isEmpty();)
                    {

                    }
                }
                case "Clause_SQL":
            }
        }
        return Return;
    }
}
