/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m_Exception;

/**
 *
 * @author BFD-501
 */
public class NullTypePoint_error extends Exception
{
    public NullTypePoint_error()
    {
        super("输入了不支持的数据类型");
    }
}
