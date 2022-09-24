package io.github.apdevteam.aptweaks.tweaks;

import io.github.apdevteam.aptweaks.Utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
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
        if (result != null && result.getType() != Material.AIR) {
            Bukkit.getLogger().info("Result: " + result.getType());
            return;
        }

        ItemStack[] contents = event.getInventory().getContents();
        if (contents.length != 2) {
            Bukkit.getLogger().info("Contents: " + contents.length);
            return; // Only allow two items in the anvil
        }

        ItemStack one = contents[0];
        ItemStack two = contents[1];
        if (one == null || two == null) {
            Bukkit.getLogger().info("One: " + one + " Two: " + two);
            return; // Both items must be present
        }
        if (!Utils.hasEnchantment(one, Enchantment.ARROW_INFINITE)
                && !Utils.hasEnchantment(two, Enchantment.ARROW_INFINITE)) {
            Bukkit.getLogger().info("One: " + Utils.hasEnchantment(one, Enchantment.ARROW_INFINITE) + " Two: " + Utils.hasEnchantment(two, Enchantment.ARROW_INFINITE));
            return; // Require one of the items to have the infinity enchantment
        }
        if (!Utils.hasEnchantment(one, Enchantment.MENDING)
                && !Utils.hasEnchantment(two, Enchantment.MENDING)) {
            Bukkit.getLogger().info("One: " + Utils.hasEnchantment(one, Enchantment.MENDING) + " Two: " + Utils.hasEnchantment(two, Enchantment.MENDING));
            return; // Require one of the items to have the mending enchantment
        }
        if (one.getType() != Material.ENCHANTED_BOOK && one.getType() != Material.BOW) {
            Bukkit.getLogger().info("One: " + one.getType());
            return; // Only allow enchanted books or bows in the first slot of the anvil
        }
        if (two.getType() != Material.ENCHANTED_BOOK) {
            Bukkit.getLogger().info("Two: " + two.getType());
            return; // Only allow enchanted books in the second slot of the anvil
            // TODO: Expand this to be more flexible
        }

        Map<Enchantment, Integer> resultEnchants = Utils.combine(Utils.getEnchantments(one), Utils.getEnchantments(two));        
        result = new ItemStack(one);
        for (Map.Entry<Enchantment, Integer> entry : resultEnchants.entrySet()) {
            Utils.setEnchantment(result, entry.getKey(), entry.getValue());
        }
        event.setResult(result);
        event.getInventory().setRepairCost(1);
        Bukkit.getLogger().info(
            "Result: " + event.getResult().getType()
            + " Repair Cost: " + event.getInventory().getRepairCost()
            + " Enchantments: " + Utils.getEnchantments(event.getResult()).size()
        );
    }
}
