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
    HashMap<String,Language_node> status_Map;  //
    private HashMap<String,Integer> next_node;  //下一单词列表
    LinkedList<String> locking;
    
    public void add_status(String[] add,HashMap Map)  //为节点添加单词
    {
        next_node=new HashMap();
        for(int loop=0;loop<add.length;loop++)
            next_node.put(add[loop], loop);
        status_Map=Map;
        locking=new LinkedList<>();
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
