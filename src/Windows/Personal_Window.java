package Windows;

import Service_pkg.Service;
import java.util.Scanner;

/**
 * @author BFD-501
 * 用户的每一行输入都要人为的在后面加上一个'\n'来保证识别一致性
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
    {//所有的错误最终只能抛出到这里来处理
        int inputline=1;
        String temp;//用这个变量处理"--"注释
        StringBuffer toSQL=new StringBuffer();
        System.out.print("SQL>");
        temp=scanf.nextLine();
        temp=temp.split("--")[0];//一行之内，将第一个"--"之后的所有字符全部截掉，只留"--"之前的部分
        toSQL.append(temp);
        if(toSQL.toString().equals("exit"))//退出命令
            System.exit(0);
        if(temp.indexOf("^c")!=-1)//假装这里就是CTRL+C
                return;
        if(toSQL.length()==0)
            return;
        while(toSQL.indexOf(";")==-1)
        {
            System.out.print(inputline+"  >");
            toSQL.append('\n');
            temp=scanf.nextLine();
            if(temp.indexOf("^c")!=-1)//假装这里就是CTRL+C
                return;
            temp=temp.split("--")[0];//一行之内，将第一个"--"之后的所有字符全部截掉，只留"--"之前的部分
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
