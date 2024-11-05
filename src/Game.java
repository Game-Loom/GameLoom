/**
 * The Game class represents a video game with a set of attributes stored as key-value pairs.
 * Attributes can include information not limited to but such as platform, hours played, Metacritic score, and release date.
 * 
 * This class provides methods to retrieve specific attributes, such as platform, 
 * as well as to get a summary of the game details via a custom toString method.
 * 
 * Key functionalities include:
 * - Storing game attributes as a Map<String, String>. (HashMap)
 * - Providing access to individual game attributes via getAttribute.
 * - Returning the platform of the game via getPlatform.
 * - Generating a string summary of key game attributes, such as Metacritic 
 *   score, platform, hours played, and release date.
 * 
 * Note: Some platform detection logic has been removed in the most recent update, 
 * focusing on the platform attribute provided within the CSV import process (as of 10/16).
 * 
 * Example of how a game's attributes may look:
 *     Map<String, String> attributes = new HashMap<>();
 *     attributes.put("platform", "Steam");
 *     attributes.put("hours", "12.5");
 *     attributes.put("metascore", "85");
 *     attributes.put("release_date", "2021-05-20");
 *     Game game = new Game(attributes);
 * 
 * @author CS321-004: Group 3
 * @version 1.3
 */
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;

public class Game {
    // Attributes map that stores game data as key-value pairs
    // Example: {"platform": "Steam", "metascore": "90"}
    private Map<String, String> attributes = new HashMap<>();


    /**
     * Constructor that initializes the Game object with a set of attributes.
     *
     * @param attributes A map containing game attributes (e.g., platform, hours played).
     */
    public Game(Map<String, String> attributes) {
        this.attributes = attributes; // Store the passed attributes in the instance's attributes map
    }


    /**
     * Retrieves the value of a specific attribute (singular)from the game.
     * Normalizes the key (trim and convert to lowercase) and return
     * the associated value or "N/A" if the key is not present
     * 
     * @param key The attribute name to search for (e.g., "platform").
     * @return The value associated with the provided key, or "N/A" if not found.
     */
    public String getAttribute(String key) {
        return attributes.getOrDefault(key.trim().toLowerCase(), "N/A"); // If it can't find a key it'll default to N/A
    }


    /**
     * Provides access to the complete set of attributes for the game.
     * 
     * Note: Is used in GUI to assign the platform from the dropdown menu - import button pairing (10/16) (CS)
     *
     * @return A map containing all attribute key-value pairs for the game.
     */
    public Map<String, String> getAttributes() {// I had been using it to print the k/v pairs to console while I was debugging
        return attributes;
    }


    /**
     * Retrieves the platform of the game from its attributes.
     * 
     * Note: Removed win/mac/linux/steam_deck platform logic so it's no longer
     * assigning Steam specific platform to everything (10/16) (CS)
     * 
     * @return The platform string (e.g., "Steam", "GOG"), or "N/A" if the platform is not available.
     */
    public String getPlatform() {
        // Check if the 'platform' attribute is present and not empty
        String platform = getAttribute("platform");
        if (!platform.equals("N/A") && !platform.isEmpty()) {
            return platform.trim();
        }

        // If 'platform' attribute is not provided
        StringBuilder platforms = new StringBuilder();
        if (platforms.length() == 0) {// If no platforms detected, return 'N/A'
            return "N/A";
        }

        return platforms.toString().trim();
    }


    /**
     * Provides a string representation of the game, summarizing key attributes (currently)such as:
     * Metacritic score, platform, hours played, and release date.
     * 
     * @return A formatted string summarizing the game's important details.
     */
    @Override
    public String toString() {// Just your standard custom class toString - right now shows metacritic score > platform > hours played > release date
        String hoursPlayed = getAttribute("hours").isEmpty() ? "0.00" : getAttribute("hours");
        String metacriticScore = getAttribute("metascore").isEmpty() ? "N/A" : getAttribute("metascore");
        try {// Only need this to parse metacriticScore as an integer
            metacriticScore = String.format("%d", Integer.parseInt(metacriticScore));
        } catch (NumberFormatException e) {
            metacriticScore = "N/A"; // Failure results in default N/A
        }
        try {// Only need this to parse hoursPlayed as a double
            hoursPlayed = String.format("%.2f", Double.parseDouble(hoursPlayed));
        } catch (NumberFormatException e) {
            hoursPlayed = "0.00"; // Failure results in default 0.00
        }
        // Formatted string with key game details (currently): metacritic score | platform | hours played | release date
        return "Metacritic Score: " + metacriticScore +
                "| Platform: " + getPlatform() +
                "| Hours Played: " + hoursPlayed +
                "| Release Date: " + getAttribute("release_date");
    }

    /******** SORTING IMPLEMENTATION **************/
    /*
      * Example of how a game's attributes may look:
 *     Map<String, String> attributes = new HashMap<>();
 *     attributes.put("platform", "Steam");
 *     attributes.put("hours", "12.5");
 *     attributes.put("metascore", "85");
 *     attributes.put("release_date", "2021-05-20"); 
 *     Game game = new Game(attributes);
     */
    public String getTitle() {
        return getAttribute("game");
    }
    
    public static final Comparator<Game> byDate (boolean isAscending) {
        return new Comparator<Game>() {
        // YYYY-MM-DATE  ex: attributes.put("release_date", "2021-05-20"); 
            @Override
            public int compare(Game game1, Game game2) {

                String date1 = game1.getAttribute("release_date");
                String date2 = game2.getAttribute("release_date");
                System.out.println(date1);

                boolean date1NotValid = (date1.equals("N/A") || date1.length() != 10);
                boolean date2NotValid = (date2.equals("N/A") || date2.length() != 10);

                if(date1NotValid || date2NotValid) {
                    if(date1NotValid && date2NotValid) {
                        return 0; //both invalid, so equal
                    } else if (date1NotValid) {
                        return isAscending ? 1 : -1; 
                        //if ascending, all invalid date goes to end
                        //if descending, all invalid dates go to start 
                        // due to nature of Collections.reversed() 
                    } else {
                        return isAscending ? -1 : 1;
                    }
                }

                int year1 = Integer.parseInt(date1.substring(0, 4));
                int month1 = Integer.parseInt(date1.substring(5, 7));
                int day1 =  Integer.parseInt(date1.substring(8));

                int year2 = Integer.parseInt(date2.substring(0, 4));
                int month2 = Integer.parseInt(date2.substring(5, 7));
                int day2 =  Integer.parseInt(date2.substring(8));

                if(year1 != year2) {
                    return Integer.compare(year1, year2);
                } else if (month1 != month2) {
                    return Integer.compare(month1, month2);
                } 
                return Integer.compare(day1, day2);
            }
        };
    }

    public static final Comparator<Game> byFieldString (boolean isAscending, String fieldName) {
        return new Comparator<Game>() {
            @Override
            public int compare(Game game1, Game game2) {
                String field1 = game1.getAttribute(fieldName);
                String field2 = game2.getAttribute(fieldName);
                
                boolean field1NotValid = (field1.equals("N/A") || field1.equals("") || field1.length() == 0);
                boolean field2NotValid = (field2.equals("N/A") || field2.equals("") || field2.length() == 0);

                if(field1NotValid || field2NotValid) {
                    if(field1NotValid && field2NotValid) {
                        return 0;
                    }
                    else if(field1NotValid) {
                        return isAscending ? 1 : -1; 
                        //if ascending, all invalid date goes to end
                        //if descending, all invalid dates go to start 
                        // due to nature of Collections.reversed()          
                    }
                } else {
                    return isAscending ? 1 : -1; 
                } 
                return field1.compareTo(field2);
            }
        };
    }

    public static double parseDouble(String number) {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            return Double.NEGATIVE_INFINITY;
        }
    }
        
    public static final Comparator<Game> byFieldDouble (boolean isAscending, String fieldName) {
        return new Comparator<Game>() {
            @Override
            public int compare(Game game1, Game game2) {                
                double field1 = parseDouble(game1.getAttribute(fieldName));
                double field2 = parseDouble(game2.getAttribute(fieldName));
                double negInf = Double.NEGATIVE_INFINITY; 

                //if double could not be parsed (returns negative infinity)
                //field will be moved to the end
                if(field1 == negInf || field2 == negInf) {
                    if(field1 == negInf && field2 == negInf) {
                        return 0;
                    }
                    else if(field1 == negInf) {
                        return isAscending ? 1 : -1; 
                        //if ascending, all invalid date goes to end
                        //if descending, all invalid dates go to start 
                        // due to nature of Collections.reversed() 
                    }
                    else {
                        return isAscending ? -1 : 1;
                    }
                }
                return Double.compare(field1, field2);
            }
        };
    }

    public static final Comparator<Game> byTitle = Comparator.comparing(Game::getTitle);
    public static final Comparator<Game> byPlatform = Comparator.comparing(Game::getPlatform);

}
