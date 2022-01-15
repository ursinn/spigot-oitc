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

package dev.ursinn.spigot.oitc.command;

import dev.ursinn.spigot.oitc.OITC;
import dev.ursinn.spigot.oitc.arena.Arena;
import dev.ursinn.spigot.oitc.arena.Arenas;
import dev.ursinn.spigot.oitc.arena.LeaveReason;
import dev.ursinn.spigot.oitc.utils.MessageEnum;
import dev.ursinn.spigot.oitc.utils.Methods;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandOITC implements CommandExecutor {

    private OITC plugin;

    public CommandOITC(OITC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // TODO MessageManager Support Add

        if (!(sender instanceof Player)) {
            sender.sendMessage(OITC.messageManager.getMessage(MessageEnum.COMMAND_NOT_PLAYER));
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");

            player.sendMessage(ChatColor.GRAY + "--------" + ChatColor.AQUA + "OITC" + ChatColor.GRAY + "--------");
            player.sendMessage(ChatColor.GRAY + "Created By: " + ChatColor.RED + "Artish1 (Original) & ursinn (Maintenance)");

            player.sendMessage(ChatColor.AQUA + "/oitc lobby" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Teleports you to the Main Lobby");
            player.sendMessage(ChatColor.AQUA + "/oitc leave" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Leaves the current arena you are in");
            if (player.hasPermission("oitc.admin")) {
                player.sendMessage(ChatColor.AQUA + "/oitc create [Arena]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Creates a new Arena");
                player.sendMessage(ChatColor.AQUA + "/oitc delete [Arena]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Deletes the Arena");
                player.sendMessage(ChatColor.AQUA + "/oitc addspawn [Arena]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Adds a spawn for the Arena");

                player.sendMessage(ChatColor.AQUA + "/oitc setmainlobby" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + " Sets the Main Lobby");
                player.sendMessage(ChatColor.AQUA + "/oitc setlobby [Arena]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + " Sets the Lobby of the Arena.");
                player.sendMessage(ChatColor.AQUA + "/oitc stop [Arena]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Force stops the Arena");
                player.sendMessage(ChatColor.AQUA + "/oitc start [Arena]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Force starts the Arena");
                player.sendMessage(ChatColor.AQUA + "/oitc reload" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Reloads the configs.");
                player.sendMessage(ChatColor.AQUA + "/oitc list" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Lists all the Arenas.");
                player.sendMessage(ChatColor.AQUA + "/oitc version" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Gives you the version of this plugin.");
            }
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                String arenas = "List of Arenas: " + ChatColor.DARK_AQUA;
                for (Arena arena : Arenas.getArenas()) {
                    arenas = arenas + arena.getName() + ", ";
                }
                OITC.sendMessage(player, arenas);
                return true;
            }

            if (args[0].equalsIgnoreCase("version")) {
                OITC.sendMessage(player, "You are using OITC Version " + ChatColor.RED + plugin.getDescription().getVersion());
                return true;
            }

            if (player.hasPermission("oitc.admin")) {
                if (args[0].equalsIgnoreCase("setmainlobby")) {
                    Methods.setLobby(player.getLocation());
                    OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_MAIN_LOBBY_SET));
                    return true;
                }

                if (args[0].equalsIgnoreCase("reload")) {
                    new Methods(plugin).loadYamls();
                    plugin.reloadConfig();
                    plugin.op.loadOptions();
                    for (Arena arena : Arenas.getArenas()) {
                        arena.updateSigns();
                    }

                    OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_RELOAD_SUCCES));
                    return true;
                }

            }

            if (args[0].equalsIgnoreCase("lobby")) {
                if (Arenas.isInArena(player)) {
                    Arena arena = Arenas.getArena(player);
                    OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_LOBBY_ARENA_LEAVE));
                    arena.removePlayer(player, LeaveReason.QUIT);
                    return true;
                }

                if (Methods.getLobby() != null) {
                    player.teleport(Methods.getLobby());
                    OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_LOBBY_WELCOME));
                    return true;
                }

                OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_MAIN_LOBBY_NOT_FOUND));
                return true;
            }

            if (args[0].equalsIgnoreCase("leave")) {
                if (!Arenas.isInArena(player)) {
                    OITC.sendMessage(player, "You are not in an Arena to leave from, But you will still be teleported back to the lobby!");
                    return true;
                }

                Arena arena = Arenas.getArena(player);
                arena.removePlayer(player, LeaveReason.QUIT);
                return true;
            }

            return true;
        }

        if (args.length == 2) {
            if (!player.hasPermission("oitc.admin"))
                return true;

            if (args[0].equalsIgnoreCase("create")) {
                if (Arenas.arenaExists(args[1])) {
                    OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_ARENA_EXIST));
                    return true;
                }

                plugin.arenas.addDefault("Arenas." + args[1], args[1]);
                plugin.arenas.addDefault("Arenas." + args[1] + ".Signs.Counter", 0);
                plugin.getConfig().addDefault(args[1] + ".Countdown", 15);
                plugin.getConfig().addDefault(args[1] + ".MaxPlayers", 20);
                plugin.getConfig().addDefault(args[1] + ".KillsToWin", 25);
                plugin.getConfig().addDefault(args[1] + ".AutoStartPlayers", 8);
                plugin.getConfig().addDefault(args[1] + ".EndTime", 600);
                plugin.getConfig().addDefault(args[1] + ".AutoHeal", false);
                //*** Advanced ***
                List<String> list = new ArrayList<>();
                List<String> list2 = new ArrayList<>();
                List<String> list3 = new ArrayList<>();
                List<String> list4 = new ArrayList<>();
                List<String> list5 = new ArrayList<>();
                //*** GameEnd ***
                plugin.getConfig().addDefault(args[1] + ".GameEnd.Arena", list);
                plugin.getConfig().addDefault(args[1] + ".GameEnd.User", list2);
                plugin.getConfig().addDefault(args[1] + ".GameEnd.Place.1", list3);
                plugin.getConfig().addDefault(args[1] + ".GameEnd.Place.2", list4);
                plugin.getConfig().addDefault(args[1] + ".GameEnd.Place.3", list5);
                //*** Enchantment ***
                plugin.getConfig().addDefault(args[1] + ".Enchantment.KNOCKBACK.use", false);
                plugin.getConfig().addDefault(args[1] + ".Enchantment.KNOCKBACK.level", 1);
                plugin.getConfig().addDefault(args[1] + ".Enchantment.DAMAGE_ALL.use", false);
                plugin.getConfig().addDefault(args[1] + ".Enchantment.DAMAGE_ALL.level", 4);
                plugin.getConfig().addDefault(args[1] + ".Enchantment.DURABILITY.use", false);
                plugin.getConfig().addDefault(args[1] + ".Enchantment.DURABILITY.level", 3);
                //*** Sword ***
                plugin.getConfig().addDefault(args[1] + ".Sword.Unbreakable", false);
                plugin.getConfig().addDefault(args[1] + ".Sword.ItemFlag.HIDE_ENCHANTS.use", false);
                plugin.getConfig().addDefault(args[1] + ".Sword.ItemFlag.HIDE_ATTRIBUTES.use", false);
                plugin.getConfig().addDefault(args[1] + ".Sword.ItemFlag.HIDE_UNBREAKABLE.use", false);
                //*** Bow ***
                plugin.getConfig().addDefault(args[1] + ".Bow.Unbreakable", false);
                plugin.getConfig().addDefault(args[1] + ".Bow.ItemFlag.HIDE_ATTRIBUTES.use", false);
                plugin.getConfig().addDefault(args[1] + ".Bow.ItemFlag.HIDE_UNBREAKABLE.use", false);

                Arena arena = new Arena(args[1]);
                Arenas.addArena(arena);
                Methods.addToList(arena);
                OITC.sendMessage(player, ChatColor.GRAY + "You have created the Arena: " + ChatColor.GOLD + arena.getName());
                Methods.saveYamls();
                plugin.saveConfig();
                return true;
            }

            if (args[0].equalsIgnoreCase("delete")) {
                if (!plugin.getConfig().contains(args[1])) {
                    OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_ARENA_NOT_FOUND).replaceAll("%arena_name%", args[1]));
                    return true;
                }

                plugin.getConfig().set(args[1], null);
                plugin.arenas.set("Arenas." + args[1], null);

                Methods.removeFromList(args[1]);

                Methods.saveYamls();
                plugin.saveConfig();
                if (Arenas.arenaExists(args[1])) {
                    Arena arena = Arenas.getArena(args[1]);
                    arena.stop();
                    Arenas.delArena(arena);
                }

                OITC.sendMessage(player, "You have deleted " + ChatColor.DARK_RED + args[1]);
                return true;
            }


            if (args[0].equalsIgnoreCase("start")) {
                if (!Arenas.arenaExists(args[1])) {
                    OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_ARENA_NOT_FOUND).replaceAll("%arena_name%", args[1]));
                    return true;
                }

                Arena arena = Arenas.getArena(args[1]);
                if (arena.getPlayers().size() >= 2) {
                    arena.start();
                    OITC.sendMessage(player, "You have started the arena " + ChatColor.DARK_AQUA + arena.getName());
                    return true;
                }

                OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_ARENA_START_FAILED));
                OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_ARENA_START_FAILED_STATUS));
                return true;
            }

            if (args[0].equalsIgnoreCase("stop")) {
                if (!Arenas.arenaExists(args[1])) {
                    OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_ARENA_NOT_FOUND).replaceAll("%arena_name%", args[1]));
                    return true;
                }

                Arena arena = Arenas.getArena(args[1]);
                arena.sendAll(ChatColor.RED + player.getName() + ChatColor.GRAY + " Has stopped the Arena!");
                arena.stop();
                return true;
            }

            if (args[0].equalsIgnoreCase("addspawn")) {
                if (!Arenas.arenaExists(args[1])) {
                    OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_ARENA_NOT_FOUND).replaceAll("%arena_name%", args[1]));
                    return true;
                }

                Arena arena = Arenas.getArena(args[1]);
                arena.addSpawn(player.getLocation());
                OITC.sendMessage(player, "You have added a spawn for " + ChatColor.DARK_AQUA + arena.getName());
                return true;
            }

            if (args[0].equalsIgnoreCase("setlobby")) {
                if (!Arenas.arenaExists(args[1])) {
                    OITC.sendMessage(player, OITC.messageManager.getMessage(MessageEnum.COMMAND_ARENA_NOT_FOUND).replaceAll("%arena_name%", args[1]));
                    return true;
                }

                Arena arena = Arenas.getArena(args[1]);
                arena.setLobbySpawn(player.getLocation());
                OITC.sendMessage(player, "You have set the lobby spawn for " + ChatColor.DARK_AQUA + arena.getName());
                return true;
            }

            return true;
        }

        return true;
    }
}
