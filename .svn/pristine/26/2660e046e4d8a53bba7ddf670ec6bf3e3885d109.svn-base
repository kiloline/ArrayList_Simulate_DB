package AOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import sun.rmi.runtime.Log;

public class MainActivity {
    public static void main(String[] ar)
    {
        new MainActivity().logTest();
    }
    @DebugTrace
    public void logTest() {
        System.out.println(Integer.MAX_VALUE);
    }

    @Pointcut("execution(@AOP.DebugTrace * *..*.*(..))")
    public void DebugTraceMethod() {}

    @Before("DebugTraceMethod()")
    public void beforeDebugTraceMethod(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        System.out.println("beforeDebugTraceMethod: " + key);
    }
}

