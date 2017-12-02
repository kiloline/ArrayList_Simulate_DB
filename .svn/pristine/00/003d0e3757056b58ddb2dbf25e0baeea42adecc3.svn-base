/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data.Vessel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author BFD-501
 */
class Table_List
{
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Table_List other = (Table_List) obj;
        if (!Objects.equals(this.Tablename, other.Tablename)) {
            return false;
        }
        if (!Objects.equals(this.Listname, other.Listname)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        //int hash = 7;
        //hash = 17 * hash + Objects.hashCode(this.Tablename);
        //hash = 17 * hash + Objects.hashCode(this.Listname);
        return 0;
    }
    String Tablename;
    String Listname;
    public Table_List(String t,String l)
    {
        this.Tablename=t;
        this.Listname=l;
    }
}
public class ListOrder {
    int size;
    LinkedList<String> order;
    HashSet<Table_List> singal;
    //HashMap<String,String> Tablefrom;
    LinkedList<String> Tablefrom;
    public ListOrder()
    {
        size=0;
        order=new LinkedList<>();
        singal=new LinkedHashSet<>();
        Tablefrom=new LinkedList<>();
    }
    public void addList(String listname,String Tablename) throws Exception
    {
        singal.add(new Table_List(Tablename,listname));
        if(singal.size()==size)
            throw new Exception();
        else size=singal.size();
    }
    public Iterator getOrder()
    {
        return singal.iterator();
    }
}
