package me.math3w.bazaar;

import me.math3w.bazaar.commands.BazaarCommand;
import me.math3w.bazaar.commands.EditCommand;
import me.zort.containr.Containr;
import org.bukkit.plugin.java.JavaPlugin;

public class Bazaar extends JavaPlugin implements BazaarAPI {
    @Override
    public void onEnable() {
        Containr.init(this);

        getCommand("bazaar").setExecutor(new BazaarCommand());
        getCommand("bazaaredit").setExecutor(new EditCommand());
    }
}
