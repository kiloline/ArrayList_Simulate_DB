package Service.Filereader;

import Data.Verticaltype.Vertical_Column;
import Data.classes.Table;
import Service.Fileloader.Table_File;
import Service.Handling.table_handling;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.List;

/**
 *
 * @author rkppo
 */
public class table_reader extends Xml_Reader 
{
    public table_reader(Table_File dbfile,boolean check_file) throws DocumentException
    {
        super();
        setFile(dbfile.getFile());
    }
    public Table getTable(table_handling th) throws Exception
    {
        List<Element> data_list=super.getElements("list");
        Vertical_Column[] vc=new Vertical_Column[data_list.size()];
        String tablename=super.getRoot().attributeValue("tbname");
        String[] ln=new String[data_list.size()];
        String[] lt=new String[data_list.size()];
        for(int loop=0;loop<data_list.size();loop++)
        {
            Element list=data_list.get(loop);
            List<Element> vertical_nodes=list.elements("node");
            String[] nodedata=new String[vertical_nodes.size()];
            for(int loopi=0;loopi<vertical_nodes.size();loopi++)
            {
                String toNode;
                if(vertical_nodes.get(loopi).attributeValue("isNull").equals("true"))
                    toNode=null;
                else
                    toNode=vertical_nodes.get(loopi).getText();
                nodedata[loopi]=toNode;
            }
            ln[loop]=list.attributeValue("name");
            lt[loop]=list.attributeValue("type");
            vc[loop]=th.setVertical_column(nodedata,ln[loop],list.attributeValue("type"),
                    new Boolean(list.attributeValue("isIndex")),
                    list.attributeValue("data_structure"));
        }
        return Table.setTable(tablename,ln, lt, vc);
    }
}
