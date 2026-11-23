package com.bliss.items;

import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerData;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.trader.TraderListener;
import com.bliss.villager.Text;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class BlissCoreItems {
    public static final int CMD_FRAGMENT = 240;
    public static final int CMD_ENERGY = 300;
    public static final int CMD_ENERGY_USELESS = 301;
    public static final int CMD_RESTORATION = 210;
    public static final int CMD_REPAIR = 200;
    public static final int CMD_GEM_SELECTOR = 260;
    public static final int CMD_DAGGER = 230;
    public static final int CMD_MEGA_MACE = 250;
    public static final int CMD_PRISMATIC_EDGE = 13;
    public static final String TEX_UPGRADER = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk5MmU0YThmNDNjNzdhYzJlMmQ3OTJkOTYwNGY2MWYzNDM4N2M4NzdhYmEzNzEyZTQwNDdiNzQwYzU3OCJ9fX0=";
    public static final String TEX_TRADER = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY2MjRhNDRlNzdiOTVkYmUxY2M3MzU1MzY5NTNlODQwYmE2YzE5YjAxNTdjNTQ5ZDliODI1MzQ2NDhjOGNlNCJ9fX0=";
    public static final int GEM_T1_CMD_OFFSET = 100;
    public static final int GEM_T2_CMD_OFFSET = 200;
    public static final NamespacedKey KEY_GEM = NamespacedKey.fromString("bliss:gem");
    public static final NamespacedKey KEY_TIER = NamespacedKey.fromString("bliss:tier");
    public static final NamespacedKey ENERGY_EXPIRY = NamespacedKey.fromString("bliss:energy_expiry");
    public static final NamespacedKey ENERGY_NONCE = NamespacedKey.fromString("bliss:energy_nonce");
    public static final NamespacedKey ENERGY_SHOWN = NamespacedKey.fromString("bliss:energy_shown");
    public static final NamespacedKey KEY_RESTORATION_CORE = NamespacedKey.fromString("bliss:restoration_core");
    public static final NamespacedKey KEY_MEGA_MACE = NamespacedKey.fromString("bliss:megamace");
    public static final NamespacedKey KEY_MEGA_MACE_UNTIL = NamespacedKey.fromString("bliss:megamaceuntil");
    public static final NamespacedKey KEY_PRISMATIC_EDGE = NamespacedKey.fromString("bliss:prismatic_edge");
    public static final NamespacedKey MODEL_PRISMATIC_EDGE = NamespacedKey.fromString("prismatic:weapons/prismatic_edge");
    public static final NamespacedKey TOOLTIP_PRISMATIC_EDGE = NamespacedKey.fromString("prismatic:prismatic");
    public static final long ENERGY_LIFETIME_MS = 3600000L;
    private static final AtomicLong ENERGY_NONCE_SEQ;
    private static BooleanSupplier LEGACY_SPEED = () -> false;
    private static PendingGemStore pending;
    private static final BlissCoreItems.GemSpec LEGACY_SPEED_SPEC;
    private static final Map<GemType, BlissCoreItems.GemSpec> SPECS;

    public ItemStack trader(int var1) {
        ItemStack var7 = PlayerHeads.texturedHead(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY2MjRhNDRlNzdiOTVkYmUxY2M3MzU1MzY5NTNlODQwYmE2YzE5YjAxNTdjNTQ5ZDliODI1MzQ2NDhjOGNlNCJ9fX0=",
            103702773
        );
        ItemMeta var8 = var7.getItemMeta();
        Component var19 = noItalic(Text.legacy("<##f7b2f7>§lᴛʀᴀᴅᴇʀ", 373192400), 599169492);
        var8.displayName(var19);
        Component var22 = noItalic(Text.legacy("§7ʀɪɢʜᴛ-ᴄʟɪᴄᴋ ᴛᴏ ᴄʜᴀɴɢᴇ ʏᴏᴜʀ ɢᴇᴍ ᴛʏᴘᴇ", 373192400), 599169492);
        Component var28 = noItalic(Text.legacy("§7ʏᴏᴜ ᴡɪʟʟ ʀᴇᴄᴇɪᴠᴇ ᴀ ʀᴀɴᴅᴏᴍ ɢᴇᴍ", 373192400), 599169492);
        Component var30 = noItalic(Text.legacy("", 373192400), 599169492);
        Component var32 = noItalic(Text.legacy("§cᴡᴀʀɴɪɴɢ: ᴜꜱɪɴɢ ᴛʜɪꜱ ᴡɪʟʟ ʟᴏꜱᴇ ʏᴏᴜʀ ᴄᴜʀʀᴇɴᴛ ɢᴇᴍ", 373192400), 599169492);
        List var23 = List.of(var22, var28, var30, var32);
        var8.lore(var23);
        Integer var25 = 64;
        var8.setMaxStackSize(var25);
        var7.setItemMeta(var8);
        return var7;
    }

    public ItemStack upgrader(int var1) {
        ItemStack var5 = PlayerHeads.texturedHead(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk5MmU0YThmNDNjNzdhYzJlMmQ3OTJkOTYwNGY2MWYzNDM4N2M4NzdhYmEzNzEyZTQwNDdiNzQwYzU3OCJ9fX0=",
            103702773
        );
        ItemMeta var6 = var5.getItemMeta();
        Component var34 = noItalic(Text.legacy("<##85ffa5>§lᴜᴘɢʀᴀᴅᴇʀ", 373192400), 599169492);
        var6.displayName(var34);
        GlobalState var7 = GlobalState.forWorld(null, 728926677);
        boolean var32;
        if (var7 != null && (var7.keepTier2OnDeath(922876535) || var7.tier2OnJoin(2023108022))) {
            var32 = false;
        } else {
            var32 = true;
        }

        List var47 = List.of(noItalic(Text.legacy("§fʀɪɢʜᴛ-ᴄʟɪᴄᴋ ᴛᴏ ᴜᴘɢʀᴀᴅᴇ ʏᴏᴜʀ ɢᴇᴍ", 373192400), 599169492));
        ArrayList var21 = new ArrayList(var47);
        if (var32) {
            Component var37 = noItalic(Text.legacy("", 373192400), 599169492);
            var21.add(var37);
            Component var40 = noItalic(Text.legacy("§cᴡᴀʀɴɪɴɢ: ᴜᴘɢʀᴀᴅᴇʀ ᴡɪʟʟ ᴅʀᴏᴘ ᴏɴ ᴅᴇᴀᴛʜ", 373192400), 599169492);
            var21.add(var40);
        }

        var6.lore(var21);
        Integer var43 = 64;
        var6.setMaxStackSize(var43);
        var5.setItemMeta(var6);
        return var5;
    }

    public ItemStack bottledEnergy(int var1) {
        String var4 = "<##96FFD9>&lᴇɴᴇʀɢʏ <##FFE4AB>ɪɴ ᴀ ʙᴏᴛᴛʟᴇ";
        ItemStack var8 = this.nautilus(var4, 300, null, 1066515530);
        return this.stampEnergy(var8, -1L, 1037254711);
    }

    public ItemStack bottledEnergy(long var1, int var3) {
        ItemStack var11 = this.bottledEnergy(1532783840);
        ItemMeta var12 = var11.getItemMeta();
        PersistentDataContainer var18 = var12.getPersistentDataContainer();
        NamespacedKey var5 = ENERGY_EXPIRY;
        PersistentDataType var6 = PersistentDataType.LONG;
        Long var42 = var1;
        var18.set(var5, var6, var42);
        long var9 = System.currentTimeMillis();
        long var39 = var1 - var9;
        String var13 = formatEnergyRemaining(Math.max(0L, var39), 679505237);
        PersistentDataContainer var25 = var12.getPersistentDataContainer();
        NamespacedKey var30 = ENERGY_SHOWN;
        PersistentDataType var41 = PersistentDataType.STRING;
        var25.set(var30, var41, var13);
        List var35 = List.of(noItalic(Text.legacy("<##96FFD9>Expires in: &f" + var13, 373192400), 599169492));
        var12.lore(var35);
        var11.setItemMeta(var12);
        return var11;
    }

    public ItemStack bottledEnergyUnique(int var1) {
        String var4 = "<##96FFD9>&lᴇɴᴇʀɢʏ <##FFE4AB>ɪɴ ᴀ ʙᴏᴛᴛʟᴇ";
        ItemStack var8 = this.nautilus(var4, 300, null, 1066515530);
        long var10 = ENERGY_NONCE_SEQ.incrementAndGet();
        return this.stampEnergy(var8, var10, 1037254711);
    }

    private ItemStack stampEnergy(ItemStack var1, long var2, int var4) {
        ItemMeta var12 = var1.getItemMeta();
        PersistentDataContainer var16 = var12.getPersistentDataContainer();
        NamespacedKey var6 = ENERGY_EXPIRY;
        PersistentDataType var7 = PersistentDataType.LONG;
        Long var45 = System.currentTimeMillis() + 3600000L;
        var16.set(var6, var7, var45);
        if (var2 >= 0L) {
            PersistentDataContainer var21 = var12.getPersistentDataContainer();
            NamespacedKey var31 = ENERGY_NONCE;
            PersistentDataType var41 = PersistentDataType.LONG;
            Long var48 = var2;
            var21.set(var31, var41, var48);
        }

        String var13 = formatEnergyRemaining(3600000L, 679505237);
        PersistentDataContainer var26 = var12.getPersistentDataContainer();
        NamespacedKey var32 = ENERGY_SHOWN;
        PersistentDataType var42 = PersistentDataType.STRING;
        var26.set(var32, var42, var13);
        List var37 = List.of(noItalic(Text.legacy("<##96FFD9>Expires in: &f" + var13, 373192400), 599169492));
        var12.lore(var37);
        var1.setItemMeta(var12);
        return var1;
    }

    public static Long energyExpiryOf(ItemStack var0, int var1) {
        if (var0 == null) {
            return null;
        } else {
            ItemMeta var5 = var0.getItemMeta();
            if (var5 == null) {
                return null;
            } else {
                PersistentDataContainer var11 = var5.getPersistentDataContainer();
                NamespacedKey var3 = ENERGY_EXPIRY;
                PersistentDataType var4 = PersistentDataType.LONG;
                return (Long)var11.get(var3, var4);
            }
        }
    }

    public static String formatEnergyRemaining(long var0, int var2) {
        long var38 = var0 / 1000L;
        long var9 = Math.max(0L, var38);
        if (var9 >= 3600L) {
            long var11 = var9 / 3600L;
            String var47;
            if (var11 == 1L) {
                var47 = "";
            } else {
                var47 = "s";
            }

            return var11 + " hour" + var47;
        } else if (var9 >= 60L) {
            long var66 = var9 / 60L;
            String var59;
            if (var66 == 1L) {
                var59 = "";
            } else {
                var59 = "s";
            }

            return var66 + " minute" + var59;
        } else {
            String var53;
            if (var9 == 1L) {
                var53 = "";
            } else {
                var53 = "s";
            }

            return var9 + " second" + var53;
        }
    }

    public void makeUseless(ItemStack var1, int var2) {
        if (var1 != null) {
            ItemMeta var5 = var1.getItemMeta();
            if (var5 != null) {
                Integer var19 = 301;
                var5.setCustomModelData(var19);
                List var23 = List.of(noItalic(Text.legacy("&8ᴜꜱᴇʟᴇꜱꜱ", 373192400), 599169492));
                var5.lore(var23);
                PersistentDataContainer var12 = var5.getPersistentDataContainer();
                NamespacedKey var24 = ENERGY_EXPIRY;
                var12.remove(var24);
                PersistentDataContainer var14 = var5.getPersistentDataContainer();
                NamespacedKey var25 = ENERGY_NONCE;
                var14.remove(var25);
                PersistentDataContainer var16 = var5.getPersistentDataContainer();
                NamespacedKey var26 = ENERGY_SHOWN;
                var16.remove(var26);
                var1.setItemMeta(var5);
            }
        }
    }

    public boolean isUselessEnergy(ItemStack var1, int var2) {
        if (var1 != null) {
            Material var7 = var1.getType();
            Material var4 = Material.NAUTILUS_SHELL;
            if (var7 == var4) {
                ItemMeta var5 = var1.getItemMeta();
                boolean var16;
                if (var5 != null && var5.hasCustomModelData() && var5.getCustomModelData() == 301) {
                    var16 = true;
                } else {
                    var16 = false;
                }

                return var16;
            }
        }

        return false;
    }

    public boolean refreshEnergyLoreIfChanged(ItemStack var1, long var2, int var4) {
        ItemMeta var9 = var1.getItemMeta();
        if (var9 == null) {
            return false;
        } else {
            String var10 = formatEnergyRemaining(var2, 679505237);
            PersistentDataContainer var19 = var9.getPersistentDataContainer();
            NamespacedKey var6 = ENERGY_SHOWN;
            PersistentDataType var7 = PersistentDataType.STRING;
            String var11 = (String)var19.get(var6, var7);
            if (var10.equals(var11)) {
                return false;
            } else {
                PersistentDataContainer var25 = var9.getPersistentDataContainer();
                NamespacedKey var32 = ENERGY_SHOWN;
                PersistentDataType var39 = PersistentDataType.STRING;
                var25.set(var32, var39, var10);
                List var37 = List.of(noItalic(Text.legacy("<##96FFD9>Expires in: &f" + var10, 373192400), 599169492));
                var9.lore(var37);
                var1.setItemMeta(var9);
                return true;
            }
        }
    }

    public ItemStack repairKit(int var1) {
        String var3 = "&7&lʀᴇᴘᴀɪʀ ᴋɪᴛ";
        return this.nautilus(var3, 200, null, 1066515530);
    }

    public ItemStack gemSelector(int var1) {
        String var3 = "<##82F3FF>&lɢᴇᴍ ꜱᴇʟᴇᴄᴛᴏʀ";
        String var5 = "&7Right-click to open the gem case,";
        String var6 = "&7then click the gem you want.";
        String var7 = "&f";
        String var8 = "&7Swaps your gem for that one and";
        String var9 = "&7is used up, like a Trader —";
        String var10 = "&7your tier and energy stay with you.";
        List var12 = List.of(var5, var6, var7, var8, var9, var10);
        return this.nautilus(var3, 260, var12, 1066515530);
    }

    public boolean isGemSelector(ItemStack var1, int var2) {
        Material var5 = Material.NAUTILUS_SHELL;
        return this.isCmd(var1, var5, 260, 1284675036);
    }

    public ItemStack restoration(int var1) {
        String var3 = "&dRestoration &fItem";
        return this.nautilus(var3, 210, null, 1066515530);
    }

    public ItemStack restorationCore(int var1) {
        Material var3 = Material.NETHER_STAR;
        ItemStack var2 = new ItemStack(var3);
        ItemMeta var7 = var2.getItemMeta();
        Component var22 = noItalic(Text.legacy("&d&lʀᴇsᴛᴏʀᴀᴛɪᴏɴ ᴄᴏʀᴇ", 373192400), 599169492);
        var7.displayName(var22);
        List var26 = List.of(noItalic(Text.legacy("&7Used to craft the &dRestoration &7item.", 373192400), 599169492));
        var7.lore(var26);
        Enchantment var27 = Enchantment.MENDING;
        var7.addEnchant(var27, 1, true);
        ItemFlag[] var29 = new ItemFlag[1];
        ItemFlag var5 = ItemFlag.HIDE_ENCHANTS;
        var29[0] = var5;
        var7.addItemFlags(var29);
        PersistentDataContainer var16 = var7.getPersistentDataContainer();
        NamespacedKey var30 = KEY_RESTORATION_CORE;
        PersistentDataType var33 = PersistentDataType.BYTE;
        Byte var36 = (byte)1;
        var16.set(var30, var33, var36);
        var2.setItemMeta(var7);
        return var2;
    }

    public boolean isRestorationCore(ItemStack var1, int var2) {
        if (var1 != null) {
            Material var8 = var1.getType();
            Material var4 = Material.NETHER_STAR;
            if (var8 == var4) {
                ItemMeta var6 = var1.getItemMeta();
                if (var6 != null) {
                    PersistentDataContainer var13 = var6.getPersistentDataContainer();
                    NamespacedKey var17 = KEY_RESTORATION_CORE;
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

    public ItemStack fragment(int var1) {
        String var3 = "<##FFD773>ɢᴇᴍ &r&7ꜰʀᴀɢᴍᴇɴᴛ";
        return this.nautilus(var3, 240, null, 1066515530);
    }

    public ItemStack enchantedObsidian(int var1) {
        Material var3 = Material.OBSIDIAN;
        ItemStack var2 = new ItemStack(var3);
        ItemMeta var6 = var2.getItemMeta();
        Component var17 = noItalic(Text.legacy("&d&lᴇɴᴄʜᴀɴᴛᴇᴅ ᴏʙꜱɪᴅɪᴀɴ", 373192400), 599169492);
        var6.displayName(var17);
        Enchantment var18 = Enchantment.MENDING;
        var6.addEnchant(var18, 1, true);
        var2.setItemMeta(var6);
        return var2;
    }

    public ItemStack megaMace(int var1) {
        Material var3 = Material.MACE;
        ItemStack var2 = new ItemStack(var3);
        ItemMeta var6 = var2.getItemMeta();
        Component var18 = noItalic(Text.legacy("<##7FD8FF>&lᴍᴇɢᴀ ᴍᴀᴄᴇ", 373192400), 599169492);
        var6.displayName(var18);
        Integer var20 = 250;
        var6.setCustomModelData(var20);
        PersistentDataContainer var12 = var6.getPersistentDataContainer();
        NamespacedKey var21 = KEY_MEGA_MACE;
        PersistentDataType var23 = PersistentDataType.BYTE;
        Byte var24 = (byte)1;
        var12.set(var21, var23, var24);
        var2.setItemMeta(var6);
        return var2;
    }

    public ItemStack prismaticEdge(int var1) {
        Material var3 = Material.NETHERITE_SWORD;
        ItemStack var2 = new ItemStack(var3);
        ItemMeta var9 = var2.getItemMeta();
        String var34 = "ᴘʀɪѕᴍᴀᴛɪᴄ ʙʟᴀᴅᴇ";
        int[] var54 = new int[]{16761579, 8316368};
        Component var35 = Text.gradient(var34, var54, 2056282608);
        Component var56 = Text.legacy(" &7(placeholder only)", 373192400);
        Component var37 = noItalic(var35.append(var56), 599169492);
        var9.displayName(var37);
        Component[] var39 = new Component[15];
        Component var87 = noItalic(Text.legacy("&8Shards unite as one,", 373192400), 599169492);
        var39[0] = var87;
        Component var90 = noItalic(Text.legacy("&8The blade seeks all", 373192400), 599169492);
        var39[1] = var90;
        Component var93 = noItalic(Text.legacy(" ", 373192400), 599169492);
        var39[2] = var93;
        Component var96 = noItalic(Text.legacy("&dPassives", 373192400), 599169492);
        var39[3] = var96;
        Component var99 = noItalic(Text.legacy("&7- Mark of Power", 373192400), 599169492);
        var39[4] = var99;
        Component var102 = noItalic(Text.legacy("&f: Broken shields yields shield piercing charge", 373192400), 599169492);
        var39[5] = var102;
        Component var105 = noItalic(Text.legacy("&7- Mark of Prowess", 373192400), 599169492);
        var39[6] = var105;
        Component var108 = noItalic(Text.legacy("&f: The more it hits, the greater its reach", 373192400), 599169492);
        var39[7] = var108;
        Component var111 = noItalic(Text.legacy(" ", 373192400), 599169492);
        var39[8] = var111;
        Component var114 = noItalic(Text.legacy("&dAbilities", 373192400), 599169492);
        var39[9] = var114;
        Component var117 = noItalic(Text.legacy("&7- Iron Edge &8(Right Click to trigger)", 373192400), 599169492);
        var39[10] = var117;
        Component var120 = noItalic(Text.legacy("&f  : Parry incoming threats", 373192400), 599169492);
        var39[11] = var120;
        Component var123 = noItalic(Text.legacy("&a- Prismatic Edge &8(Double move to trigger)", 373192400), 599169492);
        var39[12] = var123;
        Component var126 = noItalic(Text.legacy("&f  : Point the sharp edge at the enemy", 373192400), 599169492);
        var39[13] = var126;
        Component var129 = noItalic(Text.legacy("&f  : And cut through it all", 373192400), 599169492);
        var39[14] = var129;
        List var40 = List.of(var39);
        var9.lore(var40);
        Enchantment var41 = Enchantment.SHARPNESS;
        var9.addEnchant(var41, 5, true);
        Enchantment var42 = Enchantment.SWEEPING_EDGE;
        var9.addEnchant(var42, 3, true);
        Enchantment var43 = Enchantment.KNOCKBACK;
        var9.addEnchant(var43, 1, true);
        Attribute var44 = Attribute.ATTACK_DAMAGE;
        NamespacedKey var131 = NamespacedKey.minecraft("base_attack_damage");
        Operation var6 = Operation.ADD_NUMBER;
        EquipmentSlotGroup var7 = EquipmentSlotGroup.MAINHAND;
        AttributeModifier var60 = new AttributeModifier(var131, 7.0, var6, var7);
        var9.addAttributeModifier(var44, var60);
        Attribute var45 = Attribute.ATTACK_SPEED;
        NamespacedKey var133 = NamespacedKey.minecraft("base_attack_speed");
        Operation var143 = Operation.ADD_NUMBER;
        EquipmentSlotGroup var145 = EquipmentSlotGroup.MAINHAND;
        AttributeModifier var61 = new AttributeModifier(var133, -2.4, var143, var145);
        var9.addAttributeModifier(var45, var61);
        Attribute var46 = Attribute.SWEEPING_DAMAGE_RATIO;
        NamespacedKey var135 = NamespacedKey.fromString("bliss:prismaticsweep");
        Operation var144 = Operation.ADD_NUMBER;
        EquipmentSlotGroup var146 = EquipmentSlotGroup.MAINHAND;
        AttributeModifier var62 = new AttributeModifier(var135, 0.75, var144, var146);
        var9.addAttributeModifier(var46, var62);
        NamespacedKey var47 = MODEL_PRISMATIC_EDGE;
        var9.setItemModel(var47);
        NamespacedKey var48 = TOOLTIP_PRISMATIC_EDGE;
        var9.setTooltipStyle(var48);
        Integer var50 = 13;
        var9.setCustomModelData(var50);
        PersistentDataContainer var30 = var9.getPersistentDataContainer();
        NamespacedKey var51 = KEY_PRISMATIC_EDGE;
        PersistentDataType var63 = PersistentDataType.BYTE;
        Byte var83 = (byte)1;
        var30.set(var51, var63, var83);
        var2.setItemMeta(var9);
        return var2;
    }

    public static boolean isPrismaticEdge(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var7 = var0.getType();
            Material var3 = Material.NETHERITE_SWORD;
            if (var7 != var3) {
                Material var9 = var0.getType();
                Material var18 = Material.DIAMOND_SWORD;
                if (var9 != var18) {
                    return false;
                }
            }

            ItemMeta var5 = var0.getItemMeta();
            if (var5 != null) {
                PersistentDataContainer var14 = var5.getPersistentDataContainer();
                NamespacedKey var19 = KEY_PRISMATIC_EDGE;
                PersistentDataType var4 = PersistentDataType.BYTE;
                if (var14.has(var19, var4)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean isMegaMace(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var7 = var0.getType();
            Material var3 = Material.MACE;
            if (var7 == var3) {
                ItemMeta var5 = var0.getItemMeta();
                if (var5 != null) {
                    PersistentDataContainer var12 = var5.getPersistentDataContainer();
                    NamespacedKey var16 = KEY_MEGA_MACE;
                    PersistentDataType var4 = PersistentDataType.BYTE;
                    if (var12.has(var16, var4)) {
                        return true;
                    }
                }

                return false;
            }
        }

        return false;
    }

    public ItemStack gemDisplay$1463922612(GemType var1, int var2) {
        Material var4 = Material.PRISMARINE_SHARD;
        ItemStack var3 = new ItemStack(var4);
        ItemMeta var6 = var3.getItemMeta();
        Component var18 = noItalic(Text.legacy(var1.coloredName() + " <##FFD773>ɢᴇᴍ", 373192400), 599169492);
        var6.displayName(var18);
        Integer var21 = var1.displayModelData();
        var6.setCustomModelData(var21);
        var3.setItemMeta(var6);
        return var3;
    }

    public ItemStack gemDisplay(GemType var1, int var2, int var3) {
        ItemStack var6 = this.gemDisplay$1463922612(var1, 1977498201);
        ItemMeta var7 = var6.getItemMeta();
        List var19 = List.of(noItalic(Text.legacy("<##FFD773>Chance: &f" + var2 + "%", 373192400), 599169492));
        var7.lore(var19);
        var6.setItemMeta(var7);
        return var6;
    }

    private static int disabledCmd(GemType var0, int var1) {
        return switch (var0) {
            case STRENGTH-> 3001;
            case FIRE-> 3002;
            case WEALTH-> 3003;
            case SPEED-> 3004;
            case PUFF-> 3005;
            case ASTRA-> 3006;
            case LIFE-> 3007;
            case FLUX-> 3008;
            default -> var0.displayModelData();
        };
    }

    public ItemStack gemDisplayDisabled(GemType var1, int var2) {
        Material var4 = Material.PRISMARINE_SHARD;
        ItemStack var3 = new ItemStack(var4);
        ItemMeta var6 = var3.getItemMeta();
        Component var18 = noItalic(Text.legacy(var1.coloredName() + " <##FFD773>ɢᴇᴍ", 373192400), 599169492);
        var6.displayName(var18);
        Integer var21 = disabledCmd(var1, 1981261185);
        var6.setCustomModelData(var21);
        var3.setItemMeta(var6);
        return var3;
    }

    public ItemStack gemDisplayCurrent(GemType var1, int var2) {
        ItemStack var6 = this.gemDisplayDisabled(var1, 259761031);
        ItemMeta var7 = var6.getItemMeta();
        Component var17 = noItalic(Text.legacy("<##FFD773>Chance: &f0%", 373192400), 599169492);
        Component var21 = noItalic(Text.legacy("&7Your current gem &8— &cautomatically off", 373192400), 599169492);
        List var18 = List.of(var17, var21);
        var7.lore(var18);
        var6.setItemMeta(var7);
        return var6;
    }

    public ItemStack gemDisplayExcluded(GemType var1, int var2) {
        ItemStack var6 = this.gemDisplayDisabled(var1, 259761031);
        ItemMeta var7 = var6.getItemMeta();
        Component var17 = noItalic(Text.legacy("<##FFD773>Chance: &f0%", 373192400), 599169492);
        Component var21 = noItalic(Text.legacy("&cDisabled &8— &7click to re-enable", 373192400), 599169492);
        List var18 = List.of(var17, var21);
        var7.lore(var18);
        var6.setItemMeta(var7);
        return var6;
    }

    public ItemStack gemItem(GemType var1, boolean var2, int var3, int var4) {
        GemType var6 = GemType.NONE;
        if (var1 == var6) {
            String var7 = "cannot build NONE gem";
            IllegalArgumentException var11 = new IllegalArgumentException(var7);
            throw var11;
        } else {
            GemType var22 = GemType.SPEED;
            BlissCoreItems.GemSpec var21;
            if (var1 == var22 && LEGACY_SPEED.getAsBoolean()) {
                var21 = LEGACY_SPEED_SPEC;
            } else {
                Map var15 = SPECS;
                var21 = (BlissCoreItems.GemSpec)var15.get(var1);
            }

            if (var21 == null) {
                String var28 = "No gem spec registered for " + var1;
                IllegalStateException var20 = new IllegalStateException(var28);
                throw var20;
            } else {
                return this.renderGem(var21, var1, var2, var3, 187382959);
            }
        }
    }

    public static void setLegacySpeedSupplier(BooleanSupplier var0, int var1) {
        LEGACY_SPEED = var0;
    }

    public ItemStack gemItem$1963390319(GemType var1, boolean var2, int var3) {
        return this.gemItem(var1, var2, 11, 256965471);
    }

    public static void giveOrDrop(Player var0, ItemStack var1, int var2) {
        if (var0 != null && var1 != null) {
            PlayerInventory var11 = var0.getInventory();
            ItemStack[] var23 = new ItemStack[]{var1};

            for (ItemStack var8 : var11.addItem(var23).values()) {
                World var21 = var0.getWorld();
                Location var25 = var0.getLocation();
                var21.dropItemNaturally(var25, var8);
            }
        }
    }

    public static int findGemSlot(Player var0, int var1) {
        if (var0 == null) {
            return -1;
        } else {
            ItemStack[] var4 = var0.getInventory().getContents();
            int var5 = 0;

            while (true) {
                int var17 = var4.length;
                if (var5 >= var17) {
                    return -1;
                }

                if (gemIdOf(var4[var5], 382441079) > 0) {
                    return var5;
                }

                var5++;
            }
        }
    }

    private static boolean empty(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var5 = var0.getType();
            Material var3 = Material.AIR;
            if (var5 != var3 && var0.getAmount() > 0) {
                return false;
            }
        }

        return true;
    }

    public static boolean placeGem(Player var0, int var1, ItemStack var2, int var3) {
        if (var0 != null && var2 != null) {
            PlayerInventory var9 = var0.getInventory();
            if (var1 >= 0) {
                int var34 = var9.getContents().length;
                if (var1 < var34 && empty(var9.getItem(var1), 1029860821)) {
                    var9.setItem(var1, var2);
                    return true;
                }
            }

            ItemStack[] var38 = new ItemStack[]{var2};
            HashMap var10 = var9.addItem(var38);
            if (var10.isEmpty()) {
                return true;
            } else if (empty(var9.getItemInOffHand(), 1029860821)) {
                ItemStack var43 = (ItemStack)var10.values().iterator().next();
                var9.setItemInOffHand(var43);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void initPendingStore(PendingGemStore var0, int var1) {
        pending = var0;
    }

    public static void giveGemOrHold(Player var0, int var1, ItemStack var2, int var3) {
        if (var0 != null && var2 != null) {
            if (!placeGem(var0, var1, var2, 2132502548)) {
                if (pending != null) {
                    PendingGemStore var11 = pending;
                    var11.owe(var0, 1730447580);
                } else {
                    giveOrDrop(var0, var2, 41085121);
                }
            }
        }
    }

    public void refreshHeldGemItem(Player var1, PlayerData var2, int var3) {
        if (findGemSlot(var1, 1702349856) >= 0) {
            this.refreshGemItem(var1, var2, 1088402172);
        }
    }

    public void refreshGemItem(Player var1, PlayerData var2, int var3) {
        int var10 = findGemSlot(var1, 1702349856);
        TraderListener.clearGemItems(var1, 579055745);
        if (!JuggernautGemGuardListener.isJuggernaut(var1, 859003337)) {
            GemType var16 = var2.gem(1413186337);
            GemType var5 = GemType.NONE;
            if (var16 != var5) {
                GemType var20 = var2.gem(1413186337);
                boolean var21 = var2.tier2(629984660);
                int var22 = var2.energy(1160853318);
                ItemStack var19 = this.gemItem(var20, var21, var22, 256965471);
                giveGemOrHold(var1, var10, var19, 2133739666);
            }
        }
    }

    private static String qualityLabel(int var0, int var1) {
        int var11 = Math.min(11, var0);
        int var5 = Math.max(1, var11);

        return switch (var5) {
            case 1 -> "&f&lᴜꜱᴇʟᴇꜱꜱ";
            case 2 -> "&f(<##FF1111>Ruined&f)";
            case 3 -> "&f(<##FFC929>Damaged&f)";
            case 4 -> "&f(<##7958db>Cracked&f)";
            case 5 -> "&f(<##57FF8F>Scratched&f)";
            case 6 -> "&f(<##57FFC7>Pristine&f)";
            default -> "&f(<##57FFC7>Pristine&f + <##96FFD9>" + (var5 - 6) + "&f)";
        };
    }

    private static int t2Cmd(int var0, int var1, int var2) {
        int var12 = Math.min(11, var1);

        return switch (Math.max(1, var12)) {
            case 1 -> 96;
            case 2 -> var0 + 80;
            case 3 -> var0 + 60;
            case 4 -> var0 + 40;
            case 5 -> var0 + 20;
            default -> var0;
        };
    }

    private static int t1Cmd(int var0, int var1, int var2) {
        if (var1 <= 1) {
            return 96;
        } else {
            int var7 = var0 - 1;
            return t2Cmd(var7, var1, 73889878);
        }
    }

    private ItemStack renderGem(BlissCoreItems.GemSpec var1, GemType var2, boolean var3, int var4, int var5) {
        Material var14;
        if (var3) {
            var14 = Material.PRISMARINE_SHARD;
        } else {
            var14 = Material.AMETHYST_SHARD;
        }

        ItemStack var15 = new ItemStack(var14);
        ItemMeta var12 = var15.getItemMeta();
        Component var46 = noItalic(Text.legacy(var1.displayName, 373192400), 599169492);
        var12.displayName(var46);
        ArrayList var19 = new ArrayList();
        String var48 = qualityLabel(var4, 445507797);
        var19.add(var48);
        if (var4 > 1) {
            List var51 = var1.commonBody;
            var19.addAll(var51);
            if (var3) {
                List var53 = var1.t2Powers;
                var19.addAll(var53);
            } else {
                var19.add("&f&lUnknown");
            }
        }

        Stream<String> var55 = var19.stream();
        Function<String, Component> var73 = var0 -> noItalic(Text.legacy(var0, 373192400), 599169492);
        List<Component> var57 = var55.map(var73).toList();
        var12.lore(var57);
        int var72;
        if (var3) {
            int var60 = var1.t2PristineCmd;
            var72 = t2Cmd(var60, var4, 73889878);
        } else {
            int var70 = var1.t2PristineCmd;
            var72 = t1Cmd(var70, var4, 1102908837);
        }

        Integer var61 = var72;
        var12.setCustomModelData(var61);
        PersistentDataContainer var31 = var12.getPersistentDataContainer();
        NamespacedKey var62 = KEY_GEM;
        PersistentDataType var75 = PersistentDataType.INTEGER;
        Integer var80 = var2.id();
        var31.set(var62, var75, var80);
        PersistentDataContainer var33 = var12.getPersistentDataContainer();
        NamespacedKey var63 = KEY_TIER;
        PersistentDataType var76 = PersistentDataType.INTEGER;
        byte var85;
        if (var3) {
            var85 = 2;
        } else {
            var85 = 1;
        }

        Integer var82 = Integer.valueOf(var85);
        var33.set(var63, var76, var82);
        if (var4 >= 11) {
            Enchantment var65 = Enchantment.MENDING;
            var12.addEnchant(var65, 1, true);
            ItemFlag[] var67 = new ItemFlag[1];
            ItemFlag var9 = ItemFlag.HIDE_ENCHANTS;
            var67[0] = var9;
            var12.addItemFlags(var67);
        }

        var15.setItemMeta(var12);
        return var15;
    }

    public static List<String> gemPassiveLines(GemType var0) {
        String var2 = "ᴀssɪᴠ";
        String var3 = "ʙɪʟ";
        return sliceBody(var0, var2, var3, 1689481266);
    }

    public static List<String> gemAbilityLines(GemType var0) {
        String var2 = "ʙɪʟ";
        String var3 = "ᴏᴡᴇʀ";
        return sliceBody(var0, var2, var3, 1689481266);
    }

    public static List<String> gemPowerLines(GemType var0) {
        Map var1 = SPECS;
        BlissCoreItems.GemSpec var3 = (BlissCoreItems.GemSpec)var1.get(var0);
        if (var3 == null) {
            return List.of();
        } else {
            ArrayList var10 = new ArrayList();
            ArrayList var4 = var10;

            for (String var6 : var3.t2Powers) {
                if (var6.contains("-")) {
                    var4.add(var6);
                }
            }

            return var4;
        }
    }

    private static List<String> sliceBody(GemType var0, String var1, String var2, int var3) {
        Map var4 = SPECS;
        BlissCoreItems.GemSpec var6 = (BlissCoreItems.GemSpec)var4.get(var0);
        if (var6 == null) {
            return List.of();
        } else {
            ArrayList var15 = new ArrayList();
            ArrayList var7 = var15;
            boolean var8 = false;

            for (String var10 : var6.commonBody) {
                if (!var8) {
                    if (var10.contains(var1)) {
                        var8 = true;
                    }
                } else {
                    if (var10.contains(var2)) {
                        break;
                    }

                    if (var10.contains("-")) {
                        var7.add(var10);
                    }
                }
            }

            return var7;
        }
    }

    public static int gemIdOf(ItemStack var0, int var1) {
        if (var0 == null) {
            return -1;
        } else {
            ItemMeta var5 = var0.getItemMeta();
            if (var5 == null) {
                return -1;
            } else {
                PersistentDataContainer var12 = var5.getPersistentDataContainer();
                NamespacedKey var3 = KEY_GEM;
                PersistentDataType var4 = PersistentDataType.INTEGER;
                Integer var6 = (Integer)var12.get(var3, var4);
                int var18;
                if (var6 == null) {
                    var18 = -1;
                } else {
                    var18 = var6;
                }

                return var18;
            }
        }
    }

    public static boolean isStrengthGem(ItemStack var0, int var1) {
        int var4 = gemIdOf(var0, 382441079);
        int var6 = GemType.STRENGTH.id();
        boolean var5;
        if (var4 == var6) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    public static boolean isFireGem(ItemStack var0, int var1) {
        int var4 = gemIdOf(var0, 382441079);
        int var6 = GemType.FIRE.id();
        boolean var5;
        if (var4 == var6) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    public static int tierOf(ItemStack var0, int var1) {
        if (var0 == null) {
            return -1;
        } else {
            ItemMeta var5 = var0.getItemMeta();
            if (var5 == null) {
                return -1;
            } else {
                PersistentDataContainer var12 = var5.getPersistentDataContainer();
                NamespacedKey var3 = KEY_TIER;
                PersistentDataType var4 = PersistentDataType.INTEGER;
                Integer var6 = (Integer)var12.get(var3, var4);
                int var16;
                if (var6 == null) {
                    var16 = -1;
                } else {
                    var16 = var6;
                }

                return var16;
            }
        }
    }

    private ItemStack nautilus(String var1, int var2, List<String> var3, int var4) {
        Material var6 = Material.NAUTILUS_SHELL;
        ItemStack var5 = new ItemStack(var6);
        ItemMeta var8 = var5.getItemMeta();
        Component var20 = noItalic(Text.legacy(var1, 373192400), 599169492);
        var8.displayName(var20);
        if (var3 != null) {
            Stream<String> var22 = var3.stream();
            Function<String, Component> var28 = var0 -> noItalic(Text.legacy(var0, 373192400), 599169492);
            List<Component> var24 = var22.map(var28).toList();
            var8.lore(var24);
        }

        Integer var26 = var2;
        var8.setCustomModelData(var26);
        var5.setItemMeta(var8);
        return var5;
    }

    private static Component noItalic(Component var0, int var1) {
        TextDecoration var3 = TextDecoration.ITALIC;
        return var0.decoration(var3, false);
    }

    public boolean isCmd(ItemStack var1, Material var2, int var3, int var4) {
        if (var1 != null) {
            Material var9 = var1.getType();
            if (var9 == var2) {
                ItemMeta var7 = var1.getItemMeta();
                if (var7 != null && var7.hasCustomModelData()) {
                    int var16 = var7.getCustomModelData();
                    if (var16 == var3) {
                        return true;
                    }
                }

                return false;
            }
        }

        return false;
    }

    public boolean isTrader(ItemStack var1, int var2) {
        if (var1 != null) {
            Material var6 = var1.getType();
            Material var4 = Material.PLAYER_HEAD;
            if (var6 == var4) {
                String var10 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY2MjRhNDRlNzdiOTVkYmUxY2M3MzU1MzY5NTNlODQwYmE2YzE5YjAxNTdjNTQ5ZDliODI1MzQ2NDhjOGNlNCJ9fX0=";
                if (matchesProfileTexture(var1, var10, 1703716950)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isUpgrader(ItemStack var1, int var2) {
        if (var1 != null) {
            Material var6 = var1.getType();
            Material var4 = Material.PLAYER_HEAD;
            if (var6 == var4) {
                String var10 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk5MmU0YThmNDNjNzdhYzJlMmQ3OTJkOTYwNGY2MWYzNDM4N2M4NzdhYmEzNzEyZTQwNDdiNzQwYzU3OCJ9fX0=";
                if (matchesProfileTexture(var1, var10, 1703716950)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isUtilityHead(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var5 = var0.getType();
            Material var3 = Material.PLAYER_HEAD;
            if (var5 == var3) {
                String var12 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY2MjRhNDRlNzdiOTVkYmUxY2M3MzU1MzY5NTNlODQwYmE2YzE5YjAxNTdjNTQ5ZDliODI1MzQ2NDhjOGNlNCJ9fX0=";
                if (!matchesProfileTexture(var0, var12, 1703716950)) {
                    String var13 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk5MmU0YThmNDNjNzdhYzJlMmQ3OTJkOTYwNGY2MWYzNDM4N2M4NzdhYmEzNzEyZTQwNDdiNzQwYzU3OCJ9fX0=";
                    if (!matchesProfileTexture(var0, var13, 1703716950)) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public boolean isBottledEnergy(ItemStack var1, int var2) {
        Material var5 = Material.NAUTILUS_SHELL;
        return this.isCmd(var1, var5, 300, 1284675036);
    }

    public boolean isRepairKit(ItemStack var1, int var2) {
        Material var5 = Material.NAUTILUS_SHELL;
        return this.isCmd(var1, var5, 200, 1284675036);
    }

    public boolean isRestoration(ItemStack var1, int var2) {
        Material var5 = Material.NAUTILUS_SHELL;
        return this.isCmd(var1, var5, 210, 1284675036);
    }

    public boolean isFragment(ItemStack var1, int var2) {
        Material var5 = Material.NAUTILUS_SHELL;
        return this.isCmd(var1, var5, 240, 1284675036);
    }

    private static boolean matchesProfileTexture(ItemStack var0, String var1, int var2) {
        if (var0.getItemMeta() instanceof SkullMeta var5) {
            PlayerProfile var36 = var5.getPlayerProfile();
            if (var36 == null) {
                return false;
            } else {
                for (ProfileProperty var8 : var36.getProperties()) {
                    String var26 = "textures";
                    String var33 = var8.getName();
                    if (var26.equals(var33)) {
                        String var35 = var8.getValue();
                        if (var1.equals(var35)) {
                            return true;
                        }
                    }
                }

                return false;
            }
        } else {
            return false;
        }
    }

    static {
        AtomicLong var31 = new AtomicLong();
        ENERGY_NONCE_SEQ = var31;
        String var1 = "§e§lꜱᴘᴇᴇᴅ <##FFD773>ɢᴇᴍ";
        String[] var68 = new String[12];
        String var5 = "&f&lᴡᴀᴛᴄʜ ᴛʜᴇ ᴡᴏʀʟᴅ ᴀʀᴏᴜɴᴅ ʏᴏᴜ ᴛᴜʀɴ ɪɴᴛᴏ ᴀ ʙʟᴜʀ ";
        var68[0] = var5;
        String var124 = "&f";
        var68[1] = var124;
        String var125 = "&7<##FEFD17>\ud83d\udd2e <##FFE4AB>ᴘᴀssɪᴠᴇs";
        var68[2] = var125;
        String var126 = "&7- Speed";
        var68[3] = var126;
        String var127 = "&7- Dolphins grace";
        var68[4] = var127;
        String var128 = "&7- Enchants Efficiency";
        var68[5] = var128;
        String var129 = "&7- Enchants Soul Speed";
        var68[6] = var129;
        String var130 = "&f";
        var68[7] = var130;
        String var131 = "<##FEFD17>\ud83d\udd2e <##82F3FF>&lᴀʙɪʟɪᴛʏ";
        var68[8] = var131;
        String var132 = "&7- <##FEFD17> Terminal Velocity";
        var68[9] = var132;
        String var133 = "&f";
        var68[10] = var133;
        String var134 = "<##FEFD17>\ud83d\udd2e <##B8FFFB>&lᴘᴏᴡᴇʀs";
        var68[11] = var134;
        List var69 = List.of(var68);
        String var78 = "&7- &f\ud83c\udfaf <##FFE86E>ʙʟᴜʀ";
        String var99 = "&f";
        String var135 = "&7- \ud83c\udf29 <##61FFEA>sᴘᴇᴇᴅʏ sᴛᴏʀᴍ &4\ud83e\uddd1\ud83c\udffb";
        String var6 = "&7- \ud83c\udf29 <##61FFEA>sᴘᴇᴇᴅʏ sᴛᴏʀᴍ &4\ud83e\udd3c";
        List var79 = List.of(var78, var99, var135, var6);
        BlissCoreItems.GemSpec var33 = new BlissCoreItems.GemSpec(var1, 8, var69, var79);
        LEGACY_SPEED_SPEC = var33;
        EnumMap var34 = new EnumMap<>(GemType.class);
        SPECS = var34;
        Map var35 = SPECS;
        GemType var51 = GemType.STRENGTH;
        String var70 = "&4&lꜱᴛʀᴇɴɢᴛʜ <##FFD773>ɢᴇᴍ";
        String[] var101 = new String[11];
        String var7 = "&f&lʜᴀᴠᴇ ᴛʜᴇ sᴛʀᴇɴɢᴛʜ ᴏғ ᴀ ᴀʀᴍʏ";
        var101[0] = var7;
        String var261 = "&f";
        var101[1] = var261;
        String var262 = "<##F10303>\ud83d\udd2e <##FFE4AB>ᴘᴀssɪᴠᴇs";
        var101[2] = var262;
        String var263 = "&7- &7Strength";
        var101[3] = var263;
        String var264 = "&7- Enchants Sharpness";
        var101[4] = var264;
        String var265 = "&7- Bloodthorns";
        var101[5] = var265;
        String var266 = "&f";
        var101[6] = var266;
        String var267 = "<##F10303>\ud83d\udd2e <##82F3FF>&lᴀʙɪʟɪᴛʏ";
        var101[7] = var267;
        String var268 = "&7- <##F10303>Bounty Hunter";
        var101[8] = var268;
        String var269 = "&f";
        var101[9] = var269;
        String var270 = "<##F10303>\ud83d\udd2e <##B8FFFB>&lᴘᴏᴡᴇʀs";
        var101[10] = var270;
        List var102 = List.of(var101);
        String var136 = "&7- &f\ud83e\udd3a <##B5B5B5>ғʀᴀɪʟᴇʀ &4\ud83e\uddd1\ud83c\udffb";
        String var163 = "&7- &f\ud83e\udd3a <##B5B5B5>ɴᴜʟʟɪғʏ &4\ud83e\udd3c";
        String var271 = "&f";
        String var8 = "&7- &f⚔<##910D0D> ᴄʜᴀᴅ sᴛʀᴇɴɢᴛʜ &a\ud83e\uddd1\ud83c\udffb";
        String var9 = "&7- &f⚔<##910D0D> ᴄʜᴀᴅ sᴛʀᴇɴɢᴛʜ &a\ud83e\udd3c";
        List var137 = List.of(var136, var163, var271, var8, var9);
        BlissCoreItems.GemSpec var60 = new BlissCoreItems.GemSpec(var70, 10, var102, var137);
        var35.put(var51, var60);
        Map var37 = SPECS;
        GemType var52 = GemType.FIRE;
        String var71 = "§6§lꜰɪʀᴇ <##FFD773>ɢᴇᴍ";
        String[] var104 = new String[12];
        String var272 = "&f&lᴍᴀɴɪᴘᴜʟᴀᴛᴇ ғɪʀᴇ";
        var104[0] = var272;
        String var273 = "&f";
        var104[1] = var273;
        String var274 = "<##FE8120>\ud83d\udd2e <##FFE4AB>ᴘᴀssɪᴠᴇs";
        var104[2] = var274;
        String var275 = "&7- Fire Resistance";
        var104[3] = var275;
        String var276 = "&7- Autosmelt";
        var104[4] = var276;
        String var277 = "&7- Flamestrike";
        var104[5] = var277;
        String var278 = "&7- Fireshot";
        var104[6] = var278;
        String var279 = "&f ";
        var104[7] = var279;
        String var280 = "<##FE8120>\ud83d\udd2e <##82F3FF>&lᴀʙɪʟɪᴛʏ";
        var104[8] = var280;
        String var281 = "&7- <##FE8120>Crisp";
        var104[9] = var281;
        String var282 = "&f";
        var104[10] = var282;
        String var283 = "<##FE8120>\ud83d\udd2e <##B8FFFB>&lᴘᴏᴡᴇʀs";
        var104[11] = var283;
        List var105 = List.of(var104);
        String var138 = "&7-&f\ud83e\udde8 <##FF5F33>ғɪʀᴇʙᴀʟʟ &4\ud83e\uddd1\ud83c\udffb";
        String var176 = "&7-&f\ud83e\udde8<##FF5F33> ᴍᴇᴛᴇᴏʀ ꜱʜᴏᴡᴇʀ &4\ud83e\udd3c";
        String var284 = "&f";
        String var369 = "&7-&f\ud83e\udd7e<##248FD1> ᴄᴏᴢʏ Cᴀᴍᴘғɪʀᴇ";
        List var139 = List.of(var138, var176, var284, var369);
        BlissCoreItems.GemSpec var61 = new BlissCoreItems.GemSpec(var71, 2, var105, var139);
        var37.put(var52, var61);
        Map var39 = SPECS;
        GemType var53 = GemType.WEALTH;
        String var72 = "§2§lᴡᴇᴀʟᴛʜ <##FFD773>ɢᴇᴍ";
        String[] var107 = new String[17];
        String var285 = "&f&lғᴜᴇʟ ᴀ ᴇᴍᴘɪʀᴇ";
        var107[0] = var285;
        String var286 = "&f";
        var107[1] = var286;
        String var287 = "<##0EC912>\ud83d\udd2e <##FFE4AB>ᴘᴀssɪᴠᴇs";
        var107[2] = var287;
        String var288 = "&7- Hero of the Village";
        var107[3] = var288;
        String var289 = "&7- Luck";
        var107[4] = var289;
        String var290 = "&7- Enchants Mending";
        var107[5] = var290;
        String var291 = "&7- Enchants Fortune";
        var107[6] = var291;
        String var292 = "&7- Enchants Looting";
        var107[7] = var292;
        String var293 = "&7- Bonus Ores";
        var107[8] = var293;
        String var294 = "&7- Extra EXP";
        var107[9] = var294;
        String var295 = "&7- Durability Chip";
        var107[10] = var295;
        String var296 = "&7- Double Debris";
        var107[11] = var296;
        String var297 = "&f";
        var107[12] = var297;
        String var298 = "<##0EC912>\ud83d\udd2e <##82F3FF>&lᴀʙɪʟɪᴛʏ";
        var107[13] = var298;
        String var299 = "&7- <##0EC912>Pockets";
        var107[14] = var299;
        String var300 = "&f";
        var107[15] = var300;
        String var301 = "<##0EC912>\ud83d\udd2e <##B8FFFB>&lᴘᴏᴡᴇʀs";
        var107[16] = var301;
        List var108 = List.of(var107);
        String var140 = "&7-&f\ud83c\udf40 &cᴜɴғᴏʀᴛᴜɴᴇ &4\ud83e\uddd1\ud83c\udffb";
        String var194 = "&7-&f\ud83c\udf40 &cɪᴛᴇᴍ ʟᴏᴄᴋ &4\ud83e\udd3c";
        String var302 = "&f";
        String var370 = "&7-&f\ud83d\udcb8 <##FFC642>ʀɪᴄʜ ʀᴜsʜ &a\ud83e\uddd1\ud83c\udffb";
        String var375 = "&7-&f\ud83d\udcb8 <##FFC642>ᴀᴍʟɪꜰɪᴄᴀᴛɪᴏɴ &a\ud83e\udd3c";
        List var141 = List.of(var140, var194, var302, var370, var375);
        BlissCoreItems.GemSpec var62 = new BlissCoreItems.GemSpec(var72, 12, var108, var141);
        var39.put(var53, var62);
        Map var41 = SPECS;
        GemType var54 = GemType.SPEED;
        String var73 = "§e§lꜱᴘᴇᴇᴅ <##FFD773>ɢᴇᴍ";
        String[] var110 = new String[12];
        String var303 = "&f&lᴡᴀᴛᴄʜ ᴛʜᴇ ᴡᴏʀʟᴅ ᴀʀᴏᴜɴᴅ ʏᴏᴜ ᴛᴜʀɴ ɪɴᴛᴏ ᴀ ʙʟᴜʀ ";
        var110[0] = var303;
        String var304 = "&f";
        var110[1] = var304;
        String var305 = "&7<##FEFD17>\ud83d\udd2e <##FFE4AB>ᴘᴀssɪᴠᴇs";
        var110[2] = var305;
        String var306 = "&7- Speed";
        var110[3] = var306;
        String var307 = "&7- Dolphins grace";
        var110[4] = var307;
        String var308 = "&7- Enchants Efficiency";
        var110[5] = var308;
        String var309 = "&7- Enchants Soul Speed";
        var110[6] = var309;
        String var310 = "&f";
        var110[7] = var310;
        String var311 = "<##FEFD17>\ud83d\udd2e <##82F3FF>&lᴀʙɪʟɪᴛʏ";
        var110[8] = var311;
        String var312 = "&7- <##FEFD17> Thunder Step";
        var110[9] = var312;
        String var313 = "&f";
        var110[10] = var313;
        String var314 = "<##FEFD17>\ud83d\udd2e <##B8FFFB>&lᴘᴏᴡᴇʀs";
        var110[11] = var314;
        List var111 = List.of(var110);
        String var142 = "&7- &f\ud83c\udfaf <##FFE86E>ʙʟᴜʀ";
        String var207 = "&f";
        String var315 = "&7- \ud83c\udf29 <##61FFEA>sᴘᴇᴇᴅ sᴛᴏʀᴍ";
        List var143 = List.of(var142, var207, var315);
        BlissCoreItems.GemSpec var63 = new BlissCoreItems.GemSpec(var73, 8, var111, var143);
        var41.put(var54, var63);
        Map var43 = SPECS;
        GemType var55 = GemType.PUFF;
        String var74 = "§f§lᴘᴜꜰꜰ <##FFD773>ɢᴇᴍ";
        String[] var113 = new String[13];
        String var316 = "&f&lʙᴇ ᴛʜᴇ ʙɪɢɢᴇsᴛ ʙɪʀᴅ";
        var113[0] = var316;
        String var317 = "&f";
        var113[1] = var317;
        String var318 = "&f\ud83d\udd2e <##FFE4AB>ᴘᴀssɪᴠᴇs";
        var113[2] = var318;
        String var319 = "&7- Fall Damage immunity";
        var113[3] = var319;
        String var320 = "&7- Enchants Power";
        var113[4] = var320;
        String var321 = "&7- Enchants Punch";
        var113[5] = var321;
        String var322 = "&7- Sculk Silence";
        var113[6] = var322;
        String var323 = "&7- Crop Tramp-Less";
        var113[7] = var323;
        String var324 = "&f";
        var113[8] = var324;
        String var325 = "&f\ud83d\udd2e <##82F3FF>&lᴀʙɪʟɪᴛʏ";
        var113[9] = var325;
        String var326 = "&7- &fDouble Jump";
        var113[10] = var326;
        String var327 = "&f";
        var113[11] = var327;
        String var328 = "&f\ud83d\udd2e <##B8FFFB>&lᴘᴏᴡᴇʀs";
        var113[12] = var328;
        List var114 = List.of(var113);
        String var144 = "&7- &f☁ ʙʀᴇᴇᴢʏ ʙᴀsʜ &4\ud83e\uddd1\ud83c\udffb";
        String var221 = "&7- &f☁ ʙʀᴇᴇᴢʏ ʙᴀsʜ &4\ud83e\udd3c";
        String var329 = "&f";
        String var371 = "&7- &f⏫ ᴅᴀsʜ";
        List var145 = List.of(var144, var221, var329, var371);
        BlissCoreItems.GemSpec var64 = new BlissCoreItems.GemSpec(var74, 6, var114, var145);
        var43.put(var55, var64);
        Map var45 = SPECS;
        GemType var56 = GemType.ASTRA;
        String var75 = "§5§lᴀꜱᴛʀᴀ <##FFD773>ɢᴇᴍ";
        String[] var116 = new String[11];
        String var330 = "&f&lᴍᴀɴᴀɢᴇ ᴛʜᴇ ᴛɪᴅᴇs ᴏғ ᴛʜᴇ ᴄᴏsᴍᴏs ";
        var116[0] = var330;
        String var331 = "<##A01FFF>\ud83d\udd2e <##FFE4AB>ᴘᴀssɪᴠᴇs";
        var116[1] = var331;
        String var332 = "&7- Phasing";
        var116[2] = var332;
        String var333 = "&7- Soul Healing";
        var116[3] = var333;
        String var334 = "&7- Soul Capture";
        var116[4] = var334;
        String var335 = "<##A01FFF>";
        var116[5] = var335;
        String var336 = "&7";
        var116[6] = var336;
        String var337 = "<##A01FFF>\ud83d\udd2e <##82F3FF>&lᴀʙɪʟɪᴛʏ";
        var116[7] = var337;
        String var338 = "&7- <##A01FFF>Dimensional Drift";
        var116[8] = var338;
        String var339 = "&f";
        var116[9] = var339;
        String var340 = "<##A01FFF>\ud83d\udd2e <##B8FFFB>&lᴘᴏᴡᴇʀs";
        var116[10] = var340;
        List var117 = List.of(var116);
        String var146 = "&7- &f\ud83d\udd2a <##BFB8B8>ᴅᴀɢɢᴇʀs &4\ud83e\uddd1\ud83c\udffb";
        String var233 = "&7- &f\ud83d\udd2a &7ᴜɴʙᴏᴜɴᴅᴇᴅ &4\ud83e\udd3c";
        String var341 = "&f";
        String var372 = "&7- &f\ud83d\udc7b <##AABBBF>ᴀsᴛʀᴀʟ ᴘʀᴏᴊᴇᴄᴛɪᴏɴ &a\ud83e\uddd1\ud83c\udffb";
        String var376 = "&7- &f\ud83d\udc7b <##AABBBF>ᴀsᴛʀᴀʟ ᴠᴏɪᴅ &a\ud83e\udd3c";
        List var147 = List.of(var146, var233, var341, var372, var376);
        BlissCoreItems.GemSpec var65 = new BlissCoreItems.GemSpec(var75, 14, var117, var147);
        var45.put(var56, var65);
        Map var47 = SPECS;
        GemType var57 = GemType.LIFE;
        String var76 = "§d§lʟɪꜰᴇ <##FFD773>ɢᴇᴍ";
        String[] var119 = new String[13];
        String var342 = "&f&lᴄᴏɴᴛʀᴏʟ ᴛʜᴇ ʙᴀʟᴀɴᴄᴇ ᴏғ ʟɪғᴇ";
        var119[0] = var342;
        String var343 = "&f";
        var119[1] = var343;
        String var344 = "<##FE04B4>\ud83d\udd2e <##FFE4AB>ᴘᴀssɪᴠᴇs";
        var119[2] = var344;
        String var345 = "&7- Green Thumb";
        var119[3] = var345;
        String var346 = "&7- Radiant Fist";
        var119[4] = var346;
        String var347 = "&7- Bonus Saturation";
        var119[5] = var347;
        String var348 = "&7- Bonus Absorption";
        var119[6] = var348;
        String var349 = "&7- Wither Immune";
        var119[7] = var349;
        String var350 = "&f";
        var119[8] = var350;
        String var351 = "<##FE04B4>\ud83d\udd2e <##82F3FF>&lᴀʙɪʟɪᴛʏ";
        var119[9] = var351;
        String var352 = "&7- <##FE04B4>Vitality Vortex";
        var119[10] = var352;
        String var353 = "&f";
        var119[11] = var353;
        String var354 = "<##FE04B4>\ud83d\udd2e <##B8FFFB>&lᴘᴏᴡᴇʀs";
        var119[12] = var354;
        List var120 = List.of(var119);
        String var148 = "&7- &f\ud83d\udc98<##FF429A> ʜᴇᴀʀᴛ ᴅʀᴀɪɴᴇʀ &4\ud83e\uddd1\ud83c\udffb";
        String var247 = "&7- &f\ud83d\udc98<##FF429A> ʜᴇᴀʀᴛʟᴏᴄᴋ &4\ud83e\udd3c";
        String var355 = "&f";
        String var373 = "&7- &f\ud83d\udc96<##B8FFFA> ᴄɪʀᴄʟᴇ ᴏғ ʟɪғᴇ &a\ud83e\uddd1\ud83c\udffb";
        String var377 = "&7- &f\ud83d\udc96<##B8FFFA> ᴄɪʀᴄʟᴇ ᴏғ ʟɪғᴇ &a\ud83e\udd3c";
        List var149 = List.of(var148, var247, var355, var373, var377);
        BlissCoreItems.GemSpec var66 = new BlissCoreItems.GemSpec(var76, 4, var120, var149);
        var47.put(var57, var66);
        Map var49 = SPECS;
        GemType var58 = GemType.FLUX;
        String var77 = "<##009ac9>§lꜰʟᴜx <##FFD773>ɢᴇᴍ";
        String[] var122 = new String[12];
        String var356 = "&f&lᴇᴠᴇʀʏᴛʜɪɴɢ ɪs ᴀ ғʟᴜᴄᴛᴜᴀᴛɪᴏɴ";
        var122[0] = var356;
        String var357 = "<##03EAFF>\ud83d\udd2e <##FFE4AB>ᴘᴀssɪᴠᴇs";
        var122[1] = var357;
        String var358 = "&7- Flow State";
        var122[2] = var358;
        String var359 = "&7- Shocking Chance";
        var122[3] = var359;
        String var360 = "&7- Tireless";
        var122[4] = var360;
        String var361 = "&7- Conduction";
        var122[5] = var361;
        String var362 = "&7- Charged";
        var122[6] = var362;
        String var363 = "&f";
        var122[7] = var363;
        String var364 = "<##03EAFF>\ud83d\udd2e <##82F3FF>&l ᴀʙɪʟɪᴛʏ";
        var122[8] = var364;
        String var365 = "&7- <##03EAFF>Static Burst";
        var122[9] = var365;
        String var366 = "&f";
        var122[10] = var366;
        String var367 = "<##03EAFF>\ud83d\udd2e <##B8FFFB>&lᴘᴏᴡᴇʀs";
        var122[11] = var367;
        List var123 = List.of(var122);
        String var150 = "&7- ☄ <##03EAFF>ᴇɴᴇʀɢʏ ʙᴇᴀᴍ &4\ud83e\uddd1\ud83c\udffb";
        String var260 = "&7- ☄ <##03EAFF>ɢʀᴏᴜɴᴅ &7 &4\ud83e\udd3c";
        String var368 = "&f";
        String var374 = "&7- \ud83c\udf00 <##03EAFF>Kinetic Overdrive &a\ud83e\udd3c";
        List var151 = List.of(var150, var260, var368, var374);
        BlissCoreItems.GemSpec var67 = new BlissCoreItems.GemSpec(var77, 98, var123, var151);
        var49.put(var58, var67);
    }

    private static record GemSpec(String displayName, int t2PristineCmd, List<String> commonBody, List<String> t2Powers) {
    }
}
