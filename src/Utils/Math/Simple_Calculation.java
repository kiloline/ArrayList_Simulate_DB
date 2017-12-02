package Utils.Math;

public final class Simple_Calculation {
    static DoubleNumber db;
    private static void before(Number left,Number right)
    {
        String lefttype=left.getClass().getSimpleName();
        String righttype=right.getClass().getSimpleName();
        try {
            Class c = Class.forName("Utils.Math.Impl." + (lefttype + righttype).toLowerCase());
            db = (DoubleNumber) c.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Number add(Number left,Number right)
    {
        before( left, right);
        return db.plus(left,right);
    }

    public static Number minus(Number left,Number right)
    {
        before( left, right);
        return db.minus(left,right);
    }

    public static Number multi(Number left,Number right)
    {
        before( left, right);
        return db.multiply(left,right);
    }

    public static Number div(Number left,Number right)
    {
        before( left, right);
        return db.division(left,right);
    }
    public static Number mi(Number left,Number right)
    {
        before( left, right);
        return db.mi(left,right);
    }
    public static Number moer(Number left,Number right)
    {
        before( left, right);
        return db.moer(left,right);
    }
}
