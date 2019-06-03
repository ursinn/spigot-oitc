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

package net.crazycraftland.spigot.oitc.arena;

import net.crazycraftland.spigot.oitc.OITC;
import net.crazycraftland.spigot.oitc.utils.Methods;
import net.crazycraftland.spigot.oitc.utils.Options;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.*;

public class Arena {

    private String name;
    private GameState state = GameState.LOBBY;
    private int id = 0;
    private int counter;
    private int endtime;
    private boolean endtimeOn = false;
    private OITC plugin;
    private List<UUID> players = new ArrayList<>();
    private Scoreboard scoreboard;

    public Arena(String Name) {
        this.name = Name;
        this.plugin = Methods.getPlugin();
    }

    private HashMap<UUID, ItemStack[]> armor = new HashMap<>();
    private HashMap<UUID, ItemStack[]> inventory = new HashMap<>();
    private HashMap<UUID, GameMode> gamemode = new HashMap<>();


    private void saveInventory(Player player) {
        armor.put(player.getUniqueId(), player.getInventory().getArmorContents());
        inventory.put(player.getUniqueId(), player.getInventory().getContents());
        gamemode.put(player.getUniqueId(), player.getGameMode());

        player.getInventory().setArmorContents(null);
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.updateInventory();
    }

    private void loadInventory(Player player) {
        if (armor.containsKey(player.getUniqueId())) {
            player.getInventory().setArmorContents(armor.get(player.getUniqueId()));
            armor.remove(player.getUniqueId());
        }

        if (inventory.containsKey(player.getUniqueId())) {
            player.getInventory().setContents(inventory.get(player.getUniqueId()));
            inventory.remove(player.getUniqueId());
        }

        if (gamemode.containsKey(player.getUniqueId())) {
            player.setGameMode(gamemode.get(player.getUniqueId()));
            gamemode.remove(player.getUniqueId());
        }
        player.updateInventory();
    }


    public void sendAll(String Message) {
        List<UUID> nulls = new ArrayList<>();
        for (UUID s : this.players) {
            if (Bukkit.getPlayer(s) != null) {
                Bukkit.getPlayer(s).sendMessage(Message);
            } else {
                nulls.add(s);
            }
        }
        for (UUID s : nulls) {
            this.players.remove(s);
        }
        nulls.clear();
    }


    public GameState getState() {
        return state;
    }

    private void setState(GameState state) {
        this.state = state;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getRandomSpawn() {
        Random rand = new Random();
        if (this.plugin.arenas.contains("Arenas." + getName() + ".Spawns.Counter")) {
            int other = this.plugin.arenas.getInt("Arenas." + getName() + ".Spawns.Counter") - 1;
            int num = rand.nextInt(other) + 1;
            return getSpawn(num);
        }
        return null;
    }

    private Location getSpawn(int id) {
        if (this.plugin.arenas.contains("Arenas." + getName() + ".Spawns." + id + ".World")) {
            Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Arenas." + getName() + ".Spawns." + id + ".World")),
                    this.plugin.arenas.getDouble("Arenas." + getName() + ".Spawns." + id + ".X"),
                    this.plugin.arenas.getDouble("Arenas." + getName() + ".Spawns." + id + ".Y"),
                    this.plugin.arenas.getDouble("Arenas." + getName() + ".Spawns." + id + ".Z"));
            loc.setPitch((float) this.plugin.arenas.getDouble("Arenas." + getName() + ".Spawns." + id + ".Pitch"));
            loc.setYaw((float) this.plugin.arenas.getDouble("Arenas." + getName() + ".Spawns." + id + ".Yaw"));
            return loc;
        }
        return null;
    }

    public void addSpawn(Location loc) {
        if (!this.plugin.arenas.contains("Arenas." + getName() + ".Spawns.1.X")) {
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.Counter", 2);
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.1" + ".X", loc.getX());
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.1" + ".Y", loc.getY());
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.1" + ".Z", loc.getZ());
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.1" + ".World", loc.getWorld().getName());
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.1" + ".Pitch", loc.getPitch());
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.1" + ".Yaw", loc.getYaw());
        } else {
            int counter = this.plugin.arenas.getInt("Arenas." + getName() + ".Spawns.Counter");
            this.plugin.arenas.set("Arenas." + getName() + ".Spawns." + counter + ".X", loc.getX());
            this.plugin.arenas.set("Arenas." + getName() + ".Spawns." + counter + ".Y", loc.getY());
            this.plugin.arenas.set("Arenas." + getName() + ".Spawns." + counter + ".Z", loc.getZ());
            this.plugin.arenas.set("Arenas." + getName() + ".Spawns." + counter + ".World", loc.getWorld().getName());
            this.plugin.arenas.set("Arenas." + getName() + ".Spawns." + counter + ".Pitch", loc.getPitch());
            this.plugin.arenas.set("Arenas." + getName() + ".Spawns." + counter + ".Yaw", loc.getYaw());

            counter++;

            this.plugin.arenas.set("Arenas." + getName() + ".Spawns.Counter", counter);
        }
        Methods.saveYamls();
    }

    public void setLobbySpawn(Location loc) {
        if (!this.plugin.arenas.contains("Arenas." + getName() + ".Lobby.Spawn")) {
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Lobby.Spawn" + ".X", loc.getX());
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Lobby.Spawn" + ".Y", loc.getY());
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Lobby.Spawn" + ".Z", loc.getZ());
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Lobby.Spawn" + ".World", loc.getWorld().getName());
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Lobby.Spawn" + ".Pitch", loc.getPitch());
            this.plugin.arenas.addDefault("Arenas." + getName() + ".Lobby.Spawn" + ".Yaw", loc.getYaw());
        } else {
            this.plugin.arenas.set("Arenas." + getName() + ".Lobby.Spawn" + ".X", loc.getX());
            this.plugin.arenas.set("Arenas." + getName() + ".Lobby.Spawn" + ".Y", loc.getY());
            this.plugin.arenas.set("Arenas." + getName() + ".Lobby.Spawn" + ".Z", loc.getZ());
            this.plugin.arenas.set("Arenas." + getName() + ".Lobby.Spawn" + ".World", loc.getWorld().getName());
            this.plugin.arenas.set("Arenas." + getName() + ".Lobby.Spawn" + ".Pitch", loc.getPitch());
            this.plugin.arenas.set("Arenas." + getName() + ".Lobby.Spawn" + ".Yaw", loc.getYaw());
        }
        Methods.saveYamls();
    }

    private Location getLobbySpawn() {
        if (this.plugin.arenas.contains("Arenas." + getName() + ".Lobby.Spawn" + ".World")) {
            Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Arenas." + getName() + ".Lobby.Spawn" + ".World")),
                    this.plugin.arenas.getDouble("Arenas." + getName() + ".Lobby.Spawn" + ".X"),
                    this.plugin.arenas.getDouble("Arenas." + getName() + ".Lobby.Spawn" + ".Y"),
                    this.plugin.arenas.getDouble("Arenas." + getName() + ".Lobby.Spawn" + ".Z"));
            loc.setPitch((float) this.plugin.arenas.getDouble("Arenas." + getName() + ".Lobby.Spawn" + ".Pitch"));
            loc.setYaw((float) this.plugin.arenas.getDouble("Arenas." + getName() + ".Lobby.Spawn" + ".Yaw"));
            return loc;
        }
        return null;
    }

    public boolean isOn() {
        return (getState() == GameState.INGAME) || getState() == GameState.STOPPING;
    }

    public List<UUID> getPlayers() {
        return this.players;
    }

    private void healAll() {
        for (UUID s : this.players) {
            if (Bukkit.getPlayer(s) != null) {
                Bukkit.getPlayer(s).setHealth(20.0D);
                Bukkit.getPlayer(s).setFoodLevel(20);
            }
        }
    }

    private void setInventories() {
        for (UUID s : getPlayers()) {
            if (Bukkit.getPlayer(s) != null) {
                Methods.setDefaultGameInventory(Bukkit.getPlayer(s));
            }
        }
    }


    @SuppressWarnings("deprecation")
    private void setScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective main = board.registerNewObjective(ChatColor.RED + "OITC", "kills");
        main.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (UUID s : getPlayers()) {
            if (Bukkit.getPlayer(s) != null) {
                Player player = Bukkit.getPlayer(s);

                main.getScore(player).setScore(0);

                player.setScoreboard(board);
            }
        }

        scoreboard = board;

    }

    private void spawnPlayers() {
        for (UUID s : this.players) {
            if (Bukkit.getPlayer(s) != null) {
                Player player = Bukkit.getPlayer(s);
                Location loc = getRandomSpawn();
                if (loc != null) {
                    player.teleport(loc);
                }
            }
        }
    }

    public void start() {
        if (getState() == GameState.INGAME || getState() == GameState.STARTING || getState() == GameState.STOPPING) {
            return;
        }

        this.counter = this.plugin.getConfig().getInt(getName() + ".Countdown");

        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
            public void run() {
                if (Arena.this.counter > 0) {
                    setState(GameState.STARTING);
                    updateSigns();
                    if (Arena.this.counter == 30) {
                        sendAll(ChatColor.AQUA + "" + counter + ChatColor.GRAY + " seconds until the game starts.");
                    }
                    if (Arena.this.counter == 45) {
                        sendAll(ChatColor.AQUA + "" + counter + ChatColor.GRAY + " seconds until the game starts.");
                    }
                    if (Arena.this.counter == 15) {
                        sendAll(ChatColor.AQUA + "" + counter + ChatColor.GRAY + " seconds until the game starts.");
                    }
                    if (Arena.this.counter <= 10) {
                        sendAll(ChatColor.AQUA + "" + counter + ChatColor.GRAY + " seconds until the game starts.");
                    }
                    Arena.this.counter -= 1;
                } else {
                    Arena.this.sendAll(ChatColor.AQUA + "The game has started!");
                    setState(GameState.INGAME);
                    Arena.this.startGameTimer();
                    Arena.this.healAll();

                    Arena.this.setScoreboard();

                    Bukkit.getScheduler().cancelTask(Arena.this.id);
                    // Arena.this.check();
                    Arena.this.updateSigns();

                    //Arena.this.timeCheck();
                    Arena.this.spawnPlayers();
                    setInventories();

                    updateSigns();
                }
            }
        }, 0L, 20L);

    }

    public void stop() {
        GameState gs = getState();
        if (getState() == GameState.STARTING) {
            Bukkit.getScheduler().cancelTask(id);
        }

        setState(GameState.STOPPING);
        updateSigns();
        healAll();

        if (this.endtimeOn) {
            Bukkit.getScheduler().cancelTask(this.endtime);
        }

        Options options = plugin.op;
        Player first_player = null;
        int first_kills = 0;
        Player second_player = null;
        int second_kills = 0;
        Player third_player = null;
        int third_kills = 0;

        for (UUID s : players) {
            if (Bukkit.getPlayer(s) != null) {
                Player player = Bukkit.getPlayer(s);
                if (Methods.getLobby() != null) {
                    player.teleport(Methods.getLobby());
                } else {
                    player.sendMessage(ChatColor.RED + "Error: It seems the Main Lobby has not been setup yet, please tell your server owner ASAP.");
                }

                player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                loadInventory(player);
                player.sendMessage(ChatColor.GREEN + "We hope you had fun :)");
                OITC.sendMessage(player, "You have been teleported back to the Main Lobby.");
                Arenas.removeArena(player);

                if (gs == GameState.INGAME) {
                    if (options.getGameEnd_User() != null) {
                        for (String cmd : options.getGameEnd_User()) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", player.getName()));
                        }
                    }
                    if (getGameEnd_User() != null) {
                        for (String cmd : getGameEnd_User()) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", player.getName()));
                        }
                    }

                    int kills = scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(player).getScore();
                    if (kills > first_kills) {
                        if (first_player == null) {
                            first_kills = kills;
                            first_player = player;
                        } else {
                            if (second_player == null) {
                                second_kills = first_kills;
                                second_player = first_player;
                                first_kills = kills;
                                first_player = player;
                            } else {
                                third_kills = second_kills;
                                third_player = second_player;
                                second_kills = first_kills;
                                second_player = first_player;
                                first_kills = kills;
                                first_player = player;
                            }
                        }
                    } else if (kills > second_kills) {
                        if (second_player == null) {
                            second_kills = kills;
                            second_player = player;
                        } else {
                            third_kills = second_kills;
                            third_player = second_player;
                            second_kills = kills;
                            second_player = player;
                        }
                    } else if (kills > third_kills) {
                        third_kills = kills;
                        third_player = player;
                    }
                }
            }
        }

        if (gs == GameState.INGAME) {
            if (options.getGameEnd_Place1() != null) {
                if (first_player != null) {
                    for (String cmd : options.getGameEnd_Place1()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", first_player.getName()));
                    }
                }
            }
            if (getGameEnd_Place1() != null) {
                if (first_player != null) {
                    for (String cmd : getGameEnd_Place1()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", first_player.getName()));
                    }
                }
            }

            if (options.getGameEnd_Place2() != null) {
                if (second_player != null) {
                    for (String cmd : options.getGameEnd_Place2()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", second_player.getName()));
                    }
                }
            }
            if (getGameEnd_Place2() != null) {
                if (second_player != null) {
                    for (String cmd : getGameEnd_Place2()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", second_player.getName()));
                    }
                }
            }

            if (options.getGameEnd_Place3() != null) {
                if (third_player != null) {
                    for (String cmd : options.getGameEnd_Place3()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", third_player.getName()));
                    }
                }
            }
            if (getGameEnd_Place3() != null) {
                if (third_player != null) {
                    for (String cmd : getGameEnd_Place3()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", third_player.getName()));
                    }
                }
            }

            if (options.getGameEnd_Arena() != null) {
                for (String cmd : options.getGameEnd_Arena()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            }
            if (getGameEnd_Arena() != null) {
                for (String cmd : getGameEnd_Arena()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            }
        }

        this.players.clear();
        this.endtimeOn = false;

        setState(GameState.LOBBY);
        updateSigns();
    }

    public int getKillsToWin() {
        return this.plugin.getConfig().getInt(getName() + ".KillsToWin");
    }

    private void startGameTimer() {
        this.endtimeOn = true;
        this.endtime = Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                Arena.this.sendAll(ChatColor.GRAY + "The time limit has been reached!");
                Arena.this.stop();
            }
        }, this.plugin.getConfig().getInt(getName() + ".EndTime") * 20);
    }


    public void updateSigns() {
        for (Location loc : getSigns()) {
            if ((loc.getBlock().getState() instanceof Sign)) {
                Sign sign = (Sign) loc.getBlock().getState();
                int total = getPlayers().size();
                if (this.getState() == GameState.INGAME) {
                    sign.setLine(3, ChatColor.BOLD + "" + total + "/" + getMaxPlayers());
                } else {
                    sign.setLine(3, ChatColor.BOLD + "" + getPlayers().size() + "/" + getMaxPlayers());
                }


                if (getState() == GameState.INGAME) {
                    sign.setLine(2, ChatColor.DARK_RED + "Ingame");
                } else {
                    if (getState() == GameState.LOBBY) {
                        sign.setLine(2, ChatColor.GREEN + "Waiting");
                    } else {
                        if (getState() == GameState.STOPPING) {
                            sign.setLine(2, ChatColor.RED + "Stopping");
                        } else {
                            if (getState() == GameState.STARTING) {
                                sign.setLine(2, ChatColor.AQUA + "Starting");
                            }
                        }
                    }
                }

                sign.update();
            }
        }
    }

    private List<Location> getSigns() {
        String ArenaName = getName();
        List<Location> locs = new ArrayList<Location>();
        for (int count = 1; this.plugin.arenas.contains("Arenas." + ArenaName + ".Signs." + count + ".X"); count++) {
            Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Arenas." + ArenaName + ".Signs." + count + ".World")),
                    this.plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + count + ".X"),
                    this.plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + count + ".Y"),
                    this.plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + count + ".Z"));
            locs.add(loc);
        }
        return locs;
    }

    public void addSign(Location loc) {
        String Arena = getName();
        int counter = this.plugin.arenas.getInt("Arenas." + Arena + ".Signs.Counter");
        counter++;
        this.plugin.arenas.addDefault("Arenas." + Arena + ".Signs." + counter + ".X", loc.getX());
        this.plugin.arenas.addDefault("Arenas." + Arena + ".Signs." + counter + ".Y", loc.getY());
        this.plugin.arenas.addDefault("Arenas." + Arena + ".Signs." + counter + ".Z", loc.getZ());
        this.plugin.arenas.addDefault("Arenas." + Arena + ".Signs." + counter + ".World", loc.getWorld().getName());

        this.plugin.arenas.set("Arenas." + Arena + ".Signs.Counter", counter);

        Methods.saveYamls();
    }

    public void removeSign(Location loc) {
        String ArenaName = getName();
        for (int count = 1; this.plugin.arenas.contains("Arenas." + ArenaName + ".Signs." + count + ".X"); count++) {
            Location loc2 = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Arenas." + ArenaName + ".Signs." + count + ".World")),
                    this.plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + count + ".X"),
                    this.plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + count + ".Y"),
                    this.plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + count + ".Z"));

            if (loc.getX() == loc2.getX() && loc.getY() == loc2.getY() && loc.getZ() == loc2.getZ()) {
                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + count + ".X", null);
                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + count + ".Y", null);
                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + count + ".Z", null);
                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + count + ".World", null);
                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + count, null);

                resetSigns();

                Methods.saveYamls();
                break;
            }
        }
    }

    private void resetSigns() {
        String ArenaName = getName();
        int newCount = 0;
        int counter = this.plugin.arenas.getInt("Arenas." + ArenaName + ".Signs.Counter");

        for (int i = 0; i <= counter; i++) {
            if (plugin.arenas.contains("Arenas." + ArenaName + ".Signs." + i + ".X")) {
                newCount++;
                double x = plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + i + ".X");
                double y = plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + i + ".Y");
                double z = plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + i + ".Z");
                String world = plugin.arenas.getString("Arenas." + ArenaName + ".Signs." + i + ".World");

                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + i + ".X", null);
                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + i + ".Y", null);
                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + i + ".Z", null);
                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + i + ".World", null);
                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + i, null);


                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + newCount + ".X",
                        x);
                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + newCount + ".Y",
                        y);
                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + newCount + ".Z",
                        z);
                plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + newCount + ".World",
                        world);

                plugin.arenas.set("Arenas." + ArenaName + ".Signs.Counter", newCount);
            }
        }

    }

    public boolean hasPlayer(Player player) {
        return players.contains(player.getUniqueId());
    }

    public void addPlayer(Player player) {
        if (!players.contains(player.getUniqueId())) {
            players.add(player.getUniqueId());
            Arenas.addArena(player, this);
            sendAll(ChatColor.AQUA + player.getName() + ChatColor.GRAY + " Has joined.");

            saveInventory(player);

            if (getState() == GameState.INGAME) {
                Location loc = getRandomSpawn();
                if (loc != null) {
                    player.teleport(loc);
                    Methods.setDefaultGameInventory(player);
                    player.setScoreboard(scoreboard);
                    player.setHealth(20.0);
                    player.setFoodLevel(20);

                }

            } else {

                Location loc = getLobbySpawn();
                if (loc != null) {
                    player.teleport(loc);
                } else {
                    OITC.sendMessage(player, "Oops, It seems there is no lobby setup for this arena yet! Please contact your server admins.");
                }

                if (canStart()) {
                    start();
                }

            }

            updateSigns();
        }
    }

    public void removePlayer(Player player, LeaveReason reason) {
        if (this.players.contains(player.getUniqueId())) {
            this.players.remove(player.getUniqueId());
        }

        loadInventory(player);
        if (reason == LeaveReason.QUIT) {
            sendAll(ChatColor.RED + player.getName() + ChatColor.GRAY + " Has quit.");
        }

        if (reason == LeaveReason.KICK) {
            sendAll(ChatColor.RED + player.getName() + ChatColor.GRAY + " Has been kicked.");
        }
        if (reason == LeaveReason.DEATHS) {
            sendAll(ChatColor.RED + player.getName() + ChatColor.GRAY + " is eliminated!");
        }

        if (reason == LeaveReason.STOPPED) {
            player.sendMessage(ChatColor.GREEN + "We hope you had fun :)");
        }

        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());

        Arenas.removeArena(player);
        if (player.isInsideVehicle()) {
            player.getVehicle().eject();
        }

        player.teleport(Methods.getLobby());
        OITC.sendMessage(player, "You have been teleported back to the Main Lobby.");
        updateSigns();


        if (getState() == GameState.INGAME || getState() == GameState.STARTING) {
            if (players.size() <= 1) {
                stop();
            }
        }
    }

    public int getMaxPlayers() {
        return this.plugin.getConfig().getInt(getName() + ".MaxPlayers");
    }

    private int getAutoStartPlayers() {
        return this.plugin.getConfig().getInt(getName() + ".AutoStartPlayers");
    }

    private boolean canStart() {
        if (getState() != GameState.INGAME && getState() != GameState.STARTING && getState() != GameState.STOPPING) {
            return this.players.size() >= getAutoStartPlayers();
        }
        return false;
    }

    private List<String> getGameEnd_Arena() {
        return this.plugin.getConfig().getStringList(getName() + ".GameEnd.Arena");
    }

    private List<String> getGameEnd_User() {
        return this.plugin.getConfig().getStringList(getName() + ".GameEnd.User");
    }

    private List<String> getGameEnd_Place1() {
        return this.plugin.getConfig().getStringList(getName() + ".GameEnd.Place.1");
    }

    private List<String> getGameEnd_Place2() {
        return this.plugin.getConfig().getStringList(getName() + ".GameEnd.Place.2");
    }

    private List<String> getGameEnd_Place3() {
        return this.plugin.getConfig().getStringList(getName() + ".GameEnd.Place.3");
    }

}
