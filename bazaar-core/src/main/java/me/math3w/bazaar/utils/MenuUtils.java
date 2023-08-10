package me.math3w.bazaar.utils;

import me.math3w.bazaar.api.menu.MenuHistory;
import me.zort.containr.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
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
}
