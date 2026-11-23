package com.bliss.goldgem;

import com.bliss.core.GemType;
import com.bliss.villager.Text;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class RitualItems {
    public static final Material WIRE_FRAGMENT_MATERIAL = Material.COPPER_INGOT;
    public static final Material FRAGMENT_CORE_MATERIAL = Material.NETHER_STAR;
    public static final Material RITUAL_PROP_MATERIAL = Material.PAPER;
    public static final int WIRE_FRAGMENT_CMD_BASE = 7000;
    public static final int FRAGMENT_CORE_CMD = 7000;
    public static final int FRAGMENT_CORE_GHOST_CMD = 7001;
    public static final int RITUAL_FLASH_CMD = 7100;
    public static final int GOLD_GEM_REVEAL_CMD = 7101;
    public static final int GOLD_GEM_EMPTY_CMD = 7120;
    public static final int GEM_SHARD_CMD_BASE = 7130;
    public static final int GEM_BROKEN_CMD_BASE = 7160;
    public static final int GOLD_GEM_VORONOI_CMD_BASE = 7110;
    public static final int GOLD_ORB_CMD = 7180;
    public static final int HOLO_ORB_CMD = 7181;
    public static final NamespacedKey KEY_WIRE_FRAGMENT = NamespacedKey.fromString("bliss:wire_fragment");
    public static final NamespacedKey KEY_FRAGMENT_CORE = NamespacedKey.fromString("bliss:fragment_core");

    private RitualItems() {
    }

    public static boolean isWireFragment(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var7 = var0.getType();
            Material var3 = WIRE_FRAGMENT_MATERIAL;
            if (var7 == var3) {
                ItemMeta var5 = var0.getItemMeta();
                if (var5 != null) {
                    PersistentDataContainer var13 = var5.getPersistentDataContainer();
                    NamespacedKey var16 = KEY_WIRE_FRAGMENT;
                    PersistentDataType var4 = PersistentDataType.INTEGER;
                    if (var13.has(var16, var4)) {
                        return true;
                    }
                }

                return false;
            }
        }

        return false;
    }

    public static int wireFragmentTask(ItemStack var0, int var1) {
        if (!isWireFragment(var0, 130584315)) {
            return -1;
        } else {
            PersistentDataContainer var9 = var0.getItemMeta().getPersistentDataContainer();
            NamespacedKey var3 = KEY_WIRE_FRAGMENT;
            PersistentDataType var4 = PersistentDataType.INTEGER;
            Integer var5 = (Integer)var9.get(var3, var4);
            int var13;
            if (var5 == null) {
                var13 = -1;
            } else {
                var13 = var5;
            }

            return var13;
        }
    }

    public static boolean isFragmentCore(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var8 = var0.getType();
            Material var3 = FRAGMENT_CORE_MATERIAL;
            if (var8 == var3) {
                ItemMeta var5 = var0.getItemMeta();
                Byte var13;
                if (var5 == null) {
                    var13 = null;
                } else {
                    PersistentDataContainer var19 = var5.getPersistentDataContainer();
                    NamespacedKey var22 = KEY_FRAGMENT_CORE;
                    PersistentDataType var4 = PersistentDataType.BYTE;
                    var13 = (Byte)var19.get(var22, var4);
                }

                boolean var17;
                if (var13 != null && var13 == 1) {
                    var17 = true;
                } else {
                    var17 = false;
                }

                return var17;
            }
        }

        return false;
    }

    public static ItemStack wireFragment(int var0, int var1) {
        int var21 = Math.min(7, var0);
        int var40 = Math.max(1, var21);
        Material var36 = WIRE_FRAGMENT_MATERIAL;
        ItemStack var9 = new ItemStack(var36);
        ItemMeta var7 = var9.getItemMeta();
        PersistentDataContainer var13 = var7.getPersistentDataContainer();
        NamespacedKey var22 = KEY_WIRE_FRAGMENT;
        PersistentDataType var37 = PersistentDataType.INTEGER;
        Integer var39 = var40;
        var13.set(var22, var37, var39);
        Integer var25 = 7000 + var40;
        var7.setCustomModelData(var25);
        Component var28 = noItalic(Text.legacy("<##F6C500>&lᴡɪʀᴇ ғʀᴀɢᴍᴇɴᴛ", 373192400), 1781512942);
        var7.displayName(var28);
        List var32 = List.of(noItalic(Text.legacy("<##FFD773>A fragment with unspoken potential", 373192400), 1781512942));
        var7.lore(var32);
        Integer var34 = 1;
        var7.setMaxStackSize(var34);
        var9.setItemMeta(var7);
        return var9;
    }

    public static ItemStack fragmentCore(int var0) {
        Material var2 = FRAGMENT_CORE_MATERIAL;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var5 = var1.getItemMeta();
        PersistentDataContainer var9 = var5.getPersistentDataContainer();
        NamespacedKey var17 = KEY_FRAGMENT_CORE;
        PersistentDataType var30 = PersistentDataType.BYTE;
        Byte var31 = (byte)1;
        var9.set(var17, var30, var31);
        Integer var19 = 7000;
        var5.setCustomModelData(var19);
        Component var22 = noItalic(Text.legacy("<##FFD773>&lꜰʀᴀɢᴍᴇɴᴛ ᴄᴏʀᴇ", 373192400), 1781512942);
        var5.displayName(var22);
        List var26 = List.of(noItalic(Text.legacy("<##C2A878>A centerpiece of divine masterpiece", 373192400), 1781512942));
        var5.lore(var26);
        Integer var28 = 1;
        var5.setMaxStackSize(var28);
        var1.setItemMeta(var5);
        return var1;
    }

    public static ItemStack goldOrb(int var0) {
        Material var2 = RITUAL_PROP_MATERIAL;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var4 = var1.getItemMeta();
        Integer var14 = 7180;
        var4.setCustomModelData(var14);
        Component var17 = noItalic(Text.legacy("<##FFD773>ᴏʀʙ", 373192400), 1781512942);
        var4.displayName(var17);
        Integer var19 = 1;
        var4.setMaxStackSize(var19);
        var1.setItemMeta(var4);
        return var1;
    }

    public static ItemStack holoOrb(int var0) {
        Material var2 = RITUAL_PROP_MATERIAL;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var4 = var1.getItemMeta();
        Integer var14 = 7181;
        var4.setCustomModelData(var14);
        Component var17 = noItalic(Text.legacy("<##4FB4FF>ᴡᴏʀʟᴅ ʙᴏʀᴅᴇʀ", 373192400), 1781512942);
        var4.displayName(var17);
        Integer var19 = 1;
        var4.setMaxStackSize(var19);
        var1.setItemMeta(var4);
        return var1;
    }

    public static ItemStack fragmentCoreGhost(int var0) {
        Material var2 = FRAGMENT_CORE_MATERIAL;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var4 = var1.getItemMeta();
        Integer var12 = 7001;
        var4.setCustomModelData(var12);
        var1.setItemMeta(var4);
        return var1;
    }

    public static ItemStack ritualFlash() {
        Material var1 = RITUAL_PROP_MATERIAL;
        ItemStack var0 = new ItemStack(var1);
        ItemMeta var3 = var0.getItemMeta();
        Integer var11 = 7100;
        var3.setCustomModelData(var11);
        var0.setItemMeta(var3);
        return var0;
    }

    public static ItemStack goldGemReveal(int var0) {
        Material var2 = RITUAL_PROP_MATERIAL;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var4 = var1.getItemMeta();
        Integer var12 = 7101;
        var4.setCustomModelData(var12);
        var1.setItemMeta(var4);
        return var1;
    }

    public static ItemStack goldGemEmpty(int var0) {
        Material var2 = RITUAL_PROP_MATERIAL;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var4 = var1.getItemMeta();
        Integer var12 = 7120;
        var4.setCustomModelData(var12);
        var1.setItemMeta(var4);
        return var1;
    }

    public static ItemStack gemShard(int var0, int var1, int var2) {
        Material var4 = RITUAL_PROP_MATERIAL;
        ItemStack var3 = new ItemStack(var4);
        ItemMeta var9 = var3.getItemMeta();
        int var29 = Math.min(7, var0);
        int var10 = Math.max(0, var29);
        int var44 = var10 * 3;
        int var31 = 7130 + var44;
        int var49 = var1 - 1;
        int var48 = Math.min(2, var49);
        int var46 = Math.max(0, var48);
        Integer var33 = var31 + var46;
        var9.setCustomModelData(var33);
        GemType var11 = GoldGem.byBit(var10, 621438557);
        GemType var34 = GemType.NONE;
        String var27;
        if (var11 == var34) {
            var27 = "&e&l";
        } else {
            var27 = var11.colorPrefix();
        }

        Component var38 = noItalic(Text.legacy(var27 + "ɢᴇᴍ ꜱʜᴀʀᴅ", 373192400), 1781512942);
        var9.displayName(var38);
        Integer var40 = 1;
        var9.setMaxStackSize(var40);
        var3.setItemMeta(var9);
        return var3;
    }

    public static ItemStack gemBroken(int var0, int var1) {
        Material var3 = RITUAL_PROP_MATERIAL;
        ItemStack var2 = new ItemStack(var3);
        ItemMeta var7 = var2.getItemMeta();
        int var23 = Math.min(7, var0);
        int var22 = Math.max(0, var23);
        Integer var17 = 7160 + var22;
        var7.setCustomModelData(var17);
        Integer var19 = 1;
        var7.setMaxStackSize(var19);
        var2.setItemMeta(var7);
        return var2;
    }

    public static ItemStack goldGemVoronoi(int var0, int var1) {
        int var15 = Math.min(6, var0);
        int var22 = Math.max(0, var15);
        Material var20 = RITUAL_PROP_MATERIAL;
        ItemStack var8 = new ItemStack(var20);
        ItemMeta var6 = var8.getItemMeta();
        Integer var18 = 7110 + var22;
        var6.setCustomModelData(var18);
        var8.setItemMeta(var6);
        return var8;
    }

    private static Component noItalic(Component var0, int var1) {
        TextDecoration var3 = TextDecoration.ITALIC;
        return var0.decoration(var3, false);
    }
}
