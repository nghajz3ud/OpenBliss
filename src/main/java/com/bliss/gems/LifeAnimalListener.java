package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.items.BlissCoreItems;
import com.destroystokyo.paper.entity.Pathfinder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public final class LifeAnimalListener implements Listener {
    private static final long ATTRACTED_DURATION_MS = 30000L;
    private static final long BREEDING_DURATION_MS = 180000L;
    private static final int ATTRACTION_RADIUS = 9;
    private static final int BREED_SEARCH_RADIUS = 30;
    private static final long ATTRACTION_PERIOD_TICKS = 20L;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final Map<UUID, Long> attracted;
    private final Map<UUID, Long> breeding;
    private BukkitTask attractionTask;

    public LifeAnimalListener(BlissPlugin var1, PlayerDataStore var2, int var3) {
        ConcurrentHashMap var5 = new ConcurrentHashMap();
        this.attracted = var5;
        ConcurrentHashMap var10 = new ConcurrentHashMap();
        this.breeding = var10;
        this.plugin = var1;
        this.players = var2;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::attractionTick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 20L, 20L);
        this.attractionTask = var10;
    }

    public void stop(int var1) {
        if (this.attractionTask != null) {
            this.attractionTask.cancel();
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onConsume(PlayerItemConsumeEvent var1) {
        Player var6 = var1.getPlayer();
        int var15 = BlissCoreItems.gemIdOf(var6.getInventory().getItemInOffHand(), 382441079);
        int var29 = GemType.LIFE.id();
        if (var15 == var29) {
            PlayerDataStore var17 = this.players;
            UUID var31 = var6.getUniqueId();
            PlayerData var8 = var17.get(var31, 1264010596);
            if (var8.energy(1160853318) > 3) {
                if (var8.canUsePassives(2051432579)) {
                    float var28 = switch (var1.getItem().getType()) {
                        case GOLDEN_APPLE, ENCHANTED_GOLDEN_APPLE-> 9.6F;
                        case GOLDEN_CARROT-> 14.4F;
                        case COOKED_BEEF-> 12.8F;
                        case COOKED_PORKCHOP-> 12.8F;
                        default -> 0.0F;
                    };
                    if (!(var28 <= 0.0F)) {
                        float var40 = var6.getSaturation();
                        float var41 = var40 + var28;
                        float var39 = Math.min(20.0F, var41);
                        var6.setSaturation(var39);
                    }
                }
            }
        }
    }

    private void attractionTick() {
        long var15 = System.currentTimeMillis();
        Collection<Long> var31 = this.attracted.values();
        Predicate<Long> var90 = var2 -> {
            long var6 = var2;
            boolean var9;
            if (var6 <= var15) {
                var9 = true;
            } else {
                var9 = false;
            }

            return var9;
        };
        var31.removeIf(var90);
        Collection<Long> var35 = this.breeding.values();
        Predicate<Long> var93 = var2 -> {
            long var6 = var2;
            boolean var9;
            if (var6 <= var15) {
                var9 = true;
            } else {
                var9 = false;
            }

            return var9;
        };
        var35.removeIf(var93);
        String var37 = "life";
        String var94 = "animal-attraction";
        double var17 = GemTuning.radius(var37, var94, 9.0, 1058601812);
        Iterator var19 = Bukkit.getOnlinePlayers().iterator();

        while (true) {
            Player var20;
            int var56;
            int var98;
            do {
                if (!var19.hasNext()) {
                    return;
                }

                var20 = (Player)var19.next();
                ItemStack var21 = var20.getInventory().getItemInMainHand();
                ItemStack var22 = var20.getInventory().getItemInOffHand();
                int var54 = BlissCoreItems.gemIdOf(var21, 382441079);
                int var96 = GemType.LIFE.id();
                if (var54 == var96) {
                    break;
                }

                var56 = BlissCoreItems.gemIdOf(var22, 382441079);
                var98 = GemType.LIFE.id();
            } while (var56 != var98);

            Location var23 = var20.getLocation().clone().add(0.0, 1.7, 0.0);
            World var62 = var20.getWorld();
            Location var102 = var20.getLocation();

            for (Entity var25 : var62.getNearbyEntities(var102, var17, var17, var17)) {
                if (isBreedable(var25, 1484961461)) {
                    UUID var104 = var25.getUniqueId();
                    if (!this.isAttracted(var104, var15, 724867577) && var25 instanceof Mob var26) {
                        Pathfinder var79 = var26.getPathfinder();
                        Location var106 = var20.getLocation();
                        var79.moveTo(var106);
                        Vector var82 = var23.toVector();
                        Vector var109 = var26.getLocation().toVector();
                        Vector var27 = var82.subtract(var109);
                        Location var28 = var26.getLocation().clone();
                        var28.setDirection(var27);
                        float var112 = var28.getYaw();
                        float var118 = var28.getPitch();
                        var26.setRotation(var112, var118);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreedRightClick(PlayerInteractEntityEvent var1) {
        EquipmentSlot var28 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var28 == var3) {
            Player var17 = var1.getPlayer();
            ItemStack var18 = var17.getInventory().getItemInOffHand();
            int var35 = BlissCoreItems.gemIdOf(var18, 382441079);
            int var84 = GemType.LIFE.id();
            if (var35 == var84) {
                if (BlissCoreItems.tierOf(var18, 1633828214) == 2) {
                    PlayerDataStore var39 = this.players;
                    UUID var87 = var17.getUniqueId();
                    if (var39.get(var87, 1264010596).canUsePassives(2051432579)) {
                        Material var45 = var17.getInventory().getItemInMainHand().getType();
                        Material var88 = Material.AIR;
                        if (var45 == var88) {
                            Entity var19 = var1.getRightClicked();
                            if (isBreedable(var19, 1484961461)) {
                                long var20 = System.currentTimeMillis();
                                UUID var90 = var19.getUniqueId();
                                if (!this.isBreeding(var90, var20, 2033793146)) {
                                    UUID var92 = var19.getUniqueId();
                                    if (!this.isAttracted(var92, var20, 724867577)) {
                                        Map var57 = this.attracted;
                                        UUID var94 = var19.getUniqueId();
                                        String var7 = "life";
                                        String var8 = "animal-attraction";
                                        long var130 = (long)(GemTuning.duration(var7, var8, 30.0, 812730832) * 1000.0);
                                        Long var110 = var20 + var130;
                                        var57.put(var94, var110);
                                        Color var112 = Color.fromRGB(255, 0, 180);
                                        DustOptions var59 = new DustOptions(var112, 1.5F);
                                        World var61 = var19.getWorld();
                                        Particle var95 = Particle.DUST;
                                        Location var114 = var19.getLocation();
                                        Particles.spawn(var61, var95, var114, 7, 1.0, 1.0, 1.0, 0.1, var59, true, 1748031581);
                                        EntityType var23 = var19.getType();
                                        String var64 = "life";
                                        String var96 = "animal-breeding";
                                        double var24 = GemTuning.radius(var64, var96, 30.0, 1058601812);
                                        World var68 = var19.getWorld();
                                        Location var98 = var19.getLocation();

                                        for (Entity var27 : var68.getNearbyEntities(var98, var24, var24, var24)) {
                                            if (!var27.equals(var19)) {
                                                EntityType var79 = var27.getType();
                                                if (var79 == var23) {
                                                    UUID var102 = var27.getUniqueId();
                                                    if (this.isAttracted(var102, var20, 724867577)) {
                                                        this.startBreeding(var19, var27, var20, 1340834307);
                                                        return;
                                                    }
                                                }
                                            }
                                        }

                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void startBreeding(final Entity var1, final Entity var2, long var3, int var5) {
        if (var1 instanceof Mob var15 && var2 instanceof Mob var16) {
            UUID var17 = var1.getUniqueId();
            UUID var18 = var2.getUniqueId();
            String var32 = "life";
            String var7 = "animal-breeding";
            long var19 = (long)(GemTuning.duration(var32, var7, 180.0, 812730832) * 1000.0);
            Map var40 = this.breeding;
            Long var57 = var3 + var19;
            var40.put(var17, var57);
            Map var43 = this.breeding;
            Long var62 = var3 + var19;
            var43.put(var18, var62);
            BukkitRunnable var45 = new BukkitRunnable() {
                int rounds = 0;

                public void run() {
                    int var47 = this.rounds + 1;
                    this.rounds = var47;
                    if (this.rounds <= 60 && var1.isValid() && var2.isValid()) {
                        World var15x = var1.getWorld();
                        World var51 = var2.getWorld();
                        if (var15x.equals(var51)) {
                            Location var19 = var1.getLocation();
                            Location var54 = var2.getLocation();
                            if (var19.distance(var54) > 1.2) {
                                Pathfinder var25 = var15.getPathfinder();
                                Location var57 = var2.getLocation();
                                var25.moveTo(var57);
                                Pathfinder var29 = var16.getPathfinder();
                                Location var60 = var1.getLocation();
                                var29.moveTo(var60);
                                return;
                            }

                            World var33 = var1.getWorld();
                            Location var63 = var1.getLocation();
                            EntityType var70 = var1.getType();
                            Entity var3 = var33.spawnEntity(var63, var70);
                            if (var3 instanceof Ageable) {
                                ((Ageable)var3).setBaby();
                            }

                            Entity var41 = var1;
                            EntityEffect var64 = EntityEffect.LOVE_HEARTS;
                            var41.playEffect(var64);
                            Entity var43 = var2;
                            EntityEffect var65 = EntityEffect.LOVE_HEARTS;
                            var43.playEffect(var65);
                            this.cancel();
                            return;
                        }
                    }

                    this.cancel();
                }
            };
            BlissPlugin var50 = this.plugin;
            var45.runTaskTimer(var50, 5L, 5L);
            return;
        }
    }

    private boolean isAttracted(UUID var1, long var2, int var4) {
        Map var10 = this.attracted;
        Long var9 = (Long)var10.get(var1);
        if (var9 != null) {
            long var15 = var9;
            if (var15 > var2) {
                return true;
            }
        }

        return false;
    }

    private boolean isBreeding(UUID var1, long var2, int var4) {
        Map var10 = this.breeding;
        Long var9 = (Long)var10.get(var1);
        if (var9 != null) {
            long var15 = var9;
            if (var15 > var2) {
                return true;
            }
        }

        return false;
    }

    private static boolean isBreedable(Entity var0, int var1) {
        boolean var10;
        if (!(var0 instanceof Cow) && !(var0 instanceof Pig) && !(var0 instanceof Chicken) && !(var0 instanceof Sheep)) {
            var10 = false;
        } else {
            var10 = true;
        }

        return var10;
    }
}
