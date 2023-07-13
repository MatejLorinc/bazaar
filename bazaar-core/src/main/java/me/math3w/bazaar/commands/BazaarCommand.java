package me.math3w.bazaar.commands;

import me.math3w.bazaar.BazaarPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BazaarCommand implements CommandExecutor {
    private final BazaarPlugin bazaarPlugin;

    public BazaarCommand(BazaarPlugin bazaarPlugin) {
        this.bazaarPlugin = bazaarPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        bazaarPlugin.getBazaar().open(player);

        return true;
    }
}
