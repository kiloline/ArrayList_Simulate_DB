package Service.Language_ExecutePlan_builder.ExecutePlan_Engine;

import Data.Vessel.TablespaceTableList_name;
import Data.Vessel.Word;
import m_Exception.Language_error;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Where {
    //对于每一个where项都是为布尔表达式，对左部和右部分开计算，最后通过中间比较符号合并到一起
    public static void where(List<Word> clause) throws Language_error {
        Word first=clause.get(0),tbsname = null,tbname = null,listname = null;
        boolean isEvaluate=false,noTL=true,middlemark=false,isfirst=true;
        List<Word> left=new LinkedList<>(),middle=new LinkedList<>(),right=new LinkedList<>(),
                Eva_WL = null,linkmark=null;
        List<List> filterItem=new ArrayList<>();//为方便执行器优化操作，

        int loop=0;//循环长度标号
        for(Word word:clause)
        {
            switch (word.getType())
            {
                case "check_Mark_Link":
                    middlemark=true;
                    left.add(new TablespaceTableList_name(first).setAll(tbsname,tbname,listname));
                    break;
                case "bool_calculation_Mark_Link"://and/or
                    if(!middlemark)
                        throw new Language_error(word,"布尔运算符必须出现在布尔表达式后。");
                    break;
                case ",":
                    if(!middlemark)
                        throw new Language_error(word,"where条件必须是布尔表达式。");

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
