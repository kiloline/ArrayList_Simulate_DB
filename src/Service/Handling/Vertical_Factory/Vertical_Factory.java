/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service.Handling.Vertical_Factory;

import Data.Verticaltype.Vertical_Column;
import Service.Handling.trans.Transelementtype;
import m_Exception.NullTypePoint_error;
import m_Exception.runtime.insertException;
import m_Exception.type.Type_not_exist;

/**
 *
 * @author rkppo
 * 单例模式：静态内部类方式，可以延迟加载，只在建表或者加载表的时候使用
 */
public class Vertical_Factory {
    
    Transelementtype trans;
    arrayFt aft;
    linkFt lft;
    setFt sft;
    
    private static class Factory
    {
        private static final Vertical_Factory VF=new Vertical_Factory();
    }
    
    private Vertical_Factory()
    {
        this.trans = new Transelementtype();
        aft=new arrayFt(trans);
        sft=new setFt(trans);
    }
    public static Vertical_Column getVertical_column(String vertical_name,String vertical_type,String data_structure) throws Type_not_exist, NullTypePoint_error
    {
        switch(data_structure)
        {
            case "array":
                return Factory.VF.aft.getVertical(vertical_name,vertical_type);
            case "set":
                return Factory.VF.sft.getVertical(vertical_name,vertical_type);
//            case "list":
            default:
                throw new Type_not_exist();
        }
    }
    public static Vertical_Column setVertical_column(String[] vertical_nodes,String vertical_name,String vertical_type,boolean isIndex,String data_structure) throws NullTypePoint_error, Type_not_exist,insertException
    {   //目前只支持int，double和String三种类型
        //table_handling th=new table_handling(backstage,new check_StringtoNumber());
        switch(data_structure)
        {
            case "array":
                return Factory.VF.aft.setVertical(vertical_nodes,vertical_name,vertical_type,isIndex);
            case "set":
                return Factory.VF.sft.setVertical(vertical_nodes,vertical_name,vertical_type,isIndex);
//            case "list":
            default:
                throw new Type_not_exist();
        }
    }
}
