public class BadGenreException extends Exception {

    public BadGenreException() {
        super("Semantic error: Genre is not valid");
    }

    public BadGenreException(String s) {
        super(s);
    }

}
