package Data.classes;

/**
 *
 * @author rkppo
 */
public class Sequence 
{
    Exception e;
    String Sequence_name;  //序列名
    long Serial_number;  //序列当前值
    long Start_number;  //序列最小值/开始值
    long Abort_number;  //序列最大值/截至值
    boolean Recycle;  //是否允许循环
    int Step_length;  //序列步长
    public Sequence(String sequence_name)
    {
        this.Sequence_name=sequence_name;
        this.Abort_number=Long.MAX_VALUE;
        this.Start_number=0;
        this.Serial_number=0;
    }
    public void setStart(long start)
    {
        this.Serial_number=start;
        this.Start_number=start;
    }
    public void setAbort(long abort)
    {
        this.Abort_number=abort;
    }
    public void setRecycle(boolean recycle)
    {
        this.Recycle=recycle;
    }
    public void setStep(int steplength)
    {
        this.Step_length=steplength;
    }
    
    public Long getNextVal() throws Exception
    {
        if(checkAbort())
        {
            this.Serial_number=this.Serial_number+this.Step_length;
            return this.Serial_number;
        }
        else
            throw e;
    }
    public Long getCurrVal() throws Exception
    {
        if(checkAbort())
        {
            Long temp=this.Serial_number;
            this.Serial_number=this.Serial_number+this.Step_length;
            return temp;
        }
        else
            throw e;
    }
    public void showSerial()
    {
        System.out.println(this.Serial_number);
    }
    
    private boolean checkAbort()
    {
        if(this.Serial_number+this.Step_length>this.Abort_number)
        {
            if(this.Recycle)
            {
                Serial_number=Serial_number-Abort_number+Start_number;
                return true;
            }
            else
                return false;
        }
        else
            return true;
    }
}
