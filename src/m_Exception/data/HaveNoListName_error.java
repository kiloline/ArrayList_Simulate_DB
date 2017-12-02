package m_Exception.data;

/**
 *
 * @author rkppo
 */
public class HaveNoListName_error extends Exception
{
    public HaveNoListName_error(String name)
    {
        super("指定的列名"+name+"不存在");
    }
}
