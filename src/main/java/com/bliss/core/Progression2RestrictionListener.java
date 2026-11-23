package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.villager.Text;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class Progression2RestrictionListener implements Listener {
    private static final long SWEEP_TICKS = 200L;
    private final BlissPlugin plugin;
    private final GlobalState state;
    private BukkitTask sweepTask;
    private final Map<UUID, Integer> lastDenyTick;

    public Progression2RestrictionListener(BlissPlugin var1, GlobalState var2, int var3) {
        ConcurrentHashMap var5 = new ConcurrentHashMap();
        this.lastDenyTick = var5;
        this.plugin = var1;
        this.state = var2;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::sweep;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 200L, 200L);
        this.sweepTask = var10;
    }

    public void stop(int var1) {
        if (this.sweepTask != null) {
            this.sweepTask.cancel();
        }
    }

    private boolean active(World var1, int var2) {
        GlobalState var4 = GlobalState.forWorld(var1, 728926677);
        boolean var10;
        if (var4.prog2(1337683067) && var4.progKitLimit(1635426977)) {
            var10 = true;
        } else {
            var10 = false;
        }

        return var10;
    }

    private boolean enchantActive(World var1, int var2) {
        GlobalState var4 = GlobalState.forWorld(var1, 728926677);
        boolean var10;
        if (var4.prog2(1337683067) && var4.progEnchantLimit(1344216646)) {
            var10 = true;
        } else {
            var10 = false;
        }

        return var10;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        Map var4 = this.lastDenyTick;
        UUID var7 = var1.getPlayer().getUniqueId();
        var4.remove(var7);
    }

    private void deny(Player var1, String var2, int var3) {
        int var7 = Bukkit.getCurrentTick();
        Map<UUID, Integer> var10 = this.lastDenyTick;
        UUID var17 = var1.getUniqueId();
        Integer var21 = var7;
        Integer var8 = var10.put(var17, var21);
        if (var8 != null) {
            int var15 = var8;
            if (var15 == var7) {
                return;
            }
        }

        Component var20 = Text.legacy(var2, 373192400);
        var1.sendMessage(var20);
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onConsume(PlayerItemConsumeEvent var1) {
        World var18 = var1.getPlayer().getWorld();
        if (this.active(var18, 1358355888)) {
            ItemStack var5 = var1.getItem();
            if (Progression2Rules.potionBanned(var5, 1686183073)) {
                var1.setCancelled(true);
                Player var24 = var1.getPlayer();
                String var25 = "&cOnly invisibility potions are allowed on Progression 2.";
                this.deny(var24, var25, 1064088254);
            } else {
                if (Progression2Rules.saturationStew(var5, 246340976)) {
                    var1.setCancelled(true);
                    Player var21 = var1.getPlayer();
                    String var4 = "&cSaturation stews are banned on Progression 2.";
                    this.deny(var21, var4, 1064088254);
                }
            }
        }
    }

    @EventHandler
    public void onThrow(PlayerInteractEvent var1) {
        World var23 = var1.getPlayer().getWorld();
        if (this.active(var23, 1358355888)) {
            if (var1.getAction().isRightClick()) {
                ItemStack var5 = var1.getItem();
                if (var5 != null) {
                    Material var6 = var5.getType();
                    Material var24 = Material.SPLASH_POTION;
                    if (var6 != var24) {
                        Material var25 = Material.LINGERING_POTION;
                        if (var6 != var25) {
                            return;
                        }
                    }

                    if (Progression2Rules.potionBanned(var5, 1686183073)) {
                        Result var26 = Result.DENY;
                        var1.setUseItemInHand(var26);
                        Player var28 = var1.getPlayer();
                        String var4 = "&cOnly invisibility potions are allowed on Progression 2.";
                        this.deny(var28, var4, 1064088254);
                    }
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onDispense(BlockDispenseEvent var1) {
        World var14 = var1.getBlock().getWorld();
        if (this.active(var14, 1358355888)) {
            ItemStack var4 = var1.getItem();
            Material var9 = var4.getType();
            Material var15 = Material.TIPPED_ARROW;
            if (var9 == var15 || Progression2Rules.potionBanned(var4, 1686183073)) {
                var1.setCancelled(true);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onArrowLaunch(ProjectileLaunchEvent var1) {
        World var37 = var1.getEntity().getWorld();
        if (this.active(var37, 1358355888)) {
            if (var1.getEntity() instanceof Arrow var5) {
                PotionType var40 = var5.getBasePotionType();
                boolean var35;
                if ((var40 == null || var40.getPotionEffects().isEmpty()) && var5.getCustomEffects().isEmpty()) {
                    var35 = false;
                } else {
                    var35 = true;
                }

                if (var35) {
                    var1.setCancelled(true);
                    if (var5.getShooter() instanceof Player var8) {
                        String var4 = "&cTipped arrows are banned on Progression 2.";
                        this.deny(var8, var4, 1064088254);
                    }
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onEnchantTable(EnchantItemEvent var1) {
        World var37 = var1.getEnchanter().getWorld();
        if (this.enchantActive(var37, 1235730062)) {
            Map<Enchantment, Integer> var4 = var1.getEnchantsToAdd();
            Set<Entry<Enchantment, Integer>> var12 = var4.entrySet();
            Predicate<Entry<Enchantment, Integer>> var38 = var0 -> Progression2Rules.bannedEnchant(var0.getKey(), 1590575899);
            var12.removeIf(var38);

            for (Entry<Enchantment, Integer> var6 : var4.entrySet()) {
                int var7 = Progression2Rules.cap((Enchantment)var6.getKey(), 326282801);
                if (var7 >= 0) {
                    int var30 = (Integer)var6.getValue();
                    if (var30 > var7) {
                        Integer var41 = var7;
                        var6.setValue(var41);
                    }
                }
            }

            if (var4.isEmpty()) {
                var1.setCancelled(true);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onAnvil(PrepareAnvilEvent var1) {
        World var38 = var1.getView().getPlayer().getWorld();
        if (this.enchantActive(var38, 1235730062)) {
            ItemStack var4 = var1.getResult();
            if (var4 != null) {
                for (Entry var6 : enchantsOf(var4, 384089470).entrySet()) {
                    if (Progression2Rules.bannedEnchant((Enchantment)var6.getKey(), 1590575899)) {
                        var1.setResult(null);
                        return;
                    }

                    int var7 = Progression2Rules.cap((Enchantment)var6.getKey(), 326282801);
                    if (var7 >= 0) {
                        int var33 = (Integer)var6.getValue();
                        if (var33 > var7) {
                            var1.setResult(null);
                            return;
                        }
                    }
                }
            }
        }
    }

    private static Map<Enchantment, Integer> enchantsOf(ItemStack var0, int var1) {
        ItemMeta var4 = var0.getItemMeta();
        return var4 instanceof EnchantmentStorageMeta ? ((EnchantmentStorageMeta)var4).getStoredEnchants() : var0.getEnchantments();
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPickup(EntityPickupItemEvent var1) {
        World var26 = var1.getEntity().getWorld();
        if (this.active(var26, 1358355888)) {
            if (var1.getEntity() instanceof Player var4) {
                Material var32 = var1.getItem().getItemStack().getType();
                Map var18 = Progression2Rules.CARRY_LIMITS;
                Integer var6 = (Integer)var18.get(var32);
                if (var6 != null) {
                    int var23 = count(var4, var32, 353157624);
                    int var30 = var6;
                    if (var23 >= var30) {
                        var1.setCancelled(true);
                    }
                }
            }
        }
    }

    private void sweep() {
        for (Player var6 : Bukkit.getOnlinePlayers()) {
            World var26 = var6.getWorld();
            if (this.active(var26, 1358355888)) {
                for (Entry var8 : Progression2Rules.CARRY_LIMITS.entrySet()) {
                    Material var29 = (Material)var8.getKey();
                    int var32 = (Integer)var8.getValue();
                    this.trim(var6, var29, var32, 455538039);
                }
            }
        }
    }

    private static int count(Player var0, Material var1, int var2) {
        int var5 = 0;

        for (ItemStack var9 : var0.getInventory().getContents()) {
            if (var9 != null) {
                Material var22 = var9.getType();
                if (var22 == var1) {
                    int var28 = var9.getAmount();
                    var5 += var28;
                }
            }
        }

        return var5;
    }

    private void trim(Player var1, Material var2, int var3, int var4) {
        int var14 = count(var1, var2, 353157624);
        int var8 = var14 - var3;
        if (var8 > 0) {
            ItemStack[] var9 = var1.getInventory().getContents();

            for (int var10 = var9.length - 1; var10 >= 0 && var8 > 0; var10 += -1) {
                ItemStack var11 = var9[var10];
                if (var11 != null) {
                    Material var30 = var11.getType();
                    if (var30 == var2) {
                        int var53 = var11.getAmount();
                        int var12 = Math.min(var8, var53);
                        ItemStack var13 = var11.clone();
                        var13.setAmount(var12);
                        int var56 = var11.getAmount();
                        int var57 = var56 - var12;
                        var11.setAmount(var57);
                        PlayerInventory var38 = var1.getInventory();
                        ItemStack var66;
                        if (var11.getAmount() <= 0) {
                            var66 = null;
                        } else {
                            var66 = var11;
                        }

                        var38.setItem(var10, var66);
                        World var40 = var1.getWorld();
                        Location var60 = var1.getLocation();
                        var40.dropItemNaturally(var60, var13);
                        var8 -= var12;
                    }
                }
            }

            String var47 = pretty(var2, 484030197);
            Component var49 = Text.legacy("&e" + var47 + " are limited to &f" + var3 + "&e on Progression 2.", 373192400);
            var1.sendMessage(var49);
        }
    }

    private static String pretty(Material var0, int var1) {
        String var11 = var0.name();
        Locale var3 = Locale.ROOT;
        String[] var5 = var11.toLowerCase(var3).split("_");
        StringBuilder var14 = new StringBuilder();
        StringBuilder var6 = var14;

        for (String var10 : var5) {
            if (var6.length() > 0) {
                var6.append(' ');
            }

            char var39 = Character.toUpperCase(var10.charAt(0));
            StringBuilder var30 = var6.append(var39);
            String var41 = var10.substring(1);
            var30.append(var41);
        }

        return var6.append('s').toString();
    }
}
