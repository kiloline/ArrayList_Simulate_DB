package Data.classes;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author rkppo
 */
public class Language_node 
{
    private String option;  //单词名称
    private HashMap<String,Language_node> status_Map;  //
    private HashMap<String,Integer> next_node;  //下一单词列表
    private LinkedList<String> locking;

    public Language_node()
    {
        locking=new LinkedList<>();
        next_node=new HashMap();
    }

    public void add_status(HashMap Map, String... add)  //为节点添加单词
            //经过一次改造之后可以达到反复添加add单词列表的目的
    {
        for(int loop=0;loop<add.length;loop++)
            next_node.put(add[loop],next_node.size()+loop);
        status_Map=Map;
    }
    public Language_node to_status(String shift)
    {
        if(next_node.get(shift)!=null)
            return status_Map.get(shift);
        return null;
    }
    public void setoption(String option)
    {
        this.option=option;
    }
    public String getoption()
    {
        return this.option;
    }
    public void lock_node(String... shift)
    {
        for(String s:shift)
        {
            Integer i=next_node.remove(s);
            if(i!=null)
                locking.add(s);
        }
    }
    public void unlockAll()
    {
        for(int loop=next_node.size();!locking.isEmpty();loop++)
            next_node.put(locking.remove(), loop);
    }
}
