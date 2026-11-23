package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TrustStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.GemAbilityRules;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public final class StrengthGemListener implements Listener {
    private static final int FRAILER_TICKS = 600;
    private static final int FRAILER_TRAIL_TICKS = 100;
    private static final int NULLIFY_TICKS = 600;
    private static final long CHAD_DURATION_MS = 30000L;
    private static final DustOptions DUST_RED;
    private static final double[][] NULLIFY_RINGS;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final TrustStore trust;
    public final Map<UUID, Long> cd2;
    public final Map<UUID, Long> cd3;
    public final Map<UUID, Long> cd4;
    public final Map<UUID, Long> bountyActiveUntil;
    public final Map<UUID, Long> chadActiveUntil;
    public final Map<UUID, Long> chadGroupActiveUntil;
    private static final DustOptions CHAD_DOT_BLACK;
    private static final DustOptions CHAD_DOT_RED;
    private final Map<UUID, Integer> testDoubleCounter;
    private final Map<UUID, Integer> testDoubleGroupCounter;
    private final Map<UUID, Long> lastEntityClickAt;
    private final Map<UUID, Long> frailedUntil;
    private final Map<UUID, BossBar> frailedBar;
    private final Map<UUID, Long> nullifiedUntil;
    private final Map<UUID, BossBar> nullifyBar;
    private final Map<UUID, List<PotionEffect>> nullifyStash;

    private static int frailerTicks(int var0) {
        String var1 = "strength";
        String var2 = "frailer";
        return (int)Math.round(GemTuning.duration(var1, var2, 30.0, 812730832) * 20.0);
    }

    private static int nullifyTicks(int var0) {
        String var1 = "strength";
        String var2 = "nullify";
        return (int)Math.round(GemTuning.duration(var1, var2, 30.0, 812730832) * 20.0);
    }

    private static long chadDurationMs(int var0) {
        String var1 = "strength";
        String var2 = "chad";
        return Math.round(GemTuning.duration(var1, var2, 30.0, 812730832) * 1000.0);
    }

    public StrengthGemListener(BlissPlugin var1, PlayerDataStore var2, TrustStore var3, int var4) {
        ConcurrentHashMap var6 = new ConcurrentHashMap();
        this.cd2 = var6;
        ConcurrentHashMap var24 = new ConcurrentHashMap();
        this.cd3 = var24;
        ConcurrentHashMap var25 = new ConcurrentHashMap();
        this.cd4 = var25;
        ConcurrentHashMap var26 = new ConcurrentHashMap();
        this.bountyActiveUntil = var26;
        ConcurrentHashMap var27 = new ConcurrentHashMap();
        this.chadActiveUntil = var27;
        ConcurrentHashMap var28 = new ConcurrentHashMap();
        this.chadGroupActiveUntil = var28;
        HashMap var29 = new HashMap();
        this.testDoubleCounter = var29;
        HashMap var30 = new HashMap();
        this.testDoubleGroupCounter = var30;
        HashMap var31 = new HashMap();
        this.lastEntityClickAt = var31;
        ConcurrentHashMap var32 = new ConcurrentHashMap();
        this.frailedUntil = var32;
        HashMap var33 = new HashMap();
        this.frailedBar = var33;
        ConcurrentHashMap var34 = new ConcurrentHashMap();
        this.nullifiedUntil = var34;
        HashMap var35 = new HashMap();
        this.nullifyBar = var35;
        HashMap var36 = new HashMap();
        this.nullifyStash = var36;
        this.plugin = var1;
        this.players = var2;
        this.trust = var3;
    }

    private static long cooldownFrailerNullify(int var0, boolean var1, int var2) {
        long var7 = (long)(switch (var0) {
            case 2 -> 485;
            case 3 -> 385;
            case 4 -> 335;
            case 5 -> 289;
            default -> 240;
        }) * 1000L;
        long var17;
        if (var1) {
            var17 = var7 / 2L;
        } else {
            var17 = var7;
        }

        return var17;
    }

    private static long cooldownChad(int var0, boolean var1, int var2) {
        long var7 = (long)(switch (var0) {
            case 2 -> 300;
            case 3 -> 240;
            case 4 -> 210;
            case 5 -> 180;
            default -> 150;
        }) * 1000L;
        long var17;
        if (var1) {
            var17 = var7 / 2L;
        } else {
            var17 = var7;
        }

        return var17;
    }

    private boolean isStrengthHeldMain(Player var1, int var2, int var3) {
        ItemStack var6 = var1.getInventory().getItemInMainHand();
        int var10 = this.tierOfStrength(var6, 1214135839);
        if (var10 == var2) {
            return true;
        } else {
            if (var2 == 2) {
                GemType var18 = GemType.STRENGTH;
                if (GoldGem.isWieldingSoulMain(var1, var18, 244096655)) {
                    return true;
                }
            }

            return false;
        }
    }

    private int tierOfStrengthOffhand(Player var1, int var2) {
        ItemStack var15 = var1.getInventory().getItemInOffHand();
        int var5 = this.tierOfStrength(var15, 1214135839);
        if (var5 != 0) {
            return var5;
        } else {
            GemType var16 = GemType.STRENGTH;
            if (!GoldGem.isWieldingSoul(var1, var16, 100010596)) {
                GemType var17 = GemType.STRENGTH;
                if (!GoldGem.hasAbsorbedSoul(var1, var17, 1603837876)) {
                    return 0;
                }
            }

            return 2;
        }
    }

    private int tierOfStrength(ItemStack var1, int var2) {
        if (!BlissCoreItems.isStrengthGem(var1, 1818434225)) {
            return 0;
        } else {
            PersistentDataContainer var10 = var1.getItemMeta().getPersistentDataContainer();
            NamespacedKey var4 = BlissCoreItems.KEY_TIER;
            PersistentDataType var5 = PersistentDataType.INTEGER;
            Integer var6 = (Integer)var10.get(var4, var5);
            int var15;
            if (var6 == null) {
                var15 = 0;
            } else {
                var15 = var6;
            }

            return var15;
        }
    }

    private boolean energyOk(Player var1, int var2) {
        PlayerDataStore var5 = this.players;
        UUID var8 = var1.getUniqueId();
        return var5.get(var8, 1264010596).canUsePowers(1616892701);
    }

    private static String formatRemaining(Map<UUID, Long> var0, UUID var1, int var2) {
        Long var7 = (Long)var0.get(var1);
        if (var7 == null) {
            return "Ready!";
        } else {
            long var8 = System.currentTimeMillis();
            long var20 = var7;
            if (var20 <= var8) {
                return "Ready!";
            } else {
                long var24 = var7;
                int var10 = (int)((var24 - var8 + 999L) / 1000L);
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

    @EventHandler(
        ignoreCancelled = true
    )
    public void onDamage(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var12) {
            this.applyHalfStrengthProg1(var12, var1, 681326899);
            if (!(var1.getEntity() instanceof Player var142)) {
                this.applyChadOnCrit(var12, var1, 737642797);
                this.applyLowHpBonus(var12, var1, 853131993);
            } else {
                boolean var80;
                label48: {
                    DamageCause var36 = var1.getCause();
                    DamageCause var84 = DamageCause.SONIC_BOOM;
                    if (var36 != var84) {
                        DamageCause var38 = var1.getCause();
                        DamageCause var85 = DamageCause.THORNS;
                        if (var38 != var85) {
                            var80 = false;
                            break label48;
                        }
                    }

                    var80 = true;
                }

                label43:
                if (!var80
                    && this.isStrengthHeldMain(var12, 2, 1882702281)
                    && this.energyOk(var12, 2007020201)
                    && GemAbilityRules.on(GemAbilityRules.GemAbility.STRENGTH_FRAILER, 425337338)) {
                    UUID var15 = var12.getUniqueId();
                    Map var49 = this.cd2;
                    Long var16 = (Long)var49.get(var15);
                    long var17 = System.currentTimeMillis();
                    if (var16 != null) {
                        long var56 = var16;
                        if (var56 > var17) {
                            Map var90 = this.cd2;
                            Component var93 = Text.legacy(
                                "&4\ud83d\udd2e &cYour §4\ud83e\udd3aFrailer &cskill is on cooldown for &4" + formatRemaining(var90, var15, 774275872),
                                373192400
                            );
                            var12.sendMessage(var93);
                            break label43;
                        }
                    }

                    TrustStore var64 = this.trust;
                    UUID var122 = var142.getUniqueId();
                    if (var64.isTrusted(var15, var122, 1736364606)) {
                        Component var99 = Text.legacy("&4\ud83d\udd2e &fYou cannot cast negative powers on allies!", 373192400);
                        var12.sendMessage(var99);
                    } else {
                        Component var103 = Text.legacy("&4\ud83d\udd2e &bYou used &4\ud83e\udd3aFrailer &bskill on &4" + var142.getName(), 373192400);
                        var12.sendMessage(var103);
                        Component var107 = Text.legacy(
                            "&4\ud83d\udd2e &bYou have been affected with &4\ud83e\udd3aFrailer &bby &e" + var12.getName(), 373192400
                        );
                        var142.sendMessage(var107);
                        PotionEffectType var5 = PotionEffectType.WEAKNESS;
                        int var6 = frailerTicks(551366239);
                        PotionEffect var108 = new PotionEffect(var5, var6, 0, false, true, true);
                        var142.addPotionEffect(var108);
                        PlayerDataStore var72 = this.players;
                        int var19 = var72.get(var15, 1264010596).energy(1160853318);
                        Map var76 = this.cd2;
                        String var129 = "strength";
                        String var134 = "frailer";
                        boolean var141 = GlobalState.isHoldingDragonEgg(var12, 2044938455);
                        long var139 = cooldownFrailerNullify(var19, var141, 1728921157);
                        long var130 = GemTuning.cd(var129, var134, 240.0, var139, 1621983041);
                        long var132 = EnergyScale.cd(var130, var19, 907518618);
                        Long var127 = var17 + var132;
                        var76.put(var15, var127);
                        this.applyFrailedFlag(var142, 2115747047);
                        this.spawnFrailerTrail(var12, var142, 1835528291);
                    }
                }

                this.applyFrailedMultiplier(var1, 1152932446);
                this.applyChadOnCrit(var12, var1, 737642797);
                this.applyLowHpBonus(var12, var1, 853131993);
            }
        } else {
            this.applyFrailedMultiplier(var1, 1152932446);
        }
    }

    private void applyHalfStrengthProg1(Player var1, EntityDamageByEntityEvent var2, int var3) {
        if (!ConsistentDamage.deliveringAbilityTo(var2.getEntity(), 450947555)) {
            DamageCause var9 = var2.getCause();
            DamageCause var5 = DamageCause.ENTITY_ATTACK;
            if (var9 != var5) {
                DamageCause var49 = DamageCause.ENTITY_SWEEP_ATTACK;
                if (var9 != var49) {
                    return;
                }
            }

            GlobalState var10 = GlobalState.forPlayer(var1, 1433087673);
            if (var10 != null && !var10.strengthProg1FullPassive(2079279880)) {
                if (var10.prog1(494064183) && !var10.prog2(1337683067)) {
                    PlayerDataStore var29 = this.players;
                    UUID var51 = var1.getUniqueId();
                    PlayerData var11 = var29.get(var51, 1264010596);
                    if (var11 != null && var11.canUsePassives(2051432579)) {
                        String var34 = "strength";
                        String var52 = "strength";
                        if (GemTuning.passive(var34, var52, true, 14309468)) {
                            boolean var48;
                            label45: {
                                if (!BlissCoreItems.isStrengthGem(var1.getInventory().getItemInMainHand(), 1818434225)
                                    && !BlissCoreItems.isStrengthGem(var1.getInventory().getItemInOffHand(), 1818434225)) {
                                    GemType var53 = GemType.STRENGTH;
                                    if (!GoldGem.hasAbsorbedSoul(var1, var53, 1603837876)) {
                                        var48 = false;
                                        break label45;
                                    }
                                }

                                var48 = true;
                            }

                            if (var48) {
                                double var57 = var2.getDamage() + 1.5;
                                var2.setDamage(var57);
                            }
                        }
                    }
                }
            }
        }
    }

    private void applyFrailedMultiplier(EntityDamageByEntityEvent var1, int var2) {
        if (var1.getEntity() instanceof Player var12) {
            if (GemAbilityRules.on(GemAbilityRules.GemAbility.STRENGTH_FRAILER, 425337338)) {
                Map var24 = this.frailedUntil;
                UUID var38 = var12.getUniqueId();
                Long var55 = (Long)var24.get(var38);
                if (var55 != null) {
                    long var29 = var55;
                    long var5 = System.currentTimeMillis();
                    if (var29 > var5) {
                        String var32 = "strength";
                        String var39 = "frailer";
                        double var14 = GemTuning.damage(var32, var39, 1.2, 1172948035) - 1.0;
                        double var41 = var1.getDamage();
                        double var53 = GemAbilityRules.scaleOf(GemAbilityRules.GemAbility.STRENGTH_FRAILER, 120299689);
                        double var51 = var14 * var53;
                        double var49 = 1.0 + var51;
                        double var43 = var41 * var49;
                        var1.setDamage(var43);
                    }
                }
            }
        }
    }

    private void applyChadOnCrit(Player var1, EntityDamageByEntityEvent var2, int var3) {
        if (var2.isCritical()) {
            if (var2.getEntity() instanceof Player var10) {
                TrustStore var26 = this.trust;
                UUID var74 = var1.getUniqueId();
                UUID var94 = var10.getUniqueId();
                if (var26.isTrusted(var74, var94, 1736364606)) {
                    return;
                }
            }

            if (GemAbilityRules.on(GemAbilityRules.GemAbility.STRENGTH_CHAD, 425337338)) {
                UUID var127 = var1.getUniqueId();
                PlayerDataStore var33 = this.players;
                if (!var33.get(var127, 1264010596).isBroken(430675503)) {
                    boolean var12;
                    boolean var45;
                    label41: {
                        long var128 = System.currentTimeMillis();
                        Map var39 = this.chadActiveUntil;
                        var12 = isActive(var39, var127, var128, 1702798966);
                        if (!var12) {
                            Map var43 = this.chadGroupActiveUntil;
                            if (isActive(var43, var127, var128, 1702798966)) {
                                var45 = true;
                                break label41;
                            }
                        }

                        var45 = false;
                    }

                    if (var12 || var45) {
                        String var99 = "strength";
                        String var7 = "chad-strength";
                        double var102 = GemTuning.damage(var99, var7, 1.8, 1172948035) - 1.0;
                        double var125 = GemAbilityRules.scaleOf(GemAbilityRules.GemAbility.STRENGTH_CHAD, 120299689);
                        double var104 = var102 * var125;
                        double var14 = 1.0 + var104;
                        Map<UUID, Integer> var53 = this.testDoubleCounter;
                        Integer var107 = 0;
                        int var16 = var53.getOrDefault(var127, var107);
                        if (var16 == 3) {
                            double var81 = var2.getDamage();
                            double var83 = var81 * var14;
                            var2.setDamage(var83);
                            var16 = 0;
                        }

                        Map var61 = this.testDoubleCounter;
                        Integer var110 = var16 + 1;
                        var61.put(var127, var110);
                        Map<UUID, Integer> var64 = this.testDoubleGroupCounter;
                        Integer var112 = 0;
                        int var17 = var64.getOrDefault(var127, var112);
                        if (var17 == 8) {
                            double var89 = var2.getDamage();
                            double var91 = var89 * var14;
                            var2.setDamage(var91);
                            var17 = 0;
                        }

                        Map var72 = this.testDoubleGroupCounter;
                        Integer var115 = var17 + 1;
                        var72.put(var127, var115);
                    }
                }
            }
        }
    }

    private static boolean isActive(Map<UUID, Long> var0, UUID var1, long var2, int var4) {
        Long var9 = (Long)var0.get(var1);
        if (var9 != null) {
            long var14 = var9;
            if (var14 > var2) {
                return true;
            }
        }

        return false;
    }

    private void applyLowHpBonus(Player var1, EntityDamageByEntityEvent var2, int var3) {
        if (!ConsistentDamage.deliveringAbilityTo(var2.getEntity(), 450947555)) {
            DamageCause var19 = var2.getCause();
            DamageCause var5 = DamageCause.SONIC_BOOM;
            if (var19 != var5) {
                DamageCause var21 = var2.getCause();
                DamageCause var119 = DamageCause.THORNS;
                if (var21 != var119) {
                    if (var2.getEntity() instanceof Player var10) {
                        TrustStore var29 = this.trust;
                        UUID var121 = var1.getUniqueId();
                        UUID var136 = var10.getUniqueId();
                        if (var29.isTrusted(var121, var136, 1736364606)) {
                            return;
                        }
                    }

                    int var172 = this.tierOfStrengthOffhand(var1, 302279681);
                    if (var172 == 0) {
                        return;
                    }

                    PlayerDataStore var35 = this.players;
                    UUID var124 = var1.getUniqueId();
                    if (!var35.get(var124, 1264010596).canUsePassives(2051432579)) {
                        return;
                    }

                    PlayerDataStore var39 = this.players;
                    UUID var126 = var1.getUniqueId();
                    int var173 = var39.get(var126, 1264010596).energy(1160853318);
                    double var12 = var1.getHealth();
                    double var14 = 0.0;
                    if (var172 == 2 && var173 > 4) {
                        if (var12 < 4.0) {
                            var14 = 3.0;
                        } else if (var12 < 5.5) {
                            var14 = 2.5;
                        } else if (var12 < 7.0) {
                            var14 = 2.0;
                        } else if (var12 < 8.0) {
                            var14 = 1.5;
                        } else if (var12 < 8.5) {
                            var14 = 1.0;
                        } else if (var12 < 9.0) {
                            var14 = 0.5;
                        }
                    } else if (var172 == 1 && var173 > 5) {
                        if (var12 < 3.0) {
                            var14 = 3.0;
                        } else if (var12 < 4.0) {
                            var14 = 2.5;
                        } else if (var12 < 5.0) {
                            var14 = 2.0;
                        } else if (var12 < 6.0) {
                            var14 = 1.5;
                        } else if (var12 < 7.0) {
                            var14 = 1.0;
                        } else if (var12 < 8.0) {
                            var14 = 0.5;
                        }
                    }

                    String var141 = "strength";
                    String var7 = "low-hp-bonus";
                    double var144 = GemTuning.damage(var141, var7, 3.0, 1172948035) / 3.0;
                    double var174 = var14 * var144;
                    if (var174 > 0.0) {
                        double var130 = var2.getDamage();
                        double var132 = var130 + var174;
                        var2.setDamage(var132);
                    }

                    return;
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent var1) {
        EquipmentSlot var10 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var10 == var3) {
            Player var7 = var1.getPlayer();
            if (this.isStrengthHeldMain(var7, 2, 1882702281)) {
                if (this.energyOk(var7, 2007020201)) {
                    Action var8 = var1.getAction();
                    Action var29 = Action.LEFT_CLICK_AIR;
                    if (var8 != var29) {
                        Action var30 = Action.LEFT_CLICK_BLOCK;
                        if (var8 != var30) {
                            Action var33 = Action.RIGHT_CLICK_AIR;
                            if (var8 != var33) {
                                Action var34 = Action.RIGHT_CLICK_BLOCK;
                                if (var8 != var34) {
                                    return;
                                }
                            }

                            this.tryChadStrength(var7, 1467254435);
                            return;
                        }
                    }

                    int var9 = Bukkit.getCurrentTick();
                    BukkitScheduler var22 = Bukkit.getScheduler();
                    BlissPlugin var32 = this.plugin;
                    Runnable var37 = () -> {
                        UUID var6 = var7.getUniqueId();
                        if (!EntityClickGuard.meleeSince(var6, var9, 1033776613)) {
                            if (var7.isOnline() && this.isStrengthHeldMain(var7, 2, 1882702281) && this.energyOk(var7, 2007020201)) {
                                this.tryNullify(var7, 32803011);
                            }
                        }
                    };
                    var22.runTaskLater(var32, var37, 1L);
                }
            }
        }
    }

    private void tryNullify(Player var1, int var2) {
        if (GemAbilityRules.on(GemAbilityRules.GemAbility.STRENGTH_NULLIFY, 425337338)) {
            UUID var14 = var1.getUniqueId();
            Map var24 = this.cd2;
            Long var15 = (Long)var24.get(var14);
            long var16 = System.currentTimeMillis();
            if (var15 != null) {
                long var31 = var15;
                if (var31 > var16) {
                    return;
                }
            }

            PlayerDataStore var35 = this.players;
            int var18 = var35.get(var14, 1264010596).energy(1160853318);
            Map var39 = this.cd2;
            String var8 = "strength";
            String var9 = "nullify";
            boolean var79 = GlobalState.isHoldingDragonEgg(var1, 2044938455);
            long var77 = cooldownFrailerNullify(var18, var79, 1728921157);
            long var71 = GemTuning.cd(var8, var9, 240.0, var77, 1621983041);
            long var73 = EnergyScale.cd(var71, var18, 907518618);
            Long var59 = var16 + var73;
            var39.put(var14, var59);
            Component var51 = Text.legacy("<##F10303>\ud83d\udd2e <##B8FFFB>You have used &f\ud83e\udd3a<##F10303>Nullify &7(radius 5)", 373192400);
            var1.sendMessage(var51);
            Location var19 = var1.getLocation();
            BlissPlugin var45 = this.plugin;
            Color var61 = Color.fromRGB(241, 3, 3);
            GemBurst.burst(var45, var19, var61, 2.5, 1155716754);
            BukkitScheduler var46 = Bukkit.getScheduler();
            BlissPlugin var54 = this.plugin;
            Runnable var63 = () -> {
                long var9x = System.currentTimeMillis();
                int var11 = nullifyTicks(1069448677);
                long var64 = (long)var11 * 50L;
                long var12 = var9x + var64;

                for (Player var15x : var1.getWorld().getPlayers()) {
                    if (!var15x.equals(var1)) {
                        Location var33 = var15x.getLocation();
                        Location var49 = var1.getLocation();
                        if (!(var33.distanceSquared(var49) > 25.0)) {
                            TrustStore var38 = this.trust;
                            UUID var51x = var1.getUniqueId();
                            UUID var69x = var15x.getUniqueId();
                            if (!var38.isTrusted(var51x, var69x, 1736364606)) {
                                Component var55 = Text.legacy(
                                    "&4\ud83d\udd2e &bYou have been affected with &f\ud83e\udd3a<##F10303>Nullify &bby " + var1.getName(), 373192400
                                );
                                var15x.sendMessage(var55);
                                this.stashAndStripPotions(var15x, 1211162830);
                                Map var43 = this.nullifiedUntil;
                                UUID var58 = var15x.getUniqueId();
                                Long var72 = var12;
                                var43.put(var58, var72);
                                this.attachNullifyBar(var15x, 493809626);
                                BukkitScheduler var46x = Bukkit.getScheduler();
                                BlissPlugin var61x = this.plugin;
                                Runnable var74 = () -> {
                                    Map var9xx = this.nullifiedUntil;
                                    UUID var18x = var15x.getUniqueId();
                                    Long var8x = (Long)var9xx.get(var18x);
                                    if (var8x != null) {
                                        long var14x = var8x;
                                        long var21 = System.currentTimeMillis() + 50L;
                                        if (var14x > var21) {
                                            return;
                                        }
                                    }

                                    UUID var20 = var15x.getUniqueId();
                                    this.clearNullify(var20, true, 262544361);
                                };
                                long var76 = (long)var11;
                                var46x.runTaskLater(var61x, var74, var76);
                            }
                        }
                    }
                }
            };
            long var69 = (long)NULLIFY_RINGS.length;
            var46.runTaskLater(var54, var63, var69);
        }
    }

    private void tryChadStrength(Player var1, int var2) {
        if (GemAbilityRules.on(GemAbilityRules.GemAbility.STRENGTH_CHAD, 425337338)) {
            UUID var13 = var1.getUniqueId();
            long var14 = System.currentTimeMillis();
            Map var27 = this.lastEntityClickAt;
            Long var16 = (Long)var27.get(var13);
            if (var16 != null) {
                long var86 = var16;
                if (var14 - var86 < 100L) {
                    return;
                }
            }

            Map var37 = this.cd3;
            Long var17 = (Long)var37.get(var13);
            if (var17 != null) {
                long var42 = var17;
                if (var42 > var14) {
                    Map var72 = this.cd3;
                    Component var75 = Text.legacy(
                        "&4\ud83d\udd2e &cYour §4⚔Chad Strength &cskill is on cooldown for &4" + formatRemaining(var72, var13, 774275872), 373192400
                    );
                    var1.sendMessage(var75);
                    return;
                }
            }

            Component var77 = Text.legacy("&4\ud83d\udd2e &bYou have activated &4⚔Chad Strength", 373192400);
            var1.sendMessage(var77);
            PlayerDataStore var48 = this.players;
            int var18 = var48.get(var13, 1264010596).energy(1160853318);
            Map var52 = this.cd3;
            String var7 = "strength";
            String var8 = "chad-strength";
            boolean var124 = GlobalState.isHoldingDragonEgg(var1, 2044938455);
            long var122 = cooldownChad(var18, var124, 965508782);
            long var113 = GemTuning.cd(var7, var8, 150.0, var122, 1621983041);
            long var115 = EnergyScale.cd(var113, var18, 907518618);
            Long var97 = var14 + var115;
            var52.put(var13, var97);
            long var19 = chadDurationMs(87303111);
            Map var57 = this.chadActiveUntil;
            Long var102 = var14 + var19;
            var57.put(var13, var102);
            Map var60 = this.chadGroupActiveUntil;
            var60.remove(var13);
            Map var63 = this.testDoubleCounter;
            Integer var104 = 0;
            var63.put(var13, var104);
            Map var66 = this.testDoubleGroupCounter;
            Integer var106 = 0;
            var66.put(var13, var106);
            BukkitScheduler var68 = Bukkit.getScheduler();
            BlissPlugin var85 = this.plugin;
            Runnable var108 = () -> {
                Map var8x = this.chadActiveUntil;
                Long var6 = (Long)var8x.get(var13);
                if (var6 != null) {
                    long var13x = var6;
                    long var4 = System.currentTimeMillis();
                    if (var13x > var4) {
                        return;
                    }
                }

                Map var17x = this.chadActiveUntil;
                var17x.remove(var13);
                Map var20 = this.testDoubleCounter;
                var20.remove(var13);
                Map var23 = this.chadGroupActiveUntil;
                Long var7x = (Long)var23.get(var13);
                if (var7x != null) {
                    long var28 = var7x;
                    long var38 = System.currentTimeMillis();
                    if (var28 > var38) {
                        return;
                    }
                }

                Map var32 = this.testDoubleGroupCounter;
                var32.remove(var13);
            };
            long var111 = var19 / 50L;
            var68.runTaskLater(var85, var108, var111);
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent var1) {
        EquipmentSlot var4 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var4 == var3) {
            if (!(var1 instanceof PlayerInteractAtEntityEvent)) {
                Player var9 = var1.getPlayer();
                this.tryChadStrengthGroup(var9, 1553890394);
            }
        }
    }

    private void tryChadStrengthGroup(Player var1, int var2) {
        if (this.isStrengthHeldMain(var1, 2, 1882702281)) {
            if (this.energyOk(var1, 2007020201)) {
                if (GemAbilityRules.on(GemAbilityRules.GemAbility.STRENGTH_CHAD, 425337338)) {
                    UUID var13 = var1.getUniqueId();
                    long var14 = System.currentTimeMillis();
                    Map var31 = this.lastEntityClickAt;
                    Long var95 = var14;
                    var31.put(var13, var95);
                    Map var34 = this.cd3;
                    Long var16 = (Long)var34.get(var13);
                    if (var16 != null) {
                        long var39 = var16;
                        if (var39 > var14) {
                            Map var89 = this.cd3;
                            Component var92 = Text.legacy(
                                "&4\ud83d\udd2e &cYour §4⚔Chad Strength &cskill is on cooldown for &4" + formatRemaining(var89, var13, 774275872), 373192400
                            );
                            var1.sendMessage(var92);
                            return;
                        }
                    }

                    Component var76 = Text.legacy("&4\ud83d\udd2e &bYou have used &4⚔Chad Strength &bGroup", 373192400);
                    var1.sendMessage(var76);
                    PlayerDataStore var44 = this.players;
                    int var17 = var44.get(var13, 1264010596).energy(1160853318);
                    Map var48 = this.cd3;
                    String var7 = "strength";
                    String var8 = "chad-strength";
                    boolean var136 = GlobalState.isHoldingDragonEgg(var1, 2044938455);
                    long var134 = cooldownChad(var17, var136, 965508782);
                    long var124 = GemTuning.cd(var7, var8, 150.0, var134, 1621983041);
                    long var126 = EnergyScale.cd(var124, var17, 907518618);
                    Long var102 = var14 + var126;
                    var48.put(var13, var102);
                    long var18 = chadDurationMs(87303111);
                    Map var53 = this.chadGroupActiveUntil;
                    Long var107 = var14 + var18;
                    var53.put(var13, var107);
                    Map var56 = this.chadActiveUntil;
                    var56.remove(var13);
                    Map var59 = this.testDoubleCounter;
                    Integer var109 = 0;
                    var59.put(var13, var109);
                    Map var62 = this.testDoubleGroupCounter;
                    Integer var111 = 0;
                    var62.put(var13, var111);
                    Location var20 = var1.getLocation();
                    this.playChadGroupRings(var20, 1660278680);
                    BukkitScheduler var67 = Bukkit.getScheduler();
                    BlissPlugin var85 = this.plugin;
                    Runnable var113 = () -> {
                        long var11 = System.currentTimeMillis() + 15000L;

                        for (Player var14x : var1.getWorld().getPlayers()) {
                            if (!var14x.equals(var1)) {
                                Location var33 = var14x.getLocation();
                                Location var68 = var1.getLocation();
                                if (!(var33.distanceSquared(var68) > 16.0)) {
                                    TrustStore var38 = this.trust;
                                    UUID var84 = var14x.getUniqueId();
                                    if (var38.isTrusted(var13, var84, 1736364606)) {
                                        UUID var15 = var14x.getUniqueId();
                                        Map var43 = this.chadGroupActiveUntil;
                                        Long var16x = (Long)var43.get(var15);
                                        if (var16x != null) {
                                            long var48x = var16x;
                                            if (var48x > var11) {
                                                continue;
                                            }
                                        }

                                        Map var52 = this.chadGroupActiveUntil;
                                        Long var89x = var11;
                                        var52.put(var15, var89x);
                                        Map var55 = this.chadActiveUntil;
                                        var55.remove(var15);
                                        Map var58 = this.testDoubleGroupCounter;
                                        Integer var91 = 0;
                                        var58.put(var15, var91);
                                        PlayerDataStore var61 = this.players;
                                        if (!var61.get(var15, 1264010596).isBroken(430675503)) {
                                            Component var78 = Text.legacy(
                                                "&4\ud83d\udd2e &bYou have received &4⚔Chad Strength &bfrom &4" + var1.getName() + " &bfor &415 seconds",
                                                373192400
                                            );
                                            var14x.sendMessage(var78);
                                        }

                                        BukkitScheduler var65 = Bukkit.getScheduler();
                                        BlissPlugin var80 = this.plugin;
                                        Runnable var93 = () -> {
                                            Map var7x = this.chadGroupActiveUntil;
                                            Long var6 = (Long)var7x.get(var15);
                                            if (var6 != null) {
                                                long var12 = var6;
                                                long var4 = System.currentTimeMillis();
                                                if (var12 <= var4) {
                                                    Map var16xx = this.chadGroupActiveUntil;
                                                    var16xx.remove(var15);
                                                    Map var19 = this.testDoubleGroupCounter;
                                                    var19.remove(var15);
                                                }
                                            }
                                        };
                                        var65.runTaskLater(var80, var93, 300L);
                                    }
                                }
                            }
                        }
                    };
                    var67.runTaskLater(var85, var113, 8L);
                    BukkitScheduler var69 = Bukkit.getScheduler();
                    BlissPlugin var87 = this.plugin;
                    Runnable var115 = () -> {
                        Map var7x = this.chadGroupActiveUntil;
                        Long var6 = (Long)var7x.get(var13);
                        if (var6 != null) {
                            long var12 = var6;
                            long var4 = System.currentTimeMillis();
                            if (var12 > var4) {
                                return;
                            }
                        }

                        Map var16x = this.chadGroupActiveUntil;
                        var16x.remove(var13);
                        Map var19 = this.testDoubleCounter;
                        var19.remove(var13);
                        Map var22 = this.testDoubleGroupCounter;
                        var22.remove(var13);
                    };
                    long var122 = var18 / 50L;
                    var69.runTaskLater(var87, var115, var122);
                }
            }
        }
    }

    private void playChadGroupRings(Location var1, int var2) {
        double[][] var12 = new double[8][];
        double[] var33 = new double[]{0.75, 1.0};
        var12[0] = var33;
        double[] var35 = new double[]{1.25, 1.5};
        var12[1] = var35;
        double[] var37 = new double[]{1.75, 2.0};
        var12[2] = var37;
        double[] var39 = new double[]{2.25, 2.5};
        var12[3] = var39;
        double[] var41 = new double[]{2.75, 3.0};
        var12[4] = var41;
        double[] var43 = new double[]{3.25, 3.5};
        var12[5] = var43;
        double[] var45 = new double[]{3.75, 4.0};
        var12[6] = var45;
        double[] var47 = new double[]{4.0};
        var12[7] = var47;
        double[][] var9 = var12;
        int var10 = 0;

        while (true) {
            int var20 = var9.length;
            if (var10 >= var20) {
                return;
            }

            double[] var11 = var9[var10];
            BukkitScheduler var17 = Bukkit.getScheduler();
            BlissPlugin var23 = this.plugin;
            Runnable var32 = () -> {
                for (double var7 : var11) {
                    spawnDustCircle(var1, var7, 494740822);
                }
            };
            long var50 = (long)var10;
            var17.runTaskLater(var23, var32, var50);
            var10++;
        }
    }

    private void applyFrailedFlag(Player var1, int var2) {
        UUID var11 = var1.getUniqueId();
        int var12 = frailerTicks(551366239);
        Map var18 = this.frailedUntil;
        long var5 = System.currentTimeMillis();
        long var53 = (long)var12 * 50L;
        Long var41 = var5 + var53;
        var18.put(var11, var41);
        String var20 = "§fFrailer";
        BarColor var32 = BarColor.RED;
        BarStyle var42 = BarStyle.SOLID;
        BarFlag[] var46 = new BarFlag[0];
        BossBar var13 = Bukkit.createBossBar(var20, var32, var42, var46);
        var13.setProgress(1.0);
        var13.addPlayer(var1);
        Map<UUID, BossBar> var25 = this.frailedBar;
        BossBar var14 = var25.put(var11, var13);
        if (var14 != null) {
            var14.removeAll();
        }

        BukkitScheduler var30 = Bukkit.getScheduler();
        BlissPlugin var38 = this.plugin;
        Runnable var45 = () -> {
            Map var10 = this.frailedUntil;
            Long var8 = (Long)var10.get(var11);
            if (var8 != null) {
                long var15 = var8;
                long var29 = System.currentTimeMillis() + 50L;
                if (var15 > var29) {
                    return;
                }
            }

            Map var19 = this.frailedUntil;
            var19.remove(var11);
            Map var22 = this.frailedBar;
            BossBar var9 = (BossBar)var22.remove(var11);
            if (var9 != null) {
                var9.removeAll();
            }
        };
        long var49 = (long)var12;
        var30.runTaskLater(var38, var45, var49);
    }

    private void stashAndStripPotions(Player var1, int var2) {
        Collection<PotionEffect> var37 = var1.getActivePotionEffects();
        ArrayList<PotionEffect> var3 = new ArrayList<>(var37);
        Predicate<PotionEffect> var29 = var0 -> {
            PotionEffectType var3x = var0.getType();
            PotionEffectType var2x = PotionEffectType.RESISTANCE;
            return var3x.equals(var2x);
        };
        var3.removeIf(var29);
        Map var12 = this.nullifyStash;
        UUID var31 = var1.getUniqueId();
        List var6 = (List)var12.get(var31);
        if (var6 != null) {
            var6.addAll(var3);
        } else {
            Map var17 = this.nullifyStash;
            UUID var33 = var1.getUniqueId();
            var17.put(var33, var3);
        }

        for (PotionEffect var8 : var3) {
            PotionEffectType var35 = var8.getType();
            var1.removePotionEffect(var35);
        }
    }

    private void attachNullifyBar(Player var1, int var2) {
        String var3 = "§fNullify";
        BarColor var4 = BarColor.RED;
        BarStyle var5 = BarStyle.SOLID;
        BarFlag[] var24 = new BarFlag[0];
        BossBar var7 = Bukkit.createBossBar(var3, var4, var5, var24);
        var7.setProgress(1.0);
        var7.addPlayer(var1);
        Map<UUID, BossBar> var13 = this.nullifyBar;
        UUID var22 = var1.getUniqueId();
        BossBar var8 = var13.put(var22, var7);
        if (var8 != null) {
            var8.removeAll();
        }
    }

    private void clearNullify(UUID var1, boolean var2, int var3) {
        Map var11 = this.nullifiedUntil;
        var11.remove(var1);
        Map var14 = this.nullifyBar;
        BossBar var6 = (BossBar)var14.remove(var1);
        if (var6 != null) {
            var6.removeAll();
        }

        if (var2) {
            Player var7 = Bukkit.getPlayer(var1);
            if (var7 != null) {
                Map var27 = this.nullifyStash;
                List<PotionEffect> var8 = (List<PotionEffect>)var27.remove(var1);
                if (var8 != null) {
                    for (PotionEffect var10 : var8) {
                        var7.addPotionEffect(var10);
                    }
                }
            }
        } else {
            Map var21 = this.nullifyStash;
            var21.remove(var1);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        Player var6 = var1.getPlayer();
        UUID var7 = var6.getUniqueId();
        Map var16 = this.nullifiedUntil;
        Long var8 = (Long)var16.get(var7);
        if (var8 != null) {
            long var21 = var8;
            long var4 = System.currentTimeMillis();
            if (var21 > var4) {
                return;
            }
        }

        Map var25 = this.nullifyStash;
        List<PotionEffect> var9 = (List<PotionEffect>)var25.remove(var7);
        if (var9 != null) {
            for (PotionEffect var11 : var9) {
                var6.addPotionEffect(var11);
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent var1) {
        Map var8 = this.nullifiedUntil;
        UUID var24 = var1.getPlayer().getUniqueId();
        Long var6 = (Long)var8.get(var24);
        if (var6 != null) {
            long var13 = var6;
            long var4 = System.currentTimeMillis();
            if (var13 > var4) {
                Material var7 = var1.getItem().getType();
                Material var25 = Material.POTION;
                if (var7 != var25) {
                    Material var26 = Material.SPLASH_POTION;
                    if (var7 != var26) {
                        Material var27 = Material.LINGERING_POTION;
                        if (var7 != var27) {
                            return;
                        }
                    }
                }

                var1.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPotionRightClick(PlayerInteractEvent var1) {
        Action var6 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var6 != var3) {
            Action var37 = Action.RIGHT_CLICK_BLOCK;
            if (var6 != var37) {
                return;
            }
        }

        Map var15 = this.nullifiedUntil;
        UUID var40 = var1.getPlayer().getUniqueId();
        Long var7 = (Long)var15.get(var40);
        if (var7 != null) {
            long var20 = var7;
            long var4 = System.currentTimeMillis();
            if (var20 > var4) {
                PlayerInventory var8 = var1.getPlayer().getInventory();
                Material var9 = var8.getItemInMainHand().getType();
                Material var10 = var8.getItemInOffHand().getType();
                Material var41 = Material.SPLASH_POTION;
                if (var9 != var41) {
                    Material var42 = Material.LINGERING_POTION;
                    if (var9 != var42) {
                        Material var43 = Material.SPLASH_POTION;
                        if (var10 != var43) {
                            Material var44 = Material.LINGERING_POTION;
                            if (var10 != var44) {
                                return;
                            }
                        }
                    }
                }

                var1.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent var1) {
        UUID var5 = var1.getEntity().getUniqueId();
        Map var10 = this.frailedUntil;
        var10.remove(var5);
        Map var13 = this.frailedBar;
        BossBar var6 = (BossBar)var13.remove(var5);
        if (var6 != null) {
            var6.removeAll();
        }

        this.clearNullify(var5, false, 262544361);
        Map var20 = this.chadActiveUntil;
        var20.remove(var5);
        Map var23 = this.chadGroupActiveUntil;
        var23.remove(var5);
        Map var26 = this.testDoubleCounter;
        var26.remove(var5);
        Map var29 = this.testDoubleGroupCounter;
        var29.remove(var5);
    }

    private void spawnFrailerTrail(final Player var1, final Player var2, int var3) {
        BukkitRunnable var4 = new BukkitRunnable() {
            int ticksLeft = 100;

            public void run() {
                int var34 = this.ticksLeft - 1;
                this.ticksLeft = var34;
                if (var34 >= 0 && var1.isOnline() && var2.isOnline() && !var2.isDead() && !var1.isDead()) {
                    World var21 = var1.getWorld();
                    World var37 = var2.getWorld();
                    if (var21.equals(var37)) {
                        Location var5 = var2.getLocation().add(0.0, 0.5, 0.0);
                        Location var6 = var1.getLocation().add(0.0, 0.5, 0.0);
                        StrengthGemListener.spawnDustLine(var5, var6, 228412811);
                        return;
                    }
                }

                this.cancel();
            }
        };
        BlissPlugin var10 = this.plugin;
        var4.runTaskTimer(var10, 0L, 1L);
    }

    private static void spawnDustLine(Location var0, Location var1, int var2) {
        World var17 = var0.getWorld();
        if (var17 != null) {
            Vector var28 = var1.toVector();
            Vector var58 = var0.toVector();
            Vector var18 = var28.subtract(var58);
            double var19 = var18.length();
            if (!(var19 <= 0.0)) {
                if (var19 > 16.0) {
                    var19 = 16.0;
                }

                Vector var21 = var18.normalize().multiply(0.25);
                double var22 = 0.0;

                for (Location var24 = var0.clone(); var22 <= var19; var22 += 0.25) {
                    Particle var61 = Particle.DUST;
                    DustOptions var16 = DUST_RED;
                    Particles.spawn(var17, var61, var24, 1, 0.0, 0.0, 0.0, 0.0, var16, 1141219473);
                    var24.add(var21);
                }
            }
        }
    }

    private static void spawnDustCircle(Location var0, double var1, int var3) {
        World var22 = var0.getWorld();
        if (var22 != null) {
            double var58 = var1 * 18.0;
            int var23 = (int)Math.max(16.0, var58);

            for (int var24 = 0; var24 < var23; var24++) {
                double var61 = (double)var24;
                double var42 = (Math.PI * 2) * var61;
                double var64 = (double)var23;
                double var25 = var42 / var64;
                double var47 = var0.getX();
                double var78 = Math.cos(var25);
                double var68 = var1 * var78;
                double var27 = var47 + var68;
                double var52 = var0.getZ();
                double var82 = Math.sin(var25);
                double var72 = var1 * var82;
                double var29 = var52 + var72;
                Particle var57 = Particle.DUST;
                double var85 = var0.getY();
                DustOptions var21 = DUST_RED;
                Particles.spawn(var22, var57, var27, var85, var29, 1, 0.0, 0.0, 0.0, 0.0, var21, 2077675459);
            }
        }
    }

    public void startChadAuraTask(int var1) {
        BukkitScheduler var2 = Bukkit.getScheduler();
        BlissPlugin var10 = this.plugin;
        Runnable var11 = this::tickChadAura;
        var2.runTaskTimer(var10, var11, 5L, 5L);
    }

    private void tickChadAura() {
        long var8 = System.currentTimeMillis();

        for (Player var11 : Bukkit.getOnlinePlayers()) {
            UUID var12 = var11.getUniqueId();
            Map var23 = this.chadActiveUntil;
            if (isActive(var23, var12, var8, 1702798966)) {
                Map<UUID, Integer> var34 = this.testDoubleCounter;
                Integer var46 = 0;
                int var37 = var34.getOrDefault(var12, var46);
                this.drawChadSoloAura(var11, var37, 1174069768);
            } else {
                Map var27 = this.chadGroupActiveUntil;
                if (isActive(var27, var12, var8, 1702798966)) {
                    Map<UUID, Integer> var41 = this.testDoubleGroupCounter;
                    Integer var48 = 0;
                    int var44 = var41.getOrDefault(var12, var48);
                    this.drawChadGroupAura(var11, var44, 724046910);
                }
            }
        }
    }

    private void drawChadSoloAura(Player var1, int var2, int var3) {
        Location var13 = var1.getLocation();
        Vector var14 = var13.getDirection().setY(0);
        if (var14.lengthSquared() < 1.0E-6) {
            Vector var30 = new Vector(0, 0, 1);
            var14 = var30;
        }

        Vector var117 = var14.normalize();
        double var86 = var117.getZ();
        double var115 = -var117.getX();
        Vector var33 = new Vector(var86, 0.0, var115);
        Location var36 = var13.clone().add(0.0, 1.2, 0.0);
        Vector var61 = var117.clone().multiply(1.0);
        Location var37 = var36.add(var61);
        Vector var64 = var33.clone().multiply(1.0);
        Location var16 = var37.add(var64);
        Location var41 = var13.clone().add(0.0, 1.2, 0.0);
        Vector var69 = var117.clone().multiply(1.4);
        Location var17 = var41.add(var69);
        Location var45 = var13.clone().add(0.0, 1.2, 0.0);
        Vector var74 = var117.clone().multiply(1.0);
        Location var46 = var45.add(var74);
        Vector var77 = var33.clone().multiply(-1.0);
        Location var18 = var46.add(var77);
        DustOptions var56;
        if (var2 >= 1) {
            var56 = CHAD_DOT_RED;
        } else {
            var56 = CHAD_DOT_BLACK;
        }

        DustOptions var50;
        if (var2 >= 2) {
            var50 = CHAD_DOT_RED;
        } else {
            var50 = CHAD_DOT_BLACK;
        }

        DustOptions var52;
        if (var2 >= 3) {
            var52 = CHAD_DOT_RED;
        } else {
            var52 = CHAD_DOT_BLACK;
        }

        spawnDust(var16, 3, var56, 2005499262);
        spawnDust(var17, 3, var50, 2005499262);
        spawnDust(var18, 3, var52, 2005499262);
    }

    private void drawChadGroupAura(Player var1, int var2, int var3) {
        Location var15 = var1.getLocation();
        Vector var16 = var15.getDirection().setY(0);
        if (var16.lengthSquared() < 1.0E-6) {
            Vector var29 = new Vector(0, 0, 1);
            var16 = var29;
        }

        Vector var207 = var16.normalize();
        double var49 = var207.getZ();
        double var175 = -var207.getX();
        Vector var32 = new Vector(var49, 0.0, var175);
        Location[] var34 = new Location[8];
        Location var63 = var15.clone().add(0.0, 0.7, 0.0);
        Vector var103 = var207.clone().multiply(1.0);
        Location var64 = var63.add(var103);
        var34[0] = var64;
        Location var67 = var15.clone().add(0.0, 0.7, 0.0);
        Vector var108 = var207.clone().multiply(-1.0);
        Location var68 = var67.add(var108);
        var34[1] = var68;
        Location var71 = var15.clone().add(0.0, 0.7, 0.0);
        Vector var113 = var207.clone().multiply(0.7);
        Location var72 = var71.add(var113);
        Vector var116 = var32.clone().multiply(-1.0);
        Location var73 = var72.add(var116);
        var34[2] = var73;
        Location var76 = var15.clone().add(0.0, 0.7, 0.0);
        Vector var121 = var207.clone().multiply(-0.7);
        Location var77 = var76.add(var121);
        Vector var124 = var32.clone().multiply(-1.0);
        Location var78 = var77.add(var124);
        var34[3] = var78;
        Location var81 = var15.clone().add(0.0, 0.7, 0.0);
        Vector var129 = var207.clone().multiply(0.7);
        Location var82 = var81.add(var129);
        Vector var132 = var32.clone().multiply(1.0);
        Location var83 = var82.add(var132);
        var34[4] = var83;
        Location var86 = var15.clone().add(0.0, 0.7, 0.0);
        Vector var137 = var207.clone().multiply(-0.7);
        Location var87 = var86.add(var137);
        Vector var140 = var32.clone().multiply(1.0);
        Location var88 = var87.add(var140);
        var34[5] = var88;
        Location var91 = var15.clone().add(0.0, 0.7, 0.0);
        Vector var145 = var32.clone().multiply(1.5);
        Location var92 = var91.add(var145);
        var34[6] = var92;
        Location var95 = var15.clone().add(0.0, 0.7, 0.0);
        Vector var150 = var32.clone().multiply(-1.5);
        Location var96 = var95.add(var150);
        var34[7] = var96;
        Location[] var18 = var34;
        int var19 = 0;

        while (true) {
            int var42 = var18.length;
            if (var19 >= var42) {
                return;
            }

            int var44 = var19 + 1;
            DustOptions var40;
            if (var2 >= var44) {
                var40 = CHAD_DOT_RED;
            } else {
                var40 = CHAD_DOT_BLACK;
            }

            Location var39 = var18[var19];
            spawnDust(var39, 3, var40, 2005499262);
            var19++;
        }
    }

    private static void spawnDust(Location var0, int var1, DustOptions var2, int var3) {
        World var18 = var0.getWorld();
        if (var18 != null) {
            Particle var5 = Particle.DUST;
            Particles.spawn(var18, var5, var0, var1, 0.0, 0.0, 0.0, 0.0, var2, true, 1748031581);
        }
    }

    static {
        Color var11 = Color.fromRGB(199, 0, 10);
        DustOptions var0 = new DustOptions(var11, 1.5F);
        DUST_RED = var0;
        double[][] var8 = new double[8][];
        double[] var26 = new double[]{0.75, 1.0};
        var8[0] = var26;
        double[] var28 = new double[]{1.25, 1.5};
        var8[1] = var28;
        double[] var30 = new double[]{1.75, 2.0};
        var8[2] = var30;
        double[] var32 = new double[]{2.25, 2.5};
        var8[3] = var32;
        double[] var34 = new double[]{2.75, 3.0};
        var8[4] = var34;
        double[] var36 = new double[]{3.25, 3.5};
        var8[5] = var36;
        double[] var38 = new double[]{3.75, 4.0};
        var8[6] = var38;
        double[] var40 = new double[]{4.0};
        var8[7] = var40;
        NULLIFY_RINGS = var8;
        Color var21 = Color.fromRGB(15, 15, 15);
        DustOptions var9 = new DustOptions(var21, 1.0F);
        CHAD_DOT_BLACK = var9;
        Color var23 = Color.fromRGB(199, 0, 10);
        DustOptions var10 = new DustOptions(var23, 1.0F);
        CHAD_DOT_RED = var10;
    }
}
