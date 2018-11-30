package Service_pkg.Language_ExecutePlan_builder.ExecutePlan_Engine;

import Data.Vessel.Evaluate_Word_List;
import Data.Vessel.Word;
import Data.classes.KVEntryImpl;
import Service_pkg.env_properties;
import m_Exception.type.Type_not_exist;

import java.util.LinkedList;
import java.util.List;

public class Insert {
    //先按照Oracle9i以前的版本来支持
    //目前的多表插入似乎只能以select作为数据来源，意义一般
    public static KVEntryImpl<String, KVEntryImpl<String, List<String>>> into(LinkedList<Word> clause)
    {
        KVEntryImpl<String,KVEntryImpl<String,List<String>>> tstl;
        String table=null,tablespace=null;
        LinkedList<String> list=new LinkedList<>();
        for(Word w:clause)
        {
            switch(w.getName())
            {
                case "T_name":
                    table=w.getSubstance();
                    break;
                case "TS_name":
                    tablespace=w.getSubstance();
                    break;
                case "L_name":
                    list.add(w.getSubstance());
                    break;
            }
        }

        if(tablespace==null)
            tablespace= env_properties.getEnvironment("default_tbs");

        tstl=new KVEntryImpl<>(tablespace,new KVEntryImpl<>(table,list));

        return tstl;
    }

    //为了简化values子句，所有项全走Evaluate走一遍是可行的
    //values传入的子句单词列表不能含有额外的括号
    public static LinkedList values(LinkedList<Word> clause) throws Type_not_exist {
        LinkedList<Evaluate_Word_List> value=new LinkedList();
        boolean isEvaluate=false;
        for(Word w:clause)
        {
            Evaluate_Word_List ewl=null;
            switch (w.getType())
            {
                case ",":
                    ewl.Add_close();
                    break;

                case "L_name":
                case "Var":
                case "(":
                case ")":
                case "MathFunction":
                case "calculation_Mark_Link":
                case "check_Mark_Link":
                    if(ewl==null)
                        ewl=new Evaluate_Word_List(w);
                    ewl.Add_word(w);
                    break;
            }
        }
        return value;
    }
}
