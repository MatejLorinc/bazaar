package me.math3w.bazaar.edit;

import com.cryptomorin.xseries.XMaterial;
import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.bazaar.Category;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.ProductCategory;
import me.math3w.bazaar.api.edit.EditManager;
import me.math3w.bazaar.api.menu.ConfigurableMenuItem;
import me.math3w.bazaar.messageinput.MessageInputManager;
import me.zort.containr.Component;
import me.zort.containr.ContextClickInfo;
import me.zort.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class DefaultEditManager implements EditManager {
    private final BazaarPlugin bazaarPlugin;

    public DefaultEditManager(BazaarPlugin bazaarPlugin) {
        this.bazaarPlugin = bazaarPlugin;
    }

    @Override
    public void openItemEdit(Player player, ConfigurableMenuItem configurableMenuItem) {
        Bazaar bazaar = bazaarPlugin.getBazaar();
        MessageInputManager messageInputManager = bazaarPlugin.getMessageInputManager();

        new EditMenuBuilder()
                .title("Edit Item")
                .updateMenuPlayerConsumer(player1 -> openItemEdit(player1, configurableMenuItem))
                .addPreviewElement(13, configurableMenuItem.getItem(), newItem -> configurableMenuItem.setItem(bazaar, newItem))
                .addNameEditElement(messageInputManager, 29, configurableMenuItem.getItem().getItemMeta().getDisplayName(), newName -> {
                    ItemStack newItem = ItemBuilder.newBuilder(configurableMenuItem.getItem()).withName(newName).build();
                    configurableMenuItem.setItem(bazaar, newItem);
                })
                .addActionEditElement(31, configurableMenuItem, bazaar)
                .addLoreEditElement(messageInputManager, 33, configurableMenuItem.getItem().getItemMeta().getLore(), newLore -> {
                    ItemStack newItem = ItemBuilder.newBuilder(configurableMenuItem.getItem()).withLore(newLore).build();
                    configurableMenuItem.setItem(bazaar, newItem);
                })
                .build()
                .open(player);
    }

    @Override
    public void openCategoryEdit(Player player, Category category) {
        MessageInputManager messageInputManager = bazaarPlugin.getMessageInputManager();

        new EditMenuBuilder()
                .title("Edit Category")
                .updateMenuPlayerConsumer(player1 -> openCategoryEdit(player1, category))
                .addPreviewElement(13, category.getIcon(), category::setIcon)
                .addNameEditElement(messageInputManager, 29, category.getIcon().getItemMeta().getDisplayName(), newName -> {
                    ItemStack newItem = ItemBuilder.newBuilder(category.getIcon()).withName(newName).build();
                    category.setIcon(newItem);
                })
                .addElement(31, Component.element()
                        .item(ItemBuilder.newBuilder(XMaterial.WRITABLE_BOOK.parseMaterial())
                                .withName(ChatColor.GREEN + "Edit Title")
                                .appendLore(ChatColor.GRAY + "Current: " + ChatColor.RESET + category.getMenu().getTitle())
                                .appendLore("")
                                .appendLore(ChatColor.YELLOW + "Click to edit menu title!")
                                .build())
                        .click(clickInfo -> {
                            player.closeInventory();
                            player.sendMessage(ChatColor.GRAY + "Enter new menu name:");
                            messageInputManager.requirePlayerMessageInput(player, newName -> {
                                category.setTitle(newName);
                                openCategoryEdit(player, category);
                            });
                        }).build())
                .addLoreEditElement(messageInputManager, 33, category.getIcon().getItemMeta().getLore(), newLore -> {
                    ItemStack newItem = ItemBuilder.newBuilder(category.getIcon()).withLore(newLore).build();
                    category.setIcon(newItem);
                })
                .build()
                .open(player);
    }

    @Override
    public void openProductCategoryEdit(Player player, ProductCategory productCategory) {
        MessageInputManager messageInputManager = bazaarPlugin.getMessageInputManager();

        new EditMenuBuilder()
                .title("Edit Product Category")
                .updateMenuPlayerConsumer(player1 -> openProductCategoryEdit(player1, productCategory))
                .addPreviewElement(13, productCategory.getRawIcon(), productCategory::setIcon)
                .addNameEditElement(messageInputManager, 30, productCategory.getRawIcon().getItemMeta().getDisplayName(), newName -> {
                    ItemStack newItem = ItemBuilder.newBuilder(productCategory.getRawIcon()).withName(newName).build();
                    productCategory.setIcon(newItem);
                    productCategory.setName(newName);
                })
                .addLoreEditElement(messageInputManager, 32, productCategory.getRawIcon().getItemMeta().getLore(), newLore -> {
                    ItemStack newItem = ItemBuilder.newBuilder(productCategory.getRawIcon()).withLore(newLore).build();
                    productCategory.setIcon(newItem);
                })
                .build()
                .open(player);
    }

    @Override
    public void openProductEdit(Player player, Product product) {
        MessageInputManager messageInputManager = bazaarPlugin.getMessageInputManager();

        new EditMenuBuilder()
                .title("Edit Product")
                .updateMenuPlayerConsumer(player1 -> openProductEdit(player1, product))
                .addPreviewElement(13, product.getRawIcon(), product::setIcon)
                .addNameEditElement(messageInputManager, 29, product.getRawIcon().getItemMeta().getDisplayName(), newName -> {
                    ItemStack newItem = ItemBuilder.newBuilder(product.getRawIcon()).withName(newName).build();
                    product.setIcon(newItem);
                    product.setName(newName);
                })
                .addElement(31, Component.element()
                        .item(ItemBuilder.newBuilder(product.getItem())
                                .appendLore("")
                                .appendLore(ChatColor.YELLOW + "Place new item here!")
                                .build())
                        .click(clickInfo -> {
                            ItemStack cursor = clickInfo.getCursor();
                            if (cursor == null || cursor.getType() == Material.AIR) {
                                player.sendMessage(ChatColor.RED + "You need to hold the item in the cursor to replace the icon");
                                return;
                            }

                            product.setItem(cursor);
                            player.setItemOnCursor(null);
                            openProductEdit(player, product);
                        }).build())
                .addLoreEditElement(messageInputManager, 33, product.getRawIcon().getItemMeta().getLore(), newLore -> {
                    ItemStack newItem = ItemBuilder.newBuilder(product.getRawIcon()).withLore(newLore).build();
                    product.setIcon(newItem);
                })
                .build()
                .open(player);
    }

    @Override
    public Consumer<ContextClickInfo> createEditableItemClickAction(Consumer<ContextClickInfo> defaultClickAction,
                                                                    Consumer<ContextClickInfo> defaultEditClickAction,
                                                                    Consumer<ContextClickInfo> editClickAction,
                                                                    Consumer<ContextClickInfo> removeClickAction,
                                                                    Consumer<ContextClickInfo> updateMenu,
                                                                    boolean editing) {
        return clickInfo -> {
            if (editing) {
                if (clickInfo.getClickType().isRightClick()) {
                    editClickAction.accept(clickInfo);
                    return;
                }

                if (clickInfo.getClickType() == ClickType.MIDDLE) {
                    removeClickAction.accept(clickInfo);
                    updateMenu.accept(clickInfo);
                    return;
                }


                defaultEditClickAction.accept(clickInfo);
                return;
            }

            defaultClickAction.accept(clickInfo);
        };
    }

    @Override
    public Consumer<ContextClickInfo> createEditableItemClickAction(Consumer<ContextClickInfo> defaultClickAction,
                                                                    Consumer<ContextClickInfo> defaultEditClickAction,
                                                                    Consumer<ContextClickInfo> editClickAction,
                                                                    boolean editing) {
        return createEditableItemClickAction(defaultClickAction, defaultEditClickAction, editClickAction, clickInfo -> {
        }, clickInfo -> {
        }, editing);
    }
}
