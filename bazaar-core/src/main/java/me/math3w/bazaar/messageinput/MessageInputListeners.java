package me.math3w.bazaar.messageinput;

import me.math3w.bazaar.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MessageInputListeners implements Listener {
    private final MessageInputManager messageInputManager;

    public MessageInputListeners(MessageInputManager messageInputManager) {
        this.messageInputManager = messageInputManager;
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!messageInputManager.isWaitingForOneLineInput(player)) return;

        messageInputManager.handleInput(player, Utils.colorize(event.getMessage()));
        event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        messageInputManager.removePlayer(event.getPlayer());
    }
}
