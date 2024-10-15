import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class GraphicalUserInterface extends Application{
    private VBox gameList;// VBox to store the list of game items (games are displayed in a vertical box layout).
    protected static ArrayList<Game> library = new ArrayList<>(); //Game library

    @Override
    public void start(Stage primaryStage){
        // Sets the title of the primary stage (the main application window).
        primaryStage.setTitle("Sample Game Library UI");

        // Top Tabs
        TabPane tabPane = new TabPane();// Holds all the tabs
        Tab tab1 = new Tab("Playstation");
        Tab tab2 = new Tab("Steam");
        Tab tab3 = new Tab("itch.io");
        Tab tab4 = new Tab("Physical Games");
        Tab tab5 = new Tab("Add a Game Library +");
        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4, tab5);// Adds all the tabs to the actual TabPane
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);// Disables the ability to close the tabs (tabs can't be closed by the user) - maybe we do the single tab with just filtering, either way we might want this

        // Left side: List of games with image placeholders
        gameList = new VBox(10);// Creates a VBox (vertical layout) to display the list of games
        gameList.setPadding(new Insets(10));// Sets spacing between elements

        // Placeholder content: Before importing from CSV, needed to display some placeholder lines - this might still apply if we do that guaranteed export and just re-import each time
        for (int i = 0; i < 5; i++) {// Only making 5 placeholder game lines to start
            gameList.getChildren().add(createGameItem("Game Name", "Short Description/Features"));// Just dummy lines
        }

        // Create a ScrollPane so that the list of games can be scrolled if it's too long
        ScrollPane scrollPane = new ScrollPane(gameList);
        scrollPane.setFitToWidth(true);// Ensures the content of the scroll pane adjusts to the width of the window

        // Right side: Sort and Filter options
        VBox sortFilterBox = new VBox(10);// Spacing of 10 between children
        sortFilterBox.setPadding(new Insets(10));// Padding around the VBox
        Label sortFilterLabel = new Label("Sort and Filter");// Label for the sort/filter section
        Button sortButton = new Button("Sort and Filter");// Button for applying sorting and filtering
        
        // Creates a VBox to hold filter options (as checkboxes)
        VBox filterOptions = new VBox(5);// Spacing of 5 between filter options
        for (int i = 0; i < 5; i++){// Making 5 placeholder filter options
            CheckBox option = new CheckBox("Option " + (i + 1));// Each checkbox labeled "Option x" for now
            filterOptions.getChildren().add(option);// Add the checkbox to the filterOptions VBox
        }
        // Adds the sort/filter label, button, and filter options to the sortFilterBox VBox
        sortFilterBox.getChildren().addAll(sortFilterLabel, sortButton, filterOptions);

        // Creates an HBox for the search bar (horizontal layout)
        HBox searchBox = new HBox(10);// Spacing of 10 between search field and button
        searchBox.setPadding(new Insets(10));// Adds padding around the search box
        TextField searchField = new TextField();// Creates a text field for entering search queries
        searchField.setPromptText("Search");// Sets the greyed out placeholder text inside the search field
        Button searchButton = new Button("Search");// Button to trigger the search action
        
        // Add the search field and button to the search box (left to right layout).
        searchBox.getChildren().addAll(searchField, searchButton);

        // Top right: User icon (just a placeholder for now, not actually implemented and placed on the window yet)
        Button userButton = new Button("User Icon");// Creates a button to represent the user icon if we choose to take the account route in whatever form

        // Bottom right: Import button for CSV
        Button importButton = new Button("Import Games from CSV");// Button to import games from CSV file
        // Makes an action occur when the import button is clicked
        importButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();// Opens a file chooser dialog to select the CSV file (this ends up being the OS window that lets us pick from our files)
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));// Filters the file chooser to show only CSV files
            File selectedFile = fileChooser.showOpenDialog(primaryStage);// Shows the open dialog and gets the selected file (actually displaying that OS file explorer window)
            if (selectedFile != null) {// If a file is selected, import the games from the CSV file.
                List<Game> importedGames = GameCSVImporter.importGamesFromCSV(selectedFile.getPath());
                populateGameList(importedGames);// Populates the game list with the imported games.
            }
        });

        //Creates new button to export game library to CSV
        Button exportButton = new Button("Export Games to CSV");
        exportButton.setOnAction(event->{
            if(library.isEmpty()){ //Creates an error dialog if the library is empty
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("CSV Export Error");
                alert.setContentText("Cannot export an empty library! Please add more games and try again.");

                alert.showAndWait();
            }
            else{
                FileChooser fileChooser = new FileChooser(); //Creates a new file chooser
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv")); //Makes sure user saves a csv file
                File newFile = fileChooser.showSaveDialog(primaryStage); //Opens new window to save the file where the user chooses
                if(newFile != null){
                    GameCSVExporter.exportGamesToCSV(newFile); //Exports library to given file
                }
            }            
        });

        // Bottom section
        HBox bottomLayout = new HBox();// HBox for the bottom section (search bar and import button side by side).
        bottomLayout.setPadding(new Insets(10));// Adds padding around the bottom section
        // Allows the search box to grow horizontally if the window is resized
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        // Adds the search box (on the left) and the import and export buttons (on the right) to the HBox
        bottomLayout.getChildren().addAll(searchBox, importButton, exportButton);

        // Main layout
        BorderPane root = new BorderPane();// Holds BorderPane layout (top, bottom, center, right, and left regions)
        root.setTop(tabPane);// Set the tabPane at the top of the BorderPane
        root.setCenter(scrollPane); // Set the scrollable game list in the center
        root.setRight(sortFilterBox);// Set the sort and filter box on the right
        root.setBottom(bottomLayout);// Set the search and import button section at the bottom
        // If we wanted to add in that account button that has a placeholder above, this is the general block we would add it to ^^

        // Creates a Scene to hold the root layout and set its width and height
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);// Sets the scene on the primary stage (the window)
        primaryStage.show();// Displays the primary stage (show the window)
    }

    // Helper method to create an HBox for each game item to be displayed in the game list
    private HBox createGameItem(String name, String description){
        // Creates an HBox to hold the game image and details
        HBox gameBox = new HBox(10);// Spacing of 10 between children
        // Creates an ImageView to display the game image (currently just a placeholder)
        ImageView gameImage = new ImageView();
        gameImage.setFitHeight(50);// Sets the image's height
        gameImage.setFitWidth(75);// Set the image's width
        Label imagePlaceholder = new Label("Image Placeholder");// Creates a label to serve as a placeholder for the game image
        gameBox.getChildren().add(gameImage);// Adds the image to the HBox

        // Creates a VBox to hold the game name and description (vertical layout)
        VBox gameDetails = new VBox(5);// Spacing of 5 between name and description
        // Creates a label for the game name and adds it to the VBox
        Label gameName = new Label(name);// Uses the provided 'name' as the game title
        // Creates a label for the game description and add it to the VBox
        Label gameDescription = new Label((description != null && !description.equals("N/A")) ? description : "No Description Available");// If no description is provided, show "No Description Available"
        gameDetails.getChildren().addAll(gameName, gameDescription);// Adds the name and description to the VBox
        // Add the name and description to the VBox
        gameBox.getChildren().add(gameDetails);
        return gameBox;// Returns the HBox representing the game item
    }


    // Populates the game list with a list of imported games
    private void populateGameList(List<Game> games){
        gameList.getChildren().clear();// Clears the current game list to make room for the newly imported games
        for (Game game : games) {// Loops through each imported game
            library.add(game);
            gameList.getChildren().add(createGameItem(game.getAttribute("game"), game.toString()));// Adds the game to the game list as a new game item (created with the helper method)
            
            //System.out.println("Game created: " + game.getAttribute("game"));//FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES
        
            // // Prints all attributes (key-value pairs) for the current game -- FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES FOR TESTING PURPOSES
            //System.out.println("Attributes for game: " + game.getAttribute("game"));
            //for (Map.Entry<String, String> entry : game.getAttributes().entrySet()){// Just a separate map that holds all the game entries (didn't want to manipulate the original)
                //System.out.println(entry.getKey() + " : " + entry.getValue());// Just spacing the entries for printing to console
           // }
           // System.out.println("");// Just separates each full game attributes list with a blank line
        }
    }
    
    

    public static void main(String[] args){
        launch(args);// Launchs the actual JavaFX application
    }
}
