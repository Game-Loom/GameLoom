/**
 * This class is integral to ensuring the consistency and clarity of data displayed in GameLoom.
 * 
 * The Normalizer class provides utility methods for standardizing and organizing game attributes 
 * imported into the GameLoom application. This class applies consistent naming conventions to attributes
 * from different sources and filters out non-game entries, like popular streaming apps. Through 
 * normalization, it ensures that data remains organized and consistent across different sources.
 * 
 * The class offers flexible handling of various game attribute fields, including platform-specific naming 
 * conventions for games and their metadata. Additionally, `Normalizer` maintains a list of popular 
 * non-game applications that can be excluded from the GameLoom library.
 * 
 * Key Features:
 * - **Attribute Normalization**: Converts various naming conventions for common attributes, such as 
 *   game names, hours played, release dates, and multiplayer/single-player labels, into a unified format.
 * - **App Filtering**: Excludes a predefined list of popular streaming and utility apps found on 
 *   PlayStation 4 and 5, ensuring that the GameLoom library focuses solely on games.
 * - **Consistent Field Structure**: Fields like `multiplayer`, `singleplayer`, and `languages` are 
 *   mapped to standardized labels, enhancing uniformity across the library.
 * 
 * Constant Lists:
 * - **game**: Variations for game names.
 * - **hours_played**: Different representations of hours played.
 * - **last_played**: Variations on "last played" date.
 * - **release_date**: Labels indicating release dates.
 * - **captions**: Language support for captions and subtitles.
 * - **multiplayer** and **singleplayer**: Representations of game modes.
 * - **languages**: Supported languages mapped to various spellings or abbreviations.
 * - **popularApps**: A list of Top 50 most popular non-game apps on game consoles to exclude from the GameLoom library.
 * 
 * @see GraphicalUserInterface
 * @see GameCSVImporter
 * 
 * @author GameLoom Team
 * @version 1.0
 */


import java.util.*;

public class Normalizer {
    /*Lists of all the different versions of a normalized attribute*/
    //Since these lists won't change once we make them I just made them all constant String arrays, feel free to change if there's a more efficient way
    private static final String title[] = {"game", "name", "title"};
    private static final String hours_played[] = {"hours"};
    private static final String last_played[] = {"last played", "last-played", "last_played"};
    private static final String release_date[] = {"release"};
    private static final String captions[] = {"captions", "subtitles"};
    private static final String multiplayer[] = {"multiplayer", "multi-player", "multi player", "coop", "co-op", "co op"};
    private static final String singleplayer[] = {"singleplayer", "single player", "single-player", "solo player"};
    private static final String languages[] = {"english", "spanish", "french", "czech", "chinese", "danish", "dutch", "finnish",
                                                "german", "greek", "hungarian", "indonesian", "italian", "japanese", "korean",
                                                "norwegian", "polish", "portuguese", "romanian", "russian", "thai", "turkish", 
                                                "ukrainian", "vietnamese", "bulgarian", "swedish"};
    private static final String empty[] = {"", "null", "n/a"};
    private static final String trueValues[] = {"x", "true"};

    // List of 50 most popular streaming and utility apps on PlayStation 4 and 5 to exclude from the GameLoom library
    protected static final String popularApps[] = {
        "Netflix", "YouTube", "Spotify", "Hulu", "Disney+", "Amazon Prime Video", "Twitch", "Crunchyroll",
        "HBO Max", "Apple TV", "Peacock", "Paramount+", "Plex", "Funimation", "TikTok", "NFL Sunday Ticket",
        "YouTube Kids", "Redbox", "ESPN", "Showtime Anytime", "iHeartRadio", "Vudu", "VRV", "Tubi", "Vevo",
        "CBS All Access", "MLB.TV", "NBA App", "Spotify Kids", "Showmax", "Rakuten TV", "BBC iPlayer", 
        "Al Jazeera", "DAZN", "Sky Go", "Red Bull TV", "MTV Play", "Deezer", "EPIX Now", "Starz", 
        "FOX NOW", "Sling TV", "FunimationNow", "Acorn TV", "MUBI", "CuriosityStream", "BritBox", 
        "FuboTV", "Shudder", "Hoopla",
    };

    // Could handle DLC very similarly for things that Sony lists as "Digital Artbook", and "Original Soundtrack", I'm sure there are others but those are the ones I have

    /**
     * Normalizes the given attribute list.
     * 
     * @param attributes - An un-normalized attribute list. Obtained directly from CSV import.
     * @return A mapping of normalized attributes and their values
     */
    public static Map<String, String> normalize(Map<String, String> attributes){
        Map<String, String> normAttributes = new LinkedHashMap<>();

        for(String key: attributes.keySet()){
            String value = attributes.get(key);

            //Normalizes attributes that are for the game's name. The condition for the attribute not containing id is to prevent the program from considering "game id"
            //The last condition is to make sure that once we've found the game's title, no other attributes with the word "title" in it will be considered
            if(contains(key, title) && !key.contains("id") && !normAttributes.containsKey("title")){ 
                normAttributes = populateNorm(normAttributes, "title", key, value);
            }
            else if(contains(key, hours_played)){
                normAttributes = populateNorm(normAttributes, "hours_played", key, value);
            }
            else if(contains(key, last_played)){
                normAttributes = populateNorm(normAttributes, "last_played", key, value);
            }
            else if(contains(key, release_date)){
                normAttributes = populateNorm(normAttributes, "release_date", key, value);
            }
            else if(contains(key, captions)){
                normAttributes = populateNorm(normAttributes, "captions", key, value);
            }
            else if(contains(key, multiplayer)){
                normAttributes = populateNorm(normAttributes, "multiplayer", key, value);
            }
            else if(contains(key, singleplayer)){
                normAttributes = populateNorm(normAttributes, "singleplayer", key, value);
            }
            else if(contains(key, languages)){
                normAttributes = populateNorm(normAttributes, "languages", key, value);
            }
            else{
                normAttributes.put(key, attributes.get(key)); //Attribute has no normalized equivalent, add it by itself
                //If the compiled attribute list doesn't already contain this key, add it
                if(!GUIDriver.attributes.contains(key)){
                    GUIDriver.attributes.add(key);
                }
            }
        }

        return normAttributes;
    }

    /**
     * Checks if the given attribute contains some version in the given normalized list.
     * 
     * @param attribute - An un-normalized attribute
     * @param normalized - One of the constant lists of words we can normalize
     * @return Whether or not the attribute contains one of the words in the given array
     */
    private static boolean contains(String attribute, String[] normalized){
        //I chose to use contains for the attribute names because it prevents us from having to consider all the versions of "title", "game-name", "game name", "game_name", etc.
        //Might cause its own problems though
        for(String word: normalized){
            if(attribute.contains(word)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given value is equivalent to any of the versions in the given list.
     * 
     * @param attribute - An un-normalized value
     * @param normalized - One of the constant lists of words
     * @return Whether or not the value is equivalent to one of the words in the given array
     */
    private static boolean equals(String value, String[] normalized){
        //Specifically used to figure out if a value is simply a blank or some sort of checkmark. Ex. The steam exporter using "x" to mark an attribute is true
        for(String word: normalized){
            if(value.equals(word)){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the normalized version of an attribute to the normalized list, or edits the attribute if its already in the list
     * 
     * @param normA - The normalized attribute list for the game
     * @param normKey - The normalized attribute
     * @param key - The un-normalized attribute
     * @param value - The value of the un-normalized attribute
     * @return
     */
    private static Map<String, String> populateNorm(Map<String, String> normA, String normKey, String key, String value){
        String lowerValue = value.toLowerCase();

        if (normA.containsKey(normKey)){ //Normalized attribute is already in the list
            //The key is the actual value we want to add to the attribute and the value is just a checkmark to mean its true
            if(equals(lowerValue, trueValues)){ //Ex. Spanish: "x" marks the game as being in spanish so instead of adding "x" to languages, we add "spanish"
                String newValue = normA.get(normKey).concat(", ");
                newValue = newValue.concat(key);
                normA.replace(normKey, newValue);          
            }
            else if(!equals(lowerValue, empty)){ //The value isn't a checkmark and isn't null/empty, we can just add it to the attribute
                String newValue = normA.get(normKey).concat(", ");
                newValue = newValue.concat(value);
                normA.replace(normKey, newValue); 
            }
        }
        else{ //Attribute isn't in the list yet
            if(equals(lowerValue, trueValues)){
                normA.put(normKey, key);
            }
            else if(!equals(lowerValue, empty)){
                normA.put(normKey, value);
            }

            if(!GUIDriver.attributes.contains(normKey)){
                GUIDriver.attributes.add(normKey);
            }
        }

        return normA;
    }

    /**
     * Normalizes the given key to match internal labeling conventions.
     * Converts the key to all lowercase and replaces spaces with underscores.
     * 
     * Used in Game.java
     *
     * @param key The input key to normalize
     * @return The normalized key
     */
    public static String normalizeKey(String key) {
        if (key == null || key.isEmpty()) {
            return key; // Return as-is if the key is null or empty
        }
        return key.trim().toLowerCase().replaceAll("\\s+", "_");
    }
}

