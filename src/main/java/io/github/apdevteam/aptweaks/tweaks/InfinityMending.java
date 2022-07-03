package io.github.apdevteam.aptweaks.tweaks;

import io.github.apdevteam.aptweaks.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InfinityMending implements Listener {
    @EventHandler
    public void onAnvilEvent(@NotNull PrepareAnvilEvent event) {
        // This isn't yet functional since vanilla prevents the merging and we'd have to do it manually.
        ItemStack result = event.getResult();
        if (result == null || result.getItemMeta() == null)
            return;
        if (result.getType() != Material.ENCHANTED_BOOK && result.getType() != Material.BOW)
            return; // Result is neither an enchanted book nor a bow
        if (!Utils.hasEnchantment(result, Enchantment.MENDING) && !Utils.hasEnchantment(result, Enchantment.ARROW_INFINITE))
            return; // Result has neither mending nor infinity

        int mending = -1;
        int infinity = -1;
        for (ItemStack item : event.getInventory().getContents()) {
            if (item == null || item.getItemMeta() == null)
                continue;
            if (Utils.hasEnchantment(item, Enchantment.MENDING)) {
                int mendingLevel = Utils.getEnchantment(item, Enchantment.MENDING);
                if (mendingLevel > mending)
                    mending = mendingLevel;
            }
            if (Utils.hasEnchantment(item, Enchantment.ARROW_INFINITE)) {
                int infinityLevel = Utils.getEnchantment(item, Enchantment.ARROW_INFINITE);
                if (infinityLevel > infinity)
                    infinity = infinityLevel;
            }
        }
        if (mending == -1 || infinity == -1)
            return; // We were unable to detect a source items with mending and infinity

        Utils.setEnchantment(result, Enchantment.MENDING, mending);
        Utils.setEnchantment(result, Enchantment.ARROW_INFINITE, infinity);
    }
}
