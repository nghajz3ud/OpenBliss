package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemAutoEnchants;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
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
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class FireAuraTask {
    private static final long PERIOD_TICKS = 5L;
    private static final long AUTO_ENCHANT_COOLDOWN_MS = 5000L;
    private static final int FIRE_RES_TICKS = 40;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final FireGemListener fire;
    private final GlobalState globalState;
    private BukkitTask task;

    public FireAuraTask(BlissPlugin var1, PlayerDataStore var2, FireGemListener var3, GlobalState var4, int var5) {
        this.plugin = var1;
        this.players = var2;
        this.fire = var3;
        this.globalState = var4;
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
        long var11 = System.currentTimeMillis();

        for (Player var14 : Bukkit.getOnlinePlayers()) {
            UUID var15 = var14.getUniqueId();
            PlayerInventory var16 = var14.getInventory();
            ItemStack var17 = var16.getItemInMainHand();
            ItemStack var18 = var16.getItemInOffHand();
            boolean var19 = BlissCoreItems.isFireGem(var17, 1896087501);
            boolean var20 = BlissCoreItems.isFireGem(var18, 1896087501);
            GemType var3 = GemType.FIRE;
            boolean var21 = GoldGem.hasAbsorbedSoul(var14, var3, 1603837876);
            if (var21) {
                PlayerDataStore var49 = this.players;
                if (var49.get(var15, 1264010596).canUsePassives(2051432579)) {
                    String var53 = "fire";
                    String var140 = "fire-resistance";
                    if (GemTuning.passive(var53, var140, true, 14309468)) {
                        PotionEffectType var5 = PotionEffectType.FIRE_RESISTANCE;
                        PotionEffect var141 = new PotionEffect(var5, 40, 0, true, false, false);
                        GrantedEffect.grant(var14, var141, 491118892);
                    }

                    boolean var138;
                    label123: {
                        Map var58 = this.fire.cd1;
                        Long var23 = (Long)var58.get(var15);
                        if (var23 != null) {
                            long var63 = var23;
                            if (var63 > var11) {
                                var138 = false;
                                break label123;
                            }
                        }

                        var138 = true;
                    }

                    if (var138
                        && (
                            GemAutoEnchants.allStages(var15, 1630297028)
                                || !GlobalState.forPlayer(var14, 1433087673).prog1(494064183) && !GlobalState.forPlayer(var14, 1433087673).prog2(1337683067)
                        )) {
                        autoEnchant(var16, 2, 1940084152);
                        Map var78 = this.fire.cd1;
                        Long var171 = var11 + 5000L;
                        var78.put(var15, var171);
                    }
                }
            }

            GemType var145 = GemType.FIRE;
            if (GoldGem.isWieldingSoul(var14, var145, 100010596)) {
                if (!AssessmentWindowBarTask.isShowing(1134156722) && !MotifSpeechListener.isSpeakingTo(var14.getUniqueId(), 1524431478)) {
                    Component var164 = Text.legacy(this.buildActionBar(2, var15, 1884412888), 373192400);
                    var14.sendActionBar(var164);
                }
            } else if (!var21 && (var19 || var20)) {
                PlayerDataStore var86 = this.players;
                if (!var86.get(var15, 1264010596).canUsePassives(2051432579)) {
                    String var90 = "fire";
                    String var147 = "fire-resistance";
                    if (GemTuning.passive(var90, var147, true, 14309468)) {
                        PotionEffectType var148 = PotionEffectType.FIRE_RESISTANCE;
                        GrantedEffect.strip(var14, var148, 553187889);
                    }
                } else {
                    String var93 = "fire";
                    String var149 = "fire-resistance";
                    if (GemTuning.passive(var93, var149, true, 14309468)) {
                        PotionEffectType var183 = PotionEffectType.FIRE_RESISTANCE;
                        PotionEffect var150 = new PotionEffect(var183, 40, 0, true, false, false);
                        GrantedEffect.grant(var14, var150, 491118892);
                    }

                    ItemStack var97;
                    if (var19) {
                        var97 = var17;
                    } else {
                        var97 = var18;
                    }

                    boolean var132;
                    int var196;
                    label102: {
                        var196 = BlissCoreItems.tierOf(var97, 1633828214);
                        Map var101 = this.fire.cd1;
                        Long var197 = (Long)var101.get(var15);
                        if (var197 != null) {
                            long var106 = var197;
                            if (var106 > var11) {
                                var132 = false;
                                break label102;
                            }
                        }

                        var132 = true;
                    }

                    if (var132
                        && (
                            GemAutoEnchants.allStages(var15, 1630297028)
                                || !GlobalState.forPlayer(var14, 1433087673).prog1(494064183) && !GlobalState.forPlayer(var14, 1433087673).prog2(1337683067)
                        )) {
                        autoEnchant(var16, var196, 1940084152);
                        Map var121 = this.fire.cd1;
                        Long var180 = var11 + 5000L;
                        var121.put(var15, var180);
                    }

                    PlayerDataStore var124 = this.players;
                    UUID var155 = var14.getUniqueId();
                    if (var124.get(var155, 1264010596).energy(1160853318) >= 2) {
                        PlayerDataStore var128 = this.players;
                        UUID var158 = var14.getUniqueId();
                        if (!var128.get(var158, 1264010596).hudSuppressed(859110487)) {
                            Component var161 = Text.legacy(this.buildActionBar(var196, var15, 1884412888), 373192400);
                            var14.sendActionBar(var161);
                        }
                    }
                }
            }
        }
    }

    private static void autoEnchant(PlayerInventory var0, int var1, int var2) {
        Enchantment var4 = Enchantment.FIRE_ASPECT;
        byte var60;
        if (var1 == 2) {
            var60 = 2;
        } else {
            var60 = 1;
        }

        int var7 = GemAutoEnchants.allow(var0, var4, var60, 987370355);
        Enchantment var45 = Enchantment.FLAME;
        int var8 = GemAutoEnchants.allow(var0, var45, 1, 987370355);
        String var17 = "fire";
        String var46 = "auto-fire-aspect";
        if (!GemTuning.passive(var17, var46, true, 14309468)) {
            var7 = 0;
        }

        String var20 = "fire";
        String var47 = "auto-flame";
        if (!GemTuning.passive(var20, var47, true, 14309468)) {
            var8 = 0;
        }

        for (ItemStack var12 : var0.getContents()) {
            if (var12 != null) {
                Material var13 = var12.getType();
                if (isSword(var13, 1063236044)) {
                    if (var7 > 0) {
                        Enchantment var50 = Enchantment.FIRE_ASPECT;
                        if (var12.getEnchantmentLevel(var50) == 0) {
                            Enchantment var51 = Enchantment.FIRE_ASPECT;
                            var12.addUnsafeEnchantment(var51, var7);
                        }
                    }
                } else {
                    Material var52 = Material.BOW;
                    if (var13 == var52 && var8 > 0) {
                        Enchantment var53 = Enchantment.FLAME;
                        if (var12.getEnchantmentLevel(var53) == 0) {
                            Enchantment var54 = Enchantment.FLAME;
                            var12.addUnsafeEnchantment(var54, var8);
                        }
                    }
                }
            }
        }
    }

    private String buildActionBar(int var1, UUID var2, int var3) {
        Map var11 = this.fire.cd2;
        String var7 = timer(var11, var2, 610093303);
        Map var15 = this.fire.cd3;
        String var8 = timer(var15, var2, 610093303);
        Map var19 = this.fire.crispActiveUntil;
        Map var29 = this.fire.cd4;
        String var9 = activeOrTimer(var19, var29, var2, 1029018002);
        return var1 == 2 ? "&f\ud83e\udde8 &b" + var8 + "  &6\ud83d\udd2e &b" + var9 + "  &f\ud83e\udd7e &b" + var7 : "&6\ud83d\udd3a &b" + var9;
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

        return timer(var1, var2, 610093303);
    }

    private static String timer(Map<UUID, Long> var0, UUID var1, int var2) {
        Long var7 = (Long)var0.get(var1);
        if (var7 == null) {
            return "&aReady!";
        } else {
            long var8 = System.currentTimeMillis();
            long var20 = var7;
            if (var20 <= var8) {
                return "&aReady!";
            } else {
                long var25 = var7;
                int var10 = (int)((var25 - var8 + 999L) / 1000L);
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

    private static boolean isSword(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD-> true;
            default -> false;
        };
    }
}
