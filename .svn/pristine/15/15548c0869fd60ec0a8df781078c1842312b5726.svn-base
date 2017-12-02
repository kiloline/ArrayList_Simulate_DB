import java.util.LinkedList;

public class linkkedlist_addfirstorlast 
{
    public static void main(String[] ar)
    {
        LinkedList<String> l;
        l=new LinkedList<>();
        l.addFirst("1");
        l.addFirst("2");
        l.addFirst("3");
        String[] t=l.toArray(new String[0]);
        for(int loop=0;loop<t.length;loop++)
            System.out.println(t[loop]);
        
        l=new LinkedList<>();
        l.addLast("1");
        l.addLast("2");
        l.addLast("3");
        t=l.toArray(new String[0]);
        for(int loop=0;loop<t.length;loop++)
            System.out.println(t[loop]);
    }//可见只有在addLast的情况下，List转成的数组才能按照添加时的顺序按照0123……排列
}
