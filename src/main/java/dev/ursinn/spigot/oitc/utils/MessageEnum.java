/*
 * MIT License
 *
 * Copyright (c) 2019-2022 Ursin Filli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.ursinn.spigot.oitc.utils;

public enum MessageEnum {

    OITC_SEND_MESSAGE("", "&7[&bOITC&7] &7%message%"),
    STARTUP_UNSUPPORTED_MINECRAFT_VERSION("", "unsupported Minecraft Server version! (%nms_version%)"),
    STARTUP_LOADING_YML_START("", "Loading YML files!"),
    STARTUP_LOADING_YML_FINISHED("", "Loaded YML files Successfully!"),
    STARTUP_LOADING_ARENA_START("", "Now Currently Loading The Arena: %arena_name%"),
    STARTUP_LOADING_ARENA_FINISHED("", "The Arena: %arena_name% Has successfully loaded!"),
    STARTUP_LOADING_ARENA_FAILED("", "WARNING, FAILED TO LOAD ARENAS."),
    STARTUP_LOADING_ARENA_SINGLE_MODE_FAILED("", "WARNING, FAILED TO LOAD ARENA."),
    STARTUP_LOADING_ARENA_SINGLE_MODE_START("", "Now Currently Loading The BungeeMode Arena: %arena_name%"),
    STARTUP_LOADING_ARENA_SINGLE_MODE_FINISHED("", "The Arena: %arena_name% Has successfully loaded!"),
    GAME_ARENA_FULL("", "&cSorry! That Arena is full!"),
    GAME_ARENA_NOT_FOUND("", "&cError! Arena not Found!"),
    GAME_ARENA_IS_STATE("", "Sorry! That Arena is %arena_state%"),
    ARENA_MAIN_LOBBY_NOT_FOUND("", "&cError: It seems the Main Lobby has not been setup yet, please tell your server owner ASAP."),
    ARENA_TIME_LIMIT("", "&7The time limit has been reached!"),
    ARENA_LOBBY_NOT_FOUND("", "Oops, It seems there is no lobby setup for this arena yet! Please contact your server admins."),
    ARENA_STOPPED("", "&aWe hope you had fun :)"),
    ARENA_BACK_TP("", "You have been teleported back to the Main Lobby."),
    ARENA_GAME_START("", "&bThe game has started!"),
    ARENA_GAME_START_COUNTDOWN("", "&b%counter% &7seconds until the game starts."),
    ARENA_PLAYER_JOIN("", "&b%player_name% &7Has joined."),
    ARENA_PLAYER_REMOVE_QUIT("", "&c%player_name% &7Has quit."),
    ARENA_PLAYER_REMOVE_KICK("", "&c%player_name% &7Has been kicked."),
    ARENA_PLAYER_REMOVE_DEATH("", "&c%player_name% &7is eliminated!"),
    COMMAND_ARENA_NOT_FOUND("", "Sorry, there is no such arena named &c%arena_name%"),
    COMMAND_NOT_PLAYER("", "Must be a player to send OITC commands"),
    COMMAND_MAIN_LOBBY_SET("", "You have set the Main Lobby!"),
    COMMAND_MAIN_LOBBY_NOT_FOUND("", "Oops, it seems there is no Main Lobby setup yet! Please alert your server admins."),
    COMMAND_ARENA_EXIST("", "&cThat Arena already Exists!"),
    COMMAND_ARENA_START_FAILED("", "Cannot start arena."),
    COMMAND_ARENA_START_FAILED_STATUS("", "It is either ingame, stopping, or not enough players."),
    COMMAND_LOBBY_ARENA_LEAVE("", "You have left your current arena and joined the lobby."),
    COMMAND_LOBBY_WELCOME("", "Welcome to the &3Main Lobby!"),
    COMMAND_RELOAD_SUCCES("", "Reloaded Configs Successfully!"),
    UPDATER_NOTIFY("", "An update is available for OITC."),
    SIGN_ALREADY_IN_ARENA("", "&cYou are already in an Arena!"),
    SIGN_ALREADY_IN_ARENA_LEAVE_NOTIFY("", "&7If you would like to leave the current arena you are in, do /oitc leave"),
    SIGN_REMOVE_HELP("", "If you want to break this sign, please sneak + break!"),
    SIGN_REMOVE_NOTIFY("", "You have removed a sign from &3%arena_name%"),
    SIGN_ADD_NOTIFY("", "You made a join sign for &6%arena_name%"),
    ;

    private String path;
    private String default_value;

    MessageEnum(String path, String default_value) {
        this.path = path;
        this.default_value = default_value;
    }

    public String getPath() {
        return path;
    }

    public String getDefault_value() {
        return default_value;
    }
}
