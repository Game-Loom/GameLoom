import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class GraphicalUserInterface extends Application {
    private VBox gameList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sample Game Library UI");

        // Top Tabs
        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab("Playstation");
        Tab tab2 = new Tab("Steam");
        Tab tab3 = new Tab("itch.io");
        Tab tab4 = new Tab("Physical Games");
        Tab tab5 = new Tab("Add a Game Library +");
        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4, tab5);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Left side: List of games with image placeholders
        gameList = new VBox(10);
        gameList.setPadding(new Insets(10));

        // Placeholder content before CSV import
        for (int i = 0; i < 5; i++) {
            gameList.getChildren().add(createGameItem("Game Name", "Short Description/Features"));
        }

        // Make the game list scrollable
        ScrollPane scrollPane = new ScrollPane(gameList);
        scrollPane.setFitToWidth(true);

        // Right side: Sort and Filter options
        VBox sortFilterBox = new VBox(10);
        sortFilterBox.setPadding(new Insets(10));
        Label sortFilterLabel = new Label("Sort and Filter");
        Button sortButton = new Button("Sort and Filter");
        VBox filterOptions = new VBox(5);
        for (int i = 0; i < 5; i++) {
            CheckBox option = new CheckBox("Option " + (i + 1));
            filterOptions.getChildren().add(option);
        }
        sortFilterBox.getChildren().addAll(sortFilterLabel, sortButton, filterOptions);

        // Search bar
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10));
        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        Button searchButton = new Button("Search");
        searchBox.getChildren().addAll(searchField, searchButton);

        // Top right: User icon (just a placeholder for now)
        Button userButton = new Button("User Icon");

        // Bottom right: Import button for CSV
        Button importButton = new Button("Import Games from CSV");
        importButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                List<Game> importedGames = GameCSVImporter.importGamesFromCSV(selectedFile.getPath());
                populateGameList(importedGames);
            }
        });

        // Layout for bottom section: Search bar on the left and Import button on the right
        HBox bottomLayout = new HBox();
        bottomLayout.setPadding(new Insets(10));
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        bottomLayout.getChildren().addAll(searchBox, importButton);

        // Main layout
        BorderPane root = new BorderPane();
        root.setTop(tabPane);
        root.setCenter(scrollPane); // Make the game list scrollable
        root.setRight(sortFilterBox);
        root.setBottom(bottomLayout);

        // Create a scene and display the window
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper method to create a game item for the list
    private HBox createGameItem(String name, String description) {
        HBox gameBox = new HBox(10);
        ImageView gameImage = new ImageView();
        gameImage.setFitHeight(50);
        gameImage.setFitWidth(75);
        Label imagePlaceholder = new Label("Image Placeholder");
        gameBox.getChildren().add(gameImage);

        VBox gameDetails = new VBox(5);
        Label gameName = new Label(name);  // Make sure 'name' contains the correct game title
        Label gameDescription = new Label((description != null && !description.equals("N/A")) ? description : "No Description Available");
        gameDetails.getChildren().addAll(gameName, gameDescription);

        gameBox.getChildren().add(gameDetails);
        return gameBox;
    }


    // Method to populate the game list with imported games
    private void populateGameList(List<Game> games) {
        gameList.getChildren().clear();
        for (Game game : games) {
            gameList.getChildren().add(createGameItem(game.getAttribute("game"), game.toString()));
            System.out.println("Game created: " + game.getAttribute("game"));//TESTLINE TESTLINE TESTLINE TESTLINE TESTLINE TESTLINE TESTLINE TESTLINE TESTLINE TESTLINE TESTLINE TESTLINE
        
            // Print all key/value pairs for this game -- FOR TESTING PURPOSES
            System.out.println("Attributes for game: " + game.getAttribute("game"));
            for (Map.Entry<String, String> entry : game.getAttributes().entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
            System.out.println("");
        }
    }
    
    

    public static void main(String[] args) {
        launch(args);
    }
}
