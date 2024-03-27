// -----------------------------------------------------
// Assignment 2: Movie Database
// Question: (include question/part number, if applicable)
// Written by: Botao Yang (40213554)
// -----------------------------------------------------

import java.io.IOException;
import java.io.FileNotFoundException;

import java.io.FileInputStream;
import java.util.Scanner;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class Driver {

    public static void main(String[] args) {

        // part 1’s manifest file
        String part1_manifest = "part1_manifest.txt";


        do_part1(part1_manifest);


        // part 2’s manifest file
        String part2_manifest = do_part1(part1_manifest /* , ... */); // partition

        // // part 3’s manifest file
        // String part3_manifest = do_part2(part2_manifest /* , ... */); // serialize
        // do_part3(part3_manifest /* , ... */); // deserialize
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

    public static void do_part2(String part2_manifest) {

    }

}


