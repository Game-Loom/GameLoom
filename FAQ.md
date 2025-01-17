# FREQUENTLY ASKED QUESTIONS (FAQ)

## Q: What is GameLoom?

A: GameLoom is a personal game library manager that helps users catalog their physical and digital game collections across multiple platforms into a composite library.


## Q: What Operating Systems can GameLoom run on?

**A:** GameLoom is *system-agnostic* and can run on any platform that supports Java and has a graphical display including **Linux, macOS, and Windows**. 
- The GameLoom team have thoroughly vetted Windows (10/11) and Linux (Debian-based distributions) although we have only been able to find limited testing with macOS due to availability of the operating system.
- Our team has additionally made very approachable video tutorials for how to run GameLoom on [Windows](https://youtu.be/uOQYS-_5dvU) and [Linux](https://youtu.be/tZD4-sFCjF0).


## Q: Does GameLoom require internet connectivity?

**A:** No. GameLoom operates entirely offline, ensuring privacy and security for your data. 
- There is no cloud synchronization or external online requirement beyond the initial download of the application in order to use it.
- **Access to the recommended exporters and YouTube tutorials will require internet connection, but use of the GameLoom software does not require any internet connection*


## Q: What filetypes does GameLoom support and create?

**A:** ***GameLoom supports all csv files that were be made to represent game data, even your own custom-made files.***

- Additionally, the included README and the help tab of the software itself both offer instructions for obtaining and using csv files generated from existing game library exporters for the following platforms:

    - Steam: [Lorenzo Stanco's Steam Library Exporter](https://www.lorenzostanco.com/lab/steam/)
    - Nintendo eShop: [eshop-purchase-history](https://github.com/redphx/eshop-purchase-history)
    - PlayStation: [PSDLE](https://repod.github.io/psdle/)
    - itch.io: [Itch.io Library to CSV Scraper](https://gist.github.com/abraxas86/ad72ba46b6cdd86dc63058bba0c629c2#file-itchiocollectiontocsv-user-js)


## Q: How do I import my existing custom game library into GameLoom?

**A:** In GameLoom:
- When selecting a choice from landing page or the "Choose import..." dropdown menu at the top of the screen, choose the "GameLoom Library" option.
    - This option will not append a platform field to the entries you are importing, so ensure you have already labeled which platform these game entries are associated with within the csv you are importing.
        - GameLoom separates Platform and Console, for example two games might both have the platform set as "Playstation" although they might have the "Console" field set to PS4, and PS5 respectively.
        - The YouTube video for using the PSDLE exporter shows you how to make this distinction on your digital library export.
    - **In the case that you have a manually maintained csv for your total game library already that you would like to use with GameLoom you will want to consider accounting for this.*


## Q: How does GameLoom handle platform-specific quirks in the various recommended exporters' data?

**A:** GameLoom includes custom logic to handle platform-specific data:
- For Nintendo eShop CSV files, the last 5 rows (summary data) are automatically ignored during import.
- For PlayStation CSV files (via PSDLE), the redundant database headers row at the bottom is skipped.
- Handles various delimiters (comma, semicolon, tab) and normalizes inconsistent fields.
- Handles scenarios where values contain the delimiter and are not in quotes (by surrounding those values in quotes during import).


## Q: What happens if my library contains duplicate entries?

**A:** GameLoom automatically identifies and prevents duplicate entries during import to maintain a clean library.
- This is not done based on the title and is rather based on the entire dataset for the game entry.
    - For example, you might own a game on Steam and also own it on Nintendo Switch, if the platform field and/or playtime is different but everything else about the game data is the same GameLoom will treat them as two separate entries rather than duplicates so you can be sure that you have logged both places where you own it.
- If the data set *is* exactly the same, the duplicate will not be processed and entered into the composite library.


## Q: Can I export my unified digital/physical composite library?

**A:** Yes. GameLoom allows you to export your unified game library into a CSV file for external use or sharing with any existing tools or platforms that support the common csv file format.
- The software includes an auto-save feature as well as an option to manually export your current library using the "Export Games to CSV" button located in the top right of the program window.
- There is also a feature built in that when you click the 'X' to close the program in the corner of your program window, there is a safety net that will ask if you wish to export your current library in case you have made changes before the next auto-save check and have tried to close the program without your full work saved, you can also just choose no and the program will close if you are sure your work is backed up.


## Q: How does the auto-save feature work?

**A:** GameLoom will auto-save your library as it detects changes. New additions, edits, etc. will register as changes as the check is hash based. 
- GameLoom isn't encrypting anything, just hashing to detect changes, a modest and resource light MD5 cipher is being used on the total library object so any changes will show a difference.
- The Software by default will check for changes every 3 minutes and will keep a maximum of 20 files
    - It will remove the oldest file in the folder when the auto-save starts to register past 20 changes.
- The software will create a folder called "GameLoom Exports" in the system-agnostic location of *the user's home folder* and place these auto-saved files in this folder.
    - This is the folder that it will check for the maximum 20 files and the naming schema is a timestamp.


## Q: Can I customize the look and feel of GameLoom?

**A:** Yes. GameLoom supports customizable themes, allowing you to personalize the user interface to your preferences.
- These themes are accessible via the "Choose Theme" dropdown in the top left of the program window.


## Q: What data fields does GameLoom support by default in my library?

**A:** GameLoom supports *any and all fields you wish to track,* and whatever choices you make on each exporter.
- By default the program will try to populate the following fields at a minimum.
    - If the fields aren't present, it will not cause a problem but the software will look for these at a bare minimum:

        1. Title (Name of Game)
        2. Console
        3. Release Date
        4. Metacritic Score
        5. Supported Languages

- **The platform will automatically be set based on your choice in the "Choose import..." dropdown menu, unless you choose GameLoom library wherein the program expects you have already set the "Platform" field in your csv.*


## Q: How do I update GameLoom?

**A:** Since using GameLoom is a fully network free experience, it does not yet support automatic updates.
- To update, download the latest version from the [official repository](https://github.com/Game-Loom/GameLoom) and replace your current version.


## Q: How do I contact GameLoom support if I need some help?

**A:** You can contact support via email at [GameLoomHelp@gmail.com]
- Additionally, clicking the "Contact Support" link within the application's help tab will open your default email client and pre-populate the recipient field to our support email address.

