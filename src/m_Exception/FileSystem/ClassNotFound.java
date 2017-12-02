package m_Exception.FileSystem;

/**
 *
 * @author rkppo
 */
public class ClassNotFound extends Exception
{
    public ClassNotFound(String classname)
    {
        super("不存在的对象："+classname);
    }
}
