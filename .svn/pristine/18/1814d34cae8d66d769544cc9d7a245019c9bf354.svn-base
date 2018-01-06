package speedtest;

import java.util.LinkedList;
import java.util.Scanner;

public class Timetrans {
    public LinkedList<String> timetranslates(LinkedList<String> dt)
    {
        LinkedList<String> tradt=new LinkedList<>();
        for(String s:dt)
        {
            tradt.add(singaltimetranslate(s));
        }
        return tradt;
    }

    public String singaltimetranslate(String time)
    {
        String year;
        String month;
        String date;
        String hhmiss;
        String[] times=time.split(" ");
        try {
            year = times[5];
            date = times[2];
            hhmiss = times[3];
            switch (times[1]) {
                case "Jan":
                    month = "01";
                    break;
                case "Feb":
                    month = "02";
                    break;
                case "Mar":
                    month = "03";
                    break;
                case "Apr":
                    month = "04";
                    break;
                case "May":
                    month = "05";
                    break;
                case "Jun":
                    month = "06";
                    break;
                case "Jul":
                    month = "07";
                    break;
                case "Aug":
                    month = "08";
                    break;
                case "Sep":
                    month = "09";
                    break;
                case "Oct":
                    month = "10";
                    break;
                case "Nov":
                    month = "11";
                    break;
                case "Dec":
                    month = "12";
                    break;
                default:
                    month = "";
            }
        }
        catch (Exception e) {
            return "";
        }
        StringBuilder sb=new StringBuilder();
        sb.append(year);
        sb.append("-");
        sb.append(month);
        sb.append("-");
        sb.append(date);
        sb.append(" ");
        sb.append(hhmiss);

        return sb.toString();
    }

    public static void main(String ar[])
    {
        Scanner scanf=new Scanner(System.in);
        LinkedList<String> tradt=new LinkedList<>();
        Timetrans timetrans=new Timetrans();

        System.out.println("标准日期格式：Thu Mar 03 17:49:47 CST 2016，输入后exit开始输出");
        for(String s="";;)
        {
            s=scanf.nextLine();
            if(s.equals("exit"))
                break;
            tradt.add(timetrans.singaltimetranslate(s));
        }
        for(String s:tradt)
        {
            System.out.println(s);
        }
    }
}
