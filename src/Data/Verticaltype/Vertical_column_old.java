package Data.Verticaltype;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import m_Exception.OutofMemory_error;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rkppo
 * @param <E>
 */
public abstract class Vertical_column_old <E> implements Runnable, Comparator<E>
{
    Exception e;
    LinkedHashMap<Integer,E> elements;
    //Ϊ��ʵ��sort��������Ȼ��������Ԫ��֮������λ��˳��
    E[] element;  //�е��е�Ԫ�أ�ʹ�÷��ͽ��б��
    int[] index_lable; //��¼����Ԫ�صĲ���˳��
    String Vertical_name;  //�����͵��ַ���ʾ
    int Size,mem; //Size���Ѿ�ʹ�õĿռ䣬mem�Ƿ�����ڴ����
    
    public Vertical_column_old(String vertical_name)
    {
        Vertical_name=vertical_name;
        Size=0;
        mem=1;
    }
    public Vertical_column_old(E[] newelement,String newname)
    {
        elements=new LinkedHashMap<>();
        this.element=newelement;
        this.index_lable=new int[newelement.length];
        for(int loop=0;loop<newelement.length;loop++)
        {
            index_lable[loop]=loop+1;
            elements.put(loop,newelement[loop]);
        }
        this.Vertical_name=newname;
        this.Size=newelement.length;
        this.mem=newelement.length;
    }
    public void alterSize(int Size)
    {
        this.Size=Size;
    }
    public int getSize()
    {
        return Size;
    }
    public String getVertical_name()
    {
        return Vertical_name;
    }
    public int getMem()
    {
        return mem;
    }
    public E getindex_element(int index)
    {
        return this.element[index];
    }
    public E[] getindex_elements(Integer[] index)
    {
        E[] result=(E[]) new Object[index.length];
        for(int loop=0;loop<index.length;loop++)
            result[loop]=this.element[index[loop]];
        return result;
    }
    public E[] getAll()
    {
        //return this.element;
        return elements.values().toArray(element);
    }
    
    public boolean insert(E element)
    {
        boolean result=false;
        this.element[this.Size]=element;
        this.index_lable[this.Size]=this.Size+1;
        this.Size++;
        result=true;
        return result;
    }
    public boolean insertAll(E[] element)
    {
        boolean result=false;
        for(int loop=0;loop<element.length;loop++)
        {
            this.element[Size+loop]=element[loop];
            this.index_lable[Size+loop]=Size+loop+1;
        }
        this.Size=Size+element.length;
        result=true;
        return result;
    }
    public void delete(Integer[] line)
    {
        int loop,loopc;
        //HashSet<Integer> linum=new HashSet<>();
        LinkedList<E> toReInit=new LinkedList<>();
        //for(loop=0;loop<line.length;loop++)
            //linum.add(line[loop]);
        for(loop=0,loopc=0;loop<this.Size;loop++)
        {
            if(loopc<line.length)
                if(loop==line[loopc]){
                    loopc++;
                    continue;
            }
            toReInit.add(element[loop]);
            //this.element[loop]=this.element[loopc];
        }
        //for(;loop<Size;loop++)
        this.element=toReInit.toArray(element);
        this.Size=toReInit.size();
    }
    public void update(Integer[] line,E element)
    {
        for(int loop=0;loop<line.length;loop++)
        {
            this.element[line[loop]]=element;
        }
    }
    
    //�����Pick_Condition()ֻ��һ����������������ķ�����Ҫ��ȫ��д
    public abstract ArrayList<Integer> Pick_Condition(String conditionSymbol, E conditionValue) throws Exception;
    /*{
    //E temp;
    //int[] local=new int[this.Size];
    ArrayList<Integer> Return=new ArrayList();
    if(!conditionValue.getClass().getSimpleName().equals(element[0].getClass().getSimpleName()))
    ;//�������ͽ���ǿ��ת��
    else
    ;//temp=conditionValue;
    
    switch(conditionSymbol)
    {//����ν�ʷ���
    case "is":case "=":/*for() if(element[loop].equal(temp)) {local.add(loop);};//�ȼ�ν��ָ��ͬһ����
    case "is not":case "!=":/*for() if(!element[loop].equal(temp)) {local[in]=loop;}*/;
//    case ">=":/*for() if(!element[loop].hashCode()>=temp.hashCode()) {local[in]=loop;}*/;
//    case "<=":/*for() if(!element[loop].hashCode()<=temp.hashCode()) {local[in]=loop;}*/;
//    case ">":/*for() if(!element[loop].hashCode()>temp.hashCode()) {local[in]=loop;}*/;
//    case "<":/*for() if(!element[loop].hashCode()<temp.hashCode()) {local[in]=loop;}*/;
//}//    case ">=":/*for() if(!element[loop].hashCode()>=temp.hashCode()) {local[in]=loop;}*/;
//    case "<=":/*for() if(!element[loop].hashCode()<=temp.hashCode()) {local[in]=loop;}*/;
//    case ">":/*for() if(!element[loop].hashCode()>temp.hashCode()) {local[in]=loop;}*/;
//    case "<":/*for() if(!element[loop].hashCode()<temp.hashCode()) {local[in]=loop;}*/;
//}

//return Return;
//}*/
    public abstract Vertical_column_old<E> checkout(String newname,Integer[] p_c);
    /*    {
    Integer[] newelement=new Integer[p_c.length];
    for(int loop=0;loop<p_c.length;loop++)
    {
    newelement[loop]=this.element[p_c[loop]];
    }
    return new Vertical_column(newelement,newname);
    }*/
    
    public abstract void realloc(int newmem) throws Exception;
    
    private CountDownLatch mStartSignal;  
    private CountDownLatch mDoneSignal;  
    private int mThreadIndex;
    public void setRun(CountDownLatch startSignal,CountDownLatch doneSignal,int threadIndex)
    {
        this.mDoneSignal = doneSignal;  
        this.mStartSignal = startSignal;  
        this.mThreadIndex = threadIndex; 
    }
    @Override  
    public void run()  
    {  
        // TODO Auto-generated method stub   
        try  
        {  
            mStartSignal.await();// �������ȴ�mStartSignal����Ϊ0���к���Ĵ���   
                                    // ���еĹ����̶߳��ڵȴ�ͬһ������������   
            //doWork();// �������   
            //System.out.println("Thread " + mThreadIndex + " Done Now:"+ System.currentTimeMillis());  
            mDoneSignal.countDown();// ����Ժ������һ   
        }  
        catch (InterruptedException e)  
        {  
            // TODO Auto-generated catch block   
            e.printStackTrace();  
        }  
    }
    
    @Override
    public abstract int compare(E c1,E c2);
}
