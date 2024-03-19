public class FileNotFoundException extends Exception {

    public FileNotFoundException() {
        super();
    }

    public FileNotFoundException(String s) {
        System.out.println("The file was not found!");
    }

}
