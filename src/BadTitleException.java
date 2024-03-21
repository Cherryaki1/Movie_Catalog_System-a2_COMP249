public class BadTitleException extends Exception {

    public BadTitleException() {
        super("Semantic error: Title is not valid");
    }

    public BadTitleException(String s) {
        super(s);
    }

}
