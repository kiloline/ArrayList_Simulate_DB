/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service_pkg.Handling.Vertical_Factory;

import Data.Verticaltype.Vertical_Array;
import Data.Verticaltype.Vertical_Column;
import Service_pkg.Handling.trans.Transelementtype;
import m_Exception.NullTypePoint_error;
import m_Exception.runtime.insertException;
import m_Exception.type.Type_not_exist;

/**
 *
 * @author rkppo
 */
public class arrayFt {
    Transelementtype trans;
    public arrayFt(Transelementtype trans)
    {
        this.trans=trans;
    }
    
    public Vertical_Column setVertical(String[] vertical_nodes,String vertical_name,String type,boolean isIndex) throws NullTypePoint_error, Type_not_exist, insertException
    {
        switch (type) 
        {
            case "int":case "Integer":
                return new Vertical_Array<Integer>(trans.typetransverter2Array(vertical_nodes,vertical_name),vertical_name,"Integer",isIndex);
            case "double":case "Double":
                return new Vertical_Array<Double>(trans.typetransverter2Array(vertical_nodes,vertical_name),vertical_name,"Double",isIndex);
            case "string":case "String":
                return new Vertical_Array<String>(trans.typetransverter2Array(vertical_nodes,vertical_name),vertical_name,"String",isIndex);
            default:
                throw new NullTypePoint_error();
        }
    }

    public Vertical_Column getVertical(String vertical_name,String vertical_type) throws NullTypePoint_error {
        switch (vertical_name) 
        {
            case "int":case "Integer":
                return new Vertical_Array<Integer>(vertical_name,"Integer");
            case "double":case "Double":
                return new Vertical_Array<Double>(vertical_name,"Double");
            case "string":case "String":
                return new Vertical_Array<String>(vertical_name,"String");
            default:
                throw new NullTypePoint_error();
        }
    }
    
}
