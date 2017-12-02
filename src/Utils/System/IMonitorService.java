package Utils.System;

/**
 *
 * @author rkppo
 */
public interface IMonitorService {
    /**
     * ��õ�ǰ�ļ�ض���.
     * @return ���ع���õļ�ض���
     * @throws Exception
     */
    public MonitorInfoBean getMonitorInfoBean() throws Exception;
    /**
     * ��ȡ��ǰ���̵�PID
     * @return
     */
    int getPid();
    /**
     * ��ȡ����ID��������
     * @return   "pid@hostname"
     */
     String getPidHostName();

    /**
     * ��ȡjvmʹ�õ��ڴ�
     * @return
     */
     long getUsedMemoryMB();


    /**
     * ��ȡjvm�����ڴ�
     * @return
     */
     long getFreeMemoryMB();
}
