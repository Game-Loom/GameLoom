import java.util.*;
import java.lang.StringBuilder;
import java.io.PrintWriter;
import java.io.File;

public class GameCSVExporter{
    /**
     * Takes in a list of games, then exports that list to a CSV file with all its attributes
     * @param games - the list of games
     * @param file - the CSV file to export the games to
     */
    public static void exportGamesToCSV(File file){
        ArrayList<Game> games = GraphicalUserInterface.library;

        ArrayList<String> attributes = getAttributes(games); //Gets an array list of attributes in Strign format
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
     * Gets all the attributes from the game list
     * Used to make headers for the CSV file and as keys for the values in games
     * @param games list of all the games
     * @return an ArrayList of all the attributes as Strings
     */
    private static ArrayList<String> getAttributes(List<Game> games){
        ArrayList<String> attributes = new ArrayList<>();

        //NOTE: This assumes that every game has the same attributes and thus only gets the attributes from the first game in the list. This may or may not be true, we might need to normalize the
        //attribute list, figure out a way to ensure every game has the same attribute list, or maybe order I can order the list based on the number of attributes for this function?
        Set<String> attributeSet = games.get(0).getAttributes().keySet(); //Gets the list of attributes from the game. 

        for (String attribute : attributeSet) {
            attributes.add(attribute);
        }

        return attributes;
    }

    /**
     * Returns an ArrayList of the rows for the CSV
     * Each row is a String formatted as attribute1Value, attribute2Value, ... attributNValue, as is normal for a CSV
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


