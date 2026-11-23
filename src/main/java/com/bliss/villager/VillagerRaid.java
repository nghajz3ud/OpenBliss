package com.bliss.villager;

import com.bliss.BlissPlugin;
import com.bliss.admin.BlissCommand;
import com.bliss.core.Particles;
import com.bliss.honored.HonoredOneManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.EntityRemoveEvent.Cause;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public final class VillagerRaid implements Listener {
    private static VillagerRaid current;
    private static final int WAVES = 8;
    private static final double SPAWN_MIN = 8.0;
    private static final double SPAWN_MAX = 40.0;
    private static final long CHECK_PERIOD = 20L;
    private static final long NEXT_WAVE_DELAY = 100L;
    public static final String RAID_TAG = "bliss_last_raid";
    private final BlissPlugin plugin;
    private final EventState state;
    private final Location center;
    private final BossBar bar;
    private final Set<UUID> alive;
    private int wave;
    private int waveSize;
    private boolean betweenWaves;
    private boolean finished;
    private BukkitTask task;
    private static final long RENDER_PERIOD = 2L;
    private static final int BELL_COUNT = 4;
    private static final float BELL_PITCH = 0.5F;
    private static final long BELL_GAP = 12L;
    private static final int WITCH_FIRE_RES_TICKS = 400;
    private int pendingSpawns;
    private static final int SCAN_RINGS = 3;
    private static final int SCAN_PTS = 18;
    private static final double SCAN_R = 1.7;
    private static final double SCAN_RING_GAP = 0.4;
    private static final double SCAN_SPEED = 0.45;
    private static final int ARRIVAL_BOLTS = 2;
    private static final double ARRIVAL_BOLT_R = 2.5;
    private static final double ARRIVAL_BOLT_H = 12.0;
    private static final double AMBIENT_BOLT_R = 20.0;
    private static final double AMBIENT_BOLT_H = 22.0;
    private static final long AMBIENT_EVERY = 35L;
    private static final double THUNDER_SOUND_CHANCE = 0.3;
    private static final int STROKE_PER_TICK = 7;
    private static final int STORM_TICKS = 24000;
    private static final int SPAWN_GROUP_MAX = 2;
    private static final long SPAWN_GAP_TICKS = 8L;
    private static final float ARRIVAL_VOLUME = 1.2F;
    private BukkitTask renderTask;
    private long refillStart;
    private long chargeStart;
    private BukkitTask boltTask;
    private BukkitTask tradeTask;
    private boolean stormHeld;
    private boolean prevStorm;
    private boolean prevThunder;
    private int prevWeather;
    private static final long CHARGE_TICKS = 40L;
    private static final int MOB_MULTIPLIER = 2;
    private static final String SCALE_PATH = "villager-event.raid-mob-scale";
    private static final double SCALE_MIN = 0.05;
    private static final double SCALE_MAX = 3.0;
    private static final String TAG_WEB = "bliss_raid_web";
    private static final String TAG_BUILD = "bliss_raid_build";
    private static final String TAG_BREAK = "bliss_raid_break";
    private static final int TRADE_FROM = 2;
    private static final int BUILD_FROM = 2;
    private static final int BREAK_FROM = 2;
    private static final int WEB_FROM = 4;
    private static final double TRADE_BASE = 0.35;
    private static final double TRADE_STEP = 0.07;
    private static final double TRADE_CAP = 0.75;
    private static final long BEHAVIOUR_PERIOD = 15L;
    private static final int EDIT_TTL = 600;
    private static final double WEB_RANGE = 12.0;
    private static final double BUILD_RANGE = 3.0;
    private static final int KEEP_CLEAR = 4;
    private final List<VillagerRaid.Edit> edits;

    public static VillagerRaid current(int var0) {
        return current;
    }

    private static Component barTitle(int var0) {
        Builder var8 = Component.text();
        int var9 = 0;

        while (true) {
            int var57 = "ᴛʜᴇ ʟᴀꜱᴛ ʀᴀɪᴅ".length();
            if (var9 >= var57) {
                TextComponent var55 = (TextComponent)var8.build();
                TextDecoration var63 = TextDecoration.BOLD;
                return var55.decoration(var63, true);
            }

            double var21;
            if ("ᴛʜᴇ ʟᴀꜱᴛ ʀᴀɪᴅ".length() == 1) {
                var21 = 0.0;
            } else {
                double var51 = (double)var9;
                double var88 = (double)("ᴛʜᴇ ʟᴀꜱᴛ ʀᴀɪᴅ".length() - 1);
                var21 = var51 / var88;
            }

            double var68 = Math.abs(var21 * 2.0 - 1.0);
            double var12 = 1.0 - var68;
            double var72 = 109.0 * var12;
            int var14 = (int)Math.round(105.0 + var72);
            double var76 = 26.0 * var12;
            int var15 = (int)Math.round(8.0 + var76);
            double var80 = 18.0 * var12;
            int var16 = (int)Math.round(16.0 + var80);
            String var59 = "ᴛʜᴇ ʟᴀꜱᴛ ʀᴀɪᴅ";
            TextComponent var61 = Component.text(var59.charAt(var9));
            TextColor var84 = TextColor.color(var14, var15, var16);
            Component var62 = var61.color(var84);
            var8.append(var62);
            var9++;
        }
    }

    public VillagerRaid(BlissPlugin var1, EventState var2, Location var3, int var4) {
        HashSet var6 = new HashSet();
        this.alive = var6;
        this.wave = 0;
        this.waveSize = 0;
        this.betweenWaves = false;
        this.finished = false;
        this.pendingSpawns = 0;
        this.refillStart = -1L;
        this.chargeStart = -1L;
        ArrayList var34 = new ArrayList();
        this.edits = var34;
        this.plugin = var1;
        this.state = var2;
        Location var38 = var3.clone();
        this.center = var38;
        Component var39 = barTitle(968969712);
        Color var7 = Color.RED;
        Overlay var8 = Overlay.PROGRESS;
        BossBar var40 = BossBar.bossBar(var39, 1.0F, var7, var8);
        this.bar = var40;
        PluginManager var24 = var1.getServer().getPluginManager();
        var24.registerEvents(this, var1);
    }

    public void start(int var1) {
        current = this;

        for (Player var19 : Bukkit.getOnlinePlayers()) {
            BossBar var27 = this.bar;
            var19.showBossBar(var27);
        }

        this.nextWave();
        this.bellToll(1681405437);
        BukkitScheduler var28 = Bukkit.getScheduler();
        BlissPlugin var36 = this.plugin;
        Runnable var43 = this::tick;
        BukkitTask var29 = var28.runTaskTimer(var36, var43, 20L, 20L);
        this.task = var29;
        BukkitScheduler var30 = Bukkit.getScheduler();
        BlissPlugin var38 = this.plugin;
        Runnable var45 = this::refreshBar;
        BukkitTask var31 = var30.runTaskTimer(var38, var45, 0L, 2L);
        this.renderTask = var31;
        BukkitScheduler var32 = Bukkit.getScheduler();
        BlissPlugin var40 = this.plugin;
        Runnable var47 = this::ambientBolt;
        BukkitTask var33 = var32.runTaskTimer(var40, var47, 35L, 35L);
        this.boltTask = var33;
        BukkitScheduler var34 = Bukkit.getScheduler();
        BlissPlugin var42 = this.plugin;
        Runnable var49 = this::raiderBehaviour;
        BukkitTask var35 = var34.runTaskTimer(var42, var49, 15L, 15L);
        this.tradeTask = var35;
        this.stormOn(106310841);
    }

    public void resume(int var1, boolean var2, int var3) {
        if (var1 < 1) {
            this.start(104815921);
        } else {
            current = this;
            this.wave = var1;
            this.betweenWaves = var2;
            this.finished = false;
            int var87 = composition(Math.min(var1, 8), 527118743).size();
            this.waveSize = var87;
            World var13 = this.center.getWorld();
            if (var13 != null) {
                int var14 = this.center.getBlockX() >> 4;
                int var15 = this.center.getBlockZ() >> 4;

                for (int var16 = -2; var16 <= 2; var16++) {
                    for (byte var153 = -2; var153 <= 2; var153 += 1) {
                        int var115 = var14 + var16;
                        int var128 = var15 + var153;
                        var13.getChunkAt(var115, var128);
                    }
                }

                for (Entity var17 : var13.getEntities()) {
                    if (!var17.isDead() && var17.isValid() && var17.getScoreboardTags().contains("bliss_last_raid")) {
                        Set var54 = this.alive;
                        UUID var93 = var17.getUniqueId();
                        var54.add(var93);
                    }
                }
            }

            for (Player var63 : Bukkit.getOnlinePlayers()) {
                BossBar var95 = this.bar;
                var63.showBossBar(var95);
            }

            BossBar var65 = this.bar;
            float var99 = (float)this.barProgress(1210770408);
            var65.progress(var99);
            if (this.betweenWaves) {
                long var101 = (long)Bukkit.getCurrentTick();
                this.refillStart = var101;
                BukkitScheduler var70 = Bukkit.getScheduler();
                BlissPlugin var104 = this.plugin;
                Runnable var117 = this::nextWave;
                var70.runTaskLater(var104, var117, 100L);
            }

            BukkitScheduler var105 = Bukkit.getScheduler();
            BlissPlugin var119 = this.plugin;
            Runnable var130 = this::tick;
            BukkitTask var106 = var105.runTaskTimer(var119, var130, 20L, 20L);
            this.task = var106;
            BukkitScheduler var107 = Bukkit.getScheduler();
            BlissPlugin var121 = this.plugin;
            Runnable var132 = this::refreshBar;
            BukkitTask var108 = var107.runTaskTimer(var121, var132, 0L, 2L);
            this.renderTask = var108;
            BukkitScheduler var109 = Bukkit.getScheduler();
            BlissPlugin var123 = this.plugin;
            Runnable var134 = this::ambientBolt;
            BukkitTask var110 = var109.runTaskTimer(var123, var134, 35L, 35L);
            this.boltTask = var110;
            BukkitScheduler var111 = Bukkit.getScheduler();
            BlissPlugin var125 = this.plugin;
            Runnable var136 = this::raiderBehaviour;
            BukkitTask var112 = var111.runTaskTimer(var125, var136, 15L, 15L);
            this.tradeTask = var112;
            this.stormOn(106310841);
        }
    }

    private void tick() {
        if (!this.finished) {
            for (Player var16 : Bukkit.getOnlinePlayers()) {
                BossBar var47 = this.bar;
                var16.showBossBar(var47);
            }

            Set<UUID> var18 = this.alive;
            Predicate<UUID> var48 = var0 -> {
                Entity var2 = Bukkit.getEntity(var0);
                boolean var9;
                if (var2 == null || !var2.isDead() && var2.isValid()) {
                    var9 = false;
                } else {
                    var9 = true;
                }

                return var9;
            };
            var18.removeIf(var48);
            if (this.waveSize > 0) {
                BossBar var23 = this.bar;
                float var52 = (float)this.barProgress(1210770408);
                var23.progress(var52);
            }

            this.witchesDrink(693138497);
            if (!this.betweenWaves && this.pendingSpawns <= 0 && this.alive.isEmpty()) {
                if (this.wave >= 8) {
                    this.victory(1699327546);
                    return;
                }

                this.betweenWaves = true;
                if (this.state != null) {
                    this.state.setRaidBetween(true, 2064038361);
                    this.state.save(1112893273);
                }

                long var57 = (long)Bukkit.getCurrentTick();
                this.refillStart = var57;
                this.bellToll(1681405437);
                BukkitScheduler var45 = Bukkit.getScheduler();
                BlissPlugin var60 = this.plugin;
                Runnable var61 = this::nextWave;
                var45.runTaskLater(var60, var61, 100L);
            }
        }
    }

    private void nextWave() {
        if (!this.finished) {
            int var53 = this.wave + 1;
            this.wave = var53;
            this.betweenWaves = false;
            this.refillStart = -1L;
            if (this.state != null) {
                EventState var20 = this.state;
                int var58 = this.wave;
                var20.setRaidWave(var58, 415126607);
                this.state.setRaidBetween(false, 2064038361);
                this.state.save(1112893273);
            }

            List var6 = composition(this.wave, 527118743);
            int var61 = var6.size();
            this.waveSize = var61;
            this.bar.progress(1.0F);
            Collections.shuffle(var6);
            int var64 = var6.size();
            this.pendingSpawns = var64;
            ThreadLocalRandom var7 = ThreadLocalRandom.current();
            long var8 = 0L;
            int var10 = 0;

            while (true) {
                int var66 = var6.size();
                if (var10 >= var66) {
                    Sound var67 = Sound.EVENT_RAID_HORN;
                    this.playAll(var67, 1.0F, 1020064225);
                    return;
                }

                int var69 = var7.nextInt(2);
                int var41 = 1 + var69;
                int var71 = var6.size();
                int var72 = var71 - var10;
                int var11 = Math.min(var41, var72);
                int var88 = var10 + var11;
                List var80 = var6.subList(var10, var88);
                ArrayList<EntityType> var43 = new ArrayList<>((List<EntityType>)var80);
                BukkitScheduler var44 = Bukkit.getScheduler();
                BlissPlugin var74 = this.plugin;
                Runnable var82 = () -> {
                    if (!this.finished) {
                        for (EntityType var6x : var43) {
                            this.spawnOne(var6x, 782542709);
                        }

                        int var17 = this.pendingSpawns;
                        int var19 = var43.size();
                        int var18 = var17 - var19;
                        this.pendingSpawns = var18;
                    }
                };
                var44.runTaskLater(var74, var82, var8);
                var10 += var11;
                var8 += 8L;
            }
        }
    }

    private void victory(int var1) {
        sweepRaiders(927052260);
        this.finished = true;
        this.cleanup(1541662022);
        String var24 = "&a&l✦ THE VILLAGE STANDS ✦ &7— the last raid is broken.";
        this.announce(var24, 118094494);
        Sound var25 = Sound.UI_TOAST_CHALLENGE_COMPLETE;
        this.playAll(var25, 1.0F, 1020064225);
        Sound var26 = Sound.ENTITY_PLAYER_LEVELUP;
        this.playAll(var26, 1.2F, 1020064225);
        this.awardHonoredOne(1919317136);
        this.state.endEvent(372527669);
        if (this.plugin.globalState(724248306).eventProgression(169255316) && this.plugin.globalState(724248306).raidAdvancesProg4(598254309)) {
            BlissCommand.applyProgression4(this.plugin, 679055365);
            String var27 = "&a✅ Villagers can now spawn and trade across the server.";
            this.announce(var27, 118094494);
        }
    }

    public void stop(boolean var1, int var2) {
        this.finished = true;
        if (var1) {
            Iterator var5 = this.alive.iterator();

            while (var5.hasNext()) {
                Entity var7 = Bukkit.getEntity((UUID)var5.next());
                if (var7 != null) {
                    var7.remove();
                }
            }

            sweepRaiders(927052260);
        }

        this.cleanup(1541662022);
    }

    public static int sweepRaiders(int var0) {
        int var3 = 0;
        Iterator var4 = Bukkit.getWorlds().iterator();

        while (var4.hasNext()) {
            for (Entity var7 : ((World)var4.next()).getEntities()) {
                if (var7.getScoreboardTags().contains("bliss_last_raid")) {
                    var7.remove();
                    var3++;
                }
            }
        }

        return var3;
    }

    private void cleanup(int var1) {
        HandlerList.unregisterAll(this);
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }

        if (this.renderTask != null) {
            this.renderTask.cancel();
            this.renderTask = null;
        }

        if (this.boltTask != null) {
            this.boltTask.cancel();
            this.boltTask = null;
        }

        if (this.tradeTask != null) {
            this.tradeTask.cancel();
            this.tradeTask = null;
        }

        this.restoreEdits(82354612);
        this.stormOff(1190504326);

        for (Player var35 : Bukkit.getOnlinePlayers()) {
            BossBar var42 = this.bar;
            var35.hideBossBar(var42);
        }

        VillagerRaid var36 = current;
        if (var36 == this) {
            current = null;
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onRaiderDamaged(EntityDamageByEntityEvent var1) {
        if (!this.finished) {
            if (var1.getEntity().getScoreboardTags().contains("bliss_last_raid")) {
                Player var5 = dealerOf(var1.getDamager(), 93154185);
                if (var5 != null) {
                    EventState var16 = this.state;
                    UUID var18 = var5.getUniqueId();
                    double var19 = var1.getFinalDamage();
                    var16.addRaidDamage(var18, var19, 1837322694);
                }
            }
        }
    }

    private static Player dealerOf(Entity var0, int var1) {
        if (var0 instanceof Player) {
            return (Player)var0;
        } else {
            if (var0 instanceof Projectile) {
                ProjectileSource var5 = ((Projectile)var0).getShooter();
                if (var5 instanceof Player) {
                    return (Player)var5;
                }
            }

            return null;
        }
    }

    private void awardHonoredOne(int var1) {
        BlissPlugin var4 = this.plugin;
        EventState var5 = this.state;
        crownTopDamager(var4, var5, 1946505142);
    }

    public static void crownTopDamager(BlissPlugin var0, EventState var1, int var2) {
        UUID var6 = var1.topRaidDamager(263540791);
        if (var6 != null) {
            Player var7 = Bukkit.getPlayer(var6);
            String var21;
            if (var7 != null) {
                var21 = var7.getName();
            } else {
                var21 = Bukkit.getOfflinePlayer(var6).getName();
            }

            String var8 = var21;
            if (var21 == null) {
                var8 = "Someone";
            }

            HonoredOneManager var19 = var0.honoredOne(783425701);
            String var22 = "&6✦ &e" + var8 + " &6dealt the most damage in the Last Raid and rises as the &l&eHonored One&6!";
            var19.assign(var6, var22, 209174169);
        }
    }

    private static List<EntityType> composition(int var0, int var1) {
        ArrayList var2 = new ArrayList();
        switch (var0) {
            case 1:
                EntityType var71 = EntityType.PILLAGER;
                add(var2, var71, 8, 605766036);
                EntityType var72 = EntityType.VINDICATOR;
                add(var2, var72, 3, 605766036);
                EntityType var73 = EntityType.RAVAGER;
                add(var2, var73, 2, 605766036);
                EntityType var74 = EntityType.WITCH;
                add(var2, var74, 2, 605766036);
                break;
            case 2:
                EntityType var87 = EntityType.PILLAGER;
                add(var2, var87, 10, 605766036);
                EntityType var88 = EntityType.VINDICATOR;
                add(var2, var88, 4, 605766036);
                EntityType var89 = EntityType.RAVAGER;
                add(var2, var89, 2, 605766036);
                EntityType var90 = EntityType.WITCH;
                add(var2, var90, 3, 605766036);
                EntityType var91 = EntityType.ZOMBIE;
                add(var2, var91, 3, 605766036);
                EntityType var92 = EntityType.ZOMBIFIED_PIGLIN;
                add(var2, var92, 2, 605766036);
                break;
            case 3:
                EntityType var59 = EntityType.PILLAGER;
                add(var2, var59, 10, 605766036);
                EntityType var60 = EntityType.VINDICATOR;
                add(var2, var60, 5, 605766036);
                EntityType var61 = EntityType.RAVAGER;
                add(var2, var61, 3, 605766036);
                EntityType var62 = EntityType.WITCH;
                add(var2, var62, 4, 605766036);
                EntityType var63 = EntityType.ZOMBIE;
                add(var2, var63, 4, 605766036);
                EntityType var64 = EntityType.ZOMBIFIED_PIGLIN;
                add(var2, var64, 3, 605766036);
                break;
            case 4:
                EntityType var65 = EntityType.PILLAGER;
                add(var2, var65, 12, 605766036);
                EntityType var66 = EntityType.VINDICATOR;
                add(var2, var66, 6, 605766036);
                EntityType var67 = EntityType.RAVAGER;
                add(var2, var67, 3, 605766036);
                EntityType var68 = EntityType.WITCH;
                add(var2, var68, 5, 605766036);
                EntityType var69 = EntityType.ZOMBIE;
                add(var2, var69, 5, 605766036);
                EntityType var70 = EntityType.ZOMBIFIED_PIGLIN;
                add(var2, var70, 3, 605766036);
                break;
            case 5:
                EntityType var81 = EntityType.PILLAGER;
                add(var2, var81, 12, 605766036);
                EntityType var82 = EntityType.VINDICATOR;
                add(var2, var82, 7, 605766036);
                EntityType var83 = EntityType.RAVAGER;
                add(var2, var83, 4, 605766036);
                EntityType var84 = EntityType.WITCH;
                add(var2, var84, 6, 605766036);
                EntityType var85 = EntityType.ZOMBIE;
                add(var2, var85, 5, 605766036);
                EntityType var86 = EntityType.ZOMBIFIED_PIGLIN;
                add(var2, var86, 4, 605766036);
                break;
            case 6:
                EntityType var53 = EntityType.PILLAGER;
                add(var2, var53, 14, 605766036);
                EntityType var54 = EntityType.VINDICATOR;
                add(var2, var54, 8, 605766036);
                EntityType var55 = EntityType.RAVAGER;
                add(var2, var55, 4, 605766036);
                EntityType var56 = EntityType.WITCH;
                add(var2, var56, 7, 605766036);
                EntityType var57 = EntityType.ZOMBIE;
                add(var2, var57, 6, 605766036);
                EntityType var58 = EntityType.ZOMBIFIED_PIGLIN;
                add(var2, var58, 4, 605766036);
                break;
            case 7:
                EntityType var93 = EntityType.PILLAGER;
                add(var2, var93, 14, 605766036);
                EntityType var94 = EntityType.VINDICATOR;
                add(var2, var94, 10, 605766036);
                EntityType var95 = EntityType.RAVAGER;
                add(var2, var95, 5, 605766036);
                EntityType var96 = EntityType.WITCH;
                add(var2, var96, 8, 605766036);
                EntityType var97 = EntityType.ZOMBIE;
                add(var2, var97, 7, 605766036);
                EntityType var98 = EntityType.ZOMBIFIED_PIGLIN;
                add(var2, var98, 5, 605766036);
                break;
            default:
                EntityType var75 = EntityType.PILLAGER;
                add(var2, var75, 16, 605766036);
                EntityType var76 = EntityType.VINDICATOR;
                add(var2, var76, 11, 605766036);
                EntityType var77 = EntityType.RAVAGER;
                add(var2, var77, 6, 605766036);
                EntityType var78 = EntityType.WITCH;
                add(var2, var78, 9, 605766036);
                EntityType var79 = EntityType.ZOMBIE;
                add(var2, var79, 8, 605766036);
                EntityType var80 = EntityType.ZOMBIFIED_PIGLIN;
                add(var2, var80, 5, 605766036);
        }

        return var2;
    }

    private static double mobScale(int var0) {
        Plugin var7 = Bukkit.getPluginManager().getPlugin("BlissPlugin");
        if (var7 == null) {
            return 1.0;
        } else {
            FileConfiguration var15 = var7.getConfig();
            String var22 = "villager-event.raid-mob-scale";
            double var5 = var15.getDouble(var22, 1.0);
            double var25 = Math.min(3.0, var5);
            return Math.max(0.05, var25);
        }
    }

    private static void add(List<EntityType> var0, EntityType var1, int var2, int var3) {
        double var11 = (double)(var2 * 2);
        double var6 = mobScale(1399238961);
        int var8 = (int)Math.round(var11 * var6);
        if (var2 > 0) {
            var8 = Math.max(1, var8);
        }

        for (int var9 = 0; var9 < var8; var9++) {
            var0.add(var1);
        }
    }

    private static void buffRaider(LivingEntity var0, int var1, int var2) {
        Attribute var4 = Attribute.MAX_HEALTH;
        AttributeInstance var13 = var0.getAttribute(var4);
        if (var13 != null) {
            double var24 = var13.getBaseValue();
            double var45 = (double)(var1 - 1);
            double var39 = 0.06 * var45;
            double var34 = 1.3 + var39;
            double var26 = var24 * var34;
            var13.setBaseValue(var26);
            double var29 = var13.getValue();
            var0.setHealth(var29);
        }

        PotionEffectType var36 = PotionEffectType.SPEED;
        PotionEffect var31 = new PotionEffect(var36, 1000000, 0, true, false, false);
        var0.addPotionEffect(var31);
        if (var1 >= 3) {
            PotionEffectType var37 = PotionEffectType.STRENGTH;
            PotionEffect var33 = new PotionEffect(var37, 1000000, 0, true, false, false);
            var0.addPotionEffect(var33);
        }
    }

    private void witchesDrink(int var1) {
        Iterator var10 = this.alive.iterator();

        while (var10.hasNext()) {
            Entity var12 = Bukkit.getEntity((UUID)var10.next());
            if (var12 instanceof Witch) {
                Witch var13 = (Witch)var12;
                if (var13.getFireTicks() > 0) {
                    PotionEffectType var3 = PotionEffectType.FIRE_RESISTANCE;
                    if (!var13.hasPotionEffect(var3)) {
                        PotionEffectType var4 = PotionEffectType.FIRE_RESISTANCE;
                        PotionEffect var35 = new PotionEffect(var4, 400, 0, true, true, true);
                        var13.addPotionEffect(var35);
                        World var34 = var13.getWorld();
                        Location var37 = var13.getLocation();
                        Sound var38 = Sound.ENTITY_WITCH_DRINK;
                        var34.playSound(var37, var38, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    private void arrivalStrike(World var1, Location var2, int var3) {
        ThreadLocalRandom var25 = ThreadLocalRandom.current();

        for (int var26 = 0; var26 < 3; var26++) {
            double var69 = (double)var26 * 0.4;
            double var27 = 1.7 - var69;
            double var29 = var25.nextDouble() * Math.PI * 2.0;

            for (int var31 = 0; var31 < 18; var31++) {
                double var92 = (double)var31;
                double var79 = (Math.PI * 2) * var92 / 18.0;
                double var32 = var29 + var79;
                Particle var63 = Particle.END_ROD;
                double var82 = var2.getX();
                double var98 = Math.cos(var32);
                double var100 = var98 * var27;
                double var84 = var82 + var100;
                double var105 = var2.getY() + 0.1;
                double var112 = var2.getZ();
                double var118 = Math.sin(var32);
                double var120 = var118 * var27;
                double var114 = var112 + var120;
                Particles.spawn(var1, var63, var84, var105, var114, 0, 0.0, 1.0, 0.0, 0.45, null, true, 954482185);
            }
        }

        for (int var123 = 0; var123 < 2; var123++) {
            BlissPlugin var39 = this.plugin;
            List var64 = DrawnBolt.path(var1, var2, 2.5, 12.0, var25, 2009889881);
            DrawnBolt.draw(var39, var1, var64, 7, 53726898);
        }

        Sound var65 = Sound.ENTITY_ZOMBIE_VILLAGER_CURE;
        var1.playSound(var2, var65, 1.2F, 1.0F);
    }

    private void assignTrade(Entity var1, int var2) {
        if (var1 instanceof Zombie) {
            ThreadLocalRandom var11 = ThreadLocalRandom.current();
            ArrayList var17 = new ArrayList();
            if (this.wave >= 2) {
                var17.add("bliss_raid_build");
            }

            if (this.wave >= 2) {
                var17.add("bliss_raid_break");
            }

            if (this.wave >= 4) {
                var17.add("bliss_raid_web");
            }

            if (!var17.isEmpty()) {
                double var62 = (double)(this.wave - 2);
                double var58 = 0.07 * var62;
                double var51 = 0.35 + var58;
                double var13 = Math.min(0.75, var51);
                double var37 = var11.nextDouble();
                if (!(var37 >= var13)) {
                    int var57 = var17.size();
                    int var56 = var11.nextInt(var57);
                    String var50 = (String)var17.get(var56);
                    var1.addScoreboardTag(var50);
                }
            }
        }
    }

    private void raiderBehaviour() {
        if (!this.finished) {
            long var5 = (long)Bukkit.getCurrentTick();
            List<VillagerRaid.Edit> var17 = this.edits;
            Predicate<VillagerRaid.Edit> var48 = var2 -> {
                long var10x = var2.until;
                if (var5 < var10x) {
                    return false;
                } else {
                    undo(var2, 1674512443);
                    return true;
                }
            };
            var17.removeIf(var48);
            Set var56 = this.alive;
            ArrayList var19 = new ArrayList(var56);
            Iterator var7 = var19.iterator();

            while (var7.hasNext()) {
                Entity var9 = Bukkit.getEntity((UUID)var7.next());
                if (var9 instanceof Mob) {
                    Mob var10 = (Mob)var9;
                    if (!var10.isDead() && var10.isValid()) {
                        this.retarget(var10, 472528997);
                        Set var11 = var10.getScoreboardTags();
                        if (var11.contains("bliss_raid_web")) {
                            this.webPlayer(var10, 730698413);
                        } else if (var11.contains("bliss_raid_build")) {
                            this.buildToward(var10, 871044537);
                        } else if (var11.contains("bliss_raid_break")) {
                            this.digToward(var10, 1146638377);
                        }
                    }
                }
            }
        }
    }

    private void retarget(Mob var1, int var2) {
        Object var5 = this.nearestVillager(var1, 542900876);
        if (var5 == null) {
            var5 = this.nearestPlayerTo(var1, 1403426661);
        }

        if (var5 != null) {
            LivingEntity var6 = var1.getTarget();
            if (var6 != var5 || !var6.isValid() || var6.isDead()) {
                var1.setTarget((LivingEntity)var5);
            }
        }
    }

    private LivingEntity nearestVillager(Mob var1, int var2) {
        Villager var7 = null;
        double var8 = Double.MAX_VALUE;

        for (int var10 = 1; var10 <= 3; var10++) {
            EventState var20 = this.state;
            Villager var11 = var20.getVillager(var10, 1571302102);
            if (var11 != null && !var11.isDead() && var11.isValid()) {
                World var28 = var11.getWorld();
                World var42 = var1.getWorld();
                if (var28.equals(var42)) {
                    Location var31 = var11.getLocation();
                    Location var44 = var1.getLocation();
                    double var12 = var31.distanceSquared(var44);
                    if (var12 < var8) {
                        var8 = var12;
                        var7 = var11;
                    }
                }
            }
        }

        return var7;
    }

    private Player nearestPlayerTo(Mob var1, int var2) {
        Player var7 = null;
        double var8 = Double.MAX_VALUE;

        for (Player var11 : var1.getWorld().getPlayers()) {
            if (!var11.isDead()) {
                GameMode var29 = var11.getGameMode();
                GameMode var4 = GameMode.SPECTATOR;
                if (var29 != var4) {
                    Location var31 = var11.getLocation();
                    Location var41 = var1.getLocation();
                    double var12 = var31.distanceSquared(var41);
                    if (var12 < var8) {
                        var8 = var12;
                        var7 = var11;
                    }
                }
            }
        }

        return var7;
    }

    private static boolean isRaider(Entity var0, int var1) {
        boolean var7;
        if (var0 != null && var0.getScoreboardTags().contains("bliss_last_raid")) {
            var7 = true;
        } else {
            var7 = false;
        }

        return var7;
    }

    @EventHandler(
        priority = EventPriority.LOWEST,
        ignoreCancelled = true
    )
    public void onRaiderFriendlyFire(EntityDamageByEntityEvent var1) {
        if (!this.finished) {
            if (isRaider(var1.getEntity(), 881495173)) {
                Entity var4 = var1.getDamager();
                if (var4 instanceof Projectile) {
                    ProjectileSource var7 = ((Projectile)var4).getShooter();
                    if (var7 instanceof Entity) {
                        var4 = (Entity)var7;
                    }
                }

                if (isRaider(var4, 881495173)) {
                    var1.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onRaiderTarget(EntityTargetEvent var1) {
        if (!this.finished) {
            if (isRaider(var1.getEntity(), 881495173)) {
                if (isRaider(var1.getTarget(), 881495173)) {
                    var1.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onRaiderTransform(EntityTransformEvent var1) {
        if (!this.finished) {
            if (isRaider(var1.getEntity(), 881495173)) {
                var1.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onRaiderRemoved(EntityRemoveEvent var1) {
        if (!this.finished) {
            Cause var6 = var1.getCause();
            Cause var3 = Cause.UNLOAD;
            if (var6 != var3) {
                Set var8 = this.alive;
                UUID var12 = var1.getEntity().getUniqueId();
                var8.remove(var12);
            }
        }
    }

    private void webPlayer(Mob var1, int var2) {
        Player var8 = null;
        double var9 = 144.0;

        for (Player var12 : var1.getWorld().getPlayers()) {
            Location var31 = var12.getLocation();
            Location var45 = var1.getLocation();
            double var13 = var31.distanceSquared(var45);
            if (var13 < var9) {
                var9 = var13;
                var8 = var12;
            }
        }

        if (var8 != null) {
            Block var41 = var8.getLocation().getBlock();
            Material var5 = Material.COBWEB;
            this.edit(var41, var5, 2034513863);
            World var26 = var1.getWorld();
            Location var43 = var8.getLocation();
            Sound var46 = Sound.BLOCK_WOOL_PLACE;
            var26.playSound(var43, var46, 1.2F, 0.8F);
        }
    }

    private void digToward(Mob var1, int var2) {
        LivingEntity var12 = var1.getTarget();
        if (var12 != null) {
            Vector var22 = var12.getLocation().toVector();
            Vector var54 = var1.getLocation().toVector();
            Vector var13 = var22.subtract(var54);
            var13.setY(0);
            if (!(var13.lengthSquared() < 1.0E-6)) {
                var13.normalize();
                Location var33 = var1.getLocation();
                Location var14 = var33.add(var13);
                Block var15 = var14.clone().add(0.0, 1.0, 0.0).getBlock();
                Block var16 = var14.getBlock();
                Block var52;
                if (this.breakable(var15, 1725497180)) {
                    var52 = var15;
                } else if (this.breakable(var16, 1725497180)) {
                    var52 = var16;
                } else {
                    var52 = null;
                }

                if (var52 != null) {
                    World var45 = var1.getWorld();
                    Location var61 = var52.getLocation();
                    Effect var66 = Effect.STEP_SOUND;
                    Material var70 = var52.getType();
                    var45.playEffect(var61, var66, var70);
                    World var47 = var1.getWorld();
                    Location var63 = var52.getLocation();
                    Sound var67 = Sound.BLOCK_STONE_BREAK;
                    var47.playSound(var63, var67, 1.4F, 0.8F);
                    var1.swingMainHand();
                    Material var68 = Material.AIR;
                    this.edit(var52, var68, 2034513863);
                }
            }
        }
    }

    private void buildToward(Mob var1, int var2) {
        LivingEntity var12 = var1.getTarget();
        if (var12 != null) {
            Vector var21 = var12.getLocation().toVector();
            Vector var65 = var1.getLocation().toVector();
            Vector var13 = var21.subtract(var65);
            var13.setY(0);
            if (!(var13.lengthSquared() < 1.0E-6)) {
                var13.normalize();
                Location var32 = var1.getLocation();
                Location var14 = var32.add(var13);
                Block var15 = var14.clone().add(0.0, -1.0, 0.0).getBlock();
                Block var16 = var14.getBlock();
                Block var84;
                if (var15.getType().isAir() && var16.getType().isAir()) {
                    var84 = var15;
                } else {
                    double var53 = var12.getLocation().getY();
                    double var79 = var1.getLocation().getY() + 1.5;
                    if (!(var53 > var79) || !var1.getLocation().getBlock().getType().isAir()) {
                        return;
                    }

                    var84 = var1.getLocation().getBlock();
                }

                var1.swingMainHand();
                World var49 = var1.getWorld();
                Location var71 = var84.getLocation();
                Sound var73 = Sound.BLOCK_STONE_PLACE;
                var49.playSound(var71, var73, 1.4F, 0.9F);
                Material var74 = Material.COBBLESTONE;
                this.edit(var84, var74, 2034513863);
            }
        }
    }

    private void edit(Block var1, Material var2, int var3) {
        Material var14 = var1.getType();
        if (var14 != var2) {
            if (!this.protectedBlock(var1, 1551897335)) {
                Location var11 = var1.getLocation();

                for (VillagerRaid.Edit var13 : this.edits) {
                    Location var32 = var13.loc;
                    if (var32.equals(var11)) {
                        var13.placed = var2;
                        var1.setType(var2, false);
                        return;
                    }
                }

                List var25 = this.edits;
                BlockData var44 = var1.getBlockData();
                long var46 = (long)(Bukkit.getCurrentTick() + 600);
                VillagerRaid.Edit var37 = new VillagerRaid.Edit(var11, var44, var2, var46, 912222555);
                var25.add(var37);
                var1.setType(var2, false);
            }
        }
    }

    private static void undo(VillagerRaid.Edit var0, int var1) {
        Block var5 = var0.loc.getBlock();
        Material var9 = var5.getType();
        Material var11 = var0.placed;
        if (var9 == var11) {
            BlockData var13 = var0.was;
            var5.setBlockData(var13, false);
        }
    }

    private boolean breakable(Block var1, int var2) {
        if (!var1.getType().isAir() && var1.getType().isSolid()) {
            boolean var13;
            if (!this.protectedBlock(var1, 1551897335)) {
                var13 = true;
            } else {
                var13 = false;
            }

            return var13;
        } else {
            return false;
        }
    }

    private boolean protectedBlock(Block var1, int var2) {
        Material var7 = var1.getType();
        Material var4 = Material.BEDROCK;
        if (var7 != var4) {
            Material var31 = Material.OBSIDIAN;
            if (var7 != var31) {
                Material var32 = Material.BEACON;
                if (var7 != var32) {
                    Material var33 = Material.END_PORTAL_FRAME;
                    if (var7 != var33) {
                        if (var1.getState() instanceof InventoryHolder) {
                            return true;
                        }

                        if (this.center.getWorld() != null) {
                            World var21 = var1.getWorld();
                            World var36 = this.center.getWorld();
                            if (var21.equals(var36)) {
                                Location var24 = var1.getLocation();
                                Location var38 = this.center;
                                if (var24.distanceSquared(var38) < 16.0) {
                                    return true;
                                }
                            }
                        }

                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void restoreEdits(int var1) {
        Iterator var3 = this.edits.iterator();

        while (var3.hasNext()) {
            undo((VillagerRaid.Edit)var3.next(), 1674512443);
        }

        this.edits.clear();
    }

    private void ambientBolt() {
        if (!this.finished) {
            World var10 = this.center.getWorld();
            if (var10 != null) {
                ThreadLocalRandom var11 = ThreadLocalRandom.current();
                Location var12 = this.center.clone();
                BlissPlugin var23 = this.plugin;
                List var30 = DrawnBolt.path(var10, var12, 20.0, 22.0, var11, 2009889881);
                DrawnBolt.draw(var23, var10, var30, 7, 53726898);
                if (var11.nextDouble() < 0.3) {
                    Sound var33 = Sound.ENTITY_ZOMBIE_VILLAGER_CURE;
                    var10.playSound(var12, var33, 1.2F, 1.0F);
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onWeatherLightning(LightningStrikeEvent var1) {
        if (!this.finished) {
            org.bukkit.event.weather.LightningStrikeEvent.Cause var6 = var1.getCause();
            org.bukkit.event.weather.LightningStrikeEvent.Cause var3 = org.bukkit.event.weather.LightningStrikeEvent.Cause.WEATHER;
            if (var6 == var3) {
                if (this.center.getWorld() != null) {
                    World var11 = var1.getWorld();
                    World var16 = this.center.getWorld();
                    if (var11.equals(var16)) {
                        var1.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    private void stormOn(int var1) {
        World var4 = this.center.getWorld();
        if (var4 != null) {
            boolean var16 = var4.hasStorm();
            this.prevStorm = var16;
            boolean var18 = var4.isThundering();
            this.prevThunder = var18;
            int var20 = var4.getWeatherDuration();
            this.prevWeather = var20;
            var4.setStorm(true);
            var4.setThundering(true);
            var4.setWeatherDuration(24000);
            var4.setThunderDuration(24000);
            this.stormHeld = true;
        }
    }

    private void stormOff(int var1) {
        if (this.stormHeld) {
            this.stormHeld = false;
            World var5 = this.center.getWorld();
            if (var5 != null) {
                boolean var16 = this.prevStorm;
                var5.setStorm(var16);
                boolean var18 = this.prevThunder;
                var5.setThundering(var18);
                int var21 = this.prevWeather;
                int var20 = Math.max(0, var21);
                var5.setWeatherDuration(var20);
            }
        }
    }

    private void spawnOne(EntityType var1, int var2) {
        World var18 = this.center.getWorld();
        if (var18 != null) {
            ThreadLocalRandom var19 = ThreadLocalRandom.current();
            double var20 = var19.nextDouble() * Math.PI * 2.0;
            double var145 = var19.nextDouble() * 1536.0;
            double var24 = Math.sqrt(64.0 + var145);
            double var58 = this.center.getX();
            double var149 = Math.cos(var20);
            double var151 = var149 * var24;
            double var26 = var58 + var151;
            double var64 = this.center.getZ();
            double var155 = Math.sin(var20);
            double var157 = var155 * var24;
            double var28 = var64 + var157;
            int var124 = (int)Math.floor(var26);
            int var163 = (int)Math.floor(var28);
            int var30 = var18.getHighestBlockYAt(var124, var163) + 1;
            double var167 = var26 + 0.5;
            double var175 = (double)var30;
            double var177 = var28 + 0.5;
            Location var71 = new Location(var18, var167, var175, var177);
            Entity var32 = var18.spawnEntity(var71, var1);
            if (var32.isValid()) {
                if (var32 instanceof Raider) {
                    ((Raider)var32).setCanJoinRaid(false);
                }

                if (var32 instanceof LivingEntity var179) {
                    var179.setRemoveWhenFarAway(false);
                    int var130 = this.wave;
                    buffRaider(var179, var130, 215391998);
                }

                if (var32 instanceof PigZombie) {
                    ((PigZombie)var32).setAngry(true);
                }

                this.assignTrade(var32, 247085117);
                if (var32 instanceof Zombie var181 && !(var32 instanceof PigZombie)) {
                    EntityEquipment var34 = var181.getEquipment();
                    if (var34 != null) {
                        Material var121;
                        if (var181.getScoreboardTags().contains("bliss_raid_break")) {
                            var121 = Material.DIAMOND_PICKAXE;
                        } else {
                            var121 = Material.IRON_AXE;
                        }

                        ItemStack var134 = new ItemStack(var121);
                        var34.setItemInMainHand(var134);
                        var34.setItemInMainHandDropChance(0.0F);
                    }
                }

                Player var182 = this.nearestPlayer(509133191);
                if (var32 instanceof Mob var183 && var182 != null) {
                    var183.setTarget(var182);
                }

                Set var116 = this.alive;
                UUID var138 = var32.getUniqueId();
                var116.add(var138);
                var32.addScoreboardTag("bliss_last_raid");
                this.arrivalStrike(var18, var71, 2061701143);
            }
        }
    }

    private Player nearestPlayer(int var1) {
        Player var6 = null;
        double var7 = Double.MAX_VALUE;

        for (Player var10 : Bukkit.getOnlinePlayers()) {
            World var24 = var10.getWorld();
            World var37 = this.center.getWorld();
            if (var24.equals(var37)) {
                Location var27 = var10.getLocation();
                Location var39 = this.center;
                double var11 = var27.distanceSquared(var39);
                if (var11 < var7) {
                    var7 = var11;
                    var6 = var10;
                }
            }
        }

        return var6;
    }

    private void announce(String var1, int var2) {
        for (Player var13 : Bukkit.getOnlinePlayers()) {
            Component var14 = Text.legacy(var1, 373192400);
            var13.sendMessage(var14);
        }
    }

    private void playAll(Sound var1, float var2, int var3) {
        for (Player var10 : Bukkit.getOnlinePlayers()) {
            Location var18 = var10.getLocation();
            var10.playSound(var18, var1, 1.0F, var2);
        }
    }

    private void playAll(String var1, float var2) {
        for (Player var9 : Bukkit.getOnlinePlayers()) {
            Location var17 = var9.getLocation();
            var9.playSound(var17, var1, 1.0F, var2);
        }
    }

    private double barProgress(int var1) {
        if (this.betweenWaves && this.refillStart >= 0L) {
            long var19 = (long)Bukkit.getCurrentTick();
            long var59 = this.refillStart;
            double var84 = (double)(var19 - var59) / 100.0;
            double var63 = Math.min(1.0, var84);
            return Math.max(0.0, var63);
        } else {
            if (this.chargeStart >= 0L) {
                long var32 = (long)Bukkit.getCurrentTick();
                long var68 = this.chargeStart;
                long var103 = var32 - var68;
                if (var103 < 40L) {
                    double var90 = (double)var103 / 40.0;
                    double var74 = Math.min(1.0, var90);
                    return Math.max(0.0, var74);
                }

                this.chargeStart = -1L;
            }

            if (this.waveSize <= 0) {
                return 1.0;
            } else {
                int var48 = this.alive.size();
                int var77 = this.pendingSpawns;
                int var57 = Math.max(0, var77);
                double var93 = (double)(var48 + var57);
                double var101 = (double)this.waveSize;
                double var95 = var93 / var101;
                double var80 = Math.min(1.0, var95);
                return Math.max(0.0, var80);
            }
        }
    }

    private void refreshBar() {
        if (!this.finished) {
            BossBar var5 = this.bar;
            float var9 = (float)this.barProgress(1210770408);
            var5.progress(var9);
        }
    }

    private void bellToll(int var1) {
        for (int var8 = 0; var8 < 4; var8++) {
            BukkitScheduler var10 = Bukkit.getScheduler();
            BlissPlugin var13 = this.plugin;
            Runnable var14 = () -> {
                Sound var2 = Sound.BLOCK_BELL_USE;
                this.playAll(var2, 0.5F, 1020064225);
            };
            long var17 = (long)var8 * 12L;
            var10.runTaskLater(var13, var14, var17);
        }
    }

    private static final class Edit {
        final Location loc;
        final BlockData was;
        final long until;
        Material placed;

        Edit(Location var1, BlockData var2, Material var3, long var4, int var6) {
            this.loc = var1;
            this.was = var2;
            this.placed = var3;
            this.until = var4;
        }
    }
}
