package me.math3w.bazaar.config;

import me.math3w.bazaar.api.config.MessagePlaceholder;
import me.math3w.bazaar.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MessagesConfig extends CustomConfig {
    public MessagesConfig(JavaPlugin plugin) {
        super(plugin, "messages");
    }

    public static String formatMessage(String message, MessagePlaceholder... placeholders) {
        for (MessagePlaceholder placeholder : placeholders) {
            message = message.replaceAll("%" + placeholder.getPlaceholder() + "%", placeholder.getValue());
        }

        return Utils.colorize(message);
    }

    public String getMessage(String path, MessagePlaceholder... placeholders) {
        return formatMessage(getConfig().getString(path, "&cMissing message: " + getFile().getName() + "/" + path), placeholders);
    }

    public void sendMessage(Player player, String path, MessagePlaceholder... placeholders) {
        String message = getMessage(path, placeholders);
        if (message.isEmpty()) return;
        player.sendMessage(message);
    }

    @Override
    protected void addDefaults() {

    }
}
