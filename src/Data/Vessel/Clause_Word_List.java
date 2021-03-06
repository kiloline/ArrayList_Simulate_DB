/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data.Vessel;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author rkppo
 */
public class Clause_Word_List extends Word{
    
    private LinkedList<Word> clause_word_list;
    public Clause_Word_List(int line,int list) {
        super("Clause_SQL", null,line,list,false);
        clause_word_list=new LinkedList<>();
    }
    
    public void Add_word(Word word)
    {
        this.clause_word_list.add(word);
    }
    
    public LinkedList<Word> getClause()
    {
        return this.clause_word_list;
    }
    
    public Word getLastWord(){
        return this.clause_word_list.getLast();
    }
}
