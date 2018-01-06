package Utils.Check;

import java.util.regex.Pattern;

/**
 *
 * @author gosiple
 */
public class check_regular 
{
    private final static String isInteger="^[+-]{0,1}\\d+";
    private final static String isDouble=isInteger+"\\.\\d+";
    private final static String isListName="^[_A-Za-z]+[_A-Za-z0-9]*";
    private final static String isTLName=isListName+"\\.[A-Za-z_]+[_A-Za-z0-9]*";
    private final static String isTSTLName=isListName+"\\.[A-Za-z_]+[_A-Za-z0-9]*\\.[_A-Za-z]+[_A-Za-z0-9]*";
    private final static String[] patterns = {isInteger,isDouble,isListName,isTLName,isTSTLName};

    public static String regular(String toCheck)
    {
        int loop;
        for(loop=0;loop<patterns.length;loop++)
        {
            if(Pattern.matches(patterns[loop], toCheck))
                break;
        }
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
        System.out.println(check_regular.regular("123"));
        System.out.println(check_regular.regular("+123"));
        System.out.println(check_regular.regular("-123.98"));
        System.out.println(check_regular.regular("a"));
        System.out.println(check_regular.regular("temp4.kill9"));
        System.out.println(check_regular.regular("name_List.temp4.toKill"));
    }
}