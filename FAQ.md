 
# FREQUENTLY ASKED QUESTIONS

## What is GameLoom?

    GameLoom is a personal game library manager that helps users catalog their physical and digital game collections across multiple platforms. It supports importing data from various platform-specific CSV exporters, such as Steam, Nintendo, PlayStation, and itch.io as well as manual entry of your game data to compile a composite digital and phyical library.


## What filetypes does GameLoom support and create?

    GameLoom supports all csv files that were be made to represent game data, even your own custom-made files. However, the included README and the help tab of the software itself both offer instructions for obtaining and using csv files generated from existing game library exporters for the following platforms:

        Steam (via Lorenzo Stanco’s Steam Library Exporter)
        Nintendo eShop (via Purchase History Exporter)
        PlayStation (via PSDLE Exporter)
        itch.io (via itch.io Library to CSV Scraper)


## Does GameLoom require internet connectivity?

    No. GameLoom operates entirely offline, ensuring privacy and security for your data. There is no cloud synchronization or external dependency beyond the initial download of the application. If you choose to use the recommended CSV exporters to obtain your digital library data, these are mostly just browser extensions and not typical software downloads All of the exporter extentions (Steam exporter is just a site, the others are browser extensions) are from reputable open source repositories that can be manually verified for security purposes and have passed the security checks required to be listed in the official Chrome and Firefox extension/add-on stores.


## What Operating Systems can I run GameLoom on?

    GameLoom is system-agnostic and can run on any platform that supports Java and has a graphical display including Linux, macOS, and Windows. The GameLoom team have thoroughly vetted Windows (10/11) and Linux (Debian-based distros) although we have only been able to find limited testing with macOS due to availablility of the operating system.


## How do I import my existing custom game library into GameLoom?

    In GameLoom:
        When selecting a choice from the "Choose import..." dropdown menu at the top of the screen,
        choose "GameLoom Library." This option will not append a platform field to the entries you are importing, so ensure you have already lableled which platform these game entries are associated with within the csv you are importing. GameLoom separates Platform and Console, for example two games might both have the platform set as "Playstation" although they might have the "Console" field set to PS4, and PS5 respectively. The YouTube video for using the PSDLE exporter shows you how to make this distinction on your digital library export, however this information seems relevant in the case that you have a manually maintained csv for your total game libary already that you would like to use with GameLoom.


## How does GameLoom handle platform-specific quirks in the various recommended exporters' data?

    GameLoom includes custom logic to handle platform-specific data:
        For Nintendo eShop CSV files, the last 5 rows (summary data) are automatically ignored during import.
        For PlayStation CSV files (via PSDLE), the redundant database headers row at the bottom is skipped.
        Handles various delimiters (comma, semicolon, tab) and normalizes inconsistent fields.
        Handles secnarios where values contain the delimiter and are not in quotes by surrounding those values in quotes during import.


## What happens if my library contains duplicate entries?

    GameLoom automatically identifies and prevents duplicate entries during import to maintain a clean library. This is not done based on the title and is rather based on the entire dataset for the game entry, for example you might own a game on Steam and also own it on Nintendo Switch, if the platform field and/or playtime is different but everything else about the game data is the same GameLoom will treat them as two separate entries rather than duplicates so you can be sure that you have logged both places where you own it.


## Can I export my unified digital/physical composite library?

    Yes. GameLoom allows you to export your unified game library into a CSV file for external use or sharing with any existing tools or platforms that support the common csv file format. The software includes an auto-save feature as well as an option to manually export your current library using the "Export Games to CSV" button located in the top right of the program window. There is also a feature built in that when you click the 'X' to close the program in the corner of your program window, there is a safety net that will ask if you wish to export your current library in case you have made changes before the next auto-save check and have tried to close the program without your full work saved, you can also just choose no and the program will close if you are sure your work is backed up.


## How does the auto-save feature work?

    GameLoom will auto-save your library as it detects changes. New additions, edits, etc. will register as changes as the check is hash based. GameLoom isn't encrypting anything, just hashing to detect changes so a modest and resource light MD5 cipher is being used on the total library object so any changes will show a difference. The Software by default will check for changes every 10 seconds and will keep a maximum of 20 files (it will remove the oldest file in the folder when the autosave starts to register past 20 changes). The software will create a folder called "GameLoom Exports" in the system-agnostic location of the user's home folder and place these auto saved files in this folder, this is the folder that it will check for the maximum 20 files and the naming schema is a timestamp.


## Can I customize the look and feel of GameLoom?

    Yes. GameLoom supports customizable themes, allowing you to personalize the user interface to your preferences.


## What data fields does GameLoom support by default in my library?

    GameLoom supports any and all fields you wish to track however by default the program will try to populate the following fields at a minimum (if the fields aren't present it will not cause a problem but the software will look for these at a bare minimum):

        Name (Title)
        Console
        Release Date
        Metacritic Score
        Supported Languages
        Online Multiplayer (e.g., PvP, Co-op)

        * The platform will automaticaly be set based on your choice in the "Choose import..." dropdown menu, unless you choose GameLoom library in which case the program expects you have already set the "Platform" field in your csv.


## How do I update GameLoom?

    Since GameLoom is a fully network free experience, it does not yet support automatic updates. To update, download the latest version from the official source and replace your current version. We may work out a way to dispense updates however at the moment users will just need to visit the GitHub page and


## How do I contact GameLoom support if I need some help?

    You can contact support via email at GameLoomHelp@gmail.com.
    Clicking the "Contact Support" link within the application's help tab will open your default email client and pre-populate the recipient field to our support email address.

