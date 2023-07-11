package me.math3w.bazaar;

import me.zort.containr.Containr;
import org.bukkit.plugin.java.JavaPlugin;

public class Bazaar extends JavaPlugin implements BazaarAPI {
    @Override
    public void onEnable() {
        Containr.init(this);
    }
}
