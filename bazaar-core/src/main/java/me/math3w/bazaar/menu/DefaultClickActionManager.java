package me.math3w.bazaar.menu;

import de.rapha149.signgui.SignGUI;
import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.OrderType;
import me.math3w.bazaar.api.menu.ClickActionManager;
import me.math3w.bazaar.api.menu.MenuInfo;
import me.zort.containr.ContextClickInfo;
import me.zort.containr.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

public class DefaultClickActionManager implements ClickActionManager {
    private final BazaarPlugin bazaarPlugin;
    private final Map<String, Function<MenuInfo, Consumer<ContextClickInfo>>> clickActions = new HashMap<>();

    public DefaultClickActionManager(BazaarPlugin bazaarPlugin) {
        this.bazaarPlugin = bazaarPlugin;

        addClickAction("close", ContextClickInfo::close);
        addClickAction("back", (Consumer<ContextClickInfo>) clickInfo -> bazaarPlugin.getMenuHistory().openPrevious(clickInfo.getPlayer()));
        addClickAction("search", clickInfo -> {
            clickInfo.getPlayer().closeInventory();

            new SignGUI()
                    .lines(bazaarPlugin.getMenuConfig().getStringList("search-sign").toArray(new String[4]))
                    .onFinish((player, lines) -> {
                        String filter = lines[0];
                        bazaarPlugin.getBazaar().openSearch(player, filter);
                        return null;
                    }).open(clickInfo.getPlayer());
        });
        addClickAction("buy-order", menuInfo -> clickInfo -> {
            if (!(menuInfo instanceof Product)) return;
            Product product = (Product) menuInfo;

            requireNumberFromPlayer(clickInfo.getPlayer(), "buy-order-amount-sign", amount -> {
                requireNumberFromPlayer(clickInfo.getPlayer(), "buy-order-price-sign", unitPrice -> {
                    BazaarOrder order = bazaarPlugin.getOrderManager().prepareBazaarOrder(product, amount, unitPrice, OrderType.BUY, clickInfo.getPlayer().getUniqueId());
                    bazaarPlugin.getBazaarConfig().getConfirmationMenuConfiguration(OrderType.BUY).getMenu(order).open(clickInfo.getPlayer());
                }, Double::parseDouble);
            }, Integer::parseInt);
        });
        addClickAction("sell-offer", menuInfo -> clickInfo -> {
            if (!(menuInfo instanceof Product)) return;
            Product product = (Product) menuInfo;

            requireNumberFromPlayer(clickInfo.getPlayer(), "sell-offer-amount-sign", amount -> {
                requireNumberFromPlayer(clickInfo.getPlayer(), "sell-offer-price-sign", unitPrice -> {
                    BazaarOrder order = bazaarPlugin.getOrderManager().prepareBazaarOrder(product, amount, unitPrice, OrderType.SELL, clickInfo.getPlayer().getUniqueId());
                    bazaarPlugin.getBazaarConfig().getConfirmationMenuConfiguration(OrderType.SELL).getMenu(order).open(clickInfo.getPlayer());
                }, Double::parseDouble);
            }, Integer::parseInt);
        });

        addClickAction("reject-order", clickInfo -> {
            bazaarPlugin.getMessagesConfig().sendMessage(clickInfo.getPlayer(), "order.reject");
            clickInfo.close();
        });

        addClickAction("confirm-order", menuInfo -> clickInfo -> {
            if (!(menuInfo instanceof BazaarOrder)) return;
            BazaarOrder order = (BazaarOrder) menuInfo;
            bazaarPlugin.getOrderManager().submitBazaarOrder(order);
            clickInfo.close();
        });
    }

    @Override
    public void addClickAction(String name, Consumer<ContextClickInfo> action) {
        addClickAction(name, menuInfo -> action);
    }

    @Override
    public void addClickAction(String name, Function<MenuInfo, Consumer<ContextClickInfo>> action) {
        clickActions.put(name, action);
    }

    @Override
    public Consumer<ContextClickInfo> getClickAction(String actionName, MenuInfo menuInfo) {
        return clickActions.getOrDefault(actionName, menuInfo1 -> clickInfo -> {
        }).apply(menuInfo);
    }

    private <T extends Number> void requireNumberFromPlayer(Player player, String sign, Consumer<T> callback, Function<String, T> parser) {
        Stack<GUI> history = bazaarPlugin.getMenuHistory().getHistory(player);
        player.closeInventory();

        new SignGUI()
                .lines(bazaarPlugin.getMenuConfig().getStringList(sign).toArray(new String[4]))
                .onFinish((signPlayer, lines) -> {
                    try {
                        T amount = parser.apply(lines[0]);
                        Bukkit.getScheduler().runTaskLater(bazaarPlugin, () -> {
                            bazaarPlugin.getMenuHistory().setHistory(player, history);
                            callback.accept(amount);
                        }, 1);
                    } catch (NumberFormatException exception) {
                        GUI lastGui = history.pop();
                        bazaarPlugin.getMenuHistory().setHistory(player, history);
                        lastGui.open(player);
                    }
                    return null;
                }).open(player);
    }
}
