package Utils.Math;

public final class MathFunction_Calculation {
    static SingleNumber sb;
    private static void before(Number right)
    {
        String righttype=right.getClass().getSimpleName();
        try {
            Class c = Class.forName("Utils.Math.Impl." + righttype.toLowerCase()+'c');
            sb = (SingleNumber) c.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Number sqrt(Number right)
    {
        before(right);
        return sb.sqrt(right);
    }
}
