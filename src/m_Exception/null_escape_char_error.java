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
public class null_escape_char_error extends Exception
{
    public null_escape_char_error()
    {
        super("不存在这样的转义字符");
    }
}
