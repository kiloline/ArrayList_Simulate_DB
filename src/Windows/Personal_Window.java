package Windows;

import Service.Service;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author BFD-501
 * �û���ÿһ�����붼Ҫ��Ϊ���ں������һ��'\n'����֤ʶ��һ����
 */
public class Personal_Window 
{
    Service service;
    Scanner scanf;
    public Personal_Window(Service backstage)
    {
        this.service=backstage;
        scanf=new Scanner(System.in);
    }
    
    public void inputStream()
    {//���еĴ�������ֻ���׳�������������
        int inputline=1;
        String temp;//�������������"--"ע��
        StringBuffer toSQL=new StringBuffer();
        System.out.print("SQL>");
        temp=scanf.nextLine();
        temp=temp.split("--")[0];//һ��֮�ڣ�����һ��"--"֮��������ַ�ȫ���ص���ֻ��"--"֮ǰ�Ĳ���
        toSQL.append(temp);
        if(toSQL.toString().equals("exit"))//�˳�����
            System.exit(0);
        if(temp.indexOf("^c")!=-1)//��װ�������CTRL+C
                return;
        if(toSQL.length()==0)
            return;
        while(toSQL.indexOf(";")==-1)
        {
            System.out.print(inputline+"  >");
            toSQL.append('\n');
            temp=scanf.nextLine();
            if(temp.indexOf("^c")!=-1)//��װ�������CTRL+C
                return;
            temp=temp.split("--")[0];//һ��֮�ڣ�����һ��"--"֮��������ַ�ȫ���ص���ֻ��"--"֮ǰ�Ĳ���
            toSQL.append(temp);
            inputline++;
        }
        
        try {
            service.Language_dispose(toSQL.toString());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ;
        }
    }
}
