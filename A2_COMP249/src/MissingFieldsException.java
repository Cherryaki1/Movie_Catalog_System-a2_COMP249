public class MissingFieldsException extends Exception {

    public MissingFieldsException() {
        super("Syntax error: One or more fields are missing");
    }

    public MissingFieldsException(String s) {
        super(s);
    }

}
