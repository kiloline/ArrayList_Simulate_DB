package Data.Verticaltype;

import java.util.ArrayList;
import java.util.LinkedList;


public class Vertical_Double extends Vertical_column_old<Double> 
{
    public Vertical_Double(String vertical_name)
    {
        super(vertical_name);
        element=new Double[1];
    }
    public Vertical_Double(Double[] newelement,String newname)
    {
        super(newelement,newname);
    }
    
    @Override
    public boolean insert(Double element)
    {
        boolean result=false;
        this.element[this.Size]=element;
        this.Size++;
        result=true;
        return result;
    }
    
    @Override
    public void update(Integer[] line,Double element)
    {
        for(int loop=0;loop<line.length;loop++)
        {
            this.element[line[loop]]=new Double(element);
        }
    }
    
    @Override
    public ArrayList<Integer> Pick_Condition(String conditionSymbol, Double conditionValue) throws Exception
    {
        //Double temp = Double.valueOf(0);
        ArrayList<Integer> Return=new ArrayList();
/*        if(!conditionValue.getClass().getSimpleName().equals(element[0].getClass().getSimpleName()))
        {//根据类型进行强制转换，或者不能转换的类型抛出错误
            if(conditionValue.getClass().getSimpleName().equals("String"))
            {
                if(check_StringtoNumber.check_StringtoInteger((String) conditionValue))
                    temp=Double.valueOf((String)conditionValue);
                else
                    throw new CantTransacation_error();
            }
            else if(conditionValue.getClass().getSimpleName().equals("Integer"))
                temp=((Integer)conditionValue).doubleValue();
        }
        else 
            temp=(Double)conditionValue;//temp=conditionValue;*/
        
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
                    else if(!(element[loop]>=conditionValue)) 
                        Return.add(loop);
                break;
            case "<=":
                for(int loop=0;loop<this.Size;loop++) 
                    if(element[loop]==null);
                    else if(!(element[loop]<=conditionValue)) 
                        Return.add(loop);
                break;
            case ">":
                for(int loop=0;loop<this.Size;loop++) 
                    if(element[loop]==null);
                    else if(!(element[loop]>conditionValue)) 
                        Return.add(loop);
                break;
            case "<":
                for(int loop=0;loop<this.Size;loop++) 
                    if(element[loop]==null);
                    else if(!(element[loop]<conditionValue)) 
                    {Return.add(loop);}
        }
        
        return Return;
    }
    @Override
    public Vertical_Double checkout(String newname, Integer[] p_c)
    {
        Double[] newelement=new Double[p_c.length];
        for(int loop=0;loop<p_c.length;loop++)
        {
            newelement[loop]=this.element[p_c[loop]];
        }
        return new Vertical_Double(newelement,newname);
    }

    @Override
    public void realloc(int newmem) throws Exception 
    {
        int loopmax;
        if(this.Size>newmem)
            loopmax=newmem;
        else
            loopmax=this.Size;
        Double[] temp=this.element;
        int[] tempindex=this.index_lable;
        this.element=new Double[newmem];
        this.index_lable=new int[newmem];
        for(int loop=0;loop<loopmax;loop++)
        {
            this.element[loop]=temp[loop];
            this.index_lable[loop]=tempindex[loop];
        }
        this.mem=newmem;
    }

    @Override
    public int compare(Double c1, Double c2) {
        if(c1>=c2)
            return 1;
        else return -1;
    }
}
