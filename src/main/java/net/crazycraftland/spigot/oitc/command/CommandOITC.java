/*
 * MIT License
 *
 * Copyright (c) 2019 Ursin Filli
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
 *
 */

package net.crazycraftland.spigot.oitc.command;

import net.crazycraftland.spigot.oitc.OITC;
import net.crazycraftland.spigot.oitc.arena.Arena;
import net.crazycraftland.spigot.oitc.arena.Arenas;
import net.crazycraftland.spigot.oitc.arena.LeaveReason;
import net.crazycraftland.spigot.oitc.utils.Methods;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOITC implements CommandExecutor {

    private OITC plugin;

    public CommandOITC(OITC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be a player to send OITC commands");
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
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                String arenas = "List of Arenas: " + ChatColor.DARK_AQUA;
                for (Arena arena : Arenas.getArenas()) {
                    arenas = arenas + arena.getName() + ", ";
                }
                OITC.sendMessage(player, arenas);
            }

            if (args[0].equalsIgnoreCase("version")) {
                OITC.sendMessage(player, "You are using OITC Version " + ChatColor.RED + plugin.getDescription().getVersion());
            }

            if (player.hasPermission("oitc.admin")) {
                if (args[0].equalsIgnoreCase("setmainlobby")) {
                    Methods.setLobby(player.getLocation());
                    OITC.sendMessage(player, "You have set the Main Lobby!");
                }

                if (args[0].equalsIgnoreCase("reload")) {
                    Methods.loadYamls();
                    plugin.reloadConfig();
                    for (Arena arena : Arenas.getArenas()) {
                        arena.updateSigns();
                    }

                    OITC.sendMessage(player, "Reloaded Configs Successfully!");
                }

            }

            if (args[0].equalsIgnoreCase("lobby")) {
                if (!Arenas.isInArena(player)) {
                    if (Methods.getLobby() != null) {
                        player.teleport(Methods.getLobby());
                        OITC.sendMessage(player, "Welcome to the " + ChatColor.DARK_AQUA + "Main Lobby!");
                    } else {
                        OITC.sendMessage(player, "Oops, it seems there is no Main Lobby setup yet! Please alert your server admins.");
                    }
                } else {
                    Arena arena = Arenas.getArena(player);
                    OITC.sendMessage(player, "You have left your current arena and joined the lobby.");
                    arena.removePlayer(player, LeaveReason.QUIT);
                }
            }

            if (args[0].equalsIgnoreCase("leave")) {
                if (Arenas.isInArena(player)) {
                    Arena arena = Arenas.getArena(player);
                    arena.removePlayer(player, LeaveReason.QUIT);
                } else {
                    OITC.sendMessage(player, "You are not in an Arena to leave from, But you will still be teleported back to the lobby!");
                }
            }
        }


        if (args.length == 2) {
            if (player.hasPermission("oitc.admin")) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (!Arenas.arenaExists(args[1])) {
                        plugin.arenas.addDefault("Arenas." + args[1], args[1]);
                        plugin.arenas.addDefault("Arenas." + args[1] + ".Signs.Counter", 0);
                        plugin.getConfig().addDefault(args[1] + ".Countdown", 15);
                        plugin.getConfig().addDefault(args[1] + ".MaxPlayers", 20);
                        plugin.getConfig().addDefault(args[1] + ".KillsToWin", 25);
                        plugin.getConfig().addDefault(args[1] + ".AutoStartPlayers", 8);
                        plugin.getConfig().addDefault(args[1] + ".EndTime", 600);
                        Arena arena = new Arena(args[1]);
                        Arenas.addArena(arena);
                        Methods.addToList(arena);
                        OITC.sendMessage(player, ChatColor.GRAY + "You have created the Arena: " + ChatColor.GOLD + arena.getName());
                        Methods.saveYamls();
                        plugin.saveConfig();
                    } else {
                        OITC.sendMessage(player, ChatColor.RED + "That Arena already Exists!");
                    }
                }

                if (args[0].equalsIgnoreCase("delete")) {
                    if (plugin.getConfig().contains(args[1])) {
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
                    } else {
                        OITC.sendMessage(player, "Sorry, there is no such arena named " + ChatColor.RED + args[1]);
                    }
                }


                if (args[0].equalsIgnoreCase("start")) {
                    if (Arenas.arenaExists(args[1])) {
                        Arena arena = Arenas.getArena(args[1]);

                        if (arena.getPlayers().size() >= 2) {
                            arena.start();
                            OITC.sendMessage(player, "You have started the arena " + ChatColor.DARK_AQUA + arena.getName());

                        } else {
                            OITC.sendMessage(player, "Cannot start arena.");
                            OITC.sendMessage(player, "It is either ingame, stopping, or not enough players.");
                        }


                    } else {
                        OITC.sendMessage(player, "Sorry, there is no such arena named " + ChatColor.RED + args[1]);
                    }
                }

                if (args[0].equalsIgnoreCase("stop")) {
                    if (Arenas.arenaExists(args[1])) {

                        Arena arena = Arenas.getArena(args[1]);
                        arena.sendAll(ChatColor.RED + player.getName() + ChatColor.GRAY + " Has stopped the Arena!");
                        arena.stop();


                    } else {
                        OITC.sendMessage(player, "Sorry, there is no such arena named " + ChatColor.RED + args[1]);
                    }
                }

                if (args[0].equalsIgnoreCase("addspawn")) {
                    if (Arenas.arenaExists(args[1])) {
                        Arena arena = Arenas.getArena(args[1]);
                        arena.addSpawn(player.getLocation());
                        OITC.sendMessage(player, "You have added a spawn for " + ChatColor.DARK_AQUA + arena.getName());

                    } else {
                        OITC.sendMessage(player, "Sorry, there is no such arena named " + ChatColor.RED + args[1]);
                    }
                }

                if (args[0].equalsIgnoreCase("setlobby")) {
                    if (Arenas.arenaExists(args[1])) {
                        Arena arena = Arenas.getArena(args[1]);
                        arena.setLobbySpawn(player.getLocation());
                        OITC.sendMessage(player, "You have set the lobby spawn for " + ChatColor.DARK_AQUA + arena.getName());
                    } else {
                        OITC.sendMessage(player, "Sorry, there is no such arena named " + ChatColor.RED + args[1]);
                    }
                }
            }

        }
        return true;
    }
}
