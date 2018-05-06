package Utils.Math;

public class utilClass {
    public static Double Sqrt(Double x)
    {
        Double xhalf = 0.5f*x;
        Integer i = x.intValue(); // get bits for floating VALUE
        i = 0x5f375a86- (i>>1); // gives initial guess y0
        x = i.doubleValue(); // convert bits BACK to float
        x = x*(1.5f-xhalf*x*x); // Newton step, repeating increases accuracy
        x = x*(1.5f-xhalf*x*x); // Newton step, repeating increases accuracy
        //x = x*(1.5f-xhalf*x*x); // Newton step, repeating increases accuracy

        return 1/x;
    }
}
