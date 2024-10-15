import java.util.HashMap;
import java.util.Map;

public class Game {
    private Map<String, String> attributes = new HashMap<>();
    // Stores the attributes for each game, attribute name is key and attribute value is value (k/v pairs) 
    public Game(Map<String, String> attributes) {
        this.attributes = attributes;
    }
    // Constructor that accepts a map of attributes and initializes the game object's attributes
    public String getAttribute(String key){// Trim and toLowerCase were just to make sure the key is normalized
        return attributes.getOrDefault(key.trim().toLowerCase(), "N/A");// If it can't find a key it'll default to N/A
    }

    // Gets the attributes map -- for testing purposes right now (may end up being utilized though)
    public Map<String, String> getAttributes(){// I had been using it to print the k/v pairs to console while I was debugging
        return attributes;
    }
    // Right now this only works for the Steam csv but will need to be generalized to the PC storefront the user owns it on.
    public String getPlatform(){ // I think the best option here may actually be to modify the storefront during import, maybe we put a secondary box where people can name their own platforms alongside choosing the csv to import (would cover physical stuff too in this way)
        StringBuilder platforms = new StringBuilder();// Super cheap fix for bundling the PC -- unlikely to stay this way for long and will probably handle it differently because this is junk hardcoding
        if (getAttribute("win").equalsIgnoreCase("x")) platforms.append("Windows ");
        if (getAttribute("mac").equalsIgnoreCase("x")) platforms.append("Mac ");
        if (getAttribute("linux").equalsIgnoreCase("x")) platforms.append("Linux ");
        if (getAttribute("steam_deck").equalsIgnoreCase("x")) platforms.append("Steam Deck ");
        // Super cheap fix for bundling the PC platforms (cont. junk hardcoding)
        if(platforms.indexOf("Windows") != 1 || platforms.indexOf("Mac") != 1 || platforms.indexOf("Linux") != 1 || platforms.indexOf("Steam Deck") != 1){// If any of the PC OSes were clocked it changes the platform to PC
            platforms.replace(0,platforms.length(), "PC");
        }
        return platforms.toString().trim();// Trim is just for normalizing
    }
    
    // Can be modified to to include whatever information we would like
    @Override
    public String toString() {// Just your standard custom class toString - right now shows metacritic score > platform > hours played > release date
        String hoursPlayed = getAttribute("hours").isEmpty() ? "0.00" : getAttribute("hours");
        String metacriticScore = getAttribute("metascore").isEmpty() ? "N/A" : getAttribute("metascore");
        try {// Only need this to parse metacriticScore as an integer
            metacriticScore = String.format("%d", Integer.parseInt(metacriticScore));
        } catch (NumberFormatException e) {
            metacriticScore = "N/A";// Failure results in default N/A
        }
        try {// Only need this to parse hoursPlayed as a double
            hoursPlayed = String.format("%.2f", Double.parseDouble(hoursPlayed));
        } catch (NumberFormatException e) {
            hoursPlayed = "0.00";// Failure results in default 0.00
        }
        return "Metacritic Score: "+metacriticScore +
               ", Platform: " + getPlatform() +
               ", Hours Played: " + hoursPlayed + 
               ", Release Date: " + getAttribute("release_date");
    }
}
