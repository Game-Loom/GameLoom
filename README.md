****DELIMITER  ON STEAM LIBRARY EXPORTER MUST BE SET TO ; BECAUSE SOME GAMES HAVE COMMAS IN THEIR NAMES****

9/17:
CSV importer mostly works with some issues
Its not populating the name field on display properly and for awhile it 
was because some of the game names will have commas in them (Warhammer 40,000: Mechanicus, etc.)
so I changed the output on the csv from the website to use a ; as a delimiter since it was an option
and that solved the problem with game names that contain commas but I still haven't worked out populating the
name field properly (which had been happening because the in-name commas were throwing off the key/value pairs)
but I'm fairly sure its close. Also there are some missing elements to the overall UI that I haven't worked out yet like the notes button
or making the tabs actually change to different pages. I just started working on the csv importer because we already had the csv so a lot of the UI
still isn't actually functional but we can work that out as we get there. 


For now you can ignore the extra apache commons libraries in the lib folder, I was trying to use apache commons to parse the csv at one point because of the commas in names issue
but then I realized the website will let me export with my choice of delimiter and maybe 1/ ~500,000 games has a semi-colon in it's name
(I honestly can't think of even one but I'm sure its probably out there). So I ended up not needing to use it but if that changes the csv and io libraries from apachecommons are in the lib folder

9/18:
.vscode folder and bin folder are removed from sync and will be ignored but should not affect your existing .vscode folder on your local drive.
Our .vscode configurations for our local machines will be left out of the github syncs automatically from now on so we don't have to change any config paths.


testing github-vsCodethings -Na Huynh

Also testing github! -Leanna
