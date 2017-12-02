package m_Exception;

/**
 *
 * @author rkppo
 */
public class CantTransacation_error extends Exception
{
    public CantTransacation_error()
    {
        super("该类型与目标类型不存在默认的隐式类型转换方法");
    }
}
