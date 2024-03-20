public class MissingQuotesException extends Exception {

    public MissingQuotesException() {
        super("Syntax error: Quotes are missing");
    }

    public MissingQuotesException(String s) {
        super(s);
    }
    
}
