package me.math3w.bazaar.config;

import me.math3w.bazaar.bazaar.category.CategoryConfiguration;
import me.math3w.bazaar.bazaar.productcategory.ProductCategoryConfiguration;
import me.math3w.bazaar.menu.CategoryMenuConfiguration;
import me.math3w.bazaar.utils.Utils;
import me.zort.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BazaarConfig extends CustomConfig {
    public BazaarConfig(JavaPlugin plugin) {
        super(plugin, "bazaar");
    }

    @Override
    protected void addDefaults() {
        addDefaultCategories();
    }

    private void addDefaultCategories() {
        ArrayList<CategoryConfiguration> categories = new ArrayList<>();

        categories.add(createDefaultCategory(Material.GOLD_HOE, "&eFarming", ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 4).build(), getDefaultFarmingProductCategories()));
        categories.add(createDefaultCategory(Material.DIAMOND_PICKAXE, "&bMining", ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 9).build(), getDefaultMiningProductCategories()));
        categories.add(createDefaultCategory(Material.IRON_SWORD, "&cCombat", ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 14).build(), getDefaultCombatProductCategories()));
        categories.add(createDefaultCategory(Material.FISHING_ROD, "&6Woods & Fishes", ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 1).build(), getDefaultWoodsFishesProductCategories()));
        categories.add(createDefaultCategory(Material.ENCHANTMENT_TABLE, "&dOddities", ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 6).build(), getDefaultOdditiesProductCategories()));

        addDefault("categories", categories);
    }

    private CategoryConfiguration createDefaultCategory(Material icon, String name, ItemStack glass, List<ProductCategoryConfiguration> productCategories) {
        String colorizedName = Utils.colorize(name);
        return new CategoryConfiguration(getDefaultMenuConfiguration(colorizedName, glass),
                ItemBuilder.newBuilder(icon)
                        .withName(colorizedName)
                        .appendLore(ChatColor.DARK_GRAY + "Category", "", ChatColor.YELLOW + "Click to view!")
                        .build(),
                name,
                productCategories);
    }

    private CategoryMenuConfiguration getDefaultMenuConfiguration(String categoryName, ItemStack glass) {
        return CategoryMenuConfiguration.createDefaultConfiguration(ChatColor.getLastColors(categoryName) + "Bazaar ➜ " + categoryName, glass);
    }

    private List<ProductCategoryConfiguration> getDefaultFarmingProductCategories() {
        return Arrays.asList(
                getFarmingProductCategoryConfiguration(Material.WHEAT, "Wheat & Seeds"),
                getFarmingProductCategoryConfiguration(Material.CARROT_ITEM, "Carrot"),
                getFarmingProductCategoryConfiguration(Material.POTATO_ITEM, "Potato"),
                getFarmingProductCategoryConfiguration(Material.PUMPKIN, "Pumpkin"),
                getFarmingProductCategoryConfiguration(Material.MELON, "Melon"),
                getFarmingProductCategoryConfiguration(Material.RED_MUSHROOM, "Mushrooms"),
                getFarmingProductCategoryConfiguration(new ItemStack(Material.INK_SACK, 1, (short) 3), "Cocoa Beans"),
                getFarmingProductCategoryConfiguration(Material.CACTUS, "Cactus"),
                getFarmingProductCategoryConfiguration(Material.SUGAR_CANE, "Sugar Cane"),
                getFarmingProductCategoryConfiguration(Material.LEATHER, "Leather & Beef"),
                getFarmingProductCategoryConfiguration(Material.PORK, "Pork"),
                getFarmingProductCategoryConfiguration(Material.RAW_CHICKEN, "Chicken & Feather"),
                getFarmingProductCategoryConfiguration(Material.MUTTON, "Mutton & Wool"),
                getFarmingProductCategoryConfiguration(Material.NETHER_STALK, "Nether Warts")
        );
    }

    private ProductCategoryConfiguration getFarmingProductCategoryConfiguration(Material icon, String name) {
        return getFarmingProductCategoryConfiguration(new ItemStack(icon), name);
    }

    private ProductCategoryConfiguration getFarmingProductCategoryConfiguration(ItemStack icon, String name) {
        return new ProductCategoryConfiguration(getDefaultProductCategoryIcon(icon, ChatColor.YELLOW + name),
                name,
                new ArrayList<>());
    }

    private List<ProductCategoryConfiguration> getDefaultMiningProductCategories() {
        return Arrays.asList(
                getMiningProductCategoryConfiguration(Material.COBBLESTONE, "Cobblestone"),
                getMiningProductCategoryConfiguration(Material.COAL, "Coal"),
                getMiningProductCategoryConfiguration(Material.IRON_INGOT, "Iron"),
                getMiningProductCategoryConfiguration(Material.GOLD_INGOT, "Gold"),
                getMiningProductCategoryConfiguration(Material.DIAMOND, "Diamond"),
                getMiningProductCategoryConfiguration(new ItemStack(Material.INK_SACK, 1, (short) 4), "Lapis"),
                getMiningProductCategoryConfiguration(Material.EMERALD, "Emerald"),
                getMiningProductCategoryConfiguration(Material.REDSTONE, "Redstone"),
                getMiningProductCategoryConfiguration(Material.QUARTZ, "Quarts"),
                getMiningProductCategoryConfiguration(Material.OBSIDIAN, "Obsidian"),
                getMiningProductCategoryConfiguration(Material.GLOWSTONE_DUST, "Glowstone"),
                getMiningProductCategoryConfiguration(Material.FLINT, "Flint & Gravel")
        );
    }

    private ProductCategoryConfiguration getMiningProductCategoryConfiguration(Material icon, String name) {
        return getMiningProductCategoryConfiguration(new ItemStack(icon), name);
    }

    private ProductCategoryConfiguration getMiningProductCategoryConfiguration(ItemStack icon, String name) {
        return new ProductCategoryConfiguration(getDefaultProductCategoryIcon(icon, ChatColor.AQUA + name),
                name,
                new ArrayList<>());
    }

    private List<ProductCategoryConfiguration> getDefaultCombatProductCategories() {
        return Arrays.asList(
                getCombatProductCategoryConfiguration(Material.ROTTEN_FLESH, "Rotten Flesh"),
                getCombatProductCategoryConfiguration(Material.BONE, "Bone"),
                getCombatProductCategoryConfiguration(Material.STRING, "String"),
                getCombatProductCategoryConfiguration(Material.SULPHUR, "Gunpowder"),
                getCombatProductCategoryConfiguration(Material.ENDER_PEARL, "Ender Pearl"),
                getCombatProductCategoryConfiguration(Material.GHAST_TEAR, "Ghast Tear"),
                getCombatProductCategoryConfiguration(Material.SLIME_BALL, "Slime Drops"),
                getCombatProductCategoryConfiguration(Material.BLAZE_ROD, "Blaze Rod")
        );
    }

    private ProductCategoryConfiguration getCombatProductCategoryConfiguration(Material icon, String name) {
        return getCombatProductCategoryConfiguration(new ItemStack(icon), name);
    }

    private ProductCategoryConfiguration getCombatProductCategoryConfiguration(ItemStack icon, String name) {
        return new ProductCategoryConfiguration(getDefaultProductCategoryIcon(icon, ChatColor.RED + name),
                name,
                new ArrayList<>());
    }

    private List<ProductCategoryConfiguration> getDefaultWoodsFishesProductCategories() {
        return Arrays.asList(
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.LOG), "Oak"),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.LOG, 1, (short) 1), "Spruce"),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.LOG, 1, (short) 2), "Birch"),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.LOG_2, 1, (short) 1), "Dark Oak"),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.LOG_2), "Acacia"),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.LOG, 1, (short) 3), "Jungle"),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.RAW_FISH), "Raw Fish"),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.RAW_FISH, 1, (short) 1), "Salmon"),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.RAW_FISH, 1, (short) 2), "Clownfish"),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.RAW_FISH, 1, (short) 3), "Pufferfish"),
                getWoodsFishesProductCategoryConfiguration(Material.PRISMARINE_SHARD, "Prismarine"),
                getWoodsFishesProductCategoryConfiguration(Material.CLAY, "Clay")
        );
    }

    private ProductCategoryConfiguration getWoodsFishesProductCategoryConfiguration(Material icon, String name) {
        return getWoodsFishesProductCategoryConfiguration(new ItemStack(icon), name);
    }

    private ProductCategoryConfiguration getWoodsFishesProductCategoryConfiguration(ItemStack icon, String name) {
        return new ProductCategoryConfiguration(getDefaultProductCategoryIcon(icon, ChatColor.GOLD + name),
                name,
                new ArrayList<>());
    }

    private List<ProductCategoryConfiguration> getDefaultOdditiesProductCategories() {
        return Arrays.asList(
                getOdditiesProductCategoryConfiguration(Material.EXP_BOTTLE, "EXP Bottles")
        );
    }

    private ProductCategoryConfiguration getOdditiesProductCategoryConfiguration(Material icon, String name) {
        return getOdditiesProductCategoryConfiguration(new ItemStack(icon), name);
    }

    private ProductCategoryConfiguration getOdditiesProductCategoryConfiguration(ItemStack icon, String name) {
        return new ProductCategoryConfiguration(getDefaultProductCategoryIcon(icon, ChatColor.LIGHT_PURPLE + name),
                name,
                new ArrayList<>());
    }

    private ItemStack getDefaultProductCategoryIcon(ItemStack icon, String name) {
        return ItemBuilder.newBuilder(icon)
                .withName(name)
                .appendLore("%product-lore%")
                .appendLore("")
                .appendLore(ChatColor.YELLOW + "Click to view products!")
                .build();
    }

    public List<CategoryConfiguration> getCategories() {
        return (List<CategoryConfiguration>) getConfig().getList("categories", new ArrayList<>());
    }
}
