package functions.meta;

import functions.Function;

public class Shift implements Function 
{
Function a;
double plus_x, plus_y;

    public Shift(Function a,double plus_x,double plus_y)
    {
        this.a=a;
        this.plus_x=plus_x;
        this.plus_y=plus_y;
    }


 @Override
    public double getLeftDomainBorder()
    {
        return a.getLeftDomainBorder() - plus_x; 
    }

    @Override
    public double getRightDomainBorder()
    {
        return a.getRightDomainBorder() - plus_x;  
    }

    @Override
    public double getFunctionValue(double x)
    {
        return a.getFunctionValue(x+plus_x)+plus_y;
    }
    
}