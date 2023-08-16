package me.math3w.bazaar.utils;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.menu.MenuHistory;
import me.math3w.bazaar.menu.MenuConfiguration;
import me.zort.containr.*;
import me.zort.containr.internal.util.ItemBuilder;
import me.zort.containr.internal.util.Items;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuUtils {
    private MenuUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static Element getElementAtSlot(ContainerComponent container, int itemSlot) {
        return getContainerElements(container).get(itemSlot);
    }

    public static void updateGuiItem(MenuHistory menuHistory, ContainerComponent container, int itemSlot, Player player, ItemStack newItem) {
        for (Map.Entry<Integer, Element> slotElementEntry : getContainerElements(container).entrySet()) {
            int slot = slotElementEntry.getKey();
            Element element = slotElementEntry.getValue();

            if (slot != itemSlot) continue;

            container.setElement(itemSlot, Component.element(newItem).click(element::click).build());
            menuHistory.refreshGui(player);
            break;
        }
    }

    private static Map<Integer, Element> getContainerElements(ContainerComponent container) {
        if (!(container instanceof Container)) {
            return container.content(null);
        }

        try {
            Field elementsField = Container.class.getDeclaredField("elements");
            elementsField.setAccessible(true);
            Map<Integer, Element> elementMap = (Map<Integer, Element>) elementsField.get(container);
            return elementMap;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getNextFreeSlot(PagedContainer container) {
        int slot = 0;
        while (!container.isFreeSlot(slot)) {
            slot++;
        }
        return slot;
    }

    public static void setPagingArrows(MenuConfiguration menuConfiguration, GUI gui, PagedContainer container, BazaarAPI bazaarApi, Player player, int previousSlot, int nextSlot, boolean edit) {
        if (container.getCurrentPageIndex() > 0) {
            gui.setElement(previousSlot, Component.element()
                    .click(info -> {
                        container.previousPage();
                        setPagingArrows(menuConfiguration, gui, container, bazaarApi, player, previousSlot, nextSlot, edit);
                        gui.update(info.getPlayer());
                    })
                    .item(Items.create(Material.ARROW, ChatColor.GREEN + "Previous page"))
                    .build());
        } else {
            menuConfiguration.getItems().stream().filter(item -> item.getSlot() == previousSlot).findAny().ifPresent(glassItem -> {
                glassItem.putItem(gui, bazaarApi, player, null, edit);
            });
        }

        if (container.getCurrentPageIndex() < container.getMaxPageIndex()) {
            gui.setElement(nextSlot, Component.element()
                    .click(info -> {
                        container.nextPage();
                        setPagingArrows(menuConfiguration, gui, container, bazaarApi, player, previousSlot, nextSlot, edit);
                        gui.update(info.getPlayer());
                    })
                    .item(Items.create(Material.ARROW, ChatColor.GREEN + "Next page"))
                    .build());
        } else {
            menuConfiguration.getItems().stream().filter(item -> item.getSlot() == nextSlot).findAny().ifPresent(glassItem -> {
                glassItem.putItem(gui, bazaarApi, player, null, edit);
            });
        }
    }

    public static ItemStack getPlusSkull(String name) {
        return Items.createSkull(name, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjBiNTVmNzQ2ODFjNjgyODNhMWMxY2U1MWYxYzgzYjUyZTI5NzFjOTFlZTM0ZWZjYjU5OGRmMzk5MGE3ZTcifX19");
    }

    public static ItemStack appendEditLore(ItemStack item, boolean shouldAppend) {
        return appendEditLore(item, shouldAppend, false);
    }

    public static ItemStack appendEditLore(ItemStack item, boolean shouldAppend, boolean removable) {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "Right-Click to edit!");
        if (removable) {
            lore.add(ChatColor.DARK_AQUA + "Middle-Click to remove!");
        }
        return shouldAppend ? ItemBuilder.newBuilder(item).appendLore(lore).build() : item;
    }
}
