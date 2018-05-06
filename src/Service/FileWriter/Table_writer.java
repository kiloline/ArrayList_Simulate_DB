package Service.FileWriter;

import Data.Verticaltype.Vertical_Column;
import Data.Vessel.TableMapping;
import Data.classes.Table;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.File;

/**
 * @author rkppo
 */
public class Table_writer extends Xml_Writer
{
    public Table_writer(String rootPath)
    {
        super(rootPath);
    }
    
    public void save_table(Table table) throws Exception
    {
        String filepath=rootpath+"\\"+table.toString()+".dba.xml";
        File file=new File(filepath);
        
        TableMapping<String,String, Vertical_Column> data=table.getTableMapping();
        String[] listOrder=table.getListOrder().toArray(new String[0]);
        
        root.addAttribute("tbname", table.toString());
        //for(int loop=listOrder.length-1;loop>=0;loop--)//这里是为了把table里面list的顺序调整到和插入是一致，
            //如果用自增的方法会导致load时的顺序和insert时正好相反
        for(int loop=0;loop<listOrder.length;loop++)
        {
            String data_structure=data.getData(listOrder[loop]).getClass().getSimpleName();
            Element temp=DocumentHelper.createElement("list");
            temp.addAttribute("name", listOrder[loop]);
            temp.addAttribute("type", data.getAttribute(listOrder[loop]));
            temp.addAttribute("isIndex", data.getData(listOrder[loop]).isIndex().toString());
            temp.addAttribute("length", new Integer(table.getSize()).toString());
            switch (data_structure)
            {
                case "Vertical_Set":
                    temp.addAttribute("data_structure","set");
                    break;
                case "Vertical_Array":
                default:
                    temp.addAttribute("data_structure","array");
                    break;
            }
            
            for(int loopi=0;loopi<table.getSize();loopi++)
            {
                Element tempi=DocumentHelper.createElement("node");
                if(data.getData(listOrder[loop]).getindex_element(loopi)==null)
                    //tempi.addText("\u0000");
                    tempi.addAttribute("isNull","true");
//                    tempi.addText("");//由于考虑各字符集的通用性，null暂时定为和空字符串等价
                else {
                    tempi.addAttribute("isNull","false");
                    tempi.addText(data.getData(listOrder[loop]).getindex_element(loopi).toString());
                }
                temp.add(tempi);
            }
            root.add(temp);
        }
        super.save(file);
    }
}
