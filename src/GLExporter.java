/**
 * The GLExporter class is responsible for exporting video game data from the GameLoom library to CSV files.
 * Each game's attributes are extracted and formatted as CSV rows, ensuring data consistency and proper handling
 * of special characters such as commas within attribute values.
 * 
 * This class supports:
 * - Exporting a list of games with all their attributes to a specified CSV file.
 * - Creating CSV headers based on game attributes.
 * - Handling special cases, such as replacing commas in values to prevent conflicts with the CSV delimiter.
 * 
 * Key functionalities include:
 * - Writing game attributes as CSV rows, formatted with a standard comma delimiter.
 * - Automatically handling empty attribute values by replacing them with "N/A".
 * - Safeguarding against delimiter conflicts by replacing internal commas with spaces within attribute values.
 * 
 * Example of how this class works:
 *     GLExporter.exportGamesToCSV(games, new File("path/to/exported_file.csv"));
 *     // Exports the given list of games to a CSV file with appropriate formatting and headers.
 * 
 * Note: The getGameValues method provides logic for formatting each game as a CSV row, ensuring data integrity and proper 
 * structure of the output.
 * 
 * @author GameLoom Team
 * @version 1.0
 */


import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class GLExporter{
    /**
     * Takes in a list of games, then exports that list to a CSV file with all its attributes
     * 
     * @param games - the list of games
     * @param file - the CSV file to export the games to
     */
    public static void exportGamesToCSV(List<Game> games, File file){
        ArrayList<String> attributes = GUIDriver.attributes; //Gets an array list of attributes in Strign format
        ArrayList<String> csvRows = getGameValues(games, attributes); //Gets the games/CSV rows of the library

        StringBuilder headers = new StringBuilder(); //Gets the headers of the CSV file based on the game attributes, seperating them with commas
        for(String attribute : attributes){
            headers.append(attribute);
            headers.append(", ");
        }
        headers = headers.delete(headers.length()-2, headers.length()); //Deletes preceding comma and space

        try(PrintWriter writer = new PrintWriter(file)){ //Opens the given file to write to
            writer.println(headers.toString());
            for(String row:csvRows){
                writer.println(row);
            }
            writer.flush();
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Returns an ArrayList of the rows for the CSV
     * Each row is a String formatted as attribute1Value, attribute2Value, ... attributNValue, as is normal for a CSV
     * 
     * @param games List of games in the library
     * @param attributes 
     * @return
     */
    private static ArrayList<String> getGameValues(List<Game> games, ArrayList<String> attributes){
        ArrayList<String> csvRows = new ArrayList<>();

        for(Game game : games){
            StringBuilder row = new StringBuilder();
            for(String attribute : attributes){
                String value = game.getAttribute(attribute);

                if(value.contains(",")){ //Since commas are the delimineter for CSVs, this just replaces any commas with a space
                    value = value.replace(',', ' ');
                }
                else if(value.equals("")){
                    value = "N/A";
                }

                row.append(value);
                row.append(", "); 
            }
            row = row.delete(row.length()-2, row.length());
            csvRows.add(row.toString());
        }

        return csvRows;
    }
}


