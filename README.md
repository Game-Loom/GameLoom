# GameLoom 🎮

**GameLoom** is a personal game library manager that acts as a unified catalog for tracking physical and digital game collections across multiple platforms.
- [Wiki](https://github.com/Game-Loom/GameLoom/wiki)
- [Userguide Tutorial Videos](https://www.youtube.com/playlist?list=PLHR8Uilp9JUiu-7pFWpwln-JUUtYWXCSD)
- [General FAQ](https://github.com/Game-Loom/GameLoom/wiki/FAQ-:-General)

## Setup
### Run from .Jar
- [Windows](https://github.com/Game-Loom/GameLoom/wiki/Windows-Setup)
- [Linux](https://github.com/Game-Loom/GameLoom/wiki/Linux-Setup)
- [MacOs](https://github.com/Game-Loom/GameLoom/wiki/MacOs-Setup) 
### Run from Source Repository
- [Developer Environment Guide](https://github.com/Game-Loom/GameLoom/wiki/Developer-Environment-Setup) 

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

## Known Limitations:
- itch.io Exporter: Limited testing due to small dataset availability.
- PSDLE (PlayStation): EPSDLE export functionality for PS3, PSP, and PS Vita is not currently working as of October 2024.

## Exporters:
Below are the recommended tools for exporting game libraries from various platforms:

- **Steam**: [Lorenzo Stanco's Steam Library Exporter](https://www.lorenzostanco.com/lab/steam/)
- **PlayStation**: [PSDLE](https://repod.github.io/psdle/)
   - The last row (database headers) is ignored during import by default.
   
- **Nintendo**: [eshop-purchase-history](https://github.com/redphx/eshop-purchase-history)
   - Automatically ignores the last 5 rows during import that include summary data.
   - Requires Tampermonkey browser extension. See below.
      - [Additional Information](https://www.reddit.com/r/nintendo/comments/8w1s65/i_made_a_script_to_export_your_purchase_history/)
     
- **itch.io**: [Itch.io Library to CSV Scraper](https://gist.github.com/abraxas86/ad72ba46b6cdd86dc63058bba0c629c2#file-itchiocollectiontocsv-user-js)
   - Requires Tampermonkey browser extension. See below.
      - [Additional Information](https://itch.io/blog/572343/big-improvements-to-library-to-csv-scraper)

### Tampermonkey Extension Links
- [Firefox Tampermonkey Extension](https://addons.mozilla.org/en-US/firefox/addon/tampermonkey/)
- [Chrome Tampermonkey Extension](https://chromewebstore.google.com/detail/tampermonkey/dhdgffkkebhmkfjojejmpbldmpobfkfo)

## Tutorial Videos:
To make the setup and usage process easier, we’ve created tutorial videos for the recommended exporters:
- [Using Steam Exporter](https://youtu.be/OeS60dwbXBQ)
- [Using PSDLE for PlayStation](https://youtu.be/vphXnajoUPY)
- [Using itch.io Exporter](https://youtu.be/0QkQZILQ5zk)
- [Using Nintendo Switch Exporter](https://youtu.be/hGhZ3xFpy00)

### License:
GameLoom is licensed under MIT License. See the LICENSE file for more details.

### Contributing & Bug Reporting
We welcome contributions to GameLoom! If you encounter bugs, have feature requests, or want to contribute code, please submit an [issue](https://github.com/Game-Loom/GameLoom/branches) or pull request to the repository.

### Contact:
For support or inquiries, please contact us via our support email [GameLoomHelp@gmail.com].
