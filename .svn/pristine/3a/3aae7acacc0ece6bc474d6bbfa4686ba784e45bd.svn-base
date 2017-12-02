package speedtest;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
//list之间相互转换会将插入顺序一并继承下来，不需要担心出现数据顺序的问题
public class link2array {
    @Test
    public void lc()
    {
        LinkedList<Integer> link=new LinkedList<>();
        ArrayList<Integer> array;
        for(int loop=0;loop<100;loop++)
        {
            link.add(loop);
        }

        array=new ArrayList<>(link);

        for(int loop=0;loop<100;loop++)
        {
            if(array.get(loop)!=loop)
                System.out.println(loop);
        }
    }
}
