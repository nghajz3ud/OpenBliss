package com.bliss.gems.legacy;

import com.bliss.BlissPlugin;
import com.bliss.core.GemAutoEnchants;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.gems.GrantedEffect;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class LegacySpeedAuraTask {
    private static final long PERIOD_TICKS = 5L;
    private static final long AUTO_ENCHANT_COOLDOWN_MS = 5000L;
    private static final int PASSIVE_TICKS = 40;
    private static final double ATTACK_SPEED_BASE = 4.0;
    private static final double ATTACK_SPEED_HASTE = 5.0;
    private static final double ATTACK_SPEED_SURGE = 7.0;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final LegacySpeedState state;
    private final GlobalState globalState;
    private BukkitTask task;
    private final Map<UUID, Long> enchantCd;
    private final Set<UUID> attackSpeedRaised;

    public LegacySpeedAuraTask(BlissPlugin var1, PlayerDataStore var2, LegacySpeedState var3, GlobalState var4, int var5) {
        ConcurrentHashMap var7 = new ConcurrentHashMap();
        this.enchantCd = var7;
        KeySetView var14 = ConcurrentHashMap.newKeySet();
        this.attackSpeedRaised = var14;
        this.plugin = var1;
        this.players = var2;
        this.state = var3;
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
        long var9 = System.currentTimeMillis();

        for (Player var12 : Bukkit.getOnlinePlayers()) {
            UUID var13 = var12.getUniqueId();
            this.applyAttackSpeed(var12, var9, 359367850);
            GlobalState var33 = GlobalState.forPlayer(var12, 1433087673);
            if (var33.legacySpeedFor(var12, 1914133154)) {
                PlayerInventory var14 = var12.getInventory();
                ItemStack var15 = var14.getItemInMainHand();
                ItemStack var16 = var14.getItemInOffHand();
                int var42 = BlissCoreItems.gemIdOf(var15, 382441079);
                int var77 = GemType.SPEED.id();
                boolean var74;
                if (var42 == var77) {
                    var74 = true;
                } else {
                    var74 = false;
                }

                int var44 = BlissCoreItems.gemIdOf(var16, 382441079);
                int var79 = GemType.SPEED.id();
                boolean var73;
                if (var44 == var79) {
                    var73 = true;
                } else {
                    var73 = false;
                }

                if (var74 || var73) {
                    PlayerDataStore var48 = this.players;
                    PlayerData var19 = var48.get(var13, 1264010596);
                    if (var19.canUsePassives(2051432579)) {
                        ItemStack var72;
                        if (var74) {
                            var72 = var15;
                        } else {
                            var72 = var16;
                        }

                        int var20 = BlissCoreItems.tierOf(var72, 1633828214);
                        int var99 = var19.energy(1160853318);
                        this.applyPassive(var12, var20, var99, 88320394);
                        if (!var19.hudSuppressed(859110487)) {
                            Component var84 = Text.legacy(this.buildActionBar(var20, var13, 1156062990), 373192400);
                            var12.sendActionBar(var84);
                        }

                        if (var74) {
                            Map var60 = this.enchantCd;
                            Long var21 = (Long)var60.get(var13);
                            if (var21 != null) {
                                long var65 = var21;
                                if (var65 > var9) {
                                    continue;
                                }
                            }

                            int var93 = var19.energy(1160853318);
                            autoEnchant(var14, var20, var93, 276416164);
                            Map var70 = this.enchantCd;
                            Long var98 = var9 + 5000L;
                            var70.put(var13, var98);
                        }
                    }
                }
            }
        }
    }

    private void applyAttackSpeed(Player var1, long var2, int var4) {
        UUID var9;
        Long var10;
        Long var11;
        double var12;
        label58: {
            var9 = var1.getUniqueId();
            Map var18 = this.state.surgeUntil;
            var10 = (Long)var18.get(var9);
            Map var23 = this.state.hasteUntil;
            var11 = (Long)var23.get(var9);
            var12 = 4.0;
            if (var10 != null) {
                long var30 = var10;
                if (var30 > var2) {
                    var12 = 7.0;
                    break label58;
                }
            }

            if (var11 != null) {
                long var77 = var11;
                if (var77 > var2) {
                    var12 = 5.0;
                }
            }
        }

        if (var10 != null) {
            long var37 = var10;
            if (var37 <= var2) {
                Map var42 = this.state.surgeUntil;
                var42.remove(var9);
            }
        }

        if (var11 != null) {
            long var46 = var11;
            if (var46 <= var2) {
                Map var51 = this.state.hasteUntil;
                var51.remove(var9);
            }
        }

        Attribute var85 = Attribute.ATTACK_SPEED;
        AttributeInstance var14 = var1.getAttribute(var85);
        if (var14 != null) {
            if (var12 != 4.0) {
                double var68 = var14.getBaseValue();
                if (var68 != var12) {
                    var14.setBaseValue(var12);
                }

                Set var73 = this.attackSpeedRaised;
                var73.add(var9);
            } else {
                Set var60 = this.attackSpeedRaised;
                if (var60.remove(var9) || var14.getBaseValue() != 4.0) {
                    var14.setBaseValue(4.0);
                }
            }
        }
    }

    private void applyPassive(Player var1, int var2, int var3, int var4) {
        String var5 = "speed";
        String var6 = "speed";
        boolean var12 = GemTuning.passive(var5, var6, true, 14309468);
        String var17 = "speed";
        String var51 = "dolphins-grace";
        boolean var13 = GemTuning.passive(var17, var51, true, 14309468);
        if (var2 == 2 && var3 > 4) {
            boolean var27;
            label61: {
                ItemStack var14 = var1.getInventory().getBoots();
                if (var14 != null) {
                    Enchantment var54 = Enchantment.DEPTH_STRIDER;
                    if (var14.getEnchantmentLevel(var54) > 0) {
                        var27 = true;
                        break label61;
                    }
                }

                var27 = false;
            }

            if (var27) {
                if (var13) {
                    PotionEffectType var55 = PotionEffectType.DOLPHINS_GRACE;
                    GrantedEffect.strip(var1, var55, 553187889);
                }

                if (var12) {
                    PotionEffectType var56 = PotionEffectType.SPEED;
                    effect(var1, var56, 1, 541252281);
                }
            } else if (var1.isInWater()) {
                if (var12) {
                    PotionEffectType var57 = PotionEffectType.SPEED;
                    effect(var1, var57, 1, 541252281);
                }

                if (var13) {
                    PotionEffectType var58 = PotionEffectType.DOLPHINS_GRACE;
                    effect(var1, var58, 0, 541252281);
                }

                if (var1.isSprinting()) {
                    trail(var1.getLocation().clone().subtract(0.0, 0.5, 0.0), 1295551841);
                }
            } else {
                if (var13) {
                    PotionEffectType var61 = PotionEffectType.DOLPHINS_GRACE;
                    GrantedEffect.strip(var1, var61, 553187889);
                }

                if (var12) {
                    PotionEffectType var62 = PotionEffectType.SPEED;
                    effect(var1, var62, 1, 541252281);
                }
            }
        } else if (var12) {
            PotionEffectType var63 = PotionEffectType.SPEED;
            effect(var1, var63, 0, 541252281);
        }
    }

    private static void effect(Player var0, PotionEffectType var1, int var2, int var3) {
        PotionEffect var5 = new PotionEffect(var1, 40, var2, true, false, false);
        GrantedEffect.grant(var0, var5, 491118892);
    }

    private static void trail(Location var0, int var1) {
        if (var0.getWorld() != null) {
            World var20 = var0.getWorld();
            Particle var3 = Particle.DUST;
            Color var22 = Color.fromRGB(244, 255, 28);
            DustOptions var14 = new DustOptions(var22, 1.0F);
            Particles.spawn(var20, var3, var0, 1, 0.0, 0.0, 0.0, 0.0, var14, false, 1748031581);
        }
    }

    private String buildActionBar(int var1, UUID var2, int var3) {
        if (var1 == 2) {
            Map var10 = this.state.blurCd;
            String var11 = timer(var10, var2, 1791325487);
            Map var21 = this.state.terminalCd;
            String var22 = timer(var21, var2, 1791325487);
            Map var26 = this.state.stormCd;
            String var27 = timer(var26, var2, 1791325487);
            return "&e\ud83c\udfaf &b" + var11 + "  &e\ud83d\udd2e &b" + var22 + " &e\ud83c\udf29 &b" + var27;
        } else {
            Map var15 = this.state.terminalCd;
            return "&e\ud83d\udd3a &b" + timer(var15, var2, 1791325487);
        }
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

    private static void autoEnchant(PlayerInventory var0, int var1, int var2, int var3) {
        Enchantment var5 = Enchantment.EFFICIENCY;
        int var67 = efficiencyLevel(var1, var2, 1111308413);
        int var9 = GemAutoEnchants.allow(var0, var5, var67, 987370355);
        Enchantment var56 = Enchantment.SOUL_SPEED;
        int var75 = efficiencyLevel(var1, var2, 1111308413);
        int var69 = Math.min(3, var75);
        int var10 = GemAutoEnchants.allow(var0, var56, var69, 987370355);
        String var18 = "speed";
        String var57 = "auto-efficiency";
        if (!GemTuning.passive(var18, var57, true, 14309468)) {
            var9 = 0;
        }

        String var21 = "speed";
        String var58 = "auto-soul-speed";
        if (!GemTuning.passive(var21, var58, true, 14309468)) {
            var10 = 0;
        }

        if (var9 > 0) {
            for (ItemStack var14 : var0.getContents()) {
                if (var14 != null && isTool(var14.getType(), 1534406547)) {
                    Enchantment var61 = Enchantment.EFFICIENCY;
                    if (var14.getEnchantmentLevel(var61) <= 0) {
                        Enchantment var62 = Enchantment.EFFICIENCY;
                        var14.addUnsafeEnchantment(var62, var9);
                    }
                }
            }
        }

        if (var10 > 0) {
            for (ItemStack var79 : var0.getContents()) {
                if (var79 != null && isBoots(var79.getType(), 737980063)) {
                    Enchantment var65 = Enchantment.SOUL_SPEED;
                    if (var79.getEnchantmentLevel(var65) <= 0) {
                        Enchantment var66 = Enchantment.SOUL_SPEED;
                        var79.addUnsafeEnchantment(var66, var10);
                    }
                }
            }
        }
    }

    private static int efficiencyLevel(int var0, int var1, int var2) {
        if (var0 == 2) {
            if (var1 > 5) {
                return 5;
            } else if (var1 == 5) {
                return 4;
            } else if (var1 == 4) {
                return 3;
            } else {
                return var1 == 3 ? 2 : 0;
            }
        } else if (var1 > 3) {
            return 2;
        } else {
            return var1 > 2 ? 1 : 0;
        }
    }

    private static boolean isTool(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE, WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE, WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE, DIAMOND_HOE, NETHERITE_HOE, WOODEN_SHOVEL, STONE_SHOVEL, IRON_SHOVEL, GOLDEN_SHOVEL, DIAMOND_SHOVEL, NETHERITE_SHOVEL-> true;
            default -> false;
        };
    }

    private static boolean isBoots(Material var0, int var1) {
        return switch (var0) {
            case LEATHER_BOOTS, IRON_BOOTS, GOLDEN_BOOTS, DIAMOND_BOOTS, NETHERITE_BOOTS, CHAINMAIL_BOOTS-> true;
            default -> false;
        };
    }
}
