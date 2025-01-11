/**
 * The ManualGameEntryTab class represents a tab within the JavaFX application that allows 
 * users to manually enter game data, including both default and custom fields.
 * This class manages the UI for the manual entry of game details, the collection of 
 * that data, and its submission to the main game library and the displayed game list.
 * 
 * The tab allows the user to:
 * - Add multiple game entries.
 * - Specify default fields like game name, platform, and release date.
 * - Add custom fields with key-value pairs.
 * - Submit the entered game details to the library and update the displayed list of games.
 * 
 * The class interacts with the global game library (an ArrayList of Game objects) and
 * the main VBox displaying games within the GUI.
 * 
 * Key components include:
 * - A list of GameEntry objects, each representing a single game entry form.
 * - The layout of the tab, built with JavaFX controls like VBox, HBox, Button, and ScrollPane.
 * - The ability to dynamically add new game entries and custom fields.
 * - Functionality to submit game entries and clear/reset the form.
 * 
 * There is also a nested inner class, GameEntry, that manages individual game entry forms 
 * with default fields (name, platform, etc.) and custom fields (user-defined attributes).
 * 
 * Helper methods overview:
 * - buildUI: Builds the layout and buttons for the manual entry tab.
 * - addGameEntry: Adds a new game entry form to the list.
 * - submitEntries: Submits all entered game data to the library and updates the game list.
 * - getTab: Returns the Tab instance for manual game entry.
 * 
 * Nested class:
 * - GameEntry: Represents a form for entering game details, supporting both default and custom fields
 * 
 *       Nested class:
 *       - CustomField: Represents a custom field in the game entry form
 * 
 * @author GameLoom Team
 * @version 1.0
 */

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.stage.PopupWindow;
import java.util.*;

public class ManualEntryTab {
    private Tab tab; // The actual Tab component for manual game entry
    private List<GameEntry> gameEntries; // List to hold the entries for each game
    private VBox entriesBox; // VBox to hold the entries
    private ArrayList<Game> library; // Reference to the library to add games to
    private VBox gameList; // Reference to the gameList in the GUI to update


    /**
     * Constructor for ManualGameEntryTab.
     * 
     * This initializes the manual game entry tab in the GUI, linking it to the main library and game list.
     * 
     * @param library  A reference to the game's library (used to add new games).
     * @param gameList A reference to the VBox displaying games (used to refresh the display with new games).
     */
    public ManualEntryTab(ArrayList<Game> library, VBox gameList) {
        this.library = library;
        this.gameList = gameList;
        this.gameEntries = new ArrayList<>();
        this.tab = new Tab("Manual Entry");

        // Build the UI
        buildUI();
        entriesBox.getStyleClass().add("fancyBackground");
    }


    /**
     * Builds the layout for the Manual Game Entry tab, including buttons for adding entries and submitting them.
     * 
     * Sets up a ScrollPane for the game entries, and adds buttons for adding new entries and submitting the form.
     */
    private void buildUI() {
        // Create the main layout for the tab
        BorderPane layout = new BorderPane();

        // Create the entriesBox that will hold all the game entries
        entriesBox = new VBox(10);
        entriesBox.setPadding(new Insets(10));

        // Create a ScrollPane to make the entries scrollable
        ScrollPane scrollPane = new ScrollPane(entriesBox);
        scrollPane.setFitToWidth(true);

        // Add the initial game entry
        addGameEntry();

        // Create the '+' button to add more game entries
        Button addButton = new Button("+");
        addButton.setOnAction(e -> addGameEntry());

        // Add a tooltip to the '+' button
        Tooltip addTooltip = new Tooltip("Click to add another game entry.");
        addTooltip.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);
        addTooltip.setShowDelay(javafx.util.Duration.millis(500)); // Delay before tooltip appears
        addTooltip.setHideDelay(javafx.util.Duration.seconds(3)); // Tooltip fades after 3 seconds
        Tooltip.install(addButton, addTooltip);

        // Create the 'Submit' button to submit the entries
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> submitEntries());

        // Add a tooltip to the 'Submit' button
        Tooltip submitTooltip = new Tooltip("Click to submit all valid entries to the library.");
        submitTooltip.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);
        submitTooltip.setShowDelay(javafx.util.Duration.millis(500)); // Delay before tooltip appears
        submitTooltip.setHideDelay(javafx.util.Duration.seconds(3)); // Tooltip fades after 3 seconds
        Tooltip.install(submitButton, submitTooltip);

        // HBox to hold the buttons
        HBox buttonBox = new HBox(10, addButton, submitButton);
        buttonBox.setPadding(new Insets(10));

        // Set the ScrollPane in the center and the buttonBox at the bottom
        layout.setCenter(scrollPane);
        layout.setBottom(buttonBox);

        // Set the content of the tab
        tab.setContent(layout);
    }


    /**
     * Adds a new game entry to the entriesBox and gameEntries list.
     * 
     * This method clears the default placeholders (if present) and dynamically generates new game entry fields.
     */
    private void addGameEntry() {
        // Create a new GameEntry instance
        GameEntry gameEntry = new GameEntry();
        gameEntries.add(gameEntry);

        // Add the UI component to entriesBox
        entriesBox.getChildren().add(gameEntry.getGameEntryBox());
    }


    /**
     * Submits all valid game entries, adds them to the library, and clears them from the manual entry tab.
     * Invalid entries remain on the tab, and their missing fields are highlighted with a red border.
     */
    private void submitEntries() {
        if (gameEntries.isEmpty()) {
            NotificationManager.showNotification("No entries to submit.", "info");
            return;
        }   

        List<GameEntry> validEntries = new ArrayList<>(); // Tracks valid entries to be submitted
        List<GameEntry> invalidEntries = new ArrayList<>(); // Tracks invalid entries that need correction
        int submittedCount = 0; // Tracks the number of successfully submitted entries  

        // Phase 1: Validate each entry and separate them into valid and invalid lists
        for (GameEntry gameEntry : gameEntries) {
            Map<String, String> attributes = gameEntry.collectData();   

            // Validate required fields
            String title = attributes.getOrDefault("title", "").replace("\"", "").trim();
            String platform = attributes.getOrDefault("platform", "").trim();
            String releaseDate = attributes.getOrDefault("release_date", "").trim();    

            boolean isValid = true; 

            // Highlight missing fields and classify entries
            if (title.isEmpty()) {
                gameEntry.highlightField("title");
                isValid = false;
            }
            if (platform.isEmpty()) {
                gameEntry.highlightField("platform");
                isValid = false;
            }
            if (releaseDate.isEmpty()) {
                gameEntry.highlightField("release_date");
                isValid = false;
            }   

            if (isValid) {
                validEntries.add(gameEntry); // Add to valid entries
            } else {
                invalidEntries.add(gameEntry); // Keep invalid entries for further correction
            }
        }   

        // Phase 2: Process valid entries
        for (GameEntry validEntry : validEntries) {
            Map<String, String> attributes = validEntry.collectData();
            Game game = new Game(attributes);       

            // Avoid adding duplicate entries to the library
            boolean isDuplicate = library.stream()
                .anyMatch(existingGame -> existingGame.equals(game)); // Use equals method to compare games
            if (!isDuplicate) {
                library.add(game);
                gameList.getChildren().add(GUIDriver.createGameItem(game.getAttribute("title"), game.toString()));
            }       

            // Remove the valid entry from the UI
            entriesBox.getChildren().remove(validEntry.getGameEntryBox());
            submittedCount++;
        }


        // Phase 3: Update the internal state
        gameEntries.clear(); // Clear the current gameEntries list
        gameEntries.addAll(invalidEntries); // Retain only invalid entries
        
        validEntries.clear(); // Clear all the valid entries in the list (from previous submission)  
        invalidEntries.clear(); // Clear all the invalid entries in the list (from previous submission)  

        // Notify the user about the results
        if (submittedCount > 0) {
            NotificationManager.showNotification(submittedCount + " game(s) successfully submitted!", "success");
        }
        if (!invalidEntries.isEmpty()) {
            NotificationManager.showNotification("Some entries have missing fields. Please review and correct them.", "error");
        }   

        // Automatically add a new blank entry if no entries remain
        if (gameEntries.isEmpty()) {
            addGameEntry();
        }
    }




    /**
     * Returns the constructed Tab for manual game entry.
     * 
     * @return The Tab containing the manual game entry interface.
     */
    public Tab getTab() {
        return tab;
    }

    /**
     * Inner class representing a single game entry form.
     * 
     * Handles the default game fields (e.g., name, platform) and allows for the addition of custom fields.
     */
    private class GameEntry {
        private VBox gameEntryBox; // Container for the game entry UI components
        private Map<String, TextField> defaultFields; // Default fields for common game data (name, platform, etc.)
        private List<CustomField> customFields; // List of custom fields added by the user
        private VBox customFieldsBox; // VBox that holds custom fields for the entry


        /**
         * Constructor for GameEntry.
         * 
         * Initializes the game entry fields and builds the UI for each entry.
         */
        public GameEntry() {
            defaultFields = new HashMap<>();
            customFields = new ArrayList<>();
            buildGameEntryBox();
        }


        /**
         * Builds the VBox containing the UI for this game entry.
         * 
         * This includes default fields (game name, platform, release date) and buttons for adding custom fields.
         */
        private void buildGameEntryBox() {
            gameEntryBox = new VBox(5); // 5px vertical spacing between elements
            gameEntryBox.setPadding(new Insets(10)); // Adds padding around the game entry box
            gameEntryBox.setStyle("-fx-border-color: gray; -fx-border-width: 1;"); // Adds a border to each game entry

            Label defaultFieldsLabel = new Label("Required Fields:"); // Label for default fields section
            // GridPane to layout the default fields in a grid format
            GridPane defaultFieldsGrid = new GridPane();
            defaultFieldsGrid.setHgap(10); // Horizontal gap between grid cells
            defaultFieldsGrid.setVgap(5); // Vertical gap between grid cells

            // Create labels and text fields for default fields
            Label nameLabel = new Label("Title:");
            TextField nameField = new TextField();
            nameField.setPromptText("Example Title"); // Placeholder text for the name field
            defaultFields.put("title", nameField); // Add the field to the defaultFields map

            // Create labels and text fields for platform field
            Label platformLabel = new Label("Platform:");
            TextField platformField = new TextField();
            platformField.setPromptText("Playstation"); // Example platform placeholder
            defaultFields.put("platform", platformField); // Add the field to the defaultFields map

            // Create labels and text fields for release date field
            Label releaseDateLabel = new Label("Release Date:");
            TextField releaseDateField = new TextField();
            releaseDateField.setPromptText("YYYY-MM-DD"); // Date format placeholder
            defaultFields.put("release_date", releaseDateField); // Add the field to the defaultFields map

            // Place all fields in a single row horizontally on the grid
            defaultFieldsGrid.addRow(0, nameLabel, nameField);
            defaultFieldsGrid.addRow(0, platformLabel, platformField);
            defaultFieldsGrid.addRow(0, releaseDateLabel, releaseDateField);

            // Custom fields section
            Label customFieldsLabel = new Label("\nCustom Fields:");
            customFieldsBox = new VBox(5); // VBox to hold custom fields (with 5px spacing)

            // Add Custom Field button
            Button addCustomFieldButton = new Button("Add Custom Field");
            addCustomFieldButton.setOnAction(e -> addCustomField()); // Adds a new custom field when clicked

            // Add all components (default fields, custom fields, and button) to the game entry box
            gameEntryBox.getChildren().addAll(
                defaultFieldsLabel, defaultFieldsGrid, customFieldsLabel, 
                customFieldsBox, addCustomFieldButton
            );
        }

    
        /**
         * Adds a custom field to the game entry.
         * 
         * A custom field consists of a key (field name) and a value (field value).
         */
        private void addCustomField() {
            HBox customFieldBox = new HBox(5); // HBox to hold a key-value pair for the custom field (5px spacing)

            // TextFields for custom key (field name)
            TextField keyField = new TextField();
            keyField.setPromptText("Field Name");
            // TextFields for custom values
            TextField valueField = new TextField();
            valueField.setPromptText("Value");
            // Add labels and text fields to the custom field box
            customFieldBox.getChildren().addAll(new Label("Field:"), keyField, new Label("Value:"), valueField);
            customFieldsBox.getChildren().add(customFieldBox); // Add the custom field box to the VBox that holds custom fields (one is singular one is plural)

            // Store the custom field
            customFields.add(new CustomField(keyField, valueField));
        }


        /**
         * Retrieves the VBox that represents the UI for this game entry.
         * 
         * @return VBox containing the UI components for the game entry.
         */
        public VBox getGameEntryBox() {
            return gameEntryBox;
        }


        /**
         * Highlights a specific field with a red border if it is invalid.
         *
         * @param fieldKey The key of the field to highlight (e.g., "title", "platform").
         */
        public void highlightField(String fieldKey) {
            TextField field = defaultFields.get(fieldKey);
            if (field != null) {
                field.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            }
        }


        /**
         * Collects data from the game entry form, including both default and custom fields,
         * and validates required fields. Highlights empty required fields.
         * 
         * @return A Map containing the key-value pairs of game attributes from the entry form.
         */
        public Map<String, String> collectData() {
            Map<String, String> attributes = new HashMap<>();       

            // Collect data from default fields (e.g., title, platform, release date)
            for (Map.Entry<String, TextField> entry : defaultFields.entrySet()) {
                String key = entry.getKey();
                TextField field = entry.getValue();
                String value = field.getText().trim();      

                // Highlight empty required fields
                if (("title".equals(key) || "platform".equals(key) || "release_date".equals(key)) && value.isEmpty()) {
                    field.setStyle("-fx-border-color: red;"); // Add red border for missing fields
                } else {
                    field.setStyle(null); // Reset border style for valid fields
                }       

                // Ensure the title is enclosed in quotes
                if ("title".equalsIgnoreCase(key) && !value.isEmpty()) {
                    if (!value.startsWith("\"") && !value.endsWith("\"")) {
                        value = "\"" + value + "\"";
                    }
                }       

                attributes.put(key, value); // Add the field to the map
            }       

            // Collect data from custom fields (key-value pairs entered by the user)
            for (CustomField customField : customFields) {
                String key = customField.getKeyField().getText().trim();
                String value = customField.getValueField().getText().trim();
                if (!key.isEmpty()) {
                    attributes.put(key, value); // Add custom fields to the map
                }
            }       

            return attributes;
        }


        /**
         * Inner class representing a custom field in the game entry form.
         * 
         * A custom field consists of a key (field name) and a value (field value).
         */
        private class CustomField {
            private TextField keyField; // Text field for the custom field's key
            private TextField valueField; // Text field for the custom field's value


            /**
             * Constructor for CustomField.
             * Initializes the key and value fields for the custom game attribute.
             * 
             * @param keyField   TextField for the custom field's name (key).
             * @param valueField TextField for the custom field's value.
             */
            public CustomField(TextField keyField, TextField valueField) {
                this.keyField = keyField;
                this.valueField = valueField;
            }

            /**
             * Returns the TextField for the custom field's key.
             * 
             * @return TextField representing the custom field's name (key).
             */
            public TextField getKeyField() {
                return keyField;
            }


            /**
             * Returns the TextField for the custom field's value.
             * 
             * @return TextField representing the custom field's value.
             */
            public TextField getValueField() {
                return valueField;
            }
        }
    }
}
