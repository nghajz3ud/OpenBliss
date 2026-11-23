package com.bliss.goldgem;

import com.bliss.core.GemType;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class GoldGem {
    public static final Material MATERIAL = Material.PRISMARINE_CRYSTALS;
    public static final int CMD_BASE = 1001;
    public static final int STATE_DORMANT = 0;
    public static final int STATE_AWAKENED = 1;
    public static final NamespacedKey KEY_GOLD = NamespacedKey.fromString("bliss:goldgem");
    public static final NamespacedKey KEY_SET = NamespacedKey.fromString("bliss:goldgem_set");
    public static final NamespacedKey KEY_STATE = NamespacedKey.fromString("bliss:goldgem_state");
    public static final NamespacedKey KEY_HOLDER = NamespacedKey.fromString("bliss:goldgem_holder");
    public static final NamespacedKey KEY_SELECTED = NamespacedKey.fromString("bliss:goldgem_selected");
    public static final NamespacedKey KEY_OWN = NamespacedKey.fromString("bliss:goldgemown");
    private static final GemType[] BIT_ORDER;
    private static final EnumMap<GemType, Integer> BIT;

    private GoldGem() {
    }

    public static int bit(GemType var0, int var1) {
        EnumMap var2 = BIT;
        Integer var4 = (Integer)var2.get(var0);
        int var9;
        if (var4 == null) {
            var9 = -1;
        } else {
            var9 = var4;
        }

        return var9;
    }

    public static GemType byBit(int var0, int var1) {
        if (var0 >= 0) {
            int var7 = BIT_ORDER.length;
            if (var0 < var7) {
                GemType[] var5 = BIT_ORDER;
                return var5[var0];
            }
        }

        return GemType.NONE;
    }

    public static int bitValue(GemType var0, int var1) {
        int var4 = bit(var0, 880748148);
        int var8;
        if (var4 < 0) {
            var8 = 0;
        } else {
            var8 = 1 << var4;
        }

        return var8;
    }

    public static List<GemType> gemsOf(int var0, int var1) {
        ArrayList var2 = new ArrayList();
        ArrayList var3 = var2;

        for (GemType var7 : BIT_ORDER) {
            int var23 = bitValue(var7, 841817363);
            if ((var0 & var23) != 0) {
                var3.add(var7);
            }
        }

        return var3;
    }

    public static int maskOf(Collection<GemType> var0) {
        int var3 = 0;

        for (GemType var5 : var0) {
            int var16 = bitValue(var5, 841817363);
            var3 |= var16;
        }

        return var3;
    }

    public static boolean has(int var0, GemType var1, int var2) {
        int var7 = bitValue(var1, 841817363);
        boolean var6;
        if ((var0 & var7) != 0) {
            var6 = true;
        } else {
            var6 = false;
        }

        return var6;
    }

    public static boolean isGoldGem(ItemStack var0) {
        if (var0 != null) {
            Material var7 = var0.getType();
            Material var2 = MATERIAL;
            if (var7 == var2) {
                ItemMeta var4 = var0.getItemMeta();
                if (var4 == null) {
                    return false;
                }

                PersistentDataContainer var12 = var4.getPersistentDataContainer();
                NamespacedKey var21 = KEY_GOLD;
                PersistentDataType var3 = PersistentDataType.INTEGER;
                Integer var5 = (Integer)var12.get(var21, var3);
                boolean var20;
                if (var5 != null && var5 == 1) {
                    var20 = true;
                } else {
                    var20 = false;
                }

                return var20;
            }
        }

        return false;
    }

    public static int getMask(ItemStack var0, int var1) {
        ItemMeta var17;
        if (var0 == null) {
            var17 = null;
        } else {
            var17 = var0.getItemMeta();
        }

        if (var17 == null) {
            return 0;
        } else {
            PersistentDataContainer var10 = var17.getPersistentDataContainer();
            NamespacedKey var3 = KEY_SET;
            PersistentDataType var4 = PersistentDataType.INTEGER;
            Integer var6 = (Integer)var10.get(var3, var4);
            int var15;
            if (var6 == null) {
                var15 = 0;
            } else {
                var15 = var6;
            }

            return var15;
        }
    }

    public static int getState(ItemStack var0, int var1) {
        ItemMeta var17;
        if (var0 == null) {
            var17 = null;
        } else {
            var17 = var0.getItemMeta();
        }

        if (var17 == null) {
            return 0;
        } else {
            PersistentDataContainer var10 = var17.getPersistentDataContainer();
            NamespacedKey var3 = KEY_STATE;
            PersistentDataType var4 = PersistentDataType.INTEGER;
            Integer var6 = (Integer)var10.get(var3, var4);
            int var16;
            if (var6 == null) {
                var16 = 0;
            } else {
                var16 = var6;
            }

            return var16;
        }
    }

    public static UUID getHolder(ItemStack var0, int var1) {
        ItemMeta var9;
        if (var0 == null) {
            var9 = null;
        } else {
            var9 = var0.getItemMeta();
        }

        if (var9 == null) {
            return null;
        } else {
            PersistentDataContainer var12 = var9.getPersistentDataContainer();
            NamespacedKey var3 = KEY_HOLDER;
            PersistentDataType var4 = PersistentDataType.STRING;
            String var6 = (String)var12.get(var3, var4);
            if (var6 != null && !var6.isEmpty()) {
                try {
                    return UUID.fromString(var6);
                } catch (IllegalArgumentException var20) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public static GemType getSelected(ItemStack var0, int var1) {
        ItemMeta var17;
        if (var0 == null) {
            var17 = null;
        } else {
            var17 = var0.getItemMeta();
        }

        if (var17 == null) {
            return GemType.NONE;
        } else {
            PersistentDataContainer var11 = var17.getPersistentDataContainer();
            NamespacedKey var3 = KEY_SELECTED;
            PersistentDataType var4 = PersistentDataType.INTEGER;
            Integer var6 = (Integer)var11.get(var3, var4);
            GemType var18;
            if (var6 == null) {
                var18 = GemType.NONE;
            } else {
                var18 = GemType.byId(var6);
            }

            return var18;
        }
    }

    public static GemType getOwn(ItemStack var0, int var1) {
        ItemMeta var18;
        if (var0 == null) {
            var18 = null;
        } else {
            var18 = var0.getItemMeta();
        }

        if (var18 == null) {
            return GemType.NONE;
        } else {
            PersistentDataContainer var11 = var18.getPersistentDataContainer();
            NamespacedKey var3 = KEY_OWN;
            PersistentDataType var4 = PersistentDataType.INTEGER;
            Integer var6 = (Integer)var11.get(var3, var4);
            GemType var15;
            if (var6 == null) {
                var15 = GemType.NONE;
            } else {
                var15 = GemType.byId(var6);
            }

            return var15;
        }
    }

    public static boolean hasOwnRecord(ItemStack var0, int var1) {
        ItemMeta var12;
        if (var0 == null) {
            var12 = null;
        } else {
            var12 = var0.getItemMeta();
        }

        if (var12 != null) {
            PersistentDataContainer var9 = var12.getPersistentDataContainer();
            NamespacedKey var3 = KEY_OWN;
            PersistentDataType var4 = PersistentDataType.INTEGER;
            if (var9.has(var3, var4)) {
                return true;
            }
        }

        return false;
    }

    public static List<GemType> absorbedGems(ItemStack var0, int var1) {
        return gemsOf(getMask(var0, 139794617), 1452126696);
    }

    public static ItemStack findInInventory(Player var0, int var1) {
        if (var0 == null) {
            return null;
        } else {
            for (ItemStack var7 : var0.getInventory().getContents()) {
                if (isGoldGem(var7)) {
                    return var7;
                }
            }

            return null;
        }
    }

    public static ItemStack heldGoldGem(Player var0, int var1) {
        if (var0 == null) {
            return null;
        } else {
            ItemStack var3 = var0.getInventory().getItemInMainHand();
            if (isGoldGem(var3)) {
                return var3;
            } else {
                ItemStack var4 = var0.getInventory().getItemInOffHand();
                return isGoldGem(var4) ? var4 : null;
            }
        }
    }

    public static GemType wieldedSoul(Player var0, int var1) {
        return soulOf(heldGoldGem(var0, 780486261), 227784551);
    }

    public static GemType wieldedSoulOf(ItemStack var0) {
        return soulOf(var0, 227784551);
    }

    private static GemType soulOf(ItemStack var0, int var1) {
        if (!isGoldGem(var0)) {
            return GemType.NONE;
        } else {
            List var4 = absorbedGems(var0, 874118425);
            if (var4.isEmpty()) {
                return GemType.NONE;
            } else {
                GemType var5 = getSelected(var0, 128405186);
                GemType var19;
                if (var4.contains(var5)) {
                    var19 = var5;
                } else {
                    var19 = (GemType)var4.get(0);
                }

                return var19;
            }
        }
    }

    public static boolean isWieldingSoul(Player var0, GemType var1, int var2) {
        if (var1 != null) {
            GemType var4 = GemType.NONE;
            if (var1 != var4) {
                GemType var7 = wieldedSoul(var0, 470804311);
                if (var7 == var1) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isWieldingSoulMain(Player var0, GemType var1, int var2) {
        if (var1 != null) {
            GemType var4 = GemType.NONE;
            if (var1 != var4 && var0 != null) {
                GemType var10 = soulOf(var0.getInventory().getItemInMainHand(), 227784551);
                if (var10 == var1) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isWieldingSoulOff(Player var0, GemType var1, int var2) {
        if (var1 != null) {
            GemType var4 = GemType.NONE;
            if (var1 != var4 && var0 != null) {
                GemType var10 = soulOf(var0.getInventory().getItemInOffHand(), 227784551);
                if (var10 == var1) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasAbsorbedSoul(Player var0, GemType var1, int var2) {
        if (var1 != null) {
            GemType var4 = GemType.NONE;
            if (var1 != var4) {
                ItemStack var5 = findInInventory(var0, 1361603346);
                if (var5 != null) {
                    int var11 = getMask(var5, 139794617);
                    if (has(var11, var1, 949760421)) {
                        return true;
                    }
                }

                return false;
            }
        }

        return false;
    }

    public static void setMask(ItemStack var0, int var1, int var2) {
        ItemMeta var8 = var0.getItemMeta();
        PersistentDataContainer var11 = var8.getPersistentDataContainer();
        NamespacedKey var4 = KEY_SET;
        PersistentDataType var5 = PersistentDataType.INTEGER;
        Integer var17 = var1 & 0xFF;
        var11.set(var4, var5, var17);
        var0.setItemMeta(var8);
        applyVisuals(var0, 632368650);
    }

    public static void addGem(ItemStack var0, GemType var1, int var2) {
        int var6 = getMask(var0, 139794617);
        int var8 = bitValue(var1, 841817363);
        int var7 = var6 | var8;
        setMask(var0, var7, 1710446259);
    }

    public static void setState(ItemStack var0, int var1, int var2) {
        ItemMeta var7 = var0.getItemMeta();
        PersistentDataContainer var10 = var7.getPersistentDataContainer();
        NamespacedKey var4 = KEY_STATE;
        PersistentDataType var5 = PersistentDataType.INTEGER;
        Integer var15 = var1;
        var10.set(var4, var5, var15);
        var0.setItemMeta(var7);
        applyVisuals(var0, 632368650);
    }

    public static void setHolder(ItemStack var0, UUID var1, int var2) {
        ItemMeta var7 = var0.getItemMeta();
        PersistentDataContainer var10 = var7.getPersistentDataContainer();
        NamespacedKey var4 = KEY_HOLDER;
        PersistentDataType var5 = PersistentDataType.STRING;
        String var15;
        if (var1 == null) {
            var15 = "";
        } else {
            var15 = var1.toString();
        }

        var10.set(var4, var5, var15);
        var0.setItemMeta(var7);
    }

    public static void setSelected(ItemStack var0, GemType var1, int var2) {
        ItemMeta var7 = var0.getItemMeta();
        PersistentDataContainer var10 = var7.getPersistentDataContainer();
        NamespacedKey var4 = KEY_SELECTED;
        PersistentDataType var5 = PersistentDataType.INTEGER;
        int var17;
        if (var1 == null) {
            var17 = 0;
        } else {
            var17 = var1.id();
        }

        Integer var15 = var17;
        var10.set(var4, var5, var15);
        var0.setItemMeta(var7);
        applyVisuals(var0, 632368650);
    }

    public static void setOwn(ItemStack var0, GemType var1, int var2) {
        ItemMeta var7 = var0.getItemMeta();
        PersistentDataContainer var10 = var7.getPersistentDataContainer();
        NamespacedKey var4 = KEY_OWN;
        PersistentDataType var5 = PersistentDataType.INTEGER;
        int var14;
        if (var1 == null) {
            var14 = 0;
        } else {
            var14 = var1.id();
        }

        Integer var15 = var14;
        var10.set(var4, var5, var15);
        var0.setItemMeta(var7);
    }

    public static ItemStack create(int var0) {
        Material var2 = MATERIAL;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var5 = var1.getItemMeta();
        PersistentDataContainer var9 = var5.getPersistentDataContainer();
        NamespacedKey var20 = KEY_GOLD;
        PersistentDataType var26 = PersistentDataType.INTEGER;
        Integer var30 = 1;
        var9.set(var20, var26, var30);
        PersistentDataContainer var11 = var5.getPersistentDataContainer();
        NamespacedKey var21 = KEY_SET;
        PersistentDataType var27 = PersistentDataType.INTEGER;
        Integer var32 = 0;
        var11.set(var21, var27, var32);
        PersistentDataContainer var13 = var5.getPersistentDataContainer();
        NamespacedKey var22 = KEY_STATE;
        PersistentDataType var28 = PersistentDataType.INTEGER;
        Integer var34 = 0;
        var13.set(var22, var28, var34);
        PersistentDataContainer var15 = var5.getPersistentDataContainer();
        NamespacedKey var24 = NamespacedKey.fromString("bliss:tier");
        PersistentDataType var29 = PersistentDataType.INTEGER;
        Integer var36 = 2;
        var15.set(var24, var29, var36);
        var1.setItemMeta(var5);
        applyVisuals(var1, 632368650);
        return var1;
    }

    public static void applyVisuals(ItemStack var0, int var1) {
        ItemMeta var6 = var0.getItemMeta();
        if (var6 != null) {
            int var7 = getMask(var0, 139794617);
            int var8 = getState(var0, 1121303736);
            int var31 = var7 & 0xFF;
            Integer var21 = 1001 + var31;
            var6.setCustomModelData(var21);
            Component var24 = noItalic(Text.legacy("<##FFD773>&lɢᴏʟᴅ ɢᴇᴍ", 373192400), 183978268);
            var6.displayName(var24);
            Stream<String> var27 = buildLore(var8, 330984021).stream();
            Function<String, Component> var32 = GoldGem::noItalic;
            List var29 = var27.map(var32).toList();
            var6.lore(var29);
            var0.setItemMeta(var6);
        }
    }

    private static List<String> buildLore(int var0, int var1) {
        ArrayList var2 = new ArrayList();
        var2.add("&f&lᴡᴀᴛᴄʜ ᴛʜᴇ ʟɪɴᴇꜱ ᴏꜰ ʀᴇᴀʟɪᴛʏ ꜰʀᴀʏ ᴀꜱ ᴇɪɢʜᴛ ꜱᴏᴜʟꜱ ʙᴇᴄᴏᴍᴇ ᴏɴᴇ");
        String var44;
        if (var0 == 1) {
            var44 = "<##FFD773>&l(ᴀᴡᴀᴋᴇɴᴇᴅ)";
        } else {
            var44 = "<##C2A878>(ᴅᴏʀᴍᴀɴᴛ)";
        }

        var2.add(var44);
        var2.add("&f");
        var2.add("<##FFD773>\ud83d\udd2e <##FFE4AB>ᴘᴀssɪᴠᴇs");
        String var48 = cryptic("&f", 11, 311222956);
        var2.add(var48);
        String var50 = cryptic("&f", 10, 311222956);
        var2.add(var50);
        String var52 = cryptic("&f", 9, 311222956);
        var2.add(var52);
        String var54 = cryptic("&f", 8, 311222956);
        var2.add(var54);
        var2.add("&f");
        var2.add("<##FFD773>\ud83d\udd2e <##FE8120>&lᴀʙɪʟɪᴛʏ");
        String var59 = cryptic("<##FFD773>", 6, 311222956) + " <##FFD773>◄";
        var2.add(var59);
        var2.add("&f");
        var2.add("<##FFD773>\ud83d\udd2e <##FFD773>&lᴘᴏᴡᴇʀs");
        String var63 = cryptic("&f", 7, 311222956);
        var2.add(var63);
        String var65 = cryptic("<##FFD773>", 13, 311222956);
        var2.add(var65);
        var2.add("&f");
        String var68 = cryptic("<##FFD773>", 11, 311222956);
        var2.add(var68);
        String var70 = cryptic("<##FFD773>", 13, 311222956);
        var2.add(var70);
        return var2;
    }

    private static String cryptic(String var0, int var1, int var2) {
        String var4 = "&7- ";
        StringBuilder var3 = new StringBuilder(var4);
        StringBuilder var5 = var3.append(var0).append("&k");

        for (int var6 = 0; var6 < var1; var6++) {
            var5.append('a');
        }

        return var5.toString();
    }

    private static Component noItalic(String var0) {
        return noItalic(Text.legacy(var0, 373192400), 183978268);
    }

    private static Component noItalic(Component var0, int var1) {
        TextDecoration var3 = TextDecoration.ITALIC;
        return var0.decoration(var3, false);
    }

    static {
        GemType[] var17 = new GemType[8];
        GemType var2 = GemType.FLUX;
        var17[0] = var2;
        GemType var38 = GemType.STRENGTH;
        var17[1] = var38;
        GemType var39 = GemType.LIFE;
        var17[2] = var39;
        GemType var40 = GemType.PUFF;
        var17[3] = var40;
        GemType var41 = GemType.ASTRA;
        var17[4] = var41;
        GemType var42 = GemType.WEALTH;
        var17[5] = var42;
        GemType var43 = GemType.SPEED;
        var17[6] = var43;
        GemType var44 = GemType.FIRE;
        var17[7] = var44;
        BIT_ORDER = var17;
        EnumMap var18 = new EnumMap<>(GemType.class);
        BIT = var18;
        int var3 = 0;

        while (true) {
            int var24 = BIT_ORDER.length;
            if (var3 >= var24) {
                break;
            }

            EnumMap var21 = BIT;
            GemType[] var25 = BIT_ORDER;
            GemType var26 = var25[var3];
            Integer var37 = var3;
            var21.put(var26, var37);
            var3++;
        }
    }
}
