/**
 * The EditTab class represents a tab within the JavaFX application that allows users 
 * to edit existing game data stored in the library. This class manages the UI for 
 * selecting, viewing, modifying, and deleting game entries, as well as adding custom fields.
 * 
 * The tab provides the following functionalities:
 * - Search and filter the existing game library.
 * - Select a game from the list and modify its attributes.
 * - Enter and edit both predefined fields (e.g., name, platform) and custom fields.
 * - Save changes made to game data and reflect updates in the main game library.
 * - Delete selected games with an optional safety confirmation.
 * 
 * The class interacts with the global game library (an ArrayList of Game objects) and
 * the main VBox displaying games within the GUI.
 * 
 * Key components include:
 * - A ListView for displaying the list of games available for editing.
 * - Input fields for selecting keys to edit, entering new values, and adding custom fields.
 * - Buttons for saving changes and deleting games.
 * - A safety checkbox to confirm game deletions.
 * - Tooltips to guide the user on how to use specific components.
 * 
 * Helper methods overview:
 * - setupEditTab: Builds the layout and initializes all components for the edit tab.
 * - filterGames: Filters the displayed game list based on a search query.
 * - loadGameAttributes: Loads the attributes of the selected game into input fields.
 * - updateGame: Applies changes to the selected game's attributes and updates the game list.
 * - deleteGame: Removes the selected game from the library with an optional confirmation dialog.
 * - refreshGameList: Refreshes the displayed list of games in the main VBox.
 * 
 * Event Handling:
 * - Pressing the 'Enter' key within the value input field triggers the save action.
 * - Pressing the 'Enter' key in the custom value input field triggers the save action if the custom key field is populated.
 * 
 * @author GameLoom Team
 * @version 1.0
 */


import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.PopupWindow;
import javafx.util.Duration;
import java.util.*;
import javafx.geometry.Pos;

public class EditTab {

    private Tab editTab;
    private VBox formContainer;
    private TextField searchField;
    private ListView<Game> gameListView;
    private ComboBox<String> keySelector;
    private TextField valueField;
    private TextField customKeyField;
    private TextField customValueField;
    private Button saveButton;
    private Button deleteButton;
    private CheckBox deleteSafetyCheck;
    private ArrayList<Game> library;
    private VBox gameList;
    private static final Map<String, String> keyDisplayMap = new LinkedHashMap<>() {{
        put("title", "Game Title");
        put("platform", "Platform");
        put("console", "Console");
        put("hours_played", "Hours Played");
        put("last_played", "Last Played");
        put("release_date", "Release Date");
        put("singleplayer", "Singleplayer");
        put("multiplayer", "Multiplayer");
        put("languages", "Languages");
    }};
    

    public EditTab(ArrayList<Game> library, VBox gameList) {
        this.library = library;
        this.gameList = gameList;
        this.editTab = new Tab("Edit");
        this.formContainer = new VBox(10);
        setupEditTab();
    }

    /**
     * Sets up the user interface for the Edit tab in the application.
     * This method initializes and configures various JavaFX UI elements including text fields, 
     * buttons, and checkboxes to provide functionality for editing, saving, and deleting game 
     * data. It also adds tooltips and event handling for interactive components.
     * 
     * The main UI components included in this method:
     * - A search bar for filtering games by title or attribute.
     * - A ListView for displaying the games in the library.
     * - A ComboBox for selecting game attributes to edit.
     * - Text fields for inputting new values and custom key-value pairs.
     * - Buttons for saving changes and deleting games.
     * - A checkbox for enabling/disabling delete confirmation with an attached tooltip.
     * 
     * Event handling is set up to allow users to:
     * - Trigger save functionality by pressing the Enter key in text fields.
     * - Display tooltips with delays for user guidance.
     * 
     * Layout configurations, including spacing and alignment, are set for each 
     * component to ensure a structured and user-friendly interface.
     */
    private void setupEditTab() {
        formContainer.setPadding(new Insets(10));
        formContainer.getStyleClass().add("fancyBackground");
        
        // Search bar for filtering games
        searchField = new TextField();
        searchField.setPromptText("Search games...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterGames(newValue));
        
        // Game list view
        gameListView = new ListView<>();
        gameListView.getItems().addAll(library);
        gameListView.setOnMouseClicked(e -> loadGameAttributes(gameListView.getSelectionModel().getSelectedItem()));
        // Set a custom cell factory to display the `toDisplayString` output
        gameListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Game game, boolean empty) {
                super.updateItem(game, empty);
                if (empty || game == null) {
                    setText(null);
                } else {
                    setText(game.toDisplayString()); // Use the custom display method
                }
            }
        }); 

        gameListView.setOnMouseClicked(e -> loadGameAttributes(gameListView.getSelectionModel().getSelectedItem()));    

        // Dropdown for selecting keys
        keySelector = new ComboBox<>();
        keySelector.setPromptText("Select Field");
        keySelector.setPrefWidth(150); // Set to match customKeyField width
        
        // Populate the key selector with display names
        keySelector.getItems().addAll(keyDisplayMap.values());

        
        // Text field for editing the value of the selected key
        valueField = new TextField();
        valueField.setPromptText("Enter new value");
        valueField.setPrefWidth(300);
    
        // Add key event handler for valueField
        valueField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                updateGame();
            }
        });
        
        // Align the saveButton to the right in the same row
        saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> updateGame());
        
        // Spacer to push the save button to the right
        Region saveButtonSpacer = new Region();
        HBox.setHgrow(saveButtonSpacer, Priority.ALWAYS);
        
        // HBox for key selector, value field, and save button
        HBox valueBox = new HBox(10, keySelector, valueField, saveButtonSpacer, saveButton);
        valueBox.setPadding(new Insets(10));
        valueBox.setAlignment(Pos.CENTER_LEFT); // Align to the left
        valueBox.getStyleClass().add("transparent");
        
        // New text fields for custom key input
        customKeyField = new TextField();
        customKeyField.setPromptText("Enter custom field");
        customKeyField.setPrefWidth(150); // Width set
        
        // New text fields for custom value input
        customValueField = new TextField();
        customValueField.setPromptText("Enter custom value");
        customValueField.setPrefWidth(300); // Match the width of valueField
    
        // Add key event handler for customValueField
        customValueField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                if (!customKeyField.getText().trim().isEmpty()) {
                    updateGame();
                }
            }
        });
        
        // Delete safety checkbox and tooltip
        deleteSafetyCheck = new CheckBox("Delete Safety");
        deleteSafetyCheck.setSelected(true);
        
        Label infoIcon = new Label("?"); // Label for the tooltip icon with styling
        infoIcon.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-cursor: hand;");
        Tooltip deleteSafetyTooltip = new Tooltip("When enabled, you will be asked to confirm before deleting a game."); // Explains the purpose of the delete safety check
        deleteSafetyTooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_RIGHT); // BOTTOM_RIGHT is where it's window will meet the cursor to keep it contained inside the program 
        deleteSafetyTooltip.setShowDelay(Duration.millis(100)); // Tooltip appears after 100 milliseconds of hover
        deleteSafetyTooltip.setHideDelay(Duration.seconds(5)); // Tooltip hides 5 seconds after the mouse moves away
        Tooltip.install(infoIcon, deleteSafetyTooltip); // Attach the tooltip to the info icon
        
        // Container for the delete safety check and tooltip icon
        HBox deleteSafetyBox = new HBox(5, deleteSafetyCheck, infoIcon);
        deleteSafetyBox.setAlignment(Pos.CENTER_RIGHT);
        deleteSafetyBox.getStyleClass().add("transparent");

        // Delete button setup
        deleteButton = new Button("Delete Game");
        deleteButton.setOnAction(e -> deleteGame());
        
        // Spacer to push the delete buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // HBox for custom key and value entry along with buttons
        HBox customEntryBox = new HBox(10, customKeyField, customValueField, spacer, deleteSafetyBox, deleteButton);
        customEntryBox.setPadding(new Insets(10));
        customEntryBox.setAlignment(Pos.CENTER_LEFT); // Align contents to the left
        customEntryBox.getStyleClass().add("transparent");
        
        formContainer.getChildren().addAll(searchField, gameListView, valueBox, customEntryBox);
        editTab.setContent(formContainer);
    }
    
    
    
   /**
    * Filters the games displayed in the game list based on the user's search query.
    * If the search query is empty, all games from the library are displayed. If the
    * query contains text, only games whose title or attributes match the query (case-insensitive)
    * are displayed.
    *
    * @param query The search string entered by the user to filter the game list
    */
    private void filterGames(String query) {
        gameListView.getItems().clear(); // Clear the current items in the game list view
    
        if (query.isEmpty()) { // If empty, add all games from the library to the game list view
            gameListView.getItems().addAll(library);
        } else { // If not empty, iterate through the library and check each game
            for (Game game : library) {
                if (game.toDisplayString().toLowerCase().contains(query.toLowerCase())) {
                    gameListView.getItems().add(game);
                }
            }
        }
    }
    

    /**
     * Loads the attributes of the selected game into the UI components for editing.
     * Clears the current selection in the keySelector ComboBox and clears the valueField.
     * Sets up an action listener on the keySelector to populate the valueField with
     * the attribute value from the selected game when a key is selected.
     *
     * @param selectedGame The game selected from the ListView for editing
     */
    private void loadGameAttributes(Game selectedGame) {
        if (selectedGame != null) { // Check if a game is selected in the ListView
            keySelector.getSelectionModel().clearSelection(); // Clear the current selection in the keySelector ComboBox
            valueField.clear(); // Clear the text in the valueField to prepare for new input
    
            keySelector.setOnAction(e -> {
                String selectedDisplayName = keySelector.getValue(); // Get selected key from the keySelector
                String selectedKey = null;
    
                // Map the display name back to the normalized key
                for (Map.Entry<String, String> entry : keyDisplayMap.entrySet()) {
                    if (entry.getValue().equals(selectedDisplayName)) {
                        selectedKey = entry.getKey();
                        break;
                    }
                }
                // If a key is selected, populate the valueField with the corresponding attribute value from the game
                if (selectedKey != null) { 
                    valueField.setText(selectedGame.getAttribute(selectedKey));
                }
            });
        }
    }
    
    

    /**
     * Updates the attributes of the currently selected game in the ListView.
     * If a key is selected and a new value is provided, the game attribute is updated.
     * 
     * Also supports adding a custom key and value if specified by the user.
     * After updating, the method clears the input fields and refreshes the game list.
     */
    private void updateGame() {
        Game selectedGame = gameListView.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            // Retrieve selected key from the dropdown
            String selectedDisplayName = keySelector.getValue();
            String selectedKey = null;
    
            // Map display name back to normalized key
            if (selectedDisplayName != null) {
                for (Map.Entry<String, String> entry : keyDisplayMap.entrySet()) {
                    if (entry.getValue().equals(selectedDisplayName)) {
                        selectedKey = entry.getKey();
                        break;
                    }
                }
            }
    
            // Update the value for the dropdown field if applicable
            if (selectedKey != null) {
                String newValue = valueField.getText().trim();
                if (!newValue.isEmpty()) {
                    selectedGame.updateAttribute(selectedKey, newValue);
                }
            }
    
            // Process custom key-value fields independently
            String customKey = customKeyField.getText().trim();
            String customValue = customValueField.getText().trim();
            if (!customKey.isEmpty() && !customValue.isEmpty()) {
                selectedGame.updateAttribute(customKey, customValue);
                customKeyField.clear();
                customValueField.clear();
            }
            
            // Refresh the displayed game list to reflect changes
            refreshGameList();
        }
    }
    
    

    /**
     * Deletes the currently selected game from the library and updates the UI.
     * If the "Delete Safety" checkbox is checked, a confirmation dialog is shown
     * before proceeding with the deletion.
     * The method ensures the game is removed from both the library and the 
     * displayed game list.
     */
    private void deleteGame() {
        // Get the currently selected game from the ListView
        Game selectedGame = gameListView.getSelectionModel().getSelectedItem();
        // Check if a game is selected
        if (selectedGame != null) {
            // Check if the delete safety feature is enabled
            if (deleteSafetyCheck.isSelected()) {
                // Create an Alert dialog for confirmation
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmation");
                confirmAlert.setHeaderText("Are you sure you want to delete this game?");
                confirmAlert.setContentText("The game(s) will be removed from your GameLoom Library!\nDo you wish to continue?");
                
                // Ensure the dialog resizes to fit the content properly
                confirmAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                
                // Create custom "Yes" and "No" buttons
                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                confirmAlert.getButtonTypes().setAll(yesButton, noButton);
    
                // Display the confirmation dialog and handle the user's response
                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (result.isPresent() && result.get() == yesButton) {
                    // Remove the game from the library and the displayed game list
                    library.remove(selectedGame);
                    gameList.getChildren().removeIf(node -> ((Label) ((VBox) ((HBox) node).getChildren().get(1)).getChildren().get(0)).getText().equals(selectedGame.getTitle()));
                    gameListView.getItems().remove(selectedGame);
                }
            } else {
                // Delete the game directly without showing a confirmation dialog
                library.remove(selectedGame);
                gameList.getChildren().removeIf(node -> ((Label) ((VBox) ((HBox) node).getChildren().get(1)).getChildren().get(0)).getText().equals(selectedGame.getTitle()));
                gameListView.getItems().remove(selectedGame);
            }
        }
    }

    /**
     * Refreshes the game list displayed in the UI by clearing the current
     * contents and repopulating it with updated game data from the library.
     * This ensures that any changes made to the library, such as updates or deletions,
     * are reflected in the UI.
     */
    private void refreshGameList() {
        // Clear all current children in the game list VBox to prepare for updating
        gameList.getChildren().clear();
        // Iterate through each game in the library and add it to the game list
        for (Game game : library) {
            // Create a new game item and add it to the game list VBox
            gameList.getChildren().add(GUIDriver.createGameItem(game.getTitle(), game.toDisplayString()));
        }
    }

    /**
     * Retrieves the Tab instance for the Edit tab.
     * This method allows other parts of the application to access and display
     * the Edit tab within the main GUI.
     *
     * @return The Tab object representing the Edit tab, which contains the user interface
     * for editing and updating game information
     */
    public Tab getTab() {
        return editTab;// Tab instance that contains the edit functionalities
    }
}
