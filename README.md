# Rooms game
Rogue-like game for blind people

# License

GNU General Public Licence v3: https://www.gnu.org/licenses/gpl-3.0.txt

# Game information

In order to change the game's language we need to change the languages.properties file and put ES, GL, EN or NL in order to play in Spanish, Galician, English or Dutch, respectively.

In some occasions the generated sentences are not read directly. This is because \textit{Java Access Bridge} is not correctly added or the correct version is incompatible (it's recommended to install both the 32 and 64 bit versions). In Windows systems \textit{Java Access Bridge} can be easily activated doing the following:

Go to Start -> Control Panel->Ease of Access->Ease of Access Center
Select Use the computer without a display
In the section "Other programs installed", select: Enable Java Access Bridge
You can also do:

%JRE_HOME%\bin\jabswitch -enable
where %JRE_HOME% is the directory where JAVA is installed.

For other OS's it would be necessary to install and activate it manually. Check http://www.oracle.com/technetwork/articles/javase/index-jsp-136191.html for more information.

# Movement system

movement - cursor keys

attack - X

spell attack - M + number (1..6)

pick item - E

throw item - T + number (1..6)

unequip item - U + descNumber(A,S,D,F,G)

description helmet - A

description weapons - S

description chest - D

description pants - F

description gloves - G

description stats - H

description monster - K

description environment - B

description possible movement positions - N

numeric description on/off - C

change colors - Q

# More information

For more information or feedback, create an issue in this project: https://github.com/dpenas/roomsgame/issues or visit our subreddit: https://www.reddit.com/r/roomsgame
