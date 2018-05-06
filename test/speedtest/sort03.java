package speedtest;

import java.util.Scanner;
public class sort03 {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Scanner scan=new Scanner(System.in);

        System.out.print("请输入数组元素个数:");
        int n = scan.nextInt();    //调用Scanner类中的方法.nextInt() 对象名.方法名

        System.out.println("您输入的数组的个数是："+n);

        System.out.print("下面请输入数组：");
        int num[]=new int[n];
        for(int i=0;i<num.length;i++){
            num[i] = scan.nextInt();
        }

        System.out.print("您输入的序列是：");
        for(int i=0;i<num.length-1;i++)
        {
            System.out.print(num[i]);
            System.out.print(',');
        }
        System.out.println(num[num.length-1]);

        java.util.Arrays.sort(num);

        System.out.print("排序以后的数组序列是：");
        for(int j=0;j<num.length-1;j++){
            System.out.print(num[j]);
            System.out.print(',');
        }
        System.out.println(num[num.length-1]);
    }
}

