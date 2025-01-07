# GameLoom 🎮

**GameLoom** is a personal game library manager that acts as a unified catalog for tracking physical and digital game collections across multiple platforms.

---

## Features
- **Unified Game Library Management**: Combine physical and digital game libraries into a single repository.
- **CSV Import Compatibility**: Import game data directly from platforms like Steam, Nintendo eShop, itch.io, and PlayStation.
- **Cross-Platform Support**: Handles a variety of CSV delimiters (commas, semicolons, and tabs) and normalizes data for consistency.
- **Auto-Save Functionality**: Automatically backs up the library every 3 minutes, with up to 20 backup versions retained.
- **Advanced Filtering & Sorting**: Search and filter your library by platform, year, custom attributes, and more.
- **Customizable Themes**: Select different UI themes to personalize your experience.
- **Offline Privacy**: All data is managed locally with no network dependencies or cloud synchronization.

---

## Requirements for Run From Source
- Java 22 or later
- JavaFX 22 or later

## Requirements for Run from .jar File
- Java 22 or later

---

## Setup

### **Run From Source**
   1. Clone the Repository:
   git clone https://github.com/your-repository/GameLoom.git

   2. Install JavaFX:
      - Download JavaFX 22.0.2 SDK from: https://jdk.java.net/javafx22/.
      - Set up the JavaFX module path in your IDE.

   3. Run the Program:
      - Compile and run GUIDriver.java using your preferred IDE or build tool.
      - Ensure the JavaFX --module-path argument is configured correctly for your environment (especially if using VSCode).

or

### **Run from .jar File**:
   1. Download the Zip file that contains the .jar and dependencies directly above in the repository's file listings.
   2. Extract the .zip wherever you would like, navigate a terminal window to that directory location.
   3. System Universal Command:

        Windows Command:
            java --module-path <insert_path_to_extracted_folder>\GameLoom\lib\windows\javafx-sdk-22.0.2\lib\ --add-modules javafx.controls,javafx.fxml -jar GameLoom.jar
            --
               (e.x) java --module-path C:\Users\Chris\Downloads\GameLoom\lib\windows\javafx-sdk-22.0.2\lib\ --add-modules javafx.controls,javafx.fxml -jar GameLoom.jar

        Linux Command:
            java --module-path <insert_path_to_extracted_folder>/GameLoom/lib/linux/javafx-sdk-22.0.2/lib/ --add-modules javafx.controls,javafx.fxml -jar GameLoom.jar
            --
               (e.x) java --module-path /home/chris/Downloads/GameLoom/lib/linux/javafx-sdk-22.0.2/lib/ --add-modules javafx.controls,javafx.fxml -jar GameLoom.jar

        Mac Command:
            java --module-path <insert_path_to_extracted_folder>/GameLoom/lib/mac/javafx-sdk-22.0.2/lib/ --add-modules javafx.controls,javafx.fxml -jar GameLoom.jar
            --
               (e.x) java --module-path /home/chris/Downloads/GameLoom/lib/mac/javafx-sdk-22.0.2/lib/ --add-modules javafx.controls,javafx.fxml -jar GameLoom.jar

## Usage:
### Importing Game Libraries:
  - **Steam**: Use Lorenzo Stanco's Steam Library Exporter.
  - **Nintendo (Switch)**: Use Nintendo eShop Purchase History Exporter.
      - *Note*: Exported CSV files include an extra 5 rows at the bottom of the file to create space betweeen a total calculation, which is automatically ignored during import.
  - **PlayStation**: Use PSDLE for exporting your PS4/PS5 library.
     - Note: Exported CSV files include an extra header row at the bottom, which is automatically ignored during import.
  - **itch.io**: Use the Itch.io Library to CSV Scraper.
     - Requires Tampermonkey to run the userscript. See below.
     - Learn More.

### Exporting Game Libraries:
GameLoom supports exporting your unified game library to a CSV file for external use or sharing.

### Auto-Save:
- Auto-save is enabled by default, backing up your library every 3 minutes to your home/GameLoom Exports folder.
- A maximum of 20 auto-save files are retained in the GameLoom Exports folder located in your home directory.

## Known Limitations:
- itch.io Exporter: Limited testing due to small dataset availability.
- PSDLE (PlayStation): EPSDLE export functionality for PS3, PSP, and PS Vita is not currently working as of October 2024.

## Contributing:
We welcome contributions to GameLoom! If you encounter bugs, have feature requests, or want to contribute code, please submit an issue, fork a copy of the repository make whatever changes you would like and submit a pull request for review.

## Exporters:
Below are the recommended tools for exporting game libraries from various platforms:

- **Steam**: https://www.lorenzostanco.com/lab/steam/
- **PlayStation**: https://repod.github.io/psdle/
   - The last row (database headers) is ignored during import by default.
   
- **Nintendo**: https://github.com/redphx/eshop-purchase-history
   - Automatically ignores the last 5 rows during import that include summary data.
   - Requires Tampermonkey browser extension. See below.
      - Additional Information: https://www.reddit.com/r/nintendo/comments/8w1s65/i_made_a_script_to_export_your_purchase_history/
     
- **itch.io**: https://gist.github.com/abraxas86/ad72ba46b6cdd86dc63058bba0c629c2#file-itchiocollectiontocsv-user-js
   - Requires Tampermonkey browser extension. See below.
      - Additional Information: https://itch.io/blog/572343/big-improvements-to-library-to-csv-scraper

### Tampermonkey Extension Links
- Firefox Tampermonkey Extension: https://addons.mozilla.org/en-US/firefox/addon/tampermonkey/
- Chrome Tampermonkey Extension: https://chromewebstore.google.com/detail/tampermonkey/dhdgffkkebhmkfjojejmpbldmpobfkfo

## Tutorial Videos:
To make the setup and usage process easier, we’ve created tutorial videos for the recommended exporters:
- Using Steam Exporter: https://youtu.be/OeS60dwbXBQ
- Using PSDLE for PlayStation: https://youtu.be/vphXnajoUPY
- Using itch.io Exporter: https://youtu.be/0QkQZILQ5zk
- Using Nintendo Switch Exporter: https://youtu.be/hGhZ3xFpy00

### License:
GameLoom is licensed under MIT License. See the LICENSE file for more details.

### Contact:
For support or inquiries, please contact us via our support email: GameLoomHelp@gmail.com
