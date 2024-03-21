public class BadDurationException extends Exception {

    public BadDurationException() {
        super("Semantic error: Duration is not valid");
    }

    public BadDurationException(String s) {
        super(s);
    }
    
}
