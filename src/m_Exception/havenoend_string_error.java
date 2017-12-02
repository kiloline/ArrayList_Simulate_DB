package m_Exception;

/**
 *
 * @author rkppo
 */
public class havenoend_string_error extends Exception
{
    public havenoend_string_error()
    {
        super("没有终结符号的字符串");
    }
}
