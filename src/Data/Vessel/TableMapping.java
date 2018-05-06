/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data.Vessel;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author rkppo
 */
public class TableMapping<key,attribute,data> implements Serializable
{
    HashMap<key,attribute> k_a;
    HashMap<key,data> k_d;
    public TableMapping()
    {
        k_a=new HashMap<>();
        k_d=new HashMap<>();
    }
    public TableMapping(int initnumber,float loadFactor)
    {
        k_a=new HashMap<>(initnumber,loadFactor);
        k_d=new HashMap<>(initnumber,loadFactor);
    }
    
    public void put(key Key, attribute Attributes, data Data)
    {
        k_a.put(Key, Attributes);
        k_d.put(Key, Data);
    }
    public void putAll(Map<? extends key,? extends attribute> ka,Map<? extends key,? extends data> kd)
    {
        k_a.putAll(ka);
        k_d.putAll(kd);
    }
    public void remove(key Key)
    {
        k_a.remove(Key);
        k_d.remove(Key);
    }
    public TableMapping clone()
    {
        TableMapping tm=new TableMapping();
        tm.putAll((Map)k_a.clone(), (Map) k_d.clone());
        return tm;
    }
    
    
    public attribute getAttribute(key Key)
    {
        return k_a.get(Key);
    }
    public attribute[] getAttributes(key[] Keys)
    {
        LinkedList<String> result=new LinkedList<>();
        for(int loop=0;loop<Keys.length;loop++)
            result.add(k_a.get(Keys[loop]).toString());
        return result.toArray((attribute[])new Object[0]);
    }
    public data getData(key Key)
    {
        return k_d.get(Key);
    }
    public int size()
    {
        return k_a.size();
    }
    public Iterator<data> DataIterator()
    {
        return k_d.values().iterator();
    }
    public Set<key> keySet()
    {
        return k_a.keySet();
    }
}
