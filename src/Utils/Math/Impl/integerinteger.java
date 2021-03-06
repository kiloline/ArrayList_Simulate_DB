package Utils.Math.Impl;

import Utils.Math.DoubleNumber;

public class integerinteger implements DoubleNumber {
    @Override
    public Number plus(Number left, Number right) {
        return left.intValue()+right.intValue();
    }

    @Override
    public Number minus(Number left, Number right) {
        return left.intValue()-right.intValue();
    }

    @Override
    public Number multiply(Number left, Number right) {
        return left.intValue()*right.intValue();
    }

    @Override
    public Number division(Number left, Number right) {
        Double result=left.doubleValue()/right.doubleValue();
        if(result.intValue()==result)
            return result.intValue();
        else
            return result;
    }

    @Override
    public Number mi(Number left, Number right) {
        return Math.pow(left.intValue(),right.intValue());
    }

    @Override
    public Number moer(Number left, Number right) {
        return left.intValue()%right.intValue();
    }

    @Override
    public Number pow(Number left, Number right) {
        return null;
    }
}
