import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GameCSVImporter {

    public static List<Game> importGamesFromCSV(String csvFilePath){
        List<Game> games = new ArrayList<>();
        Path pathToFile = Paths.get(csvFilePath);

        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8)){
            String line;
            String[] headers = null;

            boolean isFirstLine = true;

            while ((line = br.readLine()) != null){
                if (isFirstLine) {
                    // Split the headers
                    headers = line.split(";");
                    for (int i = 0; i < headers.length; i++){
                        headers[i] = headers[i].trim();
                    }

                    // Check for BOM in the first header
                    if (headers[0].startsWith("\uFEFF")) {
                        headers[0] = headers[0].substring(1);
                    }

                    // Print headers and their Unicode code points
                    System.out.println("Headers and their Unicode code points:");
                    for (String header : headers) {
                        System.out.print("Header: '");
                        for (char c : header.toCharArray()) {
                            System.out.print(c + "' (" + String.format("\\u%04x", (int) c) + ") ");
                        }
                        System.out.println();
                    }

                    isFirstLine = false;
                    continue;
                }

                // Split the line by semicolons to get the values
                String[] values = line.split(";");

                // Map the values to their corresponding header
                Map<String, String> attributes = new LinkedHashMap<>();
                for (int i = 0; i < values.length; i++) {
                    attributes.put(headers[i], values[i].trim());
                }

                // Create a new Game object and add it to the list
                Game game = new Game(attributes);
                games.add(game);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return games;
    }
}
