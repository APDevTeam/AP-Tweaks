package io.github.apdevteam.aptweaks;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Utils {
    public static boolean hasEnchantment(@Nullable ItemStack item, @NotNull Enchantment enchantment) {
        if (item == null || item.getItemMeta() == null)
            return false;
        if (item.containsEnchantment(enchantment))
            return true; // Simple enchantment

        // Enchanted books have a special type of metadata for their enchantments
        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof EnchantmentStorageMeta))
            return false;

        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) meta;
        return enchantmentStorageMeta.hasStoredEnchant(enchantment);
    }

    public static int getEnchantment(@Nullable ItemStack item, @NotNull Enchantment enchantment) {
        if (item == null || item.getItemMeta() == null)
            return 0;
        if (item.containsEnchantment(enchantment))
            return item.getEnchantmentLevel(enchantment); // Simple enchantment

        // Enchanted books have a special type of metadata for their enchantments
        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof EnchantmentStorageMeta))
            return 0;

        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) meta;
        return enchantmentStorageMeta.getStoredEnchantLevel(enchantment);
    }

    public static void setEnchantment(@Nullable ItemStack item, @NotNull Enchantment enchantment, int level) {
        if (item == null || item.getItemMeta() == null)
            return;
        if (item.containsEnchantment(enchantment)) {
            // Simple enchantment
            item.removeEnchantment(enchantment);
            item.addUnsafeEnchantment(enchantment, level);
            return;
        }

        // Enchanted books have a special type of metadata for their enchantments
        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof EnchantmentStorageMeta)) {
            item.addUnsafeEnchantment(enchantment, level);
            return;
        }

        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) meta;
        if (enchantmentStorageMeta.hasStoredEnchant(enchantment))
            enchantmentStorageMeta.removeStoredEnchant(enchantment);
        enchantmentStorageMeta.addStoredEnchant(enchantment, level, true);
        item.setItemMeta(enchantmentStorageMeta);
    }
}
