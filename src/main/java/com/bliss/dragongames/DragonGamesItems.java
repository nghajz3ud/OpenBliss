package com.bliss.dragongames;

import com.bliss.villager.Text;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class DragonGamesItems {
    public static final NamespacedKey KEY_HEART = NamespacedKey.fromString("bliss:dg_heart");
    public static final NamespacedKey KEY_TRACKER = NamespacedKey.fromString("bliss:dg_tracker");
    public static final int HEART_CMD = 16;

    private DragonGamesItems() {
    }

    public static ItemStack extraHeart$1999829859(int var0) {
        Material var2 = Material.RED_DYE;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var5 = var1.getItemMeta();
        Component var17 = noItalic("&cExtra Heart", 1188798615);
        var5.displayName(var17);
        List var20 = List.of(noItalic("&aRight Click to consume", 1188798615));
        var5.lore(var20);
        Integer var22 = 16;
        var5.setCustomModelData(var22);
        PersistentDataContainer var12 = var5.getPersistentDataContainer();
        NamespacedKey var23 = KEY_HEART;
        PersistentDataType var25 = PersistentDataType.BYTE;
        Byte var26 = (byte)1;
        var12.set(var23, var25, var26);
        var1.setItemMeta(var5);
        return var1;
    }

    public static ItemStack extraHeart(int var0, int var1) {
        ItemStack var5 = extraHeart$1999829859(1128735326);
        int var8 = Math.max(1, var0);
        var5.setAmount(var8);
        return var5;
    }

    public static ItemStack trackingCompass(int var0) {
        Material var2 = Material.COMPASS;
        ItemStack var1 = new ItemStack(var2);
        CompassMeta var5 = (CompassMeta)var1.getItemMeta();
        Component var18 = noItalic("&bTracking Compass", 1188798615);
        var5.displayName(var18);
        List var21 = List.of(noItalic("&2Lasts for 1 hours", 1188798615));
        var5.lore(var21);
        var5.setLodestoneTracked(false);
        PersistentDataContainer var13 = var5.getPersistentDataContainer();
        NamespacedKey var23 = KEY_TRACKER;
        PersistentDataType var25 = PersistentDataType.BYTE;
        Byte var26 = (byte)1;
        var13.set(var23, var25, var26);
        var1.setItemMeta(var5);
        return var1;
    }

    public static boolean isExtraHeart(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var6 = var0.getType();
            Material var3 = Material.RED_DYE;
            if (var6 == var3 && var0.hasItemMeta()) {
                PersistentDataContainer var11 = var0.getItemMeta().getPersistentDataContainer();
                NamespacedKey var14 = KEY_HEART;
                PersistentDataType var4 = PersistentDataType.BYTE;
                return var11.has(var14, var4);
            }
        }

        return false;
    }

    public static boolean isTracker(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var6 = var0.getType();
            Material var3 = Material.COMPASS;
            if (var6 == var3 && var0.hasItemMeta()) {
                PersistentDataContainer var11 = var0.getItemMeta().getPersistentDataContainer();
                NamespacedKey var14 = KEY_TRACKER;
                PersistentDataType var4 = PersistentDataType.BYTE;
                return var11.has(var14, var4);
            }
        }

        return false;
    }

    static Component noItalic(String var0, int var1) {
        Component var5 = Text.legacy(var0, 373192400);
        TextDecoration var3 = TextDecoration.ITALIC;
        return var5.decoration(var3, false);
    }
}
