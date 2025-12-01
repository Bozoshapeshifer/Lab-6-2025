package functions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException 
{
    private String message;

    public FunctionPointIndexOutOfBoundsException(String message)
    {
        this.message=message;
    }

    public String getMessage()
    {
        return message;
    }
    
}