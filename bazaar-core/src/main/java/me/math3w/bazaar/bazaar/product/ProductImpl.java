package me.math3w.bazaar.bazaar.product;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.ProductCategory;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.OrderType;
import me.math3w.bazaar.api.config.MessagePlaceholder;
import me.math3w.bazaar.menu.LazyLorePlaceholder;
import me.math3w.bazaar.utils.Utils;
import me.zort.containr.ContainerComponent;
import me.zort.containr.internal.util.ItemBuilder;
import me.zort.containr.internal.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

public class ProductImpl implements Product {
    private final ProductCategory productCategory;
    private final ProductConfiguration config;

    public ProductImpl(ProductCategory productCategory, ProductConfiguration config) {
        this.productCategory = productCategory;
        this.config = config;
    }

    @Override
    public ItemStack getItem() {
        return config.getItem();
    }

    @Override
    public void setItem(ItemStack item) {
        config.setItem(item);
        productCategory.getCategory().getBazaar().saveConfig();
    }

    @Override
    public ItemStack getIcon(ContainerComponent container, int itemSlot, Player player) {
        BazaarAPI bazaarApi = getBazaarApi();
        ItemStack icon = config.getIcon();

        return bazaarApi.getMenuConfig().replaceLorePlaceholders(icon,
                "product-lore",
                new LazyLorePlaceholder(bazaarApi,
                        container,
                        icon,
                        itemSlot,
                        player,
                        "buy-price",
                        getLowestBuyPrice().thenApply(Utils::getTextPrice),
                        bazaarApi.getMenuConfig().getString("loading")),
                new LazyLorePlaceholder(bazaarApi,
                        container,
                        icon,
                        itemSlot,
                        player,
                        "sell-price",
                        getHighestSellPrice().thenApply(Utils::getTextPrice),
                        bazaarApi.getMenuConfig().getString("loading")));
    }

    @Override
    public ItemStack getRawIcon() {
        return config.getIcon().clone();
    }

    @Override
    public void setIcon(ItemStack icon) {
        config.setIcon(icon);
        productCategory.getCategory().getBazaar().saveConfig();
    }

    @Override
    public ItemStack getConfirmationIcon(double unitPrice, int amount) {
        return getBazaarApi().getMenuConfig().replaceLorePlaceholders(
                ItemBuilder.newBuilder(config.getItem()).appendLore("%confirm-lore%").build(),
                "confirm-lore",
                new MessagePlaceholder("unit-price", Utils.getTextPrice(unitPrice)),
                new MessagePlaceholder("total-price", Utils.getTextPrice(unitPrice * amount)),
                new MessagePlaceholder("amount", String.valueOf(amount)),
                new MessagePlaceholder("product", getName()));
    }

    @Override
    public String getName() {
        return Utils.colorize(config.getName());
    }

    @Override
    public void setName(String name) {
        config.setName(name);
        productCategory.getCategory().getBazaar().saveConfig();
    }

    @Override
    public String getId() {
        return getName().replace(" ", "_").toLowerCase();
    }

    @Override
    public ProductCategory getProductCategory() {
        return productCategory;
    }

    @Override
    public CompletableFuture<Double> getLowestBuyPrice() {
        return getPrice(OrderType.SELL);
    }

    @Override
    public CompletableFuture<Double> getHighestSellPrice() {
        return getPrice(OrderType.BUY);
    }

    private CompletableFuture<Double> getPrice(OrderType orderType) {
        return getBazaarApi().getOrderManager().getOrders(this, orderType, orders -> false).thenApply(orders -> orders.isEmpty() ? 0 : orders.get(0).getUnitPrice());
    }

    @Override
    public CompletableFuture<Pair<Double, Integer>> getBuyPriceWithOrderableAmount(int amount) {
        return getPriceWithOrderableAmount(OrderType.SELL, amount);
    }

    @Override
    public CompletableFuture<Pair<Double, Integer>> getSellPriceWithOrderableAmount(int amount) {
        return getPriceWithOrderableAmount(OrderType.BUY, amount);
    }

    private CompletableFuture<Pair<Double, Integer>> getPriceWithOrderableAmount(OrderType orderType, int amount) {
        return getBazaarApi().getOrderManager().getOrders(this, orderType, orders -> orders.stream().map(BazaarOrder::getOrderableItems).count() < amount)
                .thenApply(orders -> {
                    double price = 0;
                    int itemAmount = 0;
                    for (BazaarOrder order : orders) {
                        price += order.getUnitPrice() * order.getOrderableItems();
                        itemAmount += order.getOrderableItems();
                    }
                    return new Pair<>(price, Math.min(itemAmount, amount));
                });
    }

    public ProductConfiguration getConfig() {
        return config;
    }

    private BazaarAPI getBazaarApi() {
        return productCategory.getCategory().getBazaar().getBazaarApi();
    }
}
