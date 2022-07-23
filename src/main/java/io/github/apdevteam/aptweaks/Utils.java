package io.github.apdevteam.aptweaks;

import java.util.HashMap;
import java.util.Map;

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

    public static Map<Enchantment, Integer> getEnchantments(@Nullable ItemStack item) {
        if (item == null || item.getItemMeta() == null)
            return new HashMap<>();
        if (!item.getEnchantments().isEmpty())
            return item.getEnchantments(); // Simple enchantments
        if (!(item.getItemMeta() instanceof EnchantmentStorageMeta))
            return new HashMap<>(); // No enchantments

        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) item.getItemMeta();
        return enchantmentStorageMeta.getStoredEnchants();
    }

    public static Map<Enchantment, Integer> combine(Map<Enchantment, Integer> one, Map<Enchantment, Integer> two) {
        Map<Enchantment, Integer> result = new HashMap<>(one);
        for (Map.Entry<Enchantment, Integer> entry : two.entrySet()) {
            Enchantment enchant = entry.getKey();
            if (result.containsKey(enchant)) {
                result.put(enchant, combine(enchant, result.get(enchant), entry.getValue()));
                continue;
            }

            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static int combine(Enchantment enchant, int one, int two) {
        if (one == enchant.getMaxLevel() || two == enchant.getMaxLevel())
            return enchant.getMaxLevel(); // Already at max level

        if (one == two)
            return one + 1;

        return Math.max(one, two);
    }
}
