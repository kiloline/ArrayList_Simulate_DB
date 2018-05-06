package Data.Vessel;

import Data.classes.KVEntryImpl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Evaluate_Word_List extends Word {
    List<Word> evaluate_word_list;
    Iterator<Word> evaluate_Iterator;
    /**
     * 记录所有计算中的数据表列名引用记录
     */
    LinkedList<KVEntryImpl<Integer,Integer>> listlocal_length;
    public Evaluate_Word_List(Word first) {
        super("Evaluate_SQL", null, first.getLocal()[0], first.getLocal()[1], false);
        evaluate_word_list=new LinkedList<>();
        listlocal_length=new LinkedList<>();
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

        evaluate_Iterator=evaluate_word_list.listIterator();
        evaluate_word_list=null;
    }

    public boolean hasnext(){
        return this.evaluate_Iterator.hasNext();
    }
    public Word getNext()
    {
        return this.evaluate_Iterator.next();
    }

    public void Add_words(List<Word> words) {
        this.evaluate_word_list=words;
    }


}
