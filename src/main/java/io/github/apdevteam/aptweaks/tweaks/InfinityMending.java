package io.github.apdevteam.aptweaks.tweaks;

import io.github.apdevteam.aptweaks.Utils;

import java.util.HashMap;
import java.util.Map;

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
        ItemStack result = event.getResult();
        if (result != null && result.getType() != Material.AIR)
            return;

        ItemStack[] contents = event.getInventory().getContents();
        if (contents.length != 2)
            return; // Only allow two items in the anvil

        ItemStack one = contents[0];
        ItemStack two = contents[1];
        if (one == null || two == null)
            return; // Both items must be present
        if (!Utils.hasEnchantment(one, Enchantment.ARROW_INFINITE)
                && !Utils.hasEnchantment(two, Enchantment.ARROW_INFINITE))
            return; // Require one of the items to have the infinity enchantment
        if (!Utils.hasEnchantment(one, Enchantment.MENDING)
                && !Utils.hasEnchantment(two, Enchantment.MENDING))
            return; // Require one of the items to have the mending enchantment
        if (one.getType() != Material.ENCHANTED_BOOK && one.getType() != Material.BOW)
            return; // Only allow enchanted books or bows in the first slot of the anvil
        if (two.getType() != Material.ENCHANTED_BOOK)
            return; // Only allow enchanted books in the second slot of the anvil

        Map<Enchantment, Integer> resultEnchants = combine(Utils.getEnchantments(one), Utils.getEnchantments(two));        
        result = new ItemStack(one);
        for (Map.Entry<Enchantment, Integer> entry : resultEnchants.entrySet()) {
            result.addUnsafeEnchantment(entry.getKey(), entry.getValue());
        }
        event.setResult(result);
    }

    private Map<Enchantment, Integer> combine(Map<Enchantment, Integer> one, Map<Enchantment, Integer> two) {
        Map<Enchantment, Integer> result = new HashMap<>(one);
        for (Map.Entry<Enchantment, Integer> entry : two.entrySet()) {
            if (result.containsKey(entry.getKey()))
                continue; // Already have this enchantment

            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
