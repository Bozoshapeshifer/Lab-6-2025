package functions.meta;
import functions.Function;

public class Power implements Function {

    Function a;
    double b;

    public Power(Function a, double b)
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
        return Math.pow(a.getFunctionValue(x),b);
    }
}