package io.github.apdevteam.aptweaks.tweaks;

import io.github.apdevteam.aptweaks.Utils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UnbreakingV implements Listener {
    public static int MAX_VANILLA_LEVEL = 3;

    @EventHandler
    public void onAnvilEvent(@NotNull PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        if (result == null || result.getItemMeta() == null || !Utils.hasEnchantment(result, Enchantment.DURABILITY))
            return;
        if (Utils.enchantmentLevel(result, Enchantment.DURABILITY) != MAX_VANILLA_LEVEL)
            return; // We only need to correct when the result is the max vanilla level

        int max = -1;
        for (ItemStack item : event.getInventory().getContents()) {
            if (item == null || item.getItemMeta() == null || !Utils.hasEnchantment(item, Enchantment.DURABILITY))
                continue;
            int unbreakingLevel = Utils.enchantmentLevel(item, Enchantment.DURABILITY);
            if (unbreakingLevel > max)
                max = unbreakingLevel;
        }
        if (max == -1 || max <= MAX_VANILLA_LEVEL)
            return; // We were unable to detect a source item with unbreaking or only detected vanilla levels

        // Set result level
        Utils.setEnchantment(result, Enchantment.DURABILITY, max);
        event.setResult(result);
    }
}
