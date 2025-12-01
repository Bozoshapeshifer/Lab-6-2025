package functions.basic;

import functions.TrigonometricFunction;


public class Cos extends TrigonometricFunction {

    @Override
    public double getFunctionValue(double x)
    {
        return Math.cos(x);
    }
}