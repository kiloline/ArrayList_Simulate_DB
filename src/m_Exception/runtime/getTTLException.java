package m_Exception.runtime;

public class getTTLException extends Exception {
    public getTTLException()
    {
        super("调用表达式右部结果之前必须先进行右部赋值。");
    }
}
