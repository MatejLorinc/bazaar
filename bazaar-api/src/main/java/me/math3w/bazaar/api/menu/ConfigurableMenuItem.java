package me.math3w.bazaar.api.menu;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.bazaar.Bazaar;
import me.zort.containr.ContainerComponent;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ConfigurableMenuItem extends ConfigurationSerializable {
    int getSlot();

    void setSlot(Bazaar bazaar, int slot);

    ItemStack getItem();

    void setItem(Bazaar bazaar, ItemStack item);

    String getAction();

    void setAction(Bazaar bazaar, String action);

    void putItem(ContainerComponent containerComponent, BazaarAPI bazaarApi, Player player, MenuInfo info, boolean editMenu);
}
