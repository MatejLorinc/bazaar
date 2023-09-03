package me.math3w.bazaar.edit;

import com.cryptomorin.xseries.XMaterial;
import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.menu.ConfigurableMenuItem;
import me.math3w.bazaar.api.menu.MenuHistory;
import me.math3w.bazaar.messageinput.MessageInputManager;
import me.zort.containr.Component;
import me.zort.containr.Element;
import me.zort.containr.GUI;
import me.zort.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EditMenuBuilder {
    private String title;
    private int rows = 5;
    private Map<Integer, Element> elements = new HashMap<>();
    private ItemStack fillerItem = ItemBuilder.newBuilder(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).withName(ChatColor.WHITE.toString()).build();
    private Consumer<Player> updateMenuPlayerConsumer;

    public EditMenuBuilder title(String title) {
        this.title = title;
        return this;
    }

    public EditMenuBuilder rows(int rows) {
        this.rows = rows;
        return this;
    }

    public EditMenuBuilder updateMenuPlayerConsumer(Consumer<Player> updateMenuPlayerConsumer) {
        this.updateMenuPlayerConsumer = updateMenuPlayerConsumer;
        return this;
    }


    public EditMenuBuilder addElement(int slot, Element element) {
        this.elements.put(slot, element);
        return this;
    }

    public EditMenuBuilder addPreviewElement(int slot, ItemStack item, Consumer<ItemStack> newItemConsumer) {
        return addElement(slot, Component.element()
                .item(ItemBuilder.newBuilder(item)
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Place new icon here!")
                        .build())
                .click(clickInfo -> {
                    ItemStack cursor = clickInfo.getCursor();
                    if (cursor == null || cursor.getType() == Material.AIR) {
                        clickInfo.getPlayer().sendMessage(ChatColor.RED + "You need to hold the item in the cursor to replace the icon");
                        return;
                    }

                    ItemMeta cursorMeta = cursor.getItemMeta();
                    if (cursorMeta.getDisplayName() == null) {
                        cursorMeta.setDisplayName(item.getItemMeta().getDisplayName());
                    }
                    if (cursorMeta.getLore() == null || cursorMeta.getLore().isEmpty()) {
                        cursorMeta.setLore(item.getItemMeta().getLore());
                    }
                    cursor.setItemMeta(cursorMeta);

                    newItemConsumer.accept(cursor);
                    clickInfo.getPlayer().setItemOnCursor(null);
                    updateMenuPlayerConsumer.accept(clickInfo.getPlayer());
                }).build());
    }

    public EditMenuBuilder addNameEditElement(MessageInputManager messageInputManager, int slot, String name, Consumer<String> newNameConsumer) {
        return addElement(slot, Component.element()
                .item(ItemBuilder.newBuilder(Material.NAME_TAG)
                        .withName(ChatColor.GREEN + "Edit Name")
                        .appendLore()
                        .appendLore(ChatColor.GRAY + "Current: " + ChatColor.RESET + name)
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to edit name!")
                        .appendLore(ChatColor.AQUA + "Right-Click to clear!")
                        .build())
                .click(clickInfo -> {
                    Player player = clickInfo.getPlayer();
                    Consumer<String> updateNameConsumer = newNameConsumer.andThen(newName -> updateMenuPlayerConsumer.accept(player));

                    if (clickInfo.getClickType().isRightClick()) {
                        updateNameConsumer.accept("");
                        return;
                    }

                    player.closeInventory();
                    player.sendMessage(ChatColor.GRAY + "Enter new item name:");
                    messageInputManager.requirePlayerMessageInput(player, updateNameConsumer);
                }).build());
    }

    public EditMenuBuilder addLoreEditElement(MessageInputManager messageInputManager, int slot, List<String> lore, Consumer<List<String>> newLoreConsumer) {
        return addElement(slot, Component.element()
                .item(ItemBuilder.newBuilder(XMaterial.OAK_SIGN.parseMaterial())
                        .withName(ChatColor.GREEN + "Edit Lore")
                        .appendLore(ChatColor.GRAY + "Current: ")
                        .appendLore(lore == null ? new ArrayList<>() : lore)
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to edit lore!")
                        .appendLore(ChatColor.AQUA + "Right-Click to clear!")
                        .build())
                .click(clickInfo -> {
                    Player player = clickInfo.getPlayer();
                    Consumer<List<String>> updateLoreConsumer = newLoreConsumer.andThen(newName -> updateMenuPlayerConsumer.accept(player));

                    if (clickInfo.getClickType().isRightClick()) {
                        updateLoreConsumer.accept(new ArrayList<>());
                        return;
                    }

                    player.closeInventory();
                    messageInputManager.requirePlayerMessageInputMultiLine(player, lore, updateLoreConsumer);
                }).build());
    }

    public EditMenuBuilder addActionEditElement(int slot, ConfigurableMenuItem configurableMenuItem, Bazaar bazaar) {
        return addElement(slot, Component.element()
                .item(ItemBuilder.newBuilder(Material.REDSTONE)
                        .withName(ChatColor.GREEN + "Edit Action")
                        .appendLore(ChatColor.GRAY + "Current: " + ChatColor.GREEN + configurableMenuItem.getAction())
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to edit click action!")
                        .build())
                .click(clickInfo -> {
                    Component.gui()
                            .title("Select Action")
                            .rows(2)
                            .prepare((gui, player) -> {
                                for (String action : bazaar.getBazaarApi().getClickActionManager().getActions()) {
                                    gui.appendElement(Component.element(ItemBuilder.newBuilder(XMaterial.MAP.parseMaterial())
                                                    .withName(ChatColor.GREEN + (action.isEmpty() ? "none" : action))
                                                    .appendLore("")
                                                    .appendLore(ChatColor.YELLOW + "Click to set this action to item!").build())
                                            .click(actionClickInfo -> {
                                                configurableMenuItem.setAction(bazaar, action);
                                                updateMenuPlayerConsumer.accept(player);
                                            })
                                            .build());
                                }
                            }).build().open(clickInfo.getPlayer());
                }).build());
    }

    public EditMenuBuilder addBackElementElement(int slot, MenuHistory menuHistory) {
        //FIXME Because menu history stores GUI the old menu is loaded even if something change
        return addElement(slot, Component.element()
                .item(ItemBuilder.newBuilder(Material.ARROW)
                        .withName(ChatColor.GREEN + "Go Back")
                        .build())
                .click(clickInfo -> menuHistory.openPrevious(clickInfo.getPlayer())).build());
    }

    public GUI build() {
        return Component.gui()
                .title(title)
                .rows(rows)
                .prepare(gui -> {
                    for (Map.Entry<Integer, Element> elementEntry : elements.entrySet()) {
                        gui.setElement(elementEntry.getKey(), elementEntry.getValue());
                    }

                    gui.fillElement(Component.element(fillerItem).build());
                })
                .build();
    }
}
