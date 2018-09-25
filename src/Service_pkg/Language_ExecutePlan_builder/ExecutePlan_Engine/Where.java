package Service_pkg.Language_ExecutePlan_builder.ExecutePlan_Engine;

import Data.Vessel.*;
import m_Exception.Language_error;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Where {
    //对于每一个where项都是为布尔表达式，对左部和右部分开计算，最后通过中间比较符号合并到一起
    public static Map<TablespaceTable_name, WhereFilter> where(List<Word> clause) throws Language_error {
        Word first=clause.get(0),tbsname = null,tbname = null,listname = null;
        boolean isEvaluate=false,noTL=true,middlemark=false,isfirst=true;
        List<Word> Eva_WL = null;
        WhereFilter filter=new WhereFilter(clause.get(0));
        List<List> filterItem=new ArrayList<>();//条件列表
        /*
        条件列表要达到的目的是：方便执行器优化操作；记录条件和表对象之间的对应关系
        如果一个条件当中参与的列来自于单个表，该条件在运算的时候应该参加子表筛选
        如果一个条件当中参与的列来自于多个表，该条件应当在连接多表的时候起作用
        这样需要在一开始的时候得到来自
        */

        int loop=0;//循环长度标号
        for(Word word:clause)
        {
            switch (word.getType())
            {
                case "check_Mark_Link":
                    middlemark=true;
                    filter..add(new TablespaceTableList_name(first).setAll(tbsname,tbname,listname));
                    middle.add(word);
                    middlemark=true;
                    break;
                case "bool_calculation_Mark_Link"://and/or
                    if(!middlemark)
                        throw new Language_error(word,"布尔运算符必须出现在布尔表达式后。");
                    Word rightW;
                    if(isEvaluate) {
                        Evaluate_Word_List ewl = new Evaluate_Word_List(Eva_WL.get(0));
                        ewl.Add_words(Eva_WL);
                        rightW=ewl;
                    }
                    else
                        rightW=new TablespaceTableList_name(first).setAll(tbsname, tbname, listname);
                    right.add(rightW);

                    Eva_WL=null;
                    tbsname = null;tbname = null;listname = null;
                    isfirst=true;
                    middlemark=false;
                    break;

                case "TS_name":
                    tbsname=word;
                    if(isfirst) {
                        isfirst=false;
                        first=word;
                    }
                    break;
                case "T_name":
                    tbname=word;
                    if(isfirst) {
                        isfirst=false;
                        first=word;
                    }
                    break;
                case "L_name":
                    listname=word;
                    if(isfirst) {
                        first=word;
                    }
                    isfirst=true;
                    break;

                case "(":
                case "Var":
                case ")":
                case "MathFunction":
                case "calculation_Mark_Link":
                    if(!middlemark)
                        throw new Language_error(word,"算术表达式必须出现在比较运算符后。");
                    if(!isEvaluate) {
                        Eva_WL = new LinkedList<>();
                        isEvaluate=true;
                    }
                    if(listname!=null) {
                        Eva_WL.add(new TablespaceTableList_name(first).setAll(tbsname, tbname, listname));

                    }
                    Eva_WL.add(word);
                    break;
            }
            loop++;
        }
    }
}
