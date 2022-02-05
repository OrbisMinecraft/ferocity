# Ferocity

**Ferocity is currently alpha software. Use with caution.**

Ferocity is a [Velocity](https://velocitypowered.com/) plugin for sharing the player list across multiple servers. 
It also allows for adding header and footer bars to the player list and provides formatting using 
[MiniMessage](https://docs.adventure.kyori.net/minimessage). Ferocity also includes basic support for LuckPerms.

## Permissions
Ferocity exposes a management command (`/ferocity`) which requires the `ferocity.manage` permission node to use.

## Building
Ferocity is a Gradle project. To build it, you will need an up-to-date build of JDK 17 installed
on your machine. To get started, download the source code (either by downloading the ZIP file or
`git clone`-ing it). Then open the folder with the source code in a terminal or command prompt
and run `./gradlew shadowJar`. You will find the plugin's JAR file in `./build/libs`.

## Other Projects
Also check out our other projects:
- [Plurality](https://github.com/OrbisMinecraft/plurality), a Velocity plugin which remembers the server a player logs off in
- [Tenacity](https://github.com/OrbisMinecraft/tenacity), a Paper plugin which saves player's inventories in a database so they can be shared across servers
- [Ionicity](https://github.com/OrbisMinecraft/ionicity), a Velocity plugin for sharing chat across multiple servers
