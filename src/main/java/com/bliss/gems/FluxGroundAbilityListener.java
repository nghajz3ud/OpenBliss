package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TagStore;
import com.bliss.core.TrustStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.GemAbilityRules;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public final class FluxGroundAbilityListener implements Listener {
    private static final long FREEZE_TICKS = 60L;
    private static final Color TRAIL_COLOR = Color.fromRGB(16, 131, 173);
    private final BlissPlugin plugin;
    private final FluxGemListener flux;
    private final PlayerDataStore players;
    private final TagStore tags;
    private final TrustStore trust;

    public FluxGroundAbilityListener(BlissPlugin var1, FluxGemListener var2, PlayerDataStore var3, TagStore var4, TrustStore var5, int var6) {
        this.plugin = var1;
        this.flux = var2;
        this.players = var3;
        this.tags = var4;
        this.trust = var5;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST,
        ignoreCancelled = true
    )
    public void onMeleeGround(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var15) {
            if (var1.getEntity() instanceof Player var251) {
                DamageCause var51 = var1.getCause();
                DamageCause var3 = DamageCause.SONIC_BOOM;
                if (var51 != var3) {
                    DamageCause var53 = var1.getCause();
                    DamageCause var158 = DamageCause.THORNS;
                    if (var53 != var158) {
                        UUID var252 = var15.getUniqueId();
                        UUID var18 = var251.getUniqueId();
                        if (var252.equals(var18)) {
                            return;
                        }

                        TagStore var61 = this.tags;
                        if (var61.isTagged(var252, 995676415)) {
                            return;
                        }

                        if (!GemAbilityRules.on(GemAbilityRules.GemAbility.FLUX_GROUND, 425337338)) {
                            return;
                        }

                        ItemStack var19 = var15.getInventory().getItemInMainHand();
                        GemType var161 = GemType.FLUX;
                        boolean var20 = GoldGem.isWieldingSoulMain(var15, var161, 244096655);
                        int var71 = BlissCoreItems.gemIdOf(var19, 382441079);
                        int var163 = GemType.FLUX.id();
                        if (var71 != var163 && !var20) {
                            return;
                        }

                        if (BlissCoreItems.tierOf(var19, 1633828214) != 2 && !var20) {
                            return;
                        }

                        PlayerDataStore var77 = this.players;
                        PlayerData var21 = var77.get(var252, 1264010596);
                        if (!var21.canUsePowers(1616892701)) {
                            return;
                        }

                        long var22 = System.currentTimeMillis();
                        Map var85 = this.flux.cd2Expiry;
                        Long var24 = (Long)var85.get(var252);
                        if (var24 != null) {
                            long var90 = var24;
                            if (var90 > var22) {
                                return;
                            }
                        }

                        TrustStore var94 = this.trust;
                        if (var94.isTrusted(var252, var18, 1736364606)) {
                            Component var190 = Text.legacy("&#FFD773\ud83d\udd2e &fYou cannot cast negative powers on allies!", 373192400);
                            var15.sendMessage(var190);
                            return;
                        }

                        PotionEffectType var168 = PotionEffectType.INVISIBILITY;
                        boolean var25 = var251.hasPotionEffect(var168);
                        PotionEffectType var169 = PotionEffectType.INVISIBILITY;
                        boolean var26 = var15.hasPotionEffect(var169);
                        String var156;
                        if (var25) {
                            var156 = "&k???";
                        } else {
                            var156 = var251.getName();
                        }

                        String var102;
                        if (var26) {
                            var102 = "&k???";
                        } else {
                            var102 = var15.getName();
                        }

                        Component var172 = Text.legacy("&b\ud83d\udd2e You have used Ground on &#0EC912" + var156, 373192400);
                        var15.sendMessage(var172);
                        Component var175 = Text.legacy("&b\ud83d\udd2e You have been affected with Ground by &#0EC912&b" + var102, 373192400);
                        var251.sendMessage(var175);
                        int var106 = var21.energy(1160853318);
                        FluxGemListener var177 = this.flux;
                        boolean var178 = var177.hasDragonEgg(var252, 1897338785);
                        long var29 = FluxGemListener.beamCooldownSeconds(var106, var178, 739019257);
                        Map var111 = this.flux.cd2Expiry;
                        String var7 = "flux";
                        String var8 = "ground";
                        long var249 = var29 * 1000L;
                        long var235 = GemTuning.cd(var7, var8, 30.0, var249, 1621983041);
                        int var248 = var21.energy(1160853318);
                        long var237 = EnergyScale.cd(var235, var248, 907518618);
                        Long var197 = var22 + var237;
                        var111.put(var252, var197);
                        Map<UUID, Double> var200 = this.flux.fluxKinetic;
                        Double var241 = 0.0;
                        double var203 = var200.getOrDefault(var252, var241);
                        double var31 = Math.min(200.0, var203);
                        Map var119 = this.flux.fluxKinetic;
                        Double var207 = 0.0;
                        var119.put(var252, var207);
                        FluxGemListener var122 = this.flux;
                        String var182 = var15.getName();
                        var122.fluxKiller = var182;
                        Map var125 = this.flux.groundFlag;
                        Boolean var209 = true;
                        var125.put(var18, var209);
                        double var130 = var251.getHealth();
                        double var134 = var130 * var31 / 100.0;
                        String var214 = "flux";
                        String var229 = "ground";
                        double var215 = GemTuning.damage(var214, var229, 1.0, 1172948035);
                        double var35 = var134 * var215;
                        if (var35 > 0.0 && GemAbilityRules.on(GemAbilityRules.GemAbility.FLUX_GROUND, 425337338)) {
                            GemAbilityRules.GemAbility var219 = GemAbilityRules.GemAbility.FLUX_GROUND;
                            double var220 = GemAbilityRules.scaled(var219, var35, 1684677315);
                            ConsistentDamage.dealVanillaOrTrue(var15, var251, var220, 0.0, 1236326608);
                        }

                        this.playGroundTrail(var15, var251, 2004075376);
                        String var145 = "flux";
                        String var186 = "ground-freeze";
                        long var37 = Math.round(GemTuning.duration(var145, var186, 3.0, 812730832) * 20.0);
                        BukkitScheduler var152 = Bukkit.getScheduler();
                        BlissPlugin var188 = this.plugin;
                        Runnable var228 = () -> {
                            Map var7x = this.flux.groundFlag;
                            Boolean var18x = false;
                            var7x.put(var18, var18x);
                            String var10 = var15.getName();
                            String var16 = this.flux.fluxKiller;
                            if (var10.equals(var16)) {
                                this.flux.fluxKiller = null;
                            }
                        };
                        var152.runTaskLater(var188, var228, var37);
                        return;
                    }
                }
            }
        }
    }

    private void playGroundTrail(final Player var1, final Player var2, int var3) {
        BukkitRunnable var4 = new BukkitRunnable() {
            int i = 0;

            public void run() {
                if (this.i < 30 && var1.isOnline() && var2.isOnline()) {
                    Location var23 = var1.getLocation().add(0.0, 1.0, 0.0);
                    Location var31 = var2.getLocation().add(0.0, 1.0, 0.0);
                    FluxGroundAbilityListener.drawLine(var23, var31, 1236919242);
                    int var33 = this.i + 1;
                    this.i = var33;
                } else {
                    this.cancel();
                }
            }
        };
        BlissPlugin var10 = this.plugin;
        var4.runTaskTimer(var10, 0L, 2L);
    }

    private static void drawLine(Location var0, Location var1, int var2) {
        World var35 = var0.getWorld();
        World var67 = var1.getWorld();
        if (var35.equals(var67)) {
            int var73 = (int)(var0.distance(var1) / 0.5);
            int var24 = Math.max(1, var73);
            Color var5 = TRAIL_COLOR;
            DustOptions var42 = new DustOptions(var5, 1.5F);
            DustOptions var25 = var42;

            for (int var26 = 0; var26 <= var24; var26++) {
                double var46 = (double)var26;
                double var77 = (double)var24;
                double var27 = var46 / var77;
                double var51 = var0.getX();
                double var80 = var1.getX();
                double var103 = var0.getX();
                double var82 = var80 - var103;
                double var84 = var82 * var27;
                double var29 = var51 + var84;
                double var56 = var0.getY();
                double var87 = var1.getY();
                double var108 = var0.getY();
                double var89 = var87 - var108;
                double var91 = var89 * var27;
                double var31 = var56 + var91;
                double var61 = var0.getZ();
                double var94 = var1.getZ();
                double var113 = var0.getZ();
                double var96 = var94 - var113;
                double var98 = var96 * var27;
                double var33 = var61 + var98;
                World var66 = var0.getWorld();
                Particle var75 = Particle.DUST;
                Particles.spawn(var66, var75, var29, var31, var33, 1, 0.0, 0.0, 0.0, 0.0, var25, true, 954482185);
            }
        }
    }
}
