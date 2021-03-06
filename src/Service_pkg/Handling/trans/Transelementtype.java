/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service_pkg.Handling.trans;

import Utils.Check.check_StringtoNumber;
import m_Exception.type.Type_not_exist;

import java.util.LinkedList;

/**
 *
 * @author rkppo
 */
public class Transelementtype {
    static check_StringtoNumber CSN=new check_StringtoNumber();
    public static Object typetransverter(String element, String type) throws Type_not_exist {
        if (element == null) {
            ;
        } else if (type.equals("Integer") && CSN.check_StringtoInteger(element)) {
            return (Object) Integer.valueOf(element);
        } else if (type.equals("Double") && CSN.check_StringtoDouble(element)) {
            return (Object) Double.valueOf(element);
        } else if (type.equals("String")) {
            return element;
        }
        return null;
    }

    public static <E> LinkedList<E> typetransverter2Array(String[] element, String type) throws Type_not_exist //,E tab
    {
        LinkedList o;
        if (type.equals("Integer")) {
            //if(element.length!=0)
            o =  new LinkedList<Integer>();
        } else if (type.equals("Double")) {
            //if(element.length!=0)
            o =  new LinkedList<Double>();
        } else if (type.equals("String")) {
            o  = new LinkedList<String>();
        } else {
            o  = new LinkedList<Object>();
        }
        for (int loop = 0; loop < element.length; loop++) {
            o.add(typetransverter(element[loop], type));
        }
        return o;
    }
    
}
