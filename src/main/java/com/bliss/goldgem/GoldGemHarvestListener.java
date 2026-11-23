package com.bliss.goldgem;

import com.bliss.BlissPlugin;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.items.PendingGemStore;
import com.bliss.trader.TraderListener;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class GoldGemHarvestListener implements Listener {
    private static final int BURST_EVERY = 20;
    private static final double GEM_UP = 5.0;
    private static final double SMALL_SCALE = 0.5;
    private static final double BIG_SCALE = 2.0;
    private static final boolean BURST_PUFF = true;
    private static final int O1_AT = 40;
    private static final int COVER1_AT = 240;
    private static final int BROKEN_AT = 252;
    private static final int O2_AT = 272;
    private static final int THUNDER_AT = 352;
    private static final int LIGHTNING_LEAD = 4;
    private static final int COVER2_AT = 356;
    private static final int HANDOFF_AT = 376;
    private static final int SHARD_SPEEDUP = 2;
    private static final int SHARDS_END_AT;
    private static final int END_FADE = 26;
    private static final int TWITCH_EVERY = 4;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final GoldGemManager manager;
    private final Map<UUID, GoldGemHarvestListener.Cinematic> pending;
    private final Set<GoldGemHarvestListener.Cinematic> active;
    private final Set<UUID> harvestDeath;

    public GoldGemHarvestListener(BlissPlugin var1, PlayerDataStore var2, GoldGemManager var3, int var4) {
        HashMap var6 = new HashMap();
        this.pending = var6;
        HashSet var13 = new HashSet();
        this.active = var13;
        HashSet var14 = new HashSet();
        this.harvestDeath = var14;
        this.plugin = var1;
        this.players = var2;
        this.manager = var3;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST,
        ignoreCancelled = true
    )
    public void onFatalHit(EntityDamageByEntityEvent var1) {
        if (var1.getEntity() instanceof LivingEntity var7) {
            Map var19 = this.pending;
            UUID var74 = var7.getUniqueId();
            if (var19.containsKey(var74)) {
                var1.setCancelled(true);
            } else {
                Player var93 = null;
                Entity var12 = var1.getDamager();
                if (var12 instanceof Player) {
                    var93 = (Player)var12;
                } else {
                    Entity var96 = var1.getDamager();
                    if (var96 instanceof Projectile) {
                        ProjectileSource var97 = ((Projectile)var96).getShooter();
                        if (var97 instanceof Player) {
                            var93 = (Player)var97;
                        }
                    }
                }

                if (var93 != null) {
                    UUID var31 = var93.getUniqueId();
                    UUID var76 = var7.getUniqueId();
                    if (!var31.equals(var76)) {
                        double var34 = var7.getHealth();
                        double var87 = var1.getFinalDamage();
                        if (var34 - var87 > 0.0) {
                            return;
                        }

                        if (GoldGem.findInInventory(var93, 1361603346) == null) {
                            return;
                        }

                        if (var7 instanceof Player var94) {
                            GoldGemManager var46 = this.manager;
                            UUID var78 = var94.getUniqueId();
                            if (!var46.isVictim(var78, 1503488012)) {
                                return;
                            }

                            GoldGemManager var49 = this.manager;
                            UUID var80 = var94.getUniqueId();
                            if (var49.isRepurposed(var80, 1313846040)) {
                                return;
                            }

                            PlayerDataStore var52 = this.players;
                            UUID var82 = var94.getUniqueId();
                            PlayerData var10 = var52.get(var82, 1264010596);
                            GemType var55 = var10.gem(1413186337);
                            GemType var83 = GemType.NONE;
                            if (var55 == var83) {
                                return;
                            }

                            if (holdsTotem(var94, 203735843)) {
                                return;
                            }

                            var1.setCancelled(true);
                            GemType var92 = var10.gem(1413186337);
                            this.begin(var94, var93, var92, false, 491091794);
                        }

                        return;
                    }
                }
            }
        }
    }

    private static boolean holdsTotem(Player var0, int var1) {
        Material var6 = var0.getInventory().getItemInMainHand().getType();
        Material var3 = Material.TOTEM_OF_UNDYING;
        if (var6 != var3) {
            Material var10 = var0.getInventory().getItemInOffHand().getType();
            Material var12 = Material.TOTEM_OF_UNDYING;
            if (var10 != var12) {
                return false;
            }
        }

        return true;
    }

    @EventHandler(
        priority = EventPriority.LOWEST
    )
    public void onAnyDamage(EntityDamageEvent var1) {
        Map var4 = this.pending;
        UUID var8 = var1.getEntity().getUniqueId();
        if (var4.containsKey(var8)) {
            var1.setCancelled(true);
        }
    }

    private void begin(LivingEntity var1, Player var2, GemType var3, boolean var4, int var5) {
        final GoldGemHarvestListener.Cinematic var6 = new GoldGemHarvestListener.Cinematic(var1, var2, var3, var4, 1813949697);
        final UUID var14 = var1.getUniqueId();
        Map var20 = this.pending;
        var20.put(var14, var6);
        Set var23 = this.active;
        var23.add(var6);
        this.holdAtBrink(var1, 2000398418);
        var1.setInvulnerable(true);
        Vector var54 = new Vector(0, 0, 0);
        var1.setVelocity(var54);
        if (var1 instanceof Mob) {
            ((Mob)var1).setAI(false);
        }

        final World var92 = var6.world;
        final FragmentCoreRitual var16 = this.plugin.fragmentCoreRitual(234148762);
        if (var16 != null) {
            var16.shaderEngage(var92, 1599768622);
            var6.shaderHeld = true;
            int var59 = var6.bit;
            var16.shaderGem(var59, 426912452);
            var16.shaderPhase(0, 707533204);
        }

        UUID var62 = this.spawnGemDisplay(var6, var16, 779865855);
        var6.gem = var62;
        Location var64 = var6.pin;
        Sound var72 = Sound.PARTICLE_SOUL_ESCAPE;
        SoundCategory var78 = SoundCategory.MASTER;
        var92.playSound(var64, var72, var78, 2.0F, 0.6F);
        Location var66 = var6.pin;
        Sound var73 = Sound.ENTITY_WARDEN_HEARTBEAT;
        SoundCategory var79 = SoundCategory.MASTER;
        var92.playSound(var66, var73, var79, 2.0F, 0.8F);
        BukkitRunnable var67 = new BukkitRunnable() {
            int t = 0;

            public void run() {
                int var170 = this.t;
                int var301 = var170 + 1;
                this.t = var301;
                double var10 = (double)var170 * 0.06;
                if (var170 >= 352) {
                    if (var170 == 352) {
                        GoldGemHarvestListener var52 = GoldGemHarvestListener.this;
                        GoldGemHarvestListener.Cinematic var194 = var6;
                        var52.stopCams(var194, 897120321);
                        World var54 = var92;
                        Location var197 = var6.pin;
                        var54.strikeLightningEffect(var197);
                        World var57 = var92;
                        Location var200 = var6.pin;
                        Sound var315 = Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
                        SoundCategory var357 = SoundCategory.MASTER;
                        var57.playSound(var200, var315, var357, 2.0F, 1.0F);
                        if (var16 != null) {
                            FragmentCoreRitual var61 = var16;
                            World var202 = var92;
                            Location var318 = var6.gemAt;
                            var61.gustBurst(var202, var318, var10, 2.0, true, 1226658703);
                            FragmentCoreRitual var63 = var16;
                            UUID var205 = var6.gem;
                            var63.removeDisplay(var205, 633746888);
                            var6.gem = null;
                            FragmentCoreRitual var67 = var16;
                            World var208 = var92;
                            Location var321 = var6.gemAt;
                            List var362 = var6.shards;
                            int var400 = var6.bit;
                            var67.spawnShards(var208, var321, var362, var400, 1225313967);
                            World var69 = var92;
                            Location var211 = var6.gemAt;
                            Sound var322 = Sound.BLOCK_AMETHYST_BLOCK_BREAK;
                            SoundCategory var363 = SoundCategory.MASTER;
                            var69.playSound(var211, var322, var363, 1.0F, 0.9F);
                        }

                        GoldGemHarvestListener var71 = GoldGemHarvestListener.this;
                        GoldGemHarvestListener.Cinematic var213 = var6;
                        var71.finish(var213, 494208567);
                    } else {
                        if (var170 == 356 && var16 != null) {
                            var16.shaderFlashPulse$80030838(20, 32, 1670127537);
                        }

                        if (var16 != null) {
                            int var175 = GoldGemHarvestListener.SHARDS_END_AT;
                            if (var170 <= var175) {
                                FragmentCoreRitual var30 = var16;
                                World var177 = var92;
                                List var307 = var6.shards;
                                int var351 = (var170 - 352) * 2;
                                Location var390 = var6.gemAt;
                                double var412 = var6.pin.getY() + 0.05;
                                Color var440 = var6.color;
                                DustOptions var433 = new DustOptions(var440, 0.8F);
                                var30.fallShards(var177, var307, var351, var390, var412, var433, 2, 1158341553);
                            }
                        }

                        if (var170 == 376) {
                            if (var16 != null) {
                                FragmentCoreRitual var35 = var16;
                                int var181 = var6.bit;
                                int[] var12 = var35.gemFlashColor(var181, 1586303220);
                                if (var6.holder.isOnline()) {
                                    FragmentCoreRitual var42 = var16;
                                    Player var184 = var6.holder;
                                    int var309 = var12[0];
                                    int var354 = var12[1];
                                    int var393 = var12[2];
                                    var42.screenFlashEntity(var184, var309, var354, var393, 0, 26, 455878892);
                                }

                                FragmentCoreRitual var44 = var16;
                                Location var187 = var6.pin;
                                Player var312 = var6.holder;
                                int var356 = var12[0];
                                int var396 = var12[1];
                                int var418 = var12[2];
                                var44.screenFlashAudience(var187, var312, var356, var396, var418, 0, 26, 1080796111);
                            }

                            GoldGemHarvestListener var46 = GoldGemHarvestListener.this;
                            GoldGemHarvestListener.Cinematic var189 = var6;
                            FragmentCoreRitual var314 = var16;
                            var46.releaseShader(var189, var314, 132514531);
                        }

                        int var190 = GoldGemHarvestListener.SHARDS_END_AT;
                        if (var170 >= var190) {
                            this.cancel();
                            GoldGemHarvestListener var50 = GoldGemHarvestListener.this;
                            GoldGemHarvestListener.Cinematic var192 = var6;
                            var50.teardown(var192, 402281274);
                        }
                    }
                } else {
                    if (var6.present(591951736)) {
                        Map var77 = GoldGemHarvestListener.this.pending;
                        UUID var215 = var14;
                        if (var77.containsKey(var215)) {
                            Location var441 = var6.victim.getLocation();
                            if (var441.getWorld() != null) {
                                World var96 = var441.getWorld();
                                World var223 = var6.world;
                                if (var96.equals(var223)) {
                                    Location var226 = var6.pin;
                                    if (var441.distanceSquared(var226) > 0.04) {
                                        Location var13 = var6.pin.clone();
                                        float var228 = var441.getYaw();
                                        var13.setYaw(var228);
                                        float var230 = var441.getPitch();
                                        var13.setPitch(var230);
                                        LivingEntity var110 = var6.victim;
                                        var110.teleport(var13);
                                    }

                                    if (var16 != null && var170 < 240 && var170 % 20 == 0) {
                                        FragmentCoreRitual var118 = var16;
                                        World var235 = var92;
                                        Location var327 = var6.pin;
                                        var118.gustBurst(var235, var327, var10, 1.0, true, 1226658703);
                                        FragmentCoreRitual var120 = var16;
                                        World var237 = var92;
                                        Location var330 = var6.gemAt;
                                        double var368 = var10 + 0.7;
                                        var120.gustBurst(var237, var330, var368, 0.5, true, 1226658703);
                                    }

                                    GoldGemHarvestListener var122 = GoldGemHarvestListener.this;
                                    GoldGemHarvestListener.Cinematic var239 = var6;
                                    FragmentCoreRitual var332 = var16;
                                    var122.spinGem(var239, var332, var170, 1208198604);
                                    if (var170 == 40 || var170 == 272) {
                                        GoldGemHarvestListener var126 = GoldGemHarvestListener.this;
                                        GoldGemHarvestListener.Cinematic var243 = var6;
                                        var126.startCams(var243, 1995683720);
                                    }

                                    if (var170 >= 40 && var170 < 240) {
                                        GoldGemHarvestListener var130 = GoldGemHarvestListener.this;
                                        GoldGemHarvestListener.Cinematic var247 = var6;
                                        double var337 = (double)(var170 - 40) / 200.0;
                                        var130.publishCams(var247, var337, 1057019892);
                                    }

                                    if (var170 == 240) {
                                        GoldGemHarvestListener var133 = GoldGemHarvestListener.this;
                                        GoldGemHarvestListener.Cinematic var250 = var6;
                                        var133.stopCams(var250, 897120321);
                                    }

                                    if (var170 >= 272 && var170 < 352) {
                                        GoldGemHarvestListener var137 = GoldGemHarvestListener.this;
                                        GoldGemHarvestListener.Cinematic var254 = var6;
                                        double var343 = (double)(var170 - 272) / 80.0;
                                        var137.publishCams(var254, var343, 1057019892);
                                    }

                                    if (var170 == 240) {
                                        if (var16 != null) {
                                            var16.shaderFlashPulse$80030838(20, 32, 1670127537);
                                        }

                                        World var144 = var92;
                                        Location var259 = var6.pin;
                                        Sound var346 = Sound.ENTITY_GENERIC_EXPLODE;
                                        SoundCategory var373 = SoundCategory.MASTER;
                                        var144.playSound(var259, var346, var373, 1.0F, 0.7F);
                                    }

                                    if (var170 == 252 && var16 != null) {
                                        FragmentCoreRitual var149 = var16;
                                        UUID var263 = var6.gem;
                                        ItemDisplay var442 = var149.display(var263, 1415410134);
                                        if (var442 != null) {
                                            ItemStack var267 = RitualItems.gemBroken(var6.bit, 1044097340);
                                            var442.setItemStack(var267);
                                        }

                                        World var154 = var92;
                                        Location var270 = var6.gemAt;
                                        Sound var347 = Sound.BLOCK_GLASS_BREAK;
                                        SoundCategory var374 = SoundCategory.MASTER;
                                        var154.playSound(var270, var347, var374, 1.0F, 0.7F);
                                    }

                                    if (var170 > 252 && var170 % 4 == 0) {
                                        ThreadLocalRandom var443 = ThreadLocalRandom.current();
                                        GoldGemHarvestListener.Cinematic var160 = var6;
                                        float var280 = (float)((var443.nextDouble() - 0.5) * 0.16);
                                        var160.jx = var280;
                                        GoldGemHarvestListener.Cinematic var162 = var6;
                                        float var288 = (float)((var443.nextDouble() - 0.5) * 0.16);
                                        var162.jy = var288;
                                        GoldGemHarvestListener.Cinematic var164 = var6;
                                        float var296 = (float)((var443.nextDouble() - 0.5) * 0.5);
                                        var164.jroll = var296;
                                    }

                                    return;
                                }
                            }

                            this.cancel();
                            GoldGemHarvestListener var167 = GoldGemHarvestListener.this;
                            GoldGemHarvestListener.Cinematic var298 = var6;
                            var167.finish(var298, 494208567);
                            GoldGemHarvestListener var169 = GoldGemHarvestListener.this;
                            GoldGemHarvestListener.Cinematic var300 = var6;
                            var169.teardown(var300, 402281274);
                            return;
                        }
                    }

                    this.cancel();
                    Map var82 = GoldGemHarvestListener.this.pending;
                    UUID var217 = var14;
                    if (var82.remove(var217) != null) {
                        var6.victim.setInvulnerable(false);
                    }

                    GoldGemHarvestListener var88 = GoldGemHarvestListener.this;
                    GoldGemHarvestListener.Cinematic var220 = var6;
                    var88.teardown(var220, 402281274);
                }
            }
        };
        var6.task = var67;
        BukkitRunnable var48 = var6.task;
        BlissPlugin var69 = this.plugin;
        var48.runTaskTimer(var69, 1L, 1L);
    }

    private void holdAtBrink(LivingEntity var1, int var2) {
        double var9 = 1.0;
        Attribute var5 = Attribute.MAX_HEALTH;
        AttributeInstance var11 = var1.getAttribute(var5);
        if (var11 != null) {
            double var24 = var11.getValue();
            var9 = Math.min(1.0, var24);
        }

        double var22 = Math.max(0.1, var9);
        var1.setHealth(var22);
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    private UUID spawnGemDisplay(GoldGemHarvestListener.Cinematic var1, FragmentCoreRitual var2, int var3) {
        if (var2 == null) {
            return null;
        } else {
            ItemStack var53;
            label39: {
                try {
                    if (var1.test) {
                        BlissCoreItems var14 = this.plugin.coreItems(401009135);
                        GemType var34 = var1.soul;
                        var53 = var14.gemItem$1963390319(var34, false, 975160804);
                        break label39;
                    }
                } catch (RuntimeException var57) {
                    boolean var10001 = false;
                    return null;
                }

                try {
                    PlayerDataStore var27 = this.players;
                    UUID var44 = var1.victim.getUniqueId();
                    PlayerData var55 = var27.get(var44, 1264010596);
                    BlissCoreItems var31 = this.plugin.coreItems(401009135);
                    GemType var46 = var55.gem(1413186337);
                    boolean var49 = var55.tier2(629984660);
                    int var52 = var55.energy(1160853318);
                    var53 = var31.gemItem(var46, var49, var52, 256965471);
                } catch (RuntimeException var56) {
                    boolean var58 = false;
                    return null;
                }
            }

            Location var36 = var1.gemAt;
            ItemDisplay var54 = var2.spawnDisplay(var36, var53, 1.0F, 1550711431);
            Billboard var37 = Billboard.FIXED;
            var54.setBillboard(var37);
            Brightness var38 = new Brightness(15, 15);
            var54.setBrightness(var38);
            var54.setGlowing(true);
            Color var41 = var1.color;
            var54.setGlowColorOverride(var41);
            return var54.getUniqueId();
        }
    }

    private void spinGem(GoldGemHarvestListener.Cinematic var1, FragmentCoreRitual var2, int var3, int var4) {
        if (var2 != null) {
            UUID var20 = var1.gem;
            ItemDisplay var13 = var2.display(var20, 1415410134);
            if (var13 != null) {
                var13.setInterpolationDelay(0);
                var13.setInterpolationDuration(2);
                float var26 = var1.jx;
                float var30 = var1.jy;
                Vector3f var7 = new Vector3f(var26, var30, 0.0F);
                Quaternionf var24 = new Quaternionf();
                float var33 = (float)var3 * 0.07F;
                float var28 = 2.1F + var33;
                Quaternionf var25 = var24.rotateY(var28);
                Vector3f var29 = new Vector3f(1.0F, 1.0F, 1.0F);
                Quaternionf var34 = new Quaternionf();
                float var38 = FragmentCoreRitual.GEM_ART_ROLL;
                float var41 = var1.jroll;
                float var39 = var38 + var41;
                Quaternionf var35 = var34.rotateZ(var39);
                Transformation var23 = new Transformation(var7, var25, var29, var35);
                var13.setTransformation(var23);
            }
        }
    }

    private void startCams(GoldGemHarvestListener.Cinematic var1, int var2) {
        this.stopCams(var1, 897120321);
        if (var1.victimPlayer != null && var1.victimPlayer.isOnline()) {
            FragmentCoreRitual.CamStreamer var47 = FragmentCoreRitual.CamStreamer.start(var1.victimPlayer, 1117156868);
            var1.camV = var47;
        }

        if (var1.holder.isOnline()) {
            FragmentCoreRitual.CamStreamer var50 = FragmentCoreRitual.CamStreamer.start(var1.holder, 1117156868);
            var1.camH = var50;
        }

        FragmentCoreRitual var5 = this.plugin.fragmentCoreRitual(234148762);
        if (var5 != null) {
            Location var52 = var1.pin;

            for (Player var7 : var5.audience(var52, 1101174349)) {
                UUID var32 = var7.getUniqueId();
                UUID var55 = var1.holder.getUniqueId();
                if (!var32.equals(var55)) {
                    if (var1.victimPlayer != null) {
                        UUID var37 = var7.getUniqueId();
                        UUID var58 = var1.victimPlayer.getUniqueId();
                        if (var37.equals(var58)) {
                            continue;
                        }
                    }

                    FragmentCoreRitual.CamStreamer var8 = FragmentCoreRitual.CamStreamer.start(var7, 1117156868);
                    if (var8 != null) {
                        List var43 = var1.camCrowd;
                        var43.add(var8);
                    }
                }
            }
        }
    }

    private void publishCams(GoldGemHarvestListener.Cinematic var1, double var2, int var4) {
        double var48 = 11.2 * var2;
        double var50 = var48 * var2;
        double var15 = 7.0 + var50;
        double var54 = 0.18 * var2;
        double var17 = 0.04 + var54;
        if (var1.camV != null) {
            FragmentCoreRitual.CamStreamer var30 = var1.camV;
            var30.publish(var15, var17, 725995291);
        }

        if (var1.camH != null) {
            FragmentCoreRitual.CamStreamer var34 = var1.camH;
            var34.publish(var15, var17, 725995291);
        }

        for (FragmentCoreRitual.CamStreamer var43 : var1.camCrowd) {
            var43.publish(var15, var17, 725995291);
        }
    }

    private void stopCams(GoldGemHarvestListener.Cinematic var1, int var2) {
        if (var1.camV != null) {
            var1.camV.stop(1260880365);
            var1.camV = null;
        }

        if (var1.camH != null) {
            var1.camH.stop(1260880365);
            var1.camH = null;
        }

        Iterator var5 = var1.camCrowd.iterator();

        while (var5.hasNext()) {
            ((FragmentCoreRitual.CamStreamer)var5.next()).stop(1260880365);
        }

        var1.camCrowd.clear();
    }

    private void releaseShader(GoldGemHarvestListener.Cinematic var1, FragmentCoreRitual var2, int var3) {
        if (var1.shaderHeld) {
            var1.shaderHeld = false;
            if (var2 != null) {
                var2.shaderRelease(990029622);
            }
        }
    }

    private void teardown(GoldGemHarvestListener.Cinematic var1, int var2) {
        FragmentCoreRitual var6 = this.plugin.fragmentCoreRitual(234148762);
        Set var12 = this.active;
        var12.remove(var1);
        this.stopCams(var1, 897120321);
        if (var6 != null) {
            UUID var33 = var1.gem;
            var6.removeDisplay(var33, 633746888);
            List var35 = var1.shards;
            var6.releaseShards(var35, 2055451716);
        }

        var1.gem = null;
        if (var1.hadAI && var1.victim instanceof Mob var7 && var7.isValid()) {
            var7.setAI(true);
        }

        this.releaseShader(var1, var6, 132514531);
    }

    private void finish(GoldGemHarvestListener.Cinematic var1, int var2) {
        LivingEntity var7 = var1.victim;
        Map var11 = this.pending;
        UUID var43 = var7.getUniqueId();
        var11.remove(var43);
        var7.setInvulnerable(false);
        if (var1.test) {
            if (var7.isValid() && !var7.isDead()) {
                if (var7 instanceof Mob var8 && var1.hadAI) {
                    var8.setAI(true);
                }

                Player var41 = var1.holder;
                Component var55 = Text.legacy(
                    "<##FFD773>The soul of &f" + var7.getName() + " <##FFD773>has been repurposed &7(mob test — nothing absorbed)", 373192400
                );
                var41.sendMessage(var55);
                var7.setHealth(0.0);
            }
        } else if (var1.victimPlayer != null && var1.victimPlayer.isOnline() && !var7.isDead()) {
            Player var46 = var1.holder;
            Player var58 = var1.victimPlayer;
            GemType var59 = var1.soul;
            if (this.absorb(var46, var58, var59, 429759327)) {
                Set var26 = this.harvestDeath;
                UUID var48 = var7.getUniqueId();
                var26.add(var48);
            }

            var7.setHealth(0.0);
        }
    }

    private boolean absorb(Player var1, Player var2, GemType var3, int var4) {
        if (var1 != null && var1.isOnline()) {
            ItemStack var8 = GoldGem.findInInventory(var1, 1361603346);
            if (var8 == null) {
                return false;
            } else {
                boolean var9 = GoldGem.absorbedGems(var8, 874118425).isEmpty();
                GoldGem.addGem(var8, var3, 876666177);
                if (var9) {
                    GoldGem.setSelected(var8, var3, 958539197);
                }

                PlayerDataStore var24 = this.players;
                UUID var38 = var2.getUniqueId();
                PlayerData var10 = var24.get(var38, 1264010596);
                GemType var39 = GemType.NONE;
                var10.setGem(var39, 2137390487);
                var10.setTier2(false, 1757992594);
                TraderListener.clearGemItems(var2, 579055745);
                GoldGemManager var30 = this.manager;
                UUID var42 = var2.getUniqueId();
                var30.markRepurposed(var42, var3, 1301773108);
                GoldGemManager var32 = this.manager;
                UUID var44 = var2.getUniqueId();
                var32.removeVictim(var44, 211332633);
                return true;
            }
        } else {
            return false;
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    public void onHarvestDeath(PlayerDeathEvent var1) {
        Set var4 = this.harvestDeath;
        UUID var11 = var1.getEntity().getUniqueId();
        if (var4.remove(var11)) {
            List<ItemStack> var7 = var1.getDrops();
            Predicate<ItemStack> var12 = var0 -> {
                boolean var3;
                if (BlissCoreItems.gemIdOf(var0, 382441079) > 0) {
                    var3 = true;
                } else {
                    var3 = false;
                }

                return var3;
            };
            var7.removeIf(var12);
            Component var17 = Text.legacy(var1.getEntity().getName() + "'s soul has been repurposed", 373192400);
            var1.deathMessage(var17);
        }
    }

    @EventHandler(
        priority = EventPriority.HIGH
    )
    public void onHolderDeath(PlayerDeathEvent var1) {
        if (this.plugin.globalState(724248306) != null && this.plugin.globalState(724248306).goldGemSoulbound(269044761)) {
            Player var5 = var1.getEntity();
            ItemStack var6 = null;
            if (var1.getKeepInventory()) {
                var6 = GoldGem.findInInventory(var5, 1361603346);
            } else {
                for (ItemStack var94 : var1.getDrops()) {
                    if (GoldGem.isGoldGem(var94)) {
                        var6 = var94;
                        break;
                    }
                }
            }

            if (var6 != null) {
                GoldGemManager var25 = this.manager;
                GemType var7 = var25.ownSoulOf(var6, 999955318);
                UUID var8 = GoldGem.getHolder(var6, 1383595883);
                if (var8 == null) {
                    var8 = var5.getUniqueId();
                }

                UUID var75 = var5.getUniqueId();
                if (var8.equals(var75) || this.stillHolder(var8, 942975482)) {
                    this.returnSouls(var6, 1734441022);
                    UUID var79 = var5.getUniqueId();
                    if (!var8.equals(var79)) {
                        List<ItemStack> var49 = var1.getDrops();
                        Predicate<ItemStack> var85 = GoldGem::isGoldGem;
                        var49.removeIf(var85);
                        if (var1.getKeepInventory()) {
                            removeGoldGems(var5, 1933446344);
                        }

                        Player var9 = Bukkit.getPlayer(var8);
                        if (var9 != null && !var9.isDead()) {
                            this.handBack(var9, var7, 1090621171);
                        } else {
                            GoldGemManager var62 = this.manager;
                            var62.setSoulboundPending(var8, var7, 1622739586);
                        }

                        Component var88 = Text.legacy("<##FFD773>The Gold Gem was never yours &7- it returns to its holder.", 373192400);
                        var5.sendMessage(var88);
                    } else {
                        if (var1.getKeepInventory()) {
                            resetToOwn(var6, var7, var8, 1988529333);
                        } else {
                            List<ItemStack> var42 = var1.getDrops();
                            Predicate<ItemStack> var80 = GoldGem::isGoldGem;
                            var42.removeIf(var80);
                        }

                        GoldGemManager var45 = this.manager;
                        var45.setSoulboundPending(var8, var7, 1622739586);
                        Component var83 = Text.legacy("<##FFD773>The Gold Gem clings to your soul &7- its awakening is undone.", 373192400);
                        var5.sendMessage(var83);
                    }
                }
            }
        }
    }

    private boolean stillHolder(UUID var1, int var2) {
        PlayerDataStore var5 = this.players;
        GemType var7 = var5.get(var1, 1264010596).gem(1413186337);
        GemType var9 = GemType.NONE;
        boolean var8;
        if (var7 == var9) {
            var8 = true;
        } else {
            var8 = false;
        }

        return var8;
    }

    private void returnSouls(ItemStack var1, int var2) {
        for (GemType var10 : GoldGem.absorbedGems(var1, 874118425)) {
            GoldGemManager var23 = this.manager;

            for (UUID var12 : var23.victimsOf(var10, 461003659)) {
                Player var13 = Bukkit.getPlayer(var12);
                if (var13 == null || !JuggernautGemGuardListener.isJuggernaut(var13, 859003337)) {
                    if (var13 != null && this.plugin.goldenDream(2108340810) != null && this.plugin.goldenDream(2108340810).dream(2117018474) != null) {
                        GoldenDreamWorld var47 = this.plugin.goldenDream(2108340810).dream(2117018474);
                        if (var47.inDream(var12, 2004945976)) {
                            continue;
                        }
                    }

                    GoldGemManager var50 = this.manager;
                    var50.clearRepurposed(var12, 566739106);
                    PlayerDataStore var53 = this.players;
                    PlayerData var14 = var53.get(var12, 1264010596);
                    var14.setGem(var10, 2137390487);
                    if (var13 != null) {
                        int var71 = BlissCoreItems.findGemSlot(var13, 1702349856);
                        BlissCoreItems var77 = this.plugin.coreItems(401009135);
                        boolean var79 = var14.tier2(629984660);
                        int var80 = var14.energy(1160853318);
                        ItemStack var78 = var77.gemItem(var10, var79, var80, 256965471);
                        BlissCoreItems.giveGemOrHold(var13, var71, var78, 2133739666);
                        Component var75 = Text.legacy("&6Your soul is your own again - your " + var10.coloredName() + "&6 gem returns.", 373192400);
                        var13.sendMessage(var75);
                    } else if (this.plugin.pendingGems(1235971379) != null) {
                        PendingGemStore var62 = this.plugin.pendingGems(1235971379);
                        var62.owe(var12, 78433279);
                    }
                }
            }
        }
    }

    private static void removeGoldGems(Player var0, int var1) {
        ItemStack[] var5 = var0.getInventory().getContents();
        int var6 = 0;

        while (true) {
            int var16 = var5.length;
            if (var6 >= var16) {
                return;
            }

            if (GoldGem.isGoldGem(var5[var6])) {
                PlayerInventory var15 = var0.getInventory();
                var15.setItem(var6, null);
            }

            var6++;
        }
    }

    private static void resetToOwn(ItemStack var0, GemType var1, UUID var2, int var3) {
        int var10 = GoldGem.bitValue(var1, 841817363);
        GoldGem.setMask(var0, var10, 1710446259);
        GoldGem.setState(var0, 0, 953378395);
        GoldGem.setSelected(var0, var1, 958539197);
        GoldGem.setOwn(var0, var1, 818871712);
        GoldGem.setHolder(var0, var2, 1407575650);
    }

    @EventHandler
    public void onHolderRespawn(PlayerRespawnEvent var1) {
        Player var4 = var1.getPlayer();
        this.deliverSoulbound(var4, 1764091517);
    }

    @EventHandler
    public void onHolderJoin(PlayerJoinEvent var1) {
        Player var4 = var1.getPlayer();
        this.deliverSoulbound(var4, 1764091517);
    }

    private void deliverSoulbound(final Player var1, int var2) {
        GoldGemManager var7 = this.manager;
        UUID var11 = var1.getUniqueId();
        if (var7.isSoulboundPending(var11, 1382078444)) {
            BukkitRunnable var9 = new BukkitRunnable() {
                public void run() {
                    Player var4 = Bukkit.getPlayer(var1.getUniqueId());
                    if (var4 != null && !var4.isDead()) {
                        GoldGemManager var14 = GoldGemHarvestListener.this.manager;
                        UUID var19 = var4.getUniqueId();
                        GemType var5 = var14.takeSoulboundPending(var19, 42566578);
                        if (var5 != null) {
                            GoldGemHarvestListener var18 = GoldGemHarvestListener.this;
                            var18.handBack(var4, var5, 1090621171);
                        }
                    }
                }
            };
            BlissPlugin var13 = this.plugin;
            var9.runTaskLater(var13, 1L);
        }
    }

    private void handBack(Player var1, GemType var2, int var3) {
        if (GoldGem.findInInventory(var1, 1361603346) == null) {
            ItemStack var7 = GoldGem.create(485535734);
            UUID var22 = var1.getUniqueId();
            resetToOwn(var7, var2, var22, 1988529333);
            BlissCoreItems.giveOrDrop(var1, var7, 41085121);
        }

        PlayerDataStore var13 = this.players;
        UUID var19 = var1.getUniqueId();
        PlayerData var23 = var13.get(var19, 1264010596);
        var23.setTier2(true, 1757992594);
        var23.setEnergy(8, 319922676);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        Map var8 = this.pending;
        UUID var29 = var1.getPlayer().getUniqueId();
        GoldGemHarvestListener.Cinematic var6 = (GoldGemHarvestListener.Cinematic)var8.remove(var29);
        if (var6 != null) {
            if (var6.task != null) {
                var6.task.cancel();
            }

            Player var7 = var1.getPlayer();
            var7.setInvulnerable(false);
            if (!var6.test) {
                Player var32 = var6.holder;
                GemType var38 = var6.soul;
                if (this.absorb(var32, var7, var38, 429759327)) {
                    Set var24 = this.harvestDeath;
                    UUID var34 = var7.getUniqueId();
                    var24.add(var34);
                }
            }

            var7.setHealth(0.0);
            this.teardown(var6, 402281274);
        }
    }

    public void shutdown(int var1) {
        Set<GoldGemHarvestListener.Cinematic> var34 = this.active;

        for (GoldGemHarvestListener.Cinematic var5 : new ArrayList<>(var34)) {
            if (var5.task != null) {
                var5.task.cancel();
            }

            Map var21 = this.pending;
            UUID var31 = var5.victim.getUniqueId();
            if (var21.containsKey(var31) && var5.victim.isValid()) {
                var5.victim.setInvulnerable(false);
            }

            this.teardown(var5, 402281274);
        }

        this.pending.clear();
        this.active.clear();
    }

    static {
        int var6 = (int)Math.ceil(22.727272727272727);
        SHARDS_END_AT = 362 + var6;
    }

    private final class Cinematic {
        final LivingEntity victim;
        final Player victimPlayer;
        final boolean test;
        final Player holder;
        final Location pin;
        final World world;
        final GemType soul;
        final int bit;
        final Color color;
        final Location gemAt;
        final boolean hadAI;
        UUID gem;
        FragmentCoreRitual.CamStreamer camV;
        FragmentCoreRitual.CamStreamer camH;
        final List<FragmentCoreRitual.CamStreamer> camCrowd;
        final List<UUID> shards;
        float jx;
        float jy;
        float jroll;
        boolean shaderHeld;
        BukkitRunnable task;

        Cinematic(LivingEntity nullx, Player nullxx, GemType nullxxx, boolean nullxxxx, int var6) {
            ArrayList var8 = new ArrayList();
            this.camCrowd = var8;
            ArrayList var39 = new ArrayList();
            this.shards = var39;
            this.victim = nullx;
            this.victimPlayer = nullx instanceof Player var43 ? var43 : null;
            this.test = nullxxxx;
            this.holder = nullxx;
            Location var48 = nullx.getLocation().clone();
            this.pin = var48;
            World var50 = nullx.getWorld();
            this.world = var50;
            this.soul = nullxxx;
            int var75 = GoldGem.bit(nullxxx, 880748148);
            int var53 = Math.max(0, var75);
            this.bit = var53;
            boolean var71;
            if (nullx instanceof Mob && !((Mob)nullx).hasAI()) {
                var71 = false;
            } else {
                var71 = true;
            }

            this.hadAI = var71;
            FragmentCoreRitual var91 = GoldGemHarvestListener.this.plugin.fragmentCoreRitual(234148762);
            int[] var38;
            if (var91 != null) {
                int var70 = this.bit;
                var38 = var91.gemBaseColor(var70, 1417188842);
            } else {
                var38 = new int[]{255, 215, 115};
            }

            int var61 = var38[0];
            int var81 = var38[1];
            int var88 = var38[2];
            Color var62 = Color.fromRGB(var61, var81, var88);
            this.color = var62;
            Location var66 = this.pin.clone().add(0.0, 5.0, 0.0);
            this.gemAt = var66;
            this.gemAt.setYaw(0.0F);
            this.gemAt.setPitch(0.0F);
        }

        boolean present(int var1) {
            boolean var10;
            if (this.victimPlayer != null) {
                if (this.victimPlayer.isOnline() && !this.victimPlayer.isDead()) {
                    var10 = true;
                } else {
                    var10 = false;
                }
            } else if (this.victim.isValid() && !this.victim.isDead()) {
                var10 = true;
            } else {
                var10 = false;
            }

            return var10;
        }
    }
}
