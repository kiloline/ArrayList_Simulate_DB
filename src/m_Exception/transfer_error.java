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
public class transfer_error extends Exception
{
    public transfer_error()
    {
        super("非法调用，传入值超出定义范围。");
    }
}
