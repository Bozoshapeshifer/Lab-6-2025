package functions;

public class InappropriateFunctionPointException extends Exception
{
    private String message;

    public InappropriateFunctionPointException(String message)
    {
        this.message=message;
    }

    public String getMessage()
    {
        return message;
    }
    
}