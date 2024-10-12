/**
 * Represents all games that can be searched against a list of items
 * @author Na Huynh
 * @version JDK 21
 */
public interface Searchable {

    //sorry my launch.json doesnt sync so i need this here T_T
    //"vmArgs": "--module-path \"C:/Program Files/Java/javafx-sdk-23/lib\" --add-modules javafx.controls,javafx.fxml",

    /**
     * Requires that searchable games must have a boolean representing if a game matches the search
     * Why: For consistency + flexibility to implement child game classes of the game parent class if needed
     * @param game the game that is being search
     * @return boolean representing if a search match is found 
     */
    public boolean matches(Game game, Searcher searcher);

}
