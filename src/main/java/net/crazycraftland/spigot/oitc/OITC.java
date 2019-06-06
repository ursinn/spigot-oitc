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

package net.crazycraftland.spigot.oitc;

import net.crazycraftland.spigot.oitc.arena.Arena;
import net.crazycraftland.spigot.oitc.arena.Arenas;
import net.crazycraftland.spigot.oitc.command.CommandOITC;
import net.crazycraftland.spigot.oitc.listeners.GameListener;
import net.crazycraftland.spigot.oitc.listeners.PlayerJoin;
import net.crazycraftland.spigot.oitc.listeners.SignListener;
import net.crazycraftland.spigot.oitc.utils.Methods;
import net.crazycraftland.spigot.oitc.utils.Options;
import net.crazycraftland.spigot.oitc.utils.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class OITC extends JavaPlugin {

    private final Logger logger = Logger.getLogger("Minecraft");
    private final Methods methods = new Methods(this);
    public final Options op = new Options(this);
    public final UpdateChecker updateChecker = new UpdateChecker(67880, this);

    public static boolean devBuild = true;

    public File arenasFile;
    public FileConfiguration arenas;
    public File optionsFile;
    public FileConfiguration options;
    public File languageFile;
    public FileConfiguration language;

    @Override
    public void onEnable() {
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        ver = ver.substring(ver.lastIndexOf('.') + 1);
        if (!Methods.getVersions().containsKey(ver)) {
            this.logger.info("[OITC] unsupported Minecraft Server version! (" + ver + ")");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        //*********** CONFIG FILES *****************
        this.logger.info("[OITC] Loading YML files!");

        this.arenasFile = new File(getDataFolder(), "arenas.yml");
        this.arenas = new YamlConfiguration();
        this.optionsFile = new File(getDataFolder(), "options.yml");
        this.options = new YamlConfiguration();
        this.languageFile = new File(getDataFolder(), "language.yml");
        this.language = new YamlConfiguration();

        methods.firstRun();
        methods.loadYamls();

        this.arenas.options().copyDefaults(true);
        this.options.options().copyDefaults(true);
        this.language.options().copyDefaults(true);
        getConfig().options().copyDefaults(true);

        op.setDefaults();
        op.loadOptions();

        this.logger.info("[OITC] Loaded YML files Successfully!");
        //*********** LISTENERS *****************
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        //*********** COMMANDS *****************
        CommandOITC commandOITC = new CommandOITC(this);
        getCommand("oitc").setExecutor(commandOITC);
        //*************************************
        if (!devBuild) {
            if (op.isUseMetrics())
                new Metrics(this);
            if (op.isUpdateCheck())
                updateChecker.checkUpdates.start();
        }

        try {
            for (String s : this.arenas.getStringList("Arenas.List")) {
                Arena arena = new Arena(s);
                this.logger.info("[OITC] Now Currently Loading The Arena: " + arena.getName());

                Arenas.addArena(arena);
                arena.updateSigns();
                this.logger.info("[OITC] The Arena: " + arena.getName() + " Has successfully loaded!");
            }
        } catch (Exception e) {
            this.logger.info("[OITC] WARNING, FAILED TO LOAD ARENAS.");
        }
    }

    @Override
    public void onDisable() {
        for (Arena arena : Arenas.getArenas()) {
            arena.stop();
        }
    }

    public static void sendMessage(Player player, String Message) {
        player.sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "OITC" + ChatColor.GRAY + "] " + ChatColor.GRAY + Message);
    }

}
