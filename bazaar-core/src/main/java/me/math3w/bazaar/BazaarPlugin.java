package me.math3w.bazaar;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.bazaar.BazaarImpl;
import me.math3w.bazaar.bazaar.category.CategoryConfiguration;
import me.math3w.bazaar.bazaar.product.ProductConfiguration;
import me.math3w.bazaar.bazaar.productcategory.ProductCategoryConfiguration;
import me.math3w.bazaar.commands.BazaarCommand;
import me.math3w.bazaar.commands.EditCommand;
import me.math3w.bazaar.config.BazaarConfig;
import me.math3w.bazaar.config.MessagesConfig;
import me.zort.containr.Containr;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class BazaarPlugin extends JavaPlugin implements BazaarAPI {
    private MessagesConfig messagesConfig;
    private BazaarConfig bazaarConfig;
    private Bazaar bazaar;

    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(ProductConfiguration.class);
        ConfigurationSerialization.registerClass(ProductCategoryConfiguration.class);
        ConfigurationSerialization.registerClass(CategoryConfiguration.class);
    }

    @Override
    public void onEnable() {
        Containr.init(this);

        messagesConfig = new MessagesConfig(this);
        bazaarConfig = new BazaarConfig(this);

        getCommand("bazaar").setExecutor(new BazaarCommand(this));
        getCommand("bazaaredit").setExecutor(new EditCommand());

        bazaar = new BazaarImpl(this);
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public BazaarConfig getBazaarConfig() {
        return bazaarConfig;
    }

    @Override
    public Bazaar getBazaar() {
        return bazaar;
    }
}
