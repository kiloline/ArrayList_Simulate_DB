package Service.Language_ExecutePlan_builder.ExecutePlan_Engine;

import Data.Vessel.Evaluate_Word_List;
import Data.Vessel.TablespaceTableList_name;
import Data.Vessel.TablespaceTable_name;
import Data.Vessel.Word;
import Data.classes.KVEntryImpl;

import java.util.*;

public class Select {
//    public static
    //select方法分析select子句中所有列名和数学表达式，返回列名与表名之间的对应关系
    //列名与表名之间，在select子句当中存在多种关系。如果只是简单的单独查询很简单，直接在Table_lists里面开一个地址放关系就行；
    //但是还存在算术表达式，这种东西就不能简单的只检查存在性，必须另外将三个名称统一起来，推给算术表达式才行。
    //而且如果涉及多表的时候，参与的列没有写明出自某个表某个空间的话，那么事后如何通知算术表达式也是一个问题。
    //假如，一个算术表达式中存在多个列，那么标记处理这些列还是用word比较方便，毕竟信息全面。
    public static Map<TablespaceTable_name,List<KVEntryImpl>> select(LinkedList<Word> clause)
    {
        boolean isEvaluate=false;
        int locationstart=0;
        Word tbsname=null,tbname=null,listname=null,new_listname=null;
        Map<TablespaceTable_name,List<KVEntryImpl>> Table_lists=new HashMap<>();
        List<Word> Eva_WL = null;
        Table_lists.put(null,new LinkedList<KVEntryImpl>());//用于存放未显式指定表名的列名

        Iterator<Word> words=clause.listIterator();
        for(int loop=0;words.hasNext();loop++)
        {
            Word temp=words.next();
            switch(temp.getType())
            {
                case ",":
                    if(isEvaluate)//如果是表达式的处理分支
                        //制作一个Evaluate_Word_List来处理
                    {
                        Evaluate_Word_List ewl=new Evaluate_Word_List(clause.get(locationstart));
                        ewl.Add_words(Eva_WL);
                        ewl.Add_close();
                        if(new_listname==null) {
                            new_listname = new Word("newL_name", ewl.toString(), ewl.getLocal()[0], ewl.getLocal()[1], false);
                            new_listname.setType("newL_name");
                        }
                        Table_lists.get(null).add(new KVEntryImpl(ewl,new_listname));
                    }
                    else
                    {
                        if(new_listname==null)
                            new_listname=listname;
                        StringBuilder mapKey=new StringBuilder();

                        TablespaceTable_name ttn=new TablespaceTable_name(clause.get(locationstart)).setAll(tbsname,tbname);

                        if(mapKey.length()!=0)
                        {
                            if(Table_lists.get(ttn)!=null)
                                Table_lists.get(ttn).add(new KVEntryImpl(listname,new_listname));
                            else {
                                List<KVEntryImpl> templist=new LinkedList<>();
                                templist.add(new KVEntryImpl(listname,new_listname));
                                Table_lists.put(ttn, templist);
                            }
                        }
                        else
                            Table_lists.get(null).add(new KVEntryImpl<>(listname,new_listname));
                    }

                    //最终清理所有临时变量，进入下一循环
                    isEvaluate=false;
                    tbsname=null;tbname=null;listname=null;new_listname=null;
                    locationstart=loop+1;
                    break;
                case "TS_name":
                    tbsname=temp;
                    break;
                case "T_name":
                    tbname=temp;
                    break;
                case "L_name":
                    listname=temp;
                    break;
                case "newList_name":
                    new_listname=temp;
                    break;

                case "(":
                case "Var":
                case ")":
                case "MathFunction":
                case "calculation_Mark_Link":
//                case "check_Mark_Link":
                    if(!isEvaluate) {
                        Eva_WL = new LinkedList<>();
                        isEvaluate=true;
                    }
                    if(listname!=null) {
                        Eva_WL.add(new TablespaceTableList_name(listname).setAll(tbsname,tbname,listname));
                        tbsname=null;tbname=null;listname=null;
                    }
                    Eva_WL.add(temp);

                    break;
            }
        }
        return Table_lists;
    }
}
