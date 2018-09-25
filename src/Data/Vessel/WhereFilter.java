package Data.Vessel;

import m_Exception.Language_error;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WhereFilter extends Word {
    public WhereFilter(Word first) {
        super("WhereFilter", null, first.getLocal()[0], first.getLocal()[1], false);
        left=new LinkedList<>();
        middle=new LinkedList<>();
        right=new LinkedList<>();
        linkmark=new LinkedList<>();
    }

    List<Word> left,middle,right,linkmark;
    Iterator<Word> leftI,middleI,rightI,linkmarkI;
    int loop=-1;

    public void setAll(List leftmarks,List middlemarks,List rightmaeks,List linkmarks) throws Language_error {
        left=leftmarks;
        right=rightmaeks;
        middle=middlemarks;
        linkmark=linkmarks;
        if(left.size()!=middle.size())
            throw new Language_error(this,"");
        if(left.size()!=right.size())
            throw new Language_error(this,"");
        if(left.size()!=linkmark.size()+1)
            throw new Language_error(this,"");

        leftI=left.iterator();
        middleI=middle.iterator();
        rightI=right.iterator();
        linkmarkI=linkmark.iterator();
    }

    public void addleft(TablespaceTable_name tt){
        left.add(tt);
    }

    public void addmiddle(Word w){
        middle.add(w);
    }

    public void addright(Evaluate_Word_List w){
        middle.add(w);
    }

    public void addlinkmark(Word w){
        linkmark.add(w);
    }

    public Word getNextLeft(){
        return leftI.next();
    }

    public Word getNextMiddle(){
        return middleI.next();
    }

    public Object getNextRight() throws Exception {
        loop++;
        return ((Evaluate_Word_List)rightI).getNextEvaluate(loop);
    }

    public Word getNextLink()
    {
        return linkmarkI.next();
    }

    public boolean hasNext()
    {
        return linkmarkI.hasNext()||leftI.hasNext();
    }
}
