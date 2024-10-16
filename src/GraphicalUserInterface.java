/**
 * The GraphicalUserInterface class is the main entry point for the JavaFX application that allows users 
 * to manage their personal GameLoom library. The GUI presents the user with multiple tabs for sorting, viewing, 
 * and managing games, as well as manually adding new game entries.
 * 
 * Key methods: (work in progress)
 * 
 * - **start(Stage primaryStage)**: Initializes the main GUI layout, including the tabs, game list, 
 *   and various user interface components. This method is the main entry point for the JavaFX application.
 * 
 * - **createGameItem(String name, String description)**: Creates an HBox that visually represents a 
 *   game in the list, displaying the name, description, and a placeholder image.
 * 
 * - **populateGameList(List<Game> games)**: Adds games from an imported CSV file to the game list 
 *   and the internal library, avoiding duplicate entries.
 * 
 * - **setupImportSection(Stage primaryStage)**: Creates a ComboBox and a button to import games from 
 *   CSV files, allowing users to select a platform and import the corresponding games into the library.
 * 
 * - **setupExportButton(Stage primaryStage)**: Creates a button that allows the user to export the 
 *   current game library to a CSV file.
 * 
 * - **setupSearchBar()**: Creates a search bar that allows users to search for specific games by name 
 *   in the library.
 * 
 * - **setupSortFilterPanel()**: Creates a panel for sorting and filtering the game list based on 
 *   different attributes, allowing users to manage how the game list is displayed.
 * 
 * - **createCommonTabLayout(Stage primaryStage)**: Sets up the layout for each tab in the TabPane, 
 *   ensuring the same scrollable game list and UI elements (search, import/export buttons) are shared 
 *   across different tabs.
 * 
 * The application utilizes a shared VBox (gameList) to ensure that all game tabs (except for the manual 
 * entry tab) display the same synchronized list of games, with the ability to update and manage the 
 * game library.
 * 
 * @author CS321-004: Group 3
 * @version 1.3
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GraphicalUserInterface extends Application {
    protected static VBox gameList; // VBox to store the list of game items (games displayed vertically)
    protected static ArrayList<Game> library = new ArrayList<>(); // Game library

    @Override
    public void start(Stage primaryStage) {
        // Sets the title of the primary stage (main application window)
       primaryStage.setTitle("My Game Library");

       // **Top Tabs**: TabPane to hold all the sections of the application
       TabPane tabPane = new TabPane(); // Holds all the tabs
       tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Prevents tabs from being closed by the user

       // **Library Tab**: Main tab to display the library
       Tab libraryTab = new Tab("Full Library");

       // Initialize the shared global game list (VBox)
       gameList = new VBox(10); // VBox with 10px spacing between game items
       gameList.setPadding(new Insets(10)); // Adds padding INSIDE the VBox

       // Set content for the Library tab and other tabs using the same shared gameList
       libraryTab.setContent(createCommonTabLayout(primaryStage));

       // **Additional Tabs**: Placeholder tabs for games sorted by platform (Steam, GOG, etc.) (Might not need these depending on how search/sort works)
       Tab tab1 = new Tab("Steam", createCommonTabLayout(primaryStage));
       Tab tab2 = new Tab("GOG", createCommonTabLayout(primaryStage));
       Tab tab3 = new Tab("itch.io", createCommonTabLayout(primaryStage));
       Tab tab4 = new Tab("Playstation", createCommonTabLayout(primaryStage));
       Tab tab5 = new Tab("Xbox", createCommonTabLayout(primaryStage));
       Tab tab6 = new Tab("Nintendo", createCommonTabLayout(primaryStage));
       Tab tab7 = new Tab("Physical Games", createCommonTabLayout(primaryStage));

       // **Manual Entry Tab**: Allows manual game entries -- separate creation logic in different file (it's kind of big)
       ManualGameEntryTab manualEntryTab = new ManualGameEntryTab(library, gameList);
       Tab manualTab = manualEntryTab.getTab(); // Adds a tab for manual game entries

       // Add all tabs to the TabPane.
       tabPane.getTabs().addAll(libraryTab, tab1, tab2, tab3, tab4, tab5, tab6, tab7, manualTab); // Adds all tabs to the TabPane

       // **Set Scene and Show Stage**.
       Scene scene = new Scene(tabPane, 800, 600); // Creates a scene with a width of 800 and height of 600
       primaryStage.setScene(scene); // Sets the scene on the stage
       primaryStage.show(); // Displays the primary stage
    }


    /**
     * Creates an HBox containing the game details (name, description) and an image placeholder.
     * This method is used to display each game as an item in the game list
     *
     * @param name        The name of the game to be displayed
     * @param description A short description of the game
     * @return HBox containing the game's image placeholder, name, and description
     */
    protected static HBox createGameItem(String name, String description) {
        HBox gameBox = new HBox(10); // HBox with 10px spacing between elements

        // Creates an ImageView placeholder to represent the game's image
        ImageView gameImage = new ImageView(); // Placeholder image
        gameImage.setFitHeight(50); // Sets image height
        gameImage.setFitWidth(75); // Sets image width
        Label imagePlaceholder = new Label("Image Placeholder"); // Placeholder text for the image until real images are implemented
        gameBox.getChildren().add(gameImage); // Adds the image to the HBox
        // TODO:IMPLEMENT IMAGES FOR GAME ENTRIES

        // Creates a VBox for game name and description
        VBox gameDetails = new VBox(5); // VBox with 5px spacing between elements
        Label gameName = new Label(name); // Adds the name of the game
        Label gameDescription = new Label(description); // Adds the game's description
        // Adds the name and description to the VBox
        gameDetails.getChildren().addAll(gameName, gameDescription);
        // Adds the VBox (game details) to the HBox
        gameBox.getChildren().add(gameDetails);

        return gameBox; // Fully assembled HBox for use in game list
    }


    /**
     * Populates the game list with games imported from a CSV file.
     * It adds the imported games to the game list while avoiding duplicates in
     * the library.
     *
     * @param games A list of Game objects imported from a CSV file
     */
    private void populateGameList(List<Game> games) {
        // Add imported games to the VBox and library, avoiding duplicates
        for (Game game : games) {
            String gameName = game.getAttribute("game"); // Retrieves game name
            String description = game.toString(); // Retrieves game details
            if (!library.contains(game)) { // Avoid adding the same game twice
                library.add(game); // Add game to the library
                gameList.getChildren().add(createGameItem(gameName, description)); // Display game in the UI
            }
        }
    }


    /**
    * Sets up the platform selection dropdown and the import button for importing games from a CSV file.
    * 
    * This method creates a ComboBox to allow the user to select a platform (e.g., Steam, GOG, Playstation)
    * and an import button that is initially disabled until a platform is selected. When the import button 
    * is clicked, it opens a FileChooser to allow the user to select a CSV file for importing games. The 
    * selected platform is added as an attribute to each imported game, and the game list is populated with 
    * the imported games.
    * 
    * @param primaryStage The main stage of the JavaFX application, used for opening dialogs.
    * @return HBox containing both the platform dropdown and the import button, which can be added to the UI.
    */
    private HBox setupImportSection(Stage primaryStage) {
        // **Platform Dropdown**: Added next to the import button for platform selection
        ComboBox<String> platformDropdown = new ComboBox<>(); // Dropdown for selecting a platform for game imports
        platformDropdown.getItems().addAll("Steam", "GOG", "Itch.io", "Playstation", "Xbox", "Nintendo"); // Adds options to the dropdown
        platformDropdown.setPromptText("Choose import type"); // Sets prompt text in the dropdown
        platformDropdown.setMaxWidth(150); // Sets the maximum width of the dropdown

        // **Import Button**: Initially disabled until a platform is selected
        Button importButton = new Button("Import Games from CSV");
        importButton.setDisable(true); // Disables the button until a platform is selected

        // Enable the import button only when a platform is selected
        platformDropdown.setOnAction(event -> {
            importButton.setDisable(platformDropdown.getValue() == null); // Button enabled if platform is selected
        });

        // Action on clicking the import button
        importButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser(); // Opens a file chooser to select a CSV file
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv")); // Limits file type to CSV
            File selectedFile = fileChooser.showOpenDialog(primaryStage); // Shows the open file dialog

            if (selectedFile != null) { // If a file is selected
                String selectedPlatform = platformDropdown.getValue(); // Gets the selected platform from the dropdown
                List<Game> importedGames = GameCSVImporter.importGamesFromCSV(selectedFile.getPath()); // Imports games from the selected CSV file

                // Assign the selected platform to each imported game
                for (Game game : importedGames) {
                    game.getAttributes().put("platform", selectedPlatform); // Adds platform attribute to each game
                }

                populateGameList(importedGames); // Adds games to the game list in the UI
            }
        });

        // Return an HBox containing both the platform dropdown and the import button
        HBox importSection = new HBox(10); // HBox with 10px spacing between elements
        importSection.getChildren().addAll(platformDropdown, importButton); // Add dropdown and button to HBox

        return importSection; // Return the HBox to be used in the main layout
    }


    /**
     * Creates and sets up the export button, which allows the user to export the game library to a CSV file.
     *
     * @param primaryStage The main stage of the JavaFX application, needed for file dialogs
     * @return A Button configured for exporting the game library to CSV
     */
    private Button setupExportButton(Stage primaryStage) {
        // **Export Button**: Allows exporting the library to CSV
        Button exportButton = new Button("Export Games to CSV");
        exportButton.setOnAction(event -> {
            if (library.isEmpty()) { // Shows error if library is empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("CSV Export Error");
                alert.setContentText("Cannot export an empty library! Please add games first.");
                alert.showAndWait();
            } else { // Opens a save dialog for exporting the library
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv")); // Limits save type to CSV
                File newFile = fileChooser.showSaveDialog(primaryStage); // Shows the save file dialog
                if (newFile != null) {
                    GameCSVExporter.exportGamesToCSV(library, newFile); // Exports the library to a CSV file
                }
            }
        }); 

        return exportButton; // Return the configured button to be added to the layout
    }


    /**
    * Sets up the search bar, including the text field and search button.
    * 
    * The search button currently does not have any backend functionality, but 
    * can be updated later to search the game library based on the text entered 
    * in the search field.
    * 
    * @return HBox containing the search field and search button
    */
    private HBox setupSearchBar() {
       // **Search Bar**: User can search for specific games
       HBox searchBox = new HBox(10); // HBox with 10px spacing between elements
       searchBox.setPadding(new Insets(10)); // Adds padding around the search box

       TextField searchField = new TextField(); // Creates a search input field
       searchField.setPromptText("Search"); // Sets placeholder text inside the search field

       Button searchButton = new Button("Search"); // Creates the search button

       // Placeholder action for the search button
       searchButton.setOnAction(event -> {
           String searchText = searchField.getText();
           // Placeholder logic for search functionality
           System.out.println("Searching for: " + searchText); // This will get replaced with the actual search logic, I'm just using it for debugging right now
       });                                                     // Likely from a different file but maybe not, might be able to c/p / integrate Na's existing logic here

       // Adds search components to the HBox
       searchBox.getChildren().addAll(searchField, searchButton); 

       return searchBox; // Returns the search bar HBox
    }   


    /**
     * Sets up the Sort and Filter panel, which contains a label, a sort button,
     * and several dummy filter options. The panel will allow users to sort and 
     * filter the displayed game list.
     * 
     * @return VBox containing the sort/filter panel with label, button, and filter options.
     */
    private VBox setupSortFilterPanel() {
        // **Sort and Filter Panel**: Right side panel with options
        VBox sortFilterBox = new VBox(10); // VBox with 10px spacing
        sortFilterBox.setPadding(new Insets(10)); // Padding around the box 

        // Label for the sort/filter panel
        Label sortFilterLabel = new Label("Sort and Filter");   

        // Button for triggering sort/filter functionality
        Button sortButton = new Button("Sort and Filter"); // A button for future sort/filter functionality 

        // Create dummy filter options (We can replace these with whatever key option we want the default filter options to be)
        VBox filterOptions = new VBox(5); // VBox with 5px spacing between options
        for (int i = 0; i < 5; i++) {
            CheckBox option = new CheckBox("Option " + (i + 1)); // Creates placeholder filter options
            filterOptions.getChildren().add(option); // Adds each option to the VBox
        }   

        // Add the components to the VBox
        sortFilterBox.getChildren().addAll(sortFilterLabel, sortButton, filterOptions);     

        return sortFilterBox; // Return the fully assembled VBox
    }


    /**
     * Creates a layout that includes the shared game list, search bar, sort/filter options,
     * and import/export buttons. This layout will be used for all relevant tabs.
     *
     * @param primaryStage The main stage of the JavaFX application, needed for file dialogs.
     * @return A BorderPane layout containing the common elements for the game library tabs.
     */
    private BorderPane createCommonTabLayout(Stage primaryStage) {
        // **Library Layout**: Organizes the main content in the tab
        BorderPane commonLayout = new BorderPane(); // Uses BorderPane to arrange components    

        // Makes the shared game list scrollable
        ScrollPane scrollPane = new ScrollPane(gameList); // Uses the shared gameList for all tabs
        scrollPane.setFitToWidth(true); // Ensures content fits the width   

        // **Sort and Filter Panel**: Extracted to a helper method
        VBox sortFilterBox = setupSortFilterPanel();    

        // **Search Bar**: Uses helper method for modular search bar setup
        HBox searchBox = setupSearchBar();  

        /// **Import Section**: Platform dropdown and import button
        HBox importSection = setupImportSection(primaryStage); // Using helper method for modular import section setup   

        // **Bottom Layout**: Contains search box, dropdown, and buttons
        HBox bottomLayout = new HBox(10); // HBox with 10px spacing between components
        bottomLayout.setPadding(new Insets(10)); // Adds padding around the layout
        HBox.setHgrow(searchBox, Priority.ALWAYS); // Allows the search box to expand on window resize
        Button exportButton = setupExportButton(primaryStage); // Using helper method to modularize logic for export section
        bottomLayout.getChildren().addAll(searchBox, importSection, exportButton); // Adds components to the bottom layout  

        // Set components into the layout
        commonLayout.setCenter(scrollPane); // Places the scrollable game list in the center
        commonLayout.setRight(sortFilterBox); // Places the sort/filter options on the right side
        commonLayout.setBottom(bottomLayout); // Places the search and buttons at the bottom    

        return commonLayout; // Return the fully assembled layout for each tab
    }


    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application.
    }
}
