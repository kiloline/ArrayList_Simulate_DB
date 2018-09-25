package Data.classes;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class KVListImpl<E> extends LinkedList<KVEntryImpl<String,String>> {
    List<String> oldlistname;

    public List<String> getoListname()
    {
        if(oldlistname==null){
            oldlistname=new LinkedList<>();
            for(int loop=0;loop<this.size();loop++)
            {
                oldlistname.add(this.get(loop).getKey());
            }
        }
        return oldlistname;
    }
}
