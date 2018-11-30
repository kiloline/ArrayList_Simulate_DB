package Utils.Math.Impl;

import Utils.Math.DoubleNumber;

public class integerdouble implements DoubleNumber {
    @Override
    public Number plus(Number left, Number right) {
        return left.intValue()+right.doubleValue();
    }

    @Override
    public Number minus(Number left, Number right) {
        return left.intValue()-right.doubleValue();
    }

    @Override
    public Number multiply(Number left, Number right) {
        return left.intValue()*right.doubleValue();
    }

    @Override
    public Number division(Number left, Number right) {
        return left.intValue()/right.doubleValue();
    }

    @Override
    public Number mi(Number left, Number right) {
        return Math.pow(left.intValue(),right.doubleValue());
    }

    @Override
    public Number moer(Number left, Number right) {
        return left.intValue()%right.doubleValue();
    }

    @Override
    public Number pow(Number left, Number right) {
        return null;
    }
}
