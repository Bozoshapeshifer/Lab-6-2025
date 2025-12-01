package functions.threads;

import functions.Function;

public class Task
{
    private Function function;
    private double leftBorder;
    private double rightBorder;
    private double step;
    private int taskCount;
    

    public Task()
    {
        this.taskCount = 0;
    }
    
    public Task(int taskCount) 
    {
        this.taskCount = taskCount;
    }
     public Function getFunction() {
        return function;
    }
    
    public double getLeftBorder() {
        return leftBorder;
    }
    
    public double getRightBorder() {
        return rightBorder;
    }
    
    public double getStep() {
        return step;
    }
    
    public int getTaskCount() {
        return taskCount;
    }
    
    public void setFunction(Function function) {
        this.function = function;
    }
    
    public void setLeftBorder(double leftBorder) {
        this.leftBorder = leftBorder;
    }
    
    public void setRightBorder(double rightBorder) {
        this.rightBorder = rightBorder;
    }
    
    public void setStep(double step) {
        this.step = step;
    }
    
    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }
}
