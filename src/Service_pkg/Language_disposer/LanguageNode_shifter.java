package Service_pkg.Language_disposer;

import Data.classes.Language_node;
import Data.Vessel.Word;

import java.util.*;

import m_Exception.Language_error;

/**
 *
 * @author gosiple
 * 自动机规约的限制，以后设想的向上规约一步只能规约个别单词；大部分“关键字”是无法规约的，只有那些性质接近变量/常量的关键字可以被规约
 */
public class LanguageNode_shifter 
{
    final HashMap<String,Language_node> word_Map;
    public LanguageNode_shifter(HashMap word_Map)
    {
        this.word_Map=word_Map;
    }
    
    public void shifter(LinkedList<Word> words) throws Language_error
    {
        for(Language_node Ln:word_Map.values())
        {
            try{
            Ln.unlockAll();
            }
            catch(Exception ex){}
        }
        switch(words.get(0).getName())
        {//通过限制SQL单词节点能转移到的下一节点来对语法DFA进行规范，避免一些不必要的错误
            case "create":
            {
                word_Map.get("L_name").lock_node("newL_name");
                word_Map.get("T_name").lock_node(",",".",";","where","values","set","union","add","del");
                break;
            }
            case "drop":
                break;
            case "select":
                break;
            case "update":
                break;
            case "insert":
                break;
            case "delete":
                break;
            case "commit":
                break;
            case "alter":
            {
                word_Map.get("SQL_initVar").lock_node(")",",");
                break;
            }
            case "show":
                break;
            default :
                throw new Language_error(words.get(0),"不正确的开始符号");
        }
        Language_node fountain=word_Map.get(words.get(0).getName());

        Iterator<Word> wordIterator=words.listIterator();
        wordIterator.next();
        for(;wordIterator.hasNext();)
        {
            //System.out.println(words[loop].getName());
            Word word=wordIterator.next();
            fountain=fountain.to_status(word.getType());
            if(fountain==null)
                throw new Language_error(word,word.getType()+"在此处是不允许的单词类型");
        }
        if(fountain.getoption().equals(";")) {
        } else
            throw new Language_error(words.getLast(),"必须以';'结尾");
    }
}
