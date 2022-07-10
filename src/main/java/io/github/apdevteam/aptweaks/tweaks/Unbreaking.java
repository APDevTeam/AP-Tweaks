package io.github.apdevteam.aptweaks.tweaks;

import io.github.apdevteam.aptweaks.Utils;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Unbreaking implements Listener {
    public static final int MAX_VANILLA_LEVEL = 3;
    public static final int MAX_LEVEL = 10;

    @EventHandler
    public void onAnvilEvent(@NotNull PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        if (result == null || result.getItemMeta() == null || !Utils.hasEnchantment(result, Enchantment.DURABILITY))
            return;
        if (Utils.getEnchantment(result, Enchantment.DURABILITY) < MAX_VANILLA_LEVEL)
            return; // We only need to correct when the result is the max vanilla level or higher

        int max = -1;
        int maxCount = -1;
        for (ItemStack item : event.getInventory().getContents()) {
            if (item == null || item.getItemMeta() == null || !Utils.hasEnchantment(item, Enchantment.DURABILITY))
                continue;
            int unbreakingLevel = Utils.getEnchantment(item, Enchantment.DURABILITY);
            if (unbreakingLevel > max) {
                max = unbreakingLevel;
                maxCount = 1;
            }
            else if (unbreakingLevel == max) {
                maxCount++;
            }
        }
        if (max == -1 || max <= MAX_VANILLA_LEVEL)
            return; // We were unable to detect a source item with unbreaking or only detected vanilla levels
        if (maxCount > 1) {
            max += 1;
            if (max > MAX_LEVEL)
                max = MAX_LEVEL;
        }

        // Set result level
        Utils.setEnchantment(result, Enchantment.DURABILITY, max);
        event.setResult(result);
    }
}
