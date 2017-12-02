package Service.FileWriter;

import Data.classes.Table;
import Data.Verticaltype.Vertical_column_old;
import Data.Vessel.TableMapping;
import java.io.File;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

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
        
        TableMapping<String,String, Vertical_column_old> data=table.getTableMapping();
        String[] listOrder=table.getListOrder().toArray(new String[0]);
        
        root.addAttribute("tbname", table.toString());
        //for(int loop=listOrder.length-1;loop>=0;loop--)//������Ϊ�˰�table����list��˳��������Ͳ�����һ�£�
            //����������ķ����ᵼ��loadʱ��˳���insertʱ�����෴
        for(int loop=0;loop<listOrder.length;loop++)
        {
            Element temp=DocumentHelper.createElement("list");
            temp.addAttribute("name", listOrder[loop]);
            temp.addAttribute("type", data.getAttribute(listOrder[loop]));
            temp.addAttribute("length", new Integer(table.getSize()).toString());
            
            for(int loopi=0;loopi<table.getSize();loopi++)
            {
                Element tempi=DocumentHelper.createElement("node");
                if(data.getData(listOrder[loop]).getindex_element(loopi)==null)
                    //tempi.addText("\u0000");
                    tempi.addText("");//���ڿ��Ǹ��ַ�����ͨ���ԣ�null��ʱ��Ϊ�Ϳ��ַ����ȼ�
                else
                    tempi.addText(data.getData(listOrder[loop]).getindex_element(loopi).toString());
                temp.add(tempi);
            }
            root.add(temp);
        }
        super.save(file);
    }
}
