package AOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Date;

public aspect before {
    @Pointcut("execution(@AOP.printtime * *.*.*(..))")
    public void Point()
    {

    }
    @Before("Point()")

    public void write(JoinPoint joinPoint) throws Throwable
    {
        System.out.println(new Date().getTime());
    }

// Advice
// execution(* com.mybry.aop.service.*.*(..)执行 任意返回值 改包下的任意类的任意方法形参不限
    before():execution(* AOP.example.*.*(..)){
// 对原来方法进行修改、增强。
        System.out.println("----------模拟执行权限检查----------");
    }
}
