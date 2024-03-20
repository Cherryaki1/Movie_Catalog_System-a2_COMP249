public class ExcessFieldsException extends Exception {

    public ExcessFieldsException() {
        super("Syntax error: Excess fields in the file");
    }

    public ExcessFieldsException(String s) {
        super(s);
    }

}
