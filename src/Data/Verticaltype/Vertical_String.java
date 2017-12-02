package Data.Verticaltype;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author rkppo
 */
public class Vertical_String extends Vertical_column_old<String> 
{
    public Vertical_String(String vertical_name)
    {
        super(vertical_name);
        element=new String[this.mem];
    }
    public Vertical_String(String[] newelement,String newname)
    {
        super(newelement,newname);
    }
    
    @Override
    public boolean insert(String element)
    {
        boolean result=false;
        this.element[this.Size]=element;
        this.Size++;
        result=true;
        return result;
    }
    
    @Override
    public void update(Integer[] line,String element)
    {
        for(int loop=0;loop<line.length;loop++)
        {
            this.element[line[loop]]=new String(element);
        }
    }
    
    @Override
    public  ArrayList<Integer> Pick_Condition(String conditionSymbol, String conditionValue)
    {
        //String temp=new String();
        ArrayList<Integer> Return=new ArrayList<>();
/*        if(!conditionValue.getClass().getSimpleName().equals(element[0].getClass().getSimpleName()))
        {//根据类型进行强制转换，或者不能转换的类型抛出错误
            if(conditionValue.getClass().getSimpleName().equals("Integer"))
                temp=((Integer)conditionValue).toString();
            else if(conditionValue.getClass().getSimpleName().equals("Double"))
                temp=((Double)conditionValue).toString();
        }
        else 
            temp=(String)conditionValue;//temp=conditionValue;*/
        
        switch(conditionSymbol)
        {
            case "is":case "=":
                for(int loop=0;loop<this.Size;loop++) 
                    if(conditionValue==null)
                    {
                        if(element[loop]==null)
                            Return.add(loop);
                    }
                    else if(element[loop]==null);
                    else if(element[loop].equals(conditionValue)) 
                        Return.add(loop);
                break;
            case "isnot":case "!=":
                for(int loop=0;loop<this.Size;loop++) 
                    if(conditionValue==null)
                    {
                        if(element[loop]!=null)
                            Return.add(loop);
                    }
                    else if(element[loop]==null);
                    else if(!element[loop].equals(conditionValue)) 
                        Return.add(loop);
                break;
            case ">=":
                for(int loop=0;loop<this.Size;loop++)  
                    if(element[loop]==null);
                    else if(!(element[loop].hashCode()>=conditionValue.hashCode())) 
                        Return.add(loop);
                break;
            case "<=":
                for(int loop=0;loop<this.Size;loop++) 
                    if(element[loop]==null);
                    else if(!(element[loop].hashCode()<=conditionValue.hashCode())) 
                        Return.add(loop);
                break;
            case ">":
                for(int loop=0;loop<this.Size;loop++) 
                    if(element[loop]==null);
                    else if(!(element[loop].hashCode()>conditionValue.hashCode())) 
                        Return.add(loop);
                break;
            case "<":
                for(int loop=0;loop<this.Size;loop++) 
                    if(element[loop]==null);
                    else if(!(element[loop].hashCode()<conditionValue.hashCode())) 
                        Return.add(loop);
                break;
        }
        
        return Return;
    }
    @Override
    public Vertical_String checkout(String newname, Integer[] p_c)
    {
        String[] newelement=new String[p_c.length];
        for(int loop=0;loop<p_c.length;loop++)
        {
            newelement[loop]=this.element[p_c[loop]];
        }
        return new Vertical_String(newelement,newname);
    }

    @Override
    public void realloc(int newmem) throws Exception 
    {
        int loopmax;
        if(this.Size>newmem)
            loopmax=newmem;
        else
            loopmax=this.Size;
        String[] temp=this.element;
        int[] tempindex=this.index_lable;
        this.element=new String[newmem];
        this.index_lable=new int[newmem];
        for(int loop=0;loop<loopmax;loop++)
        {
            this.element[loop]=temp[loop];
            this.index_lable[loop]=tempindex[loop];
        }
        this.mem=newmem;
    }

    @Override
    public int compare(String c1, String c2) {
        if(c1.hashCode()>=c2.hashCode())
            return 1;
        else return -1;
    }
}
