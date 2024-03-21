public class BadRatingException extends Exception {

    public BadRatingException() {
        super("Semantic error: Rating is not valid");
    }

    public BadRatingException(String s) {
        super(s);
    }
    
}
