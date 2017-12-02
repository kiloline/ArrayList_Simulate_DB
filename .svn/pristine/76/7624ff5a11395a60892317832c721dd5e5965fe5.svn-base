package Data.judgement;

import java.util.Objects;

/**
 *
 * @author BFD-501
 * 这是仿照Boolean类制作的一个开关，总共有三个状态：letter、mark、stop
 * 分别对应字符、标点、空格
 */
public class Coolean 
{
    public static Coolean letter=new Coolean("letter");
    public static Coolean mark=new Coolean("mark");
    //public static Coolean s_q=new Coolean("single_quotation");
    //public static Coolean barket=new Coolean("barket");
    public static Coolean stop=new Coolean("stop");
    String status;
    private Coolean(String str)
    {
        this.status=str;
    }
    @Override
    public String toString()
    {
        return status;
    }
    @Override
    public boolean equals(Object o)
    {
        if(o.hashCode()==this.hashCode())//&&this.toString().equals(o.toString()))
            return true;
        else 
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 9;
        hash = 37 * hash + Objects.hashCode(this.status);
        return hash;
    }
}
