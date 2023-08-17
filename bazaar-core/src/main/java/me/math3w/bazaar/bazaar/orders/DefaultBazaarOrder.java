package me.math3w.bazaar.bazaar.orders;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.OrderType;
import me.math3w.bazaar.api.config.MenuConfig;
import me.math3w.bazaar.api.config.MessagePlaceholder;
import me.math3w.bazaar.utils.Utils;
import me.zort.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.UUID;

public class DefaultBazaarOrder implements BazaarOrder {
    private final Product product;
    private final int amount;
    private final double unitPrice;
    private final OrderType type;
    private final UUID player;
    private final Instant createdAt;
    private int databaseId = -1;
    private int filled;
    private int claimed;

    public DefaultBazaarOrder(int databaseId, Product product, int amount, double unitPrice, OrderType type, UUID player, int filled, int claimed, Instant createdAt) {
        this.databaseId = databaseId;
        this.product = product;
        this.amount = amount;
        this.unitPrice = unitPrice;
        this.type = type;
        this.player = player;
        this.createdAt = createdAt;
        this.filled = filled;
        this.claimed = claimed;
    }

    public DefaultBazaarOrder(Product product, int amount, double unitPrice, OrderType type, UUID player, int filled, int claimed, Instant createdAt) {
        this(-1, product, amount, unitPrice, type, player, filled, claimed, createdAt);
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public OrderType getType() {
        return type;
    }

    @Override
    public UUID getPlayer() {
        return player;
    }

    @Override
    public int getFilled() {
        return filled;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public void fill(int amount) {
        this.filled += amount;
    }

    @Override
    public int getClaimed() {
        return claimed;
    }

    @Override
    public void claim(int amount) {
        this.claimed += amount;
    }

    @Override
    public boolean isSimilar(BazaarOrder other) {
        if (!product.equals(other.getProduct())) return false;
        if (unitPrice != other.getUnitPrice()) return false;
        if (type != other.getType()) return false;
        return true;
    }

    @Override
    public ItemStack getIcon() {
        BazaarAPI bazaarApi = product.getProductCategory().getCategory().getBazaar().getBazaarApi();
        MenuConfig menuConfig = bazaarApi.getMenuConfig();

        String placeholder = "order." + type.name().toLowerCase() + ".lore";
        ItemStack icon = ItemBuilder.newBuilder(product.getItem())
                .withName(menuConfig.getString("order." + type.name().toLowerCase() + ".prefix") + ChatColor.RESET + product.getName())
                .appendLore("%" + placeholder + "%")
                .build();

        return menuConfig.replaceLorePlaceholders(icon,
                placeholder,
                new MessagePlaceholder("total-coins", Utils.getTextPrice(unitPrice * amount)),
                new MessagePlaceholder("amount", String.valueOf(amount)),
                new MessagePlaceholder("filled", String.valueOf(filled)),
                new MessagePlaceholder("percent", String.valueOf((int) ((filled / (float) amount) * 100))),
                new MessagePlaceholder("coins", String.valueOf(unitPrice)),
                new MessagePlaceholder("available", String.valueOf(getAvailableItems())),
                new MessagePlaceholder("available-coins", String.valueOf(getAvailableCoins())));
    }

    @Override
    public double getAvailableCoins() {
        return unitPrice * getAvailableItems();
    }

    @Override
    public int getOrderableItems() {
        return amount - filled;
    }

    @Override
    public int getAvailableItems() {
        return filled - claimed;
    }

    @Override
    public int getDatabaseId() {
        return databaseId;
    }
}
