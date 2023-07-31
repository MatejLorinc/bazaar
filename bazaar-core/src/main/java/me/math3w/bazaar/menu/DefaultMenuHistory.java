package me.math3w.bazaar.menu;

import me.math3w.bazaar.api.menu.MenuHistory;
import me.zort.containr.GUI;
import org.bukkit.entity.Player;

import java.util.*;

public class DefaultMenuHistory implements MenuHistory {
    private final Map<UUID, Stack<GUI>> playerHistories = new HashMap<>();

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
    public boolean openPrevious(Player player) {
        boolean hasPrevious = false;

        UUID uniqueId = player.getUniqueId();

        if (playerHistories.containsKey(uniqueId)) {
            Stack<GUI> history = playerHistories.get(uniqueId);

            if (!history.isEmpty()) {
                history.pop();
                GUI gui = history.peek();
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
        if (list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(list.size() - 2));
    }
}
