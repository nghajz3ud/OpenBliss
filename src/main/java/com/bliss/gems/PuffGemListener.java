package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.AchievementStore;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TrustStore;
import com.bliss.core.AchievementStore.Advancement;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.GemAbilityRules;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public final class PuffGemListener implements Listener {
    private static final long DJ_POST_LANDING_COOLDOWN_MS = 5000L;
    private static final int DJ_LAND_GRACE_TICKS = 12;
    private static final int DJ_LAND_MAX_TICKS = 600;
    private static final long DJ_IN_AIR_T1_MS = 10000L;
    private static final long DJ_IN_AIR_T2_MS = 5000L;
    private static final double DJ_STOMP_DAMAGE = 6.0;
    private static final double DJ_STOMP_RADIUS = 1.0;
    private static final double DJ_STOMP_BOUNCE = 0.42;
    private static final int DJ_STOMP_MAX_TICKS = 100;
    public final Map<UUID, Long> cd1;
    public final Map<UUID, Long> cd2;
    public final Map<UUID, Long> cd3;
    public final Map<UUID, Long> cd4;
    public final Set<UUID> stomping;
    public final Set<UUID> dashing;
    private static final long DJ_MOVE_RECENCY_TICKS = 6L;
    private static final long DJ_SPEED_RECENCY_TICKS = 2L;
    private static final double DJ_MOVE_MIN_SQ = 9.0E-4;
    private final Map<UUID, double[]> lastMove;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final TrustStore trust;

    public PuffGemListener(BlissPlugin var1, PlayerDataStore var2, int var3) {
        ConcurrentHashMap var5 = new ConcurrentHashMap();
        this.cd1 = var5;
        ConcurrentHashMap var16 = new ConcurrentHashMap();
        this.cd2 = var16;
        ConcurrentHashMap var17 = new ConcurrentHashMap();
        this.cd3 = var17;
        ConcurrentHashMap var18 = new ConcurrentHashMap();
        this.cd4 = var18;
        KeySetView var19 = ConcurrentHashMap.newKeySet();
        this.stomping = var19;
        KeySetView var20 = ConcurrentHashMap.newKeySet();
        this.dashing = var20;
        ConcurrentHashMap var21 = new ConcurrentHashMap();
        this.lastMove = var21;
        this.plugin = var1;
        this.players = var2;
        TrustStore var25 = var1.trust(310732214);
        this.trust = var25;
    }

    @EventHandler
    public void onFlightToggle(PlayerToggleFlightEvent var1) {
        Player var11 = var1.getPlayer();
        GameMode var21 = var11.getGameMode();
        GameMode var3 = GameMode.CREATIVE;
        if (var21 != var3) {
            var1.setCancelled(true);
            var11.setAllowFlight(false);
            PlayerDataStore var25 = this.players;
            UUID var65 = var11.getUniqueId();
            if (var25.get(var65, 1264010596).canUseAbilities(325243089)) {
                long var13 = System.currentTimeMillis();
                Map var32 = this.cd4;
                UUID var67 = var11.getUniqueId();
                Long var15 = (Long)var32.get(var67);
                if (var15 != null) {
                    long var37 = var15;
                    if (var37 > var13) {
                        return;
                    }
                }

                ItemStack var16 = var11.getInventory().getItemInMainHand();
                ItemStack var17 = var11.getInventory().getItemInOffHand();
                int var47 = BlissCoreItems.gemIdOf(var16, 382441079);
                int var69 = GemType.PUFF.id();
                int var18;
                if (var47 == var69) {
                    var18 = BlissCoreItems.tierOf(var16, 1633828214);
                } else {
                    int var49 = BlissCoreItems.gemIdOf(var17, 382441079);
                    int var71 = GemType.PUFF.id();
                    if (var49 == var71) {
                        var18 = BlissCoreItems.tierOf(var17, 1633828214);
                    } else {
                        GemType var72 = GemType.PUFF;
                        if (!GoldGem.isWieldingSoul(var11, var72, 100010596)) {
                            return;
                        }

                        var18 = BlissCoreItems.tierOf(GoldGem.heldGoldGem(var11, 780486261), 1633828214);
                    }
                }

                if (var18 == 2) {
                    String var82 = "&f\ud83d\udd2e";
                    this.puffDoubleJump(var11, 0.7, 0.6, var82, 5000L, 1640377298);
                } else {
                    String var8 = "&f\ud83d\udd3a";
                    this.puffDoubleJump(var11, 0.6, 0.4, var8, 10000L, 1640377298);
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent var1) {
        Location var9 = var1.getFrom();
        Location var10 = var1.getTo();
        if (var10 != null) {
            double var22 = var10.getX();
            double var48 = var9.getX();
            double var11 = var22 - var48;
            double var27 = var10.getY();
            double var51 = var9.getY();
            double var13 = var27 - var51;
            double var32 = var10.getZ();
            double var54 = var9.getZ();
            double var15 = var32 - var54;
            double var38 = var11 * var11;
            double var60 = var15 * var15;
            if (!(var38 + var60 < 9.0E-4)) {
                Map var44 = this.lastMove;
                UUID var47 = var1.getPlayer().getUniqueId();
                double[] var65 = new double[]{var11, var13, var15, 0.0};
                double var75 = (double)Bukkit.getCurrentTick();
                var65[3] = var75;
                var44.put(var47, var65);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        Map var4 = this.lastMove;
        UUID var7 = var1.getPlayer().getUniqueId();
        var4.remove(var7);
    }

    private double[] freshMoveSample(UUID var1, int var2) {
        Map var8 = this.lastMove;
        double[] var7 = (double[])var8.get(var1);
        if (var7 == null) {
            return null;
        } else {
            long var14 = (long)Bukkit.getCurrentTick();
            long var23 = (long)var7[3];
            return var14 - var23 > 2L ? null : var7;
        }
    }

    private Vector recentMoveDir(UUID var1, int var2) {
        Map var13 = this.lastMove;
        double[] var11 = (double[])var13.get(var1);
        if (var11 == null) {
            return null;
        } else {
            long var19 = (long)Bukkit.getCurrentTick();
            long var35 = (long)var11[3];
            if (var19 - var35 > 6L) {
                return null;
            } else {
                double var40 = var11[0];
                double var45 = var11[2];
                Vector var25 = new Vector(var40, 0.0, var45);
                return var25.lengthSquared() < 1.0E-6 ? null : var25.normalize();
            }
        }
    }

    private void puffDoubleJump(Player var1, double var2, double var4, String var6, long var7, int var9) {
        UUID var24 = var1.getUniqueId();
        PlayerDataStore var41 = this.players;
        PlayerData var25 = var41.get(var24, 1264010596);
        Map var44 = this.cd4;
        long var12 = System.currentTimeMillis();
        Long var133 = var12 + var7;
        var44.put(var24, var133);
        String var46 = "puff";
        String var111 = "double-jump";
        long var26 = GemTuning.cd(var46, var111, 5.0, 5000L, 1621983041);
        long var138 = var26 / 1000L;
        Component var114 = Text.legacy(var6 + " &bYou have used &fDouble Jump! &bNow on cooldown for &f" + var138 + " seconds", 373192400);
        var1.sendMessage(var114);
        Vector var28 = this.recentMoveDir(var24, 440596877);
        if (var28 == null) {
            Vector var29 = var1.getLocation().getDirection().setY(0);
            Vector var62;
            if (var29.lengthSquared() > 1.0E-4) {
                var62 = var29.normalize();
            } else {
                var62 = null;
            }

            var28 = var62;
        }

        String var142 = "puff";
        String var14 = "double-jump-up";
        double var143 = GemTuning.power(var142, var14, 1.0, 1503850832);
        double var224 = var4 * var143;
        String var145 = "puff";
        String var169 = "double-jump-forward";
        double var146 = GemTuning.power(var145, var169, 1.0, 1503850832);
        double var30 = var2 * var146;
        double var32 = var30;
        double var34 = var224;
        Set var76 = this.dashing;
        if (var76.contains(var24)) {
            double[] var36 = this.freshMoveSample(var24, 176794326);
            if (var36 != null) {
                double var149 = var36[0];
                double var184 = var36[0];
                double var151 = var149 * var184;
                double var187 = var36[2];
                double var202 = var36[2];
                double var189 = var187 * var202;
                double var155 = Math.sqrt(var151 + var189);
                var32 = Math.max(var30, var155);
                double var158 = var36[1];
                var34 = Math.max(var224, var158);
            }
        }

        Vector var90;
        if (var28 == null) {
            var90 = new Vector(0.0, var34, 0.0);
        } else {
            Vector var109 = var28.multiply(var32);
            var90 = var109.setY(var34);
        }

        var1.setVelocity(var90);
        double var37 = EnergyScale.radius(var25.energy(1160853318), 1465433230);
        World var97 = var1.getWorld();
        Particle var120 = Particle.CLOUD;
        Location var163 = var1.getLocation();
        double var195 = 0.1 * var37;
        double var210 = 0.1 * var37;
        double var216 = 0.1 * var37;
        Particles.spawn(var97, var120, var163, 5, var195, var210, var216, 0.0, 1986132876);
        if (!GlobalState.forPlayer(var1, 1433087673).legacyPuff(253639763)) {
            World var102 = var1.getWorld();
            Particle var121 = Particle.GUST_EMITTER_SMALL;
            Location var165 = var1.getLocation();
            Particles.spawn(var102, var121, var165, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
            World var104 = var1.getWorld();
            Location var123 = var1.getLocation();
            Sound var166 = Sound.ENTITY_BREEZE_JUMP;
            SoundCategory var174 = SoundCategory.PLAYERS;
            var104.playSound(var123, var166, var174, 1.0F, 1.0F);
        }

        this.scheduleTrail(var1, 176768536);
        this.scheduleLandingReset(var1, var7, 1505972806);
        this.scheduleStompCheck(var1, 1117806902);
    }

    private void scheduleStompCheck(final Player var1, int var2) {
        BukkitRunnable var3 = new BukkitRunnable() {
            int ticks = 0;
            boolean launched = false;

            public void run() {
                if (!var1.isOnline()) {
                    this.cancel();
                } else {
                    int var124 = this.ticks + 1;
                    this.ticks = var124;
                    if (var124 > 100) {
                        this.cancel();
                    } else if (!this.launched) {
                        if (var1.getVelocity().getY() > 0.0) {
                            this.launched = true;
                        }
                    } else if (var1.isOnGround()) {
                        this.cancel();
                    } else if (!(var1.getVelocity().getY() >= 0.0)) {
                        if (!GemAbilityRules.on(GemAbilityRules.GemAbility.PUFF_DJ_STOMP, 425337338)) {
                            this.cancel();
                        } else {
                            Location var15 = var1.getLocation();
                            World var52 = var15.getWorld();

                            for (Entity var17 : var52.getNearbyEntities(var15, 1.0, 1.0, 1.0)) {
                                if (var17 instanceof LivingEntity) {
                                    LivingEntity var18 = (LivingEntity)var17;
                                    Player var129 = var1;
                                    if (!var18.equals(var129) && !var18.isDead()) {
                                        if (var18 instanceof Player) {
                                            Player var19 = (Player)var18;
                                            TrustStore var74 = PuffGemListener.this.trust;
                                            UUID var132 = var1.getUniqueId();
                                            UUID var154 = var19.getUniqueId();
                                            if (var74.isTrusted(var132, var154, 1736364606)) {
                                                continue;
                                            }
                                        }

                                        double var77 = var15.getY();
                                        double var157 = var18.getLocation().getY();
                                        double var204 = var77 - var157;
                                        if (!(var204 < -0.1)) {
                                            double var164 = var18.getHeight() + 0.4;
                                            if (!(var204 > var164)) {
                                                UUID var20 = var1.getUniqueId();
                                                Set var92 = PuffGemListener.this.stomping;
                                                var92.add(var20);

                                                try {
                                                    if (GemAbilityRules.on(GemAbilityRules.GemAbility.PUFF_DJ_STOMP, 425337338)) {
                                                        Player var97 = var1;
                                                        GemAbilityRules.GemAbility var166 = GemAbilityRules.GemAbility.PUFF_DJ_STOMP;
                                                        String var175 = "puff";
                                                        String var184 = "double-jump-stomp";
                                                        double var176 = GemTuning.damage(var175, var184, 6.0, 1172948035);
                                                        double var167 = GemAbilityRules.scaled(var166, var176, 1684677315);
                                                        ConsistentDamage.dealFallAware(var97, var18, var167, 2104137282);
                                                    }
                                                } finally {
                                                    PuffGemListener.this.stomping.remove(var20);
                                                }
                                                Player var109 = var1;
                                                Vector var140 = var1.getVelocity().setY(0.42);
                                                var109.setVelocity(var140);
                                                PlayerDataStore var112 = PuffGemListener.this.players;
                                                UUID var143 = var1.getUniqueId();
                                                double var206 = EnergyScale.radius(var112.get(var143, 1264010596).energy(1160853318), 1465433230);
                                                World var118 = var18.getWorld();
                                                Particle var144 = Particle.CLOUD;
                                                Location var173 = var18.getLocation().add(0.0, 1.0, 0.0);
                                                double var187 = 0.2 * var206;
                                                double var196 = 0.2 * var206;
                                                double var200 = 0.2 * var206;
                                                Particles.spawn(var118, var144, var173, 8, var187, var196, var200, 0.0, 1986132876);
                                                World var120 = var18.getWorld();
                                                Location var146 = var18.getLocation();
                                                Sound var174 = Sound.ENTITY_GENERIC_BIG_FALL;
                                                var120.playSound(var146, var174, 1.0F, 1.2F);
                                                this.cancel();
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
        };
        BlissPlugin var10 = this.plugin;
        var3.runTaskTimer(var10, 1L, 1L);
    }

    private void scheduleTrail(Player var1, int var2) {
        for (int var7 = 1; var7 <= 6; var7++) {
            int var8 = (var7 - 1) * 2 + 1;
            int var9 = (var7 - 1) * 2 + 2;
            BukkitScheduler var19 = Bukkit.getScheduler();
            BlissPlugin var30 = this.plugin;
            Runnable var33 = () -> {
                World var13 = var1.getWorld();
                Particle var2x = Particle.CLOUD;
                Location var14 = var1.getLocation();
                Particles.spawn(var13, var2x, var14, 2, 0.1, 0.1, 0.1, 0.0, 1986132876);
            };
            long var36 = (long)var8;
            var19.runTaskLater(var30, var33, var36);
            BukkitScheduler var21 = Bukkit.getScheduler();
            BlissPlugin var32 = this.plugin;
            Runnable var35 = () -> {
                World var13 = var1.getWorld();
                Particle var2x = Particle.CLOUD;
                Location var14 = var1.getLocation();
                Particles.spawn(var13, var2x, var14, 3, 0.1, 0.1, 0.1, 0.0, 1986132876);
            };
            long var39 = (long)var9;
            var21.runTaskLater(var32, var35, var39);
        }
    }

    private void scheduleLandingReset(final Player var1, final long var2, int var4) {
        final UUID var11 = var1.getUniqueId();
        BukkitRunnable var13 = new BukkitRunnable() {
            int waited = 0;

            public void run() {
                int var41 = this.waited + 1;
                this.waited = var41;
                if (!var1.isOnline()) {
                    this.cancel();
                } else {
                    if (this.waited < 12 || !var1.isOnGround() && !var1.isInWater() && !var1.isClimbing()) {
                        if (this.waited >= 600) {
                            this.cancel();
                        } else {
                            Map var37 = PuffGemListener.this.cd4;
                            UUID var48 = var11;
                            long var61 = System.currentTimeMillis();
                            long var70 = var2;
                            Long var65 = var61 + var70;
                            var37.put(var48, var65);
                        }
                    } else {
                        String var22 = "puff";
                        String var43 = "double-jump";
                        long var23 = GemTuning.cd(var22, var43, 5.0, 5000L, 1621983041);
                        PlayerDataStore var53 = PuffGemListener.this.players;
                        UUID var66 = var11;
                        int var55 = var53.get(var66, 1264010596).energy(1160853318);
                        long var6 = EnergyScale.cd(var23, var55, 907518618);
                        Map var29 = PuffGemListener.this.cd4;
                        UUID var45 = var11;
                        long var56 = System.currentTimeMillis();
                        Long var60 = var56 + var6;
                        var29.put(var45, var60);
                        this.cancel();
                    }
                }
            }
        };
        BlissPlugin var16 = this.plugin;
        var13.runTaskTimer(var16, 1L, 1L);
    }

    @EventHandler
    public void onPuffFallOrContact(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Player var8) {
            DamageCause var50 = var1.getCause();
            DamageCause var3 = DamageCause.FALL;
            if (var50 != var3) {
                DamageCause var42 = DamageCause.CONTACT;
                if (var50 != var42) {
                    return;
                }
            }

            if (this.isHoldingPuff(var8, 1435992693)) {
                PlayerDataStore var24 = this.players;
                UUID var45 = var8.getUniqueId();
                if (var24.get(var45, 1264010596).canUsePassives(2051432579)) {
                    DamageCause var46 = DamageCause.FALL;
                    if (var50 == var46) {
                        long var10 = (long)Math.ceil(var1.getDamage());
                        if (var10 > 0L) {
                            AchievementStore var40 = this.plugin.advancements(1975441698);
                            Advancement var49 = Advancement.ALMOST_FELL;
                            var40.progress(var8, var49, var10, 223202830);
                        }
                    }

                    var1.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPuffBlockTrample(PlayerInteractEvent var1) {
        Action var6 = var1.getAction();
        Action var3 = Action.PHYSICAL;
        if (var6 == var3) {
            if (var1.getClickedBlock() != null) {
                Material var11 = var1.getClickedBlock().getType();
                Material var22 = Material.FARMLAND;
                if (var11 == var22) {
                    Player var4 = var1.getPlayer();
                    if (this.isHoldingPuff(var4, 1435992693)) {
                        PlayerDataStore var17 = this.players;
                        UUID var25 = var4.getUniqueId();
                        if (var17.get(var25, 1264010596).canUsePassives(2051432579)) {
                            var1.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    private boolean isHoldingPuff(Player var1, int var2) {
        ItemStack var5 = var1.getInventory().getItemInMainHand();
        ItemStack var6 = var1.getInventory().getItemInOffHand();
        int var13 = BlissCoreItems.gemIdOf(var5, 382441079);
        int var19 = GemType.PUFF.id();
        if (var13 != var19) {
            int var15 = BlissCoreItems.gemIdOf(var6, 382441079);
            int var21 = GemType.PUFF.id();
            if (var15 != var21) {
                GemType var22 = GemType.PUFF;
                if (!GoldGem.hasAbsorbedSoul(var1, var22, 1603837876)) {
                    return false;
                }
            }
        }

        return true;
    }
}
