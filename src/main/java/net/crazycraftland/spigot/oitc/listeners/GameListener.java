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
import net.crazycraftland.spigot.oitc.arena.LeaveReason;
import net.crazycraftland.spigot.oitc.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class GameListener implements Listener {

    private OITC plugin;

    public GameListener(OITC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        final Player player = e.getPlayer();

        if (Arenas.isInArena(player)) {
            e.setCancelled(true);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                public void run() {
                    player.updateInventory();
                }
            }, 1L);
        }
    }


    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Arrow))) {
            Arrow arrow = (Arrow) e.getDamager();
            if ((arrow.getShooter() instanceof Player)) {
                Player attacker = (Player) arrow.getShooter();
                Player player = (Player) e.getEntity();
                if ((Arenas.isInArena(player)) && (Arenas.isInArena(attacker))) {
                    Arena arena = Arenas.getArena(player);
                    if (arena.isOn()) {
                        if (!player.getName().equalsIgnoreCase(attacker.getName())) {
                            e.setDamage(100.0D);
                        } else {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onProjHit(ProjectileHitEvent e) {

        if (e.getEntity() instanceof Arrow) {

            Arrow arrow = (Arrow) e.getEntity();

            if (arrow.getShooter() instanceof Player) {

                Player shooter = (Player) arrow.getShooter();

                if (Arenas.isInArena(shooter)) {
                    Arena arena = Arenas.getArena(shooter);
                    if (arena.isOn()) {
                        arrow.remove();
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();

        if (!Arenas.isInArena(player)) {
            return;
        }

        Arena arena = Arenas.getArena(player);
        if (arena.isOn()) {
            e.setRespawnLocation(arena.getRandomSpawn());
            Methods.setDefaultGameInventory(player);
            player.updateInventory();
        }


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        final Player player = e.getEntity();
        if (Arenas.isInArena(player)) {
            Arena arena = Arenas.getArena(player);
            if (arena.isOn()) {
                e.getDrops().clear();
                e.setDeathMessage("");
                e.setDroppedExp(0);

                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        player.spigot().respawn();
                    }
                }, 1);

                if (player.getKiller() != null) {
                    Player killer = player.getKiller();
                    onPlayerKill(killer, player);

                }
            }
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (!Arenas.isInArena(player)) {
            return;
        }

        Arena arena = Arenas.getArena(player);
        player.teleport(Methods.getLobby());
        player.getInventory().clear();
        arena.removePlayer(player, LeaveReason.QUIT);

    }


    public void onPlayerKill(Player killer, Player player) {
        Arena arena = Arenas.getArena(killer);
        killer.sendMessage(ChatColor.GRAY + "You have killed " + ChatColor.AQUA + player.getName());
        player.sendMessage(ChatColor.DARK_RED + killer.getName() + ChatColor.GRAY + " Has killed you!");

        Methods.addArrow(killer);

        Scoreboard board = killer.getScoreboard();
        @SuppressWarnings("deprecation")
        Score score = board.getObjective(DisplaySlot.SIDEBAR).getScore(killer);
        int kills = score.getScore();
        kills++;
        score.setScore(kills);

        if (kills >= arena.getKillsToWin()) {
            arena.sendAll("");
            arena.sendAll("");
            arena.sendAll("");
            arena.sendAll("");
            arena.sendAll("");
            arena.sendAll("");
            arena.sendAll("");
            arena.sendAll("");
            arena.sendAll("");

            arena.sendAll(ChatColor.GREEN + "================" + ChatColor.GRAY + "[" + ChatColor.AQUA + "OITC" + ChatColor.GRAY + "]" + ChatColor.GREEN + "================");


            arena.sendAll(ChatColor.RED + killer.getName() + ChatColor.GRAY +
                    " Has reached the kill goal of " + ChatColor.GOLD + arena.getKillsToWin() + ChatColor.GRAY +
                    " and has won in the Arena: " + ChatColor.AQUA + arena.getName());
            arena.sendAll(ChatColor.GREEN + "================" + ChatColor.GRAY + "[" + ChatColor.AQUA + "OITC" + ChatColor.GRAY + "]" + ChatColor.GREEN + "================");

            arena.sendAll("");
            arena.sendAll("");


            arena.stop();


        }

    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("oitc.admin") || player.isOp()) {
            return;
        }

        if (Arenas.isInArena(player)) {
            Arena arena = Arenas.getArena(player);
            if (arena.isOn()) {
                if (!e.getMessage().equalsIgnoreCase("/oitc") &&
                        !e.getMessage().equalsIgnoreCase("/oitc lobby") &&
                        !e.getMessage().equalsIgnoreCase("/oitc leave")) {

                    e.setCancelled(true);
                    OITC.sendMessage(player, "You cannot do any other commands besides the default /oitc commands");
                    OITC.sendMessage(player, "if you would like to leave, please do " + ChatColor.RED + "/oitc leave , OR /oitc lobby");
                }


            }
        }


    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (!Arenas.isInArena(player))
            return;

        Arena arena = Arenas.getArena(player);
        if (arena.isOn()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!Arenas.isInArena(player))
            return;

        Arena arena = Arenas.getArena(player);
        if (arena.isOn()) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getClickedBlock().getType() == Material.CHEST) {
                    e.setUseInteractedBlock(Event.Result.DENY);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (!Arenas.isInArena(player))
            return;

        Arena arena = Arenas.getArena(player);
        if (arena.isOn()) {
            e.setCancelled(true);
        }

    }

}
