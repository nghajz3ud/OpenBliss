package com.bliss.core;

import com.bliss.items.BlissCoreItems;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public final class MaceRestrictionsListener implements Listener {
    @EventHandler
    public void onEnchantItem(EnchantItemEvent var1) {
        Material var7 = var1.getItem().getType();
        Material var3 = Material.MACE;
        if (var7 == var3) {
            boolean var4 = BlissCoreItems.isMegaMace(var1.getItem(), 2030001584);
            Map<Enchantment, Integer> var5 = var1.getEnchantsToAdd();
            Set<Entry<Enchantment, Integer>> var14 = var5.entrySet();
            Predicate<Entry<Enchantment, Integer>> var20 = var1x -> {
                boolean var7x;
                if (!var4 && isWindBurst(var1x.getKey(), 283952539)) {
                    var7x = false;
                } else {
                    var7x = true;
                }

                return var7x;
            };
            var14.removeIf(var20);
            if (var5.isEmpty()) {
                var1.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent var1) {
        ItemStack var6 = var1.getInventory().getFirstItem();
        if (var6 != null) {
            Material var16 = var6.getType();
            Material var3 = Material.MACE;
            if (var16 == var3) {
                ItemStack var7 = var1.getResult();
                if (var7 == null) {
                    return;
                }

                boolean var8 = BlissCoreItems.isMegaMace(var6, 2030001584);
                Map<Enchantment, Integer> var9 = var6.getEnchantments();

                for (Entry<Enchantment, Integer> var11 : var7.getEnchantments().entrySet()) {
                    if (var8 || !isWindBurst(var11.getKey(), 283952539)) {
                        int var41 = var11.getValue();
                        Enchantment var48 = var11.getKey();
                        Integer var49 = 0;
                        int var46 = var9.getOrDefault(var48, var49);
                        if (var41 > var46) {
                            var1.setResult(null);
                            return;
                        }
                    }
                }

                return;
            }
        }
    }

    static boolean isWindBurst(Enchantment var0, int var1) {
        if (var0 == null) {
            return false;
        } else {
            NamespacedKey var4 = var0.getKey();
            if (var4 == null) {
                return false;
            } else {
                String var9 = "wind_burst";
                String var15 = var4.getKey();
                if (!var9.equalsIgnoreCase(var15)) {
                    String var11 = "minecraft:wind_burst";
                    String var17 = var4.toString();
                    if (!var11.equalsIgnoreCase(var17)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private static String describe(Set<Enchantment> var0) {
        String var2 = "[";
        StringBuilder var1 = new StringBuilder(var2);
        StringBuilder var3 = var1;
        boolean var4 = true;

        for (Enchantment var6 : var0) {
            if (!var4) {
                var3.append(", ");
            }

            NamespacedKey var27 = var6.getKey();
            var3.append(var27);
            var4 = false;
        }

        return var3.append("]").toString();
    }
}
