import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;

// import javafx.collections.ObservableList;
// import javafx.scene.Node;

import java.lang.reflect.Method;
import java.util.Scanner;

//ater request
//"vmArgs": "--module-path \"C:/Program Files/Java/javafx-sdk-23/lib\" --add-modules javafx.controls,javafx.fxml",

/**
 * The Searcher class represents a search entry with multiple search terms.
 * The class takes a search entry and compares it with our hashmap of games.
 */
public class Searcher {

    private ArrayList<String> searchTerms; //arraylist in case we want a multi key word search -- a future feature
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
                String myAttributes = myGame.getAttribute("game");
                if((myAttributes.indexOf(searchTerms.get(i)) != -1)) {
                    results.add(myGame);
                }
            }
        }
        return results;
    }
    //testing
    public static void main(String[] args) {
        /* 
        ArrayList<Game> myGameList = new ArrayList<Game>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("title", "Title1");
        Game game = new Game(attributes);
        myGameList.add(game);

        ArrayList<String> searchQuery = new ArrayList<String>();
        searchQuery.add("Title1");

        Searcher search = new Searcher(myGameList);
        search.setSearch(searchQuery);
        ArrayList<Game> results = search.matchesSearch();

        System.out.println("Size of list: " + results.size());
        for(Game gameElement : results) {
            System.out.println("game results " + gameElement);
        }
            */
    }
        
}
