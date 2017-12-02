package Data.classes;

import Data.Verticaltype.Vertical_Node;

/**
 *
 * @author rkppo
 * 目前打算酱这个类作为处理join/笛卡儿积 合成表的容器
 * 针对join产生的连接，先将每个
 */
public class Cursor 
{
    long rownum;
    Vertical_Node[] line_data;
    String cursor_name;
    
    Table table;
    String[] Vertical_names;
    
    public Cursor(int element_num)
    {
        line_data=new Vertical_Node[element_num];
    }
    
    public String getCursor_name()
    {
        return "cursor";
    }
    
    public void Build_line(Object[] obja)
    {
        for(int loop=0;loop<line_data.length;loop++)
        {
            line_data[loop].updateelement(obja[loop]);
        }
    }
    
    
    
    public boolean fetch()
    {
        if(rownum==table.getSize())
            return false;
        else
        {
            
            rownum++;
            return true;
        }
    }
    
    public Object[] showConcequence()
    {
        return this.line_data;
    }
    
    public void close()
    {
        //this=null;
    }
}
