
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
public class teststringarrayequals {
    public static void main(String[] ar)
    {
        LinkedList<String> a,b;
        a=new LinkedList<>();
        b=new LinkedList<>();
        a.add("123");
        a.add("234");
        a.add("345");
        a.add("asd");
        b.add("123");
        b.add("234");
        b.add("345");
        b.add("asd");
        if(a.toArray(new String[0]).equals(b.toArray(new String[0])))
            System.out.println("equals");
        //String[]的equals方法并不能完整比较两个数组间的相似性
    }
}
