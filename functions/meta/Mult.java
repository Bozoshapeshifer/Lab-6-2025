package functions.meta;

import functions.Function;

public class Mult implements Function
{
    Function a,b;

    public Mult(Function a,Function b)

    {
        this.a=a;
        this.b=b;
    }

    
    @Override
    public double getLeftDomainBorder() 
    {
        return Math.max(a.getLeftDomainBorder(),b.getLeftDomainBorder());
    }
    
    @Override
    public double getRightDomainBorder()
    {
        return Math.min(a.getRightDomainBorder(),b.getRightDomainBorder());
    }

    @Override
    public double getFunctionValue(double x)
    {
        return a.getFunctionValue(x)*b.getFunctionValue(x); 
    }

    
    
}