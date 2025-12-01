package functions;

import functions.meta.*;

public final class Functions {
public static Function shift(Function f, double shiftX, double shiftY)
{
    return new Shift(f,shiftX,shiftY);

}

public static Function scale(Function f, double scaleX, double scaleY)
{
    return new Scale(f,scaleX,scaleY);

}
    
public static Function power(Function f, double power)
{
    return new Power(f, power); 
}

public static Function sum(Function f1, Function f2)
{
    return new Sum(f1,f2);
}

public static Function mult(Function f1, Function f2)
{
    return new Mult(f1,f2);
}

public static Function composition(Function f1, Function f2)
{
    return new Composition(f1,f2);
}

 public static double integrate(Function function, double leftBorder, double rightBorder, double step)
{
    if (leftBorder >= rightBorder) 
    {
        throw new IllegalArgumentException("Левая граница должна быть меньше правой: leftBorder=" + leftBorder + ", rightBorder=" + rightBorder);
    }
        if (step <= 0) 
    {
        throw new IllegalArgumentException("Шаг должен быть положительным: step=" + step);
     }
        
        // Проверка области определения
        if (leftBorder < function.getLeftDomainBorder() || rightBorder > function.getRightDomainBorder())
        {
            throw new IllegalArgumentException(String.format("Интервал [%.2f, %.2f] выходит за границы области определения [%.2f, %.2f]",leftBorder, rightBorder, function.getLeftDomainBorder(), function.getRightDomainBorder()));
        }

    double integral = 0;
    double x=leftBorder;
    int n= (int) Math.ceil((rightBorder-leftBorder)/step);
    for(int i=0; i<n;i++)
    {
        double x1=x;
        double x2 = (i == n - 1) ? rightBorder : Math.min(x + step, rightBorder);
        double y1 = function.getFunctionValue(x1);
        double y2 = function.getFunctionValue(x2);
        double S = (y1+y2)*(x2-x1)/2.0;
        integral += S;
        x=x2;
    }
    return integral;
    
}

}