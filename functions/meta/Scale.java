package functions.meta;
import functions.Function;

public class Scale implements Function {
    Function a;
    double kx,ky;

    public Scale(Function a, double kx,double ky)
    {
        this.a=a;
        this.kx=kx;
        this.ky=ky;
    }
  @Override 
    public double getLeftDomainBorder()
    {
        if (kx > 0) {
            return a.getLeftDomainBorder() / kx;
        } else if (kx < 0) {
            return a.getRightDomainBorder() / kx;
        } else {
            return Double.NEGATIVE_INFINITY;
        }
    }

    @Override
    public double getRightDomainBorder()
    {
        if (kx > 0) 
        {
            return a.getRightDomainBorder() / kx;
        } 
        else if (kx < 0) 
        {
            return a.getLeftDomainBorder() / kx;
        } 
        else
        {
            return Double.POSITIVE_INFINITY;
        }
    }

    @Override

    public double getFunctionValue(double x)
    {
        return a.getFunctionValue(x*kx)*ky;
    }
}

