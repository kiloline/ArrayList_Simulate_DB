package Data.Vessel;

import Data.Numeral_Calculations.Evaluate;
import Data.Verticaltype.Vertical_Column;
import Data.Verticaltype.Vertical_Node;
import Data.classes.KVEntryImpl;
import Service_pkg.Fileloader.FileSystem_link_tree;
import m_Exception.runtime.deepCloneException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Evaluate_Word_List extends Word {
    List<Word> evaluate_word_list;
    Iterator<Word> evaluate_Iterator;
    int marknum,evaSize=0;
    /**
     * 记录所有计算中的数据表列名引用记录
     */
    HashMap<Integer,Vertical_Column> listlocal_length;

    private boolean canGetEvaluate;

    public Evaluate_Word_List(Word first) {
        super("Evaluate_SQL", null, first.getLocal()[0], first.getLocal()[1], false);
        evaluate_word_list=new LinkedList<>();
        listlocal_length=new HashMap<>();
    }

    public void Add_word(Word word)
    {
        this.evaluate_word_list.add(word);
    }

    public void Add_close() {
        StringBuilder sb=new StringBuilder();
        for(Word t:evaluate_word_list)
            sb.append(t.toString());
        substance=sb.toString();
        marknum=evaluate_word_list.size();
        evaluate_Iterator=evaluate_word_list.listIterator();
//        evaluate_word_list=null;
    }

//    public boolean hasnext(){
//        return this.evaluate_Iterator.hasNext();
//    }

    public void Add_words(List<Word> words) {
        this.evaluate_word_list=words;
    }

    public int startEvaluate()throws deepCloneException
    {//开始计算之前首先返回需要调度的列对象，作为缓存
        for(int loop=0;loop<marknum;loop++) {
            Word w = evaluate_Iterator.next();
            if (w.getClass().getSimpleName().equals("TablespaceTableList_name")) {
                Vertical_Column vc=FileSystem_link_tree.getReadonlyList((TablespaceTableList_name) w);
                listlocal_length.put(loop, vc);
                if (vc.getSize() > evaSize)
                    evaSize = vc.getSize();
            }
        }
        return evaSize;
    }
    public Object getNextEvaluate(int evalocal) throws Exception {
        Evaluate evaluate=new Evaluate();
        for(int loopi=0;loopi<marknum;loopi++){
            Word w = evaluate_Iterator.next();
            if(w.getClass().getSimpleName().equals("TablespaceTableList_name")){
                evaluate.pushElement(listlocal_length.get(loopi).getindex_element(evalocal));
            }
            else{
                evaluate.pushElement(w);
            }
        }
        return evaluate.getthisEvaluate();
    }
}
