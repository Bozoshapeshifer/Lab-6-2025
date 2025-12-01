package functions;

import java.io.Serializable;
import java.util.Objects;


public class LinkedListTabulatedFunction implements TabulatedFunction,Serializable {
    private class FunctionNode
    {
    private FunctionPoint point;
    private FunctionNode prev;
    private FunctionNode next;
    
    public FunctionNode()
    {
        next=null;
        prev=null;
        point = new FunctionPoint();

    }

    public FunctionNode(FunctionNode prev,FunctionNode next)
    {
        point = new FunctionPoint();
        this.prev = prev;
        this.next = next;
        
    }

    public FunctionPoint getPoint()
    {
        return point;
    }

    public void setPoint(FunctionPoint point)
    {
        this.point=point;
    }

    public FunctionNode getPrev() 
    {
            return prev;
    }
    public void setPrev(FunctionNode prev)
    {
        this.prev=prev;
    }

    
    public FunctionNode getNext()
    {
        return next;
    }

    public void setNext(FunctionNode next)
    {
        this.next=next;
    }
 
}
private FunctionNode head;
private int pointsCount;


public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException{
    this.pointsCount = 0; // сначала 0
    if (leftX >= rightX) throw new IllegalArgumentException("the left border is larger than the right one");
    if (pointsCount < 2) throw new IllegalArgumentException("Number of points is less than 2");
    
    head = new FunctionNode();
    head.setNext(head);
    head.setPrev(head); // создаем циклический список
    
    double step = (rightX - leftX) / (pointsCount - 1);
    for (int i = 0; i < pointsCount; i++) {
        FunctionPoint point = new FunctionPoint(leftX + i * step, 0);
        addNodeToTail().setPoint(point);
    }
}

    public LinkedListTabulatedFunction(double leftX, double rightX, double values[])throws IllegalArgumentException {
        if (leftX >= rightX) throw new IllegalArgumentException("the left border is larger than the right one");
        if (values.length < 2) throw new IllegalArgumentException("Number of points is less than 2");
        head = new FunctionNode(head, head);
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            FunctionPoint point = new FunctionPoint(leftX + i * step, values[i]);
            addNodeToTail().setPoint(point);
        }
    }
    public LinkedListTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
    if (points.length < 2) {
        throw new IllegalArgumentException("Number of points is less than 2");
    }
    
    // Проверка упорядоченности точек
    for (int i = 1; i < points.length; i++) {
        if (points[i].getX() <= points[i - 1].getX()) {
            throw new IllegalArgumentException("Points are not ordered by X coordinate");
        }
    }
    
    head = new FunctionNode();
    head.setNext(head);
    head.setPrev(head);
    pointsCount = 0;
    
    for (int i = 0; i < points.length; i++) {
        FunctionPoint point = new FunctionPoint(points[i].getX(), points[i].getY());
        addNodeToTail().setPoint(point);
    }
}
    public double getLeftDomainBorder() 
    {
        return head.getNext().getPoint().getX();
    }

    public double getRightDomainBorder() 
    {
        return head.getPrev().getPoint().getX();
    }
     public int getPointsCount() 
    {
        return this.pointsCount;
    }

    public double getFunctionValue(double x)
     {
        double epcilon=1e-9;
        double leftX=getLeftDomainBorder();
        double rightX=getRightDomainBorder();
        if (x < leftX|| x > rightX)
            return Double.NaN;
        if(Math.abs(x-leftX)<epcilon)
        {
            return getNodeByIndex(0).getPoint().getY();
        }
        if(Math.abs(x-rightX)<epcilon)
        {
            return getNodeByIndex(pointsCount-1).getPoint().getY();
        }
        int i = 0;
        double value=0;
        for (i=0;x > getNodeByIndex(i).getPoint().getX();++i)
        {
        value = getNodeByIndex(i).getPoint().getY() + (getNodeByIndex(i+1).getPoint().getY() - getNodeByIndex(i).getPoint().getY()) * (x - getNodeByIndex(i).getPoint().getX()) / (getNodeByIndex(i+1).getPoint().getX() - getNodeByIndex(i).getPoint().getX());
        }
        return value;

    }

    private FunctionNode getNodeByIndex(int index) {
    if (index < 0 || index >= pointsCount) 
        throw new FunctionPointIndexOutOfBoundsException("Invalid index");
    
    FunctionNode current;
    if(index < pointsCount/2) {
        current = head.getNext(); 
        for(int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    } else {
        current = head.getPrev();
        for(int i = pointsCount - 1; i > index; i--) {
            current = current.prev;
        }
        return current;
    }
}
private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode();
        
        
        if (pointsCount == 0) {
            newNode.setNext(head);
            newNode.setPrev(head);
            head.setNext(newNode);
            head.setPrev(newNode);
        } else {
            
            FunctionNode tail = head.getPrev();
            newNode.setPrev(tail);
            newNode.setNext(head);
            tail.setNext(newNode);
            head.setPrev(newNode);
        }
        
        pointsCount++;
        return newNode;
    }

private FunctionNode addNodeByIndex(int index)
{
    FunctionNode Node = getNodeByIndex(index);
    FunctionNode prevNode = Node.getPrev();
    FunctionNode newNode=new FunctionNode();
    newNode.setNext(Node);
    newNode.setPrev(prevNode);
    Node.setPrev(newNode);
    prevNode.setNext(newNode);
    pointsCount++;
    return newNode;
}

private FunctionNode deleteNodeByIndex(int index)
{
    FunctionNode Node = getNodeByIndex(index);
    FunctionNode nextNode = Node.getNext();
    FunctionNode prevNode = Node.getPrev();
    Node.setNext(null);
    Node.setPrev(null);
    pointsCount--;
    prevNode.setNext(nextNode);
    nextNode.setPrev(prevNode);
    return Node;

}

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
        }
        return getNodeByIndex(index).getPoint();
    }

public void setPoint(int index,FunctionPoint point) throws InappropriateFunctionPointException
{
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
        FunctionNode node = getNodeByIndex(index);
        node.setPoint(point);
    }

public double getPointX(int index)
{
    if (0 > index || index >= pointsCount) throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
    return getPoint(index).getX();
}

public void setPointX(int index, double x) throws InappropriateFunctionPointException
{
    double leftX=getLeftDomainBorder();
    double rightX=getRightDomainBorder();
    if (index < 0 || index >= pointsCount)
        {
            throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
        }
        if(leftX > x || rightX < x)
        {
            throw new InappropriateFunctionPointException("x out of boreder");
        }
        FunctionNode node = getNodeByIndex(index);
        node.getPoint().setX(x);

}
public double getPointY(int index)
{
    if (index < 0 || index >= pointsCount)
    {
        throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
    }
    FunctionNode node = getNodeByIndex(index);
    return node.getPoint().getY();
}

public void setPointY(int index, double y)
{
    if (index < 0 || index >= pointsCount)
        {
            throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
        }

    FunctionNode node = getNodeByIndex(index);
    node.getPoint().setY(y);
}

public void deletePoint(int index)
{
    if (index < 0 || index >= pointsCount)
        {
            throw new FunctionPointIndexOutOfBoundsException("out-of-bounds");
        }

    if(pointsCount<3)
    {
        throw new IllegalStateException("The number of points is less than 3");
    }
    
    deleteNodeByIndex(index); 
}

public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
    double epsilon = 1e-9;
    
    // Проверка уникальности X
    FunctionNode current = head.getNext();
    while (current != head) {
        if (Math.abs(current.getPoint().getX() - point.getX()) < epsilon) {
            throw new InappropriateFunctionPointException("There is already such an x");
        }
        current = current.getNext();
    }
    
    // Поиск позиции для вставки
    current = head.getNext();
    int index = 0;
    while (current != head && current.getPoint().getX() < point.getX()) {
        current = current.getNext();
        index++;
    }
    
    // Вставка
    if (current == head) {
        addNodeToTail().setPoint(point);
    } else {
        addNodeByIndex(index).setPoint(point);
    }
}
@Override
public String toString()
{
    StringBuilder list_num =new StringBuilder();
    list_num.append("{");
    FunctionNode current = head.getNext();
    while (current!=head) 
    {
        list_num.append(current.getPoint().toString());
        if(current!=head)
            {
            list_num.append(",");
            }
            current=current.getNext();

    }
    list_num.append("}");
    return list_num.toString();


}

@Override
public boolean equals(Object o) {
    if (this == o) {
        return true;
    }

    if (o == null) {
        return false;
    }

    if (o instanceof LinkedListTabulatedFunction) {
        LinkedListTabulatedFunction one = (LinkedListTabulatedFunction) o;

        if (this.pointsCount != one.pointsCount) {
            return false;
        }

        FunctionNode current = this.head.getNext();
        FunctionNode one_current = one.head.getNext();

        while (current != this.head && one_current != one.head) {
            if (!current.getPoint().equals(one_current.getPoint())) {
                return false;
            }
            current = current.getNext();
            one_current = one_current.getNext();  
        }
        return true;
    } else if (o instanceof TabulatedFunction) {
        TabulatedFunction one = (TabulatedFunction) o;

        if (this.getPointsCount() != one.getPointsCount()) {
            return false;
        }

        
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
        int result = pointsCount;
        for (int i = 0; i < pointsCount; ++i) {
            result ^= getPoint(i).hashCode();
        }
        return result;
    }
     @Override
    public TabulatedFunction clone() {
        try {
            LinkedListTabulatedFunction cloned = (LinkedListTabulatedFunction) super.clone();
            
            // Создаем новую голову для клонированного списка
            cloned.head = new FunctionNode();
            cloned.head.setNext(cloned.head);
            cloned.head.setPrev(cloned.head);
            cloned.pointsCount = 0;
            
            // "Пересобираем" список, копируя точки из исходного
            FunctionNode current = this.head.getNext();
            while (current != this.head) {
                FunctionPoint pointCopy = new FunctionPoint(current.getPoint().getX(), current.getPoint().getY());
                cloned.addNodeToTail().setPoint(pointCopy);
                current = current.getNext();
            }
            
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Клонирование не поддерживается", e);
        }
    }

}