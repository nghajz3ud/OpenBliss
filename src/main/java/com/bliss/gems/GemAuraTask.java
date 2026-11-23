package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemAutoEnchants;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.entity.MotifSpeechListener;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.AssessmentWindowBarTask;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class GemAuraTask {
    private static final int TICK_PERIOD = 20;
    private static final int STRENGTH_EFFECT_TICKS = 40;
    private static final int SHARPNESS_COOLDOWN_SECS = 5;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final StrengthGemListener strengthListener;
    private final Map<UUID, Integer> sharpnessCooldown;
    private BukkitTask task;

    public GemAuraTask(BlissPlugin var1, PlayerDataStore var2, StrengthGemListener var3, int var4) {
        HashMap var6 = new HashMap();
        this.sharpnessCooldown = var6;
        this.plugin = var1;
        this.players = var2;
        this.strengthListener = var3;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 20L, 20L);
        this.task = var10;
    }

    private boolean fullStrengthPassiveAllowed(Player var1, int var2) {
        GlobalState var4 = GlobalState.forPlayer(var1, 1433087673);
        if (var4 == null) {
            return true;
        } else {
            boolean var14;
            if (var4.prog1(494064183) && !var4.prog2(1337683067) && !var4.strengthProg1FullPassive(2079279880)) {
                var14 = false;
            } else {
                var14 = true;
            }

            return var14;
        }
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void tick() {
        for (Player var11 : Bukkit.getOnlinePlayers()) {
            UUID var12 = var11.getUniqueId();
            byte var37;
            if (GlobalState.forPlayer(var11, 1433087673).strengthTwoPassive(1352452848)) {
                var37 = 1;
            } else {
                var37 = 0;
            }

            Map<UUID, Integer> var39 = this.sharpnessCooldown;
            Integer var162 = 0;
            int var14 = var39.getOrDefault(var12, var162);
            if (var14 > 0) {
                Map var45 = this.sharpnessCooldown;
                Integer var165 = var14 - 1;
                var45.put(var12, var165);
            }

            PlayerInventory var15 = var11.getInventory();
            ItemStack var16 = var15.getItemInMainHand();
            ItemStack var17 = var15.getItemInOffHand();
            boolean var18 = BlissCoreItems.isStrengthGem(var16, 1818434225);
            boolean var19 = BlissCoreItems.isStrengthGem(var17, 1818434225);
            GemType var130 = GemType.STRENGTH;
            boolean var20 = GoldGem.hasAbsorbedSoul(var11, var130, 1603837876);
            if (var20) {
                PlayerDataStore var61 = this.players;
                PlayerData var21 = var61.get(var12, 1264010596);
                if (var21.canUsePassives(2051432579)) {
                    if (this.fullStrengthPassiveAllowed(var11, 1823644299)) {
                        String var67 = "strength";
                        String var133 = "strength";
                        if (GemTuning.passive(var67, var133, true, 14309468)) {
                            PotionEffectType var178 = PotionEffectType.STRENGTH;
                            PotionEffect var134 = new PotionEffect(var178, 40, var37, true, false, false);
                            GrantedEffect.grant(var11, var134, 491118892);
                        }
                    }

                    if (var14 == 0) {
                        int var136 = var21.energy(1160853318);
                        int var22 = sharpnessLevelFor(2, var136, 1197689277);
                        if (var22 > 0) {
                            PlayerInventory var75 = var11.getInventory();
                            applySharpness(var75, 2, var22, 663217689);
                        }

                        Map var77 = this.sharpnessCooldown;
                        Integer var169 = 5;
                        var77.put(var12, var169);
                    }
                }
            }

            GemType var139 = GemType.STRENGTH;
            if (GoldGem.isWieldingSoul(var11, var139, 100010596)) {
                if (!AssessmentWindowBarTask.isShowing(1134156722) && !MotifSpeechListener.isSpeakingTo(var11.getUniqueId(), 1524431478)) {
                    Component var161 = Text.legacy(this.buildActionBar(2, var12, 1629848743), 373192400);
                    var11.sendActionBar(var161);
                }
            } else if (!var20 && (var18 || var19)) {
                ItemStack var123;
                if (var18) {
                    var123 = var16;
                } else {
                    var123 = var17;
                }

                PersistentDataContainer var87 = var123.getItemMeta().getPersistentDataContainer();
                NamespacedKey var140 = BlissCoreItems.KEY_TIER;
                PersistentDataType var170 = PersistentDataType.INTEGER;
                Integer var188 = (Integer)var87.get(var140, var170);
                int var92;
                if (var188 == null) {
                    var92 = 1;
                } else {
                    var92 = var188;
                }

                PlayerDataStore var94 = this.players;
                PlayerData var24 = var94.get(var12, 1264010596);
                if (var24.canUsePassives(2051432579)) {
                    if (this.fullStrengthPassiveAllowed(var11, 1823644299)) {
                        String var100 = "strength";
                        String var143 = "strength";
                        if (GemTuning.passive(var100, var143, true, 14309468)) {
                            PotionEffectType var179 = PotionEffectType.STRENGTH;
                            PotionEffect var144 = new PotionEffect(var179, 40, var37, true, false, false);
                            GrantedEffect.grant(var11, var144, 491118892);
                        }
                    }

                    if (var14 == 0) {
                        int var146 = var24.energy(1160853318);
                        int var25 = sharpnessLevelFor(var92, var146, 1197689277);
                        if (var25 > 0) {
                            applySharpness(var15, var92, var25, 663217689);
                        }

                        Map var109 = this.sharpnessCooldown;
                        Integer var174 = 5;
                        var109.put(var12, var174);
                    }
                } else {
                    String var120 = "strength";
                    String var157 = "strength";
                    if (GemTuning.passive(var120, var157, true, 14309468)) {
                        PotionEffectType var158 = PotionEffectType.STRENGTH;
                        GrantedEffect.strip(var11, var158, 553187889);
                    }
                }

                PlayerDataStore var112 = this.players;
                UUID var150 = var11.getUniqueId();
                if (var112.get(var150, 1264010596).energy(1160853318) >= 2) {
                    PlayerDataStore var116 = this.players;
                    UUID var153 = var11.getUniqueId();
                    if (!var116.get(var153, 1264010596).hudSuppressed(859110487)) {
                        Component var156 = Text.legacy(this.buildActionBar(var92, var12, 1629848743), 373192400);
                        var11.sendActionBar(var156);
                    }
                }
            }
        }
    }

    private String buildActionBar(int var1, UUID var2, int var3) {
        String var30;
        if (this.strengthListener == null) {
            var30 = "&aReady!";
        } else {
            Map var29 = this.strengthListener.cd2;
            var30 = timer(var29, var2, 464906722);
        }

        String var13;
        if (this.strengthListener == null) {
            var13 = "&aReady!";
        } else {
            Map var26 = this.strengthListener.cd3;
            var13 = timer(var26, var2, 464906722);
        }

        String var31;
        if (this.strengthListener == null) {
            var31 = "&aReady!";
        } else {
            Map var18 = this.strengthListener.bountyActiveUntil;
            Map var33 = this.strengthListener.cd4;
            var31 = activeOrTimer(var18, var33, var2, 1117624992);
        }

        return var1 == 2 ? "&4\ud83e\udd3a &b" + var30 + "  &4\ud83d\udd2e " + var31 + " &4⚔ &b" + var13 : "&4\ud83d\udd3a " + var31;
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

        return timer(var1, var2, 464906722);
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

    private static int sharpnessLevelFor(int var0, int var1, int var2) {
        if (var0 == 1) {
            if (var1 > 4) {
                return 2;
            } else {
                return var1 > 3 ? 1 : 0;
            }
        } else if (var1 > 5) {
            return 5;
        } else if (var1 == 5) {
            return 4;
        } else {
            return var1 == 4 ? 3 : 0;
        }
    }

    private static void applySharpness(PlayerInventory var0, int var1, int var2, int var3) {
        for (ItemStack var10 : var0.getContents()) {
            if (var10 != null && isSwordOrAxe(var10.getType(), 1514566486)) {
                Enchantment var36 = Enchantment.SHARPNESS;
                int var11 = GemAutoEnchants.allow(var0, var36, var2, 987370355);
                String var26 = "strength";
                String var37 = "auto-sharpness";
                if (GemTuning.passive(var26, var37, true, 14309468) && var11 > 0) {
                    Enchantment var38 = Enchantment.SHARPNESS;
                    int var12 = var10.getEnchantmentLevel(var38);
                    if ((var1 != 1 || var12 <= 0) && var12 < var11) {
                        Enchantment var41 = Enchantment.SHARPNESS;
                        var10.addUnsafeEnchantment(var41, var11);
                    }
                }
            }
        }
    }

    private static boolean isSwordOrAxe(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD, WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE-> true;
            default -> false;
        };
    }
}
