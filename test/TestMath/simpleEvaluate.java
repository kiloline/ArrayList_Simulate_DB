package TestMath;

import Data.Numeral_Calculations.Evaluate;
import Data.Vessel.Word;
import Service_pkg.Language_disposer.InitLanguage_node;
import Service_pkg.Language_disposer.Word_Segmentation_Machine;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;

public class simpleEvaluate {
    @Test
    public void math() throws Exception {
        String a="select 1+sqrt(30+20/2+20*3)+100*3/5+55;";
//        check_StringtoNumber csn =new check_StringtoNumber();
        InitLanguage_node allNodes=new InitLanguage_node();//初始化单词节点
        Word_Segmentation_Machine WSS=new Word_Segmentation_Machine(allNodes.setWord_Set());//分词器
        Evaluate eva=new Evaluate();

        LinkedList<Word> w = WSS.Segment(a);
        for(Word word:w.subList(1,w.size()-1))
        {
            eva.pushElement(word);
        }
        System.out.println(eva.getthisEvaluate());
    }
}
