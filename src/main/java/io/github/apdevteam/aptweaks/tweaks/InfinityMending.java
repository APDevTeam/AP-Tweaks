package io.github.apdevteam.aptweaks.tweaks;

import io.github.apdevteam.aptweaks.Utils;

import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InfinityMending implements Listener {
    private final Logger logger;

    public InfinityMending(Logger logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onAnvilEvent(@NotNull PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        if (result != null && result.getType() != Material.AIR) {
            // logger.info("Result: " + result.getType());
            return;
        }

        ItemStack[] contents = event.getInventory().getContents();
        if (contents.length != 2) {
            // logger.info("Contents: " + contents.length);
            return; // Only allow two items in the anvil
        }

        ItemStack one = contents[0];
        ItemStack two = contents[1];
        if (one == null || two == null) {
            // logger.info("One: " + one + " Two: " + two);
            return; // Both items must be present
        }
        if (!Utils.hasEnchantment(one, Enchantment.INFINITY)
                && !Utils.hasEnchantment(two, Enchantment.INFINITY)) {
            // logger.info("One: " + Utils.hasEnchantment(one, Enchantment.INFINITY) + " Two: " + Utils.hasEnchantment(two, Enchantment.INFINITY));
            return; // Require one of the items to have the infinity enchantment
        }
        if (!Utils.hasEnchantment(one, Enchantment.MENDING)
                && !Utils.hasEnchantment(two, Enchantment.MENDING)) {
            // logger.info("One: " + Utils.hasEnchantment(one, Enchantment.MENDING) + " Two: " + Utils.hasEnchantment(two, Enchantment.MENDING));
            return; // Require one of the items to have the mending enchantment
        }
        if (one.getType() != Material.ENCHANTED_BOOK && one.getType() != Material.BOW) {
            // logger.info("One: " + one.getType());
            return; // Only allow enchanted books or bows in the first slot of the anvil
        }
        if (two.getType() != Material.ENCHANTED_BOOK) {
            // logger.info("Two: " + two.getType());
            return; // Only allow enchanted books in the second slot of the anvil
        }

        Map<Enchantment, Integer> resultEnchants = Utils.combine(Utils.getEnchantments(one), Utils.getEnchantments(two));        
        result = new ItemStack(one);
        for (Map.Entry<Enchantment, Integer> entry : resultEnchants.entrySet()) {
            Utils.setEnchantment(result, entry.getKey(), entry.getValue());
        }
        event.setResult(result);
        event.getInventory().setRepairCost(1);
        logger.info(
            "Infinity Mending Result: " + event.getResult().getType()
            + " Repair Cost: " + event.getInventory().getRepairCost()
            + " Enchantments: " + Utils.getEnchantments(event.getResult()).size()
        );
    }
}
