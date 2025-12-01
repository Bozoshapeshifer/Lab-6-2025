package functions.basic;

import functions.TrigonometricFunction;



public class Sin extends TrigonometricFunction{

    @Override
    public double getFunctionValue(double x)
    {
        return Math.sin(x);
    }
}