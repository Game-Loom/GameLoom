import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Arrays;
import java.util.Collections;
import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;
import java.lang.reflect.Method;
import java.util.Scanner;

//"vmArgs": "--module-path \"C:/Program Files/Java/javafx-sdk-23/lib\" --add-modules javafx.controls,javafx.fxml",

/**
 * The Searcher class represents a search entry with multiple search terms.
 * The class takes a search entry and compares it with our hashmap of games.
 */
public class Searcher {

    private ArrayList<String> searchTerms; //arraylist in case we want best match or exact match
    private ArrayList<Game> gamesList;

    public Searcher(ArrayList<Game> gamesList) {
        this.gamesList = gamesList;
    }

    public void setSearch(ArrayList<String> searchTerms) {
        this.searchTerms = searchTerms;
    }

    public void setSearch(String searchTerm) {
        this.searchTerms = new ArrayList<String>();
        searchTerms.add(searchTerm);
    }

    /**
     * Represents a successful search within the Game Titles
     * @param game the game being searched
     * @return an arraylist with all matching games
     */
    //@Override
    public ArrayList<Game> matchesSearch() {
        //if a search term exists in the key return true
        ArrayList<Game> results = new ArrayList<Game>();
        for(int i = 0; i < searchTerms.size(); i++) {
            for(int j = 0; j < gamesList.size(); j++) {
                Game myGame = gamesList.get(j);
                String myAttributes = myGame.getAttribute("title");
                if((myAttributes.indexOf(searchTerms.get(i)) != -1)) {
                    results.add(myGame);
                }
            }
        }
        return results;
    }

    //Turn all the game attributes into a string
    private String gameToString(Game game) {
        return "";
    }

    //testing
    public static void main() {
        ArrayList<String> searchTerm = new ArrayList<String>(Arrays.asList("Steam"));
        Searcher gameSearch = new Searcher(searchTerm);
        ArrayList<Game> results = gameSearch.matches(searchTerm);



    }

}
