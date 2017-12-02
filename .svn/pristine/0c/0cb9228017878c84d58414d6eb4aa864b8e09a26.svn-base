/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service.Handling.LinkTable;

import Data.classes.Table;
import Data.Verticaltype.Vertical_column_old;
import Data.Vessel.TableMapping;
import java.util.LinkedList;
import m_Exception.OutofMemory_error;

/**
 *
 * @author rkppo
 */
public class unionLinkHandling 
{
    public static Table UnionLink(Table t1,Table t2) throws OutofMemory_error
    {
        t1.reSize(t1.getSize()+t2.getSize());
        t1.realloc();
        LinkedList<String> ll1=t1.getListOrder();
        LinkedList<String> ll2=t2.getListOrder();
        TableMapping<String,String,Vertical_column_old> tm1=t1.getTableMapping();
        TableMapping<String,String,Vertical_column_old> tm2=t2.getTableMapping();
        for(int loop=0;loop<ll1.size();loop++)
            tm1.getData(ll1.get(loop)).insertAll(tm2.getData(ll2.get(loop)).getAll());
        return t1;
    }
}
