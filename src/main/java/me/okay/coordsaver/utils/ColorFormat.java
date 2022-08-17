package me.okay.coordsaver.utils;

import org.bukkit.ChatColor;

public class ColorFormat {
    // returns the string colored by &
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
