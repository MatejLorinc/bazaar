package me.math3w.bazaar.utils;

import org.bukkit.ChatColor;

public class Utils {
    private Utils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
