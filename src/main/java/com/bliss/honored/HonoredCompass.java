package com.bliss.honored;

import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class HonoredCompass {
    public static final NamespacedKey KEY = NamespacedKey.fromString("bliss:ho_compass");
    public static final String JUGGERNAUT = "juggernaut";
    public static final String PLAYER = "player";

    private HonoredCompass() {
    }

    public static ItemStack juggernaut(int var0) {
        String var1 = "juggernaut";
        String var2 = "<##ff5555>&lJuggernaut's Compass";
        List var6 = List.of("&7Points to the <##ffcc00>Honored One", "&7while the Assessment window is open.");
        return build(var1, var2, var6, 2013200776);
    }

    public static ItemStack player(int var0) {
        String var1 = "player";
        String var2 = "<##ffcc00>&lHonored One Compass";
        List var5 = List.of("&7Points to the <##ffcc00>Honored One&7.");
        return build(var1, var2, var5, 2013200776);
    }

    private static ItemStack build(String var0, String var1, List<String> var2, int var3) {
        Material var5 = Material.COMPASS;
        ItemStack var4 = new ItemStack(var5);
        CompassMeta var8 = (CompassMeta)var4.getItemMeta();
        Component var34 = Text.legacy(var1, 373192400);
        TextDecoration var43 = TextDecoration.ITALIC;
        Component var35 = var34.decoration(var43, false);
        var8.displayName(var35);
        ArrayList var16 = new ArrayList();
        ArrayList var9 = var16;

        for (String var11 : var2) {
            Component var41 = Text.legacy(var11, 373192400);
            TextDecoration var45 = TextDecoration.ITALIC;
            Component var42 = var41.decoration(var45, false);
            var9.add(var42);
        }

        var8.lore(var9);
        var8.setLodestoneTracked(false);
        PersistentDataContainer var24 = var8.getPersistentDataContainer();
        NamespacedKey var38 = KEY;
        PersistentDataType var44 = PersistentDataType.STRING;
        var24.set(var38, var44, var0);
        var4.setItemMeta(var8);
        return var4;
    }

    public static boolean isHoCompass(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var6 = var0.getType();
            Material var3 = Material.COMPASS;
            if (var6 == var3 && var0.hasItemMeta()) {
                PersistentDataContainer var11 = var0.getItemMeta().getPersistentDataContainer();
                NamespacedKey var14 = KEY;
                PersistentDataType var4 = PersistentDataType.STRING;
                return var11.has(var14, var4);
            }
        }

        return false;
    }

    public static String typeOf(ItemStack var0) {
        if (!isHoCompass(var0, 736733534)) {
            return null;
        } else {
            PersistentDataContainer var8 = var0.getItemMeta().getPersistentDataContainer();
            NamespacedKey var2 = KEY;
            PersistentDataType var3 = PersistentDataType.STRING;
            return (String)var8.get(var2, var3);
        }
    }

    public static boolean aim(ItemStack var0, Location var1, int var2) {
        if (!isHoCompass(var0, 736733534)) {
            return false;
        } else {
            CompassMeta var7 = (CompassMeta)var0.getItemMeta();
            Location var8 = var7.getLodestone();
            if (var1 == null) {
                if (var8 == null) {
                    return false;
                }

                var7.setLodestone(null);
            } else {
                if (var8 != null) {
                    World var24 = var8.getWorld();
                    World var35 = var1.getWorld();
                    if (var24 == var35 && var8.distanceSquared(var1) < 0.01) {
                        return false;
                    }
                }

                var7.setLodestoneTracked(false);
                var7.setLodestone(var1);
            }

            var0.setItemMeta(var7);
            return true;
        }
    }
}
