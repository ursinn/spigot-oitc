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

package dev.ursinn.spigot.oitc;

import dev.ursinn.spigot.oitc.arena.Arena;
import dev.ursinn.spigot.oitc.arena.Arenas;
import dev.ursinn.spigot.oitc.command.CommandOITC;
import dev.ursinn.spigot.oitc.listeners.GameListener;
import dev.ursinn.spigot.oitc.listeners.PlayerJoin;
import dev.ursinn.spigot.oitc.listeners.SignListener;
import dev.ursinn.spigot.oitc.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class OITC extends JavaPlugin {

    public static MessageManager messageManager = new MessageManager();
    public static boolean devBuild = true;
    public Options op = new Options(this);
    public UpdateChecker updateChecker = new UpdateChecker(67880, this);
    public File arenasFile;
    public FileConfiguration arenas;
    public File optionsFile;
    public FileConfiguration options;
    public File languageFile;
    public FileConfiguration language;
    private Methods methods = new Methods(this);

    public static void sendMessage(Player player, String message) {
        player.sendMessage(messageManager.getMessage(MessageEnum.OITC_SEND_MESSAGE).replaceAll("%message%", message));
    }

    @Override
    public void onEnable() {
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        ver = ver.substring(ver.lastIndexOf('.') + 1);
        if (!Methods.getVersions().containsKey(ver)) {
            getLogger().info(messageManager.getMessage(MessageEnum.STARTUP_UNSUPPORTED_MINECRAFT_VERSION));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        //*********** CONFIG FILES *****************
        getLogger().info(messageManager.getMessage(MessageEnum.STARTUP_LOADING_YML_START));

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

        getLogger().info(messageManager.getMessage(MessageEnum.STARTUP_LOADING_YML_FINISHED));
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
                new Metrics(this, 4780);
            if (op.isUpdateCheck())
                updateChecker.checkUpdates.start();
        }

        if (!op.isBungeeMode()) {
            try {
                for (String s : this.arenas.getStringList("Arenas.List")) {
                    Arena arena = new Arena(s);
                    getLogger().info(messageManager.getMessage(MessageEnum.STARTUP_LOADING_ARENA_START).replaceAll("%arena_name%", arena.getName()));

                    Arenas.addArena(arena);
                    arena.updateSigns();
                    getLogger().info(messageManager.getMessage(MessageEnum.STARTUP_LOADING_ARENA_FINISHED).replaceAll("%arena_name%", arena.getName()));
                }
            } catch (Exception e) {
                getLogger().severe(messageManager.getMessage(MessageEnum.STARTUP_LOADING_ARENA_FAILED));
            }
        } else {
            try {
                if (!this.arenas.getStringList("Arenas.List").contains(op.getBungeeModeArena()))
                    getLogger().severe(messageManager.getMessage(MessageEnum.STARTUP_LOADING_ARENA_SINGLE_MODE_FAILED));
                else {
                    Arena arena = new Arena(op.getBungeeModeArena());
                    getLogger().info(messageManager.getMessage(MessageEnum.STARTUP_LOADING_ARENA_SINGLE_MODE_START).replaceAll("%arena_name%", arena.getName()));

                    Arenas.addArena(arena);
                    getLogger().info(messageManager.getMessage(MessageEnum.STARTUP_LOADING_ARENA_SINGLE_MODE_FINISHED).replaceAll("%arena_name%", arena.getName()));
                }
            } catch (Exception e) {
                getLogger().severe(messageManager.getMessage(MessageEnum.STARTUP_LOADING_ARENA_SINGLE_MODE_FAILED));
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }

    @Override
    public void onDisable() {
        for (Arena arena : Arenas.getArenas()) {
            arena.stop();
        }
    }

}
