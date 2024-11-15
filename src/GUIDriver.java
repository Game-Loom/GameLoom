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
 * - **setupAutoSave()**: Sets up the auto-save mechanism, including creating the auto-save directory and 
 *   scheduling a periodic timer to check for library changes and trigger auto-save when necessary.
 * 
 * - **calculateLibraryHash()**: Calculates a hash value based on the current library contents, allowing 
 *   the program to detect changes in the library and initiate auto-save only when modifications are detected.
 * 
 * - **setupTabs(Stage primaryStage, TabPane tabPane)**: Configures the various tabs in the interface, 
 *   including the main library tab and platform-specific tabs (Steam, GOG, etc.), and sets their behavior 
 *   to filter the displayed games based on the selected tab.
 * 
 * - **setupSafetyNet(Stage primaryStage)**: Sets up an alert that prompts the user to export the library
 *   before exiting the application, providing options to export, close without saving, or cancel the exit.
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
 * @version 1.6
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
//import javafx.scene.shape.Path; // Conflicts with auto-save java.nio.file.Path but doesn't seem to break anything when I remove it, likely a relic from something old I was doing at some point 
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
// Relates to files & data
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// Relates to auto-save
import java.util.Arrays;
import java.util.Map;
import java.util.Timer; 
import java.util.TimerTask;
import java.security.MessageDigest; // For the MD5 hash
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // End timer imports


import java.util.Collections;
import java.util.Comparator;

public class GUIDriver extends Application {
    // Data Structure Variables
    protected static VBox gameList; // VBox to store the list of game items (games displayed vertically)
    protected static ArrayList<Game> library = new ArrayList<>(); // Game library
    protected static ArrayList<String> attributes = new ArrayList<>(); // Stores the list of game attribute names used for display and export
    private Timer autoSaveTimer; // Schedules periodic auto-save tasks for the game library
    private int lastLibraryHash; // Used to detect any changes to the library and trigger auto-saving when necessary
    
    // Quick-Edit "Control" Variables
    private static final long AUTO_SAVE_INTERVAL = 180000; // 180000 = 3 minutes in milliseconds -- was modifiying to 10000 = 10 seconds for testing
    private static final int MAX_AUTO_SAVE_FILES = 20; // Limit the number of auto-save files (20 * 3 min = version control for your last hour of work if you mess something up)


    @Override
    public void start(Stage primaryStage) {
        //Sets up a safety net for when the user closes the window
        setupSafetyNet(primaryStage);

        // Sets up auto-save functionality based on a hash of full library
        setupAutoSave();

        // Sets the title of the primary stage (main application window)
       primaryStage.setTitle("My Game Library");

       // **Top Tabs**: TabPane to hold all the sections of the application
       TabPane tabPane = new TabPane(); // Holds all the tabs
       tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Prevents tabs from being closed by the user

       // Initialize the shared global game list (VBox)
       gameList = new VBox(10); // VBox with 10px spacing between game items
       gameList.setPadding(new Insets(10)); // Adds padding INSIDE the VBox
       gameList.getStyleClass().add("toTheTop");

       //Sets up the various tabs and their content/actions
        setupTabs(primaryStage, tabPane);

       // **Set Scene and Show Stage**.
       Scene scene = new Scene(tabPane, 1000, 700); // Creates a scene with a width of 1000 and height of 700
       
        /**Sets the style of the scene */
        try{
            File cssFile = new File("styles/Blue-Green(Default).css");
            scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
        }
        catch(Exception e){
            e.printStackTrace();
        }

       primaryStage.setScene(scene); // Sets the scene on the stage
       primaryStage.show(); // Displays the primary stage
    }

    /**
     * Sets up the auto-save mechanism.
     * 
     * Creates a designated folder for auto-save files and starts a background Timer that checks the library for changes at: 
     * AUTO_SAVE_INTERVAL frequency (Quick-Edit Global). If any changes are detected, it triggers an auto-save operation.
     */
    private void setupAutoSave() {
        try {
            Path autoSaveDir = Paths.get(System.getProperty("user.home"), "GameLoom Exports"); // users.home is system agnostic - GameLoom Exports is the folder name it finds/makes
            if (!Files.exists(autoSaveDir)) { // Ensure directory exists for saving files
                Files.createDirectory(autoSaveDir); // Create folder in users home directory if it doesn't already exist
            }
            // Set the initial library hash
            lastLibraryHash = calculateLibraryHash();
            // Schedule the auto-save timer
            autoSaveTimer = new Timer(true); // From docs.oracle: Marks this thread as either a daemon thread or a user thread.
            autoSaveTimer.schedule(new TimerTask() { // ^>The Java Virtual Machine exits when the only threads running are all daemon threads. 
                @Override
                public void run() {
                    checkAndAutoSave(autoSaveDir); // Perform auto-save check at each interval
                }
            }, AUTO_SAVE_INTERVAL, AUTO_SAVE_INTERVAL); // Interval every AUTO_SAVE_INTERVAL milliseconds
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
    * Calculates a hash value based on the current library contents using MD5 hashing.
    * This hash allows the program to detect changes in the library and trigger 
    * auto-save only when modifications are detected.
    *
    * @return An integer hash of the current library's state
    */
    private int calculateLibraryHash() {
       try {
           // Initialize an MD5 MessageDigest instance to compute the hash
           MessageDigest digest = MessageDigest.getInstance("MD5");

           // Iterate through each game in the library and add all attributes to the hash.
           for (Game game : library) { // Hashing the library object will return a hash of the memory address.
               // Retrieve the full attribute map for the game
               Map<String, String> attributes = game.getAttributes();

               // Convert each attribute key-value pair to bytes and update the digest
               for (Map.Entry<String, String> entry : attributes.entrySet()) {
                   digest.update(entry.getKey().getBytes(StandardCharsets.UTF_8)); // Update with attribute key
                   digest.update(entry.getValue().getBytes(StandardCharsets.UTF_8)); // Update with attribute value
               }
           }

           // Complete the hash computation and retrieve the result as a byte array
           byte[] hashBytes = digest.digest();
           return Arrays.hashCode(hashBytes); // Return an integer representation of the hash for easy comparison
       } catch (Exception e) {
           e.printStackTrace();
           return 0;
       }    
    }   


    /**
     * Checks if the library's hash has changed. If changes are detected, a new auto-save file 
     * is created with a timestamped filename, and older auto-save files are cleaned up to stay 
     * within the maximum limit. Current limit set to: 20
     *
     * @param autoSaveDir The directory for saving auto-save files
     */
    private void checkAndAutoSave(Path autoSaveDir) {
        int currentLibraryHash = calculateLibraryHash();
        // Proceed only if changes are detected (hash has changed)
        if (currentLibraryHash != lastLibraryHash) {
            // Generate a timestamp for the filename
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
            File autoSaveFile = autoSaveDir.resolve("GameLoomLibrary-" + timestamp + ".csv").toFile(); // Default filename: GameLoomLibrary-<timestamp>.csv
            // Export the library to the auto-save file
            GLExporter.exportGamesToCSV(library, autoSaveFile);
            lastLibraryHash = currentLibraryHash; // Update last hash to the current hash
            // Clean up older files if file count exceeds MAX_AUTO_SAVE_FILES
            cleanUpOldAutoSaves(autoSaveDir);
        }
    }


    /**
     * Cleans up old auto-save files in the designated auto-save directory by keeping 
     * only the most recent files, as defined by MAX_AUTO_SAVE_FILES. Files are sorted 
     * by creation date, and the oldest are deleted first.
     *
     * @param autoSaveDir The directory containing auto-save files.
     */
    private void cleanUpOldAutoSaves(Path autoSaveDir) {
        try (Stream<Path> files = Files.list(autoSaveDir)) {// Open a stream to list files in the auto-save directory
            List<Path> autoSaveFiles = files
                    .filter(path -> path.getFileName().toString().startsWith("GameLoomLibrary-")) // Filter the files in the directory to keep only those that start with "GameLoomLibrary-" (auto-save files)
                    .sorted(Comparator.comparingLong(path -> path.toFile().lastModified())) // Sort the files by last modified time to arrange them from oldest to newest
                    .collect(Collectors.toList()); // Collect the sorted files into a list

            // Delete oldest files if file count exceeds MAX_AUTO_SAVE_FILES
            if (autoSaveFiles.size() > MAX_AUTO_SAVE_FILES) {
                for (Path file : autoSaveFiles.subList(0, autoSaveFiles.size() - MAX_AUTO_SAVE_FILES)) {
                    Files.deleteIfExists(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the various style buttons and their section
     * @return an Hbox with the various buttons
     */
    private HBox setupStyleChoices(Stage primaryStage){
        //Creates a dropdown menu with the various style/theme options
        ComboBox<String> chooseStyle = new ComboBox<>();
        chooseStyle.getItems().addAll("Blue-Green(Default)", "Dark-Mode", "Black-Red", "Peach-Pink");
        chooseStyle.setPromptText("Choose library theme");
        chooseStyle.setMaxWidth(200);

        Button styleButton = new Button("Set Library Theme");
        styleButton.setDisable(true);

        //Button only works if a style has been chosen from the dropdown menu
        chooseStyle.setOnAction(event -> {
            styleButton.setDisable(chooseStyle.getValue() == null); 
        });

        //When clicked, the button changed the style of the library
        styleButton.setOnAction(event -> {
            String cssFilepath = "styles/" + chooseStyle.getValue() + ".css";

            try{
                File cssFile = new File(cssFilepath);
                primaryStage.getScene().getStylesheets().removeFirst();
                primaryStage.getScene().getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });

        HBox styleSection = new HBox(10);
        styleSection.getChildren().addAll(chooseStyle, styleButton);

        return styleSection;
    }
    
    

    /**
     * Sets up the various tabs in the stage, besides the main library tab.
     * 
     * @param primaryStage - the main stage
     * @param tabPane - the tabpane to add all the tabs onto
     */
    private void setupTabs(Stage primaryStage, TabPane tabPane){
        // **Library Tab**: Main tab to display the library
       Tab libraryTab = new Tab("Full Library");

        // **Additional Tabs**: Placeholder tabs for games sorted by platform (Steam, GOG, etc.) (for Quick-Filter)
       Tab tab1 = new Tab("Steam");
       Tab tab2 = new Tab("GOG");
       Tab tab3 = new Tab("itch.io");
       Tab tab4 = new Tab("Playstation");
       Tab tab5 = new Tab("Xbox");
       Tab tab6 = new Tab("Nintendo");
       Tab tab7 = new Tab("Physical Games");

       //Sets the tabs up so that they display their filtered libraries when selected
        setupTabActions(libraryTab, "", primaryStage);
        setupTabActions(tab1, "steam", primaryStage);
        setupTabActions(tab2, "gog", primaryStage);
        setupTabActions(tab3, "itch.io", primaryStage);
        setupTabActions(tab4, "playstation", primaryStage);
        setupTabActions(tab5, "xbox", primaryStage);
        setupTabActions(tab6, "nintendo", primaryStage);
        setupTabActions(tab7, "physical", primaryStage);

        //Sets up the logos for every tab
        setupTabImages(tab1, "imgs/steam.png");
        setupTabImages(tab2, "imgs/gog.png");
        setupTabImages(tab3, "imgs/itch.png");
        setupTabImages(tab4, "imgs/playstation.png");
        setupTabImages(tab5, "imgs/xbox.png");
        setupTabImages(tab6, "imgs/nintendo.png");
        setupTabImages(tab7, "imgs/physical.png");

       // **Manual Entry Tab**: Allows manual game entries -- separate creation logic in different file (it's kind of big)
       ManualEntryTab manualEntryTab = new ManualEntryTab(library, gameList);
       Tab manualTab = manualEntryTab.getTab(); // Adds a tab for manual game entries

       // **Edit Tab**: Allows editing of game entries -- separate creation logic in different file (it's kind of big)
       EditTab editEntryTab = new EditTab(library, gameList);
       Tab editTab = editEntryTab.getTab();

       // **Help Tab**: Provides useful resources and links -- separate creation logic in different file
       HelpTab helpTab = new HelpTab();
       Tab helpTabInstance = helpTab.getTab(); // Adds a tab for the help page

       // Add all tabs to the TabPane.
       tabPane.getTabs().addAll(libraryTab, tab1, tab2, tab3, tab4, tab5, tab6, tab7, manualTab, editTab,helpTabInstance); // Adds all tabs to the TabPane

    }


    /**
     * Sets up the selection event for the given tab, which is displaying the game list filtered via the given word.
     * 
     * @param tab - the tab to set up the event for
     * @param filter - the word to filter the game library on
     * @param primaryStage - the stage everything is set on
     */
    private void setupTabActions(Tab tab, String filter, Stage primaryStage){
        tab.setOnSelectionChanged(event->{
            if(tab.isSelected()){
                filterGameList(filter); //Filters the library by the given platform
                tab.setContent(createCommonTabLayout(primaryStage)); //Sets the tab layout
            }
        });
    }

    /**
     * Adds an the given image to the given tab
     * 
     * @param tab - the tab that we're setting an image for
     * @param imagePath - the path to the image
     */
    private static void setupTabImages(Tab tab, String imagePath){
        try{
            Image image = new Image(new FileInputStream(imagePath), 25, 25, true, false);
            ImageView logo = new ImageView(image);
            tab.setGraphic(logo);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Sets up a safety net for when the user closes the window by creating an alert popup and asking them if they wish to export the library
     * before they exit. 
     * If they click 'Yes' then they will be given the option to name their export file and pick its location before the window closes.
     * If they click 'No' then the stage will simply close.
     * If they click 'Cancel' then the alert will close and go back to the primary stage.
     * 
     * @param primaryStage the primary stage hosting the GUI
     */
    private void setupSafetyNet(Stage primaryStage){
        primaryStage.setOnCloseRequest(event -> {
            if(!library.isEmpty()){
                //Sets up an alert to pop up when the user exits
                Alert exitAlert = new Alert(AlertType.CONFIRMATION);
                exitAlert.setTitle("Confirm Exit");
                exitAlert.setHeaderText("Would you like to export your library?");
                exitAlert.setContentText("To ensure user privacy and security:\n\n" +
                                        "GameLoom is a network-free experience and will not save your library data internally.\n\n"+
                                        "Would you like to export a csv file containing your library data before exiting?");
                exitAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE); //Resizes dialog to fit text
                //Creates custom buttons and puts them on the alert
                ButtonType exportButton = new ButtonType("Yes");
                ButtonType noExportButton = new ButtonType("No");
                ButtonType cancelButton = ButtonType.CANCEL;
                exitAlert.getButtonTypes().setAll(exportButton, noExportButton, cancelButton);
                //Actions based on  which button was chosen
                Optional<ButtonType> result = exitAlert.showAndWait();
                if(result.get() == exportButton){
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv")); // Limits save type to CSV
                    File newFile = fileChooser.showSaveDialog(primaryStage); // Shows the save file dialog
                    if (newFile != null) {
                        GLExporter.exportGamesToCSV(library, newFile); // Exports the library to a CSV file
                    }
                }
                else if(result.get() == noExportButton ){
                    primaryStage.close();
                }
                else{
                    event.consume();
                }
            }
        });
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
        platformDropdown.getItems().addAll("GameLoom Library", "Steam", "GOG", "Itch.io", "Playstation", "Xbox", "Nintendo"); // Adds options to the dropdown
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
                List<Game> importedGames = GLImporter.importGamesFromCSV(selectedFile.getPath()); // Imports games from the selected CSV file

                // Only assign the platform if the selected option is not "GameLoom Library" (import an existing library from our program)
                if (!"GameLoom Library".equals(selectedPlatform)) {
                    for (Game game : importedGames) {
                        game.getAttributes().put("platform", selectedPlatform); // Adds platform attribute to each game
                    }
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
                    GLExporter.exportGamesToCSV(library, newFile); // Exports the library to a CSV file
                }
            }
        }); 

        return exportButton; // Return the configured button to be added to the layout
    }


    /**
     * Sets up the search bar, including a text field for search input and a search button.
     * When the user enters one or more search keywords, separated by spaces, and clicks the search button,
     * the method will filter the game list based on whether the game names or descriptions contain the search terms.
     * 
     * This method supports case-insensitive, multi-keyword searching. The keywords are matched against the game name 
     * and description.
     * 
     * @return HBox containing the search field and search button.
     */
    private HBox setupSearchBar() {
        HBox searchBox = new HBox(10); // HBox with 10px spacing between elements
        searchBox.setPadding(new Insets(10)); // Adds padding around the search box
        TextField searchField = new TextField(); // Creates a search input field
        searchField.setPromptText("e.g. Name Platform Year"); // Default text to let user know it takes multiple keywords at once
        Button searchButton = new Button("Search"); // Creates the search button

        // Define the action when the search button is clicked
        searchButton.setOnAction(event -> {
            String searchQuery = searchField.getText().toLowerCase().trim(); // Normalize input (lowercase + trim spaces)
            filterGameList(searchQuery); // Call helper method to filter the game list based on the search query
        });

        //Search bar also searches when enter is pressed in the search box
        searchBox.setOnKeyPressed(event -> {
            if( event.getCode() == KeyCode.ENTER ){
                String searchQuery = searchField.getText().toLowerCase().trim();
                filterGameList(searchQuery);
            }
        });

        // Adds search components to the HBox
        searchBox.getChildren().addAll(searchField, searchButton);

        return searchBox; // Returns the search bar HBox
    }

    /**
     * Filters the game list based on a search query entered by the user. 
     * The search query is split into individual keywords, and the method checks whether each game's 
     * name or description contains all the keywords. 
     * 
     * If no search query is provided, the method will display all the games. The filtering is 
     * case-insensitive and supports multi-keyword searches.
     * 
     * @param searchText The search query entered by the user. Multiple keywords should be separated by spaces.
     */
    private void filterGameList(String searchText) {
        gameList.getChildren().clear(); // Clear the current game list in the UI    

        // Split searchText by space to handle multiple keywords
        String[] searchTerms = searchText.split("\\s");    

        // If searchText is empty, display all games when search is clicked
        if (searchText.isEmpty()) {
            for (Game game : library) {
                gameList.getChildren().add(createGameItem(game.getAttribute("game"), game.toString()));
            }
        } else {
            // Filter the games based on the search keyword (searching both game name and description)
            for (Game game : library) {
                String gameName = game.getAttribute("game").toLowerCase(); // Normalize game name to lowercase
                String description = game.toString().toLowerCase(); // Normalize game description to lowercase
                boolean matchFound = true;// Initialize the match flag

                // Check if all search terms are found in the game name or description
                for (String term : searchTerms) {
                    if (!gameName.contains(term) && !description.contains(term)) {
                        matchFound = false; // Set matchFound to false if any term doesn't match
                        break; // Exit the loop early since this game doesn't match
                    }
                }

                // If all terms match, add the game to the displayed game list
                if (matchFound) {
                    gameList.getChildren().add(createGameItem(game.getAttribute("game"), game.toString()));
                }
            }
        }
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
        sortFilterLabel.setStyle("-fx-font-weight: bold");

        // Button for triggering sort/filter functionality
        Button sortButton = new Button("Sort and Filter"); // A button for future sort/filter functionality 


        /** FILTERING FEATURE */
        // Create dummy filter options (We can replace these with whatever key option we want the default filter options to be)
        

        /*HELLO WORLDDDDDDDDDDDDDDDDDDDDDDDDDD! */

        VBox filterOptions = new VBox(5); // VBox with 5px spacing between options
    
        String[] filterNames = {"Platform, Release Date ranging from:, Custom"};
        /* 
        int numberOfOptions = 3;
        for (int i = 0; i < 3; i++) {
            CheckBox option = new CheckBox(filterNames[i] + (i + 1)); // Creates placeholder filter options
            filterOptions.getChildren().add(option); // Adds each option to the VBox
        }  
        */ 

        /** Filter Option 1: By Platform */
        CheckBox platformSelected = new CheckBox("Platform: ");
        TextField platformField = new TextField();
        platformField.setPrefWidth(63); 
        HBox platformFilterBox = new HBox(10, platformSelected, platformField);

        /** Filter Option 3: By Custom Field (Includes "keyword") */
        CheckBox filterKeywordCheckBox = new CheckBox("Includes: ");
        TextField targetField = new TextField();
        targetField.setPromptText("e.g. Fantasy");
        targetField.setPrefWidth(80); 
        Label fieldPromptLabel = new Label("in");
        TextField inAttribute = new TextField();
        inAttribute.setPromptText("e.g. Genre");
        inAttribute.setPrefWidth(80); 
        HBox keywordHBox = new HBox(10, filterKeywordCheckBox, targetField, fieldPromptLabel, inAttribute);

        /** Filter Option 3: Boxes for dates _____ to ______ */
        CheckBox dateSelected = new CheckBox("Year from: ");
        TextField startDate = new TextField();
        startDate.setPromptText("e.g. 1999");
        TextField endDate = new TextField();
        endDate.setPromptText("e.g. 2024");
        Label datePrompts = new Label("to");
        startDate.setPrefWidth(63); 
        endDate.setPrefWidth(63);   
        HBox dateFilterBox = new HBox(10, dateSelected, startDate, datePrompts, endDate);

        platformFilterBox.getStyleClass().add("transparent");
        keywordHBox.getStyleClass().add("transparent");
        dateFilterBox.getStyleClass().add("transparent");

        /** Filter Option 4: By Custom Field (Includes "keyword") */

        /** Filter Option 5: By Custom FIeld (Range) */






        /************ SORTING FEATURE */
        Label sortLabel = new Label("Sort By:");   
        VBox sortOptions = new VBox(5); // VBox with 5px spacing between options

        //Sort Options Dropdown
        ComboBox<String> sortDropDown = new ComboBox<>(); // Dropdown for selecting a platform for game imports
        sortDropDown.getItems().addAll("Title", "Platform", "Date", "Custom"); // Adds options to the dropdown
        sortDropDown.setPromptText("Sort by"); // Sets prompt text in the dropdown
        
        //Adding a label for sort by custom field
        Label customFieldLabel = new Label("Custom Field: ");
        TextField textField = new TextField();
        HBox hb = new HBox();
        hb.getChildren().addAll(textField);

        hb.getStyleClass().add("transparent");

        /** Declared Label for error messages */
        final Label errorMsg = new Label();
        GridPane.setConstraints(errorMsg, 0, 1);
        GridPane.setColumnSpan(errorMsg, 1);

        /** Line Break to separate Alphabetical & Ascending */
        Label lineBreak = new Label("-------------------------------");   
        
        /** Types of Option Formatting **/
        ToggleGroup ascendGroup = new ToggleGroup();
        RadioButton ascendButton = new RadioButton("Ascending");
        RadioButton descendButton = new RadioButton("Descending");
        ascendButton.setToggleGroup(ascendGroup);
        descendButton.setToggleGroup(ascendGroup);

        ToggleGroup alphaGroup = new ToggleGroup();
        RadioButton alphaButton = new RadioButton("Alphabetical");
        RadioButton numButton = new RadioButton("Numerical");
        alphaButton.setToggleGroup(alphaGroup);
        numButton.setToggleGroup(alphaGroup);
        
        //Default Options Selected
        sortDropDown.getSelectionModel().selectFirst();
        alphaButton.setSelected(true);
        ascendButton.setSelected(true);

        //Default Settings For Specific Options
        sortDropDown.setOnAction(event -> {
            String field = sortDropDown.getValue();
            //byDate -- automatically selects numerical
            if(field.equals("Date")) { 
                numButton.setSelected(true);
                numButton.setDisable(false);
                alphaButton.setDisable(true);

            }
            //byTitle or byPlatform - selects alphabetical
            else if(field.equals("Title") || field.equals("Platform")) {
                alphaButton.setSelected(true);
                alphaButton.setDisable(false);
                numButton.setDisable(true);
            } else {
                alphaButton.setDisable(false);
                numButton.setDisable(false);
            }

        });

        //Other Options
        sortButton.setOnAction(event -> {
            String field = sortDropDown.getValue();     
            ArrayList<Game> sortedLibrary  = null;
            boolean isAscending = false;
            boolean isAlphabetical = false;

            if(ascendGroup.getSelectedToggle() != null) 
            {
                RadioButton ans = (RadioButton)ascendGroup.getSelectedToggle();
                isAscending = ans.getText().equals("Ascending"); 
            }
            if(alphaGroup.getSelectedToggle() != null) {
                RadioButton ans = (RadioButton)alphaGroup.getSelectedToggle();
                isAlphabetical = ans.getText().equals("Alphabetical"); 
            }
            
            if(library == null || library.isEmpty()) {
                errorMsg.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
                errorMsg.setText("Please import a library");
            } 
            else {
                if(field.equals("Custom")) {
                    String customFieldText = textField.getText().trim().toLowerCase();
                    if(customFieldText == null || customFieldText.equals("") || customFieldText.length() == 0) {
                        errorMsg.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
                        errorMsg.setText("Please enter a Custom Field");
                    } else {
                        if(customFieldText.equals("hours played")) {
                            customFieldText = "hours";
                        } else if (customFieldText.equals("metacritic score")){
                            customFieldText = "Metacritic Score";
                        } 
                        sortedLibrary = sort(library, field, customFieldText, isAscending, isAlphabetical);            
                    }
                } else {
                    sortedLibrary = sort(library, field, "", isAscending, isAlphabetical);            
                }
                if(sortedLibrary != null) {
                    gameList.getChildren().clear(); //clear game list
                    for(Game game : sortedLibrary) {
                        gameList.getChildren().add(createGameItem(game.getAttribute("game"), game.toString()));
                    }
                }
            }
        });
        
        // Add the components to the VBox
        // sortFilterBox.getChildren().addAll(sortFilterLabel, sortButton, filterOptions);  
        sortFilterBox.getChildren().addAll(
        sortFilterLabel, sortButton,
        filterOptions, platformFilterBox, keywordHBox, dateFilterBox,  //filter options 
        sortLabel,  //sorting options
        sortDropDown, errorMsg, customFieldLabel, hb, sortOptions, ascendButton, 
        descendButton, lineBreak, alphaButton, numButton);  


        
        return sortFilterBox; // Return the fully assembled VBox
    }


    /***** SORTING IMPLEMENTATION */
    /**
     * This method sorts the games library. The sorting logic can be found in the game class.
     * @param library list of games we are sorting
     * @param field the field we are sorting by (i.e. Title, Platform, etc)
     * @param customField the custom field if the custom option is selected
     * @param isAscending whether the order is ascending or not
     * @param isAlphabetical whether the order is alphabetical (unicode), or by numerical value 
     * @return the game library entries sorted 
     */
    private ArrayList<Game> sort(ArrayList<Game> library, String field, String customField, boolean isAscending, boolean isAlphabetical) {
        field = field.trim().toLowerCase();
        Comparator<Game> comparator = null;
        if(field.equals("title")) {
            comparator = Game.byTitle;
        } else if (field.equals("platform")){
            comparator = Game.byPlatform;
        } else if (field.equals("date")){
            comparator = Game.byDate(isAscending);
        } else {
            if(isAlphabetical) {
                comparator = Game.byFieldString(isAscending, customField);
            }
            else {
                comparator = Game.byFieldDouble(isAscending, customField);
            }
        }
        if(!isAscending) {
            comparator = comparator.reversed();
        }
        Collections.sort(library, comparator);
        return library;
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
        //scrollPane.setFitToWidth(true); // Ensures content fits the width   // REMOVED ON 10/26 TO ALLOW FOR HORIZONTAL SCROLLING
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Enable horizontal scrolling as needed
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Enable vertical scrolling as needed
    
        // **Sort and Filter Panel**: Extracted to a helper method
        VBox sortFilterBox = setupSortFilterPanel();    

        // **Search Bar**: Uses helper method for modular search bar setup
        HBox searchBox = setupSearchBar();  

        /// **Import Section**: Platform dropdown and import button
        HBox importSection = setupImportSection(primaryStage); // Using helper method for modular import section setup
        
        /// **Style Section**: Platform dropdown and style/theming button
        HBox styleSection = setupStyleChoices(primaryStage);

        // **Bottom Layout**: Contains search box, dropdown, and buttons
        HBox bottomLayout = new HBox(10); // HBox with 10px spacing between components
        bottomLayout.setPadding(new Insets(10)); // Adds padding around the layout
        HBox.setHgrow(searchBox, Priority.ALWAYS); // Allows the search box to expand on window resize
        Button exportButton = setupExportButton(primaryStage); // Using helper method to modularize logic for export section
        bottomLayout.getChildren().addAll(searchBox, styleSection, importSection, exportButton); // Adds components to the bottom layout  

        // Set components into the layout
        commonLayout.setCenter(scrollPane); // Places the scrollable game list in the center
        commonLayout.setRight(sortFilterBox); // Places the sort/filter options on the right side
        commonLayout.setBottom(bottomLayout); // Places the search and buttons at the bottom
        commonLayout.getStyleClass().add("fancyBackground");
        sortFilterBox.getStyleClass().add("fancyBackground");

        return commonLayout; // Return the fully assembled layout for each tab
    }


    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application.
    }
}
