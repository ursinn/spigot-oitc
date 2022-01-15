# DEPRECATED: OITC

<!--
[![Jenkins](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.ursinn.dev%2Fjob%2Fursinn%2Fjob%2Fspigot-oitc&logo=jenkins&style=for-the-badge)](https://ci.ursinn.dev/job/ursinn/job/spigot-oitc)
-->
[![GitHub last commit](https://img.shields.io/github/last-commit/ursinn/spigot-oitc?logo=github&style=for-the-badge)](https://github.com/ursinn/spigot-oitc/commits)
[![Code Climate maintainability](https://img.shields.io/codeclimate/maintainability/ursinn/spigot-oitc?logo=codeclimate&style=for-the-badge)](https://codeclimate.com/github/ursinn/spigot-oitc)
[![Snyk Vulnerabilities for GitHub Repo](https://img.shields.io/snyk/vulnerabilities/github/ursinn/spigot-oitc?logo=snyk&style=for-the-badge)](https://snyk.io/test/github/ursinn/spigot-oitc)
[![License](https://img.shields.io/github/license/ursinn/spigot-oitc?style=for-the-badge)](https://github.com/ursinn/spigot-oitc/blob/main/LICENSE)

[![GitHub release](https://img.shields.io/github/release/ursinn/spigot-oitc.svg?logo=github&style=for-the-badge)](https://github.com/ursinn/spigot-oitc/releases/latest)
[![GitHub All Releases](https://img.shields.io/github/downloads/ursinn/spigot-oitc/total.svg?logo=github&style=for-the-badge)](https://github.com/ursinn/spigot-oitc/releases)

## **Features**

* Join signs!
* Multiple spawn creation for each arena. After a player has died they will respawn back to a Random location every time
* Simple to use and setup
* A scoreboard at the side of the screen, Shows each players stats with their names and kills together
* A Custom Countdown (Seconds can be changed in the config.yml for each Arena)
* Saving and restoring Player's inventories when joining/leaving an Arena.
* An Automatic Start Mechanism! (Configure how many players are needed to auto start in the config.yml
* Free for all
* No breaking blocks ingame
* No placing blocks ingame
* No opening chests ingame
* Automatic respawn, Nobody's got time for clicking "Respawn" Anymore. Just die and GO!

## **Commands**

* **/oitc** The main command of the plugin, and a shortened command for One in the Chamber
* **/oitc start [Arena]** Force starts the Arena, (One player must atleast be in to actually force start it.)
* **/oitc create [Arena]** Creates the arena with the name specified in the argument
* **/oitc lobby** Teleports you to the Main lobby of OITC (Where the players will teleport back when a game is finished)
* **/oitc addspawn [Arena]** Adds a spawn to the Arena, Can make multiple spawns (As many as you want)
* **/oitc setmainlobby** Sets the main lobby to where players will teleport to after the game and with /oitc lobby
* **/oitc setlobby [Arena]** This sets the Per-Arena Lobby's that players will be teleported to when they click on a
  sign.
* **/oitc leave** leaves the current arena you are in.
* **/oitc stop [ArenaName]** force stops the current arena.
* **/oitc version** Gives you your current version of OITC.
* **/oitc reload** Reloads the Config files for OITC(Instead of just Reloading the whole server)

## **Permission**

* **oitc.admin**

## **Sign Setup**

* Line 1: **oitc** _(Caps doesn't matter)_
* Line 2: "**Your ArenaName Here**"
* Line 3: _Nothing_
* Line 4: _Nothing_

## **Configuration Help**

**Note that all values listed below are _DEFAULT_ Values**

* **Countdown : 15** How many seconds for the countdown to... countdown
* **MaxPlayers: 20** The Amount of maximum players the Arena can hold.
* **KillsToWin: 25** The Amount of kills needed to win the game
* **AutoStartPlayers: 8** The amount of players needed to join the arena until the arena Automatically starts by itself
* **EndTime: 600** The Amount of seconds the Arena will run for, By default is 10 Minutes (600 seconds), so after 10
  minutes, if nobody gets to the KillsToWin amount, it will then stop itself.

**[Advanced Config](https://github.com/ursinn/Spigot-OITC/tree/master/Advanced-Config)**

## **Metrics**

This plugin utilizes bStats plugin metrics system, which means that the following information is collected and sent to
bstats.org:

* Your server's randomly generated UUID
* The amount of players on your server
* The online mode of your server
* The bukkit version of your server
* The java version of your system (e.g. Java 8)
* The name of your OS (e.g. Windows)
* The version of your OS
* The architecture of your OS (e.g. amd64)
* The system cores of your OS (e.g. 8)
* bStats-supported plugins
* Plugin version of bStats-supported plugins

[![Metrcis](https://bstats.org/signatures/bukkit/OITC.svg)](https://bstats.org/plugin/bukkit/OITC/4780)
