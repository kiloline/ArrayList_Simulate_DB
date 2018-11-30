package m_Exception;

/**
 *
 * @author rkppo
 */
public class havenoend_string_error extends Language_error
{
    public havenoend_string_error(int line,int list)
    {
        super(line,list,"没有终结符号的字符串");
    }
}
