package Utils.Math.Impl;

import Utils.Math.SingleNumber;
import Utils.Math.utilClass;

public class doublec implements SingleNumber{
    @Override
    public Number sqrt(Number right) {
        return utilClass.Sqrt(right.doubleValue());
    }

    @Override
    public Number abs(Number right) {
        return Math.abs(right.doubleValue());
    }
}
