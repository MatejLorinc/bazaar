package me.math3w.bazaar.commands;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.messageinput.MessageInputManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditCommand implements CommandExecutor {
    private final BazaarPlugin bazaarPlugin;

    public EditCommand(BazaarPlugin bazaarPlugin) {
        this.bazaarPlugin = bazaarPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        if (args.length == 0) {
            bazaarPlugin.getBazaar().openEdit(player, bazaarPlugin.getBazaar().getCategories().get(0));
            return true;
        }

        if (args.length >= 2 && args[0].equals("lore")) {
            MessageInputManager messageInputManager = bazaarPlugin.getMessageInputManager();
            int lineIndex = args.length >= 3 ? Integer.parseInt(args[2]) : -1;
            switch (args[1]) {
                case "remove":
                    if (lineIndex < 0) break;
                    messageInputManager.removeLine(player, lineIndex);
                    break;
                case "edit":
                    if (lineIndex < 0) break;
                    messageInputManager.editLine(player, lineIndex);
                    break;
                case "add":
                    messageInputManager.addLine(player);
                    break;
                case "confirm":
                    messageInputManager.confirmMultiLineInput(player);
                    break;
            }
        }

        //bazaaredit lore edit/remove/add/confirm lineindex

        return true;
    }
}
