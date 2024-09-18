import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GameCSVImporter {

    // Method to import games from a CSV file with dynamic attributes
    public static List<Game> importGamesFromCSV(String csvFilePath){
        List<Game> games = new ArrayList<>();
        Path pathToFile = Paths.get(csvFilePath);

        try (BufferedReader br = Files.newBufferedReader(pathToFile)){
            String line;
            String[] headers = null;
            
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null){
                if (isFirstLine) {
                    // Make sure headers are trimmed to remove any extra spaces
                    headers = line.split(";");
                    for (int i = 0; i < headers.length; i++){
                        headers[i] = headers[i].trim();
                    }
                    isFirstLine = false;
                    continue;
                }

                // Split the line by semi-colons to get the values
                String[] values = line.split(";");

                // Map the values to their corresponding header
                Map<String, String> attributes = new HashMap<>();
                for (int i = 0; i < values.length; i++) {
                    attributes.put(headers[i].trim(), values[i].trim());
                }

                // Create a new Game object and add it to the list
                Game game = new Game(attributes);
                games.add(game);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        //TEST ITERATING ALL HEADERS TO SCREEN HERE TEST ITERATING ALL HEADERS TO SCREEN HERE TEST ITERATING ALL HEADERS TO SCREEN HERE
        return games;
    }
}
