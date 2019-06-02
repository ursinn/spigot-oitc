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

import java.util.ArrayList;
import java.util.List;

public class Options {

    private boolean BungeeMode;
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
        list.add("");
        this.plugin.options.addDefault("BungeeMode", false);
        this.plugin.options.addDefault("GameEnd.Arena", list);
        this.plugin.options.addDefault("GameEnd.User", list2);
        this.plugin.options.addDefault("GameEnd.Place.1", list3);
        this.plugin.options.addDefault("GameEnd.Place.2", list4);
        this.plugin.options.addDefault("GameEnd.Place.3", list5);
        Methods.saveYamls();
    }

    public void loadOptions() {
        this.BungeeMode = this.plugin.options.getBoolean("BungeeMode");
        this.GameEnd_Arena = this.plugin.options.getStringList("GameEnd.Arena");
        this.GameEnd_User = this.plugin.options.getStringList("GameEnd.User");
        this.GameEnd_Place1 = this.plugin.options.getStringList("GameEnd.Place.1");
        this.GameEnd_Place2 = this.plugin.options.getStringList("GameEnd.Place.2");
        this.GameEnd_Place3 = this.plugin.options.getStringList("GameEnd.Place.3");
    }

    public boolean isBungeeMode() {
        return BungeeMode;
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
}
