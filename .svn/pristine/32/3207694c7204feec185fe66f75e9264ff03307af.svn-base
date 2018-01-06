package Utils.Check;

import m_Exception.type.Type_not_exist;

import java.io.Serializable;

/**
 *
 * @author rkppo
 */
public class check_StringtoNumber implements Serializable
{
    public boolean check_StringtoInteger(String string) throws Type_not_exist//按位检查String key的值，必须处于48~57之间，为可能的String转int做好准备
    {
        int loop;
        boolean l=false,i=false;
        {
            if(string.charAt(0)=='-'||string.charAt(0)=='+')
                l=true;
            if(string.charAt(0)<58&&string.charAt(0)>47)
                i=true;
            if(!(l|i))
                throw new Type_not_exist();
                //return false;
        }
        for(loop=1;loop<string.length();loop++)
        {
            if(string.charAt(loop)>57||string.charAt(loop)<48)
                throw new Type_not_exist();
                //return false;
        }
        return true;
    }
    public boolean check_StringtoDouble(String string) throws Type_not_exist//按位检查String key的值，必须处于48~57之间，为可能的String转int做好准备
    {
        boolean point=true;
        int loop;
        boolean l=false,i=false;
        {
            if(string.charAt(0)=='-'||string.charAt(0)=='+')
                l=true;
            if(string.charAt(0)<58&&string.charAt(0)>47)
                i=true;
            if(!(l|i))
                throw new Type_not_exist();
                //return false;
        }
        for(loop=1;loop<string.length();loop++)
        {
            if(string.charAt(loop)==46&&point)//当前符号是小数点
            {
                point=false;
                continue;
            }
            if(string.charAt(loop)>57||string.charAt(loop)<48)
                throw new Type_not_exist();
                //return false;
        }
        return true;
    }
}

