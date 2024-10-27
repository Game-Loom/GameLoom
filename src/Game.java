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
    private String capitalizeAndFormatKey(String key) {
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
}
