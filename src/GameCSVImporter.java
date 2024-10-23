/**
 * The GameCSVImporter class is responsible for importing video games from CSV files into the GameLoom library. 
 * Each game's data is represented as a set of attributes stored as key-value pairs, with flexible support for different 
 * delimiter types, including commas, semicolons, and tabs.
 * 
 * This class automatically detects the delimiter used in the CSV file, handles cases where values may contain 
 * delimiters that are not enclosed in quotes, and converts each row of the CSV into a Game object.
 * 
 * Key functionalities include:
 * - Dynamically detecting the delimiter used in the CSV file (supports comma, semicolon, and tab).
 * - Storing game attributes as a LinkedHashMap and returning a list of Game objects.
 * - Handling rows where values may contain commas, semicolons, or tabs within the data without quotes.
 * - Supporting flexible CSV imports from various sources while maintaining data integrity.
 * 
 * Example of how this class works:
 *     List<Game> importedGames = GameCSVImporter.importGamesFromCSV("path/to/file.csv");
 *     for (Game game : importedGames) {
 *         System.out.println(game.getAttribute("platform"));  Prints platform for each game
 *     }
 * 
 * Note: The method customSplitWithoutQuotes provides custom logic for handling rows with values containing delimiters, 
 * ensuring robust import processing for non-quoted values.
 * 
 * @author CS321-004: Group 3
 * @version 1.4
 */


import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GameCSVImporter {

    /**
     * Imports games from a CSV file and returns a list of Game objects. The method automatically 
     * detects the delimiter used in the file (comma, semicolon, or tab) and handles rows where 
     * values may contain delimiters but are not enclosed in quotes (particularly usefule with commas).
     * 
     * @param csvFilePath The file path of the CSV to import.
     * @return A List of Game objects populated from the CSV file.
     */
    public static List<Game> importGamesFromCSV(String csvFilePath) {

        List<Game> games = new ArrayList<>(); // List to store Game objects
        Path pathToFile = Paths.get(csvFilePath); // Converts file path to a Path object

        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8)) { // Read the file using UTF-8 encoding
            String line;
            String[] headers = null; // Array to store headers (first row)
            String delimiter = null; // Variable to hold the determined delimiter
            boolean isFirstLine = true; // Track if we are reading the first line (headers)

            // Reads the CSV file line by line
            while ((line = br.readLine()) != null) {
                // If it's the first line (headers), determine the delimiter and split the headers
                if (isFirstLine) {
                    delimiter = detectDelimiter(line); // Determine delimiter based on the header line
                    headers = line.split(delimiter); // Split headers using detected delimiter

                    // Clean headers by trimming spaces and removing BOM (Byte Order Mark) if present
                    for (int i = 0; i < headers.length; i++) {
                        headers[i] = headers[i].trim().toLowerCase();
                    }

                    // Remove BOM (Byte Order Mark) if present in the first header
                    if (headers[0].startsWith("\uFEFF")) {
                        headers[0] = headers[0].substring(1);
                    }

                    isFirstLine = false; // Move to data rows
                    continue;
                }

                // Handle the data rows, using custom split logic for supported delimiters
                String[] values;
                if ("\t".equals(delimiter) || ",".equals(delimiter) || ";".equals(delimiter)) {
                    values = customSplitWithoutQuotes(line, delimiter); // Custom splitting for multiple delimiters
                } else {
                    values = line.split(delimiter); // Use normal splitting for other delimiters
                }

                // Create a LinkedHashMap to store key-value pairs (header-value)
                Map<String, String> attributes = new LinkedHashMap<>();
                for (int i = 0; i < values.length; i++) {
                    if (i < headers.length) {
                        attributes.put(headers[i], values[i].trim()); // Trim values and associate them with headers
                    }
                }

                attributes = Normalizer.normalize(attributes);

                // Create a new Game object and add it to the list
                Game game = new Game(attributes);
                games.add(game);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return games;
    }

    /**
     * Determines the delimiter used in the CSV by examining the header row. 
     * Assumes that headers do not contain spaces and selects the delimiter that 
     * results in the most columns.
     * 
     * Supported delimiters: comma, semicolon, and tab.
     * 
     * @param headerLine The first line of the CSV (headers).
     * @return The detected delimiter (comma, semicolon, or tab).
     */
    private static String detectDelimiter(String headerLine) {
        String[] potentialDelimiters = { ",", ";", "\t" }; // Handles common delimiters: comma, semi-colon, tab
        String chosenDelimiter = ","; // Default to comma
        int maxColumns = 0;

        // Iterate through the possible delimiters and select the one that results in the most columns
        for (String delimiter : potentialDelimiters) {
            String[] columns = headerLine.split(delimiter);
            if (columns.length > maxColumns) { // Choose the delimiter that produces the most columns
                maxColumns = columns.length;
                chosenDelimiter = delimiter;
            }
        }
        return chosenDelimiter; // Return the detected delimiter
    }

    /**
     * Custom logic to split lines that contain delimiters (comma, tab, semicolon) in values, 
     * where the values are **not** enclosed in quotes.
     * This method ensures that values with delimiters inside them are handled properly.
     * 
     * If a line contains values with commas, tabs, or semicolons but the values are not quoted, 
     * this method will split the line while preserving the integrity of such values.
     * 
     * @param s The line from the CSV file.
     * @param delimiter The delimiter to use (comma, tab, semicolon).
     * @return An array of values split by the delimiter.
     */
    public static String[] customSplitWithoutQuotes(String s, String delimiter) {
        ArrayList<String> words = new ArrayList<>();
        boolean insideValue = false; // Track whether we are inside a quoted value
        int start = 0;

        // Iterate through the line character by character
        for (int i = 0; i < s.length(); i++) {
            // When encountering a delimiter, split the string only if we are not inside a quoted value
            if (s.startsWith(delimiter, i) && !insideValue) {
                words.add(s.substring(start, i).trim()); // Add the value up to the delimiter
                start = i + delimiter.length(); // Move start index to after the delimiter
                i += delimiter.length() - 1; // Adjust loop index to skip over the delimiter
            }
            // Track if inside quotes (in case of quoted values in users file)
            else if (s.charAt(i) == '"') { // and our export file will have quotes if they needed to be appended on import)
                insideValue = !insideValue; // Toggle inside-quote status
            }
        }
        words.add(s.substring(start).trim()); // Add the last segment
        return words.toArray(new String[0]); // Convert ArrayList to array
    }
}
