package speedtest;

import Annotation.TimeUtil;
import Annotation.Timer;

import java.util.*;

public class b {
    static int num=0;
    static boolean[] isPrime;

    public static void main(String[] ar)
    {
        TimeUtil tu = new TimeUtil();
        num=20000000;
        tu.getTime();
    }

    @Timer
    public static void suvec()
    {
        Vector<Integer> su=new Vector<>();
        su.add(2);
        int i;
        for(i=3;i<num;i=i+2)
        {
            int loop=0;
            while(loop<su.size())
            {
                if(su.get(loop)*su.get(loop)>i) {
                    loop=su.size();
                    break;
                }
                else if(i%su.get(loop)==0)
                    break;
                loop=loop+1;
            }

            if(loop==su.size())
            {
                su.add(i);
            }
        }
        //su.trimToSize();
        System.out.println(su.size());
    }

    @Timer
    public static void suall()
    {
        ArrayList<Integer> su=new ArrayList<>();
        su.add(2);
        Iterator<Integer> it=su.iterator();
        int i;
        for(i=3;i<num;i=i+2)
        {
            boolean bis=false;
            while(it.hasNext())
            {
                int iti=it.next();
                if(iti*iti>i) {
                    bis=true;
                    break;
                }
                else if(i%iti==0)
                    break;
            }

            if(bis)
            {
                su.add(i);
                it=su.iterator();
            }
        }
        //su.trimToSize();
        System.out.println(su.size());
    }

    @Timer
    public static void sull()
    {
        LinkedList<Integer> su=new LinkedList<>();
        su.add(2);
        Iterator<Integer> it=su.iterator();
        int i;
        for(i=3;i<num;i=i+2)
        {
            boolean bis=false;
            while(it.hasNext())
            {
                int iti=it.next();
                if(iti*iti>i) {
                    bis=true;
                    break;
                }
                else if(i%iti==0)
                    break;
            }

            if(bis)
            {
                su.add(i);
                it=su.iterator();
            }
        }
        System.out.println(su.size());
    }

    @Timer
    public static Integer[] suar()
    {
        int sulength=num,inssize=1;
        Integer[] su=new Integer[sulength/15];
        su[0]=2;
        int i;
        for(i=3;i<sulength;i=i+2)
        {
            int loop=0;
            while(loop<inssize)
            {
                if(su[loop]*su[loop]>i) {
                    loop=inssize;
                    break;
                }
                else if(i%su[loop]==0)
                    break;
                loop=loop+1;
            }

            if(loop==inssize)
            {
                //System.out.println(i);
                su[inssize]=i;
                inssize++;
            }
        }
        System.out.println(inssize);
        return su;
    }

    @Timer
    public static boolean[] Eratosthenes()
    {
        int inssize=1;
        boolean[] isPrime=new boolean[num+1];//默认赋值全是false

        for(int i=2;i<num;i++)
        {
            if(isPrime[i])
                continue;
            else
            {
                int shai=i;
                int cheng=2;
                while(shai*cheng<num)
                {
                    isPrime[shai*cheng]=true;
                    cheng++;
                }
            }
        }
        return isPrime;
    }

    @Timer
    public static boolean[] EratosthenesGai()
    {
        boolean[] isPrime=new boolean[num+1];//默认赋值全是false

        int cheng=2;
        while(2*cheng<num)
        {
            isPrime[2*cheng]=true;
            cheng++;
        }

        for(int i=3;i<num;i=i+2)
        {
            if(isPrime[i])
                continue;
            else
            {
                int shai=i;
                cheng=2;
                while(shai*cheng<num)//这个地方和未来统计质数个数的循环终止条件必须一致，
                // 这里事实上并没有验证num这个数
                {
                    isPrime[shai*cheng]=true;
                    cheng++;
                }
            }
        }
        return isPrime;
    }
}
