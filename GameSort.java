import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;

//Like with the search function, will write separate file first, then
//Will refactor code into database

/**
 * This class is overrides comparison methods for sort and is the actual sorting implementation.
 * @author Na Huynh 
 * @version JDK-21
 */
public class GameSort extends Game {
    private Map<String, String> attributes;

    public Game(Map<String, String> attributes) {
        super(attributes);
    }

    /**
     * This method returns an integer stating whether the initial
     * instance is ordered before or after the compared instance
     * @param obj the game instance being compared to
     * @return an integer stating its order
     */
    @Override
    public int compareTo(Game obj) {
        //Title,  


        return 1; // dummy code
    }

    /**
     * This method returns a boolean
     * showing if the two game are of the same type
     * @param game1 the initial game 
     * @return a boolean for the same or not
     */
    private boolean gamesAreSameType(Game game1, Game game2) {
        return true; //dummy code
    }

    /**
     * This method is returns an integer
     * comparing Games via their Title, then the playtime
     * in ascending order
     * @param game1 the initial game
     * @param game2 the game that is being compared
     * @return an integer stating its order
     */
    private int compareGamesByTitle() {
        
        return 1;
    }   


    /**
     * This method returns an integer
     * comparing Games via their playtime, then title
     * in ascending order
     * @return an integer stating its order
     */
    private int compareGamesByPlaytime() {
        return 1; //dummy code
    }

    /**
     * This method returns an integer
     * comparing Games via their platform, then title
     * in ascending order
     * @return an integer stating its answer
     */
    private int compareGamesByPlatform() {
        return 1;
    }
}