
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rkppo
 */
public class listorder {
    public static void main(String[] ar)
    {
        LinkedList<String> ll=new LinkedList<>();
        ll.add("1");
        ll.add("2");
        for(int i=0;i<ll.toArray(ar).length;i++)
            System.out.println(ll.toArray(ar)[i]);
    }
}
