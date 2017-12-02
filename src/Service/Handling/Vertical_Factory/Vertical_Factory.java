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
 * ����ģʽ����̬�ڲ��෽ʽ�������ӳټ��أ�ֻ�ڽ�����߼��ر��ʱ��ʹ��
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
    }
    public static Vertical_Column getVertical_column(String vertical_name,String vertical_type,String data_structure) throws Type_not_exist, NullTypePoint_error
    {
        switch(data_structure)
        {
            case "array":
                return Factory.VF.aft.getVertical(vertical_name,vertical_type);
            case "set":
            case "list":
            default:
                throw new Type_not_exist();
        }
    }
    public static Vertical_Column setVertical_column(String[] vertical_nodes,String vertical_name,String vertical_type,boolean isIndex,String data_structure) throws NullTypePoint_error, Type_not_exist,insertException
    {   //Ŀǰֻ֧��int��double��String��������
        //table_handling th=new table_handling(backstage,new check_StringtoNumber());
        switch(data_structure)
        {
            case "array":
                return Factory.VF.aft.setVertical(vertical_nodes,vertical_name,vertical_type,isIndex);
            case "set":
            case "list":
            default:
                throw new Type_not_exist();
        }
    }
}
