package manager;

public class StartTimeOverlapException extends IllegalArgumentException{
    public StartTimeOverlapException(String message) {
        super(message);
    }
}
