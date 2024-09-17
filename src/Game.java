import java.util.HashMap;
import java.util.Map;

public class Game {
    private Map<String, String> attributes = new HashMap<>();

    public Game(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getAttribute(String key) {
        return attributes.getOrDefault(key, "N/A");
    }
    // Right now this only works for the steam csv but will need to be generalized to the PC storefront the user owns it on ( i was thinking about maybe doing that based on the csv itself, idk yet though)
    public String getPlatform() {
        StringBuilder platforms = new StringBuilder();
        if (getAttribute("win").equalsIgnoreCase("x")) platforms.append("Windows ");
        if (getAttribute("mac").equalsIgnoreCase("x")) platforms.append("Mac ");
        if (getAttribute("linux").equalsIgnoreCase("x")) platforms.append("Linux ");
        if (getAttribute("steam_deck").equalsIgnoreCase("x")) platforms.append("Steam Deck ");
        return platforms.toString().trim();
    }

    @Override
    public String toString() {
        String hoursPlayed = getAttribute("hours").isEmpty() ? "0.00" : getAttribute("hours");
        try {
            hoursPlayed = String.format("%.2f", Double.parseDouble(hoursPlayed));
        } catch (NumberFormatException e) {
            hoursPlayed = "0.00";
        }
        return "Platform: " + getPlatform() +
               ", Hours Played: " + hoursPlayed + 
               ", Release Date: " + getAttribute("release_date");
    }
}
