package Service.Language_disposer;
import Data.Vessel.Clause_Word_List;
import Data.classes.Language_node;
import Data.Vessel.Word;
import Data.judgement.Coolean;
import Service.Check.check_StringtoNumber;
import Service.Check.check_commalocation;
import Service.Check.check_regular;
import static java.lang.System.exit;
import java.util.HashMap;
import java.util.LinkedList;
import m_Exception.Language_error;
import m_Exception.havenoend_string_error;
import m_Exception.null_escape_char_error;
/**
 * @author rkppo
 * 分词器
 * 1.两个引号当中的所有字符应当视为一个String，
 * 然后通过栈来处理String。即，读到第一个引号时，以后的字符全部放到一处，等到第二个匹配的引号出现时
 * 2.目前只允许from子句后面跟上嵌套子查询，where子句后的子查询暂时认为数据格式不匹配（列对象对表对象）
 * 3.应该把';'视为调用执行计划产生器的一个标识，而不是认为用户输入应该到此结束。
 *   尤其是用户一次性复制大量SQL同时执行的时候。
 * 4.把嵌套SQL变成一个独立的Word模块，并且继续的识别下去。
 * 
 * 问题：
 * 1.如果有两个子SQL只用一个逗号分开的情况，能否正常识别，不至于将SQL插入错误的序列？
 */
public class Word_Segmentation_Machine 
{
    HashMap<String,Language_node> WordMap;
    check_StringtoNumber CSN;
    check_commalocation CC;
    check_regular CR;
    LinkedList<Word> LW;//主SQL序列
    LinkedList<String> markstack;//关键字队列（非符号）
    LinkedList<String> bracketstack;//括号匹配栈
    LinkedList<Integer> bracketstack_tempbottom;//用于临时指定括号匹配栈的栈底，以便分析嵌套SQL
    LinkedList<Integer> Clausestack_select;//这是专门给嵌套SQL中select位置分析准备的前标量，记录下最后一个select在markstack中的位置参数
    boolean Clause_label;
    Coolean status;
    String toSQL;
    public Word_Segmentation_Machine(HashMap<String,Language_node> map)
    {
        LW = new LinkedList<>();
        WordMap=map;
        markstack=new LinkedList<>();
        bracketstack=new LinkedList<>();
        bracketstack_tempbottom=new LinkedList<>();
        Clausestack_select=new LinkedList<>();
        CR = new check_regular();
        Clause_label=false;
    }
    public LinkedList<Word> Segment(String content) throws Exception
    {
        LW.clear();
        toSQL=content;
        StringBuffer toWord=new StringBuffer();
        status=Coolean.letter; 
        for(int loop=0;loop<toSQL.length();loop++)
        {
            char c=toSQL.charAt(loop);
            Coolean nowstatus=c_BuildWord(c);
            boolean isStop=nowstatus.equals(Coolean.stop);//判断当前字符是不是停顿符
            boolean charStop=charStop(nowstatus);//通过status和nowstatus的取值和相互关系得出是否继续读入字符
            boolean single_quotation=(c=='\'');//判断是否转入String类型处理函数
            boolean Stop=isStop|charStop|single_quotation;
            //switch不能用于复杂类型
            //如果联系出现符号和单引号的时候，需要先判断之前的符号是不是单词，然后再处理单引号的String。
            //所以暂时先把单引号列为一个stop观察一下是否可行。已验证不可行
            //因此单引号需要单独用一个Boolean标记
            if(Stop&&toWord.length()!=0)
            {//当前字符是空格，则停下建立新的单词
                String isWord=toWord.toString().toLowerCase();//统一转换成小写
                if(WordMap.get(isWord)==null)
                {
                    this.build_undefined_var(isWord);
                }
                else if(Stop&&toWord.length()==0)
                    continue;
                else if(c=='\'')
            //在这里通过略微损失效率，每字符进行比较，从而正确完成解析
            //遇到单引号，需要构造String
                {
                    String cp_length=Stringinquotation(loop);
                    create_word_and_add("String",cp_length);
                    loop=loop+cp_length.length()+1;//loop停在后面的单引号上
                    nowstatus=Coolean.mark;
                    continue;
                }
                else if(isWord.equals("("))
                {
                    create_word_and_add(isWord,null);
                    //读取到可能是嵌套SQL的迹象，但是这里不能像String用单独的程序处理，必须用一个信号，把Segment的处理结果拦截下来。
                    if(markstack.getLast().equals("from")||markstack.getLast().equals("where")) {
                        if (markstack.get(Clausestack_select.getLast()).equals("select"))//这里必须确定每一个嵌套SQL的select单词的位置，否则就无法分析。
                        {
                            Clause_label = true;//更改分析开关的走向来控制分析过程
                            //这个时候不将左右括号加进ListWord中，但是要加入到括号匹配栈中，并且更新临时栈底
                            bracketstack_tempbottom.add(this.bracketstack.size() - 1);//此时下面的字符扫描函数已经将括号加进去了，因此这里只更新栈底就可以
                            if (this.LW.getLast().getClass().getSimpleName().equals("Word"))
                                LW.add(this.create_Clauseword_and_add());
                            else//此时说明主SQL序列中的最后一个单词是Clause
                            {
                                Clause_Word_List clause_temp = (Clause_Word_List) LW.getLast();
                                for (; ; ) {
                                    while (clause_temp.getLastWord().getClass().getSimpleName().equals("Word")) {
                                        clause_temp.Add_word(this.create_Clauseword_and_add());
                                    }
                                    clause_temp = (Clause_Word_List) clause_temp.getLastWord();
                                }
                            }
                            continue;
                        }
                    }
                }
                else if(isWord.equals(")"))
                {
                    create_word_and_add(isWord,null);
                    if(this.Clause_label)
                    {
                        
                    }
                }
                else
                {
                    if(status.equals(Coolean.letter)) //不加入符号
                        markstack.add(isWord);//这里以及下面一行原来是toWord.toString()方法
                    create_word_and_add(isWord,null);
                    if(isWord.equals("select"))
                        this.Clausestack_select.add(this.markstack.size()-1);
                }
                toWord.delete(0, toWord.length());
            }
            if(status.equals(Coolean.mark))
            {
                if(WordMap.get(toWord.toString())!=null)
                    if(WordMap.get(toWord.toString()+c)==null)
                    {
                        create_word_and_add(toWord.toString(),null);
                        toWord.delete(0, toWord.length());
                    }
            }
            if(!nowstatus.equals(Coolean.stop))
            {
                toWord.append(c);
            }
            
            status=nowstatus;
        }
        create_word_and_add(";",null);
        
        if(!bracketstack.isEmpty())
        {
            throw new Language_error("还有"+bracketstack.size()+"个左括号没有对应的括号匹配");
        }
        
        return LW;
    }
    
    private void build_undefined_var(String isWord) throws Language_error
    {
            boolean nocreate=true;
        //接下来有两种可能，1.这是一个对象名称、2.这是不存在的单词
                    if(status.equals(Coolean.mark))
                        throw new Language_error(isWord+"符号不存在");
                    
                    String object=CR.regular(isWord);
                    switch(LW.getLast().getName())
                    {
                        case "L_name": 
                        {
                            if(markstack.getLast().equals("select"))
                                create_word_and_add("newList_name",isWord);
                            nocreate=false;
                            break;
                        }
                        case ">":case "<":case ">=":case "<=":case "=":case "!=":
                            switch(object)
                            {
                                case "isInteger":
                                {
                                    String nummark=LW.getLast().getName();
                                    if(nummark.equals("+")|nummark.equals("-"))
                                        LW.removeLast();
                                    else
                                        nummark="";
                                    create_word_and_add("Integer",nummark+isWord);break;
                                }
                                case "isDouble":
                                {
                                    String nummark=LW.getLast().getName();
                                    if(nummark.equals("+")|nummark.equals("-"))
                                        LW.removeLast();
                                    else
                                        nummark="";
                                    create_word_and_add("Double",nummark+isWord);break;
                                }
                                case "isListName":
                                case "isTLName":
                                default:
                                    throw new Language_error(isWord+"，请检查该输入");
                            }
                            nocreate=false;
                            break;
                        case "(":case ",":
                            break;
                        case "+":case "-":case "*":case "/":
                        {
                            switch(object)
                            {
                                case "isInteger":
                                    create_word_and_add("Integer",isWord);
                                    nocreate=false;
                                    break;
                                case "isDouble":
                                    create_word_and_add("Double",isWord);
                                    nocreate=false;
                                    break;
                            }
                            break;
                        }
                    }
                    if(nocreate)
                    OUTER:
                    switch (markstack.getLast()) {
                        case "tablespace":
                            if(object.equals("isListName")) 
                                create_word_and_add("TS_name",isWord);
                            else
                                throw new Language_error(isWord+"，请检查该输入");
                            break;
                        case "table":  //create table语句
                            if(!LW.getLast().getName().equals("table"))
                            {
                                if(object.equals("isListName")) 
                                    create_word_and_add("L_name",isWord);
                                else
                                    throw new Language_error(isWord+"，请检查该输入");
                                break;
                            }
                        case "from":
                        case "into": 
                            if(object.equals("isListName"))
                            {
                                if(LW.getLast().getName().equals("(")||LW.getLast().getName().equals(",")){
                                    create_word_and_add("L_name",isWord);
                                    break;
                                    //此处原来会把insert.s_vertical识别为T_name
                                }
                                if(LW.getLast().getName().equals(")"))
                                {
                                    create_word_and_add("newTable_name",isWord);
                                    break;
                                }
                                create_word_and_add("T_name",isWord);
                            }
                            else if(object.equals("isTLName"))//需要分割
                            {
                                String[] temp=isWord.split("\\."); //split识别"."的时候要输入"\\."
                                create_word_and_add("TS_name",temp[0]);
                                create_word_and_add(".",null);
                                create_word_and_add("T_name",temp[1]);
                            }
                            else
                                throw new Language_error(isWord+"，请检查该输入");
                            break;
                        case "select":
                        case "where":
                        case "and":
                        case "or":
                        case "set":
                            switch (object) {
                                case "isListName":
                                    create_word_and_add("L_name",isWord);
                                    break;
                            //需要分割
                                case "isTLName":{
                                    String[] temp=isWord.split("\\.");
                                    create_word_and_add("T_name",temp[0]);
                                    create_word_and_add(".",null);
                                    create_word_and_add("L_name",temp[1]);
                                        break OUTER;
                                    }
                            //需要分割
                                case "isTSTLName":{
                                    String[] temp=isWord.split("\\.");
                                    create_word_and_add("TS_name",temp[0]);
                                    create_word_and_add(".",null);
                                    create_word_and_add("T_name",temp[1]);
                                    create_word_and_add(".",null);
                                    create_word_and_add("L_name",temp[2]);
                                        break OUTER;
                                    }
                                case "isInteger":
                                {
                                    String nummark=LW.getLast().getName();
                                    if(nummark.equals("+")|nummark.equals("-"))
                                        LW.removeLast();
                                    else
                                        nummark="";
                                    create_word_and_add("Integer",nummark+isWord);break;
                                }
                                case "isDouble":
                                {
                                    String nummark=LW.getLast().getName();
                                    if(nummark.equals("+")|nummark.equals("-"))
                                        LW.removeLast();
                                    else
                                        nummark="";
                                    create_word_and_add("Double",nummark+isWord);break;
                                }
                                default:
                                    throw new Language_error(isWord+"，请检查该输入");
                            }
                            break;
                        case "update":
                            if(object.equals("isListName")) 
                                create_word_and_add("T_name",isWord);
                            else if(object.equals("isTLName"))//需要分割
                            {
                                String[] temp=isWord.split("\\."); 
                                create_word_and_add("TS_name",temp[0]);
                                create_word_and_add(".",null);
                                create_word_and_add("T_name",temp[1]);break;
                            }
                            else
                                throw new Language_error(isWord+"，请检查该输入");
                            break;
                        case "values":
                            if(object.equals("isInteger"))
                            {
                                String nummark=LW.getLast().getName();
                                if(nummark.equals("+")|nummark.equals("-"))
                                    LW.removeLast();
                                else
                                    nummark="";
                                create_word_and_add("Integer",nummark+isWord);
                            }
                            else if(object.equals("isDouble"))
                            {
                                String nummark=LW.getLast().getName();
                                if(nummark.equals("+")|nummark.equals("-"))
                                    LW.removeLast();
                                else
                                    nummark="";
                                create_word_and_add("Double",nummark+isWord);
                            }
                            else
                                throw new Language_error(isWord+"，请检查该输入");
                            break;
                        default:
                            switch(object)
                            {
                                case "isInteger":
                                {
                                    String nummark=LW.getLast().getName();
                                    if(nummark.equals("+")|nummark.equals("-"))
                                        LW.removeLast();
                                    else
                                        nummark="";
                                    create_word_and_add("Integer",nummark+isWord);break;
                                }
                                case "isDouble":
                                {
                                    String nummark=LW.getLast().getName();
                                    if(nummark.equals("+")|nummark.equals("-"))
                                        LW.removeLast();
                                    else
                                        nummark="";
                                    create_word_and_add("Double",nummark+isWord);break;
                                }
                                case "isListName"://也有可能是sequence或者cursor，但是暂时先不考虑
                                    create_word_and_add("L_name",isWord);break;
                                case "isTLName"://需要分割
                                {
                                    String[] temp=isWord.split("\\.");
                                    create_word_and_add("T_name",temp[0]);
                                    create_word_and_add(".",null);
                                    create_word_and_add("L_name",temp[1]);break;
                                }
                                case "isTSTLName"://需要分割
                                {
                                    String[] temp=isWord.split("\\.");
                                    create_word_and_add("TS_name",temp[0]);
                                    create_word_and_add(".",null);
                                    create_word_and_add("T_name",temp[1]);
                                    create_word_and_add(".",null);
                                    create_word_and_add("L_name",temp[2]);break;
                                }
                                default:
                                    throw new Language_error(isWord+"是非法单词");
                            }
                    }
                    else
                        nocreate=false;
    }
    
    private Coolean c_BuildWord(char c) throws Language_error
    {
        switch(c)
        {
            case ' ':case '\n':case '\r':case ';':
            //识别为stop的时候不将当前字符列为单词
            //case '\''://但是单引号是特例，另有专门的方法处理
                return Coolean.stop;
                
            case '(':
                this.bracketstack.add(new StringBuffer().append(c).toString());
                return Coolean.mark;
            case ')':
                if(bracketstack.remove().charAt(0)+1==c)//左小括号与右小括号的ASCII值相差1
                    return Coolean.mark;
                else
                    throw new Language_error("')'没有对应的'('相匹配");
            
            case '!':case '%':case '*':case '+':case ',':
            case '-':case '/'://46是'.'，要作为小数点保留//case '\"':
            case ':':case '<':case '=':case '>':case '^':case '\'':
                return Coolean.mark;//识别为mark的时候将当前字符列为单词
                
            case '0':case '1':case '2':case '3':case '4':
            case '5':case '6':case '7':case '8':case '9':
            case 'a':case 'b':case 'c':case 'd':case 'e':
            case 'f':case 'g':case 'h':case 'i':case 'j':
            case 'k':case 'l':case 'm':case 'n':case 'o':
            case 'p':case 'q':case 'r':case 's':case 't':
            case 'u':case 'v':case 'w':case 'x':case 'y':
            case 'z':case 'A':case 'B':case 'C':case 'D':
            case 'E':case 'F':case 'G':case 'H':case 'I':
            case 'J':case 'K':case 'L':case 'M':case 'N':
            case 'O':case 'P':case 'Q':case 'R':case 'S':
            case 'T':case 'U':case 'V':case 'W':case 'X':
            case 'Y':case 'Z':case '.':
                return Coolean.letter; 
            default:
                throw new Language_error(c+"是非法字符");
        }
    }
    
    private String Stringinquotation(int loopo/*,String wt*/) throws havenoend_string_error, null_escape_char_error {
        //loopo是引号所在的位置，包括单双引号,注意SQL只支持单引号字符串
        //反斜杠以及转义的识别是严重问题
        //String example1="'\""; 单引号和双引号区间内不同种引号的识别情况
        //char example2='"';
        char stop='\'';int loop=1;
        StringBuffer str=new StringBuffer();
        /*if(wt.equals("\""))
        stop='\"';
        else if(wt.equals("\'"))
            stop='\'';
        else
            throw new transfer_error();*/
        while(toSQL.charAt(loopo+loop)!=stop)
        {
            if(loopo+loop==toSQL.length()-1)
                throw new havenoend_string_error();
            if(toSQL.charAt(loopo+loop)=='\\')//出现转义的情况
            {
                str.append(Escape(loopo,loop));
                loop++;//跳过被转义的字符
            }
            else
                str.append(toSQL.charAt(loopo+loop));
            loop++;
        }
        //最后要将生成的String和String的结尾位置返回上层函数，同时将整个String视为一个"字"
        status=Coolean.letter;
        return str.toString();
    }
    
    //转义字符处理函数
    private char Escape(int loopo,int loop) throws null_escape_char_error
    {
        switch(toSQL.charAt(loopo+loop+1))
        {
            //case 'a': return '\a'; //java不支持\a？
            case 'b': return '\b';
            case 'f': return '\f';
            case 'n': return '\n';
            case 'r': return '\r';
            case 't': return '\t';
            //case 'v': return '\v'; //java不支持\v？
            case '\\': return '\\';
            case '\'': return '\'';
            case '\"': return '\"';
            //case '\?': return '\?';
            default: throw new null_escape_char_error();
        }
    }
    //生成简单SQL关键字的方法
    private void create_word_and_add(String name,String substance)
    {
        Word word=new Word(name,substance);
        if(Clause_label)
        {
            Clause_Word_List temp= (Clause_Word_List) this.LW.getLast();
            while(temp.getClause().getLast().getClass().getSimpleName().equals("Clause_Word_List"))
                temp= (Clause_Word_List) temp.getClause().getLast();
            temp.Add_word(word);
        }
        else
            LW.add(word);
    }
    //生成嵌套SQLWord的方法
    private Clause_Word_List create_Clauseword_and_add()
    {
        Clause_Word_List cwl=new Clause_Word_List();
        return cwl;
    }
    
    //通过status和nowstatus的取值和相互关系得出是否继续读入字符
    private boolean charStop(Coolean nowstatus)
    {
        if(status.equals(Coolean.stop))
            return false;
        else if(status.equals(nowstatus))
            return false;
        else 
            return true;
    }
    
    public static void main(String[] ar)
    {
        String[] SQL={
        "select * from (select k,i from last) where k=(select m from apple);"};
        for(int loopo=0;loopo<SQL.length;loopo++)
        {
        LinkedList<Word> words = null;
        try {
            HashMap test=new InitLanguage_node().setWord_Map();
            words=new Word_Segmentation_Machine(test).Segment(SQL[loopo]);
        } catch (Exception ex) {
             System.out.println(ex.getMessage());
            exit(1);
        }
        
        for(int loop=0;loop<words.size();loop++)
        {
            System.out.print(words.get(loop).getName());
            System.out.print(' ');
            System.out.println(words.get(loop).getSubstance());
        }
        }
    }
}