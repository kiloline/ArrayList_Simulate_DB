package Service.Language_ExecutePlan_builder.ExecutePlan_Engine;

import Data.Vessel.Word;
import Data.classes.Table;
import Service.Language_ExecutePlan_builder.ExecutePlan_builder;
import Service.Service;
import m_Exception.FileSystem.ClassNotFound;
import m_Exception.Language_error;

import java.util.*;

/**
 * from中，对于既有表名又有表空间名的表，同时在map里加上"表空间' '表"和"表"两个字符串进行匹配
 */
public class From {
    //from方法分析from自举中所有表名的存在性，并且通过map将表名和对应的表对象进行关联
    public static Map<String,Table> from(ExecutePlan_builder backstage, LinkedList<Word> clause) throws ClassNotFound, Language_error {
        List<String> Clause_Table=new ArrayList<>();
        Map<String,Table> Return=new HashMap<>();
        String TS_name=null,T_name=null,newT_name=null;
        boolean hasClause=false;
        for(Word w:clause)
        {
            switch(w.getType())
            {
                case "TS_name":
                    TS_name=w.getSubstance();
                    break;
                case "T_name":
                    T_name=w.getSubstance();
                    hasClause=false;
                    break;
                case "newT_name":
                    newT_name=w.getSubstance();
                    break;
                case "Clause_SQL":
                    hasClause=true;
                    break;
                case ",":
                {
                    String pstname=TS_name,ptname=T_name,pntname=newT_name;
                    TS_name=null;T_name=null;newT_name=null;
                    Table from_tb=null;
                    if(hasClause)
                    {
                        if(Return.get(pntname)!=null)
                            throw new Language_error(w, "同名表重复");

                        Clause_Table.add(pntname);
                        Return.put(pntname, from_tb);
                        break;
                    }
                    //如果双表关联，来自两个表空间的不同表名，要允许只按表名查找，而不是必须写全表空间和表两个名字
                    from_tb=backstage.CheckClasses(pstname,ptname);
                    if(pntname!=null)
                        ptname=pntname;
                    if (pstname == null)
                        pstname= Service.env_properties.getEnvironment("default_tbs");

                    Return.put(pstname+' '+ptname,from_tb);
                    if(Return.get(ptname)!=null) {
                        if(Clause_Table.contains(ptname))
                            break;
                        else {
                            Return.put(ptname, null);
                            break;
                        }
                    }
                    Return.put(ptname, from_tb);

                    break;
                }
            }
        }
        return Return;
    }
}
