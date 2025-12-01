package functions;

import java.io.Serializable;
import java.util.Objects;

public class FunctionPoint implements Serializable {
	private double x;
	private double y;

	public FunctionPoint(double x, double y)
	{
		this.x=x;
		this.y=y;
	}
	public FunctionPoint(FunctionPoint point)
	{	 
        this.x = point.x;
        this.y = point.y;
	}

	public FunctionPoint()
	{
		this.x = 0;
        this.y = 0;
	}
	
    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getX()
    {
        return this.x;
    }

    public  double getY()
    {
        return this.y;
    }

    @Override

    public  String toString()
    {
        return "(" + x + ";" + y + ")";
    }

    @Override

     public boolean equals(Object o)
    {
        double epcilon=1e-9;
        if(this==o)
        {
            return true;
        }
        if(null==o || getClass()!=o.getClass())
        {
            return false;
        }

        FunctionPoint that = (FunctionPoint) o;

         return Math.abs(this.x - that.x) < epcilon && Math.abs(this.y - that.y) < epcilon; 
               
    }
     
    @Override
    public Object clone() 
    {
        return new FunctionPoint(this);  //создаём копию
    }

    @Override
public int hashCode() {
    return Objects.hash(x, y);
}


}