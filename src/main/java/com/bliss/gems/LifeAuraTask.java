package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemAutoEnchants;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.entity.MotifSpeechListener;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.AssessmentWindowBarTask;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class LifeAuraTask {
    private static final long PERIOD_TICKS = 5L;
    private static final NamespacedKey BASELINE_KEY = NamespacedKey.fromString("bliss:life_max_baseline");
    private static final long T1_HEAL_INTERVAL_MS = 9000L;
    private static final long T2_HEAL_INTERVAL_MS = 6000L;
    private static final double HEAL_AMOUNT = 1.0;
    private static final int UNBREAKING_LEVEL = 3;
    private static final int UNBREAKING_LEVEL_T1 = 1;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final LifeGemListener life;
    private BukkitTask task;
    private static final int COL_CASTER_HP = 10;
    private static final int COL_ALLY_HP = 4;
    private static final int DRAIN_SOLO_HP = -4;
    private static final int DRAIN_GROUP_HP = -8;

    public LifeAuraTask(BlissPlugin var1, PlayerDataStore var2, LifeGemListener var3, int var4) {
        this.plugin = var1;
        this.players = var2;
        this.life = var3;
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

        for (Player var5 : Bukkit.getOnlinePlayers()) {
            LifeGemListener var17 = this.life;
            restoreBaseline(var17, var5, 464898981);
        }
    }

    public static void restoreBaseline(LifeGemListener var0, Player var1, int var2) {
        Map var7 = var0.baselineMaxHealth;
        UUID var15 = var1.getUniqueId();
        Double var5 = (Double)var7.remove(var15);
        if (var5 != null) {
            Attribute var16 = Attribute.MAX_HEALTH;
            AttributeInstance var6 = var1.getAttribute(var16);
            if (var6 != null) {
                double var18 = var5;
                var6.setBaseValue(var18);
            }
        }
    }

    private void tick() {
        long var10 = System.currentTimeMillis();

        for (Player var13 : Bukkit.getOnlinePlayers()) {
            if (!var13.isDead()) {
                UUID var14 = var13.getUniqueId();
                this.applyHeartLimits(var13, var10, 1721998215);
                ItemStack var15 = var13.getInventory().getItemInMainHand();
                ItemStack var16 = var13.getInventory().getItemInOffHand();
                int var44 = BlissCoreItems.gemIdOf(var15, 382441079);
                int var140 = GemType.LIFE.id();
                boolean var45;
                if (var44 == var140) {
                    var45 = true;
                } else {
                    var45 = false;
                }

                int var47 = BlissCoreItems.gemIdOf(var16, 382441079);
                int var142 = GemType.LIFE.id();
                boolean var48;
                if (var47 == var142) {
                    var48 = true;
                } else {
                    var48 = false;
                }

                GemType var143 = GemType.LIFE;
                boolean var19 = GoldGem.hasAbsorbedSoul(var13, var143, 1603837876);
                if (var19) {
                    PlayerDataStore var53 = this.players;
                    PlayerData var20 = var53.get(var14, 1264010596);
                    if (var20.canUsePassives(2051432579)) {
                        String var57 = "life";
                        String var145 = "wither-immunity";
                        if (GemTuning.passive(var57, var145, true, 14309468) && var20.energy(1160853318) > 4) {
                            PotionEffectType var147 = PotionEffectType.WITHER;
                            if (var13.hasPotionEffect(var147)) {
                                PotionEffectType var148 = PotionEffectType.WITHER;
                                var13.removePotionEffect(var148);
                            }
                        }

                        label126: {
                            Map var66 = this.life.nextHealMs;
                            Long var21 = (Long)var66.get(var14);
                            if (var21 != null) {
                                long var71 = var21;
                                if (var71 > var10) {
                                    break label126;
                                }
                            }

                            String var150 = "life";
                            String var190 = "passive-heal";
                            double var151 = GemTuning.health(var150, var190, 1.0, 77875367);
                            double var210 = EnergyScale.power(var20, 1425120665);
                            double var153 = var151 * var210;
                            heal(var13, var153, 799776769);
                            Map var77 = this.life.nextHealMs;
                            Long var195 = var10 + 6000L;
                            var77.put(var14, var195);
                        }

                        applyUnbreaking(var13, 2, 1018650147);
                    } else {
                        String var135 = "life";
                        String var185 = "golden-apple-absorption";
                        if (GemTuning.passive(var135, var185, true, 14309468)) {
                            PotionEffectType var186 = PotionEffectType.ABSORPTION;
                            GrantedEffect.strip(var13, var186, 553187889);
                        }
                    }
                }

                GemType var157 = GemType.LIFE;
                if (GoldGem.isWieldingSoul(var13, var157, 100010596)) {
                    if (!AssessmentWindowBarTask.isShowing(1134156722) && !MotifSpeechListener.isSpeakingTo(var13.getUniqueId(), 1524431478)) {
                        Component var160 = Text.legacy(this.buildActionBar(2, var14, 47900889), 373192400);
                        var13.sendActionBar(var160);
                    }
                } else if (!var19 && (var45 || var48)) {
                    PlayerDataStore var91 = this.players;
                    PlayerData var221 = var91.get(var14, 1264010596);
                    if (!var221.canUsePassives(2051432579)) {
                        String var132 = "life";
                        String var183 = "golden-apple-absorption";
                        if (GemTuning.passive(var132, var183, true, 14309468)) {
                            PotionEffectType var184 = PotionEffectType.ABSORPTION;
                            GrantedEffect.strip(var13, var184, 553187889);
                        }
                    } else {
                        ItemStack var138;
                        if (var45) {
                            var138 = var15;
                        } else {
                            var138 = var16;
                        }

                        int var222 = BlissCoreItems.tierOf(var138, 1633828214);
                        PlayerDataStore var98 = this.players;
                        UUID var163 = var13.getUniqueId();
                        if (var98.get(var163, 1264010596).energy(1160853318) >= 2) {
                            PlayerDataStore var102 = this.players;
                            UUID var166 = var13.getUniqueId();
                            if (!var102.get(var166, 1264010596).hudSuppressed(859110487)) {
                                Component var169 = Text.legacy(this.buildActionBar(var222, var14, 47900889), 373192400);
                                var13.sendActionBar(var169);
                            }
                        }

                        String var106 = "life";
                        String var170 = "wither-immunity";
                        if (GemTuning.passive(var106, var170, true, 14309468) && var221.energy(1160853318) > 4) {
                            PotionEffectType var172 = PotionEffectType.WITHER;
                            if (var13.hasPotionEffect(var172)) {
                                PotionEffectType var173 = PotionEffectType.WITHER;
                                var13.removePotionEffect(var173);
                            }
                        }

                        label139: {
                            Map var115 = this.life.nextHealMs;
                            Long var22 = (Long)var115.get(var14);
                            if (var22 != null) {
                                long var120 = var22;
                                if (var120 > var10) {
                                    break label139;
                                }
                            }

                            String var175 = "life";
                            String var201 = "passive-heal";
                            double var176 = GemTuning.health(var175, var201, 1.0, 77875367);
                            double var217 = EnergyScale.power(var221, 1425120665);
                            double var178 = var176 * var217;
                            heal(var13, var178, 799776769);
                            long var130;
                            if (var222 == 2) {
                                var130 = 6000L;
                            } else {
                                var130 = 9000L;
                            }

                            Map var127 = this.life.nextHealMs;
                            Long var206 = var10 + var130;
                            var127.put(var14, var206);
                        }

                        applyUnbreaking(var13, var222, 1018650147);
                    }
                }
            }
        }
    }

    private static void applyUnbreaking(Player var0, int var1, int var2) {
        String var3 = "life";
        String var4 = "unbreaking-level";
        int var7 = (int)GemTuning.power(var3, var4, 3.0, 1503850832);
        if (var1 < 2) {
            var7 = Math.min(var7, 1);
        }

        UUID var20 = var0.getUniqueId();
        Enchantment var41 = Enchantment.UNBREAKING;
        int var8 = GemAutoEnchants.allow(var20, var41, var7, 1997807392);
        if (var8 > 0) {
            for (ItemStack var12 : var0.getInventory().getContents()) {
                if (var12 != null && canCarryUnbreaking(var12.getType(), 1292684205)) {
                    Enchantment var44 = Enchantment.UNBREAKING;
                    int var37 = var12.getEnchantmentLevel(var44);
                    if (var37 < var8) {
                        Enchantment var46 = Enchantment.UNBREAKING;
                        var12.addUnsafeEnchantment(var46, var8);
                    }
                }
            }
        }
    }

    private static boolean canCarryUnbreaking(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD, WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE, WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE, WOODEN_SHOVEL, STONE_SHOVEL, IRON_SHOVEL, GOLDEN_SHOVEL, DIAMOND_SHOVEL, NETHERITE_SHOVEL, WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE, DIAMOND_HOE, NETHERITE_HOE-> true;
            case LEATHER_HELMET, IRON_HELMET, GOLDEN_HELMET, DIAMOND_HELMET, NETHERITE_HELMET, CHAINMAIL_HELMET, TURTLE_HELMET, LEATHER_CHESTPLATE, IRON_CHESTPLATE, GOLDEN_CHESTPLATE, DIAMOND_CHESTPLATE, NETHERITE_CHESTPLATE, CHAINMAIL_CHESTPLATE, ELYTRA, LEATHER_LEGGINGS, IRON_LEGGINGS, GOLDEN_LEGGINGS, DIAMOND_LEGGINGS, NETHERITE_LEGGINGS, CHAINMAIL_LEGGINGS, LEATHER_BOOTS, IRON_BOOTS, GOLDEN_BOOTS, DIAMOND_BOOTS, NETHERITE_BOOTS, CHAINMAIL_BOOTS-> true;
            case BOW, CROSSBOW, TRIDENT, SHIELD, FISHING_ROD, MACE, FLINT_AND_STEEL, SHEARS, CARROT_ON_A_STICK, WARPED_FUNGUS_ON_A_STICK, BRUSH-> true;
            default -> false;
        };
    }

    private void applyHeartLimits(Player var1, long var2, int var4) {
        UUID var12;
        boolean var13;
        boolean var14;
        boolean var15;
        boolean var16;
        LifeGemListener.HeartLockState var17;
        boolean var134;
        label79: {
            var12 = var1.getUniqueId();
            Map var27 = this.life.drainedGroupExpiry;
            var13 = isFlag(var27, var12, var2, 1879303369);
            Map var31 = this.life.drainedSoloExpiry;
            var14 = isFlag(var31, var12, var2, 1879303369);
            Map var35 = this.life.colGroupExpiry;
            var15 = isFlag(var35, var12, var2, 1879303369);
            Map var39 = this.life.colCasterExpiry;
            var16 = isFlag(var39, var12, var2, 1879303369);
            Map var43 = this.life.heartLocked;
            var17 = (LifeGemListener.HeartLockState)var43.get(var12);
            if (var17 != null) {
                long var48 = var17.expiry;
                if (var48 > var2) {
                    var134 = true;
                    break label79;
                }
            }

            var134 = false;
        }

        Attribute var139 = Attribute.MAX_HEALTH;
        AttributeInstance var19 = var1.getAttribute(var139);
        if (var19 != null) {
            if (!var13 && !var14 && !var15 && !var16 && !var134) {
                Map var61 = this.life.baselineMaxHealth;
                Double var20 = (Double)var61.remove(var12);
                if (var20 == null) {
                    PersistentDataContainer var66 = var1.getPersistentDataContainer();
                    NamespacedKey var141 = BASELINE_KEY;
                    PersistentDataType var165 = PersistentDataType.DOUBLE;
                    if (var66.has(var141, var165)) {
                        PersistentDataContainer var69 = var1.getPersistentDataContainer();
                        NamespacedKey var142 = BASELINE_KEY;
                        PersistentDataType var166 = PersistentDataType.DOUBLE;
                        var20 = (Double)var69.get(var142, var166);
                    }
                }

                if (var20 != null) {
                    double var144 = var20;
                    var19.setBaseValue(var144);
                    PersistentDataContainer var75 = var1.getPersistentDataContainer();
                    NamespacedKey var146 = BASELINE_KEY;
                    var75.remove(var146);
                }

                if (var17 != null && !var134) {
                    Map var80 = this.life.heartLocked;
                    var80.remove(var12);
                }
            } else {
                Map var84 = this.life.baselineMaxHealth;
                Function var168 = var1x -> var19.getBaseValue();
                double var200 = (Double)var84.computeIfAbsent(var12, var168);
                PersistentDataContainer var90 = var1.getPersistentDataContainer();
                NamespacedKey var149 = BASELINE_KEY;
                PersistentDataType var169 = PersistentDataType.DOUBLE;
                if (!var90.has(var149, var169)) {
                    PersistentDataContainer var93 = var1.getPersistentDataContainer();
                    NamespacedKey var150 = BASELINE_KEY;
                    PersistentDataType var170 = PersistentDataType.DOUBLE;
                    Double var194 = var200;
                    var93.set(var150, var170, var194);
                }

                double var202;
                if (var134) {
                    double var180 = var19.getValue();
                    double var196 = var19.getBaseValue();
                    double var182 = var180 - var196;
                    double var206 = Math.max(0.0, var182);
                    double var120 = var17.maxHp;
                    var202 = var120 - var206;
                    String var124 = "life";
                    String var156 = "heart-lock-max-taken";
                    double var22 = GemTuning.health(var124, var156, 0.0, 77875367);
                    if (var22 > 0.0) {
                        double var192 = var200 - var22;
                        var202 = Math.max(var202, var192);
                    }
                } else {
                    int var21 = combineDelta(var13, var14, var15, var16, 1921507851);
                    double var173 = (double)var21;
                    var202 = var200 + var173;
                }

                if (var202 < 1.0) {
                    var202 = 1.0;
                }

                var19.setBaseValue(var202);
                double var204 = var19.getValue();
                double var111 = var1.getHealth();
                if (var111 > var204) {
                    var1.setHealth(var204);
                }
            }
        }
    }

    private static int combineDelta(boolean var0, boolean var1, boolean var2, boolean var3, int var4) {
        int var36;
        if (var3) {
            String var32 = "life";
            String var41 = "circle-caster";
            var36 = (int)GemTuning.health(var32, var41, 10.0, 77875367);
        } else if (var2) {
            String var29 = "life";
            String var40 = "circle-ally";
            var36 = (int)GemTuning.health(var29, var40, 4.0, 77875367);
        } else {
            var36 = 0;
        }

        int var35;
        if (var1) {
            String var15 = "life";
            String var6 = "drain-solo";
            var35 = -((int)GemTuning.health(var15, var6, 4.0, 77875367));
        } else {
            var35 = 0;
        }

        byte var10 = (byte)var35;
        int var28;
        if (var0) {
            String var24 = "life";
            String var39 = "drain-group";
            var28 = -((int)GemTuning.health(var24, var39, 8.0, 77875367));
        } else {
            var28 = 0;
        }

        int var12 = Math.min(var10, var28);
        return var36 + var12;
    }

    private static boolean isFlag(Map<UUID, Long> var0, UUID var1, long var2, int var4) {
        Long var9 = (Long)var0.get(var1);
        if (var9 != null) {
            long var14 = var9;
            if (var14 > var2) {
                return true;
            }
        }

        return false;
    }

    private static void heal(Player var0, double var1, int var3) {
        Attribute var5 = Attribute.MAX_HEALTH;
        AttributeInstance var9 = var0.getAttribute(var5);
        double var16;
        if (var9 == null) {
            var16 = 20.0;
        } else {
            var16 = var9.getValue();
        }

        double var22 = var0.getHealth();
        double var24 = var22 + var1;
        double var20 = Math.min(var16, var24);
        var0.setHealth(var20);
    }

    private String buildActionBar(int var1, UUID var2, int var3) {
        if (var1 == 2) {
            Map var10 = this.life.cd3;
            String var11 = timer(var10, var2, 2128318791);
            Map var21 = this.life.vortexActiveUntil;
            Map var27 = this.life.cd4;
            String var22 = activeOrTimer(var21, var27, var2, 204466623);
            Map var30 = this.life.cd2;
            String var31 = timer(var30, var2, 2128318791);
            return "\ud83d\udc98 &b" + var11 + " &d\ud83d\udd2e &b" + var22 + " &d\ud83d\udc96 &b" + var31;
        } else {
            Map var15 = this.life.vortexActiveUntil;
            Map var25 = this.life.cd4;
            return "&d\ud83d\udd3a &b" + activeOrTimer(var15, var25, var2, 204466623);
        }
    }

    private static String activeOrTimer(Map<UUID, Long> var0, Map<UUID, Long> var1, UUID var2, int var3) {
        Long var8 = (Long)var0.get(var2);
        if (var8 != null) {
            long var13 = var8;
            long var6 = System.currentTimeMillis();
            if (var13 > var6) {
                return "&cActive..";
            }
        }

        return timer(var1, var2, 2128318791);
    }

    private static String timer(Map<UUID, Long> var0, UUID var1, int var2) {
        Long var7 = (Long)var0.get(var1);
        if (var7 == null) {
            return "&aReady!";
        } else {
            long var8 = System.currentTimeMillis();
            long var19 = var7;
            if (var19 <= var8) {
                return "&aReady!";
            } else {
                long var23 = var7;
                int var10 = (int)((var23 - var8 + 999L) / 1000L);
                int var11 = var10 / 60;
                int var12 = var10 % 60;
                if (var11 == 0) {
                    return var12 + "s";
                } else {
                    return var12 == 0 ? var11 + "m" : var11 + "m " + var12 + "s";
                }
            }
        }
    }
}
