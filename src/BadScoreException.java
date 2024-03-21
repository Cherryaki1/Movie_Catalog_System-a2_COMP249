public class BadScoreException extends Exception {

    public BadScoreException() {
        super("Semantic error: Score is not valid");
    }

    public BadScoreException(String s) {
        super(s);
    }
}
