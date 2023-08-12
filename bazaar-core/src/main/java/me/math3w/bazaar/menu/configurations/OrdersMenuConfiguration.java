package me.math3w.bazaar.menu.configurations;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.menu.ConfigurableMenuItem;
import me.math3w.bazaar.menu.MenuConfiguration;
import me.math3w.bazaar.utils.MenuUtils;
import me.zort.containr.Component;
import me.zort.containr.GUI;
import me.zort.containr.PagedContainer;
import me.zort.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrdersMenuConfiguration extends MenuConfiguration {
    public OrdersMenuConfiguration(String name, int rows, List<ConfigurableMenuItem> items) {
        super(name, rows, items);
    }

    public static OrdersMenuConfiguration createDefaultConfiguration() {
        int[] glassSlots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 32, 33, 34, 35};

        List<ConfigurableMenuItem> items = new ArrayList<>();
        for (int glassSlot : glassSlots) {
            items.add(new ConfigurableMenuItem(glassSlot, ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 15).withName(ChatColor.WHITE.toString()).build(), ""));
        }

        items.add(new ConfigurableMenuItem(30,
                ItemBuilder.newBuilder(Material.ARROW)
                        .withName(ChatColor.GREEN + "Go Back")
                        .appendLore(ChatColor.GRAY + "To Bazaar")
                        .build(),
                "back"));
        items.add(new ConfigurableMenuItem(31,
                ItemBuilder.newBuilder(Material.BARRIER)
                        .withName(ChatColor.RED + "Close")
                        .build(),
                "close"));

        return new OrdersMenuConfiguration("Bazaar Orders", 4, items);
    }

    public static OrdersMenuConfiguration deserialize(Map<String, Object> args) {
        return new OrdersMenuConfiguration((String) args.get("name"),
                (Integer) args.get("rows"),
                (List<ConfigurableMenuItem>) args.get("items"));
    }

    public GUI getMenu(BazaarAPI bazaarApi) {
        return getMenuBuilder().prepare((gui, player) -> {
            super.loadItems(gui, bazaarApi, player, null);

            PagedContainer ordersContainer = Component.pagedContainer()
                    .size(7, 2)
                    .init(container -> {
                        bazaarApi.getOrderManager().getUnclaimedOrders(player.getUniqueId()).thenAccept(orders -> {
                            for (BazaarOrder order : orders) {
                                container.appendElement(Component.element()
                                        .click(clickInfo -> {
                                            int claimed = bazaarApi.getOrderManager().claimOrder(order);
                                            if (claimed > 0) {
                                                clickInfo.close();
                                            }
                                        })
                                        .item(order.getIcon())
                                        .build());
                            }
                            MenuUtils.setPagingArrows(this, gui, container, bazaarApi, player, 28, 34);
                            bazaarApi.getMenuHistory().refreshGui(player);
                        }).exceptionally(throwable -> {
                            throwable.printStackTrace();
                            return null;
                        });
                    })
                    .build();

            gui.setContainer(10, ordersContainer);
        }).build();
    }
}
