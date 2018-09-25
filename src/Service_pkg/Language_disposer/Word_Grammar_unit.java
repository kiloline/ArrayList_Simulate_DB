package Service_pkg.Language_disposer;

import Data.Vessel.Clause_Word_List;
import Data.Vessel.Word;
import Utils.Check.check_regular;
import m_Exception.Language_error;
import m_Exception.bracket_matching_error;

import java.util.*;

/**
 * 使用反向倒转来表示一对多关系，即Key、Value位置反转
 * 对*和=符号的特定区分
 */
public class Word_Grammar_unit {
    private check_regular CR;
    private LinkedList<String> markstack;
    private HashSet<String> word_List;
    private List<HashMap<String,String>> Grammar_unit;
    private LinkedList<Word> bracketstack;//括号匹配栈，专用于嵌套子句，泛型改成Word是为了方便定位
    private LinkedList<Integer> Clause_bracketstack;
    private LinkedList<Clause_Word_List> Clause_list;
    private LinkedList<Word> LW;
    private boolean word_Add_Deflection;

    public Word_Grammar_unit(HashSet<String> word_List,List<HashMap<String,String>> Grammar_unit)
    {
        this.word_List=word_List;
        this.Grammar_unit=Grammar_unit;
        markstack=new LinkedList<>();
        bracketstack=new LinkedList<>();
        Clause_list=new LinkedList<>();
        Clause_bracketstack=new LinkedList<>();
        word_Add_Deflection=false;
    }
    public LinkedList<Word> set_Word_Grammar_unit(LinkedList<Word> words) throws bracket_matching_error {//fugue位置矛盾，如果放在前面的话会导致非关键字识别错误，如果放在后面会导致部分关键字识别错误
        LW=new LinkedList<>();
        for(Iterator<Word> wordIterator = words.listIterator();wordIterator.hasNext();)
        {
            Word word=wordIterator.next();
            if(word_List.contains(word.getName()))//判断是否为关键字
            {
                if(!word.isMark())
                    markstack.add(word.getName());
                else if(word.getName().equals("("))
                {
                    bracketstack.add(word);
                    if(markstack.getLast().equals("from"))
                    {//位于from后出现的括号必然是
                        Clause_Word_List cwltemp=new Clause_Word_List(word.getLocal()[0],word.getLocal()[1]);
                        Clause_list.add(cwltemp);
                        Clause_bracketstack.add(bracketstack.size()-1);
                        word_Add_Deflection=true;
                        continue;//用于标识嵌套SQL的括号不加入LW列表中
                    }
                    //以后可能添加where下的嵌套子句，但是现在不加入，只靠from也能完成大部分功能
                }
                else if(word.getName().equals(")"))
                {
                    if(Clause_bracketstack.getLast()+1==bracketstack.size())
                    {
                        word_Add_Deflection=false;//先暂时放开单词加入LW列表的压制，免得嵌套SQL把自己加入到自己的列表中
                        add_Word(this.Clause_list.remove());
                        Clause_bracketstack.remove();
                        this.word_Add_Deflection=!Clause_bracketstack.isEmpty();//然后根据嵌套标量栈的记录重新建立压制
                        bracketstack.remove();
                        continue;//用于标识嵌套SQL的括号不加入LW列表中
                    }
                    bracketstack.remove();
                }
            }
            else
                switch(check_regular.regular(word.getName()))
                {//number的正负号要处理一下
                    case "isInteger":{
                        word.setSubstance(isnumber_process()+word.getName());
                        word.setName("Integer");
                        break;
                    }
                    case "isDouble":{
                        word.setSubstance(isnumber_process()+word.getName());
                        word.setName("Double");
                        break;
                    }
                    case "isListName":{//newlistname
                        word.setSubstance(word.getName());
                        if(LW.getLast().getName().equals("L_name"))
                            word.setName("newL_name");
                        else if(LW.getLast().getName().equals("Clause_SQL"))
                            word.setName("newT_name");
                        else if(LW.getLast().getName().equals("T_name"))//尚未包含嵌套子句的识别
                            word.setName("newT_name");
                        else switch (markstack.getLast()) {
                            case "update":
                            case "table":
                            case "from":
                                word.setName("T_name");
                                break;
                            case "tablespace":
                                word.setName("TS_name");
                                break;
                            default:
                                word.setName("L_name");
                                break;
                        }
                        break;
                    }
                    case "isTLName":{//newtablename
                        String tl[]=word.getName().split("\\.");
                        int[] index=word.getLocal();
                        switch (markstack.getLast()) {
                            case "table":
                            case "update":
                            case "into":
                            case "from":
                                add_Word(new Word("TS_name", tl[0], index[0], index[1], false));
                                add_Word(new Word(".", null, index[0], index[1] + tl[0].length(), false));
                                word = new Word("T_name", tl[1], index[0], index[1] + tl[0].length() + 1, false);
                                break;
                            default:
                                add_Word(new Word("T_name", tl[0], index[0], index[1], false));
                                add_Word(new Word(".", null, index[0], index[1] + tl[0].length(), false));
                                word = new Word("L_name", tl[1], index[0], index[1] + tl[0].length() + 1, false);
                                break;
                        }
                        break;
                    }
                    case "isTSTLName":{
                        String tl[]=word.getName().split("\\.");
                        int[] index=word.getLocal();
                        int list=index[1];
                        add_Word(new Word("TS_name",tl[0],index[0],list,false));
                        add_Word(new Word(".",null,index[0],list+tl[0].length(),false));
                        add_Word(new Word("T_name",tl[1],index[0],list+tl[0].length()+1,false));
                        add_Word(new Word(".",null,index[0],list+tl[0].length()+tl[1].length(),false));
                        word=new Word("L_name",tl[2],index[0],list+tl[0].length()+tl[1].length()+2,false);
                        break;
                    }
                }
            add_Word(word);
        }
        if(!bracketstack.isEmpty())
        {
            StringBuilder error_message=new StringBuilder();
            while(!bracketstack.isEmpty())
                error_message.append(new Language_error(bracketstack.remove(),"出现了没有右括号对应的左括号\n").getMessage());
            throw new bracket_matching_error(error_message.toString());
        }
        return LW;
    }

    private void add_Word(Word word)
    {
        fugue(word);
        if(word_Add_Deflection){
            Clause_list.getLast().Add_word(word);
        }
        else {
            LW.add(word);
        }
    }

    private Word fugue(Word word)
    {
        if(word.getClass().getSimpleName().equals("Clause_Word_List"))
        {
            word.setType("Clause_SQL");
            return word;
        }
        for(HashMap<String,String> unit:Grammar_unit)
        {
            if(word.getName().equals("*"))
            {
                if(markstack.getLast().equals("select"))
                    break;
            }
            if(word.getName().equals("="))
            {
                if(markstack.getLast().equals("set"))//将set后的等号视为赋值符
                {
                    break;
                }
            }
            if(unit.get(word.getName())!=null) {
                word.setType(unit.get(word.getName()));
                return word;
            }
        }
        word.setType(word.getName());
        return word;
    }

    private String isnumber_process()//确定number的正负号
    {
        String nummark=LW.getLast().getName();
        String fronttype=LW.get(LW.size()-2).getType();
        String uppertype=LW.get(LW.size()-3).getType();
//        switch(uppertype)
//        {
//            case "Var":
//            case "L_name":
//            case ")":
//        }
        if(nummark.equals("+")||nummark.equals("-"))
        {
            switch(fronttype)
            {
                case "L_name":
                case "Var":
                case ")":
                    nummark="";
                    break;
                default:
                    LW.removeLast();
            }
        }
        else
            nummark="";
        return nummark;
    }
}
