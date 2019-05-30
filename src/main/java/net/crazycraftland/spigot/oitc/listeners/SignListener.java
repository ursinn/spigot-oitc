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

package net.crazycraftland.spigot.oitc.listeners;

import net.crazycraftland.spigot.oitc.OITC;
import net.crazycraftland.spigot.oitc.arena.Arena;
import net.crazycraftland.spigot.oitc.arena.Arenas;
import net.crazycraftland.spigot.oitc.arena.GameState;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

    @EventHandler
    public void onSignBreak(BlockBreakEvent e) {
        if (e.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) e.getBlock().getState();
            if (e.getPlayer().hasPermission("oitc.admin")) {
                if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "OITC" + ChatColor.DARK_GRAY + "]")) {
                    if (e.getPlayer().isSneaking()) {
                        for (Arena arena : Arenas.getArenas()) {
                            if (sign.getLine(1).equalsIgnoreCase(ChatColor.BOLD + arena.getName())) {
                                arena.removeSign(e.getBlock().getLocation());
                                OITC.sendMessage(e.getPlayer(), "You have removed a sign from " + ChatColor.DARK_AQUA + arena.getName());

                                break;
                            }
                        }
                    } else {
                        e.setCancelled(true);
                        sign.update(true);
                        OITC.sendMessage(e.getPlayer(), "If you want to break this sign, please sneak + break!");
                    }
                }
            } else {
                if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "OITC" + ChatColor.DARK_GRAY + "]")) {
                    e.setCancelled(true);
                    sign.update(true);
                }
            }
        }
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent e) {
        Player player = e.getPlayer();
        if ((e.getLine(0).equalsIgnoreCase("oitc")) &&
                (player.hasPermission("oitc.admin"))) {
            for (Arena arena : Arenas.getArenas()) {
                if (e.getLine(1).equalsIgnoreCase(arena.getName())) {
                    e.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "OITC" + ChatColor.DARK_GRAY + "]");
                    e.setLine(1, ChatColor.BOLD + arena.getName());
                    e.setLine(3, ChatColor.BOLD + "" + arena.getPlayers().size() + "/" + arena.getMaxPlayers());
                    if (arena.getState() == GameState.INGAME) {
                        e.setLine(2, ChatColor.DARK_RED + "Ingame");
                    } else {
                        if (arena.getState() == GameState.LOBBY) {
                            e.setLine(2, ChatColor.GREEN + "Waiting");
                        } else {
                            if (arena.getState() == GameState.STOPPING) {
                                e.setLine(2, ChatColor.RED + "Stopping");
                            } else {
                                if (arena.getState() == GameState.STARTING) {
                                    e.setLine(2, ChatColor.AQUA + "Starting");
                                }
                            }
                        }
                    }

                    arena.addSign(e.getBlock().getLocation());
                    arena.updateSigns();
                    player.sendMessage(ChatColor.GRAY + "You made a join sign for " + ChatColor.GOLD + arena.getName());
                }
            }
        }
    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.hasBlock() && e.getClickedBlock().getState() instanceof Sign) {
            Sign sign = (Sign) e.getClickedBlock().getState();
            if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() +
                    "OITC" + ChatColor.DARK_GRAY + "]")) {
                sign.update();

                if (Arenas.isInArena(player)) {
                    player.sendMessage(ChatColor.RED + "You are already in an Arena!");
                    player.sendMessage(ChatColor.GRAY + "If you would like to leave the current arena you are in, do /oitc leave");
                    return;
                }

                for (Arena arena : Arenas.getArenas()) {
                    if (sign.getLine(1).equalsIgnoreCase(ChatColor.BOLD + arena.getName())) {
                        if (!arena.hasPlayer(player)) {
                            //if(!arena.isOn()){
                            if (arena.getMaxPlayers() > arena.getPlayers().size()) {
                                arena.addPlayer(player);
                            } else {
                                player.sendMessage(ChatColor.RED + "Sorry! That Arena is full!");
                            }

                            //}else{
                            //player.sendMessage(ChatColor.RED + "Sorry! That Arena is " + arena.getState().toString());
                            //}
                        }

                        break;
                    }
                }
            }
        }

    }

}
