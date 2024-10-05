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

/**
 * The Searcher class represents a search entry with multiple search terms.
 * The class takes a search entry and compares it with our hashmap of games.
 */
public class Searcher {

    private Collection<String> searchTerms;

    public Searcher(Collection<String> searchTerms) {
        this.searchTerms = new ArrayList<String>(searchTerms);
    }

    /**
     * Represents a successful search
     * @param game the game being searched
     * @return boolean if a match is found -- there exists a game that matches the search
     */
    @Override
    public boolean matches(Game game) {
            //if a search term exists in the key return true

            //if a search term exists in the value (game details) return true
            gameToString(game);
            

    }

    //Turn all the game attributes into a string
    private String gameToString(Game game) {
        
    }

    //testing
    public static void main() {
        ArrayList<String> searchTerm = new ArrayList<String>(Arrays.asList("Steam"));
        Searcher gameSearch = new Searcher(searchTerm);
        ArrayList<Game> results = gameSearch.matches(searchTerm);



    }

}
