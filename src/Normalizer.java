import java.util.*;

public class Normalizer {
    /*Lists of all the different versions of a normalized attribute*/
    //Since these lists won't change once we make them I just made them all constant String arrays, feel free to change if there's a more efficient way
    private static final String game[] = {"game", "name", "title"};
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

    /**
     * Normalizes the given attribute list.
     * @param attributes - An un-normalized attribute list. Obtained directly from CSV import.
     * @return A mapping of normalized attributes and their values
     */
    public static Map<String, String> normalize(Map<String, String> attributes){
        Map<String, String> normAttributes = new LinkedHashMap<>();

        for(String key: attributes.keySet()){
            String value = attributes.get(key);

            //Normalizes attributes that are for the game's name. The condition for the attribute not containing id is to prevent the program from considering "game id"
            //The last condition is to make sure that once we've found the game's title, no other attributes with the word "game" in it will be considered
            if(contains(key, game) && !key.contains("id") && !normAttributes.containsKey("game")){ 
                normAttributes = populateNorm(normAttributes, "game", key, value);
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
                if(!GraphicalUserInterface.attributes.contains(key)){
                    GraphicalUserInterface.attributes.add(key);
                }
            }
        }

        return normAttributes;
    }

    /**
     * Checks if the given attribute contains some version in the given normalized list.
     * @param attribute - An un-normalized attribute
     * @param normalized - One of the constant lists of words we can normalize
     * @return Whether or not the attribute contains one of the words in the given array
     */
    private static boolean contains(String attribute, String[] normalized){
        //I chose to use contains for the attribute names because it prevents us from having to consider all the versions of "game", "game-name", "game name", "game_name", etc.
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

            if(!GraphicalUserInterface.attributes.contains(normKey)){
                GraphicalUserInterface.attributes.add(normKey);
            }
        }

        return normA;
    }

}

