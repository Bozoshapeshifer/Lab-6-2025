package functions.basic;

import functions.TrigonometricFunction;


public class Tan extends TrigonometricFunction {

    @Override
    public double getFunctionValue(double x) 
    {
        return Math.tan(x);
    }
}