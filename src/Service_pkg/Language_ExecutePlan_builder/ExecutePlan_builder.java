package Service_pkg.Language_ExecutePlan_builder;

import Data.Vessel.ExecutePlan_Package;
import Data.Vessel.TablespaceTable_name;
import Data.Vessel.WhereFilter;
import Data.Vessel.Word;
import Data.classes.KVEntryImpl;
import Data.classes.KVListImpl;
import Data.classes.Table;
import Service_pkg.Language_ExecutePlan_builder.ExecutePlan_Engine.*;
import Service_pkg.Service;

import java.util.*;
import java.util.Set;

import m_Exception.FileSystem.ClassNotFound;
import m_Exception.Language_error;

/**
 *
 * @author rkppo
 */
public class ExecutePlan_builder 
{
    private Service service;
    //LinkedList<Word> words;
    private String default_tbs= Service_pkg.env_properties.getEnvironment("default_tbs");
    private ExecutePlan_Package EPP;
    ArrayList<String> clause_stop;

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
    private ExecutePlan_Package childEPP;
    private ExecutePlan_Package unionEPP;
    private LinkedList<ExecutePlan_Package> joinEPP;
    private LinkedList<String> call_tablespace;
    private LinkedList<String> call_table;

    public ExecutePlan_builder(Service backstage,ArrayList<String> clause_stop)
    {
        this.service=backstage;
        this.clause_stop=clause_stop;

    }

    private void init_ExecuteEngine(List<Word> words)
    {
        start=words.get(0).getName();
        call_tablespace=new LinkedList<>();
        call_table=new LinkedList<>();
        EPP=new ExecutePlan_Package(start);
        joinEPP=new LinkedList<>();
        toClassify= (LinkedList<Word>) words;
    }
    public ExecutePlan_Package make_ExecutePlan(List<Word> words) throws ClassNotFound, Language_error
    {
        LinkedList<LinkedList<Word>> Division;

        init_ExecuteEngine(words);
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
                select();
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

    public Table CheckClasses(String Lttbs, String Lttb) throws ClassNotFound
    {//用于检查记录的表名和表空间名是否可用
     // 针对create的检查应当由自动机通过封闭路径的方式完成，这里就可以极大的简化逻辑
        if(Lttbs==null)
            Lttbs=default_tbs;
        if(service.check_File_exist(Lttbs, Lttb))
            return service.call_FileSystem().getTable(Lttbs,Lttb);
        else
            throw new ClassNotFound(Lttbs+'.'+Lttb);
    }
    
    public List<String> callTablecheckselectlist(String tbs,String tb,LinkedList<String> s_vertical)
    {//为了进行动态的列名检查而设计的
        //对应select多表连接或者insert into select这种情况
        return service.call_FileSystem().getTable(tbs, tb).checkselectlist(s_vertical);
    }

    public ArrayList<String> getCstop()
    {
        return clause_stop;
    }

    private void show() {
        s_vertical = new LinkedList<>();
        for (int loop = 1; loop < toClassify.size(); loop++)
            s_vertical.addLast(toClassify.get(loop).getName());
        EPP.setShow(s_vertical.toArray(new String[0]));
    }

    private void create() throws Language_error {
        for (int loop = 1; loop < toClassify.size(); loop++) {
            switch (toClassify.get(loop).getName()) {
                case "T_name":
                    call_table.add(toClassify.get(loop).getSubstance());
                    break;
                case "TS_name":
                    call_tablespace.add(toClassify.get(loop).getSubstance());
                    break;
                case "L_name":
                    s_vertical.addLast(toClassify.get(loop).getSubstance());
                    break;
                case "string":
                case "int":
                case "double":
                    s_condition.addLast(toClassify.get(loop).getName());
                    break;
                case "table":
                    s_vertical = new LinkedList<>();
                    s_condition = new LinkedList<>();
                case "tablespace":
                    EPP.setCreateType(toClassify.get(loop).getName());
                    break;
            }
        }
        if (EPP.getCreateType().equals("table") && s_vertical.isEmpty())
            throw new Language_error("建表语句不能没有实际列项");
        if (s_vertical != null)
            EPP.setCreate(s_vertical.toArray(new String[0]), s_condition.toArray(new String[0]));
    }

    private void drop() {
        for (int loop = 1; loop < toClassify.size(); loop++) {
            switch (toClassify.get(loop).getName()) {
                case "T_name":
                    call_table.add(toClassify.get(loop).getSubstance());
                    break;
                case "TS_name":
                    call_tablespace.add(toClassify.get(loop).getSubstance());
                    break;
                case "table":
                case "tablespace":
                    EPP.setCreateType(toClassify.get(loop).getName());
                    break;
            }
        }
    }

    private void insert() {
        boolean value = true;
        s_vertical = null;
        insertion_sequence = new LinkedList<>();
        for (int loop = 1; loop < toClassify.size(); loop++) {
            switch (toClassify.get(loop).getName()) {
                case "T_name":
                    call_table.add(toClassify.get(loop).getSubstance());
                    break;
                case "TS_name":
                    call_tablespace.add(toClassify.get(loop).getSubstance());
                    break;
                case "Integer":
                case "Double":
                case "String":
                    insertion_sequence.addLast(toClassify.get(loop).getSubstance());
                    break;
                case "L_name":
                    s_vertical.addLast(toClassify.get(loop).getSubstance());
                    break;
                case "(":
                    if (toClassify.get(loop - 1).getName().equals("T_name")) {
                        s_vertical = new LinkedList<>();
                        value = false;
                    }
                    break;
            }
        }
        if (value)
            EPP.setInsert(null, insertion_sequence.toArray(new String[0]));
        else
            EPP.setInsert(s_vertical.toArray(new String[0]), insertion_sequence.toArray(new String[0]));
    }

    private void delete() {
        s_vertical = new LinkedList<>();
        boolean where = true;
        for (int loop = 1; loop < toClassify.size(); loop++) {
            switch (toClassify.get(loop).getName()) {
                case "where":
                    f_vertical = new LinkedList<>();
                    f_option = new LinkedList<>();
                    f_condition = new LinkedList<>();
                    linkmark = new LinkedList<>();
                    where = false;
                    break;
                case "Integer":
                case "Double":
                case "String":
                case "null":
                    f_condition.addLast(toClassify.get(loop).getSubstance());
                    break;
                case "L_name":
                    f_vertical.addLast(toClassify.get(loop).getSubstance());
                    break;
                case "is":
                case "isnot":
                case "=":
                case "!=":
                case ">":
                case "<":
                case ">=":
                case "<=":
                    f_option.addLast(toClassify.get(loop).getName());
                    break;
                case "and":
                case "or":
                    linkmark.addLast(toClassify.get(loop).getName());
                    break;
                case "T_name":
                    call_table.add(toClassify.get(loop).getSubstance());
                    break;
                case "TS_name":
                    call_tablespace.add(toClassify.get(loop).getSubstance());
                    break;
            }
        }
        if (where)
            EPP.setDelete(s_vertical.toArray(new String[0]), null, null, null, null);
        else
            EPP.setDelete(s_vertical.toArray(new String[0]), linkmark.toArray(new String[0]), f_vertical.toArray(new String[0]), f_option.toArray(new String[0]), f_condition.toArray(new String[0]));
    }

    private void update() {
        boolean where = true;
        s_vertical = new LinkedList<>();
        s_condition = new LinkedList<>();
        for (int loop = 1; loop < toClassify.size(); loop++) {
            if (where)
                switch (toClassify.get(loop).getName()) {
                    case "T_name":
                        call_table.add(toClassify.get(loop).getSubstance());
                        break;
                    case "TS_name":
                        call_tablespace.add(toClassify.get(loop).getSubstance());
                        break;
                    case "L_name":
                        s_vertical.addLast(toClassify.get(loop).getSubstance());
                        break;
                    case "Integer":
                    case "Double":
                    case "String":
                    case "null":
                        s_condition.addLast(toClassify.get(loop).getSubstance());
                        break;
                    case "where":
                        where = false;
                        linkmark = new LinkedList<>();
                        f_vertical = new LinkedList<>();
                        f_option = new LinkedList<>();
                        f_condition = new LinkedList<>();
                        break;
                }
            else
                switch (toClassify.get(loop).getName()) {
                    case "L_name":
                        f_vertical.addLast(toClassify.get(loop).getSubstance());
                        break;
                    case "Integer":
                    case "Double":
                    case "String":
                    case "null":
                        f_condition.addLast(toClassify.get(loop).getSubstance());
                        break;
                    case "is":
                    case "isnot":
                    case "=":
                    case "!=":
                    case ">":
                    case "<":
                    case ">=":
                    case "<=":
                        f_option.addLast(toClassify.get(loop).getName());
                        break;
                    case "and":
                    case "or":
                        linkmark.addLast(toClassify.get(loop).getName());
                        break;
                }
        }
        if (where)
            EPP.setUpdate(s_vertical.toArray(new String[0]), s_condition.toArray(new String[0]), null, null, null, null);
        else
            EPP.setUpdate(s_vertical.toArray(new String[0]), s_condition.toArray(new String[0]), linkmark.toArray(new String[0]), f_vertical.toArray(new String[0]), f_option.toArray(new String[0]), f_condition.toArray(new String[0]));
    }

    private void alter() {
        s_vertical = new LinkedList<>();
        insertion_sequence = new LinkedList<>();
        for (int loop = 1; loop < toClassify.size(); loop++) {
            switch (toClassify.get(loop).getName()) {
                case "T_name":
                    call_table.add(toClassify.get(loop).getSubstance());
                    break;
                case "TS_name":
                    call_tablespace.add(toClassify.get(loop).getSubstance());
                    break;
                case "int":
                case "double":
                case "string":
                    insertion_sequence.addLast(toClassify.get(loop).getName());
                    break;
                case "L_name":
                    s_vertical.addLast(toClassify.get(loop).getSubstance());
                    break;
                case "add":
                case "del":
                    EPP.setCreateType(toClassify.get(loop).getName());
                    break;
            }
        }
        EPP.setAlter(s_vertical.toArray(new String[0]), insertion_sequence.toArray(new String[0]));
    }

    private void select() throws ClassNotFound, Language_error {
        Map<TablespaceTable_name, KVListImpl<KVEntryImpl>> select_result;
        Map<TablespaceTable_name,Table> from_result;
        Map<TablespaceTable_name, WhereFilter> toFilteTable;
        List<List<Word>> Division = Word_Classifier.toFind_clause_start(this.toClassify, clause_stop);
        HashMap<String,LinkedList<Word>> select_Map=new HashMap<>();//将select的子句和起始单词分别对应

        for(String s:clause_stop)
        {
            select_Map.put(s,new LinkedList<Word>());
        }

        String s_Cnode=null;
        for(List<Word> clause:Division)
        {
            if(select_Map.get(clause.get(0).getName())!=null)
            {
                s_Cnode=clause.remove(0).getName();
                select_Map.get(clause.get(0).getName()).addAll(clause);
            }
            else if(s_Cnode!=null)
                select_Map.get(s_Cnode).addAll(clause);
            else
                throw new Language_error(clause.get(0),"子句起始单词有误");
        }
        from_result=From.from(this,select_Map.get("from"));
        select_result=Select.select(select_Map.get("select"));

        //先检查select子项的合法性
        //首先要将null代表的列名全抽出来，否则可能遇到一些问题
        KVListImpl<KVEntryImpl> forNull=select_result.remove(null);
        Iterator<TablespaceTable_name> listNameLists=select_result.keySet().iterator();
        Set<String> ntemp=new HashSet<>();
        for(;listNameLists.hasNext();)
        {
            TablespaceTable_name ttn=listNameLists.next();
            Table t=from_result.get(ttn);
            List ltemp=t.checkselectlist(select_result.get(ttn).getoListname()),fntemp;
            if(ltemp.size()!=select_result.get(ttn).getoListname().size())
            {
//                throw
            }

            //如何通过一个简单的方法知道，forNull当中不存在ntemp的某个元素，同时还不改变这两个集合的内容
            //或者也可以检查最后的ntemp当中有没有重复项
            //用set集合试试？
            try {
                fntemp=t.checkselectlist(forNull.getoListname());
                ntemp.addAll(fntemp);
            }catch (Exception e){
                //如果遇到重复项，会报错
                ntemp.retainAll(t.checkselectlist(forNull.getoListname()));
                //此时ntemp当中的所有项目需要依次展示出来
                return;
            }
            ltemp.addAll(fntemp);
        }

        if(ntemp.size()!=forNull.getoListname().size()){
            //两边数量不对也会导致报错
            List<String> etemp=new ArrayList<>();
            etemp.addAll(forNull.getoListname());
            etemp.removeAll(ntemp);
            //etemp当中所有项目是在已知范围内找不到的列项
            throw new Language_error("");
        }

        //从from开始，将不同的表建立不同的计划，以达到“先选择，再连接”的目的
        toFilteTable=
    }
}
