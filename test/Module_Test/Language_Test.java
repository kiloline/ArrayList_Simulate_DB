package Module_Test;

import Data.Vessel.ExecutePlan_Package;
import Data.Vessel.Word;
import Service_pkg.Language_ExecutePlan_builder.ExecutePlan_builder;
import Service_pkg.Language_disposer.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class Language_Test {
    String[] SQL = {
            "select * from first where id =3+5;",
            "update first set id=1.2*3;",
            "alter table first add column im int;",
            "select * from first\n" +
                    "union\n" +
                    "select id pid,name pname,time from first\n" +
                    "where id=3\n" +
                    "or name='kppom';",
            "select tra,rbg,dsf,dsa \n" +
                    "from fisrt,(select *from second) second \n" +
                    "where fisrt.name is null;"};

    @Test
    public void word_and_shift() {
        for (int loopo = 0; loopo < SQL.length; loopo++) {
            LinkedList<Word> words = null;
            try {
                HashSet<String> test = new InitLanguage_node().setWord_Set();
                words = new Word_Segmentation_Machine(test).Segment(SQL[loopo]);
                words = new Word_Grammar_unit(test, InitGrammer_unit.getGrammar_unit()).
                        set_Word_Grammar_unit(words);
                new LanguageNode_shifter(new InitGrammer_unit().setWord_Type_Map()).shifter(words);
                for (int loop = 0; loop < words.size(); loop++) {
                    System.out.print(words.get(loop).getType());
                    System.out.print(' ');
                    System.out.println(words.get(loop).getSubstance());
                }
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
                //exit(1);
            }
        }
    }

    @Test
    public void ExecutePlan() throws Exception
    {
        for (int loopo = 0; loopo < SQL.length; loopo++) {
            InitLanguage_node allNodes = new InitLanguage_node();
            ArrayList<String> clause_stop = allNodes.setclause_stop();
            Word_Segmentation_Machine WSS = new Word_Segmentation_Machine(allNodes.setWord_Set());

            LinkedList<Word> word_list = WSS.Segment(SQL[loopo]);
            word_list = new Word_Grammar_unit(allNodes.setWord_Set(), InitGrammer_unit.getGrammar_unit()).
                    set_Word_Grammar_unit(word_list);
            ExecutePlan_builder EPB = new ExecutePlan_builder(null, clause_stop);

            ExecutePlan_Package make_ExecutePlan = EPB.make_ExecutePlan(word_list);
            System.out.println();
        }
    }
}
