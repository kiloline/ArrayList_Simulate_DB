package Service.Language_ExecutePlan_builder.ExecutePlan_Engine;

import Data.Vessel.Word;

import java.util.LinkedList;
import java.util.List;

public class Show {
    public static List<String> show(List<Word> toClassify)
    {
        List<String> s_vertical=new LinkedList<>();
        for(int loop=1;loop<toClassify.size();loop++)
            s_vertical.add(toClassify.get(loop).getName());
        return s_vertical;
//        EPP.setShow(s_vertical.toArray(new String[0]));
    }
}
