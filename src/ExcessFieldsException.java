public class ExcessFieldsException extends Exception {

    public ExcessFieldsException() {
        super("Syntax error: Excess fields in the movie information line");
    }

    public ExcessFieldsException(String s) {
        super(s);
    }

}
