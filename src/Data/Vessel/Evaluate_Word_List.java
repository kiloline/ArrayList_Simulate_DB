package Data.Vessel;

import Data.Numeral_Calculations.Evaluate;
import Data.Verticaltype.Vertical_Column;
import Data.Verticaltype.Vertical_Node;
import Data.classes.KVEntryImpl;
import Service_pkg.Fileloader.FileSystem_link_tree;
import m_Exception.runtime.deepCloneException;

import java.util.*;

public class Evaluate_Word_List extends Word {
    private List<Word> evaluate_word_list;
    int marknum=0,evaSize=0;
    /**
     * 记录所有计算中的数据表列名引用记录
     */
    HashMap<Integer,Vertical_Column> listlocal_length;

    private boolean canGetEvaluate;

    public Evaluate_Word_List(Word first) {
        super("Evaluate_SQL", null, first.getLocal()[0], first.getLocal()[1], false);
        evaluate_word_list=new ArrayList<>();
        listlocal_length=new HashMap<>();
    }

    public void Add_word(Word word)
    {
        this.evaluate_word_list.add(word);
        marknum=marknum++;
    }

    public void Add_close() {
        StringBuilder sb=new StringBuilder();
        for(Word t:evaluate_word_list)
            sb.append(t.toString());
        substance=sb.toString();
    }

    public void Add_words(List<Word> words) {
        this.evaluate_word_list=words;
        marknum=evaluate_word_list.size();
    }

    public int startEvaluate()throws deepCloneException
    {//开始计算之前首先返回需要调度的列对象，作为缓存
        for(int loop=0;loop<marknum;loop++) {
            Word w = evaluate_word_list.get(loop);
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
        for(int loop=0;loop<marknum;loop++){
            Word w = evaluate_word_list.get(loop);
            if(w.getClass().getSimpleName().equals("TablespaceTableList_name")){
                evaluate.pushElement(listlocal_length.get(loop).getindex_element(evalocal));
            }
            else{
                evaluate.pushElement(w);
            }
        }
        return evaluate.getthisEvaluate();
    }

    public Word getEvaluatePart(int index){
        return this.evaluate_word_list.get(index);
    }
    public int getSize(){
        return this.marknum;
    }
}
