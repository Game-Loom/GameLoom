**10/26**
Auto-save feature currently set to 3 minute interval - 20 maximum files (if someone is consistently adding/editing for 1+ hours they'll have a 1 hour backup)

**10/20**
PSDLE export and itch.io export files now available in project documents tab of discord.
I only own the one thing on itch so there's not a lot of troubleshooting we can do with that one for the normalization without having a larger export file for it but we have all the fields it'll have for the one we recommend. 

Itch.io exporter to csv is done via a userscript that runs using the verified extension Tampermonkey:
https://www.tampermonkey.net/  (has links to each major browser's offical extensions/add-ons page)

The itch.io exporter script can be found here:
https://gist.github.com/abraxas86/ad72ba46b6cdd86dc63058bba0c629c2#file-itchiocollectiontocsv-user-js
For additional information:
https://itch.io/blog/572343/big-improvements-to-library-to-csv-scraper

The link we can provide for PSDLE is: (page has links for all methods of using it, I just did the Firefox extension)
https://repod.github.io/psdle/

And since the ps3, psp, vita thing doesn't function as of 10/20 I'll record a new video just showing how to use it for the PS4/PS5 functionality that currently works.
I can also record a quick video of how to use the itch.io scraper and put a link in our readme or in a help tab or something, however we end up working that out.


For Steam we recommend: https://www.lorenzostanco.com/lab/steam/

For Nintendo (Switch) we recommend: https://github.com/redphx/eshop-purchase-history


**10/18**
**Semi-colon delimiter no longer a requirement, program can handle commas, semi-colons, and tabs including when values are unquoted and contain the delimiter in the value(s)**

**Note: 10/18**: Don't remove apache commons stuff just yet, if we end up doing the steam API stuff ourselves one of the required libraries to use it is one of the apache commons io libraries

**9/18:**
.vscode folder and bin folder are removed from sync and will be ignored but should not affect your existing .vscode folder on your local drive.
Our .vscode configurations for our local machines will be left out of the github syncs automatically from now on so we don't have to change any config paths.

**9/17:**
CSV importer mostly works with some issues
Its not populating the name field on display properly and for awhile it 
was because some of the game names will have commas in them (Warhammer 40,000: Mechanicus, etc.)
so I changed the output on the csv from the website to use a ; as a delimiter since it was an option
and that solved the problem with game names that contain commas but I still haven't worked out populating the
name field properly (which had been happening because the in-name commas were throwing off the key/value pairs)
but I'm fairly sure its close. Also there are some missing elements to the overall UI that I haven't worked out yet like the notes button
or making the tabs actually change to different pages. I just started working on the csv importer because we already had the csv so a lot of the UI
still isn't actually functional but we can work that out as we get there. 
