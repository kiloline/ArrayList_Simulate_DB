package Service_pkg.Language_ExecutePlan_builder;

import Data.Vessel.Word;

import java.util.LinkedList;
import java.util.List;

/**
 * @author gosiple
 * 单词分类器，用于第一步粗筛，将整句中的单词按照一定得语法格子分开（也就是俗称的子句），传递到下一层
 * 处理子查询的时候尤其要小心，最好设置两个方式来传递
 * 目前自从对分析复杂select的计划提上日程之后，分类器就负担了绝大部分语义分析的工作
 * 被迫写成了复杂的递归，我真的不想这么写的，但是光是写成这样计算量已经有点大了，递推执行……还完全没有方案。
 */
public class Word_Classifier {
    public static List<List<Word>> toFind_clause_start(LinkedList<Word> words, List<String> clause_stop)
    {
        LinkedList<Word> toDiv=(LinkedList<Word>) words.clone();
        boolean in=false;
        List<List<Word>> Return=new LinkedList<>();
        LinkedList<Word> lw=new LinkedList<>();
        for(;!toDiv.isEmpty();)
        {
            //toDiv.get(0).getName();
            if(clause_stop.indexOf(toDiv.get(0).getName())!=-1)
            {
                in=!in;
            }
            if(in)
            {
                lw.add(toDiv.remove());
            }
            else
            {
                Return.add(lw);
                lw=new LinkedList<>();
            }
        }
        Return.add(lw);
        return Return;
    }
}
