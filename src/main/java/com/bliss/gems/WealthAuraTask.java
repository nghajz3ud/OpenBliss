package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemAutoEnchants;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
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

public final class WealthAuraTask {
    private static final long PERIOD_TICKS = 5L;
    private static final long AUTO_ENCHANT_COOLDOWN_MS = 5000L;
    private static final int PASSIVE_TICKS = 1200;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final WealthGemListener wealth;
    private BukkitTask task;

    public WealthAuraTask(BlissPlugin var1, PlayerDataStore var2, WealthGemListener var3, int var4) {
        this.plugin = var1;
        this.players = var2;
        this.wealth = var3;
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
            int var41 = BlissCoreItems.gemIdOf(var17, 382441079);
            int var138 = GemType.WEALTH.id();
            boolean var42;
            if (var41 == var138) {
                var42 = true;
            } else {
                var42 = false;
            }

            int var44 = BlissCoreItems.gemIdOf(var18, 382441079);
            int var140 = GemType.WEALTH.id();
            boolean var45;
            if (var44 == var140) {
                var45 = true;
            } else {
                var45 = false;
            }

            GemType var141 = GemType.WEALTH;
            boolean var21 = GoldGem.hasAbsorbedSoul(var14, var141, 1603837876);
            if (var21) {
                PlayerDataStore var50 = this.players;
                label127:
                if (var50.get(var15, 1264010596).canUsePassives(2051432579)) {
                    PlayerInventory var227 = var14.getInventory();
                    String var116 = "wealth";
                    String var171 = "hero-of-the-village";
                    if (GemTuning.passive(var116, var171, true, 14309468)) {
                        PotionEffectType var204 = PotionEffectType.HERO_OF_THE_VILLAGE;
                        PotionEffect var172 = new PotionEffect(var204, 1200, 1, true, false, false);
                        GrantedEffect.grant(var14, var172, 491118892);
                    }

                    String var119 = "wealth";
                    String var173 = "luck";
                    if (GemTuning.passive(var119, var173, true, 14309468)) {
                        PotionEffectType var205 = PotionEffectType.LUCK;
                        PotionEffect var174 = new PotionEffect(var205, 1200, 0, true, false, false);
                        GrantedEffect.grant(var14, var174, 491118892);
                    }

                    Map var124 = this.wealth.cd1;
                    Long var229 = (Long)var124.get(var15);
                    if (var229 != null) {
                        long var129 = var229;
                        if (var129 > var11) {
                            break label127;
                        }
                    }

                    autoEnchant(var227, 2, 948614183);
                    Map var135 = this.wealth.cd1;
                    Long var200 = var11 + 5000L;
                    var135.put(var15, var200);
                } else {
                    String var53 = "wealth";
                    String var143 = "hero-of-the-village";
                    if (GemTuning.passive(var53, var143, true, 14309468)) {
                        PotionEffectType var144 = PotionEffectType.HERO_OF_THE_VILLAGE;
                        GrantedEffect.strip(var14, var144, 553187889);
                    }

                    String var56 = "wealth";
                    String var145 = "luck";
                    if (GemTuning.passive(var56, var145, true, 14309468)) {
                        PotionEffectType var146 = PotionEffectType.LUCK;
                        GrantedEffect.strip(var14, var146, 553187889);
                    }
                }
            }

            GemType var147 = GemType.WEALTH;
            if (GoldGem.isWieldingSoul(var14, var147, 100010596)) {
                if (!AssessmentWindowBarTask.isShowing(1134156722) && !MotifSpeechListener.isSpeakingTo(var14.getUniqueId(), 1524431478)) {
                    Component var170 = Text.legacy(this.buildActionBar(2, var15, 1199620128), 373192400);
                    var14.sendActionBar(var170);
                }
            } else if (!var21 && (var42 || var45)) {
                PlayerDataStore var65 = this.players;
                if (!var65.get(var15, 1264010596).canUsePassives(2051432579)) {
                    String var102 = "wealth";
                    String var164 = "hero-of-the-village";
                    if (GemTuning.passive(var102, var164, true, 14309468)) {
                        PotionEffectType var165 = PotionEffectType.HERO_OF_THE_VILLAGE;
                        GrantedEffect.strip(var14, var165, 553187889);
                    }

                    String var105 = "wealth";
                    String var166 = "luck";
                    if (GemTuning.passive(var105, var166, true, 14309468)) {
                        PotionEffectType var167 = PotionEffectType.LUCK;
                        GrantedEffect.strip(var14, var167, 553187889);
                    }
                } else {
                    ItemStack var137;
                    if (var42) {
                        var137 = var17;
                    } else {
                        var137 = var18;
                    }

                    int var23 = BlissCoreItems.tierOf(var137, 1633828214);
                    String var71 = "wealth";
                    String var149 = "hero-of-the-village";
                    if (GemTuning.passive(var71, var149, true, 14309468)) {
                        PotionEffectType var5 = PotionEffectType.HERO_OF_THE_VILLAGE;
                        byte var216;
                        if (var23 == 2) {
                            var216 = 1;
                        } else {
                            var216 = 0;
                        }

                        PotionEffect var150 = new PotionEffect(var5, 1200, var216, true, false, false);
                        GrantedEffect.grant(var14, var150, 491118892);
                    }

                    String var74 = "wealth";
                    String var151 = "luck";
                    if (GemTuning.passive(var74, var151, true, 14309468)) {
                        PotionEffectType var201 = PotionEffectType.LUCK;
                        PotionEffect var152 = new PotionEffect(var201, 1200, 0, true, false, false);
                        GrantedEffect.grant(var14, var152, 491118892);
                    }

                    PlayerDataStore var78 = this.players;
                    UUID var154 = var14.getUniqueId();
                    if (var78.get(var154, 1264010596).energy(1160853318) >= 2) {
                        PlayerDataStore var82 = this.players;
                        UUID var157 = var14.getUniqueId();
                        if (!var82.get(var157, 1264010596).hudSuppressed(859110487)) {
                            Component var160 = Text.legacy(this.buildActionBar(var23, var15, 1199620128), 373192400);
                            var14.sendActionBar(var160);
                        }
                    }

                    if (var42) {
                        Map var89 = this.wealth.cd1;
                        Long var24 = (Long)var89.get(var15);
                        if (var24 != null) {
                            long var94 = var24;
                            if (var94 > var11) {
                                continue;
                            }
                        }

                        autoEnchant(var16, var23, 948614183);
                        Map var100 = this.wealth.cd1;
                        Long var188 = var11 + 5000L;
                        var100.put(var15, var188);
                    }
                }
            }
        }
    }

    private String buildActionBar(int var1, UUID var2, int var3) {
        if (var1 == 2) {
            Map var9 = this.wealth.cd3;
            String var10 = timer(var9, var2, 288834408);
            Map var16 = this.wealth.cd2;
            String var17 = timer(var16, var2, 288834408);
            return "&a\ud83c\udf40 &b" + var10 + "  &2\ud83d\udd2e &a\ud83d\udcb8 &b" + var17;
        } else {
            return "&2\ud83d\udd3a";
        }
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

    private static void autoEnchant(PlayerInventory var0, int var1, int var2) {
        boolean var15;
        if (var1 == 2) {
            var15 = true;
        } else {
            var15 = false;
        }

        boolean var6 = var15;
        byte var17;
        if (var15) {
            var17 = 3;
        } else {
            var17 = 1;
        }

        byte var7 = var17;
        byte var70;
        if (var15) {
            var70 = 3;
        } else {
            var70 = 1;
        }

        byte var8 = var70;

        for (ItemStack var12 : var0.getContents()) {
            if (var12 != null) {
                Material var13 = var12.getType();
                if (isPickaxe(var13, 871292223) || isAxe(var13, 431231762) || isShovel(var13, 1168529355)) {
                    Enchantment var73 = Enchantment.FORTUNE;
                    int var14 = GemAutoEnchants.allow(var0, var73, var7, 987370355);
                    String var38 = "wealth";
                    String var74 = "auto-fortune";
                    if (!GemTuning.passive(var38, var74, true, 14309468)) {
                        var14 = 0;
                    }

                    if (var14 > 0) {
                        Enchantment var75 = Enchantment.SILK_TOUCH;
                        if (var12.getEnchantmentLevel(var75) == 0) {
                            Enchantment var76 = Enchantment.FORTUNE;
                            if (var12.getEnchantmentLevel(var76) == 0) {
                                Enchantment var77 = Enchantment.FORTUNE;
                                var12.addUnsafeEnchantment(var77, var14);
                            }
                        }
                    }
                }

                if (isSword(var13, 829737458)) {
                    Enchantment var78 = Enchantment.LOOTING;
                    int var95 = GemAutoEnchants.allow(var0, var78, var8, 987370355);
                    String var51 = "wealth";
                    String var79 = "auto-looting";
                    if (!GemTuning.passive(var51, var79, true, 14309468)) {
                        var95 = 0;
                    }

                    if (var95 > 0) {
                        Enchantment var80 = Enchantment.LOOTING;
                        int var56 = var12.getEnchantmentLevel(var80);
                        if (var56 < var95) {
                            Enchantment var82 = Enchantment.LOOTING;
                            var12.addUnsafeEnchantment(var82, var95);
                        }
                    }
                }

                if (var6 && isMendable(var13, 501993998)) {
                    Enchantment var83 = Enchantment.MENDING;
                    int var96 = GemAutoEnchants.allow(var0, var83, 1, 987370355);
                    String var63 = "wealth";
                    String var84 = "auto-mending";
                    if (!GemTuning.passive(var63, var84, true, 14309468)) {
                        var96 = 0;
                    }

                    if (var96 > 0) {
                        Enchantment var85 = Enchantment.MENDING;
                        if (var12.getEnchantmentLevel(var85) == 0) {
                            Enchantment var86 = Enchantment.MENDING;
                            var12.addUnsafeEnchantment(var86, var96);
                        }
                    }
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

    private static boolean isPickaxe(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE-> true;
            default -> false;
        };
    }

    private static boolean isAxe(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE-> true;
            default -> false;
        };
    }

    private static boolean isShovel(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_SHOVEL, STONE_SHOVEL, IRON_SHOVEL, GOLDEN_SHOVEL, DIAMOND_SHOVEL, NETHERITE_SHOVEL-> true;
            default -> false;
        };
    }

    private static boolean isMendable(Material var0, int var1) {
        if (!isSword(var0, 829737458) && !isPickaxe(var0, 871292223) && !isAxe(var0, 431231762)) {
            Material var3 = Material.SHIELD;
            if (var0 == var3) {
                return true;
            } else {
                return switch (var0) {
                    case WOODEN_SHOVEL, STONE_SHOVEL, IRON_SHOVEL, GOLDEN_SHOVEL, DIAMOND_SHOVEL, NETHERITE_SHOVEL-> true;
                    case LEATHER_HELMET, IRON_HELMET, GOLDEN_HELMET, DIAMOND_HELMET, NETHERITE_HELMET, CHAINMAIL_HELMET, TURTLE_HELMET-> true;
                    case LEATHER_CHESTPLATE, IRON_CHESTPLATE, GOLDEN_CHESTPLATE, DIAMOND_CHESTPLATE, NETHERITE_CHESTPLATE, CHAINMAIL_CHESTPLATE, ELYTRA-> true;
                    case LEATHER_LEGGINGS, IRON_LEGGINGS, GOLDEN_LEGGINGS, DIAMOND_LEGGINGS, NETHERITE_LEGGINGS, CHAINMAIL_LEGGINGS-> true;
                    case LEATHER_BOOTS, IRON_BOOTS, GOLDEN_BOOTS, DIAMOND_BOOTS, NETHERITE_BOOTS, CHAINMAIL_BOOTS-> true;
                    case WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE, DIAMOND_HOE, NETHERITE_HOE-> true;
                    default -> false;
                };
            }
        } else {
            return true;
        }
    }
}
