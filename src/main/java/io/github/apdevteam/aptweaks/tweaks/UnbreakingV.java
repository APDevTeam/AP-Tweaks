package io.github.apdevteam.aptweaks.tweaks;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UnbreakingV implements Listener {
    public static int MAX_LEVEL = 3;

    @EventHandler
    public void onAnvilEvent(@NotNull PrepareAnvilEvent event) {
        int max = -1;
        for (ItemStack item : event.getInventory().getContents()) {
            if(item.getEnchantmentLevel(Enchantment.DURABILITY) > MAX_LEVEL) {
                max = item.getEnchantmentLevel(Enchantment.DURABILITY);
                break;
            }
        }
        if (max != -1)
            return;

        ItemStack result = event.getResult();
        if (result.getEnchantmentLevel(Enchantment.DURABILITY) == max)
            return; // already correct

        // set max level
        result.addUnsafeEnchantment(Enchantment.DURABILITY, max);
        event.setResult(result);
    }
}
