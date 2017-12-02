package Utils.System;

/**
 *
 * @author rkppo
 */
import com.sun.management.OperatingSystemMXBean;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.StringTokenizer;

public class MonitorService implements IMonitorService {
    private static final int CPUTIME = 1;
    private static final int PERCENT = 100;
    private static final int FAULTLENGTH = 10;
    private static String linuxVersion = null;
    /**
     * ��õ�ǰ�ļ�ض���.
     *
     * @return ���ع���õļ�ض���
     * @throws Exception
     * @author GuoHuang
     */
    public MonitorInfoBean getMonitorInfoBean() throws Exception {
        int kb = 1024;
        // ��ʹ���ڴ�
        long totalMemory = Runtime.getRuntime().totalMemory() / kb;
        // ʣ���ڴ�
        long freeMemory = Runtime.getRuntime().freeMemory() / kb;
        // ����ʹ���ڴ�
        long maxMemory = Runtime.getRuntime().maxMemory() / kb;
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // ����ϵͳ
        String osName = System.getProperty("os.name");
        // �ܵ������ڴ�
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;
        // ʣ��������ڴ�
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;
        // ��ʹ�õ������ڴ�
        long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / kb;
        // ����߳�����
        ThreadGroup parentThread;
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread.getParent() != null; parentThread = parentThread.getParent());
        int totalThread = parentThread.activeCount();
        double cpuRatio = 0;
        if (osName.toLowerCase().startsWith("windows")) {
            cpuRatio = this.getCpuRatioForWindows();
        } else {
            cpuRatio = getCpuRateForLinux();
        }
        // ���췵�ض���
        MonitorInfoBean infoBean = new MonitorInfoBean();
        infoBean.setFreeMemory(freeMemory/1024+"MB");
        infoBean.setFreePhysicalMemorySize(freePhysicalMemorySize/1024+"MB");
        infoBean.setMaxMemory(maxMemory/1024+"MB");
        infoBean.setOsName(osName);
        infoBean.setTotalMemory(totalMemory/1024+"MB");
        infoBean.setTotalMemorySize(totalMemorySize/1024+"MB");
        infoBean.setTotalThread(totalThread);
        infoBean.setUsedMemory(usedMemory/1024+"MB");
        infoBean.setCpuRatio(cpuRatio+"%");
        return infoBean;
    }

    @Override
    public int getPid() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName(); // format: "pid@hostname"
        //System.out.println("name:"+name);
        try {
            return Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public String getPidHostName() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        return  runtime.getName(); // format: "pid@hostname"
    }

    @Override
    public long getUsedMemoryMB() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1048576; // 1024 * 1024 = 1048576;
    }

    @Override
    public long getFreeMemoryMB() {
        return Runtime.getRuntime().freeMemory()/1048576; // 1024 * 1024 = 1048576;
    }

    private static double getCpuRateForLinux() {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat = null;
        try {
            System.out.println("Get usage rate of CUP , linux version: " + linuxVersion);
            Process process = Runtime.getRuntime().exec("top -b -n 1");
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            brStat = new BufferedReader(isr);
            if (linuxVersion.equals("2.4")) {
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                tokenStat = new StringTokenizer(brStat.readLine());
                tokenStat.nextToken();
                tokenStat.nextToken();
                String user = tokenStat.nextToken();
                tokenStat.nextToken();
                String system = tokenStat.nextToken();
                tokenStat.nextToken();
                String nice = tokenStat.nextToken();
                System.out.println(user + " , " + system + " , " + nice);
                user = user.substring(0, user.indexOf("%"));
                system = system.substring(0, system.indexOf("%"));
                nice = nice.substring(0, nice.indexOf("%"));
                float userUsage = new Float(user).floatValue();
                float systemUsage = new Float(system).floatValue();
                float niceUsage = new Float(nice).floatValue();
                return (userUsage + systemUsage + niceUsage) / 100;
            } else {
                brStat.readLine();
                brStat.readLine();
                tokenStat = new StringTokenizer(brStat.readLine());
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                String cpuUsage = tokenStat.nextToken();
                System.out.println("CPU idle : " + cpuUsage);
                Float usage = new Float(cpuUsage.substring(0, cpuUsage.indexOf("%")));
                return (1 - usage.floatValue() / 100);
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            freeResource(is, isr, brStat);
            return 1;
        } finally {
            freeResource(is, isr, brStat);
        }
    }
    private static void freeResource(InputStream is, InputStreamReader isr,
                                     BufferedReader br) {
        try {
            if (is != null)
                is.close();
            if (isr != null)
                isr.close();
            if (br != null)
                br.close();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
    /**
     * ���CPUʹ����.
     *
     * @return ����cpuʹ����
     * @author GuoHuang
     */
    private double getCpuRatioForWindows() {
        try {
            String procCmd = System.getenv("windir") + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // ȡ������Ϣ
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return Double.valueOf(128 * (busytime) / (busytime + idletime)).doubleValue();
            } else {
                return 0.0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }
    /**
     * ��ȡCPU��Ϣ.
     * @param proc
     * @return
     * @author GuoHuang
     */
    private long[] readCpu(final Process proc) {
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }
                // �ֶγ���˳��Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption =line.substring( capidx, cmdidx - 1).trim();
                String cmd =line.substring(cmdidx, kmtidx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0) {
                    continue;
                }
                String s1 = line.substring(kmtidx, rocidx - 1).trim();
                String s2 =line.substring( umtidx, wocidx - 1).trim();
                if (caption.equals("System Idle Process") || caption.equals("System")) {
                    if (s1.length() > 0)
                        idletime += Long.valueOf(s1).longValue();
                    if (s2.length() > 0)
                        idletime += Long.valueOf(s2).longValue();
                    continue;
                }
                if (s1.length() > 0)
                    kneltime += Long.valueOf(s1).longValue();
                if (s2.length() > 0)
                    usertime += Long.valueOf(s2).longValue();
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        IMonitorService service = new MonitorService();
        MonitorInfoBean monitorInfo = service.getMonitorInfoBean();
        System.out.println("cpuռ����=" + monitorInfo.getCpuRatio());

        //System.out.println("����ʹ���ڴ�=" + monitorInfo.getMaxMemory());
        //System.out.println("��ʹ���ڴ�=" + monitorInfo.getTotalMemory());
        //System.out.println("ʣ���ڴ�=" + monitorInfo.getFreeMemory());


        //System.out.println("����ϵͳ=" + monitorInfo.getOsName());

        //System.out.println("�ܵ������ڴ�=" + monitorInfo.getTotalMemorySize());
        //System.out.println("��ʹ�õ������ڴ�=" + monitorInfo.getUsedMemory());
        //System.out.println("ʣ��������ڴ�=" + monitorInfo.getFreePhysicalMemorySize());
        //System.out.println("�߳�����=" + monitorInfo.getTotalThread());

        System.out.println("pid: " +service.getPid() );
        //System.out.println("FreeMemory: " + service.getFreeMemoryMB());
        //System.out.println("usedMemory: " + service.getUsedMemoryMB());
        //System.in.read(); // block the program so that we can do some probing on it
    }
}
