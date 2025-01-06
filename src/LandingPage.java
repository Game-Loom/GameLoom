/**
 * The LandingPage class contains a single public, static method used to create the application's
 * landing page upon opening. This class includes various components to get the user started, 
 * including ways to manual multiple platforms into the libray, entering games manually, and
 * a link to the help tab, where they can find more detailed information about GameLoom.
 * 
 * This page provides the following functionalities:
 * - Displays a welcome message to game loom and instructions on how to import files.
 * - Clickable icons for various platforms that allow users to import CSV files.
 * - Provides a link to manual entry tab (also allows access to the fill library UI).
 * - Provides a link to the GameLoom help tab.
 */
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LandingPage {
    /**
     * The primary stage of the JavaFX application
     */
    private static Stage primaryStage;

    /**
     * The library scene itself
     */
    private static Scene libraryScene;

    /**
     * Creates and formats the main landing page for GameLoom
     * @param stage - The primary stage of the JavaFX aplication
     * @param scene - The scene for the library UI
     * @param libraryTabs - The tabPane that contains the various tabs in the library scene
     * @return a Scene object with the landing page components and formatting
     */
    public static Scene createLandingPage(Stage stage, Scene scene, TabPane libraryTabs){
        primaryStage = stage;
        libraryScene = scene;

        //Sets up the main container for the scene
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);

        //Sets up the landing page itself
        VBox landingPage = new VBox(15);
        landingPage.setPadding(new Insets(20));
        landingPage.setAlignment(Pos.CENTER);
        
        VBox.setVgrow(landingPage, Priority.ALWAYS); //Prioritizes the landing page as the main VBox

        //Creation and styling for the Welcome label
        Label welcome = new Label("Welcome to GameLoom!");
        welcome.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: serif;");

        //Creation and styling for the importation instructions
        Label importCaption = new Label("Start by importing a CSV from a Gameloom Save file or another service.\nClick on one of the icons below!");
        importCaption.setStyle("-fx-font-size: 16px; -fx-text-alignment: center;");

        //Sets up the grid of icons that users can use to import their specific CSV files
        GridPane importIcons = setupIconGrid();

        //Sets up the hyperlink and instruction label for entering a game manually 
        Label manualLabel = new Label("Want to enter a game manually?");
        Hyperlink manualEntry = new Hyperlink("Go straight to the library!");
        manualEntry.setOnAction(event-> {
            primaryStage.setScene(libraryScene); //Switches the current scene to the library UI
            libraryTabs.getSelectionModel().select(8); //Takes users to the manual entry tab directly
        });
        manualLabel.setStyle("-fx-font-size: 14px;");
        manualEntry.setStyle("-fx-font-size: 14px;");

        //Sets up the hyperlink and instructions for accessing the help tab
        Label help = new Label("Don't have a file or don't know where to start?");
        Hyperlink helpPage = setupHelpButton(layout, landingPage);
        help.setStyle("-fx-font-size: 14px;");
        helpPage.setStyle("-fx-font-size: 14px;");
        
        landingPage.getChildren().addAll(welcome, importCaption, importIcons, manualLabel, manualEntry, help, helpPage);
        layout.getChildren().addAll(landingPage);

        Scene mainScene = new Scene(layout, 958, 700);
        return mainScene;
    }

    /**
     * Sets up the grid of clickable icons used to import the user's CSV files
     * @return a GridPane containg the clickable icons
     */
    private static GridPane setupIconGrid(){
        //Sets up the grid pane
        GridPane iconGrid = new GridPane();
        iconGrid.setMinSize(300, 300);
        iconGrid.setAlignment(Pos.CENTER);
        iconGrid.setPadding(new Insets(15));
        iconGrid.setVgap(20);
        iconGrid.setHgap(35);
        
        //Sets up the various icons and labels
        Label steam = setupIcon("imgs/steam.png", "Steam CSV", "Steam");
        Label gog = setupIcon("imgs/gog.png", "GOG CSV", "GOG");
        Label itch = setupIcon("imgs/itch.png", "itch.io CSV", "Itch.io");
        Label playstation = setupIcon("imgs/playstation.png", "Playstation CSV", "Playstation");
        Label xbox = setupIcon("imgs/xbox.png", "Xbox CSV", "Xbox");
        Label nintendo = setupIcon("imgs/nintendo.png", "Nintendo CSV", "Nintendo");
        Label gameloom = setupIcon("imgs/GameLoomIcon.png", "GameLoom CSV", "GameLoom Library");

        //Adds the icons to their respective places inside the grid pane
        iconGrid.add(gameloom, 1, 0);
        iconGrid.add(steam, 0, 1);
        iconGrid.add(gog, 1, 1);
        iconGrid.add(itch, 2, 1);
        iconGrid.add(playstation, 0, 2);
        iconGrid.add(xbox, 1, 2);
        iconGrid.add(nintendo, 2, 2);
        
        return iconGrid;
    }

    /**
     * Sets up the clickable icons for th grid pane
     * @param imagePath = The path for the image used in the icon
     * @param text - The text for the label placed below the icon
     * @param type - The type of platform the CSV was obtained from (GOG, Playstation, etc.)
     * @return a Label representing a clickable icon
     */
    private static Label setupIcon(String imagePath, String text, String type){
        ImageView logo = null;

        try{
            //Gets the image using its given path
            Image image = new Image(new FileInputStream(imagePath), 75, 75, false, false);
            logo = new ImageView(image);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //Sets up the icon's graphic, label, and cursor
        Label icon = new Label(text, logo);
        icon.setContentDisplay(ContentDisplay.TOP);
        icon.setGraphicTextGap(10);
        icon.setStyle("-fx-cursor: hand;");

        //Upon clicking the icon, opens a dialog to choose the CSV file, then switches the scene to the main library UI
        icon.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser(); // Opens a file chooser to select a CSV file
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv")); // Limits file type to CSV
            File selectedFile = fileChooser.showOpenDialog(primaryStage); // Shows the open file dialog

            if (selectedFile != null) { // If a file is selected
                List<Game> importedGames = GLImporter.importGamesFromCSV(selectedFile.getPath(), type); // Imports games from the selected CSV file

                // Only assign the platform if the selected option is not "GameLoom Library" (import an existing library from our program)
                if (!"GameLoom Library".equals(type)) {
                    for (Game game : importedGames) {
                        game.getAttributes().put("platform", type); // Adds platform attribute to each game
                    }
                }

                GUIDriver.populateGameList(importedGames); // Adds games to the game list in the UI
                primaryStage.setScene(libraryScene);
            }
        });
        return icon;
    }

    /**
     * Sets up the hyperlink used to open the GameLoom help tab
     * @param layout
     * @param landingPage
     * @return
     */
    private static Hyperlink setupHelpButton(VBox layout, VBox landingPage){
        Hyperlink help = new Hyperlink("GameLoom Help Page");
        help.setOnAction(event -> {
            //Creates a tab pane for the landing page and the help tab
            TabPane tabs = new TabPane();
            tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            VBox.setVgrow(tabs, Priority.ALWAYS);

            //creates the help tab
            HelpTab helpTab = new HelpTab();
            Tab helpTabInstance = helpTab.getTab();

            //Mains the landing page a new tab
            Tab landingPageTab = new Tab("Main Page");
            landingPageTab.setContent(landingPage);

            tabs.getTabs().addAll(landingPageTab, helpTabInstance);

            layout.getChildren().clear();
            layout.getChildren().addAll(tabs);
            tabs.getSelectionModel().selectNext();
        });

        return help;
    }
}
