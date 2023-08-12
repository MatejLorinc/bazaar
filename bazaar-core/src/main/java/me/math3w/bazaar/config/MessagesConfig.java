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
        addDefault("order.reject", "&cThe order was rejected");
        addDefault("order.buy.success", "&7The buy order &6%amount%x %product% &7for &6%coins% coins &7was placed to the bazaar");
        addDefault("order.buy.not-enough", "&cYou don't have enough coins for this buy order &7[&6%amount%x %product% &7for &6%coins% coins&7]");
        addDefault("order.sell.success", "&7The sell offer &6%amount%x %product% &7for &6%coins% coins &7was placed to the bazaar");
        addDefault("order.sell.not-enough", "&cYou don't have enough items for this sell offer &7[&6%amount%x %product% &7for &6%coins% coins&7]");
        addDefault("claim.buy", "&7Claimed &a%amount%&7x &f%product% &7worth &6%total-coins% coins &7bought for &6%unit-coins% &7each");
        addDefault("claim.sell", "&7Claimed &6%total-coins% coins &7from selling &a%amount%&7x &f%product% &7at &6%unit-coins% &7each");
    }
}
