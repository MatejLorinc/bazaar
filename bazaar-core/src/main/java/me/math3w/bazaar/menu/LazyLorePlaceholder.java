package me.math3w.bazaar.menu;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.config.Placeholder;
import me.math3w.bazaar.utils.MenuUtils;
import me.math3w.bazaar.utils.Utils;
import me.zort.containr.ContainerComponent;
import me.zort.containr.Element;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LazyLorePlaceholder implements Placeholder {
    private final BazaarAPI bazaarAPI;
    private final ContainerComponent container;
    private final ItemStack item;
    private final int itemSlot;
    private final Player player;
    private final String placeholder;
    private final CompletableFuture<String> valueFuture;
    private final String loadingValue;

    public LazyLorePlaceholder(BazaarAPI bazaarAPI, ContainerComponent container, ItemStack item, int itemSlot, Player player, String placeholder, CompletableFuture<String> valueFuture, String loadingValue) {
        this.bazaarAPI = bazaarAPI;
        this.container = container;
        this.item = item;
        this.itemSlot = itemSlot;
        this.player = player;
        this.placeholder = placeholder;
        this.valueFuture = valueFuture;
        this.loadingValue = loadingValue;
    }

    private static LoreLine getLoreLine(String line, List<String> lore) {
        for (int i = 0; i < lore.size(); i++) {
            String loreLine = lore.get(i);
            if (!loreLine.contains(line)) continue;
            return new LoreLine(loreLine, i);
        }
        return null;
    }

    @Override
    public String replace(String text) {
        if (!text.contains("%" + placeholder + "%")) return text;

        String loadingLine = text.replaceAll("%" + placeholder + "%", loadingValue);

        //Need to wait at least 3 ticks because the ContainrGUI opens the menu with 3 ticks delay. So without this element will be null and value will stuck at loading if our valueFuture task would take less than 3 ticks
        Instant startInstant = Instant.now();
        valueFuture.thenAccept(value -> {
            Instant doneInstant = Instant.now();
            long waitTicks = Math.max(3 - (Duration.between(startInstant, doneInstant).toMillis() / 50), 0);

            Bukkit.getScheduler().runTaskLater(bazaarAPI.getPlugin(), () -> {
                Element element = MenuUtils.getElementAtSlot(container, itemSlot);
                if (element == null) return;

                ItemStack newItem = element.item(player).clone();
                ItemMeta itemMeta = newItem.getItemMeta();

                if (itemMeta == null || !itemMeta.hasLore()) return;
                List<String> lore = new ArrayList<>(itemMeta.getLore());

                LoreLine loreLine = getLoreLine(Utils.colorize(loadingLine), lore);
                if (loreLine == null) return;

                lore.set(loreLine.getIndex(), loreLine.getContent().replaceAll(loadingValue, value));

                itemMeta.setLore(lore);
                newItem.setItemMeta(itemMeta);

                MenuUtils.updateGuiItem(bazaarAPI.getMenuHistory(), container, itemSlot, player, newItem);
            }, waitTicks);
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });

        return loadingLine;
    }

    private static class LoreLine {
        private final String content;
        private final int index;

        public LoreLine(String content, int index) {
            this.content = content;
            this.index = index;
        }

        public String getContent() {
            return content;
        }

        public int getIndex() {
            return index;
        }
    }
}
