package Service_pkg.Language_ExecutePlan_builder.ExecutePlan_Engine;

import Data.Vessel.*;
import Data.classes.Table;
import m_Exception.Language_error;

import java.util.*;

public class Where {
    List<Word> bcML;
    List<Where_Subcondition> conditionList;
    Map<TablespaceTable_name, Table> regTables;

    public Where(Map<TablespaceTable_name, Table> from_result) {
        bcML = new LinkedList<>();
        conditionList = new LinkedList<>();
        regTables = from_result;
    }

    //对于每一个where项都是为布尔表达式，对左部和右部分开计算，最后通过中间比较符号合并到一起
    //通过引入from子句的分析结果，规定不在from当中声明的表不能参与where计算
    public Map<TablespaceTable_name, WhereFilterconditions> where(List<Word> clause) throws Language_error {
        Word first = null, tbsname = null, tbname = null, listname = null;
        boolean isEvaluate = false, sampleNameCheck = true, middlemark = false, isfirst = true;

        first = clause.get(0);
        /*
        条件列表要达到的目的是：方便执行器优化操作；记录条件和表对象之间的对应关系
        如果一个条件当中参与的列来自于单个表，该条件在运算的时候应该参加子表筛选
        如果一个条件当中参与的列来自于多个表，该条件应当在连接多表的时候起作用
        这样需要在一开始的时候得到来自上层组件的支援，确定表->列关系的对应性
        */
        /*
         * 另外在组织where条件的时候，需要注意条件之间的先后顺序和布尔关系*/
        /*
         * 如果在两个表的条件列表之间出现布尔条件连接词（and/or），
         * and：前后两表尝试进行内连接
         * or：前后两表尝试进行外连接
         */
        Iterator<Word> clauseIter = clause.iterator();
        boolean boolcodition = false;
        while (true) {
            TablespaceTableList_name left = null;
            Word middle = null;
            Word word = clauseIter.next();
            List<Word> Eva_WL = new LinkedList<>();//布尔表达式右部的单词列表
            TablespaceTable_name tt2check = null;
            switch (word.getType()) {
                case "bool_calculation_Mark_Link"://and/or
                    if (!middlemark) throw new Language_error(word, "布尔运算符必须出现在布尔表达式后。");

                    boolcodition = true;
                    bcML.add(word);

                    tbsname = null;
                    tbname = null;
                    listname = null;
                    isfirst = true;//
                    middlemark = false;
                    break;
                case "check_Mark_Link"://>=<=
                    left = checkList2Table(tbsname, tbname, listname);
                    tt2check = left.getTTname();
                    middle = word;
                    middlemark = true;
                    isfirst = true;
                    break;
                case "TS_name":
                    tbsname = word;
                    if (isfirst) {
                        isfirst = false;
                        first = word;
                    }
                    break;
                case "T_name":
                    tbname = word;
                    if (isfirst) {
                        isfirst = false;
                        first = word;
                    }
                    break;
                case "L_name":
                    listname = word;
                    if (isfirst) {
                        first = word;
                    }
                    isfirst = true;
                    break;
                case ","://应对多值函数使用
                case "(":
                case "Var":
                case ")":
                case "MathFunction":
                case "calculation_Mark_Link":
                    if (!middlemark) throw new Language_error(word, "算术表达式必须出现在比较运算符后。");
                    if (!isEvaluate) {
                        isEvaluate = true;
                    }
                    if (listname != null) {
                        TablespaceTableList_name right=checkList2Table(tbsname, tbname, listname);
                        Eva_WL.add(right);
                    }
                    Eva_WL.add(word);
                    break;
            }
            if (!clauseIter.hasNext() || boolcodition) {
                boolcodition = false;
                if (!isEvaluate) {
                    Eva_WL.add(new TablespaceTableList_name(first).setAll(tbsname, tbname, listname));
                }
                makeCondition(Eva_WL, tt2check, left, middle);

                if (!clauseIter.hasNext()) break;
            }
        }
        return reOrderConditions();
    }

    private Map<TablespaceTable_name, WhereFilterconditions> reOrderConditions() throws Language_error {
        Map<TablespaceTable_name, WhereFilterconditions> whereFilterMap = new HashMap<>();//条件列表

        int nextstart = 0, nowReorderNumber = 0;
        TablespaceTable_name nowCheckName = conditionList.get(nextstart).getBelongTable();
        while (nextstart < this.conditionList.size() - 1) {
            for (int loop = nextstart + 1; loop < this.conditionList.size(); loop++) {
                if (conditionList.get(loop).getBelongTable().equals(nowCheckName)) {
                    Where_Subcondition temp = conditionList.remove(loop);
                    Word wtemp = this.bcML.get(loop - 1);

                    conditionList.add(nextstart + 1, temp);
                    bcML.add(nextstart, wtemp);

                    nowReorderNumber++;
                }
            }
            WhereFilterconditions filter = new WhereFilterconditions();

            if (nextstart + nowReorderNumber == this.conditionList.size())
                filter.setAll(conditionList.subList(nextstart, nextstart + nowReorderNumber), bcML.subList(nextstart, nextstart + nowReorderNumber - 1));
            else
                filter.setAll(conditionList.subList(nextstart, nextstart + nowReorderNumber), bcML.subList(nextstart, nextstart + nowReorderNumber));
            whereFilterMap.put(nowCheckName, filter);

            nextstart = nextstart + 1 + nowReorderNumber;
            nowReorderNumber = 0;
        }
        return whereFilterMap;
    }

    private void makeCondition(List<Word> Eva_WL, TablespaceTable_name tt2check, TablespaceTableList_name left, Word middle) {
        Evaluate_Word_List ewl;
        ewl = new Evaluate_Word_List(Eva_WL.get(0));
        ewl.Add_words(Eva_WL);


        for (int loop = 0; loop < ewl.getSize(); loop++) {//对一个条件中所有出现的列是否在同一个表当中的检查
            Word w = ewl.getEvaluatePart(loop);
            if (w.getClass().getSimpleName().equals("TablespaceTableList_name")) {
                if (!tt2check.equals(((TablespaceTableList_name) w).getTTname())) {
                    tt2check = new TablespaceTable_name("", "");
                    break;
                }
            }
        }

        conditionList.add(new Where_Subcondition().
                setBelongTable(tt2check).
                SetConditions(left, middle, ewl));
    }

    /*
    检查列->表对应关系的时候，第一需要基于一致的表空间名称和表名称，判断这个列是否存在；
    第二需要基于已存在的列名称反推/补全表空间名称和表名称。
     */
    private TablespaceTableList_name checkList2Table(Word tbsname, Word tbname, Word listname) throws Language_error {
        boolean isfind = false;
        TablespaceTable_name result = null;
        if (tbsname == null && tbname == null) {
            for (TablespaceTable_name tt : regTables.keySet()) {
                if (tbname == null || tt.getTable().equals(tbname.getSubstance()))
                    if (regTables.get(tt).checkselectlist(listname.getSubstance())) {
                        if (isfind == false) {
                            isfind = true;
                            result = tt;
                        } else break;
                    }
            }
            if (isfind == false) throw new Language_error(listname.getLocal()[0], listname.getLocal()[1], "没有找到列对应的表");
            else if (result == null)
                throw new Language_error(listname.getLocal()[0], listname.getLocal()[1], "一个列在两个表当中出现");
            else
                return new TablespaceTableList_name(listname).setAll(result.getSpace(), result.getTable(), listname.getSubstance());
        } else {
            if (regTables.get(new TablespaceTable_name(tbsname.getSubstance(), tbname.getSubstance())).checkselectlist(listname.getSubstance()))
                return new TablespaceTableList_name(tbsname).setAll(tbsname, tbname, listname);
        }
        throw new Language_error(listname.getLocal()[0], listname.getLocal()[1], "没有找到列对应的表");
    }
}
