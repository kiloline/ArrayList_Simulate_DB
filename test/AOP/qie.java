package AOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

public class qie {
    @Pointcut("execution(@AOP.DebugTrace * *..*.*(..))")
    public void DebugTraceMethod() {}

    @Before("DebugTraceMethod()")
    public void beforeDebugTraceMethod(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        System.out.println("beforeDebugTraceMethod: " + key);
    }
}
