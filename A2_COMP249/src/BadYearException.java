public class BadYearException extends Exception {

    public BadYearException() {
        super("Semantic error: Year is not within 1990-1999");
    }

    public BadYearException(String s) {
        super(s);
    }

}
