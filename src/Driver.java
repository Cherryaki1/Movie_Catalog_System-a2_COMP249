// -----------------------------------------------------
// Assignment 2: Movie Database
// Question: (include question/part number, if applicable)
// Written by: Botao Yang (40213554)
// -----------------------------------------------------

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.EOFException;
import java.io.FileInputStream;
import java.util.Scanner;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

public class Driver {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // part 1’s manifest file
        String part1_manifest = "part1_manifest.txt";

        // part 2’s manifest file
        String part2_manifest = do_part1(part1_manifest); // partition

        // part 3’s manifest file
        String part3_manifest = do_part2(part2_manifest); // serialize
        do_part3(part3_manifest); // deserialize


int genreSelector = 0;
int currentPosition = 0;
int oldPosition = 0;
String choice = "";
Movie[][] allMovies = do_part3(part3_manifest);

while (!choice.equals("x")) {
    displayMainMenu(allMovies, genreSelector);
    choice = sc.nextLine();

    if (choice.equals("s")) {
        displayGenreSubMenu();
        try {
            genreSelector = sc.nextInt();
            if (genreSelector < 0 || genreSelector > 16) {
                System.out.println("Invalid input");
                continue;
            }
        } catch (Exception e) {
            System.out.println("Invalid input");
            sc.next();
        }
    } else if (choice.equals("n")) {
        if (allMovies[genreSelector].length == 0) {
            System.out.println("No movies in this genre");
            continue;
        }
        
        int navigate = 0;
        do {
            System.out.println("Enter your choice: ");
            navigate = sc.nextInt();
            System.out.println();
            if (navigate < 0) {
                navigate = Math.abs(navigate);
                if (currentPosition - (navigate - 1) <= 0) {
                    System.out.println("BOF has been reached");
                    currentPosition = 0;
                } else {
                    currentPosition -= navigate;
                }
            } else if (navigate > 0) { 
                navigate = Math.abs(navigate);
                if (currentPosition + (navigate - 1) >= allMovies[genreSelector].length - 1) {
                    System.out.println("EOF has been reached");
                    currentPosition = allMovies[genreSelector].length - 1;
                } else {
                    currentPosition += navigate;
                }
            }
        } while (navigate != 0);
    }
}
System.out.println("Exiting program...");
sc.close();

        
        


        // // and navigate
        // return;

    } // end of main method

    // ---------------------------------- OUTSIDE OF MAIN METHOD ----------------------------------

    // Sections:
    // 1. Syntax Errors checker
    // 2. Movie info spliter
    // 3. Semantic Errors checker
    // 4. do_part1 Method
    // 5. do_part2 Method
    // 6. Get number of records for each genre
    // 7. Display Main Menu
    // 8. Display Genre Sub-Menu
    // 9. do_part3 Method

    // --------------------------------------------------------------------------------------------
    //                                   1. Syntax Errors checker
    // --------------------------------------------------------------------------------------------

    // 1.1 Check for valid quote amount
    public static boolean validQuoteAmount(String movieInfo) {

        try {
            int quoteCount = 0;
            for (int i = 0; i < movieInfo.length(); i++) {
                if (movieInfo.charAt(i) == '\"') {
                    quoteCount++;
                }
            }
            if (quoteCount % 2 != 0) {
                throw new MissingQuotesException();
            } else {
                return true;
            }
        } catch (MissingQuotesException e) {
            e.getMessage();
            return false;
        }
    } // end of validateQuoteAmount

    // 1.2 Check if we have excess/missing fields:
    public static boolean validFieldAmount(String movieInfo) {

        try {
            int quoteCount = 0;
            int commaCount = 0;
            for (int i = 0; i < movieInfo.length(); i++) { // Ignore commas inside quotes
                if (movieInfo.charAt(i) == '\"') {
                    quoteCount++;
                }
                if (movieInfo.charAt(i) == ',' && quoteCount % 2 == 0) {
                    commaCount++;
                }
            }
            if (commaCount == 9) {
                return true;
            } else if (commaCount > 9) {
                throw new ExcessFieldsException();
            } else {
                throw new MissingFieldsException();
            }
        } catch (ExcessFieldsException e) {
            e.getMessage();
            return false;
        } catch (MissingFieldsException e) {
            e.getMessage();
            return false;
        }
    } // end of validFieldAmount

    // --------------------------------------------------------------------------------------------
    //                                2. Movie info spliter
    // --------------------------------------------------------------------------------------------

    public static String[] movieInfoSplit(String movieInfo) {

        String[] result = new String[10];
        boolean inQuotes = false;
        String current = "";
        int index = 0;
        try {
            for (int i = 0; i < movieInfo.length(); i++) {
                char c = movieInfo.charAt(i);
                switch (c) {
                    case ',':
                        if (inQuotes) {
                            current += c;
                        } else {
                            if (current.startsWith("\"") && current.endsWith("\"")) {
                                current = current.substring(1, current.length() - 1);
                            }
                            result[index++] = current;
                            current = "";
                        }
                        break;
                    case '\"':
                        inQuotes = !inQuotes;
                    default:
                        current += c;
                        break;
                }
            }
            if (current.startsWith("\"") && current.endsWith("\"")) {
                current = current.substring(1, current.length() - 1);
            }
            if (index < result.length) {
                result[index] = current;
            } else {
                throw new ExcessFieldsException();
            }
    
            // Check for missing or excess fields
            if (index < 9) {
                throw new MissingFieldsException();
            } else if (index > 9) {
                throw new ExcessFieldsException();
            }
    
            // Check for missing quotes
            if (inQuotes) {
                throw new MissingQuotesException();
            }
    
            return result;
        } catch (MissingFieldsException e) {
            e.getMessage();
            return null;
        } catch (ExcessFieldsException e) {
            e.getMessage();
            return null;
        } catch (MissingQuotesException e) {
            e.getMessage();
            return null;
        }
    }
        
    
    // --------------------------------------------------------------------------------------------
    //                                 3. Semantic Errors checker
    // --------------------------------------------------------------------------------------------

    // 3.1 Year checker
    public static boolean validYear(String movieInfo) {

        try {
            String[] splitMovieInfo = movieInfoSplit(movieInfo);
            if (splitMovieInfo == null) {
                throw new BadYearException("Invalid movie info.");
            }
            try {
                // check for empty string
                if (splitMovieInfo[0].equals("")) {
                    throw new BadYearException("You have an empty string for the year.");
                }
                if (Integer.parseInt(splitMovieInfo[0]) >= 1990
                        && Integer.parseInt(splitMovieInfo[0]) <= 1999) {
                    return true;
                } else {
                    throw new BadYearException();
                }
            } catch (NumberFormatException e) {
                throw new BadYearException();
            }
        } catch (BadYearException e) {
            e.getMessage();
            return false;
        }
    }


    // 3.2 Duration checker
    public static boolean validDuration(String movieInfo) {
        try {
            String[] splitMovieInfo = movieInfoSplit(movieInfo);
            if (splitMovieInfo == null || splitMovieInfo[2].equals("")) {
                // Handle the case where movieInfoSplit returns null or the duration is an empty string
                return false;
            }
            int duration = Integer.parseInt(splitMovieInfo[2]);
            if (duration >= 0) {
                return true;
            } else {
                throw new BadDurationException();
            }
        } catch (NumberFormatException | BadDurationException e) {
            e.getMessage();
            return false;
        }
    }
   

    // 3.3 Score checker
    public static boolean validScore(String movieInfo) {
        try {
            String[] splitMovieInfo = movieInfoSplit(movieInfo);
            if (splitMovieInfo == null || splitMovieInfo[5].equals("")) {
                // Handle the case where movieInfoSplit returns null or the score is an empty string
                return false;
            }
            double score = Double.parseDouble(splitMovieInfo[5]);
            if (score >= 0.0 && score <= 10.0) {
                return true;
            } else {
                throw new BadScoreException();
            }
        } catch (NumberFormatException | BadScoreException e) {
            e.getMessage();
            return false;
        }
    }

    // 3.4 Rating checker
    public static boolean validRating(String movieInfo) {
        try {
            String[] splitMovieInfo = movieInfoSplit(movieInfo);
            if (splitMovieInfo == null) {
                // Handle the case where movieInfoSplit returns null
                return false;
            }
            if (splitMovieInfo[4].equals("")) {
                throw new BadRatingException("You have an empty string for the rating.");
            }
            if (splitMovieInfo[4].equals("G") || splitMovieInfo[4].equals("PG") || splitMovieInfo[4].equals("PG-13") 
            || splitMovieInfo[4].equals("R") || splitMovieInfo[4].equals("NC-17")) {
                return true;
            } else {
                throw new BadRatingException();
            }
        } catch (BadRatingException e) {
            e.getMessage();
            return false;
        } 
    }

    // 3.5 Genres checker
    public static boolean validGenres(String movieInfo) {
        try {
            String[] splitMovieInfo = movieInfoSplit(movieInfo);
            if (splitMovieInfo == null) {
                // Handle the case where movieInfoSplit returns null
                return false;
            }
            if (splitMovieInfo[3].equals("")) {
                throw new BadGenreException("You have an empty string for the genre.");
            }
            splitMovieInfo[3] = splitMovieInfo[3].substring(0, 1).toUpperCase() + splitMovieInfo[3].substring(1);
    
            if (splitMovieInfo[3].equals("Musical") || splitMovieInfo[3].equals("Comedy") 
            || splitMovieInfo[3].equals("Animation") || splitMovieInfo[3].equals("Adventure") 
            || splitMovieInfo[3].equals("Drama") || splitMovieInfo[3].equals("Crime") 
            || splitMovieInfo[3].equals("Biography") || splitMovieInfo[3].equals("Horror") 
            || splitMovieInfo[3].equals("Action") || splitMovieInfo[3].equals("Documentary") 
            || splitMovieInfo[3].equals("Fantasy") || splitMovieInfo[3].equals("Mystery") 
            || splitMovieInfo[3].equals("Sci-fi") || splitMovieInfo[3].equals("Family") 
            || splitMovieInfo[3].equals("Western") || splitMovieInfo[3].equals("Romance") 
            || splitMovieInfo[3].equals("Thriller")) {
                return true;
            } 
            else {
                throw new BadGenreException();
            }
        } catch (BadGenreException e) {
            e.getMessage();
            return false;
        }
    }

    // --------------------------------------------------------------------------------------------
    //                                4. do_part1 Method
    // --------------------------------------------------------------------------------------------

    public static String do_part1(String part1_manifest) { // do_part1 method

        Scanner reader = null;
        String[] fileNames = null;
        // 4.1 Read the manifest file:
        try {
            reader = new Scanner(new FileInputStream(part1_manifest));
            String line = reader.nextLine();
            while (reader.hasNextLine()) {
                line += "\n" + reader.nextLine();
            }
            fileNames = line.split("\n"); // Split each manifest file name into an array

            // ---------------------------------- File Writers ----------------------------------
            PrintWriter wrongPrintWriter = new PrintWriter(new FileWriter("bad_movie_records.txt", false)); // Append to the file
            PrintWriter writerToMusicalCSV = new PrintWriter(new FileWriter("Musical.csv", false));
            PrintWriter writerToComedyCSV = new PrintWriter(new FileWriter("Comedy.csv", false));
            PrintWriter writerToAnimationCSV = new PrintWriter(new FileWriter("Animation.csv", false));
            PrintWriter writerToAdventureCSV = new PrintWriter(new FileWriter("Adventure.csv", false));
            PrintWriter writerToDramaCSV = new PrintWriter(new FileWriter("Drama.csv", false));
            PrintWriter writerToCrimeCSV = new PrintWriter(new FileWriter("Crime.csv", false));
            PrintWriter writerToBiographyCSV = new PrintWriter(new FileWriter("Biography.csv", false));
            PrintWriter writerToHorrorCSV = new PrintWriter(new FileWriter("Horror.csv", false));
            PrintWriter writerToActionCSV = new PrintWriter(new FileWriter("Action.csv", false));
            PrintWriter writerToDocumentaryCSV = new PrintWriter(new FileWriter("Documentary.csv", false));
            PrintWriter writerToFantasyCSV = new PrintWriter(new FileWriter("Fantasy.csv", false));
            PrintWriter writerToMysteryCSV = new PrintWriter(new FileWriter("Mystery.csv", false));
            PrintWriter writerToSciFiCSV = new PrintWriter(new FileWriter("Sci-fi.csv", false));
            PrintWriter writerToFamilyCSV = new PrintWriter(new FileWriter("Family.csv", false));
            PrintWriter writerToWesternCSV = new PrintWriter(new FileWriter("Western.csv", false));
            PrintWriter writerToRomanceCSV = new PrintWriter(new FileWriter("Romance.csv", false));
            PrintWriter writerToThrillerCSV = new PrintWriter(new FileWriter("Thriller.csv", false));
            PrintWriter writerPart2Manifest = new PrintWriter(new FileWriter("part2_manifest.txt", false));
            // -----------------------------------------------------------------------------------
            // Files writers must be outside the double for loop since we only want to create them once


            // 4.2 Read files inside manifest file:
            for (int i = 0; i < fileNames.length; i++) {

                reader = new Scanner(new FileInputStream(fileNames[i]));
                String movies = reader.nextLine(); // Set first file to line 1 to avoid blanc line 1
                while (reader.hasNextLine()) {
                    movies += "\n" + reader.nextLine(); // Follow up with the remaining files each on their own line
                }

                // Split each movie into an index of an array so we can validate each movie individually
                String[] movieArray = movies.split("\n");

                try {

                    for (int j = 0; j < movieArray.length; j++) { 

                        // 4.3 Check for bad movie records:

                        if (!validQuoteAmount(movieArray[j])) { // We try catching Syntax and Semantic errors first
                            wrongPrintWriter.println("Syntax Error: Invalid quote amount at line " + (j + 1) + " in file "
                                    + fileNames[i] + ". The movie info is:\n" + movieArray[j] + "\n");
                        } 
                        else if (!validFieldAmount(movieArray[j])) {
                            wrongPrintWriter.println("Syntax Error: Invalid field amount at line " + (j + 1) + " in file "
                                    + fileNames[i] + ". The movie info is:\n" + movieArray[j] + "\n");
                        } else { // If we pass the Syntax errors, we check for Semantic errors
                            if (!validYear(movieArray[j])) {
                                wrongPrintWriter.println("Semantic Error: Invalid year at line " + (j + 1) + " in file "
                                        + fileNames[i] + ". The movie info is:\n" + movieArray[j] + "\n");
                            }
                            if (!validDuration(movieArray[j])) {
                                wrongPrintWriter.println("Semantic Error: Invalid duration at line " + (j + 1) + " in file "
                                        + fileNames[i] + ". The movie info is:\n" + movieArray[j] + "\n");
                            }
                            if (!validScore(movieArray[j])) {
                                wrongPrintWriter.println("Semantic Error: Invalid score at line " + (j + 1) + " in file "
                                        + fileNames[i] + ". The movie info is: \n" + movieArray[j] + "\n");
                            }
                            if (!validRating(movieArray[j])) {
                                wrongPrintWriter.println("Semantic Error: Invalid rating at line " + (j + 1) + " in file "
                                        + fileNames[i] + ". The movie info is: \n" + movieArray[j] + "\n");
                            }
                            if (!validGenres(movieArray[j])) {
                                wrongPrintWriter.println("Semantic Error: Invalid genre at line " + (j + 1) + " in file "
                                        + fileNames[i] + ". The movie info is: \n" + movieArray[j] + "\n");
                            }
                        }

                        // 4.4 If no errors, write to respective movie genre csv file

                        if (validQuoteAmount(movieArray[j]) && validFieldAmount(movieArray[j]) && validYear(movieArray[j])
                                && validDuration(movieArray[j]) && validScore(movieArray[j]) && validRating(movieArray[j])
                                && validGenres(movieArray[j])){
                            String[] splitMovieInfo = movieInfoSplit(movieArray[j]);
                            splitMovieInfo[3] = splitMovieInfo[3].substring(0, 1).toUpperCase() + splitMovieInfo[3].substring(1);
                            String genre = splitMovieInfo[3];
                            switch (genre) {
                                case "Musical":
                                    writerToMusicalCSV.println(movieArray[j]);
                                    break;
                                case "Comedy":
                                    writerToComedyCSV.println(movieArray[j]);
                                    break;
                                case "Animation":
                                    writerToAnimationCSV.println(movieArray[j]);
                                    break;
                                case "Adventure":
                                    writerToAdventureCSV.println(movieArray[j]);
                                    break;
                                case "Drama":
                                    writerToDramaCSV.println(movieArray[j]);
                                    break;
                                case "Crime":
                                    writerToCrimeCSV.println(movieArray[j]);
                                    break;
                                case "Biography":
                                    writerToBiographyCSV.println(movieArray[j]);
                                    break;
                                case "Horror":
                                    writerToHorrorCSV.println(movieArray[j]);
                                    break;
                                case "Action":
                                    writerToActionCSV.println(movieArray[j]);
                                    break;
                                case "Documentary":
                                    writerToDocumentaryCSV.println(movieArray[j]);
                                    break;
                                case "Fantasy":
                                    writerToFantasyCSV.println(movieArray[j]);
                                    break;
                                case "Mystery":
                                    writerToMysteryCSV.println(movieArray[j]);
                                    break;
                                case "Sci-fi":
                                    writerToSciFiCSV.println(movieArray[j]);
                                    break;
                                case "Family":
                                    writerToFamilyCSV.println(movieArray[j]);
                                    break;
                                case "Western":
                                    writerToWesternCSV.println(movieArray[j]);
                                    break;
                                case "Romance":
                                    writerToRomanceCSV.println(movieArray[j]);
                                    break;
                                case "Thriller":
                                    writerToThrillerCSV.println(movieArray[j]);
                                    break;
                            }
                        
                        }
                    }
                }

                catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        reader.close();
                    }

                }

            }

            // 4.5 Write to part 2’s manifest file all the genre names
            writerPart2Manifest.println("Musical.csv");
            writerPart2Manifest.println("Comedy.csv");
            writerPart2Manifest.println("Animation.csv");
            writerPart2Manifest.println("Adventure.csv");
            writerPart2Manifest.println("Drama.csv");
            writerPart2Manifest.println("Crime.csv");
            writerPart2Manifest.println("Biography.csv");
            writerPart2Manifest.println("Horror.csv");
            writerPart2Manifest.println("Action.csv");
            writerPart2Manifest.println("Documentary.csv");
            writerPart2Manifest.println("Fantasy.csv");
            writerPart2Manifest.println("Mystery.csv");
            writerPart2Manifest.println("Sci-fi.csv");
            writerPart2Manifest.println("Family.csv");
            writerPart2Manifest.println("Western.csv");
            writerPart2Manifest.println("Romance.csv");
            writerPart2Manifest.println("Thriller.csv");

            // 4.6 Close all writers
            wrongPrintWriter.close();
            writerToMusicalCSV.close();
            writerToComedyCSV.close();
            writerToAnimationCSV.close();
            writerToAdventureCSV.close();
            writerToDramaCSV.close();
            writerToCrimeCSV.close();
            writerToBiographyCSV.close();
            writerToHorrorCSV.close();
            writerToActionCSV.close();
            writerToDocumentaryCSV.close();
            writerToFantasyCSV.close();
            writerToMysteryCSV.close();
            writerToSciFiCSV.close();
            writerToFamilyCSV.close();
            writerToWesternCSV.close();
            writerToRomanceCSV.close();
            writerToThrillerCSV.close();
            writerPart2Manifest.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return "part2_manifest.txt";
    } // end of do_part1 method

    // --------------------------------------------------------------------------------------------
    //                                5. do_part2 Method
    // --------------------------------------------------------------------------------------------

    public static String do_part2(String part2_manifest) {

        Scanner reader = null;
        String[] fileNames = null;
        ObjectOutputStream oos = null;
        
        // 5.1 Read the manifest file:
        try {
            reader = new Scanner(new FileInputStream(part2_manifest));
            String line = reader.nextLine();
            while (reader.hasNextLine()) {
                line += "\n" + reader.nextLine();
            }
            fileNames = line.split("\n"); // Split each manifest file name into an array

        } catch (FileNotFoundException e) {
            e.getMessage();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        Movie[] musicalMovies = null;
        Movie[] comedyMovies = null;
        Movie[] animationMovies = null;
        Movie[] adventureMovies = null;
        Movie[] dramaMovies = null;
        Movie[] crimeMovies = null;
        Movie[] biographyMovies = null;
        Movie[] horrorMovies = null;
        Movie[] actionMovies = null;
        Movie[] documentaryMovies = null;
        Movie[] fantasyMovies = null;
        Movie[] mysteryMovies = null;
        Movie[] sciFiMovies = null;
        Movie[] familyMovies = null;
        Movie[] westernMovies = null;
        Movie[] romanceMovies = null;
        Movie[] thrillerMovies = null;

        // 5.2 Load the movies from the csv files into the respective genre array of Movie objects

        try {
            for (int i = 0; i < fileNames.length; i++) { // Loop through each file in the manifest file
                reader = new Scanner(new FileInputStream(fileNames[i]));

                // First pass: count the lines
                int count = 0;
                while (reader.hasNextLine()) {
                    reader.nextLine();
                    count++;
                }
                reader.close();
                

                Movie[] tempMovies = new Movie[count]; // Temporary array to store the movies

                // Second pass: populate the array
                reader = new Scanner(new FileInputStream(fileNames[i]));
                for (int j = 0; j < count; j++) {
                    String movieInfo = reader.nextLine();
                    String[] currentMovie = movieInfoSplit(movieInfo);
                    tempMovies[j] = new Movie(Integer.parseInt(currentMovie[0]), currentMovie[1],
                            Integer.parseInt(currentMovie[2]), currentMovie[3], currentMovie[4],
                            Double.parseDouble(currentMovie[5]), currentMovie[6], currentMovie[7],
                            currentMovie[8], currentMovie[9]);
                }
                reader.close();

                switch (fileNames[i]) { // Store the movies in the respective genre array
                    case "Musical.csv":
                        musicalMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            musicalMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Comedy.csv":
                        comedyMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            comedyMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Animation.csv":
                        animationMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            animationMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Adventure.csv":
                        adventureMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            adventureMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Drama.csv":
                        dramaMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            dramaMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Crime.csv":
                        crimeMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            crimeMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Biography.csv":
                        biographyMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            biographyMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Horror.csv":
                        horrorMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            horrorMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Action.csv":
                        actionMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            actionMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Documentary.csv":
                        documentaryMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            documentaryMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Fantasy.csv":
                        fantasyMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            fantasyMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Mystery.csv":
                        mysteryMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            mysteryMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Sci-fi.csv":  
                        sciFiMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            sciFiMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Family.csv":
                        familyMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            familyMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Western.csv":
                        westernMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            westernMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Romance.csv":
                        romanceMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            romanceMovies[k] = tempMovies[k];
                        }
                        break;
                    case "Thriller.csv":
                        thrillerMovies = new Movie[count];
                        for (int k = 0; k < tempMovies.length; k++) {
                            thrillerMovies[k] = tempMovies[k];
                        }
                        break;
                }

            }

            // 5.3 Serialize each genre array of Movie objects into a binary file
            Movie[][] allMovies = {musicalMovies, comedyMovies, animationMovies, adventureMovies, dramaMovies, crimeMovies,
                    biographyMovies, horrorMovies, actionMovies, documentaryMovies, fantasyMovies, mysteryMovies,
                    sciFiMovies, familyMovies, westernMovies, romanceMovies, thrillerMovies};

            try {
                for (int i = 0 ; i < fileNames.length ; i++) {
                    oos = new ObjectOutputStream(new FileOutputStream(fileNames[i].substring(0,fileNames[i].length()-4) + ".ser"));
                    if (allMovies[i] != null) {
                        for (int j = 0; j < allMovies[i].length; j++) {
                            oos.writeObject(allMovies[i]);
                        }
                    }
                    
                }
                oos.close();
                
            }
            catch (IOException e) {
                e.getMessage();
            }
            
        } catch (FileNotFoundException e) {
            e.getMessage();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        // 5.4 Write to part 3’s manifest file all the genre names
        try {

            PrintWriter writerPart3Manifest = new PrintWriter(new FileOutputStream("part3_manifest.txt"));
            for (int i = 0; i < fileNames.length; i++) {
                writerPart3Manifest.println(fileNames[i].substring(0, fileNames[i].length() - 4) + ".ser");
            }
            writerPart3Manifest.close();

        } catch (IOException e) {
            e.getMessage();
        }
        return "part3_manifest.txt";
    }

    // --------------------------------------------------------------------------------------------
    //                            6. Get number of records for each genre
    // --------------------------------------------------------------------------------------------

    public static int getNumOfRecords(String genre) {
        
        try {
            Scanner reader = new Scanner(new FileInputStream(genre + ".csv"));
            int count = 0;
            while (reader.hasNextLine()) {
                reader.nextLine();
                count++;
            }
            reader.close();
            return count;
        } catch (FileNotFoundException e) {
            e.getMessage();
            return 0;
        }
        
    }

    // --------------------------------------------------------------------------------------------
    //                                      7. Display Main Menu
    // --------------------------------------------------------------------------------------------

    public static void displayMainMenu(Movie[][] allMovies, int selection) {
        String[] genres = {"Musical", "Comedy", "Animation", "Adventure", "Drama", "Crime", "Biography", "Horror", "Action", "Documentary", "Fantasy", "Mystery", "Sci-fi", "Family", "Western", "Romance", "Thriller"};
        System.out.println("-----------------------------");
        System.out.println("        Main Menu            ");
        System.out.println("-----------------------------");
        System.out.println("s Select a movie array to navigate");
        System.out.println("n Navigate " + genres[selection] + " movies (" + getNumOfRecords(genres[selection]) + " records)");
        System.out.println("x Exit");
        System.out.println("-----------------------------");
        System.out.println();
        System.out.print("Enter Your Choice: ");
    }

    // --------------------------------------------------------------------------------------------
    //                                    8. Display Genre Sub-Menu
    // --------------------------------------------------------------------------------------------

    public static void displayGenreSubMenu() {
        System.out.println("-----------------------------");
        System.out.println("        Genre Sub-Menu:      ");
        System.out.println("-----------------------------");
        System.out.println("1 musical                           (" + getNumOfRecords("Musical") + " records)");
        System.out.println("2 comedy                            (" + getNumOfRecords("Comedy") + " records)");
        System.out.println("3 animation                         (" + getNumOfRecords("Animation") + " records)");
        System.out.println("4 adventure                         (" + getNumOfRecords("Adventure") + " records)");
        System.out.println("5 drama                             (" + getNumOfRecords("Drama") + " records)");
        System.out.println("6 crime                             (" + getNumOfRecords("Crime") + " records)");
        System.out.println("7 biography                         (" + getNumOfRecords("Biography") + " records)");
        System.out.println("8 horror                            (" + getNumOfRecords("Horror") + " records)");
        System.out.println("9 action                            (" + getNumOfRecords("Action") + " records)");
        System.out.println("10 documentary                      (" + getNumOfRecords("Documentary") + " records)");
        System.out.println("11 fantasy                          (" + getNumOfRecords("Fantasy") + " records)");
        System.out.println("12 mystery                          (" + getNumOfRecords("Mystery") + " records)");
        System.out.println("13 sci-fi                           (" + getNumOfRecords("Sci-fi") + " records)");
        System.out.println("14 family                           (" + getNumOfRecords("Family") + " records)");
        System.out.println("15 western                          (" + getNumOfRecords("Western") + " records)");
        System.out.println("16 romance                          (" + getNumOfRecords("Romance") + " records)");
        System.out.println("17 thriller                         (" + getNumOfRecords("Thriller") + " records)");
        System.out.println("18 Exit");
        System.out.println("-----------------------------");
        System.out.println();
        System.out.print("Enter Your Choice: ");
    }

    // --------------------------------------------------------------------------------------------
    //                                     9. do_part3 Method
    // --------------------------------------------------------------------------------------------

    public static Movie[][] do_part3(String part3_manifest) {

        Movie[][] allMovies = new Movie[17][];
        String[] fileNames = null;
        Scanner fileNameReader = null;
        ObjectInputStream ois = null;

        // 9.1 Read the manifest file:
        try {
            fileNameReader = new Scanner(new FileInputStream(part3_manifest));
            String line = fileNameReader.nextLine();
            while (fileNameReader.hasNextLine()) {
                line += "\n" + fileNameReader.nextLine();
            }
            fileNames = line.split("\n"); // Split each manifest file name into an array

        } catch (FileNotFoundException e) {
            e.getMessage();
        } finally {
            if (fileNameReader != null) {
                fileNameReader.close();
            }
        }

        // 9.2 Load the movies from the ser files into the respective genre array of
        // Movie objects
        for (int i = 0; i < fileNames.length; i++) { // Loop through each file in the manifest file
            try {
                ois = new ObjectInputStream(new FileInputStream(fileNames[i]));
                //for (int j = 0; j < allMovies.length; j++) {
                    Movie[] movieGenre = (Movie[]) ois.readObject();
                    allMovies[i] = movieGenre;
                    
                    ois.close();
                //}
            }
            catch(EOFException e) {
                continue;
            }
            catch (IOException e) {
                e.getMessage();
            } catch (ClassNotFoundException e) {
                e.getMessage();
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.getMessage();
                    }
                }

            }
        }
        for(int i = 0; i < allMovies.length; i++){
            if(allMovies[i] == null) {
                allMovies[i] = new Movie[0];
            }
        }
    
        
        return allMovies;


    }

}


