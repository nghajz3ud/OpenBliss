package com.bliss.mythics;

import com.bliss.mythics.util.Text;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class MythicItems {
    public static final NamespacedKey KEY_MYTHIC_BLOOD = NamespacedKey.fromString("bliss:mythic_blood");
    public static final NamespacedKey KEY_MYTHIC_AEGIS = NamespacedKey.fromString("bliss:mythic_aegis");
    public static final int CMD_BLOOD_GEM = 30001;
    public static final int CMD_AEGIS_GEM = 30002;
    private static final String MYTHIC_LABEL = "&f(<##8B7CA0>Mythic&f)";

    private MythicItems() {
    }

    public static ItemStack bloodGem(int var0) {
        Material var2 = Material.REPEATING_COMMAND_BLOCK;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var6 = var1.getItemMeta();
        Component var21 = Text.item("<##87161B>&lʜᴇʀᴇᴛɪᴄ <##FFD773>ɢᴇᴍ", 196975792);
        var6.displayName(var21);
        Component[] var23 = new Component[14];
        Component var51 = Text.item("&f&lꜰᴇᴀʀ ɪꜱ ᴛʜᴇ ᴏɴʟʏ ꜱᴀʟᴠᴀᴛɪᴏɴ", 196975792);
        var23[0] = var51;
        Component var53 = Text.item("&f(<##8B7CA0>Mythic&f)", 196975792);
        var23[1] = var53;
        Component var55 = Text.item("&f", 196975792);
        var23[2] = var55;
        Component var57 = Text.item("<##87161B>\ud83d\udd2e <##FFE4AB>ᴘᴀssɪᴠᴇs", 196975792);
        var23[3] = var57;
        Component var59 = Text.item("&7- Auto Crit", 196975792);
        var23[4] = var59;
        Component var61 = Text.item("&7- Enduring Strength", 196975792);
        var23[5] = var61;
        Component var63 = Text.item("&7- Hemorrhage", 196975792);
        var23[6] = var63;
        Component var65 = Text.item("&7- Blood Hardening", 196975792);
        var23[7] = var65;
        Component var67 = Text.item("&f", 196975792);
        var23[8] = var67;
        Component var69 = Text.item("<##87161B>\ud83d\udd2e <##B8FFFB>&lᴘᴏᴡᴇʀs", 196975792);
        var23[9] = var69;
        Component var71 = Text.item("&7- \ud83d\udd20  <##A81325>ʙʟᴏᴏᴅ sᴀᴡs", 196975792);
        var23[10] = var71;
        Component var73 = Text.item("&f", 196975792);
        var23[11] = var73;
        Component var75 = Text.item("&f", 196975792);
        var23[12] = var75;
        Component var77 = Text.item("&7- \ud83d\udd21  <##A81325>ʙʟᴏᴏᴅ ʟɪɴᴋɪɴɢ", 196975792);
        var23[13] = var77;
        List var24 = List.of(var23);
        var6.lore(var24);
        Integer var26 = 30001;
        var6.setCustomModelData(var26);
        Enchantment var27 = Enchantment.MENDING;
        var6.addEnchant(var27, 1, true);
        ItemFlag[] var29 = new ItemFlag[1];
        ItemFlag var78 = ItemFlag.HIDE_ENCHANTS;
        var29[0] = var78;
        var6.addItemFlags(var29);
        PersistentDataContainer var16 = var6.getPersistentDataContainer();
        NamespacedKey var30 = KEY_MYTHIC_BLOOD;
        PersistentDataType var33 = PersistentDataType.BYTE;
        Byte var50 = (byte)1;
        var16.set(var30, var33, var50);
        var1.setItemMeta(var6);
        return var1;
    }

    public static boolean isMythicBloodGem(ItemStack var0, int var1) {
        NamespacedKey var3 = KEY_MYTHIC_BLOOD;
        return hasMarker(var0, var3, 101767461);
    }

    public static ItemStack aegisGem(int var0) {
        Material var2 = Material.REPEATING_COMMAND_BLOCK;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var6 = var1.getItemMeta();
        Component var21 = Text.item("<##E3B94F>&lᴀᴜʀᴀᴛᴜs <##FFD773>ɢᴇᴍ", 196975792);
        var6.displayName(var21);
        Component[] var23 = new Component[14];
        Component var51 = Text.item("&f&lᴍᴀʏ ᴛʜᴇ ᴍᴀʀᴛᴜʀᴇᴅ ɴᴏᴛ ʙᴇ ᴀꜰʀᴀɪᴅ", 196975792);
        var23[0] = var51;
        Component var53 = Text.item("&f(<##8B7CA0>Mythic&f)", 196975792);
        var23[1] = var53;
        Component var55 = Text.item("&f", 196975792);
        var23[2] = var55;
        Component var57 = Text.item("<##E3B94F>\ud83d\udd2e <##FFE4AB>ᴘᴀssɪᴠᴇs", 196975792);
        var23[3] = var57;
        Component var59 = Text.item("&7- Divine Purity", 196975792);
        var23[4] = var59;
        Component var61 = Text.item("&7- Angel's Grasp", 196975792);
        var23[5] = var61;
        Component var63 = Text.item("&7- Feathered Fall", 196975792);
        var23[6] = var63;
        Component var65 = Text.item("&7- Hauling Strike", 196975792);
        var23[7] = var65;
        Component var67 = Text.item("&f", 196975792);
        var23[8] = var67;
        Component var69 = Text.item("<##E3B94F>\ud83d\udd2e <##B8FFFB>&lᴘᴏᴡᴇʀs", 196975792);
        var23[9] = var69;
        Component var71 = Text.item("&7- ⊳ <##E3B94F>ᴠᴇɴᴇʀᴀᴛᴇᴅ ᴘᴇʀꜰᴏʀᴀᴛᴏʀs", 196975792);
        var23[10] = var71;
        Component var73 = Text.item("&f", 196975792);
        var23[11] = var73;
        Component var75 = Text.item("&f", 196975792);
        var23[12] = var75;
        Component var77 = Text.item("&7- ⊲ <##FFE9A8>ᴇᴄʜᴏɪɴɢ ᴀᴇɢɪs", 196975792);
        var23[13] = var77;
        List var24 = List.of(var23);
        var6.lore(var24);
        Integer var26 = 30002;
        var6.setCustomModelData(var26);
        Enchantment var27 = Enchantment.MENDING;
        var6.addEnchant(var27, 1, true);
        ItemFlag[] var29 = new ItemFlag[1];
        ItemFlag var78 = ItemFlag.HIDE_ENCHANTS;
        var29[0] = var78;
        var6.addItemFlags(var29);
        PersistentDataContainer var16 = var6.getPersistentDataContainer();
        NamespacedKey var30 = KEY_MYTHIC_AEGIS;
        PersistentDataType var33 = PersistentDataType.BYTE;
        Byte var50 = (byte)1;
        var16.set(var30, var33, var50);
        var1.setItemMeta(var6);
        return var1;
    }

    public static boolean isMythicAegisGem(ItemStack var0, int var1) {
        NamespacedKey var3 = KEY_MYTHIC_AEGIS;
        return hasMarker(var0, var3, 101767461);
    }

    public static boolean hasAegisInInventory(Player var0, int var1) {
        ItemStack[] var4 = var0.getInventory().getContents();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; var6++) {
            if (isMythicAegisGem(var4[var6], 73386651)) {
                return true;
            }
        }

        if (isMythicAegisGem(var0.getInventory().getItemInOffHand(), 73386651)) {
            return true;
        } else {
            ItemStack[] var40 = var0.getInventory().getArmorContents();
            int var41 = var40.length;

            for (int var42 = 0; var42 < var41; var42++) {
                if (isMythicAegisGem(var40[var42], 73386651)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean hasMythicGemInInventory(Player var0, int var1) {
        for (ItemStack var7 : var0.getInventory().getContents()) {
            if (isMythicBloodGem(var7, 824514243) || isMythicAegisGem(var7, 73386651)) {
                return true;
            }
        }

        ItemStack var54 = var0.getInventory().getItemInOffHand();
        if (!isMythicBloodGem(var54, 824514243) && !isMythicAegisGem(var54, 73386651)) {
            for (ItemStack var8 : var0.getInventory().getArmorContents()) {
                if (isMythicBloodGem(var8, 824514243) || isMythicAegisGem(var8, 73386651)) {
                    return true;
                }
            }

            ItemStack var56 = var0.getItemOnCursor();
            boolean var50;
            if (!isMythicBloodGem(var56, 824514243) && !isMythicAegisGem(var56, 73386651)) {
                var50 = false;
            } else {
                var50 = true;
            }

            return var50;
        } else {
            return true;
        }
    }

    private static boolean hasMarker(ItemStack var0, NamespacedKey var1, int var2) {
        if (var0 != null) {
            Material var9 = var0.getType();
            Material var4 = Material.REPEATING_COMMAND_BLOCK;
            if (var9 == var4) {
                ItemMeta var6 = var0.getItemMeta();
                if (var6 == null) {
                    return false;
                }

                PersistentDataContainer var15 = var6.getPersistentDataContainer();
                PersistentDataType var5 = PersistentDataType.BYTE;
                Byte var7 = (Byte)var15.get(var1, var5);
                boolean var22;
                if (var7 != null && var7 == 1) {
                    var22 = true;
                } else {
                    var22 = false;
                }

                return var22;
            }
        }

        return false;
    }
}
