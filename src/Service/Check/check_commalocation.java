package Service.Check;

import m_Exception.Language_error;

/**
 *
 * @author BFD-501
 */
public class check_commalocation 
{
    public static void check_commalocation(char up,char down) throws Language_error
    {
        if(up=='('||down==')')
            throw new Language_error("����һ���Ƿ���','");
        if(down==',')
            throw new Language_error("','������������");
    }
}
