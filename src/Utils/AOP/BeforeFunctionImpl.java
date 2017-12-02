package Utils.AOP;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BeforeFunctionImpl {
    public static void before() {
        // ��ȡ��ǰ��������
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        System.out.println("current className(expected): " + className);
        try {
            Class c = Class.forName(className);
            Object obj = c.newInstance();
            Method[] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                // �жϸ÷����Ƿ����BeforeFunctionע��
                if (m.isAnnotationPresent(BeforeFunction.class)) {
                    m.setAccessible(true);
                    // ִ�и÷���
                    m.invoke(obj);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
