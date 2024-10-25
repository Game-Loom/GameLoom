import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;


//Like with the search function, will write separate file first, then
//Will refactor code into database

/**
 * This class is a subclass of Game, representing all courses that can be sorted.
 * This class is overrides comparison methods for sort and is the actual sorting implementation.
 * @author Na Huynh 
 * @version JDK-21
 */
public class SortedGame extends Game {
    private Map<String, String> attributes;

    public SortedGame(Map<String, String> attributes) {
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
        SortedGame game1 = this;
        SortedGame game2 = (SortedGame)(obj);
        String title1 = this.getAttribute("Game");
        String title2 = obj.getAttribute("Game");
        
        //Sort by Custom Type (if applicable), Game Title > Platform
        
        if(gamesAreSameType(game1, game2, "customTag"))
        {
            if(game1.getAttribute("customTag"))
            if(title1.equals(title2)) {
                return game1.getPlatform().compareTo(game2.getPlatform());
            } 
            else {
                    return (title1.compareTo(title2));
            }
        } else {
            if(title1.equals(title2)) {
                return game1.getPlatform().compareTo(game2.getPlatform());
            } 
            else {
                    return (title1.compareTo(title2));
            }
        }

    }

    /**
     * This method returns a boolean
     * showing if the two game are of the same type
     * meaning they both have the same custom tag
     * @param game1 the initial game 
     * @return a boolean for the same or not
     */
    private boolean gamesAreSameType(Game game1, Game game2, String type) {
        if(!(game1.getAttribute(type).equals("N/A")) && !(game2.getAttribute(type).equals("N/A"))) {
            return true;
        }
        return false; //dummy code
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