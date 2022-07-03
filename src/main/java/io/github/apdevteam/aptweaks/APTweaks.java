package io.github.apdevteam.aptweaks;

import io.github.apdevteam.aptweaks.tweaks.UnbreakingV;
import org.bukkit.plugin.java.JavaPlugin;

public final class APTweaks extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new UnbreakingV(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
