package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.core.GlobalState;
import com.bliss.gems.WealthGemListener;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class KitRestrictionListener implements Listener {
    private static final long SWEEP_TICKS = 10L;
    private final BlissPlugin plugin;
    private final KitRules rules;
    private final GlobalState global;
    private final Map<UUID, Long> lastWindThrow;
    private BukkitTask sweepTask;
    private static final Set<Material> CLAMP_EXEMPT;
    private static final List<NamespacedKey> IDENTITY_KEYS;
    private final Map<UUID, Integer> lastDenyTick;
    private static final int PEARL_VANILLA_COOLDOWN = 20;

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        Map var4 = this.lastWindThrow;
        UUID var10 = var1.getPlayer().getUniqueId();
        var4.remove(var10);
        Map var7 = this.lastDenyTick;
        UUID var13 = var1.getPlayer().getUniqueId();
        var7.remove(var13);
    }

    public KitRestrictionListener(BlissPlugin var1, KitRules var2, GlobalState var3, int var4) {
        ConcurrentHashMap var6 = new ConcurrentHashMap();
        this.lastWindThrow = var6;
        ConcurrentHashMap var12 = new ConcurrentHashMap();
        this.lastDenyTick = var12;
        this.plugin = var1;
        this.rules = var2;
        this.global = var3;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::sweep;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 10L, 10L);
        this.sweepTask = var10;
    }

    public void stop(int var1) {
        if (this.sweepTask != null) {
            this.sweepTask.cancel();
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onCobwebPlace(BlockPlaceEvent var1) {
        if (!this.rules.cobwebsAllowed(1748007571)) {
            Material var8 = var1.getBlockPlaced().getType();
            Material var3 = Material.COBWEB;
            if (var8 == var3) {
                var1.setCancelled(true);
                Player var11 = var1.getPlayer();
                Component var14 = Text.legacy("&cCobwebs are banned by the Honored One.", 373192400);
                var11.sendMessage(var14);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPotionDrink(PlayerItemConsumeEvent var1) {
        ItemStack var4 = var1.getItem();
        Material var8 = var4.getType();
        Material var3 = Material.ENCHANTED_GOLDEN_APPLE;
        if (var8 == var3 && !this.rules.godApplesAllowed(564954237)) {
            var1.setCancelled(true);
            Player var14 = var1.getPlayer();
            Component var25 = Text.legacy("&cGod apples are banned by the Honored One.", 373192400);
            var14.sendMessage(var25);
        } else {
            Material var16 = var4.getType();
            Material var26 = Material.POTION;
            if (var16 == var26) {
                Component var5 = this.potionDeny(var4, 342703240);
                if (var5 != null) {
                    var1.setCancelled(true);
                    Player var22 = var1.getPlayer();
                    var22.sendMessage(var5);
                }
            }
        }
    }

    @EventHandler
    public void onThrowables(PlayerInteractEvent var1) {
        if (var1.getAction().isRightClick()) {
            ItemStack var6 = var1.getItem();
            if (var6 != null) {
                Player var7 = var1.getPlayer();
                Material var8 = var6.getType();
                Material var3 = Material.SPLASH_POTION;
                if (var8 != var3) {
                    Material var63 = Material.LINGERING_POTION;
                    if (var8 != var63) {
                        Material var64 = Material.SHIELD;
                        if (var8 == var64 && !this.rules.shieldAllowed(914900028)) {
                            Result var65 = Result.DENY;
                            var1.setUseItemInHand(var65);
                            Component var81 = Text.legacy("&cShields are banned by the Honored One.", 373192400);
                            this.deny(var7, var81, 303844542);
                            return;
                        }

                        Material var67 = Material.WIND_CHARGE;
                        if (var8 == var67) {
                            switch (this.rules.wind(1317558545)) {
                                case ALLOWED:
                                default:
                                    break;
                                case BANNED:
                                    Result var72 = Result.DENY;
                                    var1.setUseItemInHand(var72);
                                    Component var83 = Text.legacy("&cWind charges are banned by the Honored One.", 373192400);
                                    this.deny(var7, var83, 303844542);
                                    break;
                                case CAPPED:
                                    long var9 = System.currentTimeMillis();
                                    Map var39 = this.lastWindThrow;
                                    UUID var75 = var7.getUniqueId();
                                    Long var11 = (Long)var39.get(var75);
                                    if (var11 != null) {
                                        long var85 = var11;
                                        if (var9 - var85 < 3000L) {
                                            Result var76 = Result.DENY;
                                            var1.setUseItemInHand(var76);
                                            long var102 = var11;
                                            long var91 = var9 - var102;
                                            long var12 = (3000L - var91 + 999L) / 1000L;
                                            Component var100 = Text.legacy("&eWind charge on cooldown for &f" + var12 + "s&e.", 373192400);
                                            this.deny(var7, var100, 303844542);
                                        }
                                    }
                            }
                        }

                        return;
                    }
                }

                Component var104 = this.potionDeny(var6, 342703240);
                if (var104 != null) {
                    Result var79 = Result.DENY;
                    var1.setUseItemInHand(var79);
                    this.deny(var7, var104, 303844542);
                }
            }
        }
    }

    private void sweep() {
        KitRules.WindMode var7 = this.rules.wind(1317558545);
        KitRules.WindMode var2 = KitRules.WindMode.CAPPED;
        boolean var8;
        if (var7 == var2) {
            var8 = true;
        } else {
            var8 = false;
        }

        boolean var3 = var8;

        for (Player var5 : Bukkit.getOnlinePlayers()) {
            this.stripBannedArmor(var5, 1284589857);
            this.clampEnchants(var5, 1811773725);
            if (var3) {
                this.trimWind(var5, 851613276);
            }

            this.trimPearls(var5, 2086104562);
        }
    }

    private void stripBannedArmor(Player var1, int var2) {
        ItemStack[] var7 = var1.getInventory().getArmorContents();
        boolean var8 = false;
        int var9 = 0;

        while (true) {
            int var89 = var7.length;
            if (var9 >= var89) {
                if (var8) {
                    PlayerInventory var24 = var1.getInventory();
                    var24.setArmorContents(var7);
                }

                return;
            }

            ItemStack var10 = var7[var9];
            if (var10 != null) {
                String var11;
                label77: {
                    var11 = null;
                    KitRules.ArmorTier var12 = KitRules.tierOf(var10.getType(), 917345542);
                    if (var12 != null) {
                        KitRules var34 = this.rules;
                        if (!var34.armorAllowed(var12, 1135935034)) {
                            var11 = var12.display + " armor is banned by the Honored One.";
                            break label77;
                        }
                    }

                    Material var58 = var10.getType();
                    Material var101 = Material.ELYTRA;
                    if (var58 == var101 && !this.rules.elytraAllowed(913902142)) {
                        var11 = "Elytra are banned by the Honored One.";
                    } else {
                        ArmorLimits var116 = this.plugin.armorLimits(1458810589);
                        ArmorLimits.Tier var117 = armorTierOf(var10.getType(), 311432424);
                        ArmorLimits.Piece var118 = armorPieceOf(var10.getType(), 37160213);
                        if (var116 != null && var117 != null && var118 != null) {
                            int var75 = var117.ordinal();
                            int var104 = var116.tier(var118, 1084990497).ordinal();
                            if (var75 > var104) {
                                String var78 = pretty(var118.name(), 476111933);
                                Locale var105 = Locale.ROOT;
                                String var16 = var78.toLowerCase(var105);
                                if (!var16.endsWith("s")) {
                                    var16 = var16 + "s";
                                }

                                String var86 = pretty(var117.name(), 476111933);
                                String var114 = pretty(var116.tier(var118, 1084990497).name(), 476111933);
                                var11 = var86 + " " + var16 + " are above the Honored One's limit (" + var114 + ").";
                            }
                        }
                    }
                }

                if (var11 != null) {
                    PlayerInventory var41 = var1.getInventory();
                    ItemStack[] var94 = new ItemStack[]{var10};

                    for (ItemStack var15 : var41.addItem(var94).values()) {
                        World var55 = var1.getWorld();
                        Location var100 = var1.getLocation();
                        var55.dropItemNaturally(var100, var15);
                    }

                    var7[var9] = null;
                    var8 = true;
                    Component var98 = Text.legacy("&c" + var11, 373192400);
                    var1.sendMessage(var98);
                }
            }

            var9++;
        }
    }

    private static ArmorLimits.Tier armorTierOf(Material var0, int var1) {
        String var5 = var0.name();
        int var6 = var5.indexOf(95);
        if (var6 < 0) {
            return null;
        } else {
            try {
                return ArmorLimits.Tier.valueOf(var5.substring(0, var6));
            } catch (IllegalArgumentException var15) {
                return null;
            }
        }
    }

    private static ArmorLimits.Piece armorPieceOf(Material var0, int var1) {
        String var5 = var0.name();
        int var6 = var5.indexOf(95);
        if (var6 < 0) {
            return null;
        } else {
            try {
                int var19 = var6 + 1;
                return ArmorLimits.Piece.valueOf(var5.substring(var19));
            } catch (IllegalArgumentException var20) {
                return null;
            }
        }
    }

    private static String pretty(String var0, int var1) {
        StringBuilder var2 = new StringBuilder();
        StringBuilder var4 = var2;
        Locale var29 = Locale.ROOT;

        for (String var8 : var0.toLowerCase(var29).split("_")) {
            if (!var8.isEmpty()) {
                if (var4.length() > 0) {
                    var4.append(' ');
                }

                char var36 = Character.toUpperCase(var8.charAt(0));
                StringBuilder var27 = var4.append(var36);
                String var38 = var8.substring(1);
                var27.append(var38);
            }
        }

        return var4.toString();
    }

    private void clampEnchants(Player var1, int var2) {
        EnchantLimits var6 = this.plugin.enchantLimits(1173102795);
        if (var6 != null) {
            int var40;
            label41: {
                WealthGemListener var7 = this.plugin.wealthGemListener(1465554246);
                if (var7 != null) {
                    UUID var41 = var1.getUniqueId();
                    if (var7.amplified(var41, 2007265206)) {
                        var40 = WealthGemListener.amplificationLevels(513507964);
                        break label41;
                    }
                }

                var40 = 0;
            }

            byte var8 = (byte)var40;
            ItemStack[] var9 = var1.getInventory().getContents();
            int var10 = 0;

            while (true) {
                int var43 = var9.length;
                if (var10 >= var43) {
                    return;
                }

                ItemStack var11 = var9[var10];
                if (var11 != null) {
                    Material var31 = var11.getType();
                    Material var45 = Material.AIR;
                    if (var31 != var45 && var11.hasItemMeta()) {
                        String var12 = clampStack(var11, var6, var8, 1680829036);
                        if (var12 != null) {
                            PlayerInventory var38 = var1.getInventory();
                            var38.setItem(var10, var11);
                            Component var50 = Text.legacy("&c" + var12, 373192400);
                            var1.sendMessage(var50);
                        }
                    }
                }

                var10++;
            }
        }
    }

    private static boolean pluginOwned(ItemStack var0, ItemMeta var1, int var2) {
        Set var3 = CLAMP_EXEMPT;
        Material var24 = var0.getType();
        if (var3.contains(var24)) {
            return true;
        } else {
            PersistentDataContainer var5 = var1.getPersistentDataContainer();

            for (NamespacedKey var7 : IDENTITY_KEYS) {
                if (var7 != null && var5.has(var7)) {
                    return true;
                }
            }

            return false;
        }
    }

    private void deny(Player var1, Component var2, int var3) {
        int var7 = Bukkit.getCurrentTick();
        Map<UUID, Integer> var10 = this.lastDenyTick;
        UUID var17 = var1.getUniqueId();
        Integer var20 = var7;
        Integer var8 = var10.put(var17, var20);
        if (var8 != null) {
            int var15 = var8;
            if (var15 == var7) {
                return;
            }
        }

        var1.sendMessage(var2);
    }

    private static String clampStack(ItemStack var0, EnchantLimits var1, int var2, int var3) {
        ItemMeta var8 = var0.getItemMeta();
        if (var8 == null) {
            return null;
        } else if (pluginOwned(var0, var8, 2081318963)) {
            return null;
        } else {
            boolean var107;
            if (!var8.hasEnchants() && (!(var8 instanceof EnchantmentStorageMeta) || !((EnchantmentStorageMeta)var8).hasStoredEnchants())) {
                var107 = false;
            } else {
                var107 = true;
            }

            if (!var107) {
                return null;
            } else {
                String var143 = null;
                boolean var11 = false;
                if (var8 instanceof EnchantmentStorageMeta var12) {
                    Map<Enchantment, Integer> var135 = var12.getStoredEnchants();
                    HashMap<Enchantment, Integer> var38 = new HashMap<>(var135);

                    for (Entry<Enchantment, Integer> var14 : var38.entrySet()) {
                        Enchantment var110 = var14.getKey();
                        int var15 = var1.cap(var110, 1602405455);
                        if (var15 >= 0) {
                            int var52 = var14.getValue();
                            if (var52 > var15) {
                                if (var2 > 0 && var15 > 0) {
                                    int var58 = var14.getValue();
                                    int var113 = var15 + var2;
                                    if (var58 <= var113) {
                                        continue;
                                    }
                                }

                                Enchantment var116 = var14.getKey();
                                var12.removeStoredEnchant(var116);
                                if (var15 > 0) {
                                    Enchantment var119 = var14.getKey();
                                    var12.addStoredEnchant(var119, var15, true);
                                }

                                var11 = true;
                                if (var143 == null) {
                                    Enchantment var68 = var14.getKey();
                                    var143 = clampNote(var68, var15, 1111583689);
                                }
                            }
                        }
                    }
                }

                Map<Enchantment, Integer> var139 = var8.getEnchants();
                HashMap<Enchantment, Integer> var70 = new HashMap<>(var139);

                for (Entry<Enchantment, Integer> var145 : var70.entrySet()) {
                    Enchantment var124 = var145.getKey();
                    int var146 = var1.cap(var124, 1602405455);
                    if (var146 >= 0) {
                        int var89 = var145.getValue();
                        if (var89 > var146) {
                            if (var2 > 0 && var146 > 0) {
                                int var95 = var145.getValue();
                                int var127 = var146 + var2;
                                if (var95 <= var127) {
                                    continue;
                                }
                            }

                            Enchantment var130 = var145.getKey();
                            var8.removeEnchant(var130);
                            if (var146 > 0) {
                                Enchantment var133 = var145.getKey();
                                var8.addEnchant(var133, var146, true);
                            }

                            var11 = true;
                            if (var143 == null) {
                                Enchantment var105 = var145.getKey();
                                var143 = clampNote(var105, var146, 1111583689);
                            }
                        }
                    }
                }

                if (!var11) {
                    return null;
                } else {
                    var0.setItemMeta(var8);
                    return var143;
                }
            }
        }
    }

    private static String clampNote(Enchantment var0, int var1, int var2) {
        String var5 = EnchantLimitHologram.prettyName(var0, 1817563720);
        String var10;
        if (var1 == 0) {
            var10 = var5 + " is banned by the Honored One.";
        } else {
            var10 = var5 + " is capped at level " + var1 + " by the Honored One.";
        }

        return var10;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onEnchantTable(EnchantItemEvent var1) {
        EnchantLimits var4 = this.plugin.enchantLimits(1173102795);
        if (var4 != null) {
            Map<Enchantment, Integer> var5 = var1.getEnchantsToAdd();
            HashMap<Enchantment, Integer> var14 = new HashMap<>(var5);

            for (Entry<Enchantment, Integer> var7 : var14.entrySet()) {
                Enchantment var42 = (Enchantment)var7.getKey();
                int var8 = var4.cap(var42, 1602405455);
                if (var8 >= 0) {
                    int var31 = (Integer)var7.getValue();
                    if (var31 > var8) {
                        if (var8 == 0) {
                            Object var45 = var7.getKey();
                            var5.remove(var45);
                        } else {
                            Enchantment var54 = (Enchantment)var7.getKey();
                            Integer var57 = var8;
                            var5.put(var54, var57);
                        }

                        Player var36 = var1.getEnchanter();
                        Enchantment var48 = (Enchantment)var7.getKey();
                        Component var51 = Text.legacy("&c" + clampNote(var48, var8, 1111583689), 373192400);
                        var36.sendMessage(var51);
                    }
                }
            }

            if (var5.isEmpty()) {
                var1.setCancelled(true);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onAnvil(PrepareAnvilEvent var1) {
        EnchantLimits var5 = this.plugin.enchantLimits(1173102795);
        if (var5 != null) {
            ItemStack var6 = var1.getResult();
            if (var6 != null) {
                Material var15 = var6.getType();
                Material var3 = Material.AIR;
                if (var15 != var3) {
                    ItemStack var7 = var6.clone();
                    if (clampStack(var7, var5, 0, 1680829036) != null) {
                        var1.setResult(var7);
                    }

                    return;
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onVillagerClick(PlayerInteractEntityEvent var1) {
        if (!this.rules.villagersAllowed(1843282219)) {
            Entity var5 = var1.getRightClicked();
            if (var5 instanceof Villager) {
                if (!((Villager)var5).getScoreboardTags().contains("BlissEventVillager")) {
                    var1.setCancelled(true);
                    EquipmentSlot var19 = var1.getHand();
                    EquipmentSlot var23 = EquipmentSlot.HAND;
                    if (var19 == var23) {
                        Player var21 = var1.getPlayer();
                        Component var25 = Text.legacy("&cVillager trading is banned by the Honored One.", 373192400);
                        var21.sendMessage(var25);
                    }
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onMerchantOpen(InventoryOpenEvent var1) {
        if (!this.rules.villagersAllowed(1843282219)) {
            Inventory var5 = var1.getInventory();
            if (var5 instanceof MerchantInventory) {
                if (((MerchantInventory)var5).getMerchant() instanceof Villager var26) {
                    if (!var26.getScoreboardTags().contains("BlissEventVillager")) {
                        var1.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onWindThrow(ProjectileLaunchEvent var1) {
        Projectile var7 = var1.getEntity();
        if (var7 instanceof WindCharge) {
            if (((WindCharge)var7).getShooter() instanceof Player var29) {
                KitRules.WindMode var22 = this.rules.wind(1317558545);
                KitRules.WindMode var3 = KitRules.WindMode.CAPPED;
                if (var22 == var3) {
                    Map var24 = this.lastWindThrow;
                    UUID var27 = var29.getUniqueId();
                    Long var28 = System.currentTimeMillis();
                    var24.put(var27, var28);
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPearlThrow(ProjectileLaunchEvent var1) {
        Projectile var8 = var1.getEntity();
        if (var8 instanceof EnderPearl) {
            if (((EnderPearl)var8).getShooter() instanceof Player var49) {
                double var50 = this.rules.pearlCooldownMult(1963696175);
                if (!(Math.abs(var50 - 1.0) < 1.0E-9)) {
                    int var10 = (int)Math.round(20.0 * var50);
                    BukkitScheduler var40 = Bukkit.getScheduler();
                    BlissPlugin var42 = this.plugin;
                    Runnable var48 = () -> {
                        if (var49.isOnline()) {
                            Material var3 = Material.ENDER_PEARL;
                            var49.setCooldown(var3, var10);
                        }
                    };
                    var40.runTask(var42, var48);
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPearlPickup(EntityPickupItemEvent var1) {
        if (var1.getEntity() instanceof Player var4) {
            Material var14 = var1.getItem().getItemStack().getType();
            Material var3 = Material.ENDER_PEARL;
            if (var14 == var3) {
                int var25 = this.rules.pearlLimit(782575185);
                if (var25 != -1) {
                    int var20 = countPearls(var4, 1461187484);
                    if (var20 >= var25) {
                        var1.setCancelled(true);
                    }
                }
            }
        }
    }

    private static int countPearls(Player var0, int var1) {
        int var4 = 0;

        for (ItemStack var8 : var0.getInventory().getContents()) {
            if (var8 != null) {
                Material var20 = var8.getType();
                Material var25 = Material.ENDER_PEARL;
                if (var20 == var25) {
                    int var27 = var8.getAmount();
                    var4 += var27;
                }
            }
        }

        return var4;
    }

    private void trimPearls(Player var1, int var2) {
        int var8 = this.rules.pearlLimit(782575185);
        if (var8 != -1) {
            int var18 = countPearls(var1, 1461187484);
            int var9 = var18 - var8;
            if (var9 > 0) {
                ItemStack[] var10 = var1.getInventory().getContents();

                for (int var11 = var10.length - 1; var11 >= 0 && var9 > 0; var11 += -1) {
                    ItemStack var12 = var10[var11];
                    if (var12 != null) {
                        Material var33 = var12.getType();
                        Material var48 = Material.ENDER_PEARL;
                        if (var33 == var48) {
                            int var50 = var12.getAmount();
                            int var13 = Math.min(var9, var50);
                            int var52 = var12.getAmount();
                            int var53 = var52 - var13;
                            var12.setAmount(var53);
                            PlayerInventory var38 = var1.getInventory();
                            ItemStack var64;
                            if (var12.getAmount() <= 0) {
                                var64 = null;
                            } else {
                                var64 = var12;
                            }

                            var38.setItem(var11, var64);
                            World var40 = var1.getWorld();
                            Location var56 = var1.getLocation();
                            Material var6 = Material.ENDER_PEARL;
                            ItemStack var63 = new ItemStack(var6, var13);
                            var40.dropItemNaturally(var56, var63);
                            var9 -= var13;
                        }
                    }
                }

                Component var60 = Text.legacy("&eEnder pearls are limited to &f" + var8 + "&e by the Honored One.", 373192400);
                var1.sendMessage(var60);
            }
        }
    }

    private void trimWind(Player var1, int var2) {
        int var6 = 0;
        ItemStack[] var7 = var1.getInventory().getContents();

        for (ItemStack var11 : var7) {
            if (var11 != null) {
                Material var24 = var11.getType();
                Material var51 = Material.WIND_CHARGE;
                if (var24 == var51) {
                    int var53 = var11.getAmount();
                    var6 += var53;
                }
            }
        }

        if (var6 > 16) {
            int var69 = var6 - 16;

            for (int var70 = var7.length - 1; var70 >= 0 && var69 > 0; var70 += -1) {
                ItemStack var71 = var7[var70];
                if (var71 != null) {
                    Material var39 = var71.getType();
                    Material var58 = Material.WIND_CHARGE;
                    if (var39 == var58) {
                        int var60 = var71.getAmount();
                        int var72 = Math.min(var69, var60);
                        int var62 = var71.getAmount();
                        int var63 = var62 - var72;
                        var71.setAmount(var63);
                        if (var71.getAmount() <= 0) {
                            PlayerInventory var46 = var1.getInventory();
                            var46.setItem(var70, null);
                        }

                        var69 -= var72;
                    }
                }
            }

            Component var67 = Text.legacy("&eWind charges are capped at &f16&e.", 373192400);
            var1.sendMessage(var67);
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onDispense(BlockDispenseEvent var1) {
        ItemStack var4 = var1.getItem();
        Material var5 = var4.getType();
        Material var3 = Material.SPLASH_POTION;
        if (var5 != var3) {
            Material var19 = Material.LINGERING_POTION;
            if (var5 != var19) {
                Material var22 = Material.WIND_CHARGE;
                if (var5 == var22) {
                    KitRules.WindMode var17 = this.rules.wind(1317558545);
                    KitRules.WindMode var23 = KitRules.WindMode.ALLOWED;
                    if (var17 != var23) {
                        var1.setCancelled(true);
                    }
                }

                return;
            }
        }

        if (this.potionDeny(var4, 342703240) != null) {
            var1.setCancelled(true);
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onTippedArrow(ProjectileLaunchEvent var1) {
        if (var1.getEntity() instanceof Arrow var5) {
            Component var36;
            if (!this.arrowAllowed(var5, 1711817750)) {
                var36 = this.potionDenyMessage(938596129);
            } else {
                PotionType var31 = var5.getBasePotionType();
                List var34 = var5.getCustomEffects();
                var36 = this.limitDeny(var31, var34, 561817616);
            }

            if (var36 != null) {
                var1.setCancelled(true);
                if (var5.getShooter() instanceof Player var27) {
                    var27.sendMessage(var36);
                }
            }
        }
    }

    private Component potionDeny(ItemStack var1, int var2) {
        if (!this.potionAllowed(var1, 772453139)) {
            return this.potionDenyMessage(938596129);
        } else if (var1.getItemMeta() instanceof PotionMeta var6) {
            PotionType var21 = var6.getBasePotionType();
            List var22 = var6.getCustomEffects();
            return this.limitDeny(var21, var22, 561817616);
        } else {
            return null;
        }
    }

    private Component limitDeny(PotionType var1, List<PotionEffect> var2, int var3) {
        PotionLimits var5 = this.plugin.potionLimits(1154581152);
        if (var5 == null) {
            return null;
        } else {
            ArrayList<PotionEffect> var15 = new ArrayList<>();
            if (var1 != null) {
                List<PotionEffect> var49 = var1.getPotionEffects();
                var15.addAll(var49);
            }

            if (var2 != null) {
                var15.addAll(var2);
            }

            for (PotionEffect var8 : var15) {
                PotionEffectType var52 = var8.getType();
                int var9 = var5.cap(var52, 1961378735);
                if (var9 >= 0) {
                    String var10 = pretty(var8.getType().getKey().getKey(), 476111933);
                    if (var9 == 0) {
                        return Text.legacy("&c" + var10 + " potions are banned by the Honored One.", 373192400);
                    }

                    int var40 = var8.getAmplifier() + 1;
                    if (var40 > var9) {
                        return Text.legacy("&c" + var10 + " is capped at level " + var9 + " by the Honored One.", 373192400);
                    }
                }
            }

            return null;
        }
    }

    private boolean arrowAllowed(Arrow var1, int var2) {
        PotionType var7 = var1.getBasePotionType();
        List<PotionEffect> var8 = var1.getCustomEffects();
        boolean var58;
        if ((var7 == null || var7.getPotionEffects().isEmpty()) && var8.isEmpty()) {
            var58 = false;
        } else {
            var58 = true;
        }

        if (!var58) {
            return true;
        } else if (!this.rules.turtleMasterAllowed(865045238) && var7 != null && var7.name().contains("TURTLE_MASTER")) {
            return false;
        } else {
            return switch (this.rules.potions(1082719516)) {
                case ALL-> true;
                case NONE-> false;
                case TIER1-> {
                    int var10 = -1;
                    if (var7 != null) {
                        for (PotionEffect var12 : var7.getPotionEffects()) {
                            int var64 = var12.getAmplifier();
                            var10 = Math.max(var10, var64);
                        }
                    }

                    for (PotionEffect var69 : var8) {
                        int var67 = var69.getAmplifier();
                        var10 = Math.max(var10, var67);
                    }

                    if (var10 < 1) {
                        yield true;
                    } else {
                        yield false;
                    }
                }
                default -> {
                    MatchException var33 = new MatchException(null, null);
                    throw var33;
                }
            };
        }
    }

    private boolean potionAllowed(ItemStack var1, int var2) {
        if (var1.getItemMeta() instanceof PotionMeta var7) {
            if (!this.rules.turtleMasterAllowed(865045238) && isTurtleMaster(var7, 298140074)) {
                return false;
            } else {
                return switch (this.rules.potions(1082719516)) {
                    case ALL-> true;
                    case NONE-> {
                        if (!hasAnyEffect(var7, 1442956011)) {
                            yield true;
                        } else {
                            yield false;
                        }
                    }
                    case TIER1-> {
                        if (maxAmplifier(var7, 198230266) < 1) {
                            yield true;
                        } else {
                            yield false;
                        }
                    }
                    default -> {
                        MatchException var21 = new MatchException(null, null);
                        throw var21;
                    }
                };
            }
        } else {
            return true;
        }
    }

    private static boolean isTurtleMaster(PotionMeta var0, int var1) {
        PotionType var4 = var0.getBasePotionType();
        if (var4 != null && var4.name().contains("TURTLE_MASTER")) {
            return true;
        } else {
            Iterator var5 = var0.getCustomEffects().iterator();

            while (var5.hasNext()) {
                PotionEffectType var24 = ((PotionEffect)var5.next()).getType();
                PotionEffectType var39 = PotionEffectType.RESISTANCE;
                if (var24.equals(var39)) {
                    Iterator var7 = var0.getCustomEffects().iterator();

                    while (var7.hasNext()) {
                        PotionEffectType var35 = ((PotionEffect)var7.next()).getType();
                        PotionEffectType var40 = PotionEffectType.SLOWNESS;
                        if (var35.equals(var40)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
    }

    private static boolean hasAnyEffect(PotionMeta var0, int var1) {
        PotionType var3 = var0.getBasePotionType();
        if (var3 != null && !var3.getPotionEffects().isEmpty()) {
            return true;
        } else {
            boolean var13;
            if (!var0.getCustomEffects().isEmpty()) {
                var13 = true;
            } else {
                var13 = false;
            }

            return var13;
        }
    }

    private static int maxAmplifier(PotionMeta var0, int var1) {
        int var4 = -1;
        PotionType var5 = var0.getBasePotionType();
        if (var5 != null) {
            for (PotionEffect var36 : var5.getPotionEffects()) {
                int var34 = var36.getAmplifier();
                var4 = Math.max(var4, var34);
            }
        }

        for (PotionEffect var7 : var0.getCustomEffects()) {
            int var32 = var7.getAmplifier();
            var4 = Math.max(var4, var32);
        }

        return var4;
    }

    private Component potionDenyMessage(int var1) {
        return switch (this.rules.potions(1082719516)) {
            case ALL-> Text.legacy("&cThat potion is banned by the Honored One.", 373192400);
            case NONE-> Text.legacy("&cPotions are banned by the Honored One.", 373192400);
            case TIER1-> Text.legacy("&cTier II potions are banned by the Honored One.", 373192400);
            default -> {
                MatchException var7 = new MatchException(null, null);
                throw var7;
            }
        };
    }

    static {
        Material var0 = Material.OBSIDIAN;
        Material var1 = Material.PRISMARINE_SHARD;
        Material var2 = Material.AMETHYST_SHARD;
        Material var3 = Material.PRISMARINE_CRYSTALS;
        CLAMP_EXEMPT = EnumSet.of(var0, var1, var2, var3);
        NamespacedKey var6 = BlissCoreItems.KEY_GEM;
        NamespacedKey var8 = BlissCoreItems.KEY_RESTORATION_CORE;
        NamespacedKey var9 = GoldGem.KEY_GOLD;
        NamespacedKey var11 = NamespacedKey.fromString("bliss:mythic_blood");
        NamespacedKey var12 = NamespacedKey.fromString("bliss:mythic_aegis");
        IDENTITY_KEYS = List.of(var6, var8, var9, var11, var12);
    }
}
