package Data.Vessel;

import m_Exception.Language_error;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WhereFilterconditions {
    private List<Where_Subcondition> condition;
    private List<Word> linkmark;

    public WhereFilterconditions() {
        condition=new LinkedList<>();
        linkmark=new LinkedList<>();
    }
    int loop=-1;

    public void setAll(List conditions,List linkmarks) throws Language_error {
        condition=conditions;
        linkmark=linkmarks;
        if(condition.size()-linkmark.size()==1)
            ;
        if(condition.size()-linkmark.size()==0)
            ;
        else
            throw new Language_error(linkmark.get(0),"");
    }

    public void addlinkmark(Word w){
        linkmark.add(w);
    }

    public Word getNextLink()
    {
        if(loop>=linkmark.size())
            return null;
        return linkmark.get(loop);
    }
    public Where_Subcondition getNextCondition(){
        if(loop>=condition.size())
            return null;
        return condition.get(loop);
    }

    public boolean hasNext()
    {
        loop++;
        return loop<linkmark.size()||loop<condition.size();
    }
}
