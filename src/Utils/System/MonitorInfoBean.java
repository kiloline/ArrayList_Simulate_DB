package Utils.System;

/**
 *
 * @author rkppo
 */
public class MonitorInfoBean {
    /** ��ʹ���ڴ�. */
    private String totalMemory;
    /** ʣ���ڴ�. */
    private String freeMemory;
    /** ����ʹ���ڴ�. */
    private String maxMemory;

    /** ����ϵͳ. */
    private String osName;
    /** �ܵ������ڴ�. */
    private String totalMemorySize;
    /** ʣ��������ڴ�. */
    private String freePhysicalMemorySize;
    /** ��ʹ�õ������ڴ�. */
    private String usedMemory;

    public String getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(String usedMemory) {
        this.usedMemory = usedMemory;
    }

    public String getFreePhysicalMemorySize() {
        return freePhysicalMemorySize;
    }

    public void setFreePhysicalMemorySize(String freePhysicalMemorySize) {
        this.freePhysicalMemorySize = freePhysicalMemorySize;
    }

    public String getTotalMemorySize() {
        return totalMemorySize;
    }

    public void setTotalMemorySize(String totalMemorySize) {
        this.totalMemorySize = totalMemorySize;
    }

    public String getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(String freeMemory) {
        this.freeMemory = freeMemory;
    }

    public String getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(String maxMemory) {
        this.maxMemory = maxMemory;
    }

    public String getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(String totalMemory) {
        this.totalMemory = totalMemory;
    }

    /** �߳�����. */
    private int totalThread;

    public String getCpuRatio() {
        return cpuRatio;
    }

    public void setCpuRatio(String cpuRatio) {
        this.cpuRatio = cpuRatio;
    }

    /** cpuʹ����. */
    private String cpuRatio;


    public String getOsName() {
        return osName;
    }
    public void setOsName(String osName) {
        this.osName = osName;
    }

    public int getTotalThread() {
        return totalThread;
    }
    public void setTotalThread(int totalThread) {
        this.totalThread = totalThread;
    }
}
