package functions.meta;

import functions.Function;

public class Composition implements Function 
{
    Function a,b;

    public Composition(Function a, Function b)
    {
        this.a=a;
        this.b=b;
    }

    @Override
    public double getLeftDomainBorder()
    {
        return a.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder()
    {
        return a.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x)
    {
        return a.getFunctionValue(b.getFunctionValue(x));

    }
    
}