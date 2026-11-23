package com.bliss.villager;

import com.bliss.BlissPlugin;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.DataComponentType.Valued;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class BlissItems {
    public static final int CMD_BOTTLED_ENERGY = 300;
    public static final int CMD_ENERGY_TOKEN = 350;
    public static final int CMD_SOUL_BASE = 359;
    public static final int CMD_VILLAGE_DISC = 370;
    public static final NamespacedKey VILLAGE_DISC_KEY = NamespacedKey.fromString("bliss:village_disc");
    private static final int[] SOUL_STOPS;
    private static final String SOUL_OWNER_LABEL = Text.smallCaps("Soul Owner", 1315898192);
    private static final int[] OWNER_STOPS;
    private final BlissPlugin plugin;

    public static Component soulDisplayName(double var0, int var2) {
        String var3 = "Villager Soul";
        int[] var7 = SOUL_STOPS;
        Component var8 = Text.flowGradient(var3, var0, var7, 566816022);
        TextDecoration var10 = TextDecoration.ITALIC;
        return var8.decoration(var10, false);
    }

    public static String soulOwner(int var0, int var1) {
        return switch (var0) {
            case 1 -> "Duke Dennis";
            case 2 -> "KaiCenat";
            default -> "Darren Watkins";
        };
    }

    public static List<Component> soulLore(int var0, double var1, int var3) {
        String var4 = SOUL_OWNER_LABEL;
        int[] var8 = OWNER_STOPS;
        Component var9 = Text.flowGradient(var4, var1, var8, 566816022);
        TextDecoration var12 = TextDecoration.ITALIC;
        Component var10 = var9.decoration(var12, false);
        Component var16 = Text.legacy("&f" + soulOwner(var0, 904057364), 373192400);
        TextDecoration var18 = TextDecoration.ITALIC;
        Component var17 = var16.decoration(var18, false);
        return List.of(var10, var17);
    }

    public BlissItems(BlissPlugin var1, int var2) {
        this.plugin = var1;
    }

    public ItemStack energyToken(int var1) {
        Material var3 = Material.NAUTILUS_SHELL;
        ItemStack var2 = new ItemStack(var3);
        ItemMeta var7 = var2.getItemMeta();
        Component var17 = Text.legacy("<#90EE90>&lᴇɴᴇʀɢʏ &r<#90EE90>ᴛᴏᴋᴇɴ", 373192400);
        TextDecoration var26 = TextDecoration.ITALIC;
        Component var18 = var17.decoration(var26, false);
        var7.displayName(var18);
        Component var20 = Text.legacy("<#90EE90>&oConverted from bottled energy.", 373192400);
        TextDecoration var27 = TextDecoration.ITALIC;
        Component var21 = var20.decoration(var27, false);
        Component var29 = Text.legacy("&7Cannot be reversed or restore gem energy.", 373192400);
        TextDecoration var32 = TextDecoration.ITALIC;
        Component var30 = var29.decoration(var32, false);
        List var22 = List.of(var21, var30);
        var7.lore(var22);
        Integer var24 = 350;
        var7.setCustomModelData(var24);
        var2.setItemMeta(var7);
        return var2;
    }

    public ItemStack soul(int var1, int var2) {
        if (var1 >= 1 && var1 <= 3) {
            Material var5 = Material.PITCHER_POD;
            ItemStack var9 = new ItemStack(var5);
            ItemMeta var7 = var9.getItemMeta();
            Component var24 = soulDisplayName(0.0, 310033912);
            var7.displayName(var24);
            List var26 = soulLore(var1, 0.0, 1257478398);
            var7.lore(var26);
            Integer var29 = 359 + var1;
            var7.setCustomModelData(var29);
            var7.setUnbreakable(true);
            Boolean var32 = true;
            var7.setEnchantmentGlintOverride(var32);
            var9.setItemMeta(var7);
            return var9;
        } else {
            String var37 = "soul id must be 1..3";
            IllegalArgumentException var20 = new IllegalArgumentException(var37);
            throw var20;
        }
    }

    public ItemStack villageDisc(String var1, int var2) {
        Material var4 = Material.MUSIC_DISC_5;
        ItemStack var3 = new ItemStack(var4);
        ItemMeta var9 = var3.getItemMeta();
        Component var20 = Text.legacy("<#DA70D6>&oPlay it in a jukebox...", 373192400);
        TextDecoration var26 = TextDecoration.ITALIC;
        Component var21 = var20.decoration(var26, false);
        Component var28 = Text.legacy("&7The melody whispers where the", 373192400);
        TextDecoration var31 = TextDecoration.ITALIC;
        Component var29 = var28.decoration(var31, false);
        Component var33 = Text.legacy("&7lost village can be found.", 373192400);
        TextDecoration var37 = TextDecoration.ITALIC;
        Component var34 = var33.decoration(var37, false);
        List var22 = List.of(var21, var29, var34);
        var9.lore(var22);
        PersistentDataContainer var14 = var9.getPersistentDataContainer();
        NamespacedKey var23 = VILLAGE_DISC_KEY;
        PersistentDataType var30 = PersistentDataType.BYTE;
        Byte var36 = (byte)1;
        var14.set(var23, var30, var36);
        var3.setItemMeta(var9);
        Valued var25 = DataComponentTypes.JUKEBOX_PLAYABLE;
        var3.unsetData(var25);
        return var3;
    }

    public boolean isVillageDisc(ItemStack var1, int var2) {
        if (var1 != null) {
            Material var8 = var1.getType();
            Material var4 = Material.MUSIC_DISC_5;
            if (var8 == var4) {
                ItemMeta var6 = var1.getItemMeta();
                if (var6 != null) {
                    PersistentDataContainer var13 = var6.getPersistentDataContainer();
                    NamespacedKey var17 = VILLAGE_DISC_KEY;
                    PersistentDataType var5 = PersistentDataType.BYTE;
                    if (var13.has(var17, var5)) {
                        return true;
                    }
                }

                return false;
            }
        }

        return false;
    }

    public int soulIdOf(ItemStack var1, int var2) {
        if (var1 != null) {
            Material var9 = var1.getType();
            Material var4 = Material.PITCHER_POD;
            if (var9 == var4) {
                ItemMeta var5 = var1.getItemMeta();
                if (var5 != null && var5.hasCustomModelData()) {
                    int var7 = var5.getCustomModelData() - 359;
                    int var21;
                    if (var7 >= 1 && var7 <= 3) {
                        var21 = var7;
                    } else {
                        var21 = -1;
                    }

                    return var21;
                }

                return -1;
            }
        }

        return -1;
    }

    public boolean isEnergyToken(ItemStack var1, int var2) {
        if (var1 != null) {
            Material var7 = var1.getType();
            Material var4 = Material.NAUTILUS_SHELL;
            if (var7 == var4) {
                ItemMeta var5 = var1.getItemMeta();
                boolean var15;
                if (var5 != null && var5.hasCustomModelData() && var5.getCustomModelData() == 350) {
                    var15 = true;
                } else {
                    var15 = false;
                }

                return var15;
            }
        }

        return false;
    }

    public boolean isBottledEnergy(ItemStack var1, int var2) {
        if (var1 != null) {
            Material var7 = var1.getType();
            Material var4 = Material.NAUTILUS_SHELL;
            if (var7 == var4) {
                ItemMeta var5 = var1.getItemMeta();
                boolean var16;
                if (var5 != null && var5.hasCustomModelData() && var5.getCustomModelData() == 300) {
                    var16 = true;
                } else {
                    var16 = false;
                }

                return var16;
            }
        }

        return false;
    }

    static {
        int[] var5 = new int[]{16755200, 16777045};
        SOUL_STOPS = var5;
        int[] var9 = new int[]{11167487, 16742365};
        OWNER_STOPS = var9;
    }
}
