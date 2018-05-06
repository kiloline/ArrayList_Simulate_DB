package Service.Language_disposer;

import Data.classes.Language_node;

import java.util.*;

/**
 *
 * @author rkppo
 */
public class InitLanguage_node 
{
    HashSet<String> word_Set;
    
    private LinkedList Give_word()
    {//所有关键字列表在此添加
        LinkedList<String> wordList=new LinkedList<String>();//新标准英语教材：单词表
        //String[] wordList=new String[100];//维护比较麻烦且并不实用，遂废弃
        wordList.add("create");
        wordList.add("drop");
        wordList.add("select");
        wordList.add("insert");
        wordList.add("update");
        wordList.add("delete");
        wordList.add("alter");//第三次修订
        wordList.add("from");
        wordList.add("where");
        //wordList.add("order");
        //wordList.add("by");
        wordList.add("union");
        wordList.add("set");
        wordList.add("into");
        wordList.add("values");
        wordList.add("add");//第三次修订
        wordList.add("del");//第三次修订
        //wordList.add("distinct");
        
        //wordList.add("sequence");
        //wordList.add("cursor");
        wordList.add("table");
        wordList.add("tablespace");
        //wordList.add("view");
        //wordList.add("dual");
        //以下三个是create的时候定义用的
        wordList.add("int");
        wordList.add("double");
        wordList.add("string");
        //以下三个是标识变量类型用的
        wordList.add("String");
        wordList.add("Integer");
        wordList.add("Double");
        
        wordList.add("column");
        wordList.add("T_name");
        wordList.add("L_name");
        wordList.add("TS_name");
        wordList.add("newList_name");
        wordList.add("newTable_name");
        wordList.add("C_name");
        wordList.add("S_name");
        
        wordList.add("+");
        wordList.add("-");
        wordList.add("*");
        wordList.add("/");
        wordList.add("=");
        wordList.add(">");
        wordList.add("<");
        wordList.add(">=");
        wordList.add("<=");
        wordList.add("!=");
        
        wordList.add("(");
        wordList.add(")");
        wordList.add(",");
        wordList.add(".");
        wordList.add("\'");
        wordList.add(";");//这个单词作为终止符，一开始认为不用添加
        
        wordList.add("and");
        wordList.add("or");
        wordList.add("in");
        
        wordList.add("null");
        wordList.add("is");
        wordList.add("isnot");
        
        //wordList.add("asc");
        //wordList.add("desc");
        
        wordList.add("show");
        wordList.add("dbtree");
        wordList.add("memory_use");
        wordList.add("cpu_use");
        //以上是第一次构想的关键字
        //wordList.add("sysdate");
        wordList.add("commit");//单独出现，没有上下文。
        
        //5.2加入
        //wordList.add("Clause_SQL");//这个是更加高级的语法非终结符，不应该加到这里
        wordList.add("^");
        wordList.add("%");
        //适用于各类数学函数符号，例如sqrt、abs、pow等
        wordList.add("sqrt");
        wordList.add("abs");
        wordList.add("pow");
        
        return wordList;
    }
    
    public HashSet<String> setWord_Set() //初始化节点地图，设定每个节点的状态转移
    {
        this.word_Set=new HashSet<>();
        Iterator<String> word_list=this.Give_word().listIterator();
        for(;word_list.hasNext();)
        {
            this.word_Set.add(word_list.next());
        }
        return word_Set;
    }
    public ArrayList<String> setclause_stop()
    {
        ArrayList<String> stop=new ArrayList<String>();
        stop.add("select");
        stop.add("from");
        //stop.add("set");
        stop.add("where");
        stop.add("union");
        stop.add("order");
        stop.add("update");
        stop.add("delete");
        stop.add("group");
        stop.add("order");

        return stop;
    }
    
    public static void main(String[] ar)
    {
        HashSet test=new InitLanguage_node().setWord_Set();
        System.out.print(test.size());
    }
}
