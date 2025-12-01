package functions.basic;
import functions.Function;

public class Log implements Function {
    private double base;
    
    public Log(double base) {
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException("Основание логарифма должно быть > 0 и ≠ 1");
        }
        this.base = base;
    }
    
    @Override
    public double getLeftDomainBorder() {
        return 0; // Граница 0, но функция определена только для x > 0
    }
    
    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    
    @Override
    public double getFunctionValue(double x) {
        // Логарифм определен только для x > 0
        if (x <= 0) 
        {
            return Double.NaN;
        }
        else{
            return Math.log(x) / Math.log(base);
        }
        
    }
}