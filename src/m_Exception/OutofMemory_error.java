/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m_Exception;

/**
 *
 * @author rkppo
 */
public class OutofMemory_error extends Exception
{
    public OutofMemory_error()
    {
        super("超出了Java允许的数组最大长度");
    }
}
