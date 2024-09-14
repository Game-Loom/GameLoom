import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class GraphicalUserInterface extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sample Game Library UI");

        // Top Tabs
        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab("PS4 Games");
        Tab tab2 = new Tab("Steam Games");
        Tab tab3 = new Tab("Physical Games");
        Tab tab4 = new Tab("itch.io Games");
        Tab tab5 = new Tab("Add a Game Library +");
        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4, tab5);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Left side: List of games with image placeholders
        VBox gameList = new VBox(10);
        gameList.setPadding(new Insets(10));
        for (int i = 0; i < 5; i++) {
            HBox gameBox = new HBox(10);

            // Placeholder for image
            ImageView gameImage = new ImageView();
            gameImage.setFitHeight(50);
            gameImage.setFitWidth(75);
            Label imagePlaceholder = new Label("Image Placeholder");
            gameBox.getChildren().add(gameImage);

            // Game details: name and description
            VBox gameDetails = new VBox(5);
            Label gameName = new Label("Game Name");
            Label gameDescription = new Label("Short Description/Features");
            gameDetails.getChildren().addAll(gameName, gameDescription);

            gameBox.getChildren().add(gameDetails);
            gameList.getChildren().add(gameBox);
        }

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

        // Main layout
        BorderPane root = new BorderPane();
        root.setTop(tabPane);
        root.setCenter(gameList);
        root.setRight(sortFilterBox);
        root.setBottom(searchBox);

        // Create a scene and display the window
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
