package Service.Filereader;

import Data.classes.Table;
import Data.Verticaltype.Vertical_column_old;
import Service.Fileloader.Table_File;
import Service.Handling.table_handling;
import java.util.List;
import org.dom4j.DocumentException;
import org.dom4j.Element;

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
        Vertical_column_old[] vc=new Vertical_column_old[data_list.size()];
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
                String toNode=vertical_nodes.get(loopi).getText();
                if(toNode.equals(""))
                    toNode=null;
                nodedata[loopi]=toNode;
            }
            ln[loop]=list.attributeValue("name");
            lt[loop]=list.attributeValue("type");
            vc[loop]=th.setVertical_column(nodedata,list.attributeValue("type"));
        }
        return Table.setTable(tablename,ln, lt, vc);
    }
}
