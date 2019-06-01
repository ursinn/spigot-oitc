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

package net.crazycraftland.spigot.oitc.utils;

import net.crazycraftland.spigot.oitc.OITC;
import net.crazycraftland.spigot.oitc.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Methods {

    private static OITC plugin;

    public Methods(OITC plugin) {
        Methods.plugin = plugin;
    }

    public static OITC getPlugin() {
        return plugin;
    }

    public static void addArrow(Player player) {
        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        player.getInventory().addItem(arrow);
    }

    public static void setDefaultGameInventory(Player player) {

        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        ItemStack sword = new ItemStack(getSwordMaterial(), 1);
        player.getInventory().clear();

        player.getInventory().addItem(sword);
        player.getInventory().addItem(bow);
        player.getInventory().addItem(arrow);

        player.updateInventory();
    }

    public static HashMap<String, Boolean> getVersions() {
        HashMap<String, Boolean> versions = new HashMap<>();
        versions.put("v_1_7_R1", true);
        versions.put("v_1_7_R2", true);
        versions.put("v_1_7_R3", true);
        versions.put("v_1_7_R4", true);
        versions.put("v1_8_R1", true);
        versions.put("v1_8_R2", true);
        versions.put("v1_8_R3", true);
        versions.put("v1_9_R1", true);
        versions.put("v1_9_R2", true);
        versions.put("v1_10_R1", true);
        versions.put("v1_11_R1", true);
        versions.put("v1_12_R1", true);
        versions.put("v1_13_R1", false);
        versions.put("v1_13_R2", false);
        versions.put("v1_14_R1", false);

        return versions;
    }

    private static Material getSwordMaterial() {
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        ver = ver.substring(ver.lastIndexOf('.') + 1);
        if (getVersions().get(ver)) {
            return Material.getMaterial("WOOD_SWORD");
        }

        return Material.getMaterial("LEGACY_WOOD_SWORD");
    }

    public static void setLobby(Location loc) {
        if (!plugin.arenas.contains("LobbySpawn")) {
            plugin.arenas.addDefault("LobbySpawn.X", loc.getX());
            plugin.arenas.addDefault("LobbySpawn.Y", loc.getY());
            plugin.arenas.addDefault("LobbySpawn.Z", loc.getZ());
            plugin.arenas.addDefault("LobbySpawn.World", loc.getWorld().getName());
            plugin.arenas.addDefault("LobbySpawn.Pitch", loc.getPitch());
            plugin.arenas.addDefault("LobbySpawn.Yaw", loc.getYaw());
        } else {
            plugin.arenas.set("LobbySpawn.X", loc.getX());
            plugin.arenas.set("LobbySpawn.Y", loc.getY());
            plugin.arenas.set("LobbySpawn.Z", loc.getZ());
            plugin.arenas.set("LobbySpawn.World", loc.getWorld().getName());
            plugin.arenas.set("LobbySpawn.Pitch", loc.getPitch());
            plugin.arenas.set("LobbySpawn.Yaw", loc.getYaw());
        }

        saveYamls();
    }

    public static Location getLobby() {
        if (plugin.arenas.contains("LobbySpawn.World")) {
            Location loc = new Location(Bukkit.getWorld(plugin.arenas.getString("LobbySpawn.World")),
                    plugin.arenas.getDouble("LobbySpawn.X"),
                    plugin.arenas.getDouble("LobbySpawn.Y"),
                    plugin.arenas.getDouble("LobbySpawn.Z"));
            loc.setPitch((float) plugin.arenas.getDouble("LobbySpawn.Pitch"));
            loc.setYaw((float) plugin.arenas.getDouble("LobbySpawn.Yaw"));
            return loc;
        }
        return null;
    }

    public static void addToList(Arena arena) {
        if (plugin.arenas.contains("Arenas.List")) {
            List<String> list = plugin.arenas.getStringList("Arenas.List");
            list.add(arena.getName());
            plugin.arenas.set("Arenas.List", list);
        } else {
            List<String> list = new ArrayList<>();
            list.add(arena.getName());
            plugin.arenas.addDefault("Arenas.List", list);
        }
    }

    public static void removeFromList(String name) {
        if (plugin.arenas.contains("Arenas.List")) {
            List<String> list = plugin.arenas.getStringList("Arenas.List");
            list.remove(name);
            plugin.arenas.set("Arenas.List", list);
        }
    }

    public static void saveYamls() {
        try {
            plugin.arenas.save(plugin.arenasFile);
            plugin.options.save(plugin.optionsFile);
            plugin.language.save(plugin.languageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadYamls() {
        try {
            plugin.arenas.load(plugin.arenasFile);
            plugin.options.load(plugin.optionsFile);
            plugin.language.load(plugin.languageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void firstRun() {
        try {
            if (!plugin.arenasFile.exists()) {
                plugin.arenas.save(plugin.arenasFile);
            }
            if (!plugin.optionsFile.exists()) {
                plugin.options.save(plugin.optionsFile);
            }
            if (!plugin.languageFile.exists()) {
                plugin.language.save(plugin.languageFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
