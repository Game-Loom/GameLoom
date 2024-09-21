import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GameCSVImporter {

    public static List<Game> importGamesFromCSV(String csvFilePath){

        List<Game> games = new ArrayList<>();// List holds the Game objects
        // Converts the file path string into a Path object for easier file operations
        Path pathToFile = Paths.get(csvFilePath);// This gets populated via the file selection in the GUI
        // Tries to create a BufferedReader object that reads the file using UTF-8 encoding as the default
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8)){// UTF-8 is just a super common encoding for .csv files so I went with this
            
            String line;// Stores each line of the CSV file
            
            String[] headers = null;// Stores the headers (column names) from the first row of the CSV

            boolean isFirstLine = true;// Used while reading to check if we are reading the first line (headers)

            // Reads the CSV file line by line
            while ((line = br.readLine()) != null){
                // If it's the first line, split it by semicolons to extract headers this block is specific to just the headers
                if (isFirstLine){
                    headers = line.split(";");// Splits the headers
                    // Cycles through all of the headers and Trims any extra spaces from header values
                    for (int i = 0; i < headers.length; i++){
                        headers[i] = headers[i].trim();
                    }

                    // Checks the first header for a Byte Order Mark (BOM) and removes it if found
                    if (headers[0].startsWith("\uFEFF")){// \uFEFFF is specifically a hidden character that UTF-8 likes to put at the beginning of files
                        headers[0] = headers[0].substring(1);// This is just ignoring that character in the 0 index position
                    }
                    // Marks that the first line has been processed, so we can move to reading data rows.
                    isFirstLine = false;
                    continue;
                }

                // For all other rows in the csv this splits the line by semicolons to get the values
                String[] values = line.split(";");

                /*
                 * The LinkedHashMap I use below has only come up in my school work very briefly and they didn't make a big deal about it but when I was looking around online
                 * I found that LinkedHashMap maintains the insertion order of the entries so the order of the key-value pairs in the map will match the order of the headers in the CSV file, 
                 * which might be useful when iterating over them later. That seemed to be the only real difference between LinkedHashMap and regular old HashMap so you can mostly treat it the same
                 * just keep in mind that ordered advantage is there if we have a need for it at some point later.
                 */

                // Map the values to their corresponding header, creating the k/v pairs.
                Map<String, String> attributes = new LinkedHashMap<>();// Holds the actual k/v pairs
                for (int i = 0; i < values.length; i++){
                    attributes.put(headers[i], values[i].trim());// Trims extra spaces from each value for normalizing
                }

                // Create a new Game object and add it to the list
                Game game = new Game(attributes);
                games.add(game);
            }
        } catch (Exception e){// Just a blanket catch, we'll see whatever is wrong in the stack trace
            e.printStackTrace();
        }
        return games;
    }
}
