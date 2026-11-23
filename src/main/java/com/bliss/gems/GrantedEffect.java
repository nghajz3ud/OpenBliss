package com.bliss.gems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class GrantedEffect {
    private static final int LINGERING_MIN_TICKS = 72000;
    private static final Map<UUID, Map<PotionEffectType, List<GrantedEffect.Grant>>> GRANTS;
    private static final int SLACK_TICKS = 2;

    private GrantedEffect() {
    }

    public static void grant(Player var0, PotionEffect var1, int var2) {
        PotionEffectType var10 = var1.getType();
        var0.addPotionEffect(var1);
        if (var0.getPotionEffect(var10) != null) {
            long var11 = (long)Bukkit.getCurrentTick();
            boolean var13 = var1.isInfinite();
            int var52;
            if (var13) {
                var52 = Integer.MAX_VALUE;
            } else {
                var52 = var1.getDuration();
            }

            long var53 = (long)var52;
            long var14 = var11 + var53;
            int var16 = var1.getAmplifier();
            Map var34 = GRANTS;
            UUID var45 = var0.getUniqueId();
            Function var55 = var0x -> new HashMap();
            Map var36 = (Map)var34.computeIfAbsent(var45, var55);
            Function var56 = var0x -> new ArrayList(3);
            List<GrantedEffect.Grant> var17 = (List<GrantedEffect.Grant>)var36.computeIfAbsent(var10, var56);
            Predicate<GrantedEffect.Grant> var49 = var5 -> {
                long var10x = var5.expiry();
                if (var10x >= var11) {
                    int var14x = var5.amp();
                    if (var14x == var16) {
                        long var16x = var5.expiry();
                        if (var16x <= var14) {
                            return true;
                        }
                    }

                    return false;
                } else {
                    return true;
                }
            };
            var17.removeIf(var49);
            GrantedEffect.Grant var50 = new GrantedEffect.Grant(var16, var14, var13);
            var17.add(var50);
        }
    }

    public static void forget(UUID var0, int var1) {
        Map var2 = GRANTS;
        var2.remove(var0);
    }

    public static void strip(Player var0, PotionEffectType var1, int var2) {
        Map var3 = GRANTS;
        UUID var49 = var0.getUniqueId();
        Map var7 = (Map)var3.get(var49);
        List<GrantedEffect.Grant> var16;
        if (var7 == null) {
            var16 = null;
        } else {
            var16 = (List<GrantedEffect.Grant>)var7.get(var1);
        }

        if (var16 != null && !var16.isEmpty()) {
            PotionEffect var9 = var0.getPotionEffect(var1);
            if (var9 == null) {
                var16.clear();
            } else {
                long var10 = (long)Bukkit.getCurrentTick();
                Predicate<GrantedEffect.Grant> var53 = var2x -> {
                    long var6 = var2x.expiry();
                    boolean var9x;
                    if (var6 < var10) {
                        var9x = true;
                    } else {
                        var9x = false;
                    }

                    return var9x;
                };
                var16.removeIf(var53);
                if (isOurs(var9, var16, var10, 1267004999)) {
                    PotionEffect var12 = var9.getHiddenPotionEffect();
                    var0.removePotionEffect(var1);
                    Predicate<GrantedEffect.Grant> var57 = var3x -> same(var3x, var9, var10, 675762639);
                    var16.removeIf(var57);
                    if (var12 != null) {
                        if (var12.isInfinite() || var12.getDuration() > 0) {
                            if (isOurs(var12, var16, var10, 1267004999)) {
                                Predicate<GrantedEffect.Grant> var61 = var3x -> same(var3x, var12, var10, 675762639);
                                var16.removeIf(var61);
                            } else {
                                var0.addPotionEffect(var12);
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isOurs(PotionEffect var0, List<GrantedEffect.Grant> var1, long var2, int var4) {
        for (GrantedEffect.Grant var17 : var1) {
            if (same(var17, var0, var2, 675762639)) {
                return true;
            }
        }

        return false;
    }

    private static boolean same(GrantedEffect.Grant var0, PotionEffect var1, long var2, int var4) {
        int var10 = var0.amp();
        int var31 = var1.getAmplifier();
        if (var10 != var31) {
            return false;
        } else if (!var1.isInfinite() && !var0.infinite()) {
            long var17 = (long)var1.getDuration();
            long var32 = var0.expiry();
            long var34 = var32 - var2;
            boolean var30;
            if (Math.abs(var17 - var34) <= 2L) {
                var30 = true;
            } else {
                var30 = false;
            }

            return var30;
        } else {
            boolean var28;
            if (var1.isInfinite() && var0.infinite()) {
                var28 = true;
            } else {
                var28 = false;
            }

            return var28;
        }
    }

    public static void stripLingering(Player var0, PotionEffect var1, int var2) {
        PotionEffectType var43 = var1.getType();
        PotionEffect var5 = var0.getPotionEffect(var43);
        if (var5 != null) {
            int var12 = var5.getAmplifier();
            int var45 = var1.getAmplifier();
            if (var12 == var45) {
                boolean var14 = var5.isAmbient();
                boolean var47 = var1.isAmbient();
                if (var14 == var47) {
                    boolean var16 = var5.hasParticles();
                    boolean var49 = var1.hasParticles();
                    if (var16 == var49) {
                        boolean var18 = var5.hasIcon();
                        boolean var51 = var1.hasIcon();
                        if (var18 == var51) {
                            if (!var5.isInfinite() && var5.getDuration() < 72000) {
                                return;
                            }

                            PotionEffect var6 = var5.getHiddenPotionEffect();
                            PotionEffectType var54 = var1.getType();
                            var0.removePotionEffect(var54);
                            Map var26 = GRANTS;
                            UUID var56 = var0.getUniqueId();
                            Map var7 = (Map)var26.get(var56);
                            List<GrantedEffect.Grant> var42;
                            if (var7 == null) {
                                var42 = null;
                            } else {
                                PotionEffectType var58 = var1.getType();
                                var42 = (List<GrantedEffect.Grant>)var7.get(var58);
                            }

                            if (var42 != null) {
                                Predicate<GrantedEffect.Grant> var60 = var1x -> {
                                    long var6x = (long)Bukkit.getCurrentTick();
                                    return same(var1x, var5, var6x, 675762639);
                                };
                                var42.removeIf(var60);
                            }

                            if (var6 != null && (var6.isInfinite() || var6.getDuration() > 0)) {
                                var0.addPotionEffect(var6);
                            }

                            return;
                        }
                    }
                }
            }
        }
    }

    static {
        HashMap var0 = new HashMap();
        GRANTS = var0;
    }

    private static record Grant(int amp, long expiry, boolean infinite) {
    }
}
