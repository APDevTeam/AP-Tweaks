package io.github.apdevteam.aptweaks;

import io.github.apdevteam.aptweaks.tweaks.InfinityMending;
import io.github.apdevteam.aptweaks.tweaks.Unbreaking;
import org.bukkit.plugin.java.JavaPlugin;

public final class APTweaks extends JavaPlugin {

    @Override
    public void onEnable() {
        // getServer().getPluginManager().registerEvents(new InfinityMending(), this); // TODO
        getServer().getPluginManager().registerEvents(new Unbreaking(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
