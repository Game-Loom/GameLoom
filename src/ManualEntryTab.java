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

        // Create the 'Submit' button to submit the entries
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> submitEntries());

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
     * Submits all game entries by collecting data from each entry, validating required fields,
     * and adding valid entries to the library and the displayed game list.
     * Displays appropriate notifications for success or validation errors.
     */
    private void submitEntries() {
        if (gameEntries.isEmpty()) {
            NotificationManager.showNotification("No entries to submit.", "info");
            return;
        }   

        boolean hasError = false; // Tracks if any errors are found
        int submittedCount = 0;  // Tracks the number of successfully submitted entries 

        for (GameEntry gameEntry : gameEntries) {
            Map<String, String> attributes = gameEntry.collectData();   

            // Ensure all required fields are not empty
            String title = attributes.getOrDefault("title", "").replace("\"", "").trim();
            String platform = attributes.getOrDefault("platform", "").trim();
            String releaseDate = attributes.getOrDefault("release_date", "").trim();    

            if (title.isEmpty() || platform.isEmpty() || releaseDate.isEmpty()) {
                hasError = true;
                NotificationManager.showNotification(
                    "All required fields (Title, Platform, Release Date) must be filled for each entry.",
                    "error"
                );
                continue; // Skip invalid entry and continue with the next one
            }   

            // If valid, add the entry to the library and display
            Game game = new Game(attributes);
            library.add(game);
            gameList.getChildren().add(GUIDriver.createGameItem(game.getAttribute("title"), game.toString()));
            submittedCount++;
        }   

        // If no valid entries were submitted, notify the user
        if (submittedCount == 0) {
            NotificationManager.showNotification("No valid entries were submitted.", "error");
            return;
        }   

        // Clear the entries and reset the form
        entriesBox.getChildren().clear();
        gameEntries.clear();
        addGameEntry(); 

        // Show success notification
        NotificationManager.showNotification(submittedCount + " game(s) successfully submitted!", "success");
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
