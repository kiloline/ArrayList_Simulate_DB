/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service.Handling.Vertical_Factory;

import Data.Verticaltype.Vertical_Array;
import Data.Verticaltype.Vertical_Column;
import Data.Verticaltype.Vertical_Set;
import Service.Handling.trans.Transelementtype;
import m_Exception.NullTypePoint_error;
import m_Exception.runtime.insertException;
import m_Exception.type.Type_not_exist;

/**
 *
 * @author rkppo
 */
public class setFt {
    Transelementtype trans;
    public setFt(Transelementtype trans)
    {
        this.trans=trans;
    }

    public Vertical_Column setVertical(String[] vertical_nodes, String vertical_name, String type, boolean isIndex) throws NullTypePoint_error, Type_not_exist, insertException
    {
        switch (type)
        {
            case "int":case "Integer":
            return new Vertical_Set(trans.typetransverter2Array(vertical_nodes,vertical_name),vertical_name,"Integer",isIndex);
            case "double":case "Double":
            return new Vertical_Set<Double>(trans.typetransverter2Array(vertical_nodes,vertical_name),vertical_name,"Double",isIndex);
            case "string":case "String":
            return new Vertical_Set<String>(trans.typetransverter2Array(vertical_nodes,vertical_name),vertical_name,"String",isIndex);
            default:
                throw new NullTypePoint_error();
        }
    }

    public Vertical_Column getVertical(String vertical_name,String vertical_type) throws NullTypePoint_error {
        switch (vertical_name)
        {
            case "int":case "Integer":
            return new Vertical_Set<Integer>(vertical_name,"Integer");
            case "double":case "Double":
            return new Vertical_Set<Double>(vertical_name,"Double");
            case "string":case "String":
            return new Vertical_Set<String>(vertical_name,"String");
            default:
                throw new NullTypePoint_error();
        }
    }
}
