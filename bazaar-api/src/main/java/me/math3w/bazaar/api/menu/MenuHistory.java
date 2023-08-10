package me.math3w.bazaar.api.menu;

import me.zort.containr.GUI;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Stack;
import java.util.UUID;

public interface MenuHistory {
    void addGui(Player player, GUI gui);

    void clearHistory(Player player);

    void setHistory(Player player, Stack<GUI> history);

    Stack<GUI> getHistory(Player player);

    boolean openPrevious(Player player);

    Optional<GUI> getPrevious(Player player);

    Optional<GUI> getPrevious(UUID uniqueId);

    Optional<GUI> getCurrent(Player player);

    Optional<GUI> getCurrent(UUID uniqueId);

    void refreshGui(Player player);
}
