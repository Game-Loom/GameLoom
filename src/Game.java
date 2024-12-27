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
 * @version 1.4
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
     * Provides a string representation of the game, summarizing all attributes except for those marked "N/A" or with the key "game".
     * 
     * @return A formatted string summarizing the game's attributes.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        // Define the order of keys to display first, if they are present in the attributes
        String[] preferredKeys = {"platform", "hours_played", "release_date", "last_played", "metascore"};

        // First, add the preferred keys in the specified order if they exist and are valid
        for (String key : preferredKeys) {
            String value = attributes.getOrDefault(key, "N/A");
            if (!value.equals("N/A") && !value.isEmpty()) {
                String formattedKey = capitalizeAndFormatKey(key);
                if (key.equalsIgnoreCase("hours_played")) {
                    value = formatIfDouble(value);
                }
                result.append(formattedKey).append(": ").append(value).append(" | ");
            }
        }

        // Next, add any remaining keys that are not part of the preferredKeys list
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // Skip attributes with key "game", any "N/A" or empty values, or keys already processed
            if (key.equalsIgnoreCase("game") || value.equals("N/A") || value.isEmpty() || containsKey(preferredKeys, key)) {
                continue;
            }

            String formattedKey = capitalizeAndFormatKey(key);
            result.append(formattedKey).append(": ").append(value).append(" | ");
        }

        // Remove the trailing separator " | " if present
        if (result.length() > 0) {
            result.setLength(result.length() - 3);
        }

        return result.toString();
    }


    /**
     * Helper method to capitalize the first letter of the key and replace underscores with spaces.
     *
     * @param key The original key string.
     * @return A formatted string with capitalized words and spaces instead of underscores.
     */
    protected String capitalizeAndFormatKey(String key) {
        String[] words = key.split("_");
        StringBuilder formattedKey = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                formattedKey.append(Character.toUpperCase(word.charAt(0)))
                            .append(word.substring(1).toLowerCase())
                            .append(" ");
            }
        }
        return formattedKey.toString().trim();
    }


    /**
     * Helper method to format a value to two decimal places if it is a number.
     * If the value is not a number, it returns the value as is.
     *
     * @param value The original value string.
     * @return The value formatted to two decimal places if numeric, or the original value otherwise.
     */
    private String formatIfDouble(String value) {
        try {
            double numericValue = Double.parseDouble(value);
            return String.format("%.2f", numericValue);
        } catch (NumberFormatException e) {
            return value;
        }
    }

    /**
     * Helper method to check if an array contains a specific key.
     */
    private boolean containsKey(String[] keys, String key) {
        for (String k : keys) {
            if (k.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to update an attribute key/value pair
     * Used in EditTab.java
     */
    public void updateAttribute(String key, String value) {
        if (value == null || value.trim().isEmpty()) {
            attributes.remove(key);
        } else {
            attributes.put(Normalizer.normalizeKey(key), value.trim());
        }
    }

    /******** SORTING IMPLEMENTATION **************/

    /**
     * This method gets the title value of the game to make the title comparator associated with a method.
     * @return String representing the title of the game
     */
    public String getTitle() {
        String title = getAttribute("game");
        int startIndex = -1;
        int endIndex = -1;
        if(title.charAt(0) == '"' || title.charAt(0) == '\'') { // ignores quote if its the first character
            title = title.substring(1);
        } 
        return title;
    }

    /**
     * This defines a comparator to sort Game objects by their date.
     * @param isAscending specifies if the order of sorting is ascending or not (descending)
     * @return comparator that compares Game objects by their date
     */
    public static final Comparator<Game> byDate (boolean isAscending) {
        return new Comparator<Game>() {
        // YYYY-MM-DATE  ex: attributes.put("release_date", "2021-05-20"); 
            @Override
            public int compare(Game game1, Game game2) {

                String date1 = game1.getAttribute("release_date");
                String date2 = game2.getAttribute("release_date");
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

    /**
     * This defines a comparator to sort Game objects by a custom field that is a string/word
     * @param isAscending specifies if the order of sorting is ascending or not (descending)
     * @return comparator that compares Game objects by a custom field
     */
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

    /**
     * This method attempts to parse the double.
     * @param number representing double we want to parse.
     * @return double representing if it parsed. Returns negative infinity upon error.
     */
    public static double parseDouble(String number) {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            return Double.NEGATIVE_INFINITY;
        }
    }

    /**
     * This defines a comparator to sort Game objects by a custom field that is a number
     * @param isAscending specifies if the order of sorting is ascending or not (descending)
     * @return comparator that compares Game objects by the custom field
     */
    public static final Comparator<Game> byFieldDouble (boolean isAscending, String fieldName) {
        return new Comparator<Game>() {
            @Override
            public int compare(Game game1, Game game2) {                
                double field1 = parseDouble(game1.getAttribute(fieldName));
                double field2 = parseDouble(game2.getAttribute(fieldName));
                double negInf = Double.NEGATIVE_INFINITY; 

                //if double could not be parsed (returns negative infinity)
                //field will be moved to the end
            
                // System.out.print(fieldName + " of game1= " + field1 + "vs game2= " + field2);
                if(field1 == negInf || field2 == negInf) {
                    if(field1 == negInf && field2 == negInf) {
                        return 0;
                    }
                    else if(field1 == negInf) {
                        return isAscending ? 1 : -1; 
                        //if ascending, all invalid date goes to end
                        //if descending, all invalid dates go to start 
                        // so when Collections.reversed() is called for descending, the invalid dates
                        // end up at the end 
                    }
                    else {
                        // System.out.println( "will return (true = -1)" + isAscending);
                        return isAscending ? -1 : 1;
                    }
                }
                return Double.compare(field1, field2);
            }
        };
    }

    /**
     * This defines a comparator to sort Game objects by their title
     */
    public static final Comparator<Game> byTitle = Comparator.comparing(Game::getTitle);
    
    /**
     * This defines a comparator to sort Game objects by their platform name
     */
    public static final Comparator<Game> byPlatform = Comparator.comparing(Game::getPlatform);

}
