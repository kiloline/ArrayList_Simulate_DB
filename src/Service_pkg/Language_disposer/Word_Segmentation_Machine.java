package Service_pkg.Language_disposer;
import Data.Vessel.Word;
import Data.judgement.Coolean;
import m_Exception.Language_error;
import m_Exception.null_escape_char_error;

import java.util.HashSet;
import java.util.LinkedList;

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
    HashSet<String> WordMap;
    LinkedList<Word> LW;//主SQL序列
    Coolean status;
    String toSQL;
    int nowline,nowlist,streamPoint=0;
    public Word_Segmentation_Machine(HashSet<String> map)
    {
        LW = new LinkedList<>();
        WordMap=map;
        nowline=1;
        nowlist=0;
    }
    public LinkedList<Word> Segment(String content) throws Exception
    {
        streamPoint=0;
        LW.clear();
        toSQL=content;
        StringBuffer toWord=new StringBuffer();
        status=Coolean.letter; 
        for(int loop=0;loop<toSQL.length();loop++)
        {
            nowlist++;
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
                if(!WordMap.contains(isWord))
                {
                    this.build_undefined_var(isWord);
                }
                else if(Stop&&toWord.length()==0)
                    continue;
                else if(c=='\'')
            //在这里通过略微损失效率，每字符进行比较，从而正确完成解析
            //遇到单引号，需要构造String
                {
                    StringBuffer quo=new StringBuffer();
                    create_word_and_add(isWord,null);
                    Integer cp_length=Stringinquotation(loop,quo);
                    create_word_and_add("String",quo.toString());
                    loop=loop+cp_length;//loop停在后面的单引号上
                    nowstatus=Coolean.mark;
                    toWord.delete(0, toWord.length());
                    continue;
                }
                else
                {
                    create_word_and_add(isWord,null);
                }
                toWord.delete(0, toWord.length());
            }
            if(status.equals(Coolean.mark))
            {//针对具有二义性的符号准备的分词方式，如将<=识别为小于等于号而不是小于和等于
                if(WordMap.contains(toWord.toString()))
                    if(WordMap.contains(toWord.toString()+c))
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
        
        return LW;
    }
    
    private void build_undefined_var(String isWord) throws Language_error
    {
        boolean nocreate=true;
        //接下来有两种可能，1.这是一个对象名称、2.这是不存在的单词
        if(status.equals(Coolean.mark))
            throw new Language_error(nowline,nowlist,isWord+"符号不存在");
                    
        else
            create_word_and_add(isWord,null);
    }
    
    private Coolean c_BuildWord(char c) throws Language_error
    {
        switch(c)
        {
            case '\n'://遇到换行符将行列数据刷新
                nowline=nowline+1;
                nowlist=0;
            case ' ':case '\r':case ';':
            //识别为stop的时候不将当前字符列为单词
            //case '\''://但是单引号是特例，另有专门的方法处理
                return Coolean.stop;
                
            case '(': case ')':
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
                throw new Language_error(nowline,nowlist,"该字符是非法字符");
        }
    }
    
    private Integer Stringinquotation(int loopo,StringBuffer str) throws Language_error, null_escape_char_error {
        //loopo是引号所在的位置，包括单双引号,注意SQL只支持单引号字符串
        //反斜杠以及转义的识别是严重问题
        //String example1="'\""; 单引号和双引号区间内不同种引号的识别情况
        //char example2='"';
        char stop='\'';int loop=1;
        while(true)
        {
            nowlist++;
            if(loopo+loop==toSQL.length()-1)
                throw new Language_error(nowline,nowlist,"没有终结符号的字符串");

            if(toSQL.charAt(loopo+loop)=='\n')
            {
                nowline=nowline+1;
                nowlist=0;
            }
            else if(toSQL.charAt(loopo+loop)=='\\')//出现转义的情况
            {
                str.append(Escape(loopo,loop));
                loop++;//跳过被转义的字符
            }
            else if(toSQL.charAt(loopo+loop)==stop)//应对标准形式的Oeacle单引号转义
            {
                if(toSQL.charAt(loopo+loop+1)==stop) {
                    str.append(stop);
                    loop++;
                }
                else
                    break;
            }
            else
                str.append(toSQL.charAt(loopo+loop));
            loop++;
        }
        //最后要将生成的String和String的结尾位置返回上层函数，同时将整个String视为一个"字"
        status=Coolean.letter;
        return loop;
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
            default: throw new null_escape_char_error(nowline,nowlist);
        }
    }
    //生成简单SQL关键字的方法
    private void create_word_and_add(String name,String substance)
    {
        Word word;
        if(name.equals("String"))
            word=new Word(name,substance,nowline,nowlist-substance.length(),status.equals(Coolean.mark));
        else
            word=new Word(name,substance,nowline,nowlist-name.length(),status.equals(Coolean.mark));
        LW.add(word);
    }
    
    //通过status和nowstatus的取值和相互关系得出是否继续读入字符
    private boolean charStop(Coolean nowstatus)
    {
        if(status.equals(Coolean.stop))
            return false;
        else if(status.equals(nowstatus)&&status.equals(Coolean.letter))
            return false;
        else 
            return true;
    }
}