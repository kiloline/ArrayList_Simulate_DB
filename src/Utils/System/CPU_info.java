package Utils.System;

import com.sun.management.OperatingSystemMXBean;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.concurrent.TimeUnit;

public class CPU_info {
    String host="127.0.0.1";
    public double getCPU_info(int port) throws IOException {
        JMXServiceURL serviceURL = new JMXServiceURL(null,null, port);
        JMXConnector conn = JMXConnectorFactory.connect(serviceURL);
        MBeanServerConnection mbs = conn.getMBeanServerConnection();

        //获取远程memorymxbean
        MemoryMXBean memBean = ManagementFactory.newPlatformMXBeanProxy
                (mbs, ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
        //获取远程opretingsystemmxbean
        OperatingSystemMXBean opMXbean =
                ManagementFactory.newPlatformMXBeanProxy(mbs,
                        ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);

        Long start = System.currentTimeMillis();
        long startT = opMXbean.getProcessCpuTime();
        /**    Collect data every 5 seconds      */
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            //logger.error("InterruptedException occurred while MemoryCollector sleeping...");
        }
        Long end = System.currentTimeMillis();
        long endT = opMXbean.getProcessCpuTime();
//end - start 即为当前采集的时间单元，单位ms
//endT - startT 为当前时间单元内cpu使用的时间，单位为ns
        double ratio = (endT-startT)/1000000.0/(end-start)/opMXbean.getAvailableProcessors();
        return ratio;
    }

    public static void main(String ar[]) {
        CPU_info Ci=new CPU_info();
        for(int port=1;port<65535;port++) {
            try {
                System.out.print(Ci.getCPU_info(port));
                break;
            } catch (IOException e) {
                //e.printStackTrace();
                continue;
            }
        }
    }
}