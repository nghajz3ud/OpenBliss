package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public final class SpeedThunderStepListener implements Listener {
    private static final double DISTANCE = 6.0;
    private static final double STEP = 0.5;
    private static final long COOLDOWN_MS = 15000L;
    private static final long REFUND_MS = 5000L;
    private static final int RING_POINTS = 40;
    private static final double RING_RADIUS = 1.5;
    private static final long DOUBLE_WINDOW_MS = 500L;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final SpeedGemListener speed;
    private final Map<UUID, Long> lastOffhandClick;
    private final Map<UUID, Integer> lastInteractTick;

    public SpeedThunderStepListener(BlissPlugin var1, PlayerDataStore var2, SpeedGemListener var3, int var4) {
        ConcurrentHashMap var6 = new ConcurrentHashMap();
        this.lastOffhandClick = var6;
        ConcurrentHashMap var12 = new ConcurrentHashMap();
        this.lastInteractTick = var12;
        this.plugin = var1;
        this.players = var2;
        this.speed = var3;
    }

    @EventHandler
    public void onOffhandDoubleRightClick(PlayerInteractEvent var1) {
        Action var37 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var37 != var3) {
            Action var39 = var1.getAction();
            Action var163 = Action.RIGHT_CLICK_BLOCK;
            if (var39 != var163) {
                return;
            }
        }

        GlobalState var42 = GlobalState.forPlayer(var1.getPlayer(), 1433087673);
        Player var165 = var1.getPlayer();
        if (!var42.legacySpeedFor(var165, 1914133154)) {
            Player var21 = var1.getPlayer();
            int var22 = Bukkit.getCurrentTick();
            Map var48 = this.lastInteractTick;
            UUID var167 = var21.getUniqueId();
            Integer var23 = (Integer)var48.get(var167);
            if (var23 != null) {
                int var53 = var23;
                if (var53 == var22) {
                    return;
                }
            }

            Map var55 = this.lastInteractTick;
            UUID var170 = var21.getUniqueId();
            Integer var210 = var22;
            var55.put(var170, var210);
            int var60 = BlissCoreItems.gemIdOf(var21.getInventory().getItemInOffHand(), 382441079);
            int var172 = GemType.SPEED.id();
            if (var60 != var172) {
                GemType var173 = GemType.SPEED;
                if (!GoldGem.isWieldingSoulOff(var21, var173, 1989261555)) {
                    return;
                }
            }

            if (!BlissCoreItems.isUtilityHead(var21.getInventory().getItemInMainHand(), 1077770594)) {
                if (!HandUse.mainHandInUse(var1, 2065896031)) {
                    Action var70 = var1.getAction();
                    Action var174 = Action.RIGHT_CLICK_BLOCK;
                    if (var70 != var174 || var21.isSneaking() || var1.getClickedBlock() == null || !var1.getClickedBlock().getType().isInteractable()) {
                        UUID var24;
                        long var25;
                        label96: {
                            var24 = var21.getUniqueId();
                            var25 = System.currentTimeMillis();
                            if (GlobalState.forPlayer(var21, 1433087673).sneakCast(704684993)) {
                                if (!var21.isSneaking()) {
                                    return;
                                }

                                Map var90 = this.lastOffhandClick;
                                var90.remove(var24);
                            } else {
                                Map var148 = this.lastOffhandClick;
                                Long var263 = (Long)var148.get(var24);
                                if (var263 == null) {
                                    break label96;
                                }

                                long var229 = var263;
                                if (var25 - var229 > 500L) {
                                    break label96;
                                }

                                Map var158 = this.lastOffhandClick;
                                var158.remove(var24);
                            }

                            PlayerDataStore var93 = this.players;
                            PlayerData var28 = var93.get(var24, 1264010596);
                            if (!var28.canUseAbilities(325243089)) {
                                return;
                            }

                            Map var99 = this.speed.thunderStepCd;
                            Long var29 = (Long)var99.get(var24);
                            if (var29 != null) {
                                long var104 = var29;
                                if (var104 > var25) {
                                    long var179 = var29;
                                    Component var185 = Text.legacy(
                                        "<##FEFD17>\ud83d\udd2e <##ED8790>Thunder Step <##B8FFFB>is on cooldown for <##82F3FF>"
                                            + readable(var179 - var25, 1150763481),
                                        373192400
                                    );
                                    var21.sendMessage(var185);
                                    return;
                                }
                            }

                            Location var30 = this.computeBlink(var21, 1929458796);
                            World var111 = var21.getWorld();
                            World var188 = var30.getWorld();
                            if (var111.equals(var188)) {
                                Location var190 = var21.getLocation();
                                if (var30.distanceSquared(var190) < 0.01) {
                                    Component var206 = Text.legacy(
                                        "<##FEFD17>\ud83d\udd2e <##B8FFFB>Thunder Step needs somewhere to go - look where you want to blink.", 373192400
                                    );
                                    var21.sendMessage(var206);
                                    return;
                                }
                            }

                            Location var192 = var21.getEyeLocation();
                            this.spawnHeadRing(var192, 1268166875);
                            World var119 = var21.getWorld();
                            Location var194 = var21.getLocation();
                            Sound var215 = Sound.ENTITY_WIND_CHARGE_WIND_BURST;
                            SoundCategory var236 = SoundCategory.PLAYERS;
                            var119.playSound(var194, var215, var236, 1.0F, 1.0F);
                            var21.teleport(var30);
                            double var31 = EnergyScale.radius(var28.energy(1160853318), 1465433230);
                            World var127 = var30.getWorld();
                            Particle var196 = Particle.DUST;
                            Location var218 = var30.clone().add(0.0, 1.0, 0.0);
                            double var242 = 0.4 * var31;
                            double var252 = 0.6 * var31;
                            double var256 = 0.4 * var31;
                            Color var261 = Color.fromRGB(255, 255, 0);
                            DustOptions var17 = new DustOptions(var261, 1.5F);
                            Particles.spawn(var127, var196, var218, 30, var242, var252, var256, 0.0, var17, true, 1748031581);
                            long var130;
                            if (GlobalState.isHoldingDragonEgg(var21, 2044938455)) {
                                var130 = 7500L;
                            } else {
                                var130 = 15000L;
                            }

                            String var132 = "speed";
                            String var197 = "thunder-step";
                            long var133 = GemTuning.cd(var132, var197, 15.0, var130, 1621983041);
                            int var222 = var28.energy(1160853318);
                            long var35 = EnergyScale.cd(var133, var222, 907518618);
                            Map var139 = this.speed.thunderStepCd;
                            Long var227 = var25 + var35;
                            var139.put(var24, var227);
                            Set var143 = this.speed.thunderStepPrimed;
                            var143.add(var24);
                            Component var204 = Text.legacy(
                                "<##FEFD17>\ud83d\udd2e <##B8FFFB>You have used <##FEFD17>Thunder Step<##B8FFFB>! Now on cooldown for <##82F3FF>"
                                    + readable(var35, 1150763481),
                                373192400
                            );
                            var21.sendMessage(var204);
                            return;
                        }

                        Map var161 = this.lastOffhandClick;
                        Long var235 = var25;
                        var161.put(var24, var235);
                    }
                }
            }
        }
    }

    private Location computeBlink(Player var1, int var2) {
        Location var11 = var1.getLocation();
        Vector var12 = var1.getEyeLocation().getDirection();
        var12.setY(0);
        if (var12.lengthSquared() < 1.0E-6) {
            return var11.clone();
        } else {
            var12.normalize();
            var11.clone();
            Location var14 = var11.clone();

            for (double var15 = 0.5; var15 <= 6.000000001; var15 += 0.5) {
                Location var41 = var11.clone();
                Vector var65 = var12.clone();
                Vector var66 = var65.multiply(var15);
                Location var80 = var41.add(var66);
                if (var80.getBlock().getType().isSolid() || var80.clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid()) {
                    break;
                }

                var14 = var80.clone();
            }

            Location var81 = this.snapToGround(var14, 506798934);
            float var71 = var11.getYaw();
            var81.setYaw(var71);
            float var73 = var11.getPitch();
            var81.setPitch(var73);
            return var81;
        }
    }

    private Location snapToGround(Location var1, int var2) {
        World var12 = var1.getWorld();
        int var13 = var1.getBlockY();

        for (byte var14 = 2; var14 >= -4; var14 += -1) {
            double var50 = var1.getX();
            double var60 = (double)(var13 + var14);
            double var71 = var1.getZ();
            Location var23 = new Location(var12, var50, var60, var71);
            Location var16 = var23.clone().subtract(0.0, 1.0, 0.0);
            Location var17 = var23.clone().add(0.0, 1.0, 0.0);
            if (var16.getBlock().getType().isSolid() && !var23.getBlock().getType().isSolid() && !var17.getBlock().getType().isSolid()) {
                double var57 = var1.getX();
                double var68 = (double)(var13 + var14);
                double var74 = var1.getZ();
                float var76 = var1.getYaw();
                float var77 = var1.getPitch();
                return new Location(var12, var57, var68, var74, var76, var77);
            }
        }

        return var1.clone();
    }

    private void spawnHeadRing(Location var1, int var2) {
        World var19 = var1.getWorld();

        for (int var20 = 0; var20 < 40; var20++) {
            double var48 = (double)var20;
            double var21 = (Math.PI * 2) * var48 / 40.0;
            double var37 = var1.getX();
            double var62 = Math.cos(var21);
            double var54 = 1.5 * var62;
            double var23 = var37 + var54;
            double var42 = var1.getY();
            double var66 = Math.sin(var21);
            double var58 = 1.5 * var66;
            double var25 = var42 + var58;
            Particle var47 = Particle.CLOUD;
            double var71 = var1.getZ();
            Location var60 = new Location(var19, var23, var25, var71);
            Particles.spawn(var19, var47, var60, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPrimedHit(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var18) {
            if (var1.getEntity() instanceof LivingEntity var101) {
                DamageCause var34 = var1.getCause();
                DamageCause var3 = DamageCause.SONIC_BOOM;
                if (var34 != var3) {
                    DamageCause var36 = var1.getCause();
                    DamageCause var69 = DamageCause.THORNS;
                    if (var36 != var69) {
                        UUID var102 = var18.getUniqueId();
                        Set var41 = this.speed.thunderStepPrimed;
                        if (!var41.contains(var102)) {
                            return;
                        }

                        int var46 = BlissCoreItems.gemIdOf(var18.getInventory().getItemInMainHand(), 382441079);
                        int var72 = GemType.SPEED.id();
                        if (var46 != var72) {
                            int var50 = BlissCoreItems.gemIdOf(var18.getInventory().getItemInOffHand(), 382441079);
                            int var74 = GemType.SPEED.id();
                            if (var50 != var74) {
                                GemType var75 = GemType.SPEED;
                                if (!GoldGem.isWieldingSoul(var18, var75, 100010596)) {
                                    return;
                                }
                            }
                        }

                        Set var55 = this.speed.thunderStepPrimed;
                        var55.remove(var102);
                        Map var59 = this.speed.thunderStepCd;
                        Long var21 = (Long)var59.get(var102);
                        if (var21 != null) {
                            Map var65 = this.speed.thunderStepCd;
                            long var4 = System.currentTimeMillis();
                            long var89 = var21 - 5000L;
                            Long var82 = Math.max(var4, var89);
                            var65.put(var102, var82);
                        }

                        World var68 = var101.getWorld();
                        Particle var79 = Particle.CRIT;
                        Location var84 = var101.getLocation();
                        double var95 = var101.getHeight() / 2.0;
                        Location var85 = var84.add(0.0, var95, 0.0);
                        Particles.spawn(var68, var79, var85, 20, 0.5, 0.5, 0.5, 0.0, 1986132876);
                        return;
                    }
                }
            }
        }
    }

    private static String readable(long var0, int var2) {
        int var8 = (int)((var0 + 999L) / 1000L);
        int var9 = var8 / 60;
        int var10 = var8 % 60;
        if (var9 == 0) {
            return var10 + "s";
        } else {
            return var10 == 0 ? var9 + "m" : var9 + "m " + var10 + "s";
        }
    }
}
