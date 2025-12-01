
package functions;
import java.io.*;
import java.util.Objects;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable  {
    
    private FunctionPoint points[];
    private int pointsCount;
    
    
    
    public ArrayTabulatedFunction() {
        points = new FunctionPoint[10]; // начальная емкость
        pointsCount = 0;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if(leftX >= rightX)
        {
            throw new IllegalArgumentException("the left border is larger than the right one");
        }

        if(pointsCount < 2)
        {
            throw new IllegalArgumentException("Number of points is less than 2");
        }
        
        points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(leftX + i * step, 0);
        }

    }
    //Externalizable
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        pointsCount = in.readInt();
        points = new FunctionPoint[pointsCount];
        
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if(leftX >= rightX)
        {
            throw new IllegalArgumentException("the left border is larger than the right one");
        }

        if(values.length < 2)
        {
            throw new IllegalArgumentException("Number of points is less than 2");
        }
        
        pointsCount = values.length;
        points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(leftX + i * step, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) {
    
    if (points.length < 2) {
        throw new IllegalArgumentException("Number of points is less than 2");
    }
    
    
    for (int i = 1; i < points.length; i++) {
        if (points[i].getX() <= points[i - 1].getX()) {
            throw new IllegalArgumentException("Points are not ordered by X coordinate");
        }
    }
    
    
    this.pointsCount = points.length;
    this.points = new FunctionPoint[pointsCount];
    for (int i = 0; i < pointsCount; i++) {
        this.points[i] = new FunctionPoint(points[i].getX(), points[i].getY());
    }
}
    @Override
    public String toString()
    {
        StringBuilder arr_num =new StringBuilder();
        arr_num.append("{");
        for(int i=0;i<pointsCount;i++)
        {
            arr_num.append("(").append(points[i].getX()).append(";").append(points[i].getY()).append(")");
            if(i<pointsCount-1)
            {
                arr_num.append(",");
            }
        }
        arr_num.append("}");
        return arr_num.toString();

    }

public boolean equals(Object o) 
{
    if (this == o) 
    {
        return true;
    }

    if (o == null) {
        return false;
    }

    if (o instanceof ArrayTabulatedFunction) 
        {
        ArrayTabulatedFunction one = (ArrayTabulatedFunction) o;
        
        if (this.pointsCount != one.pointsCount) 
        {
            return false;
        }
        
        for (int i = 0; i < pointsCount; i++) 
            {
            if (!this.points[i].equals(one.points[i])) 
            {
                return false;
            }
        }
        return true;
    } 
    else if (o instanceof TabulatedFunction) 
        {
        TabulatedFunction one = (TabulatedFunction) o;
            
        if (this.getPointsCount() != one.getPointsCount())
        {
            return false;
        }
            
        //equals класса FunctionPoint
        for (int i = 0; i < this.getPointsCount(); i++) 
            {
            FunctionPoint thisPoint = new FunctionPoint(this.getPointX(i), this.getPointY(i));
            FunctionPoint onePoint = new FunctionPoint(one.getPointX(i), one.getPointY(i));
                
            if (!thisPoint.equals(onePoint)) 
            {
                return false;
            }
        }
        return true;
    }
        
    return false;
}

    @Override
    public int hashCode() {
        int hash = pointsCount; // Включаем количество точек в хэш
        
        // Комбинируем хэш-коды всех точек
        for (int i = 0; i < pointsCount; i++) {
            hash ^= points[i].hashCode();
        }
        
        return hash;
    }

    @Override
    public TabulatedFunction clone() 
    {
        ArrayTabulatedFunction cloned= new ArrayTabulatedFunction();
        cloned.pointsCount=this.pointsCount;
        cloned.points=new FunctionPoint[this.points.length];

        for(int i=0;i<cloned.pointsCount;i++)
        {
            cloned.points[i]=(FunctionPoint)this.points[i].clone();
        }
        return cloned;

    }

    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    public double getRightDomainBorder() {
        return points[pointsCount-1].getX();
    }

    public double getFunctionValue(double x)
     {
        double epcilon=1e-9;
        double leftX=points[0].getX();
        double rightX=points[pointsCount-1].getX();
        if (x < leftX|| x > rightX)
            return Double.NaN;
        if(Math.abs(x-leftX)<epcilon)
        {
            return points[0].getY();
        }
        if(Math.abs(x-rightX)<epcilon)
        {
            return points[pointsCount-1].getY();
        }
        int i = 0;
        double value=0;
        for (i=0;x > points[i].getX();++i)
        {
        value = points[i].getY() + (points[i + 1].getY() - points[i].getY()) * (x - points[i].getX()) / (points[i + 1].getX() - points[i].getX());
        }
        return value;

    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        if(index<0||index>=pointsCount)
        {
            throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
        }
        return new FunctionPoint(points[index]); 
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        double leftX=getLeftDomainBorder();
        double rightX=getRightDomainBorder();
        if (index < 0 || index >= pointsCount)
        {
            throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
        }
        if(leftX > point.getX() || rightX < point.getX())
        {
            throw new InappropriateFunctionPointException("x out of boreder");
        }
        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) {

        if (index < 0 || index >= pointsCount)
        {
            throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
            
        }
             if (index < 0 || index >= pointsCount)
             {
                return Double.NaN;
             }
        return points[index].getX();
    }


public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
    double leftX=getLeftDomainBorder();
    double rightX=getRightDomainBorder();
    if (index < 0 || index >= pointsCount) {
        throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
    }

    double minX;
if (index == 0) {
    minX = leftX;
} else {
    minX = points[index - 1].getX();
}

double maxX;
if (index == pointsCount - 1) {
    maxX = rightX;
} else {
    maxX = points[index + 1].getX();
}
    
    if (x < minX || x > maxX) {
        throw new InappropriateFunctionPointException("x out of border");
    }

    double epсilon = 1e-9;
    for (int i = 0; i < pointsCount; i++) {
        if (i != index && Math.abs(points[i].getX() - x) < epсilon) {
            throw new InappropriateFunctionPointException("Точка с таким x уже существует");
        }
    }

    points[index].setX(x);
}

    public double getPointY(int index) {
        if (index < 0 || index > pointsCount)
        {
            throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
            
        }
         if (index < 0 || index >= pointsCount)
        {
        return Double.NaN;
        }

        return points[index].getY();
    }

    public void setPointY(int index, double y)
    {
        if (index < 0 || index > pointsCount)
        {
            throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
        }
        points[index].setY(y);
    }

  public void deletePoint(int index){
    if (index < 0 || index >= pointsCount)
        {
            throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
        }
    if(pointsCount<3)
    {
        throw new IllegalStateException("The number of points is less than 3");
    }
        
        if(index == pointsCount - 1){
            pointsCount--;
            points[pointsCount] = null;
        }
        else{ 
            System.arraycopy(points, index + 1, points, index, pointsCount - 1 - index);
            pointsCount--;
            points[pointsCount] = null;
        }
        
    
        }

public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
    double epсilon = 1e-9;
    
    
    for (int i = 0; i < pointsCount; i++) {
        if (Math.abs(points[i].getX() - point.getX()) < epсilon) {
            throw new InappropriateFunctionPointException("There is already such an x");
        }
    }
    
    
    int insertIndex = 0;
    while (insertIndex < pointsCount && point.getX() > points[insertIndex].getX()) {
        insertIndex++;
    }
    
    
    FunctionPoint[] newPoints = new FunctionPoint[pointsCount + 1];
    System.arraycopy(points, 0, newPoints, 0, insertIndex);
    newPoints[insertIndex] = new FunctionPoint(point);
    System.arraycopy(points, insertIndex, newPoints, insertIndex + 1, pointsCount - insertIndex);
    
    points = newPoints;
    pointsCount++;
}

}