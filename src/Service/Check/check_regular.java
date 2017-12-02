package Service.Check;

import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 *
 * @author gosiple
 */
public class check_regular 
{
    private LinkedList<String> patterns;
    public check_regular()
    {
        String isInteger="^[+-]{0,1}\\d+";
        String isDouble=isInteger+"\\.\\d+";
        String isListName="^[_A-Za-z]+[_A-Za-z0-9]*";
        String isTLName=isListName+"\\.[A-Za-z_]+[_A-Za-z0-9]*";
        String isTSTLName=isListName+"\\.[A-Za-z_]+[_A-Za-z0-9]*\\.[_A-Za-z]+[_A-Za-z0-9]*";
        patterns=new LinkedList<>();
        patterns.add(isInteger);
        patterns.add(isDouble);
        patterns.add(isListName);
        patterns.add(isTLName);
        patterns.add(isTSTLName);
    }
    public String regular(String toCheck)
    {
        int loop=0;
        //for(int loop=0;loop<patterns.size();loop++)
        {
            
        }
        while(!Pattern.matches(patterns.get(loop), toCheck))
            loop++;
        switch(loop)
        {
            case 0:return "isInteger";
            case 1:return "isDouble";
            case 2:return "isListName";
            case 3:return "isTLName";
            case 4:return "isTSTLName";
            default:return null;
        }
    }
    
    public static void main(String[] ar)
    {
        check_regular cr=new check_regular();
        System.out.println(cr.regular("123"));
        System.out.println(cr.regular("+123"));
        System.out.println(cr.regular("-123.98"));
        System.out.println(cr.regular("a"));
        System.out.println(cr.regular("temp4.kill9"));
        System.out.println(cr.regular("name_List.temp4.toKill"));
    }
}