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

package dev.ursinn.spigot.oitc.arena;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Arenas {

    private static HashMap<String, Arena> arenas = new HashMap<>();
    private static HashMap<String, Arena> playerArena = new HashMap<>();
    private static List<Arena> list = new ArrayList<>();

    public static Arena getArena(String ArenaName) {
        if (arenas.containsKey(ArenaName)) {
            return arenas.get(ArenaName);
        }
        return null;
    }

    public static List<Arena> getArenas() {
        return list;
    }

    public static boolean isInArena(Player player) {
        return playerArena.containsKey(player.getName());
    }

    public static void removeArena(Player player) {
        playerArena.remove(player.getName());
    }

    public static void addArena(Arena arena) {
        if (!arenas.containsKey(arena.getName())) {
            arenas.put(arena.getName(), arena);
            if (!list.contains(arena)) {
                list.add(arena);
            }
        }
    }

    public static void delArena(Arena arena) {
        if (arenas.containsKey(arena.getName())) {
            arenas.remove(arena.getName());
            list.remove(arena);
        }
    }

    public static void addArena(Player player, Arena arena) {
        if (!playerArena.containsKey(player.getName())) {
            playerArena.put(player.getName(), arena);
        }
    }

    public static Arena getArena(Player player) {
        String name = player.getName();
        if (playerArena.containsKey(name)) {
            return playerArena.get(name);
        }
        return null;
    }

    public static boolean arenaExists(String ArenaName) {
        return arenas.containsKey(ArenaName);
    }

}
