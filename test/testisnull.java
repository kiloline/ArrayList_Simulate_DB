/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rkppo
 */
public class testisnull {
    public static void main(String[] ar)
    {
        String s="a";
        t(s);
        System.out.println(s);
//        Double a=null,b=null;
//        if(a==b)
//            System.out.println("==");
//        a=1.0;b=1.3;
//        if(a<b)
//            System.out.println("==");
//        if(a!=null)
//            System.out.println("!=");
    }

    public static void t(String s)
    {
        s=new String("s");
    }
}
