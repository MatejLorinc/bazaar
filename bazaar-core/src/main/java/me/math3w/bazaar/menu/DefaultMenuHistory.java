package me.math3w.bazaar.menu;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.menu.MenuHistory;
import me.zort.containr.GUI;
import me.zort.containr.GUIRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class DefaultMenuHistory implements MenuHistory {
    private final BazaarPlugin plugin;
    private final Map<UUID, Stack<GUI>> playerHistories = new HashMap<>();
    private final Set<UUID> refreshingPlayers = new HashSet<>();

    public DefaultMenuHistory(BazaarPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void addGui(Player player, GUI gui) {
        UUID uniqueId = player.getUniqueId();

        if (!playerHistories.containsKey(uniqueId)) {
            playerHistories.put(uniqueId, new Stack<>());
        }

        Stack<GUI> history = playerHistories.get(uniqueId);
        history.add(gui);
    }

    @Override
    public void clearHistory(Player player) {
        playerHistories.remove(player.getUniqueId());
    }

    @Override
    public void setHistory(Player player, Stack<GUI> history) {
        playerHistories.put(player.getUniqueId(), history);
    }

    @Override
    public Stack<GUI> getHistory(Player player) {
        return playerHistories.get(player.getUniqueId());
    }

    @Override
    public boolean openPrevious(Player player) {
        boolean hasPrevious = false;

        UUID uniqueId = player.getUniqueId();

        if (playerHistories.containsKey(uniqueId)) {
            Stack<GUI> history = playerHistories.get(uniqueId);

            if (history.size() > 1) {
                history.pop();
                GUI gui = history.pop();
                gui.open(player);
                hasPrevious = true;
            }
        }

        if (!hasPrevious) {
            player.closeInventory();
        }

        return hasPrevious;
    }

    @Override
    public Optional<GUI> getPrevious(Player player) {
        return getPrevious(player.getUniqueId());
    }

    @Override
    public Optional<GUI> getPrevious(UUID uniqueId) {
        Stack<GUI> list = playerHistories.getOrDefault(uniqueId, new Stack<>());
        if (list.size() < 2) return Optional.empty();
        return Optional.of(list.get(list.size() - 2));
    }

    @Override
    public Optional<GUI> getCurrent(Player player) {
        return getCurrent(player.getUniqueId());
    }

    @Override
    public Optional<GUI> getCurrent(UUID uniqueId) {
        Stack<GUI> list = playerHistories.getOrDefault(uniqueId, new Stack<>());
        if (list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(list.size() - 1));
    }

    @Override
    public void refreshGui(Player player) {
        UUID playerUniqueId = player.getUniqueId();
        if (refreshingPlayers.contains(playerUniqueId)) return;

        refreshingPlayers.add(playerUniqueId);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            GUIRepository.reopenCurrent(player);
            refreshingPlayers.remove(playerUniqueId);
        }, 5);
    }
}
