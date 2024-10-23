import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;

/**
 * This GameCatalog class represents the SORTED list of all games
 * This is like the driver code.
 * This will be refactored.
 * This is the logic equivalent of being in the GUI class, where we will call: Collections.sort(Game, Title);
 * @author Na Huynh
 * @version JDK-21
 */
public class GameCatalog {
    private ArrayList<Game> gamesList; //list of games
    public GameCatalog(ArrayList<Game> gamesList) { 
            super();
            this.gamesList = gamesList;
            sort();
    }

    //this will sort the catalog in ascending order
    public void sort() {
        Collections.sort(gamesList);
    }
 }

