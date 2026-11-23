package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TagStore;
import com.bliss.core.TrustStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.GemAbilityRules;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class FluxKineticBurstListener implements Listener {
    private static final int RADIUS = 5;
    private static final double PUSH_SPEED = 2.0;
    private static final long COOLDOWN_MS = 30000L;
    private static final long DOUBLE_CLICK_WINDOW_MS = 1000L;
    private final BlissPlugin plugin;
    private final FluxGemListener flux;
    private final PlayerDataStore players;
    private final TagStore tags;
    private final TrustStore trust;
    private final Map<UUID, Long> firstClickAt;
    private final Map<UUID, Integer> lastClickTick;

    public FluxKineticBurstListener(BlissPlugin var1, FluxGemListener var2, PlayerDataStore var3, TagStore var4, TrustStore var5, int var6) {
        HashMap var8 = new HashMap();
        this.firstClickAt = var8;
        HashMap var16 = new HashMap();
        this.lastClickTick = var16;
        this.plugin = var1;
        this.flux = var2;
        this.players = var3;
        this.tags = var4;
        this.trust = var5;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var31 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var31 != var3) {
            Action var33 = var1.getAction();
            Action var141 = Action.RIGHT_CLICK_BLOCK;
            if (var33 != var141) {
                return;
            }
        }

        Player var14 = var1.getPlayer();
        UUID var15 = var14.getUniqueId();
        int var16 = Bukkit.getCurrentTick();
        Map<UUID, Integer> var40 = this.lastClickTick;
        Integer var167 = var16;
        Integer var17 = var40.put(var15, var167);
        if (var17 != null) {
            int var45 = var17;
            if (var45 == var16) {
                return;
            }
        }

        TagStore var47 = this.tags;
        if (!var47.isTagged(var15, 995676415)) {
            if (GemAbilityRules.on(GemAbilityRules.GemAbility.FLUX_KINETIC_BURST, 425337338)) {
                PlayerDataStore var52 = this.players;
                PlayerData var18 = var52.get(var15, 1264010596);
                if (var18.canUseAbilities(325243089)) {
                    if (isSwordAxeOrAir(var14.getInventory().getItemInMainHand(), 2019019186)) {
                        GemType var146 = GemType.FLUX;
                        boolean var20 = GoldGem.isWieldingSoulOff(var14, var146, 1989261555);
                        ItemStack var21 = var14.getInventory().getItemInOffHand();
                        int var67 = BlissCoreItems.gemIdOf(var21, 382441079);
                        int var148 = GemType.FLUX.id();
                        if (var67 == var148 || var20) {
                            if (!var20) {
                                int var22 = BlissCoreItems.tierOf(var21, 1633828214);
                                if (var22 != 1 && var22 != 2) {
                                    return;
                                }
                            }

                            if (!var14.getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType().name().contains("COPPER")) {
                                long var23;
                                label90: {
                                    var23 = System.currentTimeMillis();
                                    if (GlobalState.forPlayer(var14, 1433087673).sneakCast(704684993)) {
                                        if (!var14.isSneaking()) {
                                            return;
                                        }

                                        Map var139 = this.firstClickAt;
                                        var139.remove(var15);
                                    } else {
                                        Map var89 = this.firstClickAt;
                                        Long var26 = (Long)var89.get(var15);
                                        if (var26 == null) {
                                            break label90;
                                        }

                                        long var169 = var26;
                                        if (var23 - var169 > 1000L) {
                                            break label90;
                                        }

                                        Map var99 = this.firstClickAt;
                                        var99.remove(var15);
                                    }

                                    Map var103 = this.flux.kineticBurstExpiry;
                                    Long var200 = (Long)var103.get(var15);
                                    if (var200 != null) {
                                        long var108 = var200;
                                        if (var108 > var23) {
                                            return;
                                        }
                                    }

                                    long var113;
                                    if (GlobalState.isHoldingDragonEgg(var14, 2044938455)) {
                                        var113 = 15000L;
                                    } else {
                                        var113 = 30000L;
                                    }

                                    Map var117 = this.flux.kineticBurstExpiry;
                                    String var7 = "flux";
                                    String var196 = "kinetic-burst";
                                    long var192 = GemTuning.cd(var7, var196, 30.0, var113, 1621983041);
                                    int var198 = var18.energy(1160853318);
                                    long var194 = EnergyScale.cd(var192, var198, 907518618);
                                    Long var179 = var23 + var194;
                                    var117.put(var15, var179);
                                    Component var159 = Text.legacy("&b\ud83d\udd2e You have activated &#FF686FStatic Burst", 373192400);
                                    var14.sendMessage(var159);
                                    Map<UUID, Double> var122 = this.flux.storedDamage;
                                    Double var182 = 0.0;
                                    double var29 = var122.getOrDefault(var15, var182);
                                    this.applyBurst(var14, var15, var29, 1028157738);
                                    Location var163 = var14.getLocation();
                                    this.playBurstVisual(var163, 1030315083);
                                    Map var131 = this.flux.storedDamage;
                                    Double var186 = 0.0;
                                    var131.put(var15, var186);
                                    return;
                                }

                                Map var134 = this.firstClickAt;
                                Long var189 = var23;
                                var134.put(var15, var189);
                            }
                        }
                    }
                }
            }
        }
    }

    private void applyBurst(Player var1, UUID var2, double var3, int var5) {
        Location var13 = var1.getLocation();
        String var8 = "flux";
        String var9 = "kinetic-burst";
        double var91 = GemTuning.radius(var8, var9, 5.0, 1058601812);
        int var14 = (int)Math.max(1.0, var91);
        String var29 = "flux";
        String var7 = "kinetic-burst-push";
        double var15 = GemTuning.power(var29, var7, 2.0, 1503850832);
        World var33 = var1.getWorld();
        double var96 = (double)var14;
        double var125 = (double)var14;
        double var130 = (double)var14;

        for (Entity var18 : var33.getNearbyEntities(var13, var96, var125, var130)) {
            if (var18 instanceof LivingEntity) {
                LivingEntity var19 = (LivingEntity)var18;
                UUID var46 = var18.getUniqueId();
                if (!var46.equals(var2)) {
                    if (var18 instanceof Player) {
                        Player var20 = (Player)var18;
                        TrustStore var53 = this.trust;
                        UUID var99 = var20.getUniqueId();
                        if (var53.isTrusted(var2, var99, 1736364606)) {
                            continue;
                        }
                    }

                    Vector var57 = var18.getLocation().toVector();
                    Vector var87 = var13.toVector();
                    Vector var132 = var57.subtract(var87);
                    if (var132.lengthSquared() < 1.0E-6) {
                        Vector var63 = new Vector(0, 1, 0);
                        var132 = var63;
                    } else {
                        var132.normalize();
                    }

                    Vector var89 = var132.multiply(var15);
                    var18.setVelocity(var89);
                    double var69 = var3 * 5.0 / 100.0;
                    double var110 = var19.getHealth();
                    double var71 = var69 * var110;
                    String var112 = "flux";
                    String var121 = "kinetic-burst";
                    double var113 = GemTuning.damage(var112, var121, 1.0, 1172948035);
                    double var21 = var71 * var113;
                    if (var21 > 0.0 && GemAbilityRules.on(GemAbilityRules.GemAbility.FLUX_KINETIC_BURST, 425337338)) {
                        GemAbilityRules.GemAbility var117 = GemAbilityRules.GemAbility.FLUX_KINETIC_BURST;
                        double var118 = GemAbilityRules.scaled(var117, var21, 1684677315);
                        ConsistentDamage.deal(var1, var19, var118, 1092002170);
                    }
                }
            }
        }
    }

    private static boolean isSwordAxeOrAir(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var6 = var0.getType();
            Material var3 = Material.AIR;
            if (var6 != var3) {
                Material var4 = var0.getType();
                return switch (var4) {
                    case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD, WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE-> true;
                    default -> false;
                };
            }
        }

        return true;
    }

    private void playBurstVisual(final Location var1, int var2) {
        BukkitRunnable var3 = new BukkitRunnable() {
            int i = 0;
            double s = 1.0;

            public void run() {
                if (this.i < 9) {
                    FluxKineticBurstListener var8 = FluxKineticBurstListener.this;
                    Location var15 = var1;
                    double var24 = this.s;
                    var8.drawFluxCircle(var15, var24, 2046845242);
                    double var18 = this.s + 0.5;
                    this.s = var18;
                    int var21 = this.i + 1;
                    this.i = var21;
                } else {
                    FluxKineticBurstListener var12 = FluxKineticBurstListener.this;
                    Location var23 = var1;
                    double var28 = this.s;
                    var12.drawFluxCircleLast(var23, var28, 1055833837);
                    this.cancel();
                }
            }
        };
        BlissPlugin var10 = this.plugin;
        var3.runTaskTimer(var10, 0L, 1L);
    }

    private void drawFluxCircleLast(final Location var1, final double var2, int var4) {
        BukkitRunnable var5 = new BukkitRunnable() {
            int n = 0;

            public void run() {
                if (this.n >= 7) {
                    this.cancel();
                } else {
                    FluxKineticBurstListener var6 = FluxKineticBurstListener.this;
                    Location var10 = var1;
                    double var13 = var2;
                    var6.drawFluxCircle(var10, var13, 2046845242);
                    int var12 = this.n + 1;
                    this.n = var12;
                }
            }
        };
        BlissPlugin var12 = this.plugin;
        var5.runTaskTimer(var12, 0L, 6L);
    }

    private void drawFluxCircle(Location var1, double var2, int var4) {
        int var74 = (int)Math.floor((Math.PI * 2) * var2 / 0.1);
        int var27 = Math.max(1, var74);
        Color var81 = Color.fromRGB(94, 215, 255);
        DustOptions var43 = new DustOptions(var81, 1.0F);
        DustOptions var28 = var43;

        for (int var29 = 0; var29 < var27; var29++) {
            double var49 = (double)var29 * 360.0;
            double var85 = (double)var27;
            double var30 = Math.toRadians(var49 / var85);
            double var89 = Math.cos(var30);
            double var32 = var2 * var89;
            double var93 = Math.sin(var30);
            double var34 = var2 * var93;
            Location var64 = var1.clone();
            Location var36 = var64.add(var32, 0.0, var34);
            World var67 = var1.getWorld();
            Particle var78 = Particle.DUST;
            Particles.spawn(var67, var78, var36, 1, 0.0, 0.0, 0.0, 0.0, var28, 1141219473);
            World var69 = var1.getWorld();
            Particle var79 = Particle.SMOKE;
            Particles.spawn(var69, var79, var36, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
        }
    }
}
