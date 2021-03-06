package Utils.System;

/**
 *
 * @author rkppo
 */
public class JVM_meminfo 
{
    public static String getMemery_use()
    {
        int loop=0;
        String bt="B";
        long total = Runtime.getRuntime().totalMemory(); // byte
        long m1 = Runtime.getRuntime().freeMemory();
        Double use= new Double(total - m1);
        while(use.intValue()>1024) {
            use = use / 1024;
            loop++;
        }
        switch(loop){
            case 1:
                bt="K"+bt;
                break;
            case 2:
                bt="M"+bt;
                break;
            case 3:
                bt="G"+bt;
                break;
            case 4:
                bt="T"+bt;
                break;
        }
        return "Memery Use: "+use.toString()+bt;
    }
}