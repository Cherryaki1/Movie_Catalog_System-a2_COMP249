public class BadNameException extends Exception {

    public BadNameException() {
        super("Semantic error: Name is not valid");
    }

    public BadNameException(String s) {
        super(s);
    }
    
}
