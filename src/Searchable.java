/**
 * Represents all games that can be searched against a list of items
 * @author Na Huynh
 * @version JDK 21
 */
public interface Searchable {

    /**
     * Requires that searchable games must have a boolean representing if a game matches the search
     * Why: For consistency + flexibility to implement child game classes of the game parent class if needed
     * @param game the game that is being search
     * @return boolean representing if a search match is found 
     */
    public boolean matchesSearch(Game game, Searcher searcher);

}
