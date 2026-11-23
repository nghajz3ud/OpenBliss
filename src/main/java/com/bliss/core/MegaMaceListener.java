package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.items.BlissCoreItems;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class MegaMaceListener implements Listener {
    private static final long BUFF_MS = 180000L;
    public static final int BREACH_LEVEL = 3;
    public static final int WIND_BURST_LEVEL = 2;
    private static final long SWEEP_TICKS = 20L;
    private final BlissPlugin plugin;
    private BukkitTask sweepTask;
    private final Map<UUID, MegaMaceListener.Hit> lastHit;

    public static boolean isBuffed(ItemStack var0, int var1) {
        if (!BlissCoreItems.isMegaMace(var0, 2030001584)) {
            return false;
        } else {
            ItemMeta var5 = var0.getItemMeta();
            if (var5 == null) {
                return false;
            } else {
                PersistentDataContainer var13 = var5.getPersistentDataContainer();
                NamespacedKey var3 = BlissCoreItems.KEY_MEGA_MACE_UNTIL;
                PersistentDataType var4 = PersistentDataType.LONG;
                Long var6 = (Long)var13.get(var3, var4);
                if (var6 != null) {
                    long var17 = System.currentTimeMillis();
                    long var23 = var6;
                    if (var17 < var23) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public MegaMaceListener(BlissPlugin var1, int var2) {
        ConcurrentHashMap var4 = new ConcurrentHashMap();
        this.lastHit = var4;
        this.plugin = var1;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::sweep;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 20L, 20L);
        this.sweepTask = var10;
    }

    public void stop(int var1) {
        if (this.sweepTask != null) {
            this.sweepTask.cancel();
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    public void onDamage(EntityDamageByEntityEvent var1) {
        if (var1.getEntity() instanceof Player var7) {
            if (var1.getDamager() instanceof Player var26) {
                Map var22 = this.lastHit;
                UUID var24 = var7.getUniqueId();
                UUID var25 = var26.getUniqueId();
                int var6 = Bukkit.getCurrentTick();
                MegaMaceListener.Hit var4 = new MegaMaceListener.Hit(var25, var6);
                var22.put(var24, var4);
            }
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    public void onResurrect(EntityResurrectEvent var1) {
        if (var1.getEntity() instanceof Player var4) {
            Map var13 = this.lastHit;
            UUID var26 = var4.getUniqueId();
            MegaMaceListener.Hit var30 = (MegaMaceListener.Hit)var13.get(var26);
            if (var30 != null) {
                int var18 = var30.tick();
                int var27 = Bukkit.getCurrentTick();
                if (var18 == var27) {
                    Player var6 = Bukkit.getPlayer(var30.damager());
                    if (var6 != null && !var6.equals(var4)) {
                        this.reward(var6, 818547759);
                    }

                    return;
                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onDeath(PlayerDeathEvent var1) {
        Player var4 = var1.getEntity();
        Map var8 = this.lastHit;
        UUID var16 = var4.getUniqueId();
        var8.remove(var16);
        Player var5 = var4.getKiller();
        if (var5 != null && !var5.equals(var4)) {
            this.reward(var5, 818547759);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        Map var4 = this.lastHit;
        UUID var7 = var1.getPlayer().getUniqueId();
        var4.remove(var7);
    }

    private void reward(Player var1, int var2) {
        ItemStack var11 = var1.getInventory().getItemInMainHand();
        if (BlissCoreItems.isMegaMace(var11, 2030001584)) {
            ItemMeta var12 = var11.getItemMeta();
            if (var12 != null) {
                PersistentDataContainer var21 = var12.getPersistentDataContainer();
                NamespacedKey var4 = BlissCoreItems.KEY_MEGA_MACE_UNTIL;
                PersistentDataType var5 = PersistentDataType.LONG;
                Long var40 = System.currentTimeMillis() + 180000L;
                var21.set(var4, var5, var40);
                Enchantment var30 = Enchantment.BREACH;
                Enchantment var8 = Enchantment.BREACH;
                int var42 = var12.getEnchantLevel(var8);
                int var35 = Math.max(3, var42);
                var12.addEnchant(var30, var35, true);
                Enchantment var31 = Enchantment.WIND_BURST;
                Enchantment var47 = Enchantment.WIND_BURST;
                int var45 = var12.getEnchantLevel(var47);
                int var37 = Math.max(2, var45);
                var12.addEnchant(var31, var37, true);
                var11.setItemMeta(var12);
                PlayerInventory var29 = var1.getInventory();
                var29.setItemInMainHand(var11);
            }
        }
    }

    private void sweep() {
        long var5 = System.currentTimeMillis();

        for (Player var8 : Bukkit.getOnlinePlayers()) {
            ItemStack[] var9 = var8.getInventory().getContents();
            int var10 = 0;

            while (true) {
                int var33 = var9.length;
                if (var10 >= var33) {
                    break;
                }

                ItemStack var11 = var9[var10];
                if (var11 != null) {
                    Material var28 = var11.getType();
                    Material var35 = Material.MACE;
                    if (var28 == var35 && clean(var11, var5, 111534474)) {
                        PlayerInventory var32 = var8.getInventory();
                        var32.setItem(var10, var11);
                    }
                }

                var10++;
            }
        }
    }

    private static boolean clean(ItemStack var0, long var1, int var3) {
        if (!BlissCoreItems.isMegaMace(var0, 2030001584)) {
            return false;
        } else {
            ItemMeta var7 = var0.getItemMeta();
            if (var7 == null) {
                return false;
            } else {
                PersistentDataContainer var8;
                boolean var9;
                Long var10;
                boolean var53;
                label47: {
                    var8 = var7.getPersistentDataContainer();
                    var9 = false;
                    NamespacedKey var5 = BlissCoreItems.KEY_MEGA_MACE_UNTIL;
                    PersistentDataType var6 = PersistentDataType.LONG;
                    var10 = (Long)var8.get(var5, var6);
                    if (var10 != null) {
                        long var59 = var10;
                        if (var1 < var59) {
                            var53 = true;
                            break label47;
                        }
                    }

                    var53 = false;
                }

                if (var10 != null && !var53) {
                    NamespacedKey var54 = BlissCoreItems.KEY_MEGA_MACE_UNTIL;
                    var8.remove(var54);
                    Enchantment var55 = Enchantment.BREACH;
                    var7.removeEnchant(var55);
                    var9 = true;
                }

                if (!var53) {
                    Set<Enchantment> var63 = var7.getEnchants().keySet();

                    for (Enchantment var13 : new ArrayList<>(var63)) {
                        if (MaceRestrictionsListener.isWindBurst(var13, 283952539)) {
                            var7.removeEnchant(var13);
                            var9 = true;
                        }
                    }
                }

                if (var9) {
                    var0.setItemMeta(var7);
                }

                return var9;
            }
        }
    }

    private static record Hit(UUID damager, int tick) {
    }
}
