/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Ursin Filli
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

package dev.ursinn.spigot.oitc.utils;

import dev.ursinn.spigot.oitc.OITC;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class Options {

    private boolean BungeeMode;
    private boolean useMetrics;
    private boolean updateCheck;
    private List<String> GameEnd_Arena;
    private List<String> GameEnd_User;
    private List<String> GameEnd_Place1;
    private List<String> GameEnd_Place2;
    private List<String> GameEnd_Place3;

    private OITC plugin;

    public Options(OITC plugin) {
        this.plugin = plugin;
    }

    public void setDefaults() {
        List<String> list = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        List<String> list4 = new ArrayList<>();
        List<String> list5 = new ArrayList<>();
        this.plugin.options.addDefault("Metrics", true);
        this.plugin.options.addDefault("UpdateCheck", true);
        this.plugin.options.addDefault("AutoHeal", false);
        //*** BungeeMode ***
        this.plugin.options.addDefault("BungeeMode.use", false);
        this.plugin.options.addDefault("BungeeMode.arena", "");
        //*** GameEnd ***
        this.plugin.options.addDefault("GameEnd.Arena", list);
        this.plugin.options.addDefault("GameEnd.User", list2);
        this.plugin.options.addDefault("GameEnd.Place.1", list3);
        this.plugin.options.addDefault("GameEnd.Place.2", list4);
        this.plugin.options.addDefault("GameEnd.Place.3", list5);
        //*** Enchantment ***
        this.plugin.options.addDefault("Enchantment.KNOCKBACK.use", false);
        this.plugin.options.addDefault("Enchantment.KNOCKBACK.level", 1);
        this.plugin.options.addDefault("Enchantment.DAMAGE_ALL.use", false);
        this.plugin.options.addDefault("Enchantment.DAMAGE_ALL.level", 4);
        this.plugin.options.addDefault("Enchantment.DURABILITY.use", false);
        this.plugin.options.addDefault("Enchantment.DURABILITY.level", 3);
        //*** Sword ***
        this.plugin.options.addDefault("Sword.Unbreakable", false);
        this.plugin.options.addDefault("Sword.ItemFlag.HIDE_ENCHANTS.use", false);
        this.plugin.options.addDefault("Sword.ItemFlag.HIDE_ATTRIBUTES.use", false);
        this.plugin.options.addDefault("Sword.ItemFlag.HIDE_UNBREAKABLE.use", false);
        //*** Bow ***
        this.plugin.options.addDefault("Bow.Unbreakable", false);
        this.plugin.options.addDefault("Bow.ItemFlag.HIDE_ATTRIBUTES.use", false);
        this.plugin.options.addDefault("Bow.ItemFlag.HIDE_UNBREAKABLE.use", false);
        Methods.saveYamls();
    }

    public void loadOptions() {
        this.BungeeMode = this.plugin.options.getBoolean("BungeeMode.use");
        this.useMetrics = this.plugin.options.getBoolean("Metrics");
        this.updateCheck = this.plugin.options.getBoolean("UpdateCheck");
        this.GameEnd_Arena = this.plugin.options.getStringList("GameEnd.Arena");
        this.GameEnd_User = this.plugin.options.getStringList("GameEnd.User");
        this.GameEnd_Place1 = this.plugin.options.getStringList("GameEnd.Place.1");
        this.GameEnd_Place2 = this.plugin.options.getStringList("GameEnd.Place.2");
        this.GameEnd_Place3 = this.plugin.options.getStringList("GameEnd.Place.3");
    }

    public boolean isBungeeMode() {
        return BungeeMode;
    }

    public boolean isUseMetrics() {
        return useMetrics;
    }

    public boolean isUpdateCheck() {
        return updateCheck;
    }

    public List<String> getGameEnd_Arena() {
        return GameEnd_Arena;
    }

    public List<String> getGameEnd_User() {
        return GameEnd_User;
    }

    public List<String> getGameEnd_Place1() {
        return GameEnd_Place1;
    }

    public List<String> getGameEnd_Place2() {
        return GameEnd_Place2;
    }

    public List<String> getGameEnd_Place3() {
        return GameEnd_Place3;
    }

    public List<SwordEnchantment> getSwordEnchantments() {
        List<SwordEnchantment> list = new ArrayList<>();
        for (Enchantment e : Enchantment.values()) {
            boolean b = this.plugin.options.getBoolean("Enchantment." + e.getName() + ".use");
            if (b) {
                int level = this.plugin.options.getInt("Enchantment." + e.getName() + ".level");
                list.add(new SwordEnchantment(e, level));
            }
        }
        return list;
    }

    public List<ItemFlag> getSwordItemFlags() {
        List<ItemFlag> list = new ArrayList<>();
        for (ItemFlag e : ItemFlag.values()) {
            boolean b = this.plugin.options.getBoolean("Sword.ItemFlag." + e.name() + ".use");
            if (b)
                list.add(e);
        }
        return list;
    }

    public boolean isSwordUnbreakable() {
        return this.plugin.options.getBoolean("Sword.Unbreakable");
    }

    public List<ItemFlag> getBowItemFlags() {
        List<ItemFlag> list = new ArrayList<>();
        for (ItemFlag e : ItemFlag.values()) {
            boolean b = this.plugin.options.getBoolean("Bow.ItemFlag." + e.name() + ".use");
            if (b)
                list.add(e);
        }
        return list;
    }

    public boolean isBowUnbreakable() {
        return this.plugin.options.getBoolean("Bow.Unbreakable");
    }

    public boolean isAutoHeal() {
        return this.plugin.options.getBoolean("AutoHeal");
    }

    public String getBungeeModeArena() {
        return this.plugin.options.getString("BungeeMode.arena");
    }

}
