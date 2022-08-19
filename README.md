# CoordSaver (Spigot plugin for version 1.19-1.19.2)
Are you tired of having a document filled with Minecraft coordinates of important locations in your survival world?

This simple spigot plugin allows you to store locations in-game, removing the tedious task of opening a file with notepad and pressing F3 to copy down coordinates.

## Installation
Simply download the .jar file from the releases page and drag and drop it into your Spigot server's plugin folder.

## How does it work?
Each player has a list of coordinates that they store for themselves. Each coordinate has an x, y, z integer and world name, along with a name that will be used to identify the coordinate.

There is also a global list of coordinates that admins can add and remove coordinates to and from, available for all players to see.

## Commands
- `coords list [<page>] [<player>]`: Lists all your saved coordinates. The admin permission `coordsaver.coords.list.others` is required to view other another player's coordinates.
    - Requires `coordsaver.coords.list`. Enabled for everyone by default.
- `coords set <name> [<x> <y> <z>] [<world>]`: Saves a coordinate. Excluding x y z or world will default to your current location. 
    - Requires `coordsaver.coords.set` permission. Enabled for everyone by default.
- `coords remove <name>`: Removes a saved coordinate.
    - Requires `coordsaver.coords.remove` permission. Enabled for everyone by default.
- `coords clear`: Clears all your saved coordinates.
    - Requires `coordsaver.coords.clear` permission. Enabled for everyone by default.
- `globalcoords list [<page>]`: Lists all global coordinates.
    - Requires `coordsaver.globalcoords.list` permission. Enabled for everyone by default.
- `globalcoords set <name> [<x> <y> <z>] [<world>]`: Sets a global coordinate. Excluding x y z or world will default to your current location. 
    - Requires `coordsaver.globalcoords.set` permission. Enabled for opped players only by default.
- `globalcoords remove <name>`: Removes a global coordinate.
    - Requires `coordsaver.globalcoords.remove` permission. Enabled for opped players only by default.
- `globalcoords clear`: Clears all global coordinates.
    - Requires `coordsaver.globalcoords.clear` permission. Enabled for opped players only by default.

**Please reach out to me if you have any issues through the GitHub Issues page or on discord at okay#2996, thank you :)**