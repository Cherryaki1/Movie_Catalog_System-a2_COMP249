public class App {
    
    public static void main(String[] args) {

        String s = "Wonderful Day";
        System.out.println(reverse(s));
    
        

    }

    public static String reverse(String s) {
            if (s.length() == 0) {
                return s;
            } else {
                return reverse(s.substring(1)) + s.charAt(0);
            }
        }
}
