package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemAutoEnchants;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerDataStore;
import com.bliss.entity.MotifSpeechListener;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.AssessmentWindowBarTask;
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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class SpeedAuraTask {
    private static final long PERIOD_TICKS = 5L;
    private static final long AUTO_ENCHANT_COOLDOWN_MS = 5000L;
    private static final int PASSIVE_TICKS = 40;
    private static final int SPEED_AMP_T1 = 0;
    private static final int SPEED_AMP_T2 = 1;
    private static final int DOLPHINS_AMP = 0;
    private static final int AUTO_EFFICIENCY_T1 = 3;
    private static final int AUTO_EFFICIENCY_T2 = 5;
    private static final int SOUL_SPEED_T1 = 1;
    private static final int SOUL_SPEED_T2 = 3;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final SpeedGemListener speed;
    private BukkitTask task;

    public SpeedAuraTask(BlissPlugin var1, PlayerDataStore var2, SpeedGemListener var3, int var4) {
        this.plugin = var1;
        this.players = var2;
        this.speed = var3;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 5L, 5L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void tick() {
        long var8 = System.currentTimeMillis();

        for (Player var11 : Bukkit.getOnlinePlayers()) {
            GlobalState var30 = GlobalState.forPlayer(var11, 1433087673);
            if (!var30.legacySpeedFor(var11, 1914133154)) {
                UUID var12 = var11.getUniqueId();
                PlayerInventory var13 = var11.getInventory();
                ItemStack var14 = var13.getItemInMainHand();
                ItemStack var15 = var13.getItemInOffHand();
                int var41 = BlissCoreItems.gemIdOf(var14, 382441079);
                int var126 = GemType.SPEED.id();
                boolean var123;
                if (var41 == var126) {
                    var123 = true;
                } else {
                    var123 = false;
                }

                int var43 = BlissCoreItems.gemIdOf(var15, 382441079);
                int var128 = GemType.SPEED.id();
                boolean var122;
                if (var43 == var128) {
                    var122 = true;
                } else {
                    var122 = false;
                }

                GemType var129 = GemType.SPEED;
                boolean var18 = GoldGem.hasAbsorbedSoul(var11, var129, 1603837876);
                if (var18) {
                    PlayerDataStore var48 = this.players;
                    label99:
                    if (var48.get(var12, 1264010596).canUsePassives(2051432579)) {
                        this.applyPassive(var11, 2, 1411408681);
                        Map var54 = this.speed.cd1;
                        Long var19 = (Long)var54.get(var12);
                        if (var19 != null) {
                            long var59 = var19;
                            if (var59 > var8) {
                                break label99;
                            }
                        }

                        autoEnchant(var13, 2, 801701310);
                        Map var65 = this.speed.cd1;
                        Long var166 = var8 + 5000L;
                        var65.put(var12, var166);
                    } else {
                        String var116 = "speed";
                        String var156 = "speed";
                        if (GemTuning.passive(var116, var156, true, 14309468)) {
                            PotionEffectType var157 = PotionEffectType.SPEED;
                            GrantedEffect.strip(var11, var157, 553187889);
                        }

                        String var119 = "speed";
                        String var158 = "dolphins-grace";
                        if (GemTuning.passive(var119, var158, true, 14309468)) {
                            PotionEffectType var159 = PotionEffectType.DOLPHINS_GRACE;
                            GrantedEffect.strip(var11, var159, 553187889);
                        }
                    }
                }

                GemType var135 = GemType.SPEED;
                if (GoldGem.isWieldingSoul(var11, var135, 100010596)) {
                    if (!AssessmentWindowBarTask.isShowing(1134156722) && !MotifSpeechListener.isSpeakingTo(var11.getUniqueId(), 1524431478)) {
                        Component var138 = Text.legacy(this.buildActionBar(2, var12, 45233350), 373192400);
                        var11.sendActionBar(var138);
                    }
                } else if (!var18 && (var123 || var122)) {
                    PlayerDataStore var78 = this.players;
                    if (!var78.get(var12, 1264010596).canUsePassives(2051432579)) {
                        String var110 = "speed";
                        String var152 = "speed";
                        if (GemTuning.passive(var110, var152, true, 14309468)) {
                            PotionEffectType var153 = PotionEffectType.SPEED;
                            GrantedEffect.strip(var11, var153, 553187889);
                        }

                        String var113 = "speed";
                        String var154 = "dolphins-grace";
                        if (GemTuning.passive(var113, var154, true, 14309468)) {
                            PotionEffectType var155 = PotionEffectType.DOLPHINS_GRACE;
                            GrantedEffect.strip(var11, var155, 553187889);
                        }
                    } else {
                        ItemStack var124;
                        if (var123) {
                            var124 = var14;
                        } else {
                            var124 = var15;
                        }

                        int var20 = BlissCoreItems.tierOf(var124, 1633828214);
                        this.applyPassive(var11, var20, 1411408681);
                        PlayerDataStore var86 = this.players;
                        UUID var142 = var11.getUniqueId();
                        if (var86.get(var142, 1264010596).energy(1160853318) >= 2) {
                            PlayerDataStore var90 = this.players;
                            UUID var145 = var11.getUniqueId();
                            if (!var90.get(var145, 1264010596).hudSuppressed(859110487)) {
                                Component var148 = Text.legacy(this.buildActionBar(var20, var12, 45233350), 373192400);
                                var11.sendActionBar(var148);
                            }
                        }

                        if (var123) {
                            Map var97 = this.speed.cd1;
                            Long var21 = (Long)var97.get(var12);
                            if (var21 != null) {
                                long var102 = var21;
                                if (var102 > var8) {
                                    continue;
                                }
                            }

                            autoEnchant(var13, var20, 801701310);
                            Map var108 = this.speed.cd1;
                            Long var176 = var8 + 5000L;
                            var108.put(var12, var176);
                        }
                    }
                }
            }
        }
    }

    private void applyPassive(Player var1, int var2, int var3) {
        byte var45;
        if (var2 >= 2) {
            var45 = 1;
        } else {
            var45 = 0;
        }

        String var18 = "speed";
        String var46 = "dolphins-grace";
        boolean var16 = GemTuning.passive(var18, var46, true, 14309468);
        boolean var44;
        if (var16 && var1.isInWater() && bareFeet(var1, 810976865)) {
            var44 = true;
        } else {
            var44 = false;
        }

        String var25 = "speed";
        String var47 = "speed";
        if (GemTuning.passive(var25, var47, true, 14309468)) {
            if (var44) {
                PotionEffectType var48 = PotionEffectType.SPEED;
                GrantedEffect.strip(var1, var48, 553187889);
            } else {
                PotionEffectType var57 = PotionEffectType.SPEED;
                PotionEffect var54 = new PotionEffect(var57, 40, var45, true, false, false);
                GrantedEffect.grant(var1, var54, 491118892);
            }
        }

        if (var1.isInWater()) {
            if (var44) {
                PotionEffectType var56 = PotionEffectType.DOLPHINS_GRACE;
                PotionEffect var53 = new PotionEffect(var56, 40, 0, true, false, false);
                GrantedEffect.grant(var1, var53, 491118892);
            } else if (var16) {
                PotionEffectType var50 = PotionEffectType.DOLPHINS_GRACE;
                GrantedEffect.strip(var1, var50, 553187889);
            }

            if (var1.isSprinting()) {
                spawnWaterTrail(var1.getLocation().clone().subtract(0.0, 0.5, 0.0), 1024869063);
            }
        } else if (var16) {
            PotionEffectType var49 = PotionEffectType.DOLPHINS_GRACE;
            GrantedEffect.strip(var1, var49, 553187889);
        }
    }

    private static void spawnWaterTrail(Location var0, int var1) {
        if (var0.getWorld() != null) {
            Color var20 = Color.fromRGB(244, 255, 28);
            DustOptions var16 = new DustOptions(var20, 1.0F);
            World var18 = var0.getWorld();
            Particle var19 = Particle.DUST;
            Particles.spawn(var18, var19, var0, 1, 0.0, 0.0, 0.0, 0.0, var16, false, 1748031581);
        }
    }

    private static void autoEnchant(PlayerInventory var0, int var1, int var2) {
        Enchantment var4 = Enchantment.EFFICIENCY;
        byte var74;
        if (var1 >= 2) {
            var74 = 5;
        } else {
            var74 = 3;
        }

        int var7 = GemAutoEnchants.allow(var0, var4, var74, 987370355);
        Enchantment var53 = Enchantment.SOUL_SPEED;
        byte var73;
        if (var1 >= 2) {
            var73 = 3;
        } else {
            var73 = 1;
        }

        int var8 = GemAutoEnchants.allow(var0, var53, var73, 987370355);
        String var19 = "speed";
        String var54 = "auto-efficiency";
        boolean var9 = GemTuning.passive(var19, var54, true, 14309468);
        String var21 = "speed";
        String var55 = "auto-soul-speed";
        boolean var10 = GemTuning.passive(var21, var55, true, 14309468);

        for (ItemStack var14 : var0.getContents()) {
            if (var14 != null) {
                String var15 = var14.getType().name();
                if (!var15.endsWith("_PICKAXE") && !var15.endsWith("_AXE") && !var15.endsWith("_SHOVEL")) {
                    if (var15.endsWith("_BOOTS") && var10 && var8 > 0) {
                        Enchantment var62 = Enchantment.SOUL_SPEED;
                        int var46 = var14.getEnchantmentLevel(var62);
                        if (var46 < var8) {
                            Enchantment var64 = Enchantment.SOUL_SPEED;
                            var14.addUnsafeEnchantment(var64, var8);
                        }
                    }
                } else if (var9 && var7 > 0) {
                    Enchantment var65 = Enchantment.EFFICIENCY;
                    int var51 = var14.getEnchantmentLevel(var65);
                    if (var51 < var7) {
                        Enchantment var67 = Enchantment.EFFICIENCY;
                        var14.addUnsafeEnchantment(var67, var7);
                    }
                }
            }
        }
    }

    private String buildActionBar(int var1, UUID var2, int var3) {
        Map var11 = this.speed.thunderStepCd;
        String var8 = timer((Long)var11.get(var2), 44500350);
        if (var1 < 2) {
            return "&e\ud83d\udd3a &b" + var8;
        } else {
            Map var18 = this.speed.blurCd;
            String var22 = timer((Long)var18.get(var2), 44500350);
            String var29 = this.stormStatus(var2, 1049829621);
            return "&e\ud83c\udfaf &b" + var22 + "  &e\ud83d\udd2e &b" + var8 + " &e\ud83c\udf29 " + var29;
        }
    }

    private String stormStatus(UUID var1, int var2) {
        SpeedGemListener var14 = this.speed;
        int var9 = var14.stormCharges(var1, 85759890);
        String var5 = "speed";
        String var6 = "speed-storm-charges";
        double var40 = GemTuning.power(var5, var6, 5.0, 1503850832);
        int var10 = (int)Math.max(1.0, var40);
        if (var9 >= var10) {
            return "&aReady!";
        } else {
            String var11 = "&b" + var9 + "/" + var10;
            SpeedGemListener var26 = this.speed;
            long var12 = var26.stormNextRechargeMs(var1, 1025301735);
            String var33;
            if (var12 <= 0L) {
                var33 = var11;
            } else {
                String var39 = duration(var12, 773121197);
                var33 = var11 + " " + var39;
            }

            return var33;
        }
    }

    private static String duration(long var0, int var2) {
        int var8 = (int)((var0 + 999L) / 1000L);
        int var9 = var8 / 60;
        int var10 = var8 % 60;
        if (var9 == 0) {
            return var10 + "s";
        } else {
            return var10 == 0 ? var9 + "m" : var9 + "m " + var10 + "s";
        }
    }

    private static String timer(Long var0, int var1) {
        if (var0 == null) {
            return "&aReady!";
        } else {
            long var6 = System.currentTimeMillis();
            long var15 = var0;
            if (var15 <= var6) {
                return "&aReady!";
            } else {
                long var20 = var0;
                int var8 = (int)((var20 - var6 + 999L) / 1000L);
                int var9 = var8 / 60;
                int var10 = var8 % 60;
                if (var9 == 0) {
                    return var10 + "s";
                } else {
                    return var10 == 0 ? var9 + "m" : var9 + "m " + var10 + "s";
                }
            }
        }
    }

    private static boolean bareFeet(Player var0, int var1) {
        ItemStack var3 = var0.getInventory().getBoots();
        boolean var10;
        if (var3 != null && !var3.getType().isAir()) {
            var10 = false;
        } else {
            var10 = true;
        }

        return var10;
    }
}
