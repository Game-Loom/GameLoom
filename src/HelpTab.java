/**
 * The HelpTab class represents a tab within the JavaFX application that provides users 
 * with helpful resources and guidance for using the GameLoom application. This class manages 
 * the UI for displaying information and linking to various support resources, including external 
 * guides, frequently asked questions, support contact information, and recommended exporters.
 * 
 * The tab provides the following functionalities:
 * - Displays a welcome message and a brief introduction to the help resources.
 * - Provides clickable links to the user guide, FAQ, and support contact page.
 * - Lists recommended exporters with descriptions and tutorial video links for each exporter.
 * 
 * The class is designed to assist users in accessing key resources to learn more about GameLoom 
 * and how to use external tools for managing their game libraries.
 * 
 * Key components include:
 * - Header text with a welcome message and introduction to available resources.
 * - Clickable hyperlinks for the GameLoom User Guide, FAQ, and Contact Support.
 * - Links to recommended exporters with descriptions for each (e.g., Steam, PlayStation).
 * - Tutorial video links corresponding to each exporter to guide users in exporting their libraries.
 * - A ScrollPane that contains all elements and dynamically adjusts to fit the window.
 * 
 * Helper methods overview:
 * - setupHelpTab: Builds the layout and initializes all components for the help tab.
 * - openLink: Opens a specified URL in the system's default web browser, supporting cross-platform compatibility.
 * 
 * Layout and Styling:
 * - The content is center-aligned within a VBox and styled to match the application's theme.
 * - Each exporter is displayed in two columns: one for the exporter link and another for the corresponding tutorial video.
 * - The ScrollPane ensures the content is scrollable and fills the window area with a blue background.
 * 
 * @author CS321-004: Group 3
 * @version 1.0
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class HelpTab {

    private Tab helpTab;

    /**
     * Constructor for HelpTab. Sets up the layout and content for the help page.
     */
    public HelpTab() {
        this.helpTab = new Tab("Help");
        this.helpTab.setClosable(false); // Prevent the user from closing the tab
        setupHelpTab();
    }

    /**
     * Sets up the content of the Help tab, including clickable links and explanatory text.
     */
    private void setupHelpTab() {
        VBox contentBox = new VBox(15); // Main container for the help tab content
        contentBox.setPadding(new Insets(20));
        contentBox.setAlignment(Pos.CENTER); // Center the content within contentBox
    
        // Header text
        Label header = new Label("Welcome to the GameLoom Help Page");
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
    
        // Paragraph explaining the links
        Text paragraph = new Text(
            "Here you can find useful resources and guidance on using the GameLoom application. "
            + "Click on the links below for more detailed instructions or to access external resources:"
        );
        paragraph.setStyle("-fx-fill: white;");
        TextFlow paragraphFlow = new TextFlow(paragraph);
        paragraphFlow.setTextAlignment(javafx.scene.text.TextAlignment.CENTER); // Center align paragraph text
    
        // Links and descriptions for general help
        Hyperlink userGuideLink = new Hyperlink("GameLoom User Guide");
        userGuideLink.setOnAction(e -> openLink("https://example.com/user-guide"));
        
        Hyperlink faqLink = new Hyperlink("Frequently Asked Questions");
        faqLink.setOnAction(e -> openLink("https://example.com/faq"));
        
        Hyperlink supportLink = new Hyperlink("Contact Support");
        supportLink.setOnAction(e -> openLink("https://example.com/support"));
        
        Label userGuideText = new Label("Learn how to use all features of GameLoom effectively.");
        Label faqText = new Label("Find answers to common questions about GameLoom.");
        Label supportText = new Label("Need help? Reach out to our support team.");
        
        // Create centered VBoxes for each link and description
        VBox userGuideBox = new VBox(userGuideLink, userGuideText);
        userGuideBox.setAlignment(Pos.CENTER); // Center-align the content
        
        VBox faqBox = new VBox(faqLink, faqText);
        faqBox.setAlignment(Pos.CENTER); // Center-align the content
        
        VBox supportBox = new VBox(supportLink, supportText);
        supportBox.setAlignment(Pos.CENTER); // Center-align the content
        
        // Add the centered VBoxes to the main linkBox
        VBox linkBox = new VBox(10);
        linkBox.setAlignment(Pos.CENTER); // Center-align the entire box of links
        linkBox.getChildren().addAll(userGuideBox, faqBox, supportBox);
        
        // Exporters section header
        Label exporterHeader = new Label("Recommended Exporters");
        exporterHeader.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        // Links and descriptions for recommended exporters
        Hyperlink steamExporterLink = new Hyperlink("Steam Library CSV Exporter");
        steamExporterLink.setOnAction(e -> openLink("https://www.lorenzostanco.com/lab/steam/"));
        Label steamExporterText = new Label("Export your Steam game library to a CSV file for easy automated import.");
        
        Hyperlink playstationExporterLink = new Hyperlink("Playstation PSDLE Exporter");
        playstationExporterLink.setOnAction(e -> openLink("https://repod.github.io/psdle/"));
        Label playstationExporterText = new Label("Export your Playstation Network library to a CSV file for easy automated import.");
        
        Hyperlink itchIoExporterLink = new Hyperlink("Itch.io Exporter");
        itchIoExporterLink.setOnAction(e -> openLink("https://gist.github.com/abraxas86/ad72ba46b6cdd86dc63058bba0c629c2#file-itchiocollectiontocsv-user-js"));
        Label itchIoExporterText = new Label("Export your itch.io library to a CSV file for easy automated import.");
        
        Hyperlink NintendoExporterLink = new Hyperlink("Nintendo Switch Exporter");
        NintendoExporterLink.setOnAction(e -> openLink("https://www.gamebrew.org/wiki/Switch_Library_Manager"));
        Label NintendoExporterText = new Label("Export your Nintendo Switch online library to a CSV file for easy automated import.");
        
        // Tutorial video links and descriptions
        Hyperlink steamVideoLink = new Hyperlink("Steam Exporter Tutorial Video");
        steamVideoLink.setOnAction(e -> openLink("https://youtu.be/WLLxdTEdJNc"));
        Label steamVideoText = new Label("Learn how to export your Steam library.");
    
        Hyperlink playstationVideoLink = new Hyperlink("Playstation Exporter Tutorial Video");
        playstationVideoLink.setOnAction(e -> openLink("https://example.com/playstation-tutorial"));
        Label playstationVideoText = new Label("Learn how to export your Playstation library.");
    
        Hyperlink itchIoVideoLink = new Hyperlink("Itch.io Exporter Tutorial Video");
        itchIoVideoLink.setOnAction(e -> openLink("https://example.com/itchio-tutorial"));
        Label itchIoVideoText = new Label("Learn how to export your itch.io library.");
    
        Hyperlink nintendoVideoLink = new Hyperlink("Nintendo Exporter Tutorial Video");
        nintendoVideoLink.setOnAction(e -> openLink("https://example.com/nintendo-tutorial"));
        Label nintendoVideoText = new Label("Learn how to export your Nintendo library.");
    
        // Left column with exporter links and descriptions
        VBox leftColumn = new VBox(10);
        leftColumn.getChildren().addAll(
            new VBox(steamExporterLink, steamExporterText),
            new VBox(playstationExporterLink, playstationExporterText),
            new VBox(itchIoExporterLink, itchIoExporterText),
            new VBox(NintendoExporterLink, NintendoExporterText)
        );
    
        // Right column with tutorial video links and descriptions
        VBox rightColumn = new VBox(10);
        rightColumn.getChildren().addAll(
            new VBox(steamVideoLink, steamVideoText),
            new VBox(playstationVideoLink, playstationVideoText),
            new VBox(itchIoVideoLink, itchIoVideoText),
            new VBox(nintendoVideoLink, nintendoVideoText)
        );
    
        HBox exportersBox = new HBox(50); // Container for two columns
        exportersBox.setAlignment(Pos.CENTER);
        exportersBox.getChildren().addAll(leftColumn, rightColumn);
    
        // Add all elements to the main container
        contentBox.getChildren().addAll(header, paragraphFlow, linkBox, exporterHeader, exportersBox);
    
        // Wrap contentBox in HBox to center within ScrollPane
        HBox centeredContainer = new HBox(contentBox);
        centeredContainer.setAlignment(Pos.CENTER);
    
        ScrollPane scrollPane = new ScrollPane(centeredContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true); // Ensure the ScrollPane fills the height
    
        // Set the content of the help tab
        this.helpTab.setContent(scrollPane);
    }
    
    
    
    
    /**
     * Opens a given URL in the system's default web browser in a platform-independent way.
     * Uses platform-specific commands as needed.
     *
     * @param url The URL to open.
     */
    private void openLink(String url) {
        new Thread(() -> { // Run in a new thread to prevent blocking the GUI
            try {
                // Attempt to open the URL using java.awt.Desktop
                java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
            } catch (Exception e) {
                // Fallbacks for each platform using ProcessBuilder
                try {
                    String os = System.getProperty("os.name").toLowerCase();
                    ProcessBuilder processBuilder;  

                    if (os.contains("win")) {
                        processBuilder = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url);
                    } else if (os.contains("mac")) {
                        processBuilder = new ProcessBuilder("open", url);
                    } else if (os.contains("nix") || os.contains("nux")) {
                        processBuilder = new ProcessBuilder("xdg-open", url);
                    } else {
                        throw new UnsupportedOperationException("Unsupported operating system. Please open the link manually: " + url);
                    }   

                    // Start the process
                    processBuilder.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * Returns the Tab instance for the Help tab.
     *
     * @return The help Tab instance.
     */
    public Tab getTab() {
        return helpTab;
    }
}
