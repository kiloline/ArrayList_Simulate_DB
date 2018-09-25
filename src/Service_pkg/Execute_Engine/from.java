package Service_pkg.Execute_Engine;

import Data.classes.Table;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class from {
    private List<Table> FromTables;
    private Set<String> FromTablesname;
    public void setTables(List<Table> fromtables)
    {
        FromTables=fromtables;
        FromTablesname=new HashSet<>();
        for(Table t:FromTables)
            FromTablesname.add(t.toString());
    }
    //û�б�ʶ��������������������
    public void setLists(List<String> unsignedlists)
    {

    }
    //��ʶ����������������

}
