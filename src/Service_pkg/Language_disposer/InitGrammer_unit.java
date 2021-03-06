package Service_pkg.Language_disposer;

import Data.classes.Language_node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class InitGrammer_unit {
    public static ArrayList<HashMap<String,String>> getGrammar_unit()
    {
        ArrayList<HashMap<String,String>> Grammar_unit=new ArrayList<>();
        HashMap<String,String> Var=new HashMap<>();
        HashMap<String,String> SQL_initVar=new HashMap<>();
        HashMap<String,String> MathFunction=new HashMap<>();
        HashMap<String,String> Class_name=new HashMap<>();
        HashMap<String,String> newClass_name=new HashMap<>();
        HashMap<String,String> check_Mark_Link=new HashMap<>();
        HashMap<String,String> bool_Mark_Link=new HashMap<>();
        HashMap<String,String> calculation_Mark_Link=new HashMap<>();
        HashMap<String,String> show_status=new HashMap<>();
        HashMap<String,String> bool_calculation_Mark_Link=new HashMap<>();
        HashMap<String,String> Class_type=new HashMap<>();//for Class_name(L,T,TS,S,C)
                //HashMap<String,String> =new HashMap<>();

        Var.put("String","Var");
        Var.put("Integer","Var");
        Var.put("Double","Var");
        Grammar_unit.add(Var);

        SQL_initVar.put("int","SQL_initVar");
        SQL_initVar.put("double","SQL_initVar");
        SQL_initVar.put("string","SQL_initVar");
        Grammar_unit.add(SQL_initVar);

        MathFunction.put("sqrt","MathFunction");
        MathFunction.put("pow","MathFunction");
        MathFunction.put("abs","MathFunction");
        Grammar_unit.add(MathFunction);

        Class_type.put("table","Class_type");
        Class_type.put("tablespace","Class_type");
        Class_type.put("sequence","Class_type");
        Class_type.put("cursor","Class_type");
        Grammar_unit.add(Class_type);//这个映射可以试一试

        check_Mark_Link.put(">","check_Mark_Link");
        check_Mark_Link.put("<","check_Mark_Link");
        check_Mark_Link.put("=","check_Mark_Link");//将单独的等号定义成赋值符，用于比较的定义成check_Mark_Link
        check_Mark_Link.put(">=","check_Mark_Link");
        check_Mark_Link.put("<=","check_Mark_Link");
        check_Mark_Link.put("!=","check_Mark_Link");
        Grammar_unit.add(check_Mark_Link);

        bool_Mark_Link.put("and","bool_calculation_Mark_Link");
        bool_Mark_Link.put("or","bool_calculation_Mark_Link");
        Grammar_unit.add(bool_Mark_Link);

        calculation_Mark_Link.put("+","calculation_Mark_Link");
        calculation_Mark_Link.put("-","calculation_Mark_Link");
        calculation_Mark_Link.put("*","calculation_Mark_Link");
        calculation_Mark_Link.put("/","calculation_Mark_Link");
        calculation_Mark_Link.put("^","calculation_Mark_Link");
        calculation_Mark_Link.put("%","calculation_Mark_Link");
        Grammar_unit.add(calculation_Mark_Link);

        show_status.put("dbtree","show_status");
        show_status.put("memory_use","show_status");
        show_status.put("cpu_use","show_status");
        Grammar_unit.add(show_status);

        bool_calculation_Mark_Link.put("is","bool_Mark_Link");
        bool_calculation_Mark_Link.put("isnot","bool_Mark_Link");
        Grammar_unit.add(bool_calculation_Mark_Link);

        return Grammar_unit;
    }

    private static LinkedList<String> Give_type_word()
    {//所有关键字列表在此添加
        LinkedList<String> word=new LinkedList<String>();
        word.add("create");
        word.add("drop");
        word.add("select");
        word.add("insert");
        word.add("update");
        word.add("delete");
        word.add("alter");//第三次修订
        word.add("from");
        word.add("where");
        //word.add("order");
        //word.add("by");
        word.add("union");
        word.add("set");
        word.add("into");
        word.add("values");
        word.add("add");//第三次修订
        word.add("del");//第三次修订
        //word.add("distinct");

        //word.add("sequence");
        //word.add("cursor");
        word.add("Class_type");
        //word.add("view");
        //word.add("dual");
        //以下三个是create的时候定义用的
        word.add("Var");
        //以下三个是标识变量类型用的
        word.add("SQL_initVar");

        word.add("column");
        word.add("L_name");
        word.add("T_name");
        word.add("TS_name");
        word.add("S_name");
        word.add("C_name");
        word.add("newT_name");
        word.add("newL_name");

        word.add("calculation_Mark_Link");
        word.add("*");//因为星号也可能单独作为全列名标记出现
        word.add("=");//赋值号
        word.add("check_Mark_Link");

        word.add("(");
        word.add(")");
        word.add(",");
        word.add(".");
        word.add(";");//这个单词作为终止符，一开始认为不用添加

        word.add("bool_calculation_Mark_Link");
        word.add("in");

        word.add("null");
        word.add("bool_Mark_Link");

        //word.add("asc");
        //word.add("desc");

        word.add("show");
        word.add("show_status");
        //以上是第一次构想的关键字
        //word.add("sysdate");
        word.add("commit");//单独出现，没有上下文。

        word.add("MathFunction");
        word.add("Clause_SQL");

        return word;
    }

    public HashMap<String,Language_node> setWord_Type_Map()
    {
        HashMap<String,Language_node> word_Map=new HashMap<>();
        LinkedList<String> words=Give_type_word();
        int word_count=words.size();
        Language_node[] word_list=new Language_node[word_count+6];
        for(int loop=0;loop<word_count;loop++)
        {
            word_list[loop]=new Language_node();
            word_list[loop].setoption(words.get(loop));
            word_Map.put(word_list[loop].getoption(), word_list[loop]);
        }

        final HashMap finalMap=word_Map; //完成节点增加后的节点地图不允许更改
        for(int loop=0;loop<word_count;loop++)
        {   //设定每个节点能够转移的状态
            //每个节点的转移状态会有一些微小的不符合SQL的地方，但是可以通过设定lock_node来限定
            switch (word_list[loop].getoption()) {
                case "create":
                case "drop":
                case "alter":
                {
                    String add="Class_type";
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "select":
                {
                    String[] add={"*","L_name","T_name","TS_name","Var",
                            "sysdate","distinct","MathFunction"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "insert":
                {
                    String add="into";
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "table":
                case "into":
                case "update":
                {
                    String[] add={"T_name","TS_name"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "delete":
                {
                    String add="from";
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "from":
                {
                    String[] add={"T_name","TS_name","(","dual"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "where":
                {
                    String[] add={"T_name","L_name","newT_name","newL_name"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "order":
                {
                    String add="by";
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "by":
                {
                    String[] add={"asc","desc"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "union":
                {
                    String add="select";
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "column":
                case "set":
                {
                    String add="L_name";
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "MathFunction":
                case "values":
                {
                    String[] add={"("};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "add":
                case "del":
                {
                    String add="column";
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "distinct":
                {
                    String[] add={"*","TS_name","T_name","L_name"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "sequence":
                case "cursor":
                {
                    break;
                }
                case "tablespace":  ///?????
                {
                    String[] add={"TS_name"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "SQL_initVar":
                {
                    String[] add={",",")"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "Var":
                {
                    String[] add={"calculation_Mark_Link",
                            "check_Mark_Link","bool_calculation_Mark_Link",";"};
                    word_list[loop].add_status(finalMap, add);
                }
                case "null":
                {
                    String[] add={",",")","bool_Mark_Link",";",
                            "where","union",};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }

                case "TS_name":
                {
                    String[] add={".",";"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "T_name":
                {
                    String[] add={"(",",",".",";","where","values","set","union","newT_name","bool_calculation_Mark_Link",
                            "add","del",")"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "L_name":
                {
                    String[] add={",",")","bool_Mark_Link","newL_name","from","=",
                            "calculation_Mark_Link","SQL_initVar",";",
                            "check_Mark_Link"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "newL_name":
                case "sysdate":
                {
                    String[] add={",","from",")"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "newT_name":
                {
                    String[] add={",",";","where","union"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "*":
                {
                    String add="from";
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "=":
                case "check_Mark_Link":
                case "calculation_Mark_Link":
                {
                    String[] add={"Var", "L_name","T_name",
                            "MathFunction","("};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "(":
                {
                    String[] add={"Var","L_name","T_name","sysdate"
                            ,"select","("};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case ")":
                {
                    String[] add={"values",";","where","newT_name",")",",",
                        "calculation_Mark_Link","check_Mark_Link"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case ",":
                {
                    String[] add={"Var","L_name","T_name","String","sysdate"
                            ,"(","Clause_SQL"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case ".":
                case "bool_calculation_Mark_Link":
                {
                    String[] add={"T_name","L_name"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "bool_Mark_Link":
                {
                    String add="null";//"not"
                    word_list[loop].add_status(finalMap, add);
                    break;
                }

                case "show": {
                    String[] add={"show_status"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "show_status":{
                    String[] add={"show_status",";"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "dual":
                case "commit":
                {
                    String[] add={";"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }

                case "Clause_SQL":
                {
                    String[] add={",",";","where","newT_name"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                case "Class_type":
                {
                    String[] add={"T_name","TS_name","S_name","C_name"};
                    word_list[loop].add_status(finalMap, add);
                    break;
                }
                default:
                    break;
            }
        }
        return finalMap;
    }
}
