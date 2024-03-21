import java.io.IOException;
import java.io.FileNotFoundException;

import java.io.FileInputStream;
import java.util.Scanner;

import java.io.FileOutputStream;
import java.io.BufferedWriter;

public class Driver {

    public static void main(String[] args) {

        // part 1’s manifest file
        String part1_manifest = "part1_manifest.txt";
        do_part1(part1_manifest);

        // // part 2’s manifest file
        // String part2_manifest = do_part1(part1_manifest /* , ... */); // partition

        // // part 3’s manifest file
        // String part3_manifest = do_part2(part2_manifest /* , ... */); // serialize
        // do_part3(part3_manifest /* , ... */); // deserialize
        // // and navigate
        // return;

    }

    // ------------------------------------OUTSIDE OF MAIN METHOD------------------------------------

    // ------------------------------------
    // Syntax Errors checker
    // ------------------------------------

    // 1. Missing quotes:
    public static boolean validQuoteAmount(String movieInfo) { // Double quotes always comes in pairs of two
        int quoteCount = 0;
        for (int i = 0; i < movieInfo.length(); i++) {
            if (movieInfo.charAt(i) == '\"') {
                quoteCount++;
            }
        }
        return quoteCount % 2 == 0;
    }

    // 2. Excess/missing fields:
    public static boolean validFieldAmount(String movieInfo) { // Ignore commas inside quotes
        int quoteCount = 0;
        int commaCount = 0;
        for (int i = 0; i < movieInfo.length(); i++) {
            if (movieInfo.charAt(i) == '\"') {
                quoteCount++;
            }
            if (movieInfo.charAt(i) == ',' && quoteCount % 2 == 0) {
                commaCount++;
            }
        }
        return commaCount == 9;
    }

    // Movie info syntax checker
    public static boolean validMovieInfoSyntax(String movieInfo) {
        return validQuoteAmount(movieInfo) && validFieldAmount(movieInfo);
    }

    // -----------------------------------------------
    // Semantic Errors checker
    // -----------------------------------------------

    // Check if title is between quotes
    public static boolean quotedTitle(String movieInfo) {
        String[] movieInfoArray = movieInfo.split(",");
        /* If there are quotes at the start and end of the title, they would appear in
         * the first and second index since the comma split would split the title into two parts */
        if (movieInfoArray[1].startsWith("\"") && movieInfoArray[2].endsWith("\""))
            ;
        {
            return true;
        }
    }

    // Movie info spliter
    public static String[] movieInfoSplitter(String movieInfo) {
        if (quotedTitle(movieInfo)) {
            String[] titleSplit = movieInfo.split("\"");
            String[] remainingSplit = titleSplit[2].substring(1, titleSplit[2].length()).split(",");
            String[] movieInfoArray = new String[10];
            movieInfoArray[0] = titleSplit[0].substring(0, titleSplit[0].length() - 1);
            movieInfoArray[1] = titleSplit[1];
            for (int i = 2; i < movieInfoArray.length; i++) {
                movieInfoArray[i] = remainingSplit[i - 2];
            }
            return movieInfoArray;
        } else {
            String[] movieInfoArray = movieInfo.split(",");
            return movieInfoArray;
        }
    }

    // 1. Year
    public static boolean validYear(String movieInfo) {
        if (quotedTitle(movieInfo)) {
            String[] titleSplit = movieInfo.split("\"");
            String[] remainingSplit = titleSplit[2].substring(1, titleSplit[2].length()).split(",");
            String[] movieInfoArray = new String[10];
            movieInfoArray[0] = titleSplit[0].substring(0, titleSplit[0].length() - 1);
            movieInfoArray[1] = titleSplit[1];
            for (int i = 2; i < movieInfoArray.length; i++) {
                movieInfoArray[i] = remainingSplit[i - 2];
            }
            return (Integer.parseInt(movieInfoArray[0]) >= 1990 && Integer.parseInt(movieInfoArray[0]) <= 1999);
        } else {
            String[] movieInfoArray = movieInfo.split(",");
            return (Integer.parseInt(movieInfoArray[0]) >= 1990 && Integer.parseInt(movieInfoArray[0]) <= 1999);
        }
    }

    // 2. Duration
    public static boolean validDuration(String movieInfo) {
        if (quotedTitle(movieInfo)) {
            String[] titleSplit = movieInfo.split("\"");
            String[] remainingSplit = titleSplit[2].substring(1, titleSplit[2].length()).split(",");
            String[] movieInfoArray = new String[10];
            movieInfoArray[0] = titleSplit[0].substring(0, titleSplit[0].length() - 1);
            ;
            movieInfoArray[1] = titleSplit[1];
            for (int i = 2; i < movieInfoArray.length; i++) {
                movieInfoArray[i] = remainingSplit[i - 2];
            }
            return (Integer.parseInt(movieInfoArray[2]) >= 30 && Integer.parseInt(movieInfoArray[2]) <= 300);
        } else {
            String[] movieInfoArray = movieInfo.split(",");
            return (Integer.parseInt(movieInfoArray[2]) >= 30 && Integer.parseInt(movieInfoArray[2]) <= 300);
        }
    }

    // 3. Score
    public static boolean validScore(String movieInfo) {
        if (quotedTitle(movieInfo)) {
            String[] titleSplit = movieInfo.split("\"");
            String[] remainingSplit = titleSplit[2].substring(1, titleSplit[2].length()).split(",");
            String[] movieInfoArray = new String[10];
            movieInfoArray[0] = titleSplit[0].substring(0, titleSplit[0].length() - 1);
            ;
            movieInfoArray[1] = titleSplit[1];
            for (int i = 2; i < movieInfoArray.length; i++) {
                movieInfoArray[i] = remainingSplit[i - 2];
            }
            return (Double.parseDouble(movieInfoArray[5]) >= 0 && Double.parseDouble(movieInfoArray[5]) <= 10);
        } else {
            String[] movieInfoArray = movieInfo.split(",");
            return (Double.parseDouble(movieInfoArray[5]) >= 0 && Double.parseDouble(movieInfoArray[5]) <= 10);
        }
    }

    // 4. Rating
    public static boolean validRating(String movieInfo) {
        if (quotedTitle(movieInfo)) {
            String[] titleSplit = movieInfo.split("\"");
            String[] remainingSplit = titleSplit[2].substring(1, titleSplit[2].length()).split(",");
            String[] movieInfoArray = new String[10];
            movieInfoArray[0] = titleSplit[0].substring(0, titleSplit[0].length() - 1);
            ;
            movieInfoArray[1] = titleSplit[1];
            for (int i = 2; i < movieInfoArray.length; i++) {
                movieInfoArray[i] = remainingSplit[i - 2];
            }
            return ((movieInfoArray[4]).equals("PG") || (movieInfoArray[4]).equals("Unrated")
                    || (movieInfoArray[4]).equals("G") || (movieInfoArray[4]).equals("R")
                    || (movieInfoArray[4]).equals("PG-13") || (movieInfoArray[4]).equals("NC-17"));
        } else {
            String[] movieInfoArray = movieInfo.split(",");
            return ((movieInfoArray[4]).equals("PG") || (movieInfoArray[4]).equals("Unrated")
                    || (movieInfoArray[4]).equals("G") || (movieInfoArray[4]).equals("R")
                    || (movieInfoArray[4]).equals("PG-13") || (movieInfoArray[4]).equals("NC-17"));
        }
    }

    // 5. Genres
    public static boolean validGenres(String movieInfo) {
        if (quotedTitle(movieInfo)) {
            String[] titleSplit = movieInfo.split("\"");
            String[] remainingSplit = titleSplit[2].substring(1, titleSplit[2].length()).split(",");
            String[] movieInfoArray = new String[10];
            movieInfoArray[0] = titleSplit[0].substring(0, titleSplit[0].length() - 1);
            ;
            movieInfoArray[1] = titleSplit[1];
            for (int i = 2; i < movieInfoArray.length; i++) {
                movieInfoArray[i] = remainingSplit[i - 2];
            }
            return ((movieInfoArray[3]).equals("Action") || (movieInfoArray[3]).equals("Adventure")
                    || (movieInfoArray[3]).equals("Animation") || (movieInfoArray[3]).equals("Biography")
                    || (movieInfoArray[3]).equals("Comedy") || (movieInfoArray[3]).equals("Crime")
                    || (movieInfoArray[3]).equals("Drama") || (movieInfoArray[3]).equals("Family")
                    || (movieInfoArray[3]).equals("Fantasy") || (movieInfoArray[3]).equals("History")
                    || (movieInfoArray[3]).equals("Horror") || (movieInfoArray[3]).equals("Music")
                    || (movieInfoArray[3]).equals("Musical") || (movieInfoArray[3]).equals("Mystery")
                    || (movieInfoArray[3]).equals("Romance") || (movieInfoArray[3]).equals("Sci-Fi")
                    || (movieInfoArray[3]).equals("Sport") || (movieInfoArray[3]).equals("Thriller")
                    || (movieInfoArray[3]).equals("War") || (movieInfoArray[3]).equals("Western"));
        } else {
            String[] movieInfoArray = movieInfo.split(",");
            return ((movieInfoArray[3]).equals("Action") || (movieInfoArray[3]).equals("Adventure")
                    || (movieInfoArray[3]).equals("Animation") || (movieInfoArray[3]).equals("Biography")
                    || (movieInfoArray[3]).equals("Comedy") || (movieInfoArray[3]).equals("Crime")
                    || (movieInfoArray[3]).equals("Drama") || (movieInfoArray[3]).equals("Family")
                    || (movieInfoArray[3]).equals("Fantasy") || (movieInfoArray[3]).equals("History")
                    || (movieInfoArray[3]).equals("Horror") || (movieInfoArray[3]).equals("Music")
                    || (movieInfoArray[3]).equals("Musical") || (movieInfoArray[3]).equals("Mystery")
                    || (movieInfoArray[3]).equals("Romance") || (movieInfoArray[3]).equals("Sci-Fi")
                    || (movieInfoArray[3]).equals("Sport") || (movieInfoArray[3]).equals("Thriller")
                    || (movieInfoArray[3]).equals("War") || (movieInfoArray[3]).equals("Western"));
        }
    }

    // Movie info semantic checker
    public static boolean validMovieInfoSemantics(String movieInfo) {
        return validYear(movieInfo) && validDuration(movieInfo) && validScore(movieInfo)
                && validRating(movieInfo) && validGenres(movieInfo);
    }

    // MOVIE VALIDATOR
    public static boolean validMovie(String movieInfo) {
        return validMovieInfoSyntax(movieInfo) && validMovieInfoSemantics(movieInfo);
    }

    // ----------------------
    // do_part1 Method
    // ----------------------

    public static void do_part1(String part1_manifest) {
        Scanner reader = null;
        try {
            reader = new Scanner(new FileInputStream(part1_manifest));
            String line = reader.nextLine();
            while (reader.hasNextLine()) {
                line += "\n" + reader.nextLine();
            }

            String[] fileNames = line.split("\n");
            
            try {
                reader = new Scanner(new FileInputStream(fileNames[0]));
                String movieInfo = reader.nextLine();
                while (reader.hasNextLine()) {
                    movieInfo += "\n" + reader.nextLine();
                }
                System.out.println(movieInfo);

            }
            catch (IOException e) {
                e.getMessage();
            }

        }
        catch (FileNotFoundException e) {
            e.getMessage();
        }
    }



}
